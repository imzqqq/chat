import axios from 'axios'
import { ResultList } from '../../../../shared/models/result-list.model'
import { GlobalStatsHistory } from '../../../shared/models/global-stats-history'
import { GlobalStats } from '../../../shared/models/global-stats.model'
import { Instance } from '../../../shared/models/instance.model'
import { buildApiUrl } from './utils'

const baseInstancePath = '/api/v1/instances'

function listInstances (params: {
  page: number
  perPage: number
  sort: string
  search?: string
  categoriesOr?: string[]
  languagesOr?: string[]
}) {
  const { search, categoriesOr, languagesOr } = params
  const options = {
    params: {
      start: (params.page - 1) * params.perPage,
      count: params.perPage,
      sort: params.sort
    }
  }

  if (search) Object.assign(options.params, { search })
  if (categoriesOr) Object.assign(options.params, { categoriesOr })
  if (languagesOr) Object.assign(options.params, { languagesOr })

  return axios.get<ResultList<Instance>>(buildApiUrl(baseInstancePath), options)
    .then(res => res.data)
}

function addInstance (host: string) {
  return axios.post(buildApiUrl(baseInstancePath), { host })
}

function getInstanceStats (params: { beforeDate?: string } = {}) {
  const options = { params }

  return axios.get<GlobalStats>(buildApiUrl(baseInstancePath) + '/stats', options)
              .then(res => res.data)
}

function getGlobalStatsHistory (params: { beforeDate?: string } = {}) {
  const options = { params }

  return axios.get<GlobalStatsHistory>(buildApiUrl(baseInstancePath) + '/stats-history', options)
              .then(res => res.data)
}

export {
  listInstances,
  addInstance,
  getGlobalStatsHistory,
  getInstanceStats
}
