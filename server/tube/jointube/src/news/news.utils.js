const fm = require('front-matter')
const fs = require('fs-extra')
const path = require('path')

function requireArticlesFromDir (directory) {
  return fs.readdirSync(directory)
    .map(file => fs.readFileSync(path.join(directory, file), 'utf8').toString())
    .map(content => fm(content))
    .map(data => {
      data.id = data.attributes.id
      data.title = data.attributes.title
      data.date = new Date(data.attributes.date)

      return data
    })
}

module.exports = {
  requireArticlesFromDir
}
