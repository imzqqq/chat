import { FindAndCountOptions, literal, Op, QueryTypes, WhereOptions } from 'sequelize'
import { AllowNull, Column, CreatedAt, DataType, Default, Is, IsInt, Max, Model, Table, UpdatedAt } from 'sequelize-typescript'
import { InstanceConnectivityStats } from 'shared/models/instance-connectivity-stats.model'
import { InstanceFilters } from 'shared/models/instance-filters.model'
import { ServerConfig } from '../../../shared/models'
import { About } from '../../../shared/models/server/about.model'
import { ServerStats } from '../../../shared/models/server/server-stats.model'
import { InstanceHost } from '../../shared/models/instance-host.model'
import { Instance } from '../../shared/models/instance.model'
import { isHostValid } from '../helpers/custom-validators/instances'
import { logger } from '../helpers/logger'
import { INSTANCE_SCORE } from '../initializers/constants'
import { getSort, throwIfNotValid } from './utils'

@Table({
  tableName: 'instance',
  indexes: [
    {
      fields: [ 'host' ],
      unique: true
    }
  ]
})
export class InstanceModel extends Model {

  @AllowNull(false)
  @Is('Host', value => throwIfNotValid(value, isHostValid, 'valid host'))
  @Column
  host: string

  @AllowNull(false)
  @Default(INSTANCE_SCORE.MAX)
  @IsInt
  @Max(INSTANCE_SCORE.MAX)
  @Column
  score: number

  @AllowNull(false)
  @Column(DataType.JSONB)
  stats: ServerStats

  @AllowNull(false)
  @Column(DataType.JSONB)
  connectivityStats: InstanceConnectivityStats

  @AllowNull(false)
  @Column(DataType.JSONB)
  config: ServerConfig

  @AllowNull(false)
  @Column(DataType.JSONB)
  about: About

  @AllowNull(false)
  @Default([])
  @Column(DataType.ARRAY(DataType.INTEGER))
  categories: number[]

  @AllowNull(false)
  @Default([])
  @Column(DataType.ARRAY(DataType.TEXT))
  languages: string[]

  @AllowNull(false)
  @Default(false)
  @Column
  blacklisted: boolean

  @CreatedAt
  createdAt: Date

  @UpdatedAt
  updatedAt: Date

  static loadByHost (host: string) {
    const query = {
      where: {
        host
      }
    }

    return InstanceModel.findOne(query)
  }

  static listForApi (options: InstanceFilters) {
    const query: FindAndCountOptions = {
      offset: options.start,
      limit: options.count,
      order: InstanceModel.getSort(options.sort),
      where: { [Op.and]: InstanceModel.buildWhereFilters(options) }
    }

    return InstanceModel.findAndCountAll(query)
      .then(({ rows, count }) => {
        return {
          data: rows,
          total: count
        }
      })
  }

  static listForHostsApi (options: InstanceFilters & { since?: string }) {
    const whereAnd = InstanceModel.buildWhereFilters(options)
    if (options.since !== undefined) {
      whereAnd.push({
        createdAt: {
          [Op.gte]: options.since
        }
      })
    }

    const query: FindAndCountOptions = {
      attributes: [ 'host', 'createdAt' ],
      offset: options.start,
      limit: options.count,
      order: InstanceModel.getSort(options.sort),
      where: { [Op.and]: whereAnd }
    }

    return InstanceModel.findAndCountAll(query)
                        .then(({ rows, count }) => {
                          return {
                            data: rows,
                            total: count
                          }
                        })
  }

  static listHostsWithId () {
    const query = {
      attributes: [ 'id', 'host' ],
      where: {
        blacklisted: false
      }
    }

    return InstanceModel.findAll(query)
  }

  static updateConfigAndStatsAndAbout (id: number, config: any, stats: any, about: About, connectivityStats: any) {
    const options = {
      where: { id }
    }

    const categories = about && about.instance && Array.isArray(about.instance.categories)
      ? about.instance.categories
      : []

    const languages = about && about.instance && Array.isArray(about.instance.languages)
      ? about.instance.languages
      : []

    return InstanceModel.update({
      config,
      stats,
      about,
      categories,
      languages,
      connectivityStats
    }, options)
  }

  static async removeBadInstances () {
    const instances = await InstanceModel.listBadInstances()

    const instancesRemovePromises = instances.map(instance => instance.destroy())
    await Promise.all(instancesRemovePromises)

    const numberOfInstancesRemoved = instances.length

    if (numberOfInstancesRemoved) logger.info('Removed bad %d instances.', numberOfInstancesRemoved)
  }

