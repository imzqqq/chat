import { readFileSync } from 'fs-extra'
import { Client } from '@elastic/elasticsearch'
import { CONFIG } from '../initializers/constants'

const elasticOptions = {
  node: CONFIG.ELASTIC_SEARCH.HTTP + '://' + CONFIG.ELASTIC_SEARCH.HOSTNAME + ':' + CONFIG.ELASTIC_SEARCH.PORT
}

if (CONFIG.ELASTIC_SEARCH.SSL.CA) {
  Object.assign(elasticOptions, {
    ssl: {
      ca: readFileSync(CONFIG.ELASTIC_SEARCH.SSL.CA)
    }
  })
}

if (CONFIG.ELASTIC_SEARCH.AUTH.USERNAME) {
  Object.assign(elasticOptions, {
    auth: {
      username: CONFIG.ELASTIC_SEARCH.AUTH.USERNAME,
      password: CONFIG.ELASTIC_SEARCH.AUTH.PASSWORD
    }
  })
}

const elasticSearch = new Client(elasticOptions)

export {
  elasticSearch
}
