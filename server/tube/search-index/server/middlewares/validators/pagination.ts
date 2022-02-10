import * as express from 'express'
import { check } from 'express-validator'
import { PAGINATION_COUNT, PAGINATION_START } from '../../initializers/constants'
import { areValidationErrors } from './utils'

const paginationValidator = [
  check('start')
    .optional()
    .isInt({ min: 0, max: PAGINATION_START.MAX }).withMessage(`Should have a number start (>= 0 and < ${PAGINATION_START.MAX})`),

  check('count')
    .optional()
    .isInt({ min: 0, max: PAGINATION_COUNT.MAX }).withMessage(`Should have a number count (> 0 and < ${PAGINATION_COUNT.MAX})`),

  (req: express.Request, res: express.Response, next: express.NextFunction) => {
    if (areValidationErrors(req, res)) return

    return next()
  }
]

// ---------------------------------------------------------------------------

export {
  paginationValidator
}
