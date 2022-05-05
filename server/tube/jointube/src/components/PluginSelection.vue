<template>
  <div class="plugin-selection" :id="title">
    <h4>
      <icon-plugin></icon-plugin>
      {{ title }}
    </h4>

    <h5>{{ subtitle }}</h5>

    <div class="img-and-description">
      <img :src="buildImgUrl('plugin-selection-previews/' + previewName)" alt="preview">

      <p>{{ description }}</p>
    </div>

    <a target="_blank" rel="noopener noreferrer" :href="url" class="jpt-button jpt-button-medium" v-translate>
      Plugin homepage
    </a>

  </div>
</template>

<style lang="scss">
  @import '../scss/_variables';

  .plugin-selection {
    h4 {
      font-weight: $font-semibold;
    }

    h5 {
      margin-bottom: 30px;
    }

    .img-and-description {
      display: flex;

      img {
        max-width: 50%;
        margin-right: 20px;
      }

      @media screen and (max-width: $small-screen) {
        flex-direction: column;

        img {
          max-width: 100%;
          margin-right: 0;
          margin-bottom: 10px;
        }
      }
    }

    .jpt-button {
      margin-left: auto;
      width: fit-content;
      min-width: 200px;
      height: 35px;
    }
  }
</style>

<script>
  import IconPlugin from './icons/IconPlugin.vue'

  export default {
    props: {
      title: String,
      subtitle: String,
      previewName: String,
      url: String,
      description: String
    },
    components: {
      IconPlugin
    }
  }
</script>
