import { Order, QueryTypes } from 'sequelize'
import { AllowNull, BelongsTo, Column, CreatedAt, DataType, ForeignKey, Model, Table, UpdatedAt } from 'sequelize-typescript'
import { ServerStats } from '../../../shared/models/server/server-stats.model'
import { MAX_HISTORY_SIZE } from '../initializers/constants'
import { InstanceModel } from './instance'

@Table({
  tableName: 'history',
  indexes: [
    {
      fields: [ 'instanceId' ]
    },
    {
      fields: [ 'createdAt' ]
    }
  ]
})
export class HistoryModel extends Model {

  @AllowNull(false)
  @Column(DataType.JSONB)
  stats: ServerStats

  @CreatedAt
  createdAt: Date

  @UpdatedAt
  updatedAt: Date

  @ForeignKey(() => InstanceModel)
  @Column
  instanceId: number

  @BelongsTo(() => InstanceModel, {
    foreignKey: {
      allowNull: false
    },
    onDelete: 'cascade'
  })
  Instance: InstanceModel

  static doesTodayHistoryExist (instanceId: number) {
    const today = new Date()
    today.setHours(0, 0, 0)

    const query = 'SELECT 1 FROM "history" WHERE "instanceId" = $instanceId AND DATE("createdAt") = CURRENT_DATE LIMIT 1'
    const options = {
      type: QueryTypes.SELECT,
      bind: { instanceId },
      raw: true
    }

    return HistoryModel.sequelize.query(query, options)
                     .then((results: any[]) => {
                       return results.length === 1
                     })
  }

  static async addEntryIfNeeded (instanceId: number, stats: ServerStats) {
    // Only add 1 entry per day
    const exists = await HistoryModel.doesTodayHistoryExist(instanceId)
    if (exists) return

    return HistoryModel.create({
      stats,
      instanceId
    })
  }

  static getInstanceHistory (instanceId: number) {
    const query = {
      order: [ [ 'createdAt', 'DESC' ] ] as Order,
      limit: MAX_HISTORY_SIZE,
      where: {
        instanceId
      }
    }

    return HistoryModel.findAll(query)
  }

  static async getGlobalStats (beforeDate?: string) {
    const createdBefore = beforeDate && `'${beforeDate}'::date`|| 'now()'

    const query = 'SELECT ' +
      'DATE("history"."createdAt") as "date", ' +
      'COUNT(*) as "totalInstances", ' +
      'SUM(("history".stats->>\'totalUsers\')::integer) as "totalUsers", ' +
      'SUM(("history".stats->>\'totalLocalVideos\')::integer) as "totalVideos", ' +
      'SUM(("history".stats->>\'totalLocalVideoComments\')::integer) as "totalVideoComments", ' +
      'SUM(("history".stats->>\'totalLocalVideoViews\')::integer) as "totalVideoViews", ' +
      'SUM(("history".stats->>\'totalLocalVideoFilesSize\')::bigint) as "totalVideoFilesSize", ' +
      'SUM(("history".stats->>\'totalDailyActiveUsers\')::integer) as "totalDailyActiveUsers", ' +
      'SUM(("history".stats->>\'totalWeeklyActiveUsers\')::integer) as "totalWeeklyActiveUsers", ' +
      'SUM(("history".stats->>\'totalMonthlyActiveUsers\')::integer) as "totalMonthlyActiveUsers" ' +
      'FROM "history" ' +
      'INNER JOIN "instance" ON "history"."instanceId" = "instance"."id" ' +
      'WHERE "instance"."blacklisted" IS FALSE ' +
        `AND "history"."createdAt" < ${createdBefore} ` +
      'GROUP BY DATE("history"."createdAt") ' +
      'ORDER BY DATE("history"."createdAt") DESC ' +
      'LIMIT ' + MAX_HISTORY_SIZE

    return InstanceModel.sequelize.query(query, { type: QueryTypes.SELECT })
                        .then((results: any[]) => results.map(res => ({
                          date: res.date,
                          stats: {
                            totalInstances: res.totalInstances,
                            totalUsers: res.totalUsers,
                            totalDailyActiveUsers: res.totalDailyActiveUsers,
                            totalWeeklyActiveUsers: res.totalWeeklyActiveUsers,
                            totalMonthlyActiveUsers: res.totalMonthlyActiveUsers,
                            totalVideos: res.totalVideos,
                            totalVideoComments: res.totalVideoComments,
                            totalVideoViews: res.totalVideoViews,
                            totalVideoFilesSize: res.totalVideoFilesSize
                          }
                        })))
  }

  toFormattedJSON () {
    return {
      date: this.createdAt.toISOString().split('T')[0],

      stats: {
        totalUsers: this.stats.totalUsers,
        totalVideos: this.stats.totalVideos,
        totalLocalVideos: this.stats.totalLocalVideos,
        totalInstanceFollowers: this.stats.totalInstanceFollowers,
        totalInstanceFollowing: this.stats.totalInstanceFollowing
      }
    }
  }
}
