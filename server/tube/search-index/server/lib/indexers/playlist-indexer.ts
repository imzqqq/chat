import { CONFIG } from '../../initializers/constants'
import { DBPlaylist, IndexablePlaylist } from '../../types/playlist.model'
import { buildPlaylistsMapping, formatPlaylistForDB } from '../elastic-search/elastic-search-playlists'
import { AbstractIndexer } from './shared'

export class PlaylistIndexer extends AbstractIndexer <IndexablePlaylist, DBPlaylist> {

  constructor () {
    super(CONFIG.ELASTIC_SEARCH.INDEXES.PLAYLISTS, formatPlaylistForDB)
  }

  async indexSpecificElement (host: string, uuid: string) {
    // We don't need to index a specific element yet, since we have all playlist information in the list endpoint
    throw new Error('Not implemented')
  }

  buildMapping () {
    return buildPlaylistsMapping()
  }
}
