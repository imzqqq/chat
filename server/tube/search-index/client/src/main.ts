import './scss/main.scss'
import { createApp } from 'vue'
import VueMatomo from 'vue-matomo'
import App from './App.vue'
import Search from './views/Search.vue'
import { createRouter, createWebHistory } from 'vue-router'
import { createGettext, useGettext } from '@jshmrtn/vue3-gettext'

const app = createApp(App)

// ############# I18N ##############

const availableLanguages = {
  en_US: 'English',
  fr_FR: 'Français',
  cs: 'Čeština',
  de: 'Deutsch',
  el: 'ελληνικά',
  es: 'Español',
  gd: 'Gàidhlig',
  gl: 'galego',
  ru: 'русский',
  it: 'Italiano',
  ja: '日本語',
  sv: 'svenska',
  nl: 'Nederlands',
  oc: 'Occitan',
  sq: 'Shqip',
  pt_BR: 'Português (Brasil)',
  bn: 'বাংলা',
  pl: 'Polski',
  uk: 'Українська'
}
const aliasesLanguages = {
  en: 'en_US',
  fr: 'fr_FR',
  br: 'pt_BR',
  pt: 'pt_BR'
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
const languageFromLocalStorage = localStorage.getItem('language')

if (allLocales.includes(localePath)) {
  currentLanguage = aliasesLanguages[localePath] ? aliasesLanguages[localePath] : localePath
  localStorage.setItem('language', currentLanguage)
} else if (languageFromLocalStorage) {
  currentLanguage = languageFromLocalStorage
} else {
  const navigatorLanguage = (window.navigator as any).userLanguage || window.navigator.language
  const snakeCaseLanguage = navigatorLanguage.replace('-', '_')
  currentLanguage = aliasesLanguages[snakeCaseLanguage] ? aliasesLanguages[snakeCaseLanguage] : snakeCaseLanguage
}

buildTranslationsPromise(defaultLanguage, currentLanguage)
  .catch(err => {
    console.error('Cannot load translations.', err)

    return { default: {} }
  })
  .then(translations => {
    const gettext = createGettext({
      translations,
      availableLanguages,
      defaultLanguage: currentLanguage,
      mutedLanguages: [ 'en_US' ]
    })

    app.use(gettext)

    // Routes
    let routes = []

    for (const locale of [ '' ].concat(allLocales)) {
      const base = locale
        ? '/' + locale + '/'
        : '/'

      routes = routes.concat([
        {
          path: base,
          component: Search
        },
        {
          path: base + 'search',
          component: Search
        }
      ])
    }

    routes.push({
      path: '/*',
      // Don't want to create a 404 page
      component: Search
    })

    const router = createRouter({
      history: createWebHistory(),
      routes
    })

    // Stats Matomo
    if (!(navigator.doNotTrack === 'yes' ||
      navigator.doNotTrack === '1' ||
      window.doNotTrack === '1')
    ) {
      app.use(VueMatomo, {
        // Configure your matomo server and site
        host: 'https://stats.framasoft.org/',
        siteId: 77,

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

      const _paq = []

      // CNIL conformity
      _paq.push([ function piwikCNIL () {
        // @ts-expect-error
        const self = this

        function getOriginalVisitorCookieTimeout () {
          const now = new Date()
          const nowTs = Math.round(now.getTime() / 1000)
          const visitorInfo = self.getVisitorInfo()
          const createTs = parseInt(visitorInfo[2], 10)
          const cookieTimeout = 33696000 // 13 months in seconds
          return (createTs + cookieTimeout) - nowTs
        }

        // @ts-expect-error
        this.setVisitorCookieTimeout(getOriginalVisitorCookieTimeout())
      } ])
    }

    app.use(router)

    app.mount('#app')
  })
  .catch(err => console.error(err))

function buildTranslationsPromise (defaultLanguage, currentLanguage) {
  const translations = {}

  // No need to translate anything
  if (currentLanguage === defaultLanguage) return Promise.resolve(translations)

  // Fetch translations from server
  const fromRemote = import('./translations/' + currentLanguage + '.json')
    .then(module => {
      const remoteTranslations = module.default
      try {
        localStorage.setItem('translations-v1-' + currentLanguage, JSON.stringify(remoteTranslations))
      } catch (err) {
        console.error('Cannot save translations in local storage.', err)
      }

      return Object.assign(translations, remoteTranslations)
    })

  // If we have a cache, try to
  const fromLocalStorage = localStorage.getItem('translations-v1-' + currentLanguage)
  if (fromLocalStorage) {
    try {
      Object.assign(translations, JSON.parse(fromLocalStorage))

      return Promise.resolve(translations)
    } catch (err) {
      console.error('Cannot parse translations from local storage.', err)
    }
  }

  return fromRemote
}
