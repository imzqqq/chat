<template>
  <main id="home">

    <a v-translate class="sr-only sr-only-focusable" href="#what-is-tube">
      Skip to content
    </a>

    <h1 v-translate>Tube, a free software to take back control of your videos</h1>

    <section class="presentation">
      <div class="first-row">
        <img class="brand" :src="buildImgUrl('brand.png')" alt="Logo of Tube, developed by Framasoft"/>

        <div class="description" v-translate>Free software to take back control of your videos</div>
      </div>

      <div class="search-bar" style="display: none">
        <sepia-search-image></sepia-search-image>

        <div class="input-search-group">
          <input
            type="text" id="search" name="search" v-bind:aria-label="searchPlaceholder" v-bind:placeholder="searchPlaceholder"
            v-on:keyup.enter="doSearch()" v-model="searchInputValue"
          >
          <icon-search v-on:click="doSearch()"></icon-search>
        </div>
      </div>

      <!-- <img class="tube-mascot" :src="buildImgUrl(mascotPaths[currentMascot])" @click="switchMascot()" alt=""> -->

      <div class="two" id="home-info">
        <div>
          <img :src="buildImgUrl('v4-banner.jpg')" alt="">
        </div>

        <h4 v-translate>
          V4 is here!
        </h4>

        <p v-translate>
          Tube V4 with channel customization, playlists search, custom instance homepage and more video filters is out!
        </p>

        <p class="text-right" v-translate>
          <a href="https://framablog.org/2021/11/30/tube-v4-more-power-to-help-you-present-your-videos/" target="_blank" class="bottom-link">
            Read the blog post
          </a>
        </p>
      </div>

      <div class="marketing">
        {{ marketingText }}
      </div>

      <div class="buttons-row">
        <a href="#what-is-tube" class="jpt-button jpt-big-button-icon">
          <icon-logo></icon-logo>

          <span v-translate>What is Tube?</span>
        </a>

        <router-link :to="{ path: '/instances', hash: 'instances-list' }" class="jpt-button jpt-big-button-icon instances-list">
          <icon-instance></icon-instance>

          <translate>See the instances list</translate>
        </router-link>
      </div>
    </section>

    <section class="contents-selection">
      <h2 class="section-title">
        <div v-translate>Discover our content selection</div>
        <div class="border-title"></div>
      </h2>

      <contents-selection :sample-size-each="1"></contents-selection>

      <div class="bottom-link-wrapper">
        <router-link to="/contents-selection" class="bottom-link">
          <span class="text" v-translate>Browse contents</span>
          <icon-right></icon-right>
        </router-link>
      </div>
    </section>

    <section id="what-is-tube">
      <h2 class="section-title">
        <div>
          <!-- FIXME: add v-translate to the div, but it does not work with vue-gettext because of the bind src attribute -->
          <translate>What is</translate>
          <img class="brand-title" :src="buildImgUrl('brand.png')" alt="Tube"/>
          <translate>?</translate>
        </div>
        <div class="border-title"></div>
      </h2>

      <div class="one-column">
        <div class="iframe-responsive">
          <iframe sandbox="allow-same-origin allow-scripts allow-popups" title="What is Tube?" v-bind:src="getIframeUrl()" frameborder="0" allowfullscreen></iframe>
        </div>

        <div>
          <div class="citation">
            <div class="left-bar"></div>

            <div class="text" v-translate>
              Tube aspires to be a <strong>decentralized and free/libre alternative</strong> to video broadcasting services.
            </div>
          </div>

          <p v-translate>
            Our aim is not to replace them, but rather to simultaneously offer something else, with different values.
          </p>
        </div>

        <img :src="buildImgUrl('tube-screenshot.jpg')" alt=""/>
      </div>

      <div class="federation">
        <div class="subtitle">
          <translate>A federation of interconnected hosting services</translate>
          <div class="border-title"></div>
        </div>

        <div class="one-column">
          <img :src="buildImgUrl('tube-federation-multiplicity.jpg')" alt=""/>

          <div>
            <div v-translate>Tube is not meant to become a huge platform that would centralize videos from all around the world.</div>

            <div class="citation">
              <div class="left-bar"></div>

              <div class="text" v-translate>
                Rather, it is <strong>a network of inter-connected small videos hosters</strong>.
              </div>
            </div>
          </div>

          <p v-translate>
            Anyone with a modicum of technical skills can host a Tube server, aka an instance.
            Each instance hosts its users and their videos.
            In this way, <strong>every instance is created, moderated and maintained independently by various administrators.</strong>
          </p>

          <div class="bottom-link-wrapper">
            <router-link :to="{ path: '/instances', hash: 'instances-list' }" class="bottom-link">
              <span class="text" v-translate>Discover Tube instances</span>
              <icon-right></icon-right>
            </router-link>
          </div>
        </div>

        <div class="one-column">
          <p v-translate>
            You can still watch from your account videos hosted by other instances though
            if the administrator of your instance had previously connected it with other instances.
          </p>

          <div class="citation">
            <div class="left-bar"></div>

            <div class="text" v-translate>This is just how a <strong>federation</strong> works!</div>
          </div>
        </div>

        <div class="one-column">
          <img :src="buildImgUrl('tube-federation-2-instances.jpg')" alt=""/>

          <p v-translate>
            And there's more! Tube uses <a target="_blank" rel="noopener noreferrer" href="https://activitypub.rocks">ActivityPub</a>,
            a federating protocol that <strong>allows you to interact with other software</strong>, provided they also use this protocol.
            For example, Tube and Mastodon -a Twitter alternative- are connected:
            <strong>you can follow a Tube user from Mastodon</strong> (the latest videos from the Tube account you follow will
            appear in your feed), <strong>and even comment on a Tube-hosted video directly from your Mastodon's account.</strong>
          </p>
        </div>

        <!--        TODO: find interesting resource regarding federation-->
        <!--        <div class="bottom-link-wrapper">-->
        <!--          <a href="#" class="bottom-link">-->
        <!--            <span class="text" v-translate>Learn more about the federation</span>-->
        <!--            <icon-right></icon-right>-->
        <!--          </a>-->
        <!--        </div>-->
      </div>

      <div class="free-software">
        <div class="subtitle">
          <translate>Open-source, free/libre license code</translate>
          <div class="border-title"></div>
        </div>

        <div class="one-column">
          <img :src="buildImgUrl('tube-free-software.jpg')" alt=""/>

          <p v-translate>
            Mainstream online video broadcasting services make money off of your data by analyzing your interactions
            so that they can then bombard your with targeted advertising.
          </p>

          <div class="citation">
            <div class="left-bar"></div>

            <div class="text" v-translate>
              Peertube is not subject to any corporate monopoly, does not rely on ads and <strong>does not track you.</strong>
            </div>
          </div>

          <p v-translate>
            Most importantly, <strong>you are a person to Tube, not a product in need of profiling so as to be stuck in video
            loops.</strong>
            For example, Tube doesn't use any biased recommendation algorithms to keep you online for hours on end.
          </p>

          <p v-translate>
            All of this is made possible by Peertube's free/libre license (GNU-AGPL).
            Its code is a digital "common", that belongs to everybody, instead of a secret formula that belongs to Google
            (in the case of Youtube) or to Vivendi/Bolloré (Dailymotion).
            This free/libre license <strong>guarantees our fundamental freedoms as users and allows many contributors to offer evolutions
            and new features.</strong>
          </p>
        </div>

        <!--        TODO: find interesting resource regarding free software -->
        <!--        <div class="bottom-link-wrapper">-->
        <!--          <a href="#" class="bottom-link">-->
        <!--            <span class="text" v-translate>Learn more about free/libre software </span>-->
        <!--            <icon-right></icon-right>-->
        <!--          </a>-->
        <!--        </div>-->
      </div>

      <div id="you-are-a-video-maker">
        <div class="subtitle">
          <translate>Are you a video maker?</translate>
          <div class="border-title"></div>
        </div>

        <div class="one-column">
          <img :src="buildImgUrl('tube-upload.jpg')" alt=""/>

          <div class="citation">
            <div class="left-bar"></div>

            <div class="text" v-translate>
              With Tube, choose <strong>your hosting company and the rules you believe in.</strong>
            </div>
          </div>

          <div v-translate>
            YouTube has clearly gone astray: its hoster, Google-Alphabet, can enforce its ContentID system (the infamous "Robocopyright")
            or its videos recommendation system, all of which appear to be as obscure as unfair.
          </div>

          <div class="citation">
            <div class="left-bar"></div>

            <div class="text" v-translate>
              Direct contact with a human-scale hoster allows for two things: you no longer are the client of a huge tech company,
              and <strong>you can nurture a special relationship with your hoster, who distributes your data.</strong>
            </div>
          </div>

          <div v-translate>
            With Tube, you get to choose your hosting provider according to their terms of use, such as their disk space limit per user,
            their moderation policy, who they chose to federate with... You are not speaking with a huge tech company, so you can
            talk it out in case of any issue, need, desire...
          </div>
        </div>

        <router-link :to="{ path: '/instances', hash: 'instances-list' }" class="jpt-button discover-instances">
          <translate>Browse/discover Tube instances</translate>
        </router-link>
      </div>

      <div class="p2p">
        <div class="subtitle">
          <translate>About peer-to-peer broadcasting and watching</translate>
          <div class="border-title"></div>
        </div>

        <div class="one-column">
          <img :src="buildImgUrl('tube-p2p.jpg')" alt=""/>

          <div class="citation">
            <div class="left-bar"></div>

            <div class="text" v-translate>
              The Tube software can, whenever necessary, use a peer-to-peer protocol (P2P) to broadcast viral videos,
              <strong>lowering the load of their hosts.</strong>
            </div>
          </div>

          <p v-translate>
            In this way, when you watch a video, your computer contributes to its broadcast.
            If a lot of people are watching the same video at the same time, their browser automatically send smalls pieces of the
            video to the other viewers.
            <strong>The server resources are not over-exploited</strong>: the stream is split, the network optimized.
          </p>

          <p v-translate>
            It might not look like it, but thanks to peer-to-peer broadcasting, popular video makers and their videos are no longer
            forced to be hosted by big companies, whose infrastructure can stand thousands of views at the same time... or to pay for a
            robust but extremely expensive independent video host.
          </p>
        </div>
      </div>

      <div id="your-move">
        <div class="subtitle">
          <translate>Your move!</translate>
          <div class="border-title"></div>
        </div>

        <div class="one-column">
          <img :src="buildImgUrl('your-move.jpg')" alt=""/>
        </div>

        <div class="buttons-row explore-create-account">
          <router-link to="/contents-selection" class="jpt-button">
            <span v-translate>Browse contents</span>
          </router-link>

          <div class="create-account-block">
            <router-link to="/instances" class="jpt-button">
              <span v-translate>Sign up</span>
            </router-link>
            <div class="jpt-button-legend" v-translate>
              Enjoy every feature: history, subscriptions, playlists, notifications...
            </div>
          </div>

        </div>
      </div>
    </section>

    <section id="behind-tube">
      <h2 class="section-title">
        <div>
          <!-- FIXME: add v-translate to the div, but it does not work with vue-gettext because of the bind src attribute -->
          <translate>Who is behind</translate>
          <img class="brand-title" :src="buildImgUrl('brand.png')" alt="Tube"/>
          <translate>?</translate>
        </div>

        <div class="border-title"></div>
      </h2>

      <div class="two-columns">
        <img class="framasoft-logo" :src="buildImgUrl('framasoft-big-logo.png')" alt=""/>

        <div>
          <p v-translate>
            Peertube is a <strong>free/libre software funded by a French non-profit organization</strong>: Framasoft
          </p>

          <p v-translate>
            Our organization started in 2004, and now devotes itself <strong>to popular education about digital technology issues.</strong>
            We are a small structure of less than 40 members and under 10 employees, well-known for the De-google-ify Internet project,
            when we offered 34 ethical and alternative online tools. As a public interest organization, <strong>over 90% of our funding
            comes from donations</strong> (tax deductible for French taxpayers).
          </p>

          <p v-translate>
            Thanks to our <a href="/hall-of-fame" target="_blank">crowdfunding (from March to July 2018)</a>,
            <strong>Framasoft were able to employ Tube's main developer.</strong>
            After a beta release in March 2018, release 1 came out in November 2018.
            Since then, several intermediary releases have brought many features along.
            Several collectives have already created Tube hosts, laying the foundation for the federation.
          </p>

          <p>
            <strong v-translate>
              The more people use, support, and contribute to Tube, the quicker it will become a concrete alternative to platforms like
              YouTube.
            </strong>
          </p>
        </div>
      </div>

      <a href="https://framasoft.org/#soutenir" target="_blank" rel="noopener noreferrer" class="jpt-button">
        <span v-translate>Donate to Framasoft</span>
      </a>
    </section>
  </main>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';
  @import '../scss/_mixins.scss';

  #home {
    // Just for page structure
    h1 {
      visibility: hidden;
      height: 0;
      width: 0;
      margin: 0;
    }

    .buttons-row {
      display: flex;
      justify-content: space-between;
      padding: 0 20px;

      .jpt-button {
        width: 330px;
        min-height: 50px;
        height: fit-content;
      }

      @media screen and (max-width: $responsive-screen) {
        padding: 0;

        .jpt-button {
          margin: 0 3px 0 0;
        }

        &.explore-create-account {
          flex-direction: column;

          .create-account-block {
            margin-top: 50px;
          }
        }
      }

      @media screen and (max-width: $small-screen) {
        .jpt-button {
          width: 100%;
        }
      }
    }

    .jpt-button-legend {
      margin-top: 5px;
      max-width: 330px;
      font-size: 13px;
      text-align: center;
    }

    .presentation {
      .first-row {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding-top: 70px;
        margin: auto;

        .brand {
          height: 47px;
          margin-bottom: 20px;
          margin-left: -10px;
        }

        div {
          flex-basis: 100%;
        }

        .description {
          font-size: 24px;
          font-weight: 600;
          text-align: center;
        }

        @media screen and (max-width: $small-screen) {
          padding: 40px 0 0 0;

          .brand {
            height: 50px !important
          }

          .description {
            font-size: 20px;
          }
        }
      }

      .tube-mascot {
        display: block;
        margin: 30px auto 0;
      }

      .marketing {
        font-size: 16px;
        margin: 60px auto 30px auto;
        width: 535px;
        text-align: center;

        @media screen and (max-width: $small-screen) {
          width: 100%;
        }
      }

      .search-bar {
        margin-top: 30px;
        display: flex;
        align-items: center;
        flex-direction: column;

        > svg {
          height: 150px;
          margin-bottom: -6px;
          z-index: 100;
          overflow: visible;
        }

        .input-search-group {
          width: 100%;
          max-width: 500px;
          position: relative;

          input {
            width: 100%;
            height: 40px;
            border-radius: 30px;
            border: 1px solid rgba(0, 0, 0, 0.3);
            outline: none;
            padding: 10px 50px 10px 20px;
            text-overflow: ellipsis;

            &:focus, &:active {
              box-shadow: 0 2px 4px 0 rgba(80, 80, 82, 0.2);
            }
          }

          > svg {
            cursor: pointer;
            position: absolute;
            right: 15px;
            top: 9px;
          }
        }
      }
    }

    .contents-selection .section-title {
      // Reduce margin top now we have the search bar
      margin-top: 50px;
    }

    #you-are-a-video-maker {
      .discover-instances {
        min-width: 420px;
        width: fit-content;
        min-height: 50px;
        margin: 50px auto;
        font-size: 20px;
      }
    }

    #your-move {
      .one-column {
        margin-top: 50px;
      }

      .buttons-row {
        margin-top: 60px;

        .jpt-button {
          font-size: 20px;
        }
      }
    }

    #behind-tube {
      .framasoft-logo {
        flex-basis: 264px;
        min-width: 264px;
        height: 241px;
      }

      .jpt-button {
        height: 50px;
        min-width: 420px;
        width: fit-content;
        margin: 60px auto;
        font-size: 20px;
      }
    }

    #home-info {
      max-width: 540px;
      border-style: solid;
      border-top: none;
      border-right: none;
      border-width: 2px;
      border-radius: 0;
      margin: 50px auto;

      img {
        width: 100%;
      }

      $one:        #dab19d; // #ff7061;
      $two:       #f7ca8f;
      $three:       #acbe82; // #78cc8a;
      $four:          #9dc2d4; // #36bcd4;

      &.one {
        border-image: linear-gradient(to left bottom, $one, #fff 15%, #fff 85%, $one) 1;
        background: linear-gradient(to bottom right, #fff, lighten($one, 15%));
      }

      &.two {
        border-image: linear-gradient(to left bottom, $two, #fff 15%, #fff 85%, $two) 1;
        background: linear-gradient(to bottom right, #fff, lighten($two, 15%));
      }

      &.three {
        border-image: linear-gradient(to left bottom, $three, #fff 15%, #fff 85%, $three) 1;
        background: linear-gradient(to bottom right, #fff, lighten($three, 15%));
      }

      &.four {
        border-image: linear-gradient(to left bottom, $four, #fff 15%, #fff 85%, $four) 1;
        background: linear-gradient(to bottom right, #fff, lighten($four, 15%));
      }

      p {
        padding: 0 20px;
      }

      h4 {
        margin-top: 20px;
        text-align: center;
      }
    }
  }
</style>

<script>
  import IconInstance from '../components/icons/IconInstance.vue'
  import IconRight from '../components/icons/IconRight.vue'
  import ContentsSelection from '../components/ContentsSelection'
  import IconLogo from '../components/icons/IconLogo'
  import IconSearch from '../components/icons/IconSearch'
  import SepiaSearchImage from '../components/SepiaSearchImage'

  export default {
    components: {
      ContentsSelection,
      IconInstance,
      IconRight,
      IconLogo,
      IconSearch,
      SepiaSearchImage
    },

    metaInfo: {
      title: 'JoinTube',
      titleTemplate: null
    },

    computed: {
      searchPlaceholder () {
        return this.$gettext('Search for Tube videos, channels and playlists')
      },

      randomRoadmapImage () {
        return Math.random() * 4 | 0
      }
    },

    data () {
      const currentLanguage = this.$language.current.replace('_', '-')

      const marketingText = this.$gettext('Tube, developed by Framasoft, is the free and decentralized alternative to video platforms, providing you over %{ videos } videos published by %{ users } users and viewed over %{ views } million times')
      const interpolatedMarketingText = this.$gettextInterpolate(marketingText, { videos: Number(400000).toLocaleString(currentLanguage), users: Number(60000).toLocaleString(currentLanguage), views: Number(15).toLocaleString(currentLanguage) })

      return {
        mascotPaths: [
          'mascot/default.png',
          'mascot/arguing.png',
          'mascot/oh.png',
          'mascot/happy.png'
        ],
        searchInputValue: '',
        currentMascot: 0,
        marketingText: interpolatedMarketingText
      }
    },

    methods: {
      switchMascot () {
        this.currentMascot++
        this.currentMascot %= this.mascotPaths.length
      },

      getIframeUrl () {
        const currentLanguage = this.$language.current.split('_')[0]
        const params = '?subtitle=' + currentLanguage

        return 'https://framatube.org/videos/embed/9c9de5e8-0a1e-484a-b099-e80766180a6d' + params
      },

      doSearch (value) {
        window.open('https://sepiasearch.org/search?search=' + this.searchInputValue, '_blank')
      }
    }
  }
</script>
