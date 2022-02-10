import * as config from 'config'
import { isTestInstance } from '../helpers/core-utils'

const API_VERSION = 'v1'

const CONFIG = {
  LISTEN: {
    PORT: config.get<number>('listen.port')
  },
  WEBSERVER: {
    SCHEME: config.get<boolean>('webserver.https') === true ? 'https' : 'http',
    HOSTNAME: config.get<string>('webserver.hostname'),
    PORT: config.get<number>('webserver.port')
  },
  ELASTIC_SEARCH: {
    HTTP: config.get<string>('elastic-search.http'),
    AUTH: {
      USERNAME: config.get<string>('elastic-search.auth.username'),
      PASSWORD: config.get<string>('elastic-search.auth.password')
    },
    SSL: {
      CA: config.get<string>('elastic-search.ssl.ca')
    },
    HOSTNAME: config.get<string>('elastic-search.hostname'),
    PORT: config.get<number>('elastic-search.port'),
    INDEXES: {
      VIDEOS: config.get<string>('elastic-search.indexes.videos'),
      CHANNELS: config.get<string>('elastic-search.indexes.channels'),
      PLAYLISTS: config.get<string>('elastic-search.indexes.playlists')
    }
  },
  LOG: {
    LEVEL: config.get<string>('log.level')
  },
  SEARCH_INSTANCE: {
    NAME: config.get<string>('search-instance.name'),
    NAME_IMAGE: config.get<string>('search-instance.name_image'),
    SEARCH_IMAGE: config.get<string>('search-instance.search_image'),
    DESCRIPTION: config.get<string>('search-instance.description'),
    LEGAL_NOTICES_URL: config.get<string>('search-instance.legal_notices_url'),
    THEME: config.get<string>('search-instance.theme')
  },
  VIDEOS_SEARCH: {
    BOOST_LANGUAGES: {
      ENABLED: config.get<boolean>('videos-search.boost-languages.enabled')
    },
    SEARCH_FIELDS: {
      NAME: {
        FIELD_NAME: 'name',
        BOOST: config.get<number>('videos-search.search-fields.name.boost')
      },
      DESCRIPTION: {
        FIELD_NAME: 'description',
        BOOST: config.get<number>('videos-search.search-fields.description.boost')
      },
      TAGS: {
        FIELD_NAME: 'tags',
        BOOST: config.get<number>('videos-search.search-fields.tags.boost')
      },
      ACCOUNT_DISPLAY_NAME: {
        FIELD_NAME: 'account.displayName',
        BOOST: config.get<number>('videos-search.search-fields.account-display-name.boost')
      },
      CHANNEL_DISPLAY_NAME: {
        FIELD_NAME: 'channel.displayName',
        BOOST: config.get<number>('videos-search.search-fields.channel-display-name.boost')
      }
    }
  },
  CHANNELS_SEARCH: {
    SEARCH_FIELDS: {
      NAME: {
        FIELD_NAME: 'name',
        BOOST: config.get<number>('channels-search.search-fields.name.boost')
      },
      DESCRIPTION: {
        FIELD_NAME: 'description',
        BOOST: config.get<number>('channels-search.search-fields.description.boost')
      },
      DISPLAY_NAME: {
        FIELD_NAME: 'displayName',
        BOOST: config.get<number>('channels-search.search-fields.display-name.boost')
      },
      ACCOUNT_DISPLAY_NAME: {
        FIELD_NAME: 'ownerAccount.displayName',
        BOOST: config.get<number>('channels-search.search-fields.account-display-name.boost')
      }
    }
  },
  PLAYLISTS_SEARCH: {
    SEARCH_FIELDS: {
      DISPLAY_NAME: {
        FIELD_NAME: 'displayName',
        BOOST: config.get<number>('playlists-search.search-fields.display-name.boost')
      },
      DESCRIPTION: {
        FIELD_NAME: 'description',
        BOOST: config.get<number>('playlists-search.search-fields.description.boost')
      }
    }
  },
  INSTANCES_INDEX: {
    URL: config.get<string>('instances-index.url'),
    PUBLIC_URL: config.get<string>('instances-index.public_url'),
    WHITELIST: {
      ENABLED: config.get<boolean>('instances-index.whitelist.enabled'),
      HOSTS: config.get<string[]>('instances-index.whitelist.hosts')
    }
  },
  API: {
    BLACKLIST: {
      ENABLED: config.get<boolean>('api.blacklist.enabled'),
      HOSTS: config.get<string[]>('api.blacklist.hosts')
    }
  }
}

