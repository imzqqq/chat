import Vue from 'vue'
import VueMatomo from 'vue-matomo'
import VueRouter from 'vue-router'
import GetTextPlugin from 'vue-gettext'
import VueMeta from 'vue-meta'

import App from './App.vue'
import Home from './views/Home.vue'
import Help from './views/Help'
import Instances from './views/Instances'
import NotFound from './views/NotFound'
import AllContentsSelection from './views/All-Contents-Selection'
import Roadmap2020 from './views/Roadmap-2020'
import Roadmap2020Placeholder from './views/Roadmap-2020-Placeholder'
import ThankYou from './views/Thank-You'

import './scss/main.scss'
import CommonMixins from './mixins/CommonMixins'

Vue.use(VueRouter)

// ############# I18N ##############

const availableLanguages = {
  'en_US': 'English',
  'fr_FR': 'Français',
  'ar': 'العربية',
  'de': 'Deutsch',
  'es': 'Español',
  'eo': 'Esperanto',
  'gd': 'Gàidhlig',
  'it': 'Italiano',
  'pl': 'Polski',
  'pt_BR': 'Português',
  'ru': 'русский',
  'sq': 'Shqip',
  'sv': 'svenska',
  'hu': 'magyar',
  'gl': 'galego',
  'ja': '日本語',
  'zh_Hant': '繁體中文（台灣）'
}
const aliasesLanguages = {
  'en': 'en_US',
  'fr': 'fr_FR',
  'pt': 'pt_BR'
}
const allLocales = Object.keys(availableLanguages).concat(Object.keys(aliasesLanguages))

const defaultLanguage = 'en_US'
let currentLanguage = defaultLanguage

const basePath = process.env.BASE_URL
const startRegexp = new RegExp('^' + basePath)

const paths = window.location.pathname
  .replace(startRegexp, '')
  .split('/')

const localePath = paths.length !== 0 ? paths[0] : ''
let languageFromLocalStorage

try {
  languageFromLocalStorage = localStorage.getItem('language')
} catch (err) {
  console.error('Cannot fetch current language from local storage.', err)
}

if (allLocales.includes(localePath)) {
  Vue.config.localePath = localePath

  currentLanguage = aliasesLanguages[localePath] ? aliasesLanguages[localePath] : localePath

  try {
    localStorage.setItem('language', currentLanguage)
  } catch (err) {
    console.error('Cannot set language in local storage.', err)
  }
} else if (languageFromLocalStorage) {
  currentLanguage = languageFromLocalStorage
} else {
  const navigatorLanguage = window.navigator.userLanguage || window.navigator.language
  const snakeCaseLanguage = navigatorLanguage.replace('-', '_')
  currentLanguage = aliasesLanguages[snakeCaseLanguage] ? aliasesLanguages[snakeCaseLanguage] : snakeCaseLanguage
}

Vue.filter('translate', value => {
  return value ? Vue.prototype.$gettext(value.toString()) : ''
})

const p = buildTranslationsPromise(defaultLanguage, currentLanguage)

