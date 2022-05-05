import * as express from 'express'
import { badRequest } from '../../helpers/utils'
import { configRouter } from './config'
import { searchChannelsRouter } from './search-channels'
import { searchPlaylistsRouter } from './search-playlists'
import { searchVideosRouter } from './search-videos'

const apiRouter = express.Router()

apiRouter.use('/', configRouter)
apiRouter.use('/', searchVideosRouter)
apiRouter.use('/', searchChannelsRouter)
apiRouter.use('/', searchPlaylistsRouter)
apiRouter.use('/ping', pong)
apiRouter.use('/*', badRequest)

// ---------------------------------------------------------------------------

export { apiRouter }

// ---------------------------------------------------------------------------

function pong (req: express.Request, res: express.Response) {
  return res.send('pong').status(200).end()
}