const SORTABLE_COLUMNS = {
  VIDEOS_SEARCH: [ 'name', 'duration', 'createdAt', 'publishedAt', 'originallyPublishedAt', 'views', 'likes', 'match' ],
  CHANNELS_SEARCH: [ 'match', 'displayName', 'createdAt' ],
  PLAYLISTS_SEARCH: [ 'match', 'displayName', 'createdAt' ]
}

const PAGINATION_START = {
  MAX: 9000
}

const PAGINATION_COUNT = {
  DEFAULT: 20,
  MAX: 500
}

const SCHEDULER_INTERVALS_MS = {
  indexation: 60000 * 60 * 24 // 24 hours
}

const INDEXER_COUNT = 10
const INDEXER_LIMIT = 500000

const INDEXER_HOST_CONCURRENCY = 3
const INDEXER_QUEUE_CONCURRENCY = 3

const REQUESTS = {
  MAX_RETRIES: 10,
  WAIT: 10000 // 10 seconds
}

const ELASTIC_SEARCH_QUERY = {
  FUZZINESS: 'AUTO:4,7',
  OPERATOR: 'OR',
  MINIMUM_SHOULD_MATCH: '3<75%',
  BOOST_LANGUAGE_VALUE: 1,
  MALUS_LANGUAGE_VALUE: 0.5,
  VIDEOS_MULTI_MATCH_FIELDS: buildMultiMatchFields(CONFIG.VIDEOS_SEARCH.SEARCH_FIELDS),
  CHANNELS_MULTI_MATCH_FIELDS: buildMultiMatchFields(CONFIG.CHANNELS_SEARCH.SEARCH_FIELDS),
  PLAYLISTS_MULTI_MATCH_FIELDS: buildMultiMatchFields(CONFIG.PLAYLISTS_SEARCH.SEARCH_FIELDS)
}

function getWebserverUrl () {
  if (CONFIG.WEBSERVER.PORT === 80 || CONFIG.WEBSERVER.PORT === 443) {
    return CONFIG.WEBSERVER.SCHEME + '://' + CONFIG.WEBSERVER.HOSTNAME
  }

  return CONFIG.WEBSERVER.SCHEME + '://' + CONFIG.WEBSERVER.HOSTNAME + ':' + CONFIG.WEBSERVER.PORT
}

function buildMultiMatchFields (fields: { [name: string]: { BOOST: number, FIELD_NAME: string } }) {
  return Object.keys(fields)
    .map(id => {
      const obj = fields[id]
      if (obj.BOOST <= 0) return ''

      return `${obj.FIELD_NAME}^${obj.BOOST}`
    })
    .filter(v => !!v)
}

if (isTestInstance()) {
  SCHEDULER_INTERVALS_MS.indexation = 1000 * 60 * 5 // 5 minutes
}

export {
  getWebserverUrl,

  CONFIG,
  API_VERSION,
  PAGINATION_COUNT,
  PAGINATION_START,
  SORTABLE_COLUMNS,
  INDEXER_QUEUE_CONCURRENCY,
  SCHEDULER_INTERVALS_MS,
  INDEXER_HOST_CONCURRENCY,
  INDEXER_COUNT,
  INDEXER_LIMIT,
  REQUESTS,
  ELASTIC_SEARCH_QUERY
}
