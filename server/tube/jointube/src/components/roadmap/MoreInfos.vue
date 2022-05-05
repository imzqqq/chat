<template>
  <section>
    <b-container
      id="more"
      class="ombre"
    >
      <b-row>
        <b-col cols="12">
          <h3 class="section-title mt-5">
            <div v-translate>
              Why progressive fundraising over 6 months?
            </div>
            <div class="border-title"></div>
          </h3>
        </b-col>
        <b-col lg="4">

          <p v-translate>
            At a time when no one knows what the future holds, we deem it inappropriate to start
            a crowdfunding campaign and threaten not to do our best on Tube if we don't get the necessary funds.
          </p>

          <p v-translate>
            We believe in the public utility of Tube, so much so that we commit to working on it
            for six months to make this v3 happen, even if we must do it with our own funds
            (which <a href="https://framablog.org/2019/11/12/tube-has-worked-twice-as-hard-to-free-your-videos-from-youtube/">we had already done for v2</a>).
          </p>

          <p v-translate>
            We still hope that by sharing this roadmap as widely as possible,
            some of you will support us in our approach with a donation that will allow us to fund this project.
          </p>
        </b-col>

        <b-col lg="8">
          <b-card
            v-for="(palier, i) in stepsContent" :key="i"
            v-show="i === activeStep"
            class="mt-0"
          >
            <b-card-body>
              <h4>
                <span
                  v-translate
                  class="right-space"
                >
                  Learn more on
                </span>
                <b-dropdown
                  right
                  :text="stepsTitle[i]"
                  variant="outline-secondary"
                >
                  <b-dropdown-item
                    v-for="(step, j) in steps"
                    :key="j"
                    @click="activeStep = j"
                  >
                    {{ stepsTitle[j] }}
                  </b-dropdown-item>
                </b-dropdown>
              </h4>

              <div v-html="stepsContent[i]"></div>
            </b-card-body>
          </b-card>
        </b-col>
      </b-row>
    </b-container>
  </section>
</template>

<style lang="scss">
  @import '../../scss/_variables.scss';

  #more {
    padding-top: 0;

    .card {
      border: 3px solid transparent;
      border-image: linear-gradient(to left bottom, $one, #fff 15%, #fff 85%, $one) 1;
      margin: 30px auto;
      background: linear-gradient(to bottom right, #fff, lighten($one, 15%));

      h4:after {
        content: " ";
        border-bottom: 5px solid $one;
        display: block;
        max-width: 60px;
        margin: 10px 0 20px;
      }

      &:nth-of-type(2) {
        border-image: linear-gradient(to left bottom, $two, #fff 15%, #fff 85%, $two) 1;
        background: linear-gradient(to bottom right, #fff, lighten($two, 15%));

        h4:after {
          border-bottom: 5px solid $two;
        }
      }

      &:nth-of-type(3) {
        border-image: linear-gradient(to left bottom, $three, #fff 15%, #fff 85%, $three) 1;
        background: linear-gradient(to bottom right, #fff, lighten($three, 15%));

        h4:after {
          border-bottom: 5px solid $three;
        }
      }

      &:nth-of-type(4) {
        border-image: linear-gradient(to left bottom, $four, #fff 15%, #fff 85%, $four) 1;
        background: linear-gradient(to bottom right, #fff, lighten($four, 15%));

        h4:after {
          border-bottom: 5px solid $four;
        }
      }
    }

    .donut > div,
    .legend > ul {
      margin: 0 auto;
      display: inline-block;
    }
  }
</style>

<script>
  import {
    BDropdown,
    BCard,
    BCardBody,
    BRow,
    BContainer,
    BCol,
    BDropdownItem
  } from 'bootstrap-vue'

  export default {
    components: {
      BDropdown,
      BDropdownItem,
      BCard,
      BCardBody,
      BRow,
      BContainer,
      BCol
    },
    props: {
      steps: {
        type: Array,
        required: true
      },
      active: {
        type: Number,
        required: true
      }
    },
    data: function () {
      return {
        activeStep: this.active,
        stepsTitle: [
          this.$gettext('Global search'),
          this.$gettext('Moderation tools'),
          this.$gettext('Plugins & Playlists'),
          this.$gettext('Live streaming')
        ],
        stepsContent: [
          this.$gettext(`
            <p>Today, when searching for videos on a Tube instance, it is hard to
            <strong>find content that is not in the federation bubble</strong> of that instance.</p>

            <p>We will create a <strong>server that indexes all videos</strong>
            and channels from all Tube instances that are listed on
            <a href="https://instances.joinpeertube.org/">the public directory</a>.
            The code for this indexing engine will be open source,
            so that anyone can host their own by setting their own eligibility rules.</p>

            <p>Each Tube instance administrator will be able to choose to <strong>
            use one of these indexing engines for their instance's search bar</strong>.</p>

            <p>In addition, <strong>announcements</strong>
            will allow instance administrators to display information to visitors.</p>
          `),

          this.$gettext(`
            <p>While each new version of Tube has brought <a
            href="https://joinpeertube.org/faq#tube-developers-did-not-add-moderation-tools">new moderation features</a>,
            there is still much to be done to <strong>facilitate moderation</strong> of content and accounts within the fediverse.
            That's why we want to <strong>dedicate a month to the development of new moderation tools</strong>.</p>

            <p>The list of features we have to develop or improve is long: reports logs and monitoring, moderation history,
            comments moderation, moderation reports related to an account, feedback on steps taken (or not) following a report,
            <strong>fight against spam</strong>, sharing blocklists of instances, accounts or content...</p>

            <p>This list is being built
            <a href="https://github.com/Chocobozzz/Tube/labels/Component%3A%20Moderation%20%3Agodmode%3A"><strong>in collaboration with the community</strong></a>,
            and it is with these needs in mind that we will try to prioritize the moderation tools developed during this month.</p>
          `),

          this.$gettext(`
            <p>While it is easy to embed a Tube video on a website or social media, the same cannot be said for playlists.
            We want to rework the <strong>embed and display of playlists</strong> on third party websites.</p>

            <p>We also want to <strong>allow two clips of the same video to be displayed in a playlist</strong>. Combined with the ability to
            extract a specific piece of video in a playlist, <strong>playlists will thus become very useful remixing tools</strong>,
            for example for educational purposes.</p>

            <p>Plugins allow anyone to contribute to new Tube features by coding them into Tube, and offer them to instance
            administrators. We want to <strong>improve the plugin system</strong> to offer more interaction possibilities.</p>

            <p>In order to put ourselves in their contributors' shoes, we are going to create a few plugins of our own,
            for example one to <strong>display information over a video to annotate it</strong> at a specific point in the viewing process.</p>

            <p>Finally, we want to work on <strong>selecting a set of plugins to showcase</strong> them on joinpeertube.org.</p>
          `),

          this.$gettext(`
            <p>The biggest challenge of live streaming on Tube is to <strong>get the live stream to be peer-to-peer</strong>.
            After extensive testing, we believe this is possible.</p>

            <p>By using the HLS technology that has been built into the Tube video player since version 1.3, we believe
            we can <strong>create a live stream with one minute of lag between streamers and viewers</strong>.</p>

            <p>We want to lay the technological foundation for this live stream feature, so <strong>this first version of
            Tube Live will be minimalist</strong>. There will be no stream captation tools, no built-in chat, no react emojis...</p>

            <p>However, we want to make sure that <strong>once the live stream is over, the video will be processed and
            added to the creator's channel</strong>, with every requirement for proper federation checked.</p>
          `)
        ]
      }
    }
  }
</script>
