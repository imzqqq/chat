<template>
  <div class="content-selection">

    <div>
      <h3 class="title-normal-screens">
        <div class="icon">
          <icon-video v-if="type === 'video'"></icon-video>
          <icon-channel v-if="type === 'channel'"></icon-channel>
          <icon-instance v-if="type === 'instance'"></icon-instance>
        </div>

        {{ title }}
      </h3>

      <div class="description">
        {{ description }}
      </div>

      <div class="tags">
        <div class="tag" v-for="tag in tags" :key="tag">{{ tag }}</div>
      </div>
    </div>

    <div>
      <h3 class="title-small-screens">
        <div class="icon">
          <icon-video v-if="type === 'video'"></icon-video>
          <icon-channel v-if="type === 'channel'"></icon-channel>
          <icon-instance v-if="type === 'instance'"></icon-instance>
        </div>

        {{ title }}
      </h3>

      <img :src="buildImgUrl('content-selection-thumbnails/' + thumbnailName)" alt="thumbnail"/>

      <a target="_blank" rel="noopener noreferrer" :href="url" class="jpt-button jpt-button-medium">
        <template v-if="type === 'video'">
          <div class="icon">
            <icon-video></icon-video>
          </div>
          <div v-translate>Watch the video</div>
        </template>

        <template v-if="type === 'channel'">
          <div class="icon">
            <icon-channel></icon-channel>
          </div>
          <div v-translate>Discover the channel</div>
        </template>

        <template v-if="type === 'instance'">
          <div class="icon">
            <icon-instance></icon-instance>
          </div>
          <div v-translate>Go on the instance</div>
        </template>
      </a>
    </div>
  </div>

</template>

<style lang="scss">
  @import '../scss/_variables';

  .content-selection {
    display: flex;
    flex-direction: row-reverse;

    .title-small-screens {
      display: none !important;
    }

    h3 {
      font-size: 24px;
      display: flex;
      align-items: center;

      .icon {
        margin-right: 10px;
        position: relative;
        top: -2px;
      }
    }

    > div + div {
      margin-right: 20px;

      img {
        display: block;
        width: 250px;
        height: 137px;
        border-radius: 3px;
      }

      a.jpt-button {
        display: flex;
        flex-direction: row !important;
        margin-top: 30px;
        width: 250px;
        height: 35px;

        .icon {
          margin-right: 10px;

          svg {
            vertical-align: sub;
            width: 20px;
            height: 20px;
          }
        }
      }
    }

    > div:first-child {
      min-height: 170px;

      .description {
        font-size: 16px;
      }

      .tags {
        display: flex;
        flex-wrap: wrap;

        .tag {
          margin: 10px;

          &:first-child {
            margin-left: 0;
          }
        }
      }
    }

    @media screen and (max-width: $small-screen) {
      flex-direction: column-reverse;
      align-items: center;

      .title-small-screens {
        display: flex !important;
        width: fit-content;
        margin-bottom: 5px;
      }

      .title-normal-screens {
        display: none;
      }

      .tags {
        display: flex;
        width: 100%;
        margin-top: 10px;

        .tag {
          margin: 5px !important;
          min-width: auto;

          &:first-child {
            margin-left: 0;
          }
        }
      }

      > div + div {
        justify-content: center;
        margin-right: 0;
      }

      > div:first-child {
        align-items: center;
        display: flex;
        flex-direction: column-reverse;
        margin-bottom: 20px;
      }
    }
  }
</style>

<script>
  import IconVideo from './icons/IconVideo.vue'
  import IconInstance from './icons/IconInstance.vue'
  import IconChannel from './icons/IconChannel.vue'

  export default {
    props: {
      type: String,
      title: String,
      thumbnailName: String,
      url: String,
      tags: Array,
      description: String
    },
    components: {
      IconVideo,
      IconInstance,
      IconChannel
    }
  }
</script>
