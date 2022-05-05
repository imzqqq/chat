import { AsyncQueue, queue } from 'async'
import { inspect } from 'util'
import { logger } from '../../../helpers/logger'
import { INDEXER_QUEUE_CONCURRENCY } from '../../../initializers/constants'
import { buildIndex, indexDocuments, refreshIndex } from '../../../lib/elastic-search/elastic-search-index'
import { removeFromHosts, removeNotExistingIdsFromHost } from '../../../lib/elastic-search/elastic-search-queries'
import { buildVideosMapping } from '../../../lib/elastic-search/elastic-search-videos'
import { IndexableDoc } from '../../../types/indexable-doc.model'

// identifier could be an uuid, an handle or a url for example
export type QueueParam = { host: string, identifier: string }

export abstract class AbstractIndexer <T extends IndexableDoc, DB> {
  protected readonly indexQueue: AsyncQueue<QueueParam>

  abstract indexSpecificElement (host: string, uuid: string): Promise<any>
  abstract buildMapping (): object

  constructor (
    protected readonly indexName: string,
    protected readonly formatterFn: (o: T) => DB
  ) {
    this.indexQueue = queue<QueueParam, Error>((task, cb) => {
      this.indexSpecificElement(task.host, task.identifier)
        .then(() => cb())
        .catch(err => {
          logger.error(
            { err: inspect(err) },
            'Error in index specific element %s of %s in index %s.', task.identifier, task.host, this.indexName
          )
          cb()
        })
    }, INDEXER_QUEUE_CONCURRENCY)
  }

  initIndex () {
    return buildIndex(this.indexName, this.buildMapping())
  }

  scheduleIndexation (host: string, identifier: string) {
    this.indexQueue.push({ identifier, host })
      .catch(err => logger.error({ err: inspect(err) }, 'Cannot schedule indexation of %s for %s', identifier, host))
  }

  refreshIndex () {
    return refreshIndex(this.indexName)
  }

  removeNotExisting (host: string, existingIds: Set<number>) {
    return removeNotExistingIdsFromHost(this.indexName, host, existingIds)
  }

  removeFromHosts (hosts: string[]) {
    return removeFromHosts(this.indexName, hosts)
  }

  indexElements (elements: T[], replace = false) {
    return indexDocuments({
      objects: elements,
      formatter: v => this.formatterFn(v),
      replace,
      index: this.indexName
    })
  }
}
