<template>
  <main id="news">
    <div class="blocks">
      <div class="title-block">
        <h1 v-translate>Tube news!</h1>

        <p>
          <span v-translate>Discover the latest Tube improvements</span>
        </p>

        <span class="icon-row">
          <a :title="getNewsletterTitle()" href="https://framalistes.org/sympa/subscribe/tube-newsletter" target="_blank">
            <span class="sr-only">{{ getNewsletterTitle() }}</span>

            <icon-envelope></icon-envelope>
          </a>

          <a :title="getSyndicationTitle()" :href="getSyndicationLink()" target="_blank">
            <span class="sr-only">{{ getSyndicationLink() }}</span>

            <icon-RSS></icon-RSS>
          </a>

          <a :title="getMastodonTitle()" href="https://framapiaf.org/@tube" target="_blank" class="icon-mastodon">
            <span class="sr-only">{{ getMastodonTitle() }}</span>

            <icon-mastodon></icon-mastodon>
          </a>

          <a :title="getTwitterTitle()" href="https://twitter.com/joinpeertube" target="_blank">
            <div class="sr-only">{{ getTwitterTitle() }}</div>

            <icon-twitter></icon-twitter>
          </a>
        </span>

        <div class="separator"></div>
      </div>

      <NewsArticles :articles="articles"></NewsArticles>

      <div class="bottom">
        <router-link to="/news-archive" class="jpt-router-link">
          <translate>See older news</translate>
        </router-link>
      </div>
    </div>
  </main>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';

  #news {
    .icon-mastodon {
      width: 22px;
      color: #424242 !important;

      svg {
        width: 100%;
        height: 100%;
      }
    }

    .blocks {
      margin: 60px auto;
    }

    .title-block {
      margin-bottom: 60px;

      a {
        border-bottom: none;
        font-weight: $font-semibold;
        color: #000;
      }

      p > span {
        display: block;
      }

      .icon-row {
        display: flex;
        justify-content: space-evenly;
        width: 50%;
        font-size: 130%;
      }
    }

    .bottom {
      display: flex;
      justify-content: center;
    }
  }
</style>

<script>
  import IconEnvelope from '../components/icons/IconEnvelope'
  import IconMastodon from '../components/icons/IconMastodon'
  import IconTwitter from '../components/icons/IconTwitter'
  import IconRSS from '../components/icons/IconRSS'
  import NewsArticles from '../components/NewsArticles'

  const archiveMeta = require('../news/archives/meta.json')

  export default {
    components: {
      IconEnvelope,
      IconRSS,
      IconTwitter,
      IconMastodon,
      NewsArticles
    },

    data: function () {
      return {
        articles: []
      }
    },

    mounted () {
      const archivedIds = archiveMeta.map(o => '#' + o.id)

      if (window.location.hash && archivedIds.includes(window.location.hash)) {
        this.$router.push({ path: '/news-archive', hash: window.location.hash })
        return
      }

      this.getNews()
    },

    methods: {
      getCurrentLocale () {
        return this.$language.current.toLowerCase().split('_')[0]
      },

      getNews () {
        // Keep static path in require.context so webpack knows which directory it can import
        const ctx = this.getCurrentLocale() === 'fr'
          ? require.context('../news/fr', true, /\.md$/)
          : require.context('../news/en', true, /\.md$/)

        this.articles = this.buildArticles(ctx)
      },

      getNewsletterTitle () {
        return this.$gettext('Subscribe to the newsletter')
      },

      getSyndicationLink () {
        return '/rss-' + this.getCurrentLocale() + '.xml'
      },

      getSyndicationTitle () {
        return this.$gettext('Subscribe to RSS feed')
      },

      getMastodonTitle () {
        return this.$gettext('Subscribe to our Mastodon account')
      },

      getTwitterTitle () {
        return this.$gettext('Subscribe to our Twitter account')
      },

      getMastodonTootTitle () {
        return this.$gettext('Open the Toot')
      },

      getTwitterTweetTitle () {
        return this.$gettext('Open the Tweet')
      }
    },

    metaInfo: function () {
      return {
        title: this.$gettext('Tube news')
      }
    }
  }
</script>
