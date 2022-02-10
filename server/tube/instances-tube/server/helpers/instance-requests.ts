import { ServerConfig } from '../../../shared/models'
import { ServerStats } from '../../../shared/models/server/server-stats.model'
import { InstanceConnectivityStats } from 'shared/models/instance-connectivity-stats.model'
import { countryLookup } from '../initializers/geoip'
import { isTestInstance } from './core-utils'
import { faultTolerantResolve } from './utils'
import { doRequest } from './requests'
import { About } from '../../../shared/models/server'
import { REQUEST_TIMEOUT } from '../initializers/constants'

async function fetchInstanceConfig (host: string) {
  const path = '/api/v1/config'

  const options = {
    uri: getScheme() + host + path,
    method: 'GET',
    json: true,
    timeout: REQUEST_TIMEOUT
  }

  const { body } = await doRequest(options)
  return body as ServerConfig
}

async function fetchInstanceAbout (host: string) {
  const path = '/api/v1/config/about'

  const options = {
    uri: getScheme() + host + path,
    method: 'GET',
    json: true,
    timeout: REQUEST_TIMEOUT
  }

  const { body } = await doRequest(options)
  return body as About
}

async function fetchInstanceStats (host: string) {
  const path = '/api/v1/server/stats'

  const options = {
    uri: getScheme() + host + path,
    method: 'GET',
    json: true,
    timeout: REQUEST_TIMEOUT
  }

  const { body } = await doRequest(options)
  const ipv4Addresses = await faultTolerantResolve(host, 'A')
  const ipv6Addresses = await faultTolerantResolve(host, 'AAAA')
  const addresses = [...ipv4Addresses, ...ipv6Addresses]

  const supportsIPv6 = ipv6Addresses.length > 0
  const country = (addresses.length > 0) ?
    (await countryLookup(addresses[0])).country.iso_code // lookup does a country lookup on a local geoip db downloaded on first use
    : undefined

  return {
    stats: body as ServerStats,
    connectivityStats: {
      supportsIPv6,
      country
    } as InstanceConnectivityStats
  }
}

async function getConfigAndStatsAndAboutInstance (host: string) {
  const config = await fetchInstanceConfig(host)
  const about = await fetchInstanceAbout(host)
  const { stats, connectivityStats } = await fetchInstanceStats(host)

  if (!config || !stats || config.serverVersion === undefined || stats.totalVideos === undefined) {
    throw new Error('Invalid remote host. Are you sure this is a Tube instance?')
  }

  return { config, stats, about, connectivityStats }
}

// ---------------------------------------------------------------------------

export {
  getConfigAndStatsAndAboutInstance,
  fetchInstanceConfig,
  fetchInstanceStats,
  fetchInstanceAbout
}

// ---------------------------------------------------------------------------

function getScheme () {
  if (isTestInstance()) return 'http://'

  return 'https://'
}
