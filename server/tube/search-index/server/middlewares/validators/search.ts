import * as express from 'express'
import { check } from 'express-validator'
import { isDateValid, toArray } from '../../helpers/custom-validators/misc'
import { isNSFWQueryValid, isNumberArray, isStringArray } from '../../helpers/custom-validators/search-videos'
import { logger } from '../../helpers/logger'
import { areValidationErrors } from './utils'

const commonFiltersValidators = [
  check('blockedAccounts')
    .optional()
    .customSanitizer(toArray)
    .custom(isStringArray).withMessage('Should have a valid blockedAccounts array'),
  check('blockedHosts')
    .optional()
    .customSanitizer(toArray)
    .custom(isStringArray).withMessage('Should have a valid hosts array'),

  (req: express.Request, res: express.Response, next: express.NextFunction) => {
    logger.debug({ query: req.query, body: req.body }, 'Checking commons filters query')

    if (areValidationErrors(req, res)) return

    return next()
  }
]

const commonVideosFiltersValidator = [
  check('categoryOneOf')
    .optional()
    .customSanitizer(toArray)
    .custom(isNumberArray).withMessage('Should have a valid one of category array'),
  check('licenceOneOf')
    .optional()
    .customSanitizer(toArray)
    .custom(isNumberArray).withMessage('Should have a valid one of licence array'),
  check('languageOneOf')
    .optional()
    .customSanitizer(toArray)
    .custom(isStringArray).withMessage('Should have a valid one of language array'),
  check('tagsOneOf')
    .optional()
    .customSanitizer(toArray)
    .custom(isStringArray).withMessage('Should have a valid one of tags array'),
  check('tagsAllOf')
    .optional()
    .customSanitizer(toArray)
    .custom(isStringArray).withMessage('Should have a valid all of tags array'),
  check('nsfw')
    .optional()
    .custom(isNSFWQueryValid).withMessage('Should have a valid NSFW attribute'),
  check('isLive')
    .optional()
    .toBoolean(),

  (req: express.Request, res: express.Response, next: express.NextFunction) => {
    if (areValidationErrors(req, res)) return

    return next()
  }
]

const videosSearchValidator = [
  check('search').optional().not().isEmpty().withMessage('Should have a valid search'),

  check('host').optional().not().isEmpty().withMessage('Should have a valid host'),

  check('startDate').optional().custom(isDateValid).withMessage('Should have a valid start date'),
  check('endDate').optional().custom(isDateValid).withMessage('Should have a valid end date'),

  check('originallyPublishedStartDate').optional().custom(isDateValid).withMessage('Should have a valid published start date'),
  check('originallyPublishedEndDate').optional().custom(isDateValid).withMessage('Should have a valid published end date'),

  check('durationMin').optional().isInt().withMessage('Should have a valid min duration'),
  check('durationMax').optional().isInt().withMessage('Should have a valid max duration'),

  check('boostLanguages')
    .optional()
    .customSanitizer(toArray)
    .custom(isStringArray).withMessage('Should have a valid boostLanguages array'),

  check('uuids')
    .optional()
    .toArray(),

  (req: express.Request, res: express.Response, next: express.NextFunction) => {
    logger.debug({ query: req.query, body: req.body }, 'Checking videos search query')

    if (areValidationErrors(req, res)) return

    return next()
  }
]

const videoChannelsSearchValidator = [
  check('search').optional().not().isEmpty().withMessage('Should have a valid search'),
  check('host').optional().not().isEmpty().withMessage('Should have a valid host'),
  check('handles').optional().toArray(),

  (req: express.Request, res: express.Response, next: express.NextFunction) => {
    logger.debug({ query: req.query, body: req.body }, 'Checking video channels search query')

    if (areValidationErrors(req, res)) return

    return next()
  }
]

const videoPlaylistsSearchValidator = [
  check('search').optional().not().isEmpty().withMessage('Should have a valid search'),

  check('host').optional().not().isEmpty().withMessage('Should have a valid host'),

  check('uuids')
    .optional()
    .toArray(),

  (req: express.Request, res: express.Response, next: express.NextFunction) => {
    logger.debug({ query: req.query, body: req.body }, 'Checking video playlists search query')

    if (areValidationErrors(req, res)) return

    return next()
  }
]

// ---------------------------------------------------------------------------

export {
  videoChannelsSearchValidator,
  commonFiltersValidators,
  commonVideosFiltersValidator,
  videoPlaylistsSearchValidator,
  videosSearchValidator
}
