<template>
  <main id="not-found">

    <img :src="buildImgUrl('mascot/defeated.png')" alt="mascot" />

    <div class="not-found-text" v-translate>
      Page not found
    </div>

    <p class="link-block">
      <router-link to="/" v-translate>
        Go back to the homepage
      </router-link>
    </p>
  </main>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';

  #not-found {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding: 50px 0;

    img {
      margin-bottom: 50px;
    }

    .not-found-text {
      font-size: 25px;
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
        title: this.$gettext('Page not found')
      }
    }
  }
</script>
