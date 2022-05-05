<template>
  <div>
    <my-header v-bind:indexName="indexName" v-bind:smallFormat="searchDone"></my-header>
    <div v-if="searchDone" class="header-left-logo">
      <img src="/img/tube-logo.svg" alt="">
    </div>

    <main>
      <form id="search-anchor" class="search-container" v-bind:class="{ 'search-container-small': !searchDone }" role="search" onsubmit="return false;">
        <input v-bind:placeholder="inputPlaceholder" autofocus  type="text" v-model="formSearch" name="search" autocapitalize="off" autocomplete="off" autocorrect="off" maxlength="1024" />

        <button v-on:click="doNewSearch()">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="11" cy="11" r="8"></circle>
            <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
          </svg>

          <translate v-if="formSearch">Go!</translate>
          <translate v-else>Explore!</translate>
        </button>
      </form>

      <h3 v-if="!searchDone" v-bind:class="{ 'none-opacity': !instancesCount }" v-translate="{ instancesCount: instancesCount, indexedInstancesUrl: indexedInstancesUrl, indexName: indexName }">
        Search for your favorite videos, channels and playlists on <a href="%{indexedInstancesUrl}" target="_blank">%{instancesCount} Tube websites</a> indexed by %{indexName}!
      </h3>

      <search-warning class="search-warning" v-bind:indexName="indexName" v-bind:highlight="!searchDone"></search-warning>

      <div class="results" v-if="searchDone">
        <div class="filters">
          <div class="button-rows">
            <button class="filters-button tube-button" v-on:click="toggleFilters()">
              <translate>Filters</translate>
              &nbsp;
              <span v-if="activeFilters">({{ activeFilters }})</span>

              <div class="arrow-down"></div>
            </button>

            <div class="sort-select">
              <label for="sort" v-translate>Sort by:</label>
              <div class="sort-select select-container">
                <select id="sort" name="sort" v-model="formSort">
                  <option v-translate value="-match">Best match</option>
                  <option v-translate value="-publishedAt">Most recent</option>
                  <option v-translate value="publishedAt">Least recent</option>
                </select>
              </div>
            </div>
          </div>

          <form v-if="displayFilters" class="filters-content">
            <div class="form-group small-height">
              <div class="radio-label label-container">
                <label v-translate>Display sensitive content</label>

                <button v-translate class="reset-button" v-on:click="resetField('nsfw')" v-if="formNSFW !== undefined">
                  Reset
                </button>
              </div>

              <div class="radio-container">
                <input type="radio" name="sensitiveContent" id="sensitiveContentYes" value="both" v-model="formNSFW">
                <label v-translate for="sensitiveContentYes" class="radio">Yes</label>
              </div>

              <div class="radio-container">
                <input type="radio" name="sensitiveContent" id="sensitiveContentNo" value="false" v-model="formNSFW">
                <label v-translate for="sensitiveContentNo" class="radio">No</label>
              </div>
            </div>

            <div class="form-group small-height">
              <div class="radio-label label-container">
                <label v-translate>Display only</label>

                <button v-translate class="reset-button" v-on:click="resetField('isLive')" v-if="formIsLive !== undefined">
                  Reset
                </button>
              </div>

              <div class="radio-container">
                <input type="radio" name="isLive" id="isLiveYes" value="both" v-model="formIsLive">
                <label v-translate for="isLiveYes" class="radio">Live videos</label>
              </div>

              <div class="radio-container">
                <input type="radio" name="isLive" id="isLiveNo" value="false" v-model="formIsLive">
                <label v-translate for="isLiveNo" class="radio">VOD videos</label>
              </div>
            </div>

            <div class="form-group">
              <div class="radio-label label-container">
                <label v-translate>Published date</label>

                <button v-translate class="reset-button" v-on:click="resetField('publishedDateRange')" v-if="formPublishedDateRange !== undefined">
                  Reset
                </button>
              </div>

              <div class="radio-container" v-for="date in publishedDateRanges" :key="date.id">
                <input type="radio" name="publishedDateRange" v-bind:id="date.id" v-bind:value="date.id" v-model="formPublishedDateRange">
                <label v-bind:for="date.id" class="radio">{{ date.label }}</label>
              </div>
            </div>

            <div class="form-group">
              <div class="radio-label label-container">
                <label v-translate>Duration</label>
                <button v-translate class="reset-button" v-on:click="resetField('durationRange')" v-if="formDurationRange !== undefined">
                  Reset
                </button>
              </div>

              <div class="radio-container" v-for="duration in durationRanges" :key="duration.id">
                <input type="radio" name="durationRange" v-bind:id="duration.id" v-bind:value="duration.id" v-model="formDurationRange">
                <label v-bind:for="duration.id" class="radio">{{ duration.label }}</label>
              </div>
            </div>

            <div class="form-group">
              <label v-translate for="category">Category</label>
              <button v-translate class="reset-button" v-on:click="resetField('categoryOneOf')" v-if="formCategoryOneOf !== undefined">
                Reset
              </button>

              <div class="select-container">
                <select id="category" name="category" v-model="formCategoryOneOf">
                  <option v-translate v-bind:value="undefined">Display all categories</option>
                  <option v-for="category in videoCategories" v-bind:value="category.id" :key="category.id">{{ category.label }}</option>
                </select>
              </div>
            </div>

            <div class="form-group">
              <label v-translate for="licence">Licence</label>
              <button v-translate class="reset-button" v-on:click="resetField('licenceOneOf')" v-if="formLicenceOneOf !== undefined">
                Reset
              </button>

              <div class="select-container">
                <select id="licence" name="licence" v-model="formLicenceOneOf">
                  <option v-translate v-bind:value="undefined">Display all licenses</option>
                  <option v-for="licence in videoLicences" v-bind:value="licence.id" :key="licence.id">{{ licence.label }}</option>
                </select>
              </div>
            </div>

            <div class="form-group">
              <label v-translate for="language">Language</label>
              <button v-translate class="reset-button" v-on:click="resetField('languageOneOf')" v-if="formLanguageOneOf !== undefined">
                Reset
              </button>

              <div class="select-container">
                <select id="language" name="language" v-model="formLanguageOneOf">
                  <option v-translate v-bind:value="undefined">Display all languages</option>
                  <option v-for="language in videoLanguages" v-bind:value="language.id" :key="language.id">{{ language.label }}</option>
                </select>
              </div>
            </div>

            <div class="form-group">
              <label v-translate for="host">Tube instance</label>

              <input type="text" id="host" name="host" v-model="formHost" class="classic-input-text" />
            </div>

            <div class="form-group">
              <label v-translate for="tagsAllOf">All of these tags</label>
              <button v-translate class="reset-button" v-on:click="resetField('tagsAllOf')" v-if="formTagsAllOf.length !== 0">
                Reset
              </button>

              <vue-tags-input v-bind:placeholder="tagsPlaceholder" @tags-changed="newTags => formTagsAllOf = newTags" v-model="formTagAllOf" :tags="formTagsAllOf" />
            </div>

            <div class="form-group">
              <label v-translate for="tagsOneOf">One of these tags</label>
              <button v-translate class="reset-button" v-on:click="resetField('tagsOneOf')" v-if="formTagsOneOf.length !== 0">
                Reset
              </button>

              <vue-tags-input v-bind:placeholder="tagsPlaceholder" @tags-changed="newTags => formTagsOneOf = newTags" v-model="formTagOneOf" :tags="formTagsOneOf" />
            </div>

            <div class="button-block">
              <input class="tube-button" type="button" v-bind:value="applyFiltersLabel" v-on:click="doNewSearch()" />
            </div>

          </form>
        </div>

        <div id="results-anchor" class="results-summary">
          <span v-if="resultsCount === 0">
            <translate>No results found</translate></span>

          <span v-if="resultsCount">
            <translate :translate-n="resultsCount" translate-plural="%{resultsCount} results found">%{resultsCount} result found</translate>
          </span>
        </div>

        <div v-for="result in results" :key="getResultKey(result)">
          <video-result v-if="isVideo(result)" v-bind:video="result"></video-result>

          <channel-result v-else-if="isChannel(result)" v-bind:channel="result"></channel-result>

          <playlist-result v-else v-bind:playlist="result"></playlist-result>
        </div>

        <pagination v-bind:maxPage="getMaxPage()" v-bind:searchDone="searchDone" v-model="currentPage" v-bind:pages="pages"></pagination>
      </div>
    </main>
  </div>
