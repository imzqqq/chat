import { exists } from '../../helpers/custom-validators/misc'
import { elasticSearch } from '../../helpers/elastic-search'
import { logger } from '../../helpers/logger'
import { buildUrl } from '../../helpers/utils'
import { CONFIG, ELASTIC_SEARCH_QUERY } from '../../initializers/constants'
import { VideosSearchQuery } from '../../types/search-query/video-search.model'
import { DBVideo, DBVideoDetails, EnhancedVideo, IndexableVideo, IndexableVideoDetails } from '../../types/video.model'
import { buildSort, extractQueryResult } from './elastic-search-queries'
import { addUUIDFilters, buildMultiMatchBool } from './shared'
import { buildChannelOrAccountSummaryMapping, formatActorForDB, formatActorSummaryForAPI } from './shared/elastic-search-actor'

async function queryVideos (search: VideosSearchQuery) {
  const bool: any = {}
  const filter: any[] = []
  const mustNot: any[] = []

  if (search.search) {
    Object.assign(bool, buildMultiMatchBool(search.search, ELASTIC_SEARCH_QUERY.VIDEOS_MULTI_MATCH_FIELDS))
  }

  if (search.blockedAccounts) {
    mustNot.push({
      terms: {
        'account.handle': search.blockedAccounts
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

  if (search.startDate) {
    filter.push({
      range: {
        publishedAt: {
          gte: search.startDate
        }
      }
    })
  }

  if (search.endDate) {
    filter.push({
      range: {
        publishedAt: {
          lte: search.endDate
        }
      }
    })
  }

  if (search.originallyPublishedStartDate) {
    filter.push({
      range: {
        originallyPublishedAt: {
          gte: search.startDate
        }
      }
    })
  }

  if (search.originallyPublishedEndDate) {
    filter.push({
      range: {
        originallyPublishedAt: {
          lte: search.endDate
        }
      }
    })
  }

  if (search.nsfw && search.nsfw !== 'both') {
    filter.push({
      term: {
        nsfw: (search.nsfw + '') === 'true'
      }
    })
  }

  if (search.categoryOneOf) {
    filter.push({
      terms: {
        'category.id': search.categoryOneOf
      }
    })
  }

  if (search.licenceOneOf) {
    filter.push({
      terms: {
        'licence.id': search.licenceOneOf
      }
    })
  }

  if (search.languageOneOf) {
    filter.push({
      terms: {
        'language.id': search.languageOneOf
      }
    })
  }

  if (search.tagsOneOf) {
    filter.push({
      terms: {
        tags: search.tagsOneOf
      }
    })
  }

  if (search.tagsAllOf) {
    for (const t of search.tagsAllOf) {
      filter.push({
        term: {
          tags: t
        }
      })
    }
  }

  if (search.durationMin) {
    filter.push({
      range: {
        duration: {
          gte: search.durationMin
        }
      }
    })
  }

  if (search.durationMax) {
    filter.push({
      range: {
        duration: {
          lte: search.durationMax
        }
      }
    })
  }

  if (exists(search.isLive)) {
    filter.push({
      term: {
        isLive: search.isLive
      }
    })
  }

  if (search.host) {
    filter.push({
      term: {
        'account.host': search.host
      }
    })
  }

  if (search.uuids) {
    addUUIDFilters(filter, search.uuids)
  }

  Object.assign(bool, { filter })

  if (mustNot.length !== 0) {
    Object.assign(bool, { must_not: mustNot })
  }

  const body = {
    from: search.start,
    size: search.count,
    sort: buildSort(search.sort)
  }

  // Allow to boost results depending on query languages
  if (
    CONFIG.VIDEOS_SEARCH.BOOST_LANGUAGES.ENABLED &&
    Array.isArray(search.boostLanguages) &&
    search.boostLanguages.length !== 0
  ) {
    const boostScript = `
      if (doc['language.id'].size() == 0) {
        return _score;
      }

      String language = doc['language.id'].value;

      for (String docLang: params.boostLanguages) {
        if (docLang == language) return _score * params.boost;
      }

      return _score * params.malus;
    `

    Object.assign(body, {
      query: {
        script_score: {
          query: { bool },
          script: {
            source: boostScript,
            params: {
              boostLanguages: search.boostLanguages,
              boost: ELASTIC_SEARCH_QUERY.BOOST_LANGUAGE_VALUE,
              malus: ELASTIC_SEARCH_QUERY.MALUS_LANGUAGE_VALUE
            }
          }
        }
      }
    })
  } else {
    Object.assign(body, { query: { bool } })
  }

  logger.debug({ body }, 'Will query Elastic Search for videos.')

  const res = await elasticSearch.search({
    index: CONFIG.ELASTIC_SEARCH.INDEXES.VIDEOS,
    body
  })

  return extractQueryResult(res)
}

function buildVideosMapping () {
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
    publishedAt: {
      type: 'date',
      format: 'date_optional_time'
    },
    originallyPublishedAt: {
      type: 'date',
      format: 'date_optional_time'
    },
    indexedAt: {
      type: 'date',
      format: 'date_optional_time'
    },

    category: {
      properties: {
        id: {
          type: 'keyword'
        },
        label: {
          type: 'text'
        }
      }
    },

    licence: {
      properties: {
        id: {
          type: 'keyword'
        },
        label: {
          type: 'text'
        }
      }
    },

    language: {
      properties: {
        id: {
          type: 'keyword'
        },
        label: {
          type: 'text'
        }
      }
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

    name: {
      type: 'text'
    },

    description: {
      type: 'text'
    },

    tags: {
      type: 'text',

      fields: {
        raw: {
          type: 'keyword'
        }
      }
    },

    duration: {
      type: 'long'
    },

    thumbnailPath: {
      type: 'keyword'
    },
    previewPath: {
      type: 'keyword'
    },
    embedPath: {
      type: 'keyword'
    },

    url: {
      type: 'keyword'
    },

    views: {
      type: 'long'
    },
    likes: {
      type: 'long'
    },
    dislikes: {
      type: 'long'
    },
    nsfw: {
      type: 'boolean'
    },
    isLive: {
      type: 'boolean'
    },

    host: {
      type: 'keyword'
    },

    account: {
      properties: buildChannelOrAccountSummaryMapping()
    },

    channel: {
      properties: buildChannelOrAccountSummaryMapping()
    }
  }
}

function formatVideoForDB (v: IndexableVideo | IndexableVideoDetails): DBVideo | DBVideoDetails {
  return {
    id: v.id,
    uuid: v.uuid,
    shortUUID: v.shortUUID,

    indexedAt: new Date(),
    createdAt: v.createdAt,
    updatedAt: v.updatedAt,
    publishedAt: v.publishedAt,
    originallyPublishedAt: v.originallyPublishedAt,

    category: {
      id: v.category.id,
      label: v.category.label
    },
    licence: {
      id: v.licence.id,
      label: v.licence.label
    },
    language: {
      id: v.language.id,
      label: v.language.label
    },
    privacy: {
      id: v.privacy.id,
      label: v.privacy.label
    },

    name: v.name,
    description: v.description,
    duration: v.duration,
    thumbnailPath: v.thumbnailPath,
    previewPath: v.previewPath,
    embedPath: v.embedPath,

    views: v.views,
    likes: v.likes,
    dislikes: v.dislikes,

    isLive: v.isLive || false,
    nsfw: v.nsfw,

    host: v.host,
    url: v.url,

    tags: (v as IndexableVideoDetails).tags ? (v as IndexableVideoDetails).tags : undefined,

    account: formatActorForDB(v.account),
    channel: formatActorForDB(v.channel)
  }
}

function formatVideoForAPI (v: DBVideoDetails, fromHost?: string): EnhancedVideo {
  return {
    id: v.id,
    uuid: v.uuid,
    shortUUID: v.shortUUID,

    score: v.score,

    createdAt: new Date(v.createdAt),
    updatedAt: new Date(v.updatedAt),
    publishedAt: new Date(v.publishedAt),
    originallyPublishedAt: v.originallyPublishedAt,

    category: {
      id: v.category.id,
      label: v.category.label
    },
    licence: {
      id: v.licence.id,
      label: v.licence.label
    },
    language: {
      id: v.language.id,
      label: v.language.label
    },
    privacy: {
      id: v.privacy.id,
      label: v.privacy.label
    },

    name: v.name,
    description: v.description,
    duration: v.duration,

    tags: v.tags,

    thumbnailPath: v.thumbnailPath,
    thumbnailUrl: buildUrl(v.host, v.thumbnailPath),

    previewPath: v.previewPath,
    previewUrl: buildUrl(v.host, v.previewPath),

    embedPath: v.embedPath,
    embedUrl: buildUrl(v.host, v.embedPath),

    url: v.url,

    isLocal: fromHost && fromHost === v.host,

    views: v.views,
    likes: v.likes,
    dislikes: v.dislikes,

    isLive: v.isLive,
    nsfw: v.nsfw,

    account: formatActorSummaryForAPI(v.account),
    channel: formatActorSummaryForAPI(v.channel)
  }
}

export {
  queryVideos,
  formatVideoForDB,
  formatVideoForAPI,
  buildVideosMapping
}
