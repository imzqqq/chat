import * as express from 'express'
import { Searcher } from '../../lib/controllers/searcher'
import { formatPlaylistForAPI, queryPlaylists } from '../../lib/elastic-search/elastic-search-playlists'
import { asyncMiddleware } from '../../middlewares/async'
import { setDefaultPagination } from '../../middlewares/pagination'
import { setDefaultSearchSort } from '../../middlewares/sort'
import { methodsValidator } from '../../middlewares/validators/method'
import { paginationValidator } from '../../middlewares/validators/pagination'
import { commonFiltersValidators, videoPlaylistsSearchValidator } from '../../middlewares/validators/search'
import { playlistsSearchSortValidator } from '../../middlewares/validators/sort'
import { PlaylistsSearchQuery } from '../../types/search-query/playlist-search.model'

const searchPlaylistsRouter = express.Router()

searchPlaylistsRouter.all('/search/video-playlists',
  methodsValidator([ 'POST', 'GET' ]),
  paginationValidator,
  setDefaultPagination,
  playlistsSearchSortValidator,
  setDefaultSearchSort,
  commonFiltersValidators,
  videoPlaylistsSearchValidator,
  asyncMiddleware(searchPlaylists)
)

// ---------------------------------------------------------------------------

export { searchPlaylistsRouter }

// ---------------------------------------------------------------------------

async function searchPlaylists (req: express.Request, res: express.Response) {
  const query = Object.assign(req.query || {}, req.body || {}) as PlaylistsSearchQuery

  const searcher = new Searcher(queryPlaylists, formatPlaylistForAPI)
  const result = await searcher.getResult(query)

  return res.json(result)
}
