const path = require('path')
const fs = require('fs-extra')
const newsUtils = require('./news.utils')

function getArchiveMeta () {
  return newsUtils.requireArticlesFromDir(path.join(__dirname, 'archives', 'en'))
    .map(a => ({ id: a.id }))
}

function regenArchiveMeta () {
  const json = getArchiveMeta()

  fs.writeJsonSync(path.join(__dirname, 'archives', 'meta.json'), json)
}

regenArchiveMeta()
