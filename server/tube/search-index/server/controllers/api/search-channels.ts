import * as express from 'express'
import { Searcher } from '../../lib/controllers/searcher'
import { formatChannelForAPI, queryChannels } from '../../lib/elastic-search/elastic-search-channels'
import { asyncMiddleware } from '../../middlewares/async'
import { setDefaultPagination } from '../../middlewares/pagination'
import { setDefaultSearchSort } from '../../middlewares/sort'
import { methodsValidator } from '../../middlewares/validators/method'
import { paginationValidator } from '../../middlewares/validators/pagination'
import { commonFiltersValidators, videoChannelsSearchValidator } from '../../middlewares/validators/search'
import { channelsSearchSortValidator } from '../../middlewares/validators/sort'
import { ChannelsSearchQuery } from '../../types/search-query/channel-search.model'

const searchChannelsRouter = express.Router()

searchChannelsRouter.all('/search/video-channels',
  methodsValidator([ 'POST', 'GET' ]),
  paginationValidator,
  setDefaultPagination,
  channelsSearchSortValidator,
  setDefaultSearchSort,
  commonFiltersValidators,
  videoChannelsSearchValidator,
  asyncMiddleware(searchChannels)
)

// ---------------------------------------------------------------------------

export { searchChannelsRouter }

// ---------------------------------------------------------------------------

async function searchChannels (req: express.Request, res: express.Response) {
  const query = Object.assign(req.query || {}, req.body || {}) as ChannelsSearchQuery

  if (query.handles && query.fromHost) {
    query.handles = query.handles.map(h => {
      if (h.includes('@')) return h

      return h + '@' + query.fromHost
    })
  }

  const searcher = new Searcher(queryChannels, formatChannelForAPI)
  const result = await searcher.getResult(query)

  return res.json(result)
}
