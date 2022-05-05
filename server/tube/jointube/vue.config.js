const markdownIt = require('./src/news/news.markdownit')

module.exports = {
  publicPath: process.env.CI_JOB_NAME === 'pages' ? '/joinpeertube/' : '/',

  pluginOptions: {
    prerenderSpa: {
      registry: undefined,
      renderRoutes: [
        '/',
        '/help',
        '/news',
        '/instances',
        '/hall-of-fame',
        '/faq',
        '/roadmap',
        '/contents-selection',
        '/thank-you',
        '/en_US',
        '/fr_FR',
        '/de',
        '/es',
        '/it',
        '/pl',
        '/pt_BR',
        '/ru',
        '/sv',
        '/en',
        '/fr',
        '/pt'
      ],
      useRenderEvent: true,
      headless: true,
      onlyProduction: true
    }
  },

  chainWebpack (config) {
    config.module.rule('md')
      .test(/\.md/)
      .use('frontmatter-markdown-loader')
      .loader('frontmatter-markdown-loader')
      .options({
        markdownIt
      })
  }
}
