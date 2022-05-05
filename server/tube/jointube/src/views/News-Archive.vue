<template>
  <main id="news-archive">
    <div class="blocks">
      <div class="title-block">
        <h1 v-translate>Tube news archive</h1>
      </div>

      <div class="go-back">
        <router-link to="/news" class="jpt-router-link">Go back to the latest news</router-link>
      </div>

      <NewsArticles :articles="articles"></NewsArticles>
    </div>
  </main>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';

  #news-archive {
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
    }

    .go-back {
      display: flex;
      justify-content: center;
      margin-bottom: 30px;
    }
  }
</style>

<script>
  import NewsArticles from '../components/NewsArticles'

  export default {
    components: {
      NewsArticles
    },

    data: function () {
      return {
        articles: []
      }
    },

    mounted () {
      this.getNews()
    },

    methods: {
      getNews () {
        const currentLocale = this.$language.current.toLowerCase().split('_')[0]

        // Keep static path in require.context so webpack knows which directory it can import
        const ctx = currentLocale === 'fr'
          ? require.context('../news/archives/fr', true, /\.md$/)
          : require.context('../news/archives/en', true, /\.md$/)

        this.articles = this.buildArticles(ctx)
      }
    },

    metaInfo: function () {
      return {
        title: this.$gettext('Tube news archive')
      }
    }
  }
</script>