</template>

<style lang="scss">
  @use 'sass:math';

  @import '../scss/_variables';

  main {
    margin: auto;
  }

  h3 {
    max-width: 600px;
    text-align: center;
    margin: 40px auto 100px auto;
    font-weight: normal;
    font-size: 16px;

    a {
      color: $orange-darken;

      &:hover {
        color: $orange;
      }
    }

    @media screen and (max-width: $small-screen) {
      font-size: 14px;
    }
  }

  .search-container {
    background-color: #fff;
    border-radius: 2px;
    position: relative;
    height: 45px;
    margin: auto;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);

    input[type=text] {
      background-color: transparent;
      outline: none;
      height: 35px;
      font-size: 15px;
      border: 0;
      width: 100%;
      height: 100%;
      color: #000;
      padding: 0 20px;
    }

    button {
      border-top-right-radius: 2px;
      border-bottom-right-radius: 2px;
      cursor: pointer;
      position: absolute;
      right: 0;
      background-color: $orange-darken;
      border: 0;
      color: #FFF;
      height: 100%;
      outline: 0;
      font-size: 15px;
      padding: 0 15px 0 10px;
      display: inline-flex;
      align-items: center;
      min-width: 100px;

      svg {
        margin-right: 10px;
      }

      &:hover {
        background-color: $orange;
      }
    }

    button + div {
      margin-top: 10px;
      font-size: 80%;
      text-align: right;
    }
  }

  .search-container-small {
    max-width: 500px;
    margin-top: 30px;
  }

  .header-left-logo {
    position: relative;
    display: block;
    height: 0;

    img {
      position: absolute;
      left: -50px;
      top: 0;
      width: 32px;
    }
  }

  .results-summary,
  .no-results {
    margin-top: 50px;
  }

  .results {
    border-top: 1px solid rgba(0, 0, 0, 0.1);
    padding-top: 20px;
  }

  .filters-content {
    display: flex;
    flex-wrap: wrap;
    margin-top: 20px;

    .form-group:nth-child(2n-1) {
      padding-right: 20px;
    }

    .form-group {
      width: 50%;
      min-height: 80px;
      display: inline-block;
      margin: 10px 0;
      font-size: 14px;
    }

    @media screen and (max-width: $small-view) {
      .form-group {
        width: 100%;
        padding-right: 0;
      }
    }

    .form-group > label,
    .label-container label {
      font-weight: $font-semibold;
    }

    .form-group > label,
    .label-container {
      display: inline-block;
      margin-bottom: 5px;
    }

    .form-group.small-height {
      min-height: 30px;
    }

    .radio-container {
      display: inline-block;
      margin: 0 10px 5px 0;
    }

    .button-block {
      width: 100%;
      text-align: right;
    }

    .radio-label {
      display: block;
    }

    .reset-button {
      background: none;
      border: none;
      font-weight: 600;
      font-size: 11px;
      opacity: 0.7;
      margin-left: 5px;
      cursor: pointer;
    }
  }

  .filters-button {
    position: relative;
    padding-right: 25px;

    .arrow-down {
      $size: 4px;

      position: absolute;
      right: 0;
      top: 50%;
      margin-top: math.div(-$size, 2);
      margin-right: 10px;
      width: 0;
      height: 0;
      border-left: $size solid transparent;
      border-right: $size solid transparent;

      border-top: $size solid #fff;
    }
  }

  .button-rows {
    display: flex;
    justify-content: space-between;

    .sort-select {
      display: flex;

      label {
        color: $grey;
        font-size: 14px;
        min-width: fit-content;
        margin: auto 5px auto 0;
      }

      .select-container {
        max-width: 150px;
      }
    }
  }

