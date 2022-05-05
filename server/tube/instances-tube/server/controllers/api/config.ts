import * as express from 'express'
import { CONFIG } from '../../initializers/constants'
import { ServerConfig } from 'shared/models/server-config.model'

const configRouter = express.Router()

configRouter.get('/config',
  getConfig
)

// ---------------------------------------------------------------------------

export { configRouter }

// ---------------------------------------------------------------------------

async function getConfig (req: express.Request, res: express.Response) {
  return res.json({
    instanceClientWarning: CONFIG.INSTANCE.CLIENT_WARNING
  } as ServerConfig)
}
