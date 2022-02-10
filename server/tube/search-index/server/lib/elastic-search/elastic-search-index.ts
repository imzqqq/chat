import { flatMap } from 'lodash'
import { elasticSearch } from '../../helpers/elastic-search'
import { logger } from '../../helpers/logger'
import { IndexableDoc } from '../../types/indexable-doc.model'

function buildIndex (name: string, mapping: object) {
  logger.info('Initialize %s Elastic Search index.', name)

  return elasticSearch.indices.create({
    index: name,
    body: {
      settings: {
        number_of_shards: 1,
        number_of_replicas: 1
      },
      mappings: {
        properties: mapping
      }
    }
  }).catch(err => {
    if (err.name === 'ResponseError' && err.meta?.body?.error.root_cause[0]?.type === 'resource_already_exists_exception') return

    throw err
  })
}

async function indexDocuments <T extends IndexableDoc> (options: {
  objects: T[]
  formatter: (o: T) => any
  replace: boolean
  index: string
}) {
  const { objects, formatter, replace, index } = options

  const elIdIndex: { [elId: string]: T } = {}

  for (const object of objects) {
    elIdIndex[object.elasticSearchId] = object
  }

  const method = replace ? 'index' : 'update'

  const body = flatMap(objects, v => {
    const doc = formatter(v)

    const options = replace
      ? doc
      : { doc, doc_as_upsert: true }

    return [
      {
        [method]: {
          _id: v.elasticSearchId,
          _index: index
        }
      },
      options
    ]
  })

  const result = await elasticSearch.bulk({
    index,
    body
  })

  const resultBody = result.body

  if (resultBody.errors === true) {
    const msg = 'Cannot insert data in elastic search.'
    logger.error({ err: resultBody }, msg)
    throw new Error(msg)
  }

  const created: T[] = result.body.items
                                  .map(i => i[method])
                                  .filter(i => i.result === 'created')
                                  .map(i => elIdIndex[i._id])

  return { created }
}

function refreshIndex (indexName: string) {
  logger.info('Refreshing %s index.', indexName)

  return elasticSearch.indices.refresh({ index: indexName })
}

export {
  buildIndex,
  indexDocuments,
  refreshIndex
}
