import { elasticSearch } from '../../helpers/elastic-search'
import { CONFIG } from '../../initializers/constants'
import { listIndexInstancesHost } from '../requests/instances-index'

async function buildInstanceHosts () {
  let indexHosts = await listIndexInstancesHost()

  if (CONFIG.INSTANCES_INDEX.WHITELIST.ENABLED) {
    const whitelistHosts = Array.isArray(CONFIG.INSTANCES_INDEX.WHITELIST.HOSTS)
      ? CONFIG.INSTANCES_INDEX.WHITELIST.HOSTS
      : []

    indexHosts = indexHosts.filter(h => whitelistHosts.includes(h))
  }

  const dbHosts = await listDBInstances()
  const removedHosts = getRemovedHosts(dbHosts, indexHosts)

  return { indexHosts, removedHosts }
}

export {
  buildInstanceHosts
}

// ##################################################

async function listDBInstances () {
  const setResult = new Set<string>()
  const indexes = [
    CONFIG.ELASTIC_SEARCH.INDEXES.VIDEOS,
    CONFIG.ELASTIC_SEARCH.INDEXES.CHANNELS
  ]

  for (const index of indexes) {
    const res = await elasticSearch.search({
      index,
      body: {
        size: 0,
        aggs: {
          hosts: {
            terms: {
              size: 5000,
              field: 'host'
            }
          }
        }
      }
    })

    for (const b of res.body.aggregations.hosts.buckets) {
      setResult.add(b.key)
    }
  }

  return Array.from(setResult)
}

function getRemovedHosts (dbHosts: string[], indexHosts: string[]) {
  return dbHosts.filter(dbHost => indexHosts.includes(dbHost) === false)
}
