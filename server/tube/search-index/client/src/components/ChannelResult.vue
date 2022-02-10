<template>
  <div class="channel root-result">

    <a target="_blank" rel="nofollow noreferrer noopener" v-bind:href="channel.url" :title="discoverChannelMessage" class="avatar">
      <img v-if="channel.avatar" v-bind:src="channel.avatar.url" alt="">
      <img v-else src="/img/default-avatar.png" alt="">
    </a>

    <div class="information">
      <div class="title-block">
        <h5 class="title">
          <a target="_blank" rel="nofollow noreferrer noopener" v-bind:href="channel.url" :title="discoverChannelMessage">
            {{ channel.displayName }}
          </a>
        </h5>

        <span class="handle">{{ channel.name }}@{{ channel.host }}</span>
      </div>

      <div class="additional-information">
        <div class="followers-count">
          {{ followersCountMessage }}
        </div>
      </div>

      <div class="description">{{ channel.description }}</div>

      <div class="button">
        <a class="button-link" rel="nofollow noreferrer noopener" target="_blank" v-bind:href="channel.url">
          {{ discoverChannelMessage }}
        </a>
      </div>
    </div>

  </div>
</template>

<style lang="scss">
  @import '../scss/_variables';

  .channel {
    .avatar {
      width: $thumbnail-width;
      min-width: $thumbnail-width;
      margin-right: 20px;
      display: flex;
      justify-content: center;

      img {
        object-fit: cover;
        width: 110px;
        height: 110px;
        min-width: 110px;
        min-height: 110px;
      }
    }
  }
</style>

<script lang="ts">
  import { defineComponent, PropType } from 'vue'
  import { VideoChannel } from '../../../Tube/shared/models'

  export default defineComponent({
    props: {
      channel: Object as PropType<VideoChannel>
    },

    computed: {
      host (): string {
        const url = this.channel.url

        return new URL(url as string).host
      },

      discoverChannelMessage (): string {
        return this.$gettextInterpolate(this.$gettext('Discover this channel on %{host}'), { host: this.host })
      },

      followersCountMessage (): string {
        const translated = this.$ngettext('%{ n } follower', '%{ n } followers', this.channel.followersCount)

        return this.$gettextInterpolate(translated, { n: this.channel.followersCount })
      }
    }
  })
</script>
