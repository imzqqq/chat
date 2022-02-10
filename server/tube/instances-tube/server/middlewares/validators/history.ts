import * as express from 'express'
import { query } from 'express-validator'
import { logger } from '../../helpers/logger'
import { areValidationErrors } from './utils'

const historyListValidator = [
  query('beforeDate').optional().isISO8601().withMessage('Should have a valid beforeDate date'),

  (req: express.Request, res: express.Response, next: express.NextFunction) => {
    logger.debug({ parameters: req.query }, 'Checking history list parameters')

    if (areValidationErrors(req, res)) return
    return next()
  }
]

// ---------------------------------------------------------------------------

export {
  historyListValidator
}
