<template>

  <div class="news-article">
    <article class="news" :id="article.id" v-for="article in articles" v-bind:key="article.id">
      <a class="title" :href="'#' + article.id">{{ article.title }}</a>

      <div class="under-title">
        <span class="dt-published">{{ article.date.toLocaleDateString() }}</span>

        <span v-if="article.mastodon">·</span>
        <a v-if="article.mastodon" :title="getMastodonTootTitle()" :href="article.mastodon" target="_blank">
          <i class="fa fa-mastodon" aria-hidden="true"></i>
        </a>

        <span v-if="article.twitter">·</span>
        <a v-if="article.twitter" :title="getTwitterTweetTitle()" :href="article.twitter" target="_blank">
          <i class="fa fa-twitter" aria-hidden="true"></i>
        </a>
      </div>

      <div class="body" v-on:click="onNewsClick($event)" v-html="article.html"></div>
    </article>

    <div id="modal" @click="closeModal()" v-bind:class="{ 'hide-modal': hideModal }">
      <img loading="lazy" :src="modalImgSrc" />

      <div class="caption">{{ modalCaption }}</div>
    </div>
  </div>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';
  @import '../scss/_mixins.scss';

  .news-article {
    p {
      margin-bottom: 10px;
    }

    p + *:not(ul),
    ul + *:not(ul) {
      margin-top: 1em;
    }

    .news {
      padding: 40px 50px;
      border: solid 1px #d9d9d9;
      border-left: 6px solid $orange;
      box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.35);
      margin-bottom: 60px;

      .title {
        @include proza-semi-bold;

        font-size: 32px;
        color: #000 !important;
      }

      .under-title {
        font-size: 16px;
        color: #70767d !important;

        span:not(:first-of-type),
        a {
          margin-left: .5rem;
          color: #70767d !important;
        }
      }

      .body {
        margin-top: 40px;
      }

      a {
        word-break: break-word;
      }

      h3 {
        font-size: 26px;
        margin-top: 1.5em;
      }

      img + h4,
      figure + h4,
      img + h3,
      figure + h3 {
        margin-top: 1.2rem;
      }

      img {
        cursor: pointer;
        max-width: 100%;
        height: auto;
      }

      figure {
        margin: 30px 0;
        border: 10px solid #fff3ea;
        border-bottom: 0;

        figcaption {
          font-size: 14px;
          background: #fff3ea;
          text-align: center;
          padding: 10px 0;
          margin-top: -10px;
        }
      }

      @media screen and (max-width: $small-screen) {
        padding: 10px;
      }
    }

    #modal {
      &.hide-modal {
        display: none;
      }

      position: fixed;
      z-index: 1;
      padding-top: 5vh;
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      overflow: auto;
      background-color: rgb(0, 0, 0);
      background-color: rgba(0, 0, 0, 0.9);

      img {
        margin: auto;
        display: block;
        height: auto;
        width: auto;
        max-height: 90%;
      }

      .caption {
        margin: auto;
        display: block;
        width: 80%;
        max-width: 700px;
        text-align: center;
        color: #ccc;
        padding: 10px;
      }

      img,
      .caption {
        animation-name: zoom;
        animation-duration: 0.6s;
      }
    }
  }
</style>

<script>
  export default {
    components: {
    },

    props: {
      articles: Array
    },

    data: function () {
      return {
        modalImgSrc: '',
        modalCaption: '',
        hideModal: true
      }
    },

    methods: {
      onNewsClick (event) {
        if (event.target instanceof HTMLImageElement) {
          return this.openModal(event)
        }
      },

      openModal: function (event) {
        const img = event.target

        this.hideModal = false
        this.modalImgSrc = img.src
        this.modalCaption = img.alt
      },

      closeModal: function () {
        this.hideModal = true
        this.modalImgSrc = ''
        this.modalCaption = ''
      },

      getNewsletterTitle () {
        return this.$gettext('Subscribe to the newsletter')
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
        title: this.$gettext('News')
      }
    }
  }
</script>
