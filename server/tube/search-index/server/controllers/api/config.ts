import * as express from 'express'
import { ServerConfig } from '../../../shared'
import { CONFIG } from '../../initializers/constants'
import { IndexationScheduler } from '../../lib/schedulers/indexation-scheduler'

const configRouter = express.Router()

configRouter.get('/config',
  getConfig
)

// ---------------------------------------------------------------------------

export { configRouter }

// ---------------------------------------------------------------------------

async function getConfig (req: express.Request, res: express.Response) {
  return res.json({
    searchInstanceName: CONFIG.SEARCH_INSTANCE.NAME,
    searchInstanceNameImage: CONFIG.SEARCH_INSTANCE.NAME_IMAGE,
    searchInstanceSearchImage: CONFIG.SEARCH_INSTANCE.SEARCH_IMAGE,
    legalNoticesUrl: CONFIG.SEARCH_INSTANCE.LEGAL_NOTICES_URL,
    indexedHostsCount: IndexationScheduler.Instance.getIndexedHosts().length,
    indexedInstancesUrl: CONFIG.INSTANCES_INDEX.PUBLIC_URL
  } as ServerConfig)
}
