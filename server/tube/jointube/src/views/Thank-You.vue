<template>
  <main id="thank-you">
    <div class="blocks">

      <div class="title-block">
        <h1 v-translate>Thank you very much for your donation to the Tube project!</h1>

        <div class="separator"></div>
      </div>

      <div class="one-column">
        <p v-translate>Thanks to you, Framasoft’s teams will be able to continue and expand the work already started on this software.</p>
      </div>

      <img class="tube-mascot" :src="buildImgUrl('mascot/happy.png')" alt="">

      <div class="one-column">
        <p v-translate>
          The success of this fundraising depends only on you: <strong>we need you</strong> to raise awareness about this roadmap, so we can share our enthousiasm about Peertube's v3.
        </p>

        <p v-translate>Feel free <strong>to share your gesture</strong> on your favourite social networks and to talk about Tube around you!</p>
      </div>
    </div>

    <ShareAndFollow />
  </main>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';

  #thank-you {
    .blocks {
      margin: 60px auto 100px auto;

      .title-block {
        max-width: 85%;

        h1 {
          font-size: 26px;
          line-height: 1.5;
        }
      }

      .one-column {
        margin: 20px auto;
      }

      img {
        display: block;
        margin: auto;
      }
    }
  }

</style>

<script>
  import ShareAndFollow from '../components/roadmap/ShareAndFollow.vue'

  export default {
    components: {
      ShareAndFollow
    },

    metaInfo: function () {
      return {
        title: this.$gettext('Thank you')
      }
    }
  }
</script>
