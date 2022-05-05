<template>
  <div class="instance-card">

    <div class="left">
      <div class="name-host">
        <div class="name">{{ instance.name }}</div>
        <div class="host">{{ instance.host }}</div>
      </div>

      <div class="description" :title="instance.shortDescription">
        {{ truncatedDescription }}
      </div>

      <div class="upload-limits" v-if="isVideoMaker">
        <div class="quota">
          <div class="icon">
            <icon-quota></icon-quota>
          </div>

          <div v-if="instance.userVideoQuota">
            {{ bytes(instance.userVideoQuota) }}
            <translate>per user</translate>
          </div>

          <div v-else v-translate>
            No video quota per user
          </div>
        </div>

        <div class="auto-blacklist" v-if="instance.autoBlacklistUserVideosEnabled">
          <div class="label">Videos publication</div>
          <div class="value">After moderation</div>
        </div>
      </div>

      <div class="tags">
        <div class="tag" v-for="category in limitedCategories" :key="category">
          {{ translatedThemes[category] }}
        </div>
      </div>
    </div>

    <div class="right">
      <div class="follow">
        <template v-if="isVideoMaker">
          <div class="icon">
            <icon-followers></icon-followers>
          </div>
          <div>
            <translate :translate-n="instance.totalInstanceFollowers"
                       translate-plural="%{ instance.totalInstanceFollowers } followers">
              %{ instance.totalInstanceFollowers } follower instance
            </translate>
          </div>
        </template>

        <template v-else>
          <div class="icon">
            <icon-following></icon-following>
          </div>
          <div>
            <translate :translate-n="instance.totalInstanceFollowing"
                       translate-plural="Follows %{ instance.totalInstanceFollowing } instances">
              Follows %{ instance.totalInstanceFollowing } instance
            </translate>
          </div>
        </template>
      </div>

      <div class="languages" v-if="Array.isArray(instance.languages) && instance.languages.length !== 0">
        <div class="icon">
          <icon-languages></icon-languages>
        </div>

        <div>{{ languages }}</div>
      </div>

      <div class="nsfw">
        <span v-translate class="label">Sensitive content</span>
        <div v-translate v-if="instance.defaultNSFWPolicy === 'do_not_list'">Hidden</div>
        <div v-translate v-if="instance.defaultNSFWPolicy === 'blur'">Blurred</div>
        <div v-translate v-if="instance.defaultNSFWPolicy === 'display'">Displayed</div>
      </div>

      <div class="link">
        <a :href="getUrl(instance)" class="bottom-link" target="_blank" rel="noopener noreferrer">
          <span class="text" v-translate>See the instance</span>
          <icon-right></icon-right>
        </a>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';
  @import '../scss/_mixins.scss';

  .instance-card {
    min-height: 185px;
    width: 770px;
    margin: auto;
    box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.35);
    border: solid 1px #d9d9d9;
    padding: 20px 25px 15px 25px;
    display: flex;

    .left,
    .right {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
    }

    .icon,
    .label {
      margin-right: 5px;
    }

    .label {
      color: $grey;
      letter-spacing: -0.5px;
    }

    .left {
      margin-right: 20px;
      width: 500px;

      .name-host {
        display: flex;
        margin-bottom: 3px;
        flex-wrap: wrap;
      }

      .name {
        @include proza-semi-bold;

        margin-right: 10px;
      }

      .host {
        color: $grey;
      }

      .description {
        margin-bottom: 8px;
        line-height: normal;
        word-break: break-word;
      }

      .upload-limits {
        display: flex;
        margin-bottom: 20px;

        .quota {
          display: flex;
          margin-right: 80px;

          .icon {
            position: relative;
            top: -2px;
          }
        }
      }

      .auto-blacklist {
        display: flex;
        flex-wrap: wrap;
        margin-top: auto;
      }

      .tags {
        display: flex;
        flex-wrap: wrap;
        margin-top: auto;

        .tag {
          margin-bottom: 5px;
        }
      }
    }

    .right {
      .follow,
      .languages {
        margin-bottom: 10px;
        display: flex;
      }

      .nsfw {
        display: flex;
      }

      .link {
        margin-top: auto;
        margin-bottom: 2px;
        align-self: flex-end;
      }
    }

    @media screen and (max-width: $responsive-screen) {
      width: auto;
      flex-direction: column;
      padding: 15px;

      .left {
        width: auto;
        margin-right: 0;
      }

      .right {
        margin-top: 20px;

        .follow,
        .languages {
          margin-bottom: 5px;
        }

        .link {
          margin-top: 20px;
        }
      }
    }
  }
</style>

<script>
  import IconRight from './icons/IconRight'
  import IconFollowers from './icons/IconFollowers'
  import IconFollowing from './icons/IconFollowing'
  import IconLanguages from './icons/IconLanguages'
  import IconQuota from './icons/IconQuota'
  import truncate from 'lodash/truncate'

  export default {
    components: {
      IconRight,
      IconFollowers,
      IconFollowing,
      IconLanguages,
      IconQuota
    },

    props: {
      instance: Object,
      translatedThemes: Object,
      translatedLanguages: Object,
      isVideoMaker: Boolean
    },

    computed: {
      truncatedDescription () {
        const t = this.instance.shortDescription

        return truncate(t, { separator: /,? /, length: 200 })
      },

      languages () {
        return this.instance.languages
          .map(l => this.translatedLanguages[l])
          .filter(l => !!l)
          .join(', ')
      },

      limitedCategories () {
        if (Array.isArray(this.instance.categories) === false) return []

        return this.instance.categories.slice(0, 3)
      }
    },

    methods: {
      bytes (value) {
        if (value === -1) return this.$gettext('Unlimited space')

        // https://github.com/danrevah/ngx-pipes/blob/master/src/pipes/math/bytes.ts
        const dictionaryBytes = [
          { max: 1024, type: this.$gettext('B') },
          { max: 1048576, type: this.$gettext('KB') },
          { max: 1073741824, type: this.$gettext('MB') },
          { max: 1.0995116e12, type: this.$gettext('GB') }
        ]

        const format = dictionaryBytes.find(function (d) { return value < d.max }) || dictionaryBytes[dictionaryBytes.length - 1]
        const calc = Math.floor(value / (format.max / 1024)).toString()

        return calc + format.type
      },

      getUrl (instance) {
        return `https://${instance.host}`
      }
    },

    mounted () {

    }
  }
</script>
