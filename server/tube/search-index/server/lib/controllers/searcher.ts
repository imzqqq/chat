import { ResultList } from '../../../Tube/shared/models'
import { CONFIG } from '../../initializers/constants'
import { CommonSearch } from '../../types/search-query/common-search.model'

export class Searcher <T extends CommonSearch, R, F> {

  constructor (
    private readonly queryFn: (query: T) => Promise<ResultList<R>>,
    private readonly formatFn: (data: R, fromHost: string) => F
  ) {}

  async getResult (queryArg: T): Promise<ResultList<F>> {
    const query = { ...queryArg }

    if (!Array.isArray(query.blockedHosts)) {
      query.blockedHosts = []
    }

    if (CONFIG.API.BLACKLIST.ENABLED && Array.isArray(CONFIG.API.BLACKLIST.HOSTS)) {
      query.blockedHosts = query.blockedHosts.concat(CONFIG.API.BLACKLIST.HOSTS)
    }

    const resultList = await this.queryFn(query)

    return {
      total: resultList.total,
      data: resultList.data.map(d => this.formatFn(d, query.fromHost))
    }
  }
}
