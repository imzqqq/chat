import { elasticSearch } from '../../helpers/elastic-search'
import { logger } from '../../helpers/logger'
import { CONFIG, ELASTIC_SEARCH_QUERY } from '../../initializers/constants'
import { DBChannel, EnhancedVideoChannel, IndexableChannel } from '../../types/channel.model'
import { ChannelsSearchQuery } from '../../types/search-query/channel-search.model'
import { buildSort, extractQueryResult } from './elastic-search-queries'
import { buildChannelOrAccountCommonMapping, buildMultiMatchBool } from './shared'
import { formatAvatarForAPI, formatAvatarForDB } from './shared/elastic-search-avatar'

async function queryChannels (search: ChannelsSearchQuery) {
  const bool: any = {}
  const mustNot: any[] = []
  const filter: any[] = []

  if (search.search) {
    Object.assign(bool, buildMultiMatchBool(search.search, ELASTIC_SEARCH_QUERY.CHANNELS_MULTI_MATCH_FIELDS))
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
        host: search.host
      }
    })
  }

  if (search.handles) {
    filter.push({
      terms: {
        handle: search.handles
      }
    })
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

  logger.debug({ body }, 'Will query Elastic Search for channels.')

  const res = await elasticSearch.search({
    index: CONFIG.ELASTIC_SEARCH.INDEXES.CHANNELS,
    body
  })

  return extractQueryResult(res)
}

function formatChannelForAPI (c: DBChannel, fromHost?: string): EnhancedVideoChannel {
  return {
    id: c.id,

    score: c.score,

    url: c.url,
    name: c.name,
    host: c.host,
    followingCount: c.followingCount,
    followersCount: c.followersCount,
    createdAt: c.createdAt,
    updatedAt: c.updatedAt,
    avatar: formatAvatarForAPI(c),

    displayName: c.displayName,
    description: c.description,
    support: c.support,
    isLocal: fromHost === c.host,

    ownerAccount: {
      id: c.ownerAccount.id,
      url: c.ownerAccount.url,

      displayName: c.ownerAccount.displayName,
      description: c.ownerAccount.description,
      name: c.ownerAccount.name,
      host: c.ownerAccount.host,
      followingCount: c.ownerAccount.followingCount,
      followersCount: c.ownerAccount.followersCount,
      createdAt: c.ownerAccount.createdAt,
      updatedAt: c.ownerAccount.updatedAt,

      avatar: formatAvatarForAPI(c.ownerAccount)
    }
  }
}

function formatChannelForDB (c: IndexableChannel): DBChannel {
  return {
    id: c.id,

    name: c.name,
    host: c.host,
    url: c.url,

    avatar: formatAvatarForDB(c),

    displayName: c.displayName,

    indexedAt: new Date(),

    followingCount: c.followingCount,
    followersCount: c.followersCount,
    createdAt: c.createdAt,
    updatedAt: c.updatedAt,

    description: c.description,
    support: c.support,
    videosCount: c.videosCount,

    handle: `${c.name}@${c.host}`,

    ownerAccount: {
      id: c.ownerAccount.id,
      url: c.ownerAccount.url,

      displayName: c.ownerAccount.displayName,
      description: c.ownerAccount.description,
      name: c.ownerAccount.name,
      host: c.ownerAccount.host,
      followingCount: c.ownerAccount.followingCount,
      followersCount: c.ownerAccount.followersCount,
      createdAt: c.ownerAccount.createdAt,
      updatedAt: c.ownerAccount.updatedAt,

      handle: `${c.ownerAccount.name}@${c.ownerAccount.host}`,

      avatar: formatAvatarForDB(c.ownerAccount)
    }
  }
}

function buildChannelsMapping () {
  const base = buildChannelOrAccountCommonMapping()

  Object.assign(base, {
    videosCount: {
      type: 'long'
    },

    support: {
      type: 'keyword'
    },

    ownerAccount: {
      properties: buildChannelOrAccountCommonMapping()
    }
  })

  return base
}

export {
  buildChannelsMapping,
  formatChannelForDB,
  formatChannelForAPI,
  queryChannels
}
