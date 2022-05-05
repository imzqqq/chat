import * as express from 'express'
import { Searcher } from '../../lib/controllers/searcher'
import { formatVideoForAPI, queryVideos } from '../../lib/elastic-search/elastic-search-videos'
import { asyncMiddleware } from '../../middlewares/async'
import { setDefaultPagination } from '../../middlewares/pagination'
import { setDefaultSearchSort } from '../../middlewares/sort'
import { methodsValidator } from '../../middlewares/validators/method'
import { paginationValidator } from '../../middlewares/validators/pagination'
import { commonFiltersValidators, commonVideosFiltersValidator, videosSearchValidator } from '../../middlewares/validators/search'
import { videosSearchSortValidator } from '../../middlewares/validators/sort'
import { VideosSearchQuery } from '../../types/search-query/video-search.model'

const searchVideosRouter = express.Router()

searchVideosRouter.all('/search/videos',
  methodsValidator([ 'POST', 'GET' ]),
  paginationValidator,
  setDefaultPagination,
  videosSearchSortValidator,
  setDefaultSearchSort,
  commonFiltersValidators,
  commonVideosFiltersValidator,
  videosSearchValidator,
  asyncMiddleware(searchVideos)
)

// ---------------------------------------------------------------------------

export { searchVideosRouter }

// ---------------------------------------------------------------------------

async function searchVideos (req: express.Request, res: express.Response) {
  const query = Object.assign(req.query || {}, req.body || {}) as VideosSearchQuery
  const searcher = new Searcher(queryVideos, formatVideoForAPI)
  const result = await searcher.getResult(query)

  return res.json(result)
}
