import * as express from 'express'
import { ResultList } from '../../Tube/shared/models/common/result-list.model'

function badRequest (req: express.Request, res: express.Response) {
  return res.type('json').status(400).end()
}

interface FormattableToJSON <T> {
  toFormattedJSON: () => T
}

function getFormattedObjects<U, T extends FormattableToJSON<U>> (objects: T[], objectsTotal: number) {
  const formattedObjects: U[] = []

  objects.forEach(object => {
    formattedObjects.push(object.toFormattedJSON())
  })

  return {
    total: objectsTotal,
    data: formattedObjects
  } as ResultList<U>
}

function buildUrl (host: string, path: string) {
  return 'https://' + host + path
}

// ---------------------------------------------------------------------------

export {
  badRequest,
  getFormattedObjects,
  buildUrl
}
