<template>
  <a v-bind:href="actor.url" rel="nofollow noreferrer noopener" target="_blank" class="actor" v-bind:title="linkTitle">
    <img v-if="actor.avatar && !avatarError" v-bind:src="actor.avatar.url" alt="" :class="{ account: isAccount }" @error="setAvatarError()">

    <strong>{{ actor.displayName }}</strong>

    <span class="actor-handle">
      {{ actor.name }}@{{actor.host}}
    </span>
  </a>
</template>

<style lang="scss">
  @import '../scss/_variables';

  .actor {
    font-size: inherit;
    color: #000;
    text-decoration: none;
    display: flex;
    flex-wrap: wrap;

    &:hover {
      text-decoration: underline;
    }

    img {
      object-fit: cover;
      width: 20px;
      height: 20px;
      min-width: 20px;
      min-height: 20px;
      margin-right: 5px;

      &.account {
        border-radius: 50%;
      }
    }

    .actor-handle {
      color: $grey;
      margin: 0 5px;
    }

    strong,
    .actor-handle {
      vertical-align: super;
    }

    @media screen and (max-width: $small-view) {
      .actor-handle {
        display: block;
        margin: 0 0 10px 0;
      }
    }
  }

</style>

<script lang="ts">
  import { defineComponent, PropType } from 'vue'
  import { AccountSummary, VideoChannelSummary } from '../../../Tube/shared/models'

  export default defineComponent({
    props: {
      actor: Object as PropType<AccountSummary | VideoChannelSummary>,
      type: String
    },

    data () {
      return {
        avatarError: false
      }
    },

    methods: {
      setAvatarError () {
        this.avatarError = true
      }
    },

    computed: {
      linkTitle (): string {
        if (this.type === 'channel') return this.$gettext('Go on this channel page')

        return this.$gettext('Go on this account page')
      },

      isAccount (): boolean {
        return this.type === 'account'
      }
    }
  })
</script>
