<template>
  <div>
    <header>
      <interface-language-dropdown class="interface-language-dropdown"></interface-language-dropdown>

      <h1>
        <span v-if="configLoaded && !titleImageUrl">{{indexName}}</span>

        <img class="title-image" :src="titleImageUrl" :alt="indexName" />
      </h1>

      <template v-if="!smallFormat">
        <h4>
          <div v-translate>A search engine of <a href="https://joinpeertube.org" target="_blank">Tube</a> videos, channels and playlists</div>

          <div v-translate>Developed by <a href="https://framasoft.org" target="_blank">Framasoft</a></div>
        </h4>

        <div class="search-home">
          <img v-if="searchImageUrl" :src="searchImageUrl" alt="">
        </div>
      </template>
    </header>
  </div>
</template>

<style lang="scss">
  @import '../scss/_variables';

  header {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    margin-bottom: 30px;
    font-family: monospace;

    .interface-language-dropdown {
      width: 20px;
      position: absolute;
      right: 10px;
      top: 10px;
    }

    .search-home {
      width: 300px;
      height: 250px;
      margin: 30px 0 0 0;

      img {
        width: inherit;
        height: inherit;
      }
    }

    h1 {
      font-size: 50px;
      // Because it's loaded dynamically
      min-height: 70px;
      margin: 0;
      text-align: center;
    }

    .title-image {
      max-width: 500px;
      height: 78px;
    }

    h4 {
      font-weight: normal;
      margin: 0;
      text-align: center;
      line-height: 25px;

      a {
        color: #000;
        font-weight: $font-semibold;
      }
    }

    @media screen and (max-width: $small-screen) {
      h1 {
        font-size: 30px;
        margin-top: 20px;
        margin-bottom: 10px;
        min-height: unset;
      }

      h4 {
        font-size: 15px !important;
        line-height: initial;

        div:first-child {
          margin-bottom: 5px;
        }
      }

      img {
        width: 100%;
        max-width: 300px;
      }
    }
  }

  @media screen and (max-height: 400px) {
    .search-home {
      display: none;
    }
  }
</style>

<script lang="ts">
  import { defineComponent } from 'vue'
  import { getConfig } from '../shared/config'
  import { buildApiUrl } from '../shared/utils'
  import InterfaceLanguageDropdown from './InterfaceLanguageDropdown.vue'

  export default defineComponent({
    components: {
      'interface-language-dropdown': InterfaceLanguageDropdown
    },

    data () {
      return {
        configLoaded: false,
        titleImageUrl: '',
        searchImageUrl: ''
      }
    },

    props: {
      indexName: String,
      smallFormat: Boolean,
    },

    async mounted () {
      const config = await getConfig()

      this.titleImageUrl = config.searchInstanceNameImage
        ? buildApiUrl(config.searchInstanceNameImage)
        : ''

      this.searchImageUrl = config.searchInstanceSearchImage
        ? buildApiUrl(config.searchInstanceSearchImage)
        : buildApiUrl('/img/search-home.png')

      this.configLoaded = true
    }
  })
</script>