</style>

<script lang="ts">
  import { defineComponent } from 'vue'
  import Header from '../components/Header.vue'
  import SearchWarning from '../components/SearchWarning.vue'
  import VideoResult from '../components/VideoResult.vue'
  import ChannelResult from '../components/ChannelResult.vue'
  import PlaylistResult from '../components/PlaylistResult.vue'
  import { searchVideos, searchVideoChannels, searchVideoPlaylists } from '../shared/search'
  import { getConfig } from '../shared/config'
  import { pageToAPIParams, durationRangeToAPIParams, publishedDateRangeToAPIParams, extractTagsFromQuery, buildApiUrl } from '../shared/utils'
  import { SearchUrl } from '../models'
  import { EnhancedVideo } from '../../../server/types/video.model'
  import { EnhancedVideoChannel } from '../../../server/types/channel.model'
  import VueTagsInput from '@sipec/vue3-tags-input'
  import Pagination from '../components/Pagination.vue'
  import { VideoChannelsSearchQuery, ResultList, VideosSearchQuery } from '../../../Tube/shared/models'
  import Nprogress from 'nprogress'
  import { EnhancedPlaylist } from '../../../server/types/playlist.model'
  import { PlaylistsSearchQuery } from '../../../server/types/search-query/playlist-search.model'

  export default defineComponent({
    components: {
      'my-header': Header,
      'search-warning': SearchWarning,
      'video-result': VideoResult,
      'channel-result': ChannelResult,
      'playlist-result': PlaylistResult,
      'vue-tags-input': VueTagsInput,
      'pagination': Pagination
    },

    data () {
      return {
        searchDone: false,

        indexName: '',
        instancesCount: 0,
        indexedInstancesUrl: '',
        results: [] as (EnhancedVideo | EnhancedVideoChannel | EnhancedPlaylist)[],

        resultsCount: null as number,
        channelsCount: null as number,
        videosCount: null as number,
        playlistsCount: null as number,

        currentPage: 1,
        pages: [],
        resultsPerVideosPage: 10,
        resultsPerChannelsPage: 3,
        resultsPerPlaylistsPage: 3,

        displayFilters: false,
        oldQuery: '',

        formSearch: '',
        formSort: '-match',

        formNSFW: undefined,
        formHost: '',
        formPublishedDateRange: undefined,
        formDurationRange: undefined,
        formCategoryOneOf: undefined,
        formLicenceOneOf: undefined,
        formLanguageOneOf: undefined,

        formTagAllOf: '',
        formTagOneOf: '',
        formTagsAllOf: [],
        formTagsOneOf: [],

        formIsLive: undefined,

        activeFilters: 0
      }
    },

    async mounted () {
      const config = await getConfig()

      this.instancesCount = config.indexedHostsCount
      this.indexedInstancesUrl = config.indexedInstancesUrl
      this.indexName = config.searchInstanceName

      if (Object.keys(this.$route.query).length !== 0) {
        // Speed up search done variable if the user came directly with a query
        // So it does not see the homepage for a few ms
        this.searchDone = true
        this.loadUrl()
        this.doSearch()
      }
    },

    watch: {
      // For pagination change
      $route(to, from) {
        const urlPage = this.$route.query.page as string

        const scrollToResults = urlPage && parseInt(urlPage) !== this.currentPage

        this.loadUrl()

        this.doSearch()
        if (scrollToResults) this.scrollToResults()
      },

      formSort () {
        this.updateUrl()
      }
    },

    computed: {
      applyFiltersLabel (): string {
        return this.$gettext('Apply filters')
      },

      inputPlaceholder (): string {
        return this.$gettext('Keyword, channel, video, playlist, etc.')
      },

      tagsPlaceholder (): string {
        return this.$gettext('Add tag')
      },

      publishedDateRanges (): { id: string, label: string }[] {
        return [
          {
            id: 'any_published_date',
            label: this.$gettext('Any')
          },
          {
            id: 'today',
            label: this.$gettext('Today')
          },
          {
            id: 'last_7days',
            label: this.$gettext('Last 7 days')
          },
          {
            id: 'last_30days',
            label: this.$gettext('Last 30 days')
          },
          {
            id: 'last_365days',
            label: this.$gettext('Last 365 days')
          }
        ]
      },

      durationRanges (): { id: string, label: string }[] {
        return [
          {
            id: 'any_duration',
            label: this.$gettext('Any')
          },
          {
            id: 'short',
            label: this.$gettext('Short (< 4 min)')
          },
          {
            id: 'medium',
            label: this.$gettext('Medium (4-10 min)')
          },
          {
            id: 'long',
            label: this.$gettext('Long (> 10 min)')
          }
        ]
      },

      videoCategories (): { id: string, label: string }[] {
        return [
          { id: '1', label: this.$gettext('Music') },
          { id: '2', label: this.$gettext('Films') },
          { id: '3', label: this.$gettext('Vehicles') },
          { id: '4', label: this.$gettext('Art') },
          { id: '5', label: this.$gettext('Sports') },
          { id: '6', label: this.$gettext('Travels') },
          { id: '7', label: this.$gettext('Gaming') },
          { id: '8', label: this.$gettext('People') },
          { id: '9', label: this.$gettext('Comedy') },
          { id: '10', label: this.$gettext('Entertainment') },
          { id: '11', label: this.$gettext('News & Politics') },
          { id: '12', label: this.$gettext('How To') },
          { id: '13', label: this.$gettext('Education') },
          { id: '14', label: this.$gettext('Activism') },
          { id: '15', label: this.$gettext('Science & Technology') },
          { id: '16', label: this.$gettext('Animals') },
          { id: '17', label: this.$gettext('Kids') },
          { id: '18', label: this.$gettext('Food') }
        ]
      },

      videoLicences (): { id: string, label: string }[] {
        return [
          { id: '1', label: this.$gettext('Attribution') },
          { id: '2', label: this.$gettext('Attribution - Share Alike') },
          { id: '3', label: this.$gettext('Attribution - No Derivatives') },
          { id: '4', label: this.$gettext('Attribution - Non Commercial') },
          { id: '5', label: this.$gettext('Attribution - Non Commercial - Share Alike') },
          { id: '6', label: this.$gettext('Attribution - Non Commercial - No Derivatives') },
          { id: '7', label: this.$gettext('Public Domain Dedication') }
        ]
      },

      videoLanguages (): { id: string, label: string }[] {
        return [
          { id: 'en', label: this.$gettext('English') },
          { id: 'fr', label: this.$gettext('Français') },
          { id: 'ja', label: this.$gettext('日本語') },
          { id: 'eu', label: this.$gettext('Euskara') },
          { id: 'ca', label: this.$gettext('Català') },
          { id: 'cs', label: this.$gettext('Čeština') },
          { id: 'eo', label: this.$gettext('Esperanto') },
          { id: 'el', label: this.$gettext('ελληνικά') },
          { id: 'de', label: this.$gettext('Deutsch') },
          { id: 'it', label: this.$gettext('Italiano') },
          { id: 'nl', label: this.$gettext('Nederlands') },
          { id: 'es', label: this.$gettext('Español') },
          { id: 'oc', label: this.$gettext('Occitan') },
          { id: 'gd', label: this.$gettext('Gàidhlig') },
          { id: 'zh', label: this.$gettext('简体中文（中国）') },
          { id: 'pt', label: this.$gettext('Português (Portugal)') },
          { id: 'sv', label: this.$gettext('svenska') },
          { id: 'pl', label: this.$gettext('Polski') },
          { id: 'fi', label: this.$gettext('suomi') },
          { id: 'ru', label: this.$gettext('русский') }
        ]
      },

      boostLanguagesQuery (): string[] {
        const languages = new Set<string>()

        for (const completeLanguage of navigator.languages) {
          const language = completeLanguage.split('-')[0]

          if (this.videoLanguages.find(vl => vl.id === language)) {
            languages.add(language)
          }
        }

        return Array.from(languages)
      }
    },

    methods: {

      doNewSearch () {
        this.currentPage = 1
        this.channelsCount = null
        this.videosCount = null
        this.playlistsCount = null
        this.resultsCount = null

        this.updateUrl()
      },

      async doSearch () {
        // First search, scroll to top
        if (this.searchDone !== true) this.scrollToTop()

        this.results = []

        Nprogress.start()

        try {
          const [ videosResult, channelsResult, playlistsResult ] = await Promise.all([
            this.searchVideos(),
            this.searchChannels(),
            this.searchPlaylists()
          ])

          Nprogress.done()

          this.activeFilters = this.countActiveFilters()

          this.channelsCount = channelsResult.total
          this.videosCount = videosResult.total
          this.playlistsCount = playlistsResult.total

          this.resultsCount = videosResult.total + channelsResult.total + playlistsResult.total

          this.results = channelsResult.data
          this.results = this.results.concat(playlistsResult.data)
          this.results = this.results.concat(videosResult.data)

          if (this.formSort === '-match') {
            this.results.sort((r1, r2) => {
              if (r1.score < r2.score) return 1
              else if (r1.score === r2.score) return 0

              return -1
            })
          }

          this.buildPages()
          this.searchDone = true
        } catch (err) {
          console.error('Cannot do search.', err)
        } finally {
          Nprogress.done()
        }
      },

      isVideo (result: EnhancedVideo | EnhancedVideoChannel | EnhancedPlaylist) {
        if ((result as EnhancedVideo).language) return true

        return false
      },

      isPlaylist (result: EnhancedVideo | EnhancedVideoChannel | EnhancedPlaylist) {
        if ((result as EnhancedPlaylist).videosLength !== undefined) return true

        return false
      },

      isChannel (result: EnhancedVideo | EnhancedVideoChannel | EnhancedPlaylist) {
        if ((result as EnhancedVideoChannel).followingCount !== undefined) return true

        return false
      },

      getResultKey (result: EnhancedVideo | EnhancedVideoChannel | EnhancedPlaylist) {
        if (this.isVideo(result)) return (result as EnhancedVideo).uuid
        if (this.isChannel(result)) return result.id + (result as EnhancedVideoChannel).host
        if (this.isPlaylist(result)) return (result as EnhancedPlaylist).uuid

        throw new Error('Unknown result')
      },

      updateUrl () {
        const query: SearchUrl = {
          search: this.formSearch,
          sort: this.formSort,
          nsfw: this.formNSFW,
          host: this.formHost,
          isLive: this.formIsLive,
          publishedDateRange: this.formPublishedDateRange,
          durationRange: this.formDurationRange,
          categoryOneOf: this.formCategoryOneOf,
          licenceOneOf: this.formLicenceOneOf,
          languageOneOf: this.formLanguageOneOf,
          tagsAllOf: this.formTagsAllOf.map(t => t.text),
          tagsOneOf: this.formTagsOneOf.map(t => t.text),
          page: this.currentPage
        }

        this.$router.push({ path: '/search', query: query as any })
      },

      loadUrl () {
        const query = this.$route.query as SearchUrl

        if (query.search) this.formSearch = query.search
        else this.formSearch = undefined

        if (query.nsfw) this.formNSFW = query.nsfw
        else this.formNSFW = undefined

        if (query.publishedDateRange) this.formPublishedDateRange = query.publishedDateRange
        else this.formPublishedDateRange = undefined

        if (query.durationRange) this.formDurationRange = query.durationRange
        else this.formDurationRange = undefined

        if (query.categoryOneOf) this.formCategoryOneOf = query.categoryOneOf
        else this.formCategoryOneOf = undefined

        if (query.licenceOneOf) this.formLicenceOneOf = query.licenceOneOf
        else this.formLicenceOneOf = undefined

        if (query.languageOneOf) this.formLanguageOneOf = query.languageOneOf
        else this.formLanguageOneOf = undefined

        if (query.tagsAllOf) this.formTagsAllOf = extractTagsFromQuery(query.tagsAllOf)
        else this.formTagsAllOf = []

        if (query.tagsOneOf) this.formTagsOneOf = extractTagsFromQuery(query.tagsOneOf)
        else this.formTagsOneOf = []

        if (query.sort) this.formSort = query.sort
        else this.formSort = '-match'

        if (query.isLive) this.formIsLive = query.isLive
        else this.formIsLive = undefined

        if (query.host) this.formHost = query.host
        else this.formHost = ''

        if (query.page && this.currentPage !== query.page) {
          this.currentPage = parseInt(query.page + '')
        } else {
          this.currentPage = 1
        }
      },

      buildVideoSearchQuery () {
        const { start, count } = pageToAPIParams(this.currentPage, this.resultsPerVideosPage)
        const { durationMin, durationMax } = durationRangeToAPIParams(this.formDurationRange)
        const { startDate, endDate } = publishedDateRangeToAPIParams(this.formPublishedDateRange)

        const boostLanguages = this.boostLanguagesQuery

        return {
          search: this.formSearch,

          durationMin,
          durationMax,

          startDate,
          endDate,

          boostLanguages,

          nsfw: this.formNSFW || false,

          categoryOneOf: this.formCategoryOneOf ? [ this.formCategoryOneOf ] : undefined,
          licenceOneOf: this.formLicenceOneOf ? [ this.formLicenceOneOf ] : undefined,
          languageOneOf: this.formLanguageOneOf ? [ this.formLanguageOneOf ] : undefined,

          tagsOneOf: this.formTagsOneOf.map(t => t.text),
          tagsAllOf: this.formTagsAllOf.map(t => t.text),

          isLive: this.formIsLive !== undefined ? this.formIsLive : undefined,

          host: this.formHost || undefined,

          start,
          count,
          sort: this.formSort
        } as VideosSearchQuery
      },

      buildChannelSearchQuery () {
        const { start, count } = pageToAPIParams(this.currentPage, this.resultsPerChannelsPage)

        let sort: string
        if (this.formSort === '-matched') sort = '-matched'
        else if (this.formSort === '-publishedAt') sort = '-createdAt'
        else if (this.formSort === 'publishedAt') sort = 'createdAt'

        return {
          search: this.formSearch,
          host: this.formHost || undefined,
          start,
          sort,
          count
        } as VideoChannelsSearchQuery
      },

      buildPlaylistSearchQuery () {
        const { start, count } = pageToAPIParams(this.currentPage, this.resultsPerChannelsPage)

        let sort: string
        if (this.formSort === '-matched') sort = '-matched'
        else if (this.formSort === '-publishedAt') sort = '-createdAt'
        else if (this.formSort === 'publishedAt') sort = 'createdAt'

        return {
          search: this.formSearch,
          host: this.formHost || undefined,
          start,
          sort,
          count
        } as PlaylistsSearchQuery
      },

      searchVideos (): Promise<ResultList<EnhancedVideo>> {
        if (!this.hasStillMoreVideosResults()) {
          return Promise.resolve({ data: [], total: this.videosCount })
        }

        const query = this.buildVideoSearchQuery()

        return searchVideos(query)
      },

      searchChannels (): Promise<ResultList<EnhancedVideoChannel>> {
        if (this.isChannelOrPlaylistSearchDisabled()) {
          return Promise.resolve({ data: [], total: 0 })
        }

         if (!this.hasStillChannelsResult()) {
          return Promise.resolve({ data: [], total: this.channelsCount })
        }

        const query = this.buildChannelSearchQuery()

        return searchVideoChannels(query)
      },

      searchPlaylists (): Promise<ResultList<EnhancedPlaylist>> {
        if (this.isChannelOrPlaylistSearchDisabled()) {
          return Promise.resolve({ data: [], total: 0 })
        }

         if (!this.hasStillPlaylistsResult()) {
          return Promise.resolve({ data: [], total: this.playlistsCount })
        }

        const query = this.buildChannelSearchQuery()

        return searchVideoPlaylists(query)
      },

      hasStillChannelsResult () {
        // Not searched yet
        if (this.channelsCount === null) return true

        return this.getChannelsMaxPage() >= this.currentPage
      },

      hasStillPlaylistsResult () {
        // Not searched yet
        if (this.playlistsCount === null) return true

        return this.getPlaylistsMaxPage() >= this.currentPage
      },

      hasStillMoreVideosResults () {
        // Not searched yet
        if (this.videosCount === null) return true

        return this.getVideosMaxPage() >= this.currentPage
      },

      getChannelsMaxPage () {
        return Math.ceil(this.channelsCount / this.resultsPerChannelsPage)
      },

      getPlaylistsMaxPage () {
        return Math.ceil(this.playlistsCount / this.resultsPerPlaylistsPage)
      },

      getVideosMaxPage () {
        return Math.ceil(this.videosCount / this.resultsPerVideosPage)
      },

      getMaxPage () {
        // Limit to 10 pages
        return Math.min(10, Math.max(this.getPlaylistsMaxPage(), this.getChannelsMaxPage(), this.getVideosMaxPage()))
      },

      buildPages () {
        this.pages = []

        for (let i = 1; i <= this.getMaxPage(); i++) {
          this.pages.push(i)
        }
      },

      toggleFilters () {
        this.displayFilters = !this.displayFilters
      },

      resetField (field: string) {
        if (field === 'nsfw') this.formNSFW = undefined
        else if (field === 'publishedDateRange') this.formPublishedDateRange = undefined
        else if (field === 'durationRange') this.formDurationRange = undefined
        else if (field === 'categoryOneOf') this.formCategoryOneOf = undefined
        else if (field === 'licenceOneOf') this.formLicenceOneOf = undefined
        else if (field === 'languageOneOf') this.formLanguageOneOf = undefined
        else if (field === 'isLive') this.formIsLive = undefined
        else if (field === 'tagsAllOf') this.formTagsAllOf = []
        else if (field === 'tagsOneOf') this.formTagsOneOf = []
        else if (field === 'host') this.formHost = ''
      },

      countActiveFilters () {
        let count = 0

        if (this.formNSFW) count++
        if (this.formHost) count++
        if (this.formPublishedDateRange) count++
        if (this.formDurationRange) count++
        if (this.formCategoryOneOf) count++
        if (this.formLicenceOneOf) count++
        if (this.formLanguageOneOf) count++
        if (this.formIsLive) count++
        if (this.formTagsAllOf && this.formTagsAllOf.length !== 0) count++
        if (this.formTagsOneOf && this.formTagsOneOf.length !== 0) count++

        return count
      },

      isChannelOrPlaylistSearchDisabled () {
        // We can search against host for playlists and channels
        if (this.formHost) return this.countActiveFilters() > 1

        return this.countActiveFilters() > 0
      },

      scrollToResults () {
        const anchor = document.getElementById('results-anchor')
        if (anchor) anchor.scrollIntoView()
      },

      scrollToTop () {
        window.scrollTo(0, 0)
      }
    }
  })
</script>
