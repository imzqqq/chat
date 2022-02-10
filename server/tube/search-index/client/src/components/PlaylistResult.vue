<template>
  <div class="playlist root-result">

    <div class="thumbnail">
      <a class="img" :title="watchMessage" target="_blank" rel="nofollow noreferrer noopener" v-bind:href="playlist.url">
        <img v-bind:src="playlist.thumbnailUrl" alt="">

        <span class="videos-length">{{ videosLengthLabel }}</span>
      </a>
    </div>

    <div class="information">
      <h5 class="title">
        <a :title="watchMessage" target="_blank" rel="nofollow noreferrer noopener" v-bind:href="playlist.url">{{ playlist.displayName }}</a>
      </h5>

      <div class="description" v-html="renderMarkdown(playlist.description)"></div>

      <div class="metadatas">
        <div class="by-account">
          <label v-translate>Created by</label>
          <actor-miniature type="account" v-bind:actor="playlist.ownerAccount"></actor-miniature>
        </div>

        <div class="by-channel">
          <label v-translate>In</label>

          <actor-miniature type="channel" v-bind:actor="playlist.videoChannel"></actor-miniature>
        </div>

        <div class="publishedAt">
          <label v-translate>Updated on</label>

          <div class="value">{{ updateDate }}</div>

        </div>
      </div>

      <div class="button">
        <a class="button-link" target="_blank" rel="nofollow noreferrer noopener" v-bind:href="playlist.url">
          {{ watchMessage }}
        </a>
      </div>
    </div>

  </div>
</template>

<style lang="scss">
  @import '../scss/_variables';

  .playlist {

    .thumbnail {
      margin-right: 20px;

      --thumbnail-width: #{$thumbnail-width};
      --thumbnail-height: #{$thumbnail-height};

      // For the videos length overlay
      .img {
        position: relative;
        display: inline-block;
        width: var(--thumbnail-width);
        height: var(--thumbnail-height);
        border-radius: 3px;
        overflow: hidden;
      }

      .videos-length {
        position: absolute;
        right: 0;
        bottom: 0;

        display: flex;
        align-items: center;
        padding: 0 10px;
        height: 100%;

        color: #fff;
        background-color: rgba(0,0,0,.7);

        font-size: 14px;
        font-weight: 600;
      }

      img {
        width: 100%;
        height: 100%;
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
  import { VideoPlaylist } from '../../../Tube/shared/models'
  import { renderMarkdown } from '../shared/markdown-render'
  import { useGettext } from '@jshmrtn/vue3-gettext'

  export default defineComponent({
    components: {
      'actor-miniature': ActorMiniature
    },

    props: {
      playlist: Object as PropType<VideoPlaylist>
    },

    computed: {
      host (): string {
        const url = this.playlist.url

        return new URL(url as string).host
      },

      updateDate (): string {
        return new Date(this.playlist.updatedAt).toLocaleDateString()
      },

      watchMessage (): string {
        return this.$gettextInterpolate(this.$gettext('Watch the playlist on %{host}'), { host: this.host })
      },

      videosLengthLabel (): string {
        return this.$gettextInterpolate(this.$gettext('%{videosLength} videos'), { videosLength: this.playlist.videosLength })
      }
    },

    methods: {
      renderMarkdown(markdown: string) {
        return renderMarkdown(markdown)
      }
    }
  })
</script>