  static async updateInstancesScoreAndRemoveBadOnes (goodInstances: number[], badInstances: number[]) {
    if (goodInstances.length === 0 && badInstances.length === 0) return

    logger.info('Updating %d good instances and %d bad instances scores.', goodInstances.length, badInstances.length)

    if (goodInstances.length !== 0) {
      await InstanceModel.incrementScores(goodInstances, INSTANCE_SCORE.BONUS)
        .catch(err => logger.error({ err }, 'Cannot increment scores of good instances.'))
    }

    if (badInstances.length !== 0) {
      await InstanceModel.incrementScores(badInstances, INSTANCE_SCORE.PENALTY)
        .then(() => InstanceModel.removeBadInstances())
        .catch(err => logger.error({ err }, 'Cannot decrement scores of bad instances.'))
    }
  }

  static async getStats () {
    const queryStats = 'SELECT ' +
      'COUNT(*) as "totalInstances", ' +
      'SUM((stats->>\'totalUsers\')::integer) as "totalUsers", ' +
      'SUM((stats->>\'totalLocalVideos\')::integer) as "totalVideos", ' +
      'SUM((stats->>\'totalLocalVideoComments\')::integer) as "totalVideoComments", ' +
      'SUM((stats->>\'totalLocalVideoViews\')::integer) as "totalVideoViews", ' +
      'SUM((stats->>\'totalLocalVideoFilesSize\')::bigint) as "totalVideoFilesSize", ' +
      'SUM((stats->>\'totalDailyActiveUsers\')::integer) as "totalDailyActiveUsers", ' +
      'SUM((stats->>\'totalWeeklyActiveUsers\')::integer) as "totalWeeklyActiveUsers", ' +
      'SUM((stats->>\'totalMonthlyActiveUsers\')::integer) as "totalMonthlyActiveUsers" ' +
      'FROM "instance" ' +
      'WHERE blacklisted IS FALSE'

    const queryVersions = 'SELECT ' +
      '(config->>\'serverVersion\') as "serverVersion", ' +
      'COUNT(*) as "total" ' +
      'FROM "instance" ' +
      'WHERE blacklisted IS FALSE ' +
      'GROUP BY (config->>\'serverVersion\')'

    const queryPlugins = 'WITH "pluginConfig" AS ' +
      '(SELECT jsonb_array_elements(config->\'plugin\'->\'registered\') AS "json" FROM instance) ' +
      'SELECT COUNT(*) AS "total", "json"->\'name\' AS "name" ' +
      'FROM "pluginConfig" ' +
      'GROUP BY "name"'

    const queryThemes = 'WITH "themeConfig" AS ' +
      '(SELECT jsonb_array_elements(config->\'theme\'->\'registered\') AS "json" FROM instance) ' +
      'SELECT COUNT(*) AS "total", "json"->\'name\' AS "name" ' +
      'FROM "themeConfig" ' +
      'GROUP BY "name"'

    const queryCountries = 'SELECT COUNT(*) as "total", "connectivityStats"->\'country\' AS "country" ' +
      'FROM "instance" ' +
      'WHERE "connectivityStats"->\'country\' IS NOT NULL ' +
      'GROUP BY "country"'

    const promises = [ queryStats, queryVersions, queryPlugins, queryThemes, queryCountries ]
      .map(query => InstanceModel.sequelize.query(query, { type: QueryTypes.SELECT }))

    const [ resStats, resVersions, resPlugins, resThemes, resCountries ] = await Promise.all(promises)

    const firstResStats = resStats[0] as any

    return {
      totalInstances: firstResStats.totalInstances,
      totalUsers: firstResStats.totalUsers,
      totalDailyActiveUsers: firstResStats.totalDailyActiveUsers,
      totalWeeklyActiveUsers: firstResStats.totalWeeklyActiveUsers,
      totalMonthlyActiveUsers: firstResStats.totalMonthlyActiveUsers,
      totalVideos: firstResStats.totalVideos,
      totalVideoComments: firstResStats.totalVideoComments,
      totalVideoViews: firstResStats.totalVideoViews,
      totalVideoFilesSize: firstResStats.totalVideoFilesSize,

      instanceVersions: resVersions.map((v: any) => ({
        serverVersion: v.serverVersion,
        total: v.total
      })),

      registeredPlugins: resPlugins.map((p: any) => ({
        name: p.name,
        total: p.total
      })),

      registeredThemes: resThemes.map((t: any) => ({
        name: t.name,
        total: t.total
      })),

      instanceCountries: resCountries.map((c: any) => ({
        countryCode: c.country,
        total: c.total
      }))
    }
  }