p.catch(err => {
  console.error('Cannot load translations.', err)
  return { default: {} }
}).then(translations => {
  Vue.use(GetTextPlugin, {
    translations,
    availableLanguages,
    defaultLanguage: 'en_US',
    silent: process.env.NODE_ENV === 'production' || currentLanguage === 'en_US'
  })

  Vue.config.language = currentLanguage

  // ###########################

  Vue.use(VueMeta)

  Vue.mixin(CommonMixins)

  const HallOfFame = () => import('./views/Hall-Of-Fame')
  const News = () => import('./views/News')
  const NewsArchive = () => import('./views/News-Archive')
  const AllPluginsSelection = () => import('./views/All-Plugins-Selection')
  const FAQ = () => import('./views/FAQ')

  let routes = []

  for (const locale of [ '' ].concat(allLocales)) {
    const base = locale
      ? '/' + locale + '/'
      : '/'

    routes = routes.concat([
      {
        path: '/' + locale,
        component: Home
      },
      {
        path: base + 'roadmap-2020',
        component: Roadmap2020
      },
      {
        path: base + 'roadmap',
        component: Roadmap2020Placeholder
      },
      {
        path: base + 'help',
        component: Help
      },
      {
        path: base + 'news',
        component: News
      },
      {
        path: base + 'news-archive',
        component: NewsArchive
      },
      {
        path: base + 'instances',
        component: Instances
      },
      {
        path: base + 'hall-of-fame',
        component: HallOfFame
      },
      {
        path: base + 'faq',
        component: FAQ
      },
      {
        path: base + 'contents-selection',
        component: AllContentsSelection
      },
      {
        path: base + 'plugins-selection',
        component: AllPluginsSelection
      },
      {
        path: base + 'thank-you',
        component: ThankYou
      }
    ])
  }

  // To generate a sitemap
  // console.log(routes.map(r => 'https://joinpeertube.org' + r.path))

  routes.push({
    path: '/404',
    component: NotFound
  })

  routes.push({
    path: '*',
    redirect: '/404'
  })

  // Don't use scroll behaviour with crawlers
  // See https://github.com/dansebcar/vue-router-2042
  let scrollBehavior
  if (
    window.location.host !== 'webcache.googleusercontent.com' && // Don't break google cache
    /bot|googlebot|crawler|spider|robot|crawling/i.test(navigator.userAgent) === false
  ) {
    scrollBehavior = (to, from, savedPosition) => {
      if (to.hash) {
        return { selector: to.hash }
      }

      return { x: 0, y: 0 }
    }
  } else {
    console.log('Disabling scroll behaviour.')
  }

  const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes,
    scrollBehavior
  })

  // Stats Matomo
  if (!(navigator.doNotTrack === 'yes' ||
    navigator.doNotTrack === '1' ||
    navigator.msDoNotTrack === '1' ||
    window.doNotTrack === '1')
  ) {
    Vue.use(VueMatomo, {
      // Configure your matomo server and site
      host: 'https://stats.framasoft.org/',
      siteId: 68,

      // Enables automatically registering pageviews on the router
      router,

      // Require consent before sending tracking information to matomo
      // Default: false
      requireConsent: false,

      // Whether to track the initial page view
      // Default: true
      trackInitialView: true,

      // Changes the default .js and .php endpoint's filename
      // Default: 'piwik'
      trackerFileName: 'p',

      enableLinkTracking: true
    })

    const _paq = _paq || [] // eslint-disable-line

    // CNIL conformity
    _paq.push([ function piwikCNIL () {
      const self = this

      function getOriginalVisitorCookieTimeout () {
        const now = new Date()
        const nowTs = Math.round(now.getTime() / 1000)
        const visitorInfo = self.getVisitorInfo()
        const createTs = parseInt(visitorInfo[2], 10)
        const cookieTimeout = 33696000 // 13 months in seconds
        return (createTs + cookieTimeout) - nowTs
      }

      this.setVisitorCookieTimeout(getOriginalVisitorCookieTimeout())
    } ])
  }

  new Vue({ // eslint-disable-line no-new
    el: '#app',
    router,
    render: h => h(App),

    mounted () {
      // You'll need this for renderAfterDocumentEvent.
      document.dispatchEvent(new Event('render-event'))

      document.dispatchEvent(new Event('x-app-rendered'))
    }
  })
})

function buildTranslationsPromise (defaultLanguage, currentLanguage) {
  const translations = {}
  const oldTranslationKeys = [ 'translations-v1-', 'translations-v2-' ]
  const translationKey = 'translations-v3-'

  // No need to translate anything
  if (currentLanguage === defaultLanguage) return Promise.resolve(translations)

  // Cleanup
  try {
    for (const key of oldTranslationKeys) {
      localStorage.removeItem(key + currentLanguage)
    }
  } catch (err) {
    console.error('Cannot remove old translations keys')
  }

  // Fetch translations from server
  const fromRemote = import('./translations/' + currentLanguage + '.json')
    .then(module => {
      const remoteTranslations = module.default
      try {
        localStorage.setItem(translationKey + currentLanguage, JSON.stringify(remoteTranslations))
      } catch (err) {
        console.error('Cannot save translations in local storage.', err)
      }

      return Object.assign(translations, remoteTranslations)
    })

  // If we have a cache, try to
  try {
    const fromLocalStorage = localStorage.getItem(translationKey + currentLanguage)

    if (fromLocalStorage) {
      Object.assign(translations, JSON.parse(fromLocalStorage))

      return Promise.resolve(translations)
    }
  } catch (err) {
    console.error('Cannot parse translations from local storage.', err)
  }

  return fromRemote
}
