import * as Bluebird from 'bluebird'
import * as request from 'request'
import { getWebserverUrl } from '../initializers/constants'
import { waitMs } from './core-utils'

function doRequest <T> (
  requestOptions: request.CoreOptions & request.UriOptions
): Bluebird<{ response: request.RequestResponse, body: T }> {
  if (!(requestOptions.headers)) requestOptions.headers = {}

  requestOptions.headers['User-Agent'] = `Tube search index (+${getWebserverUrl()})`

  return new Bluebird<{ response: request.RequestResponse, body: T }>((res, rej) => {
    request(requestOptions, (err, response, body) => err ? rej(err) : res({ response, body }))
  })
}

async function doRequestWithRetries<T> (
  requestOptions: request.CoreOptions & request.UriOptions,
  maxRetries: number,
  msToWait: number,
  currentRetry = 0
): Promise<{ response: request.RequestResponse, body: T }> {
  const updatedRequestOptions = Object.assign({}, requestOptions, { timeout: 10000 })

  const res = await doRequest<T>(updatedRequestOptions)

  if (res.response.statusCode === 429) {
    if (currentRetry < maxRetries) {
      await waitMs(msToWait)
      return doRequestWithRetries(requestOptions, maxRetries, msToWait, currentRetry + 1)
    }

    throw new Error('Exceeded max retries for request ' + requestOptions.uri)
  }

  return res
}

export {
  doRequest,
  doRequestWithRetries
}
