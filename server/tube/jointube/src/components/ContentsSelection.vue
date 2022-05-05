<template>
  <div id="content-selections">
    <div class="content-selection" v-for="contentSelection in getContentSelections()" :key="contentSelection.url">
      <content-selection :type="contentSelection.type" :title="contentSelection.title"
                         :thumbnail-name="contentSelection.thumbnailName" :url="contentSelection.url" :tags="contentSelection.tags"
                         :description="contentSelection.description"
      >
      </content-selection>
    </div>
  </div>
</template>

<style lang="scss">
  @import '../scss/_variables';

  #content-selections {
    > .content-selection {
      margin-bottom: 60px;

      @media screen and (max-width: $small-screen) {
        margin-bottom: 15px;
      }
    }
  }
</style>

<script>
  import ContentSelection from './ContentSelection'
  import ContentSelectionsEN from '../mixins/ContentSelectionsEN'
  import ContentSelectionsFR from '../mixins/ContentSelectionsFR'
  import sampleSize from 'lodash/sampleSize'

  export default {
    mixins: [
      ContentSelectionsEN,
      ContentSelectionsFR
    ],

    props: {
      sampleSizeEach: Number
    },

    components: {
      ContentSelection
    },

    methods: {
      getContentSelections () {
        if (this.$language.current.startsWith('fr_')) {
          return this.sampleIfNeeded(this.contentSelectionsFR)
        }

        return this.sampleIfNeeded(this.contentSelectionsEN)
      },

      sampleIfNeeded (objects) {
        const videos = []
        const channels = []
        const instances = []

        for (const o of objects) {
          if (o.type === 'video') videos.push(o)
          else if (o.type === 'channel') channels.push(o)
          else if (o.type === 'instance') instances.push(o)
          else console.error('Unknown content selection type %s.', o.type)
        }

        return sampleSize(videos, this.sampleSizeEach || videos.length)
          .concat(sampleSize(channels, this.sampleSizeEach || channels.length))
          .concat(sampleSize(instances, this.sampleSizeEach || instances.length))
      }
    }
  }
</script>
