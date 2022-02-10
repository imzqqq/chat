<template>
  <div id="instances-list">
    <div class="filters">
      <form @change="onFormChange()">
        <fieldset>
          <legend>
            <translate>Filter according to your preferences</translate>

            <span class="sr-only" v-translate>Updating one of the fields will automatically update results below the form</span>
          </legend>

          <div class="group">
            <label for="profile" v-translate>Your profile</label>

            <b-form-radio-group id="profile" buttons name="profile" v-model="profile">
              <b-form-radio value="viewer">
                <translate>Viewer</translate>
              </b-form-radio>

              <b-form-radio value="video-maker">
                <translate>Video maker</translate>
              </b-form-radio>
            </b-form-radio-group>
          </div>

          <div class="group">
            <label for="want-to" v-translate>You want to</label>

            <b-form-radio-group id="want-to" buttons name="want-to" v-model="wantTo">
              <b-form-radio value="create-account">
                <translate>Create an account</translate>
              </b-form-radio>

              <b-form-radio value="discover-instances">
                <translate>Discover instances</translate>
              </b-form-radio>
            </b-form-radio-group>
          </div>

          <div class="group" v-show="isStreamEnabled()">
            <label for="is-streamer" v-translate>And do live streams</label>

            <b-form-radio-group id="is-streamer" buttons name="is-streamer" v-model="isStreamer">
              <b-form-radio value="yes">
                <translate>Yes</translate>
              </b-form-radio>

              <b-form-radio value="no">
                <translate>No</translate>
              </b-form-radio>
            </b-form-radio-group>
          </div>

          <div class="group">
            <label for="themes" class="label-checkbox" v-translate>Topics</label>

            <b-form-checkbox-group
              v-model="themes"
              :options="availableThemes"
              name="themes"
              id="themes"
              size="lg"
            ></b-form-checkbox-group>
          </div>

          <div class="group">
            <label for="profile" v-translate>Sensitive videos</label>

            <b-form-radio-group id="nsfw" buttons name="nsfw" v-model="nsfw">
              <b-form-radio value="do_not_list">
                <translate>Hide</translate>
              </b-form-radio>

              <b-form-radio value="blur">
                <translate>Blur</translate>
              </b-form-radio>

              <b-form-radio value="display">
                <translate>Display</translate>
              </b-form-radio>

              <b-form-radio value="no-opinion">
                <translate>No opinion</translate>
              </b-form-radio>
            </b-form-radio-group>
          </div>

          <div class="group">
            <label for="instance-languages" class="label-checkbox" v-translate>Instance languages</label>

            <b-form-checkbox-group
              v-model="languages"
              :options="availableLanguages"
              name="instance-languages"
              id="instance-languages"
              size="lg"
            ></b-form-checkbox-group>
          </div>

          <div class="group" v-show="isQuotaEnabled()">
            <label for="quota" v-translate>Allowed video space</label>

            <b-form-select v-model="quota" id="quota" name="quota">
              <option value="1000000000">
                <translate>At least 1GB</translate>
              </option>

              <option value="5000000000">
                <translate>At least 5GB</translate>
              </option>

              <option value="20000000000">
                <translate>At least 20GB</translate>
              </option>

              <option value="50000000000">
                <translate>At least 50GB</translate>
              </option>
            </b-form-select>
          </div>
        </fieldset>
      </form>
    </div>

    <div class="list">
      <div class="block-title">
        <div class="left">
          <h2 v-translate>Instances list</h2>

          <div class="sr-only" role="status" v-if="noResults" v-translate>No instances found</div>
          <div class="sr-only" role="status" v-else-if="instances.length !== 0" v-translate>{{ instances.length }} instances found</div>

          <div class="additional-info">
            <div v-translate>Built from <a href="https://instances.joinpeertube.org">instances.joinpeertube.org</a></div>

            <div>
              <a href="mailto:rt+abuse-tube@framasoft.org" rel="noopener" target="_blank" v-translate>Report an instance</a> breaking our <a href="https://instances.joinpeertube.org">ToS</a>
             </div>
          </div>
        </div>

        <div class="mascot-loading-block">
          <img v-bind:class="{ animate: loadingAnimation }" v-on:animationend="loadingAnimation = false" class="mascot-loading" :src="buildImgUrl('mascot/happy.png')" alt="Loading...">
        </div>
      </div>

      <div class="list" v-bind:class="{ unloaded: !error && !noResults && instances.length === 0 }">
        <div v-for="instance of instances" class="instance" :key="instance.host">
          <instance-card
            :instance="instance" :isVideoMaker="isVideoMaker()"
            :translatedLanguages="translatedLanguages" :translatedThemes="translatedThemes"
          >
          </instance-card>
        </div>
      </div>

      <div v-if="error" class="message">
        <img :src="buildImgUrl('mascot/defeated.png')" alt="">

        <div class="alert alert-danger">Sorry, but we cannot fetch the instances list. Please retry later.</div>
      </div>

      <div v-if="noResults" class="message">
        <img :src="buildImgUrl('mascot/oh.png')" alt="">

        <div class="alert alert-info">Sorry, but we did not find any instance matching your filters.</div>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';

  .custom-checkbox {
    .custom-control-label > span {
      font-size: 16px;
    }

    .custom-control-input:checked + label {
      font-weight: $font-semibold;
    }
  }

  #themes {
    .custom-control {
      &:nth-child(4n) {
        width: 100px;
        margin-right: 0;
      }

      &:nth-child(4n-1) {
        width: 190px;
      }

      &:nth-child(4n-2) {
        width: 130px;
      }

      &:nth-child(4n-3) {
        width: 100px;
      }
    }
  }

  .list {
    .block-title {
      height: 150px;
      display: flex;
      align-items: center;

      .left {
        max-width: 250px;
      }

      .additional-info {
        display: flex;
        flex-direction: column;
        font-style: italic;
        font-size: 12px;
      }
    }

    .instance {
      margin-bottom: 40px;
    }

    .list.unloaded {
      min-height: 400px;
    }
  }

  .mascot-loading-block {
    flex-grow: 1;

    .mascot-loading {
      display: none;

      @media screen and (min-width: $responsive-screen) {
        &.animate {
          display: block;
          animation: mascotLoadingAnimation 1s normal ease-out;

          @keyframes mascotLoadingAnimation {
            0% {
              margin-left: 0;
            }

            50% {
              opacity: 1;
            }

            100% {
              margin-left: calc(100% - 150px);
              opacity: 0;
            }
          }
        }
      }
    }
  }

  @media screen and (max-width: $responsive-screen) {
    .custom-checkbox {
      .custom-control-label > span {
        font-size: 15px;
      }
    }

    #themes {
      .custom-control {
        margin-right: 10px;
        width: calc(50% - 10px) !important;
      }
    }
  }

  #instances-list {
    .filters {
      margin-bottom: 100px;

      fieldset {
        border-left: 6px solid $orange;
        padding-left: 24px;

        legend {
          font-size: 24px;
          font-family: 'Proza Libre', sans-serif;
          font-weight: $font-semibold;
          margin-bottom: 25px;
        }
      }

      form {

        .group {
          margin-bottom: 30px;
          display: flex;
          align-items: center;

          & > label {
            min-width: 140px;
            margin: 0 10px 0 0;

            &.label-checkbox {
              align-self: flex-start;
            }
          }
        }

        select {
          max-width: 280px;
          width: 100%;
          height: 35px;
          line-height: 21px;
          border-radius: 0;
        }

        .btn-group-toggle {

          .btn {
            display: flex;
            align-items: center;
            color: #000;
            background-color: #fff;
            border-color: $orange;
            min-height: 30px;
            padding: 0 15px;
            cursor: pointer;

            &.active {
              color: #fff;
              font-weight: $font-semibold;
              background-color: $orange;
            }
          }
        }
      }
    }

    .message img {
      display: block;
      margin: 0 auto 50px;
    }

    @media screen and (max-width: $responsive-screen) {
      .filters {
        margin-bottom: 100px;

        form {
          padding-left: 15px;

          .group {
            flex-direction: column;
            align-items: flex-start;

            label {
              margin-bottom: 5px;
              font-size: 15px;
            }
          }

          .btn-group-toggle {
            .btn {
              padding: 0 8px;
            }
          }
        }
      }
    }
  }