  private static buildWhereFilters (options: InstanceFilters) {
    const whereAnd: WhereOptions[] = [
      {
        blacklisted: false
      }
    ]

    if (options.healthy !== undefined) {
      const symbol = options.healthy === 'true' ? Op.gte : Op.lt

      whereAnd.push({
        score: {
          [symbol]: INSTANCE_SCORE.HEALTHY_AT
        }
      })
    }

    if (options.signup !== undefined) {
      whereAnd.push({
        config: {
          signup: {
            allowed: options.signup === 'true'
          }
        }
      })
    }

    if (options.nsfwPolicy !== undefined) {
      whereAnd.push({
        config: {
          instance: {
            defaultNSFWPolicy: {
              [ Op.any ]: options.nsfwPolicy
            }
          }
        }
      })
    }

    if (options.liveEnabled !== undefined) {
      whereAnd.push({
        config: {
          live: {
            enabled: options.liveEnabled === 'true'
          }
        }
      })
    }

    if (options.search) {
      whereAnd.push({
        host: {
          [ Op.iLike ]: `%${options.search}%`
        }
      })
    }

    if (Array.isArray(options.languagesOr)) {
      whereAnd.push({
        languages: {
          [ Op.overlap ]: options.languagesOr
        }
      })
    }

    if (Array.isArray(options.categoriesOr)) {
      whereAnd.push({
        categories: {
          [ Op.overlap ]: options.categoriesOr
        }
      })
    }

    if (options.minUserQuota) {
      whereAnd.push({
        [ Op.or ]: [
          {
            config: {
              user: {
                videoQuota: {
                  [ Op.gte ]: options.minUserQuota
                }
              }
            }
          },
          {
            config: {
              user: {
                videoQuota: {
                  [ Op.eq ]: -1
                }
              }
            }
          }
        ]
      })
    }

    return whereAnd
  }

  private static listBadInstances () {
    const query = {
      where: {
        score: {
          [Op.lte]: 0
        }
      },
      logging: false
    }

    return InstanceModel.findAll(query)
  }

  private static incrementScores (instances: number[], value: number) {
    const instancesString = instances.map(id => id.toString()).join(',')

    const query = `UPDATE "instance" SET "score" = LEAST("score" + ${value}, ${INSTANCE_SCORE.MAX}) ` +
      'WHERE id IN (' + instancesString + ')'

    const options = {
      type: QueryTypes.BULKUPDATE
    }

    return InstanceModel.sequelize.query(query, options)
  }

  private static getSort (sort: string) {
    const mappingColumns = {
      totalUsers: literal(`stats->'totalUsers'`),
      totalVideos: literal(`stats->'totalVideos'`),
      totalLocalVideos: literal(`stats->'totalLocalVideos'`),
      totalInstanceFollowers: literal(`stats->'totalInstanceFollowers'`),
      totalInstanceFollowing: literal(`stats->'totalInstanceFollowing'`),
      signupAllowed: literal(`config->'signup'->'allowed'`),
      liveEnabled: literal(`COALESCE(config->'live'->'enabled', 'false')`),
      name: literal(`config->'instance'->'name'`),
      version: literal(`config->'serverVersion'`),
      health: 'score',
      country: literal(`"connectivityStats"->'country'`)
    }

    return getSort(sort, [ 'id', 'ASC' ], mappingColumns)
  }

  toFormattedJSON (): Instance {
    let userVideoQuota: number
    if (this.config.user) userVideoQuota = this.config.user.videoQuota

    return {
      id: this.id,
      host: this.host,

      // config
      name: this.config.instance.name,
      shortDescription: this.config.instance.shortDescription,
      version: this.config.serverVersion,
      signupAllowed: this.config.signup.allowed,
      userVideoQuota,

      liveEnabled: this.config.live
        ? this.config.live.enabled
        : false,

      categories: this.categories,
      languages: this.languages,

      autoBlacklistUserVideosEnabled: this.config.autoBlacklist
        ? this.config.autoBlacklist.videos.ofUsers.enabled
        : false,

      defaultNSFWPolicy: this.config.instance.defaultNSFWPolicy,
      isNSFW: this.config.instance.isNSFW,

      // stats
      totalUsers: this.stats.totalUsers,
      totalVideos: this.stats.totalVideos,
      totalLocalVideos: this.stats.totalLocalVideos,
      totalInstanceFollowers: this.stats.totalInstanceFollowers,
      totalInstanceFollowing: this.stats.totalInstanceFollowing,

      // connectivity
      supportsIPv6: this.connectivityStats ? this.connectivityStats.supportsIPv6 : undefined,
      country: this.connectivityStats ? this.connectivityStats.country : undefined,

      // computed stats
      health: Math.round((this.score / INSTANCE_SCORE.MAX) * 100),

      createdAt: this.createdAt.toISOString()
    }
  }

  toHostFormattedJSON (): InstanceHost {
    return {
      host: this.host
    }
  }
}
