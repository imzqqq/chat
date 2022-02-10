import { ELASTIC_SEARCH_QUERY } from '../../../initializers/constants'
import validator from 'validator'

function addUUIDFilters (filters: any[], uuids: string[]) {
  if (!filters) return

  const result = {
    shortUUIDs: [] as string[],
    uuids: [] as string[]
  }

  for (const uuid of uuids) {
    if (validator.isUUID(uuid)) result.uuids.push(uuid)
    else result.shortUUIDs.push(uuid)
  }

  filters.push({
    bool: {
      should: [
        {
          terms: {
            uuid: result.uuids
          }
        },
        {
          terms: {
            shortUUID: result.shortUUIDs
          }
        }
      ]
    }
  })
}

function buildMultiMatchBool (search: string, fields: string[]) {
  return {
    must: [
      {
        multi_match: {
          query: search,
          fields,
          fuzziness: ELASTIC_SEARCH_QUERY.FUZZINESS,
          operator: ELASTIC_SEARCH_QUERY.OPERATOR,
          minimum_should_match: ELASTIC_SEARCH_QUERY.MINIMUM_SHOULD_MATCH
        }
      }
    ],
    should: [
      // Better score for exact search
      {
        multi_match: {
          query: search,
          fields,
          operator: ELASTIC_SEARCH_QUERY.OPERATOR,
          minimum_should_match: ELASTIC_SEARCH_QUERY.MINIMUM_SHOULD_MATCH
        }
      }
    ]
  }
}

export {
  addUUIDFilters,
  buildMultiMatchBool
}
