<template>
  <div class="video root-result">

    <div class="thumbnail">
      <a class="img" :title="watchVideoMessage" target="_blank" rel="nofollow noreferrer noopener" v-bind:href="video.url">
        <img v-bind:src="getVideoThumbnailUrl()" alt="" @error="setThumbnailError()" :class="{ error: thumbnailError }">

        <span v-if="video.isLive" class="live-info" v-translate>LIVE</span>
        <span v-else class="duration">{{ formattedDuration }}</span>
      </a>
    </div>

    <div class="information">
      <h5 class="title">
        <a :title="watchVideoMessage" target="_blank" rel="nofollow noreferrer noopener" v-bind:href="video.url">{{ video.name }}</a>
      </h5>

      <div class="description" v-html="renderMarkdown(video.description)"></div>

      <div class="metadatas">
        <div class="by-account">
          <label v-translate>Created by</label>
          <actor-miniature type="account" v-bind:actor="video.account"></actor-miniature>
        </div>

        <div class="by-channel">
          <label v-translate>In</label>

          <actor-miniature type="channel" v-bind:actor="video.channel"></actor-miniature>
        </div>

        <div class="publishedAt">
          <label v-translate>On</label>

          <div class="value">{{ publicationDate }}</div>

        </div>

        <div class="language">
          <label v-translate>Language</label>

          <div class="value">{{ video.language.label }}</div>
        </div>

        <div class="tags" v-if="video.tags && video.tags.length !== 0">
          <label v-translate>Tags</label>

          <div class="value">{{ video.tags.join(', ') }}</div>
        </div>
      </div>

      <div class="button">
        <a class="button-link" target="_blank" rel="nofollow noreferrer noopener" v-bind:href="video.url">
          {{ watchVideoMessage }}
        </a>
      </div>
    </div>

  </div>
</template>

<style lang="scss">
  @import '../scss/_variables';

  .video {

    .thumbnail {
      margin-right: 20px;

      --thumbnail-width: #{$thumbnail-width};
      --thumbnail-height: #{$thumbnail-height};

      // For the duration overlay
      .img {
        position: relative;
      }

      img {
        background-color: #E5E5E5;
        width: var(--thumbnail-width);
        height: var(--thumbnail-height);
        border-radius: 3px;

        &.error {
          border: 1px solid #E5E5E5;
        }
      }

      .duration,
      .live-info {
        position: absolute;
        padding: 2px 5px;
        right: 5px;
        bottom: 5px;
        display: inline-block;
        color: #fff;
        font-size: 11px;
        border-radius: 3px;
      }

      .duration {
        background-color: rgba(0, 0, 0, 0.7);
      }

      .live-info {
        background-color: rgba(224, 8, 8 ,.8);
        font-weight: $font-semibold;
      }

      @media screen and (max-width: $small-view) {
        --thumbnail-width: calc(100% + 10px);
        --thumbnail-height: auto;

        img {
          border-radius: 0;
        }
      }
    }
  }
</style>

<script lang="ts">
  import { defineComponent, PropType } from 'vue'
  import ActorMiniature from './ActorMiniature.vue'
  import { durationToString } from '../shared/utils'
  import { renderMarkdown } from '../shared/markdown-render'
  import { EnhancedVideo } from '../../../server/types/video.model'

  export default defineComponent({
    components: {
      'actor-miniature': ActorMiniature
    },

    props: {
      video: Object as PropType<EnhancedVideo>
    },

    data () {
      return {
        thumbnailError: false
      }
    },

    computed: {
      host (): string {
        const url = this.video.url

        return new URL(url as string).host
      },

      formattedDuration (): string {
        return durationToString(this.video.duration)
      },

      windowWidth (): number {
        return window.innerWidth
      },

      publicationDate (): string {
        return new Date(this.video.publishedAt).toLocaleDateString()
      },

      watchVideoMessage (): string {
        return this.$gettextInterpolate(this.$gettext('Watch the video on %{host}'), { host: this.host })
      }
    },

    methods: {
      getVideoThumbnailUrl () {
        if (this.windowWidth >= 900) {
          return this.video.thumbnailUrl
        }

        return this.video.previewUrl
      },

      setThumbnailError () {
        this.thumbnailError = true
      },

      renderMarkdown(markdown: string) {
        return renderMarkdown(markdown)
      }
    }
  })
</script>
