import * as geolite2 from 'geolite2-redist'
import maxmind, { CountryResponse, Reader } from 'maxmind'
import { logger } from '../helpers/logger'

let _countryLookup: Promise<Reader<CountryResponse>> = undefined

function getLookup () {
  if (_countryLookup) return _countryLookup

  _countryLookup = geolite2.downloadDbs()
    .then(() => {
      return geolite2.open('GeoLite2-Country', path => {
        return maxmind.open(path)
      })
    })

  return _countryLookup
}

async function countryLookup (ip: string): Promise<CountryResponse> {
  const lookup = await getLookup()

  try {
    return lookup.get(ip)
  } catch (err) {
    logger.error({ err }, 'Cannot lookup ip.')
  }
}

export {
  countryLookup
}
