import Vue from 'vue'

export default {
  methods: {
    buildImgUrl: function (imageName) {
      return process.env.BASE_URL + 'img/' + imageName
    },

    buildPublicUrl: function (name) {
      return process.env.BASE_URL + name
    },

    buildRoute: function (route) {
      if (Vue.config.localePath) return '/' + Vue.config.localePath + route

      return route
    },

    hasHashInURL (hash) {
      return window.location.hash === `#${hash}`
    },

    formatCurrency (amount) {
      if (isNaN(amount)) return ''

      const options = { style: 'currency', currency: 'EUR', minimumFractionDigits: 0, maximumFractionDigits: 0 }
      return new Intl.NumberFormat(this.getShortLocale(), options).format(amount)
    },

    getShortLocale () {
      return this.$language.current.split('_')[0]
    },

    buildArticles (ctx) {
      return ctx.keys()
        .map(ctx)
        .map(a => {
          a.id = a.attributes.id
          a.title = a.attributes.title
          a.mastodon = a.attributes.mastodon
          a.twitter = a.attributes.twitter
          a.date = new Date(a.attributes.date)

          return a
        })
        .sort((a, b) => {
          if (a.date < b.date) return 1
          if (a.date > b.date) return -1
          return 0
        })
    }
  }
}
