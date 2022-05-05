import axios from 'axios'
import { buildApiUrl } from './utils'
import { ServerConfig } from '../../../shared/models/server-config.model'

const basePath = '/api/v1/config'
let serverConfigPromise: Promise<ServerConfig>

function getConfigHttp () {
  return axios.get<ServerConfig>(buildApiUrl(basePath))
}

async function loadServerConfig () {
  const res = await getConfigHttp()

  return res.data
}

function getConfig () {
  if (serverConfigPromise) return serverConfigPromise

  serverConfigPromise = loadServerConfig()

  return serverConfigPromise
}

export {
  getConfig,
  loadServerConfig
}
