import * as express from 'express'
import { badRequest } from '../../helpers/utils'
import { instancesRouter } from './instances'
import { configRouter } from './config'

const apiRouter = express.Router()

apiRouter.use('/', configRouter)
apiRouter.use('/instances', instancesRouter)
apiRouter.use('/*', badRequest)

// ---------------------------------------------------------------------------

export { apiRouter }
