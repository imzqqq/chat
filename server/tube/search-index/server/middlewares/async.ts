import { eachSeries } from 'async'
import { NextFunction, Request, RequestHandler, Response } from 'express'
import { ValidationChain } from 'express-validator'

export type ExpressPromiseHandler = (req: Request<any>, res: Response, next: NextFunction) => Promise<any>

export type RequestPromiseHandler = ValidationChain | ExpressPromiseHandler

function asyncMiddleware (fun: RequestPromiseHandler | RequestPromiseHandler[]) {
  return (req: Request, res: Response, next: NextFunction) => {
    if (Array.isArray(fun) === true) {
      return eachSeries(fun as RequestHandler[], (f, cb) => {
        Promise.resolve(f(req, res, err => cb(err)))
          .catch(err => next(err))
      }, next)
    }

    return Promise.resolve((fun as RequestHandler)(req, res, next))
      .catch(err => next(err))
  }
}

// ---------------------------------------------------------------------------

export {
  asyncMiddleware
}
