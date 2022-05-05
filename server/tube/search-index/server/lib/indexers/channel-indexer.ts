import { logger } from '../../helpers/logger'
import { CONFIG } from '../../initializers/constants'
import { DBChannel, IndexableChannel } from '../../types/channel.model'
import { buildChannelsMapping, formatChannelForDB } from '../elastic-search/elastic-search-channels'
import { getChannel } from '../requests/tube-instance'
import { AbstractIndexer } from './shared'

export class ChannelIndexer extends AbstractIndexer <IndexableChannel, DBChannel> {

  constructor () {
    super(CONFIG.ELASTIC_SEARCH.INDEXES.CHANNELS, formatChannelForDB)

    this.indexQueue.drain(async () => {
      logger.info('Refresh channels index.')

      await this.refreshIndex()
    })
  }

  async indexSpecificElement (host: string, name: string) {
    const channel = await getChannel(host, name)

    logger.info('Indexing specific channel %s@%s.', name, host)

    return this.indexElements([ channel ], true)
  }

  buildMapping () {
    return buildChannelsMapping()
  }
}
