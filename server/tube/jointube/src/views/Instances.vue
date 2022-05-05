<template>
  <main id="instances">
    <div class="blocks">

      <div class="title-block">
        <h1 v-translate>Create an account</h1>

        <p v-translate>
          If you would like to interact with videos (like, comment, download...), subscribe to channels, create playlists or play videos,
          then all you have to do is <strong>create an account on the Tube instance of your choice.</strong>
        </p>

        <a href="#instances-list" class="jpt-button">
          <span v-translate>See the instances list</span>
        </a>
      </div>

      <div class="block-with-image step-1">
        <div>
          <img :src="buildImgUrl('tube-federation-multiplicity.jpg')" alt="Multiple instances illustration">
        </div>

        <div>
          <div class="step" v-translate>1. Find the instance that suits you best</div>

          <p v-translate>
            By <strong>filtering according to your profile</strong> (video maker or viewer), <strong>topics</strong> that you are
            looking for or <strong>languages</strong> you speak, <strong>find an instance whose rules match your needs!</strong>
          </p>
        </div>
      </div>

      <div class="block-with-image step-2">
        <div>
          <div class="step" v-translate>2. Create your account and enjoy Tube</div>

          <p v-translate>
            A username, an email, a password and you can already <strong>enjoy all the features of Tube!</strong>
          </p>
        </div>

        <div>
          <img :src="buildImgUrl('tube-upload.jpg')" alt="Tube upload illustration">
        </div>
      </div>

      <instances-list></instances-list>
    </div>
  </main>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';
  @import '../scss/_mixins.scss';

  #instances {
    .blocks {
      margin: 60px auto;
      width: 770px;
    }

    .title-block {
      .jpt-button {
        margin-top: 25px;
        border: 2px solid $orange;
        min-width: 225px;
        height: 35px;
      }
    }

    .block-with-image {
      display: flex;
      justify-content: center;
      align-items: center;
      margin-bottom: 100px;

      & > div {
        flex-basis: 100%;

        img {
          width: 370px;
          height: 210px;
        }
      }

      & > * {
        margin-right: 20px;
      }

      .step {
        @include proza-semi-bold;

        font-size: 24px;
        margin-bottom: 20px;
      }
    }

    @media screen and (max-width: $small-screen) {
      .blocks {
        width: auto;
      }

      .block-with-image {
        flex-direction: column;

        & > * {
          margin-right: 0;
        }

        &.step-2 {
          flex-direction: column-reverse;
        }

        & > div {
          img {
            margin-bottom: 20px;
            width: 100%;
            height: auto;
          }
        }
      }
    }
  }
</style>

<script>
  import InstancesList from '../components/InstancesList'

  export default {
    components: {
      InstancesList
    },

    metaInfo: function () {
      return {
        title: this.$gettext('Tube instances')
      }
    }
  }
</script>
