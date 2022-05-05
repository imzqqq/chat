import { difference } from 'lodash'
import { ApiResponse } from '@elastic/elasticsearch'
import { elasticSearch } from '../../helpers/elastic-search'
import { logger } from '../../helpers/logger'

async function removeNotExistingIdsFromHost (indexName: string, host: string, existingIds: Set<number>) {
  const idsFromDB = await getIdsOf(indexName, host)

  const idsToRemove = difference(idsFromDB, Array.from(existingIds))

  logger.info({ idsToRemove }, 'Will remove %d entries from %s of host %s.', idsToRemove.length, indexName, host)

  return elasticSearch.delete_by_query({
    index: indexName,
    body: {
      query: {
        bool: {
          filter: [
            {
              terms: {
                id: idsToRemove
              }
            },
            {
              term: {
                host
              }
            }
          ]
        }
      }
    }
  })
}

function removeFromHosts (indexName: string, hosts: string[]) {
  if (hosts.length === 0) return

  logger.info({ hosts }, 'Will remove entries of index %s from hosts.', indexName)

  return elasticSearch.delete_by_query({
    index: indexName,
    body: {
      query: {
        bool: {
          filter: {
            terms: {
              host: hosts
            }
          }
        }
      }
    }
  })
}

async function getIdsOf (indexName: string, host: string) {
  const res = await elasticSearch.search({
    index: indexName,
    body: {
      size: 0,
      aggs: {
        ids: {
          terms: {
            size: 500000,
            field: 'id'
          }
        }
      },
      query: {
        bool: {
          filter: [
            {
              term: {
                host
              }
            }
          ]
        }
      }
    }
  })

  return res.body.aggregations.ids.buckets.map(b => b.key)
}

function extractQueryResult (result: ApiResponse<any, any>) {
  const hits = result.body.hits

  return { total: hits.total.value, data: hits.hits.map(h => Object.assign(h._source, { score: h._score })) }
}

function buildSort (value: string) {
  let sortField: string
  let direction: 'asc' | 'desc'

  if (value.substring(0, 1) === '-') {
    direction = 'desc'
    sortField = value.substring(1)
  } else {
    direction = 'asc'
    sortField = value
  }

  const field = sortField === 'match'
    ? '_score'
    : sortField

  return [
    {
      [field]: { order: direction }
    }
  ]
}

export {
  elasticSearch,
  removeNotExistingIdsFromHost,
  getIdsOf,
  extractQueryResult,
  removeFromHosts,
  buildSort
}
