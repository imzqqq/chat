import * as express from 'express'
import { query, validationResult } from 'express-validator'
import { logger } from '../../helpers/logger'

function areValidationErrors (req: express.Request, res: express.Response) {
  const errors = validationResult(req)

  if (!errors.isEmpty()) {
    logger.warn({ path: req.originalUrl, err: errors.mapped() }, 'Incorrect request parameters')
    res.status(400).json({ errors: errors.mapped() })

    return true
  }

  return false
}

function checkSort (sortableColumns: string[]) {
  return [
    query('sort').optional().isIn(sortableColumns).withMessage('Should have correct sortable column'),

    (req: express.Request, res: express.Response, next: express.NextFunction) => {
      logger.debug({ parameters: req.query }, 'Checking sort parameters')

      if (areValidationErrors(req, res)) return

      return next()
    }
  ]
}

function createSortableColumns (sortableColumns: string[]) {
  const sortableColumnDesc = sortableColumns.map(sortableColumn => '-' + sortableColumn)

  return sortableColumns.concat(sortableColumnDesc)
}

// ---------------------------------------------------------------------------

export {
  areValidationErrors,
  checkSort,
  createSortableColumns
}
