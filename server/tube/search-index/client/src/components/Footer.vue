<template>
  <footer id="main-footer">

    <div class="header">
      <img src="/img/bottom-tube-logo-v3.svg" alt="">

      <div v-translate class="description">A free software to take back control of your videos</div>
    </div>

    <div class="columns">
      <div>
        <div v-translate class="subtitle">Open your own videos website with Tube!</div>

        <a target="_blank" v-translate href="https://tube.docs.imzqqq.top/#/install-any-os">Install Tube</a>

        <a target="_blank" v-translate href="https://joinpeertube.org#what-is-tube">Why should I have my own Tube website?</a>
      </div>

      <div>
        <div v-translate class="subtitle">Create an account to take back control of your videos</div>

        <a target="_blank" v-translate href="https://joinpeertube.org/instances">Open an account on a Tube website</a>

        <a target="_blank" v-translate href="https://tube.docs.imzqqq.top/#/use-library?id=playlist">Create playlists</a>
      </div>
    </div>

    <div class="big-link">
      <a href="https://joinpeertube.org" target="_blank" v-translate> &gt;&gt; Check all guides on joinpeertube.org &lt;&lt; </a>
    </div>

    <div class="footer">
      <a v-translate href="https://framagit.org/framasoft/tube/search-index/" target="_blank">Source code</a>

      <a v-if="legalNoticesUrl" v-translate :href="legalNoticesUrl" target="_blank">Legal notices</a>
    </div>
  </footer>
</template>

<style lang="scss">
  @import '../scss/_variables';

  footer {
    width: $container-width;
    margin: auto;
    background-color: $orange-lighten;
    padding: 30px 100px 10px 100px;
    font-family: monospace;

    .header {
      margin: 50px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;

      img {
        width: 100%;
        max-width: 500px;
        margin-bottom: 20px;
      }
    }

    .description {
      color: $brown;
      text-align: center;
    }

    .columns {
      display: flex;

      > div {
        display: flex;
        flex-direction: column;
        text-align: center;

        &:first-child {
          margin-right: 100px;
        }
      }

      @media screen and (max-width: $container-width) {
        flex-direction: column;

        > div {
          margin-top: 50px;
          margin-right: 0 !important;
        }
      }
    }

    .columns a,
    .big-link a {
      color: $orange;
      margin: 0 auto 20px auto;
      min-height: 40px;
      display: flex;
      width: fit-content;

      &:hover {
        color: $orange-darken;
      }
    }

    .subtitle {
      color: $brown;
      font-size: 20px;
      margin-bottom: 50px;
    }

    .big-link {
      margin-top: 50px;

      a {
        font-size: 20px;
        font-weight: bold;
        text-decoration: none;
        text-align: center;
      }
    }

    .footer {
      margin-top: 80px;
      display: flex;
      justify-content: center;

      a {
        color: $orange;
        font-size: 10px;
        margin-right: 10px;
      }
    }

    @media screen and (max-width: $container-width) {
      width: 100%;
    }

    @media screen and (max-width: $small-view) {
      padding: 30px;

      .title {
        font-size: 20px;
      }

      .subtitle {
        font-size: 16px;
      }

      .footer a {
        font-size: 16px;
      }
    }
  }
</style>

<script>
  import { defineComponent } from 'vue'
  import { getConfig } from '../shared/config'

  export default defineComponent({
    data () {
      return {
        legalNoticesUrl: ''
      }
    },

    async mounted () {
      const config = await getConfig()

      this.legalNoticesUrl = config.legalNoticesUrl
    }
  })
</script>
