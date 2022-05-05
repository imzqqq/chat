import * as retry from 'async/retry'
import * as Bluebird from 'bluebird'
import { logger } from './logger'

type RetryTransactionWrapperOptions = { errorMessage: string, arguments?: any[] }
function retryTransactionWrapper <T> (
  functionToRetry: (...args) => Promise<T> | Bluebird<T>,
  options: RetryTransactionWrapperOptions
): Promise<T> {
  const args = options.arguments ? options.arguments : []

  return transactionRetryer<T>(callback => {
    functionToRetry.apply(this, args)
        .then((result: T) => callback(null, result))
        .catch(err => callback(err))
  })
  .catch(err => {
    console.error(err)
    logger.error({ err }, options.errorMessage)
    throw err
  })
}

function transactionRetryer <T> (func: (err: any, data: T) => any) {
  return new Promise<T>((res, rej) => {
    retry({
      times: 5,

      errorFilter: err => {
        const willRetry = (err.name === 'SequelizeDatabaseError')
        logger.debug({ willRetry }, 'Maybe retrying the transaction function.')
        return willRetry
      }
    }, func, (err, data) => err ? rej(err) : res(data))
  })
}

// ---------------------------------------------------------------------------

export {
  retryTransactionWrapper,
  transactionRetryer
}
