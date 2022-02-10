import { elasticSearch } from '../../helpers/elastic-search'
import { logger } from '../../helpers/logger'
import { buildUrl } from '../../helpers/utils'
import { CONFIG, ELASTIC_SEARCH_QUERY } from '../../initializers/constants'
import { DBPlaylist, EnhancedPlaylist, IndexablePlaylist } from '../../types/playlist.model'
import { PlaylistsSearchQuery } from '../../types/search-query/playlist-search.model'
import { buildSort, extractQueryResult } from './elastic-search-queries'
import { addUUIDFilters, buildMultiMatchBool } from './shared'
import { buildChannelOrAccountSummaryMapping, formatActorForDB, formatActorSummaryForAPI } from './shared/elastic-search-actor'

async function queryPlaylists (search: PlaylistsSearchQuery) {
  const bool: any = {}
  const mustNot: any[] = []
  const filter: any[] = []

  if (search.search) {
    Object.assign(bool, buildMultiMatchBool(search.search, ELASTIC_SEARCH_QUERY.PLAYLISTS_MULTI_MATCH_FIELDS))
  }

  if (search.blockedAccounts) {
    mustNot.push({
      terms: {
        'ownerAccount.handle': search.blockedAccounts
      }
    })
  }

  if (search.blockedHosts) {
    mustNot.push({
      terms: {
        host: search.blockedHosts
      }
    })
  }

  if (search.host) {
    filter.push({
      term: {
        'ownerAccount.host': search.host
      }
    })
  }

  if (search.uuids) {
    addUUIDFilters(filter, search.uuids)
  }

  if (filter.length !== 0) {
    Object.assign(bool, { filter })
  }

  if (mustNot.length !== 0) {
    Object.assign(bool, { must_not: mustNot })
  }

  const body = {
    from: search.start,
    size: search.count,
    sort: buildSort(search.sort),
    query: { bool }
  }

  logger.debug({ body }, 'Will query Elastic Search for playlists.')

  const res = await elasticSearch.search({
    index: CONFIG.ELASTIC_SEARCH.INDEXES.PLAYLISTS,
    body
  })

  return extractQueryResult(res)
}

function formatPlaylistForAPI (p: DBPlaylist, fromHost?: string): EnhancedPlaylist {
  return {
    id: p.id,
    uuid: p.uuid,
    shortUUID: p.shortUUID,

    score: p.score,

    isLocal: fromHost === p.host,

    url: p.url,

    displayName: p.displayName,
    description: p.description,

    privacy: {
      id: p.privacy.id,
      label: p.privacy.label
    },

    videosLength: p.videosLength,

    type: {
      id: p.type.id,
      label: p.type.label
    },

    thumbnailPath: p.thumbnailPath,
    thumbnailUrl: buildUrl(p.host, p.thumbnailPath),

    embedPath: p.embedPath,
    embedUrl: buildUrl(p.host, p.embedPath),

    createdAt: p.createdAt,
    updatedAt: p.updatedAt,

    ownerAccount: formatActorSummaryForAPI(p.ownerAccount),
    videoChannel: formatActorSummaryForAPI(p.videoChannel)
  }
}

function formatPlaylistForDB (p: IndexablePlaylist): DBPlaylist {
  return {
    id: p.id,
    uuid: p.uuid,
    shortUUID: p.shortUUID,

    indexedAt: new Date(),
    createdAt: p.createdAt,
    updatedAt: p.updatedAt,

    host: p.host,
    url: p.url,

    displayName: p.displayName,
    description: p.description,

    thumbnailPath: p.thumbnailPath,
    embedPath: p.embedPath,

    type: {
      id: p.type.id,
      label: p.type.label
    },
    privacy: {
      id: p.privacy.id,
      label: p.privacy.label
    },

    videosLength: p.videosLength,

    ownerAccount: formatActorForDB(p.ownerAccount),
    videoChannel: formatActorForDB(p.videoChannel)
  }
}

function buildPlaylistsMapping () {
  return {
    id: {
      type: 'long'
    },

    uuid: {
      type: 'keyword'
    },
    shortUUID: {
      type: 'keyword'
    },
    createdAt: {
      type: 'date',
      format: 'date_optional_time'
    },
    updatedAt: {
      type: 'date',
      format: 'date_optional_time'
    },
    indexedAt: {
      type: 'date',
      format: 'date_optional_time'
    },

    privacy: {
      properties: {
        id: {
          type: 'keyword'
        },
        label: {
          type: 'text'
        }
      }
    },

    displayName: {
      type: 'text'
    },

    description: {
      type: 'text'
    },

    thumbnailPath: {
      type: 'keyword'
    },
    embedPath: {
      type: 'keyword'
    },

    url: {
      type: 'keyword'
    },

    host: {
      type: 'keyword'
    },

    videosLength: {
      type: 'long'
    },

    ownerAccount: {
      properties: buildChannelOrAccountSummaryMapping()
    },

    videoChannel: {
      properties: buildChannelOrAccountSummaryMapping()
    }
  }
}

export {
  formatPlaylistForAPI,
  buildPlaylistsMapping,
  formatPlaylistForDB,
  queryPlaylists
}
