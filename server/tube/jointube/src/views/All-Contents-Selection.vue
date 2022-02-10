<template>
  <main id="all-contents-selection">
    <div class="blocks">

      <div class="title-block">
        <h1 v-translate>Our content selection</h1>

        <div class="separator"></div>
      </div>

      <contents-selection></contents-selection>
    </div>
  </main>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';

  #all-contents-selection {
    .blocks {
      margin: 60px auto;
    }
  }
</style>

<script>
  import ContentsSelection from '../components/ContentsSelection'

  export default {
    components: {
      ContentsSelection
    },

    metaInfo: function () {
      return {
        title: this.$gettext('Our contents selection')
      }
    }
  }
</script>