</style>

<script>
  import { BFormCheckboxGroup, BFormRadio, BFormRadioGroup, BFormSelect } from 'bootstrap-vue'
  import axios from 'axios'
  import InstanceCard from './InstanceCard'

  export default {
    components: {
      'b-form-radio-group': BFormRadioGroup,
      'b-form-radio': BFormRadio,
      'b-form-checkbox-group': BFormCheckboxGroup,
      'b-form-select': BFormSelect,
      InstanceCard
    },

    data () {
      return {
        error: false,
        noResults: false,
        loadingAnimation: false,

        instances: [],
        translatedThemes: {
          '1': this.$gettext('Music'),
          '2': this.$gettext('Films'),
          '3': this.$gettext('Vehicles'),
          '4': this.$gettext('Art'),
          '5': this.$gettext('Sports'),
          '6': this.$gettext('Travels'),
          '7': this.$gettext('Gaming'),
          '8': this.$gettext('People'),
          '9': this.$gettext('Comedy'),
          '10': this.$gettext('Entertainment'),
          '11': this.$gettext('News & Politics'),
          '12': this.$gettext('How To'),
          '13': this.$gettext('Education'),
          '14': this.$gettext('Activism'),
          '15': this.$gettext('Science & Technology'),
          '16': this.$gettext('Animals'),
          '17': this.$gettext('Kids'),
          '18': this.$gettext('Food')
        },
        translatedLanguages: {
          'en': this.$gettext('English'),
          'fr': this.$gettext('Français'),
          'ja': this.$gettext('日本語'),
          'eu': this.$gettext('Euskara'),
          'ca': this.$gettext('Català'),
          'cs': this.$gettext('Čeština'),
          'eo': this.$gettext('Esperanto'),
          'el': this.$gettext('ελληνικά'),
          'de': this.$gettext('Deutsch'),
          'it': this.$gettext('Italiano'),
          'nl': this.$gettext('Nederlands'),
          'es': this.$gettext('Español'),
          'oc': this.$gettext('Occitan'),
          'gd': this.$gettext('Gàidhlig'),
          'zh': this.$gettext('简体中文（中国）'),
          'pt': this.$gettext('Português (Portugal)'),
          'sv': this.$gettext('svenska'),
          'pl': this.$gettext('Polski'),
          'fi': this.$gettext('suomi'),
          'ru': this.$gettext('русский')
        },

        profile: 'viewer',
        wantTo: 'create-account',
        themes: [],
        nsfw: 'blur',
        languages: [],
        quota: '5000000000',
        isStreamer: 'no'
      }
    },

    computed: {
      availableThemes: function () {
        const result = []

        for (const key of Object.keys(this.translatedThemes)) {
          result.push({
            value: key,
            text: this.translatedThemes[key]
          })
        }

        return result
      },

      availableLanguages: function () {
        const available = [
          {
            value: 'en',
            text: this.translatedLanguages.en
          }
        ]

        const navigatorLanguage = window.navigator.userLanguage || window.navigator.language
        const smallLanguage = navigatorLanguage.split('-')[0]

        const found = this.translatedLanguages[smallLanguage]
        if (found && smallLanguage !== 'en') {
          available.push({
            value: smallLanguage,
            text: found
          })
        }

        return available
      }
    },

    methods: {
      isVideoMaker () {
        return this.profile === 'video-maker'
      },

      wantToRegister () {
        return this.wantTo === 'create-account'
      },

      // Thanks https://stackoverflow.com/a/6274381
      shuffle (a) {
        for (let i = a.length - 1; i > 0; i--) {
          const j = Math.floor(Math.random() * (i + 1));
          [ a[i], a[j] ] = [ a[j], a[i] ]
        }

        return a
      },

      onFormChange () {
        // Wait for v-model to be updated
        // https://github.com/vuejs/vue/issues/293#issuecomment-265716984
        setTimeout(() => {
          console.log('Updating instances list.')

          this.fetchInstances()
        })
      },

      isQuotaEnabled () {
        return this.isVideoMaker() && this.wantToRegister()
      },

      isStreamEnabled () {
        return this.isVideoMaker() && this.wantToRegister()
      },

      fetchInstances () {
        this.noResults = false
        this.loadingAnimation = true

        const params = {
          start: 0,
          count: 250,
          healthy: true
        }

        if (this.wantTo === 'create-account') params.signup = true
        if (this.themes.length !== 0) params.categoriesOr = this.themes
        if (this.languages.length !== 0) params.languagesOr = this.languages
        if (this.isQuotaEnabled()) params.minUserQuota = this.quota
        if (this.isStreamEnabled() && this.isStreamer === 'yes') params.liveEnabled = true

        if (this.nsfw !== 'no-opinion') {
          params.nsfwPolicy = [ this.nsfw ]

          if (this.nsfw === 'blur') {
            // Also display instances that hide NSFW content if the blur filter is selected
            params.nsfwPolicy.push('do_not_list')
          }
        }

        const options = {
          method: 'GET',
          params
        }

        axios('https://instances.joinpeertube.org/api/v1/instances', options)
          .then(response => {
            this.instances = this.shuffle(response.data.data)

            if (this.instances.length === 0) this.noResults = true
          })
          .catch(err => {
            console.error(err)
            this.error = true
          })
      }
    },

    mounted () {
      this.fetchInstances()
    }
  }
</script>
