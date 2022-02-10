<template>
  <main id="roadmap-2020-placeholder">

    <img :src="buildImgUrl('v3-banner.jpg')" />

    <div class="text" v-translate>
      Thanks to our crowdfunfing contributors, Tube V3 with live streaming support, global search, playlists embed and more moderation tools is out!
    </div>

    <p class="link-block" v-translate>
      <a href="https://framablog.org/2021/01/07/tube-v3-its-a-live-a-liiiiive/" target="_blank">
        Read the blog post for more information
      </a>
    </p>
  </main>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';

  #roadmap-2020-placeholder {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding: 50px 0;

    img {
      margin-bottom: 50px;
    }

    .text {
      text-align: center;
      font-size: 20;
      max-width: 500px
    }

    .link-block {
      margin-top: 30px;
    }
  }
</style>

<script>
  export default {
    components: { },

    metaInfo: function () {
      return {
        title: this.$gettext('2020 roadmap')
      }
    }
  }
</script>
