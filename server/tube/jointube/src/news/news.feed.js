const path = require('path')
const fs = require('fs-extra')
const Feed = require('feed').Feed
const newsUtils = require('./news.utils')
const markdownIt = require('./news.markdownit')

const options = {
  title: 'Tube news!',
  description: 'Discover the latest Tube improvements',
  link: 'https://joinpeertube.org',
  image: 'https://joinpeertube.org/img/card-opengraph.jpg',
  favicon: 'https://joinpeertube.org/img/icons/favicon.png',
  copyright: 'Tube news! content © 2021, Framasoft, licenced under CC-BY-SA 4.0',
  updated: new Date(),
  generator: 'Framasoft',
  author: {
    name: 'Framasoft',
    email: 'contact@framasoft.org',
    link: 'https://contact.framasoft.org'
  }
}

function regenRSS (language) {
  const feed = new Feed({
    title: options.title,
    description: options.description,
    id: options.link,
    link: options.link,
    language,
    image: options.image,
    favicon: options.favicon,
    copyright: options.copyright,
    updated: options.updated,
    generator: options.generator,
    author: options.author
  })

  getArticles(language).forEach(article => {
    feed.addItem({
      title: article.title,
      id: `${options.link}/news#${article.id}`,
      link: `${options.link}/news#${article.id}`,
      content: markdownIt.render(article.body),
      date: new Date(article.date)
    })
  })

  const outputPath = 'dist/rss-' + language + '.xml'
  fs.writeFileSync(outputPath, feed.rss2())
}

function getArticles (language) {
  return newsUtils.requireArticlesFromDir(path.join(__dirname, language))
    .concat(newsUtils.requireArticlesFromDir(path.join(__dirname, 'archives', language)))
    .sort((a, b) => {
      if (a.date < b.date) return 1
      if (a.date > b.date) return -1
      return 0
    })
}

regenRSS('en')
regenRSS('fr')
