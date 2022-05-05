import * as express from 'express'
import { ServerConfig } from '../../../../shared/models'
import { ServerStats } from '../../../../shared/models/server/server-stats.model'
import { InstanceConnectivityStats } from 'shared/models/instance-connectivity-stats.model'
import { retryTransactionWrapper } from '../../helpers/database-utils'
import { getConfigAndStatsAndAboutInstance } from '../../helpers/instance-requests'
import { logger } from '../../helpers/logger'
import { getFormattedObjects } from '../../helpers/utils'
import { asyncMiddleware } from '../../middlewares/async'
import { setDefaultPagination } from '../../middlewares/pagination'
import { setDefaultSort } from '../../middlewares/sort'
import {
  instanceGetValidator,
  instanceHostsValidator,
  instancesAddValidator,
  instancesListValidator
} from '../../middlewares/validators/instances'
import { paginationValidator } from '../../middlewares/validators/pagination'
import { instanceHostsSortValidator, instancesSortValidator } from '../../middlewares/validators/sort'
import { InstanceModel } from '../../models/instance'
import { HistoryModel } from '../../models/history'
import { InstanceStatsHistory } from '../../../shared/models/instance-stats-history.model'
import { GlobalStats } from '../../../shared/models/global-stats.model'
import { GlobalStatsHistory } from '../../../shared/models/global-stats-history'
import { About } from '../../../../shared/models/server'
import { historyListValidator } from '../../middlewares/validators/history'

const instancesRouter = express.Router()

instancesRouter.get('/hosts',
  instancesListValidator,
  instanceHostsValidator,
  paginationValidator,
  instanceHostsSortValidator,
  setDefaultSort,
  setDefaultPagination,
  asyncMiddleware(listInstanceHosts)
)

instancesRouter.get('/',
  instancesListValidator,
  paginationValidator,
  instancesSortValidator,
  setDefaultSort,
  setDefaultPagination,
  asyncMiddleware(listInstances)
)

instancesRouter.get('/:host/stats-history',
  instanceGetValidator,
  getInstanceStatsHistory
)

instancesRouter.get('/stats',
  asyncMiddleware(getGlobalStats)
)

instancesRouter.get('/stats-history',
  historyListValidator,
  asyncMiddleware(getGlobalStatsHistory)
)

instancesRouter.post('/',
  asyncMiddleware(instancesAddValidator),
  asyncMiddleware(createInstanceRetryWrapper)
)

// ---------------------------------------------------------------------------

export {
  instancesRouter
}

// ---------------------------------------------------------------------------

async function createInstanceRetryWrapper (req: express.Request, res: express.Response, next: express.NextFunction) {
  const host = req.body.host

  let config: ServerConfig
  let stats: ServerStats
  let about: About
  let connectivityStats: InstanceConnectivityStats

  try {
    const res = await getConfigAndStatsAndAboutInstance(host)
    config = res.config
    stats = res.stats
    about = res.about
    connectivityStats = res.connectivityStats
  } catch (err) {
    logger.warn(err)

    return res.status(409)
              .json({
                error: err.message
              })

  }

  const options = {
    arguments: [ { host, config, stats, connectivityStats, about } ],
    errorMessage: 'Cannot insert the instance with many retries.'
  }
  const instance = await retryTransactionWrapper(createInstance, options)

  return res.json({
    instance: {
      id: instance.id,
      host: instance.host
    }
  })
}

async function createInstance (options: {
  host: string,
  config: ServerConfig,
  stats: ServerStats,
  about: About,
  connectivityStats: InstanceConnectivityStats
}) {
  const { host, config, stats, about, connectivityStats } = options

  const instanceCreated = await InstanceModel.create({
    host,
    config,
    stats,
    about,
    connectivityStats
  })

  logger.info('Instance %s created.', host)

  return instanceCreated
}

async function listInstances (req: express.Request, res: express.Response) {
  const options = {
    start: req.query.start,
    count: req.query.count,
    sort: req.query.sort,
    signup: req.query.signup,
    healthy: req.query.healthy,
    nsfwPolicy: req.query.nsfwPolicy,
    search: req.query.search,
    categoriesOr: req.query.categoriesOr,
    languagesOr: req.query.languagesOr,
    liveEnabled: req.query.liveEnabled,
    minUserQuota: req.query.minUserQuota
  }

  const resultList = await InstanceModel.listForApi(options)

  return res.json(getFormattedObjects(resultList.data, resultList.total))
}

async function listInstanceHosts (req: express.Request, res: express.Response) {
  const options = {
    start: req.query.start,
    count: req.query.count,
    sort: req.query.sort,
    signup: req.query.signup,
    healthy: req.query.healthy,
    nsfwPolicy: req.query.nsfwPolicy,
    search: req.query.search,
    categoriesOr: req.query.categoriesOr,
    languagesOr: req.query.languagesOr,
    minUserQuota: req.query.minUserQuota,
    liveEnabled: req.query.liveEnabled,
    since: req.query.since
  }

  const resultList = await InstanceModel.listForHostsApi(options)

  return res.json({
    total: resultList.total,
    data: resultList.data.map(d => d.toHostFormattedJSON())
  })
}

async function getGlobalStats (req: express.Request, res: express.Response) {
  const data: GlobalStats = await InstanceModel.getStats()

  return res.json(data)
}

async function getInstanceStatsHistory (req: express.Request, res: express.Response) {
  const instance = res.locals.instance

  const rows = await HistoryModel.getInstanceHistory(instance.id)

  const result: InstanceStatsHistory = {
    data: rows.map(d => d.toFormattedJSON())
  }

  return res.json(result)
}

async function getGlobalStatsHistory (req: express.Request, res: express.Response) {
  const beforeDate = req.query.beforeDate
  const rows = await HistoryModel.getGlobalStats(beforeDate)

  const result: GlobalStatsHistory = {
    data: rows
  }

  return res.json(result)
}
