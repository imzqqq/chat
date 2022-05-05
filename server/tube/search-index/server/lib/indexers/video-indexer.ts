import { logger } from '../../helpers/logger'
import { CONFIG } from '../../initializers/constants'
import { DBVideo, IndexableVideo } from '../../types/video.model'
import { buildVideosMapping, formatVideoForDB } from '../elastic-search/elastic-search-videos'
import { getVideo } from '../requests/tube-instance'
import { AbstractIndexer } from './shared'

export class VideoIndexer extends AbstractIndexer <IndexableVideo, DBVideo> {

  constructor () {
    super(CONFIG.ELASTIC_SEARCH.INDEXES.VIDEOS, formatVideoForDB)
  }

  async indexSpecificElement (host: string, uuid: string) {
    const video = await getVideo(host, uuid)

    logger.info('Indexing specific video %s of %s.', uuid, host)

    return this.indexElements([ video ], true)
  }

  buildMapping () {
    return buildVideosMapping()
  }
}
