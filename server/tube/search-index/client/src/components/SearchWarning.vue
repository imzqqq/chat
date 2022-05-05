<template>
  <div class="block-warning" v-bind:class="{ highlight: highlight }" >
    <img src="/img/sepia-warning.svg" alt="">

    <div v-translate>
      <strong>%{indexName}</strong> displays videos and channels that match your search but is not the publisher, nor the owner.
      If you notice any problems with a video, report it to the administrators on the Tube website where the video is published.
    </div>
  </div>
</template>

<style lang="scss">
  @import '../scss/_variables';

  .block-warning {
    font-style: italic;
    color: $orange;
    display: flex;
    align-items: center;
    margin: 20px auto;

    img {
      display: none;
      margin-right: 20px;
      height: 70px;
    }

    &.highlight {
      img {
        display: block;
      }

      max-width: 800px;
      color: $brown;
      border: 3px solid #f4af81;
      border-radius: 10px;
      font-style: normal;
      padding: 10px 20px;
    }
  }

  @media screen and (max-width: $small-screen) {
    .block-warning img {
      display: none !important;
    }
  }


</style>

<script lang="ts">
  import { defineComponent } from 'vue'

  export default defineComponent({
    props: {
      indexName: String,
      highlight: Boolean
    }
  })
</script>
