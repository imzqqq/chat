<template>
  <main id="help">
    <div class="blocks">

      <div class="title-block">
        <h1 v-translate>Help</h1>

        <p v-translate>Questions on Tube? Need help? You've come to the right place!</p>

        <div class="separator"></div>
      </div>

      <div class="block-with-image understand">
        <div class="image-block">
          <img width="183px" height="200px" :src="buildImgUrl('mascot/pointing.svg')" alt="">
        </div>

        <div class="text-block">
          <h2 v-translate>Better understand and use Tube</h2>

          <p v-translate>You have a question?</p>
          <div class="bottom-link-wrapper">
            <router-link :to="buildRoute('/faq')" class="bottom-link">
              <span class="text" v-translate>Discover our FAQ</span>
              <icon-right></icon-right>
            </router-link>
          </div>

          <p v-translate>Need a detailed guide?</p>
          <div class="bottom-link-wrapper">
            <a class="bottom-link" href="https://tube.docs.imzqqq.top" target="_blank" rel="noopener noreferrer">
              <span class="text" v-translate>Read the documentation</span>
              <icon-right></icon-right>
            </a>
          </div>
        </div>
      </div>

      <div class="block-with-image community">
        <div class="text-block">
          <h2 v-translate>Ask questions to the community</h2>

          <div class="bottom-link-wrapper">
            <a href="https://framacolibri.org/c/tube" class="bottom-link" target="_blank" rel="noopener noreferrer">
              <span class="text" v-translate>Go to the forum</span>
              <icon-right></icon-right>
            </a>
          </div>
        </div>

        <div class="image-block">
          <img width="189px" height="205px" :src="buildImgUrl('help/cat.png')" alt="">
        </div>
      </div>

      <div class="block-with-image administrators">
        <div class="image-block">
          <img width="158px" height="219px" :src="buildImgUrl('help/panda.png')" alt="">
        </div>

        <div class="text-block">
          <h2 v-translate>For Tube admins</h2>

          <div class="bottom-link-wrapper">
            <a class="bottom-link" href="https://tube.docs.imzqqq.top/install-any-os" target="_blank" rel="noopener noreferrer">
              <span class="text" v-translate>Install Tube</span>
              <icon-right></icon-right>
            </a>
          </div>

          <div class="bottom-link-wrapper">
            <a class="bottom-link" href="https://tube.docs.imzqqq.top/install-any-os?id=upgrade" target="_blank" rel="noopener noreferrer">
              <span class="text" v-translate>Upgrade Tube</span>
              <icon-right></icon-right>
            </a>
          </div>

          <div class="bottom-link-wrapper">
            <a class="bottom-link" href="https://tube.docs.imzqqq.top/admin-following-instances" target="_blank" rel="noopener noreferrer">
              <span class="text" v-translate>Administer Tube</span>
              <icon-right></icon-right>
            </a>
          </div>
        </div>
      </div>

      <div class="block-with-image contribute">
        <div class="text-block">
          <h2 v-translate>Contribute to Tube</h2>

          <div class="bottom-link-wrapper">
            <a class="bottom-link" href="https://tube.docs.imzqqq.top/contribute-getting-started" target="_blank" rel="noopener noreferrer">
              <span class="text" v-translate>How to contribute?</span>
              <icon-right></icon-right>
            </a>
          </div>
        </div>

        <div class="image-block">
          <img width="242px" height="221px" :src="buildImgUrl('help/dog.png')" alt="">
        </div>
      </div>

    </div>
  </main>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';

  #help {
    .blocks {
      margin: 60px auto;
      width: 500px;
    }

    .block-with-image {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 100px;

      .image-block + .text-block,
      .text-block + .image-block {
        margin-left: 40px;
      }

      .text-block {
        text-align: right;
        width: 100%;

        p {
          margin-bottom: 0;
        }

        h2 {
          font-size: 24px;
          margin-bottom: 25px;
        }

        .bottom-link-wrapper {
          margin-top: 5px;
          margin-bottom: 0;
        }

        .bottom-link-wrapper + * {
          margin-top: 40px;
        }
      }

      .image-block + .text-block {
        text-align: left;
      }
    }

    @media screen and (max-width: $small-screen) {
      .title-block {
        margin-bottom: 60px;
      }

      .blocks {
        width: auto;
      }

      .block-with-image {
        flex-direction: column;

        &.community,
        &.contribute {
          flex-direction: column-reverse;
        }

        .text-block {
          margin-left: 0 !important;
        }

        .text-block {
          text-align: left;
        }

        .image-block {
          margin: 40px 0 !important;
        }
      }
    }
  }
</style>

<script>
  import IconRight from '../components/icons/IconRight'

  export default {
    components: {
      IconRight
    },

    metaInfo: function () {
      return {
        title: this.$gettext('Help')
      }
    }
  }
</script>
