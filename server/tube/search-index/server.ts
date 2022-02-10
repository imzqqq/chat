import { isTestInstance } from './server/helpers/core-utils'

if (isTestInstance()) {
  require('source-map-support').install()
}

import * as bodyParser from 'body-parser'
import * as express from 'express'
import * as cors from 'cors'
import * as morgan from 'morgan'
import { apiRouter } from './server/controllers/api'
import { logger } from './server/helpers/logger'
import { API_VERSION, CONFIG, getWebserverUrl } from './server/initializers/constants'
import { IndexationScheduler } from './server/lib/schedulers/indexation-scheduler'
import { join } from 'path'
import { readFile } from 'fs-extra'

const app = express()
const url = getWebserverUrl()

app.use(morgan('combined', {
  stream: { write: logger.info.bind(logger) }
}))

app.use(bodyParser.json({
  type: [ 'application/json', 'application/*+json' ],
  limit: '5mb'
}))
app.use(bodyParser.urlencoded({ extended: false }))

// ----------- Views, routes and static files -----------

app.use(cors())

const apiRoute = '/api/' + API_VERSION
app.use(apiRoute, apiRouter)

// Static client files
app.use('/js/', express.static(join(__dirname, '../client/dist/js'), { maxAge: '30d' }))
app.use('/css/', express.static(join(__dirname, '../client/dist/css'), { maxAge: '30d' }))
app.use('/img/', express.static(join(__dirname, '../client/dist/img'), { maxAge: '30d' }))
app.use('/theme/', express.static(join(__dirname, './themes'), { maxAge: '30d' }))

app.use('/opensearch.xml', async function (req, res) {
  const data = `<?xml version="1.0" encoding="UTF-8" ?>
  <OpenSearchDescription xmlns="http://a9.com/-/spec/opensearch/1.1/" xmlns:moz="http://www.mozilla.org/2006/browser/search/">
    <ShortName>${CONFIG.SEARCH_INSTANCE.NAME}</ShortName>
    <Description>${CONFIG.SEARCH_INSTANCE.DESCRIPTION}</Description>
    <Image height="64" width="64" type="image/png">${url}/img/favicon.png</Image>
    <Language>*</Language>
    <Tags>tube video</Tags>
    <Url type="text/html" method="get" rel="results" template="${url}/search?search={searchTerms}&amp;page={startPage?}"/>
    <Url type="application/opensearchdescription+xml" rel="self" template="${url}/opensearch.xml"/>
    <Query role="example" searchTerms="tube"/>
    <SyndicationRight>open</SyndicationRight>
    <AdultContent>true</AdultContent>
    <InputEncoding>UTF-8</InputEncoding>
    <OutputEncoding>UTF-8</OutputEncoding>
    <Developer>Framasoft: contact.framasoft.org</Developer>
  </OpenSearchDescription>`

  return res.type('application/xml').send(data).end()
})

let indexHTML: string

app.use('/*', async function (req, res) {
  res.set('Content-Type', 'text/html; charset=UTF-8')

  if (indexHTML) return res.send(indexHTML)

  let bufferCSS: Buffer
  if (CONFIG.SEARCH_INSTANCE.THEME !== 'default') {

    try {
      bufferCSS = await readFile(join(__dirname, 'themes', CONFIG.SEARCH_INSTANCE.THEME, 'index.css'))
    } catch (err) {
      logger.error({ err }, 'Cannot fetch CSS theme.')
    }
  }

  const title = CONFIG.SEARCH_INSTANCE.NAME
  const description = CONFIG.SEARCH_INSTANCE.DESCRIPTION
  const styleCSS = bufferCSS
    ? `<style type="text/css">${bufferCSS.toString()}</style>`
    : ''

  const tags = `
  <title>${title}</title>
  <meta name="description" content="${description}">

  <meta property="og:url" content="${url}">
  <meta property="og:title" content="${title}">
  <meta property="og:description" content="${description}">
  <meta property="og:image" content="${url}/img/card-opengraph.png">
  <meta property="og:site_name" content="${title}">

  <meta name="twitter:card" content="summary_large_image">
  <meta name="twitter:site" content="@joinpeertube">
  <meta name="twitter:creator" content="@chocobozzz">
  <meta name="twitter:title" content="${title}">
  <meta name="twitter:description" content="${description}">
  <meta name="twitter:image" content="${url}/img/card-opengraph.png">

  <link rel="search" type="application/opensearchdescription+xml" title="${title}" href="${url}/opensearch.xml" />

  ${styleCSS}`

  const buffer = await readFile(join(__dirname, '../client/dist/index.html'))

  indexHTML = buffer.toString()
  indexHTML = indexHTML.replace('</head>', tags + '</head>')

  return res.send(indexHTML)
})

// ----------- Errors -----------

// Catch 404 and forward to error handler
app.use(function (req, res, next) {
  const err = new Error('Not Found') as any
  err.status = 404
  next(err)
})

app.use(function (err, req, res, next) {
  let error = 'Unknown error.'
  if (err) {
    error = err.stack || err.message || err
  }

  logger.error({ error }, 'Error in controller.')
  return res.status(err.status || 500).end()
})

// ----------- Run -----------

app.listen(CONFIG.LISTEN.PORT, async () => {
  logger.info('Server listening on port %d', CONFIG.LISTEN.PORT)

  IndexationScheduler.Instance.enable()

  try {
    await IndexationScheduler.Instance.initIndexes()
  } catch (err) {
    logger.error('Cannot init videos index.', { err })
    process.exit(-1)
  }

  IndexationScheduler.Instance.execute()
    .catch(err => logger.error('Cannot run video indexer', { err }))
})
