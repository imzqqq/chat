<template>
  <div id="plugin-selections">
    <div class="plugin-selection" v-for="pluginSelection in getPluginSelections()" :key="pluginSelection.url">
      <plugin-selection
        :title="pluginSelection.title" :url="pluginSelection.url" :description="pluginSelection.description"
        :previewName="pluginSelection.preview" :subtitle="pluginSelection.subtitle"
      >
      </plugin-selection>
    </div>
  </div>
</template>

<style lang="scss">
  @import '../scss/_variables';

  #plugin-selections {
    .plugin-selection {
      margin-bottom: 80px;
    }
  }
</style>

<script>
  import PluginSelection from './PluginSelection'
  import PluginSelectionsEN from '../mixins/PluginSelectionsEN'
  import PluginSelectionsFR from '../mixins/PluginSelectionsFR'
  import sampleSize from 'lodash/sampleSize'

  export default {
    mixins: [
      PluginSelectionsEN,
      PluginSelectionsFR
    ],

    props: {
      sampleSizeEach: Number
    },

    components: {
      PluginSelection
    },

    methods: {
      getPluginSelections () {
        if (this.$language.current.startsWith('fr_')) {
          return this.sampleIfNeeded(this.pluginSelectionsFR)
        }

        return this.sampleIfNeeded(this.pluginSelectionsEN)
      },

      sampleIfNeeded (objects) {
        const plugins = [ ...objects ]
        return sampleSize(plugins, this.sampleSizeEach || plugins.length)
      }
    }
  }
</script>
