const markdownIt = require('markdown-it')
const iterator = require('markdown-it-for-inline')
const replaceLink = require('markdown-it-replace-link')

function lazyLoadingPlugin(md, options) {
  md.renderer.rules.image = function (tokens, idx, options, env, self) {
    const token = tokens[idx]

    const src = token.attrs[token.attrIndex('src')][1]
    const title = token.attrIndex('title') >= 0
      ? token.attrs[token.attrIndex('title')][1]
      : ''

    const titleElem = title
      ? `<figcaption>${title}</figcaption>`
      : ''

    return '<figure>\n' +
           `  <img loading="lazy" src="${src}" title="${title}" alt="${title}" />` +
           `  ${titleElem}` +
           '</figure>\n'
  }
}

module.exports = markdownIt({
  html: true,
  linkify: true,
  breaks: true
})
  .use(iterator, 'url_new_win', 'link_open', function (tokens, idx) {
    // adding target="_blank"
    tokens[idx].attrSet('target', '_blank')
    tokens[idx].attrSet('rel', 'noopener noreferrer')
  })
  .use(lazyLoadingPlugin)
  .use(replaceLink)
