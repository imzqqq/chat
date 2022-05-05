import * as express from 'express'

const setDefaultSort = setDefaultSortFactory('-createdAt')

const setDefaultSearchSort = setDefaultSortFactory('-match')

// ---------------------------------------------------------------------------

export {
  setDefaultSort,
  setDefaultSearchSort
}

// ---------------------------------------------------------------------------

function setDefaultSortFactory (sort: string) {
  return (req: express.Request, res: express.Response, next: express.NextFunction) => {
    if (!req.query.sort) req.query.sort = sort

    return next()
  }
}
