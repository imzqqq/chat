<template>
  <main id="all-plugin-selection">
    <div class="blocks">

      <div class="title-block">
        <h1 v-translate>Our plugin selection</h1>

        <div class="separator"></div>
      </div>

      <plugins-selection></plugins-selection>
    </div>
  </main>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';

  #all-plugin-selection {
    .blocks {
      margin: 60px auto;
    }
  }
</style>

<script>
  import PluginsSelection from '../components/PluginsSelection'

  export default {
    components: {
      PluginsSelection
    },

    metaInfo: function () {
      return {
        title: this.$gettext('Our plugins selection')
      }
    }
  }
</script>
