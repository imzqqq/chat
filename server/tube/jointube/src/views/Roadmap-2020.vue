<template>
  <div>
    <main id="roadmap">
      <section class="presentation d-none d-sm-flex">
        <b-container>
          <b-row
            align-v="center"
            align-h="around"
          >
            <b-col md="4" >
              <h2
                v-translate
                class="text-secondary"
              >
                Let's pave the way to Tube live streaming in v3!
              </h2>
            </b-col>
            <b-col md="5" />
          </b-row>
        </b-container>
      </section>

      <section
        id="funding2"
        :class="fundingClass"
      >
        <b-container>
          <b-row
            align-v="center"
            align-h="around"
          >
            <b-col md="4" />
            <!-- Right column -->
            <b-col
              class="funding"
              md="5"
            >
              <p
                class="h3"
                v-translate
              >
                Support the development of Tube!
              </p>
              <p v-translate>
                Donate, share: we will need all the help we can get to complete this roadmap.
              </p>

              <b-row>
                <b-col cols="12">
                  <b-progress
                    :max="100"
                    variant="info"
                    class="h2"
                  >
                    <b-progress-bar :value="Math.round(this.progress.total * 100 / steps[3])">
                      <b>{{ Math.round(this.progress.total * 100 / steps[3]) }}%</b>
                    </b-progress-bar>
                  </b-progress>
                  <div class="progress-meter">
                    <div
                      v-for="(file, index) in ['four', 'three', 'two', 'one']"
                      :key="index"
                      class="meter meter-right"
                      :style="`width: ${milestonesContent[file].width}%`"
                    >
                      <span class="meter-text">
                        <span class="money">{{ formatCurrency(steps[3 - index]) }}</span><br />
                        <span class="label">{{ milestonesContent[file].subtitle }}</span><br />
                        <span class="date">{{ milestonesContent[file].date }}</span>
                      </span>
                    </div>
                  </div>
                </b-col>
                <b-col cols="7">
                  <h3 id="donations">
                    {{ formatCurrency(progress.total) }}
                  </h3>

                  <span class="text-muted" v-if="days">
                    <translate :translate-n="days" translate-plural="raised in %{ days } days">raised in %{ days } day</translate>
                  </span>
                </b-col>
                <b-col cols="5">
                  <h3 id="donators">{{ count }}</h3>
                  <span
                    class="text-muted"
                    v-translate
                  >
                    contributors
                  </span>
                </b-col>
                <b-col>
                  <b-button
                    block size="lg"
                    variant="primary mb-0"
                    href="#support"
                    rel="noopener noreferrer"
                    disabled
                    v-translate
                  >
                    Contribute
                  </b-button>
                </b-col>
              </b-row>
            </b-col>
          </b-row>
        </b-container>
      </section>

      <section>
        <b-container class="ombre">
          <b-row align-h="around">
            <!-- Main column -->
            <!-- Story -->
            <b-col
              id="story"
              lg="9"
            >
              <div id="step">
                <h3 class="section-title mt-5">
                  <div v-translate>
                    A roadmap in 4 steps
                  </div>
                  <div class="border-title"></div>
                </h3>
                <p v-translate>
                  From June to November 2020, Framasoft will develop new features and improvements for Tube.
                  We have broken down this roadmap into 4 main steps, each with its own schedule and cost,
                  leading to the addition of peer-to-peer powered live streaming to Tube v3.
                </p>
                <p v-translate>
                  These developments are funded by donations made to our small nonprofit organization: Framasoft.
                  We will be raising funds progressively over the next six months of development, so that you can help us in our efforts.
                </p>

                <div
                  v-for="(file, index) in ['one', 'two', 'three', 'four']"
                  :key="index"
                >
                  <b-card
                    no-body
                    class="overflow-hidden mb-5"
                  >
                    <b-row no-gutters>
                      <b-col
                        md="6"
                        :order-md="(index % 2) * 12"
                      >
                        <h4 class="text-center font-weight-bolder">
                          <span v-html="milestonesContent[file].title"></span>
                          <br />
                          <i>{{ formatCurrency(steps[index]) }}</i>
                        </h4>

                        <b-card-img
                          :src="buildImgUrl(`roadmap/step-${file}.png`)"
                          class="rounded-0"
                        ></b-card-img>
                      </b-col>
                      <b-col
                        md="6"
                        class="d-flex align-items-center justify-content-center"
                      >
                        <b-card-body>
                          <b-card-text>
                            <h5 class="h4 text-center p-0">
                              <span
                                class="font-weight-bolder text-secondary"
                                v-html="milestonesContent[file].subtitle"
                              ></span><br />
                              <small
                                class="text-secondary font-italic"
                                v-html="milestonesContent[file].date"
                              ></small>
                            </h5>
                            <ul>
                              <li
                                v-for="(content, index) in milestonesContent[file].ul"
                                :key="index"
                                v-html="content"
                              ></li>
                            </ul>
                          </b-card-text>
                        </b-card-body>
                      </b-col>
                    </b-row>
                  </b-card>
                </div>
                <p class="text-italic text-center">
                  <a href="#more">
                    <span v-translate>Learn more about those steps</span>
                  </a>
                </p>
              </div>
            </b-col>
          </b-row>
        </b-container>
      </section>

      <section class="quote">
        <b-container>
          <b-row>
            <b-col>
              <blockquote class="blockquote text-right">
                <span>
                  <translate>"Another" world cannot be built using surveillance capitalism's tools.</translate>

                  <br />

                  <translate>
                    Making it easier to access content, sharing power and responsibilities, opening up to contributions, generalizing live
                    broadcasting thanks to peer-to-peer...
                    Tube’s v3 aims to consolidate it as an alternative to Google-Youtube and Facebook-Live that is more and more convivial.
                  </translate>
                </span>

                <footer class="blockquote-footer" v-translate>
                  <a href="https://framablog.org/2020/05/26/our-plans-for-tube-v3-progressive-fundraising-live-streaming-coming-next-fall">Read Framasoft's statement on the Framablog</a>
                </footer>
              </blockquote>
            </b-col>
          </b-row>
        </b-container>
      </section>

      <Team />

      <MoreInfos
        :steps="steps"
        :active="activeStep"
      />

      <a id="support" class="anchor"></a>
      <section style="display: none" id="action">
        <b-container class="ombre">
          <b-row align-h="center">
            <b-col
              lg="9"
            >
              <h3 class="section-title mt-5">
                <div v-translate>
                  Contribute
                </div>
                <div class="border-title"></div>
              </h3>
              <p v-translate>
                The money donated via this form will be exclusively dedicated to the Tube project.
              </p>
              <DonateForm />
            </b-col>
            <b-col lg="9">
              <!-- Share & follow -->
              <h3 class="section-title mt-5 mb-4">
                <div v-translate>
                  Share and follow
                </div>
                <div class="border-title"></div>
              </h3>
              <ShareAndFollow />
            </b-col>
          </b-row>
        </b-container>
      </section>
    </main>
  </div>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';
  @import '../../node_modules/fork-awesome/css/fork-awesome.min.css';

  /* Sections background */
  body.in-roadmap {

    #app.container {
      width: 100%;
      max-width: 100%;
      padding: 0;

      main {
        box-shadow: none;
        border: none;
      }
    }

    #main-header {
      background: #fff;
      margin: 0;

      .container {
        max-width: 1024px;

        nav {
          box-shadow: none;
        }
      }
    }

    p:not([class]) { /* C’est pas classe, ma chérie ! */
      margin-bottom: 15px;
      font-size: 18px;
    }

    #main-footer {
      max-width: 1024px;
      margin: 0 auto;
    }
  }

  #roadmap {
    padding: 0;
    background: transparent;

    .container{
      &.ombre { /* = main on other pages */
        margin: 30px auto;
        padding: 0px 70px 30px;
        background-color: #fff;
        -webkit-box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.35);
        box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.35);
        border: solid 1px #d9d9d9;
      }
      max-width: 1024px;
    }

    #story {
      padding: 30px;
    }

    #step {
      h4 {
        color: #413f43;
        margin-bottom: 0;
        margin-top: 1.3rem;
        font-size: 1.8rem;
        line-height: 2.5rem;
      }

      h5 {
        line-height: 2.5rem;
        padding-left: 25px;
      }

      > div {
        .card {
          border: 2px solid transparent;
          margin: 20px auto;
          min-height: 290px;

          ul {
            list-style: none;
            margin-top: 1rem;
            padding-left: 15px;
            margin-left: auto;
            margin-right: auto;
            max-width: 260px;

            li {
              &::before {
                content: "\25B8";
                padding-right: 5px;
              }

              &:nth-of-type(2)::before {
                color: #fff;
              }

              &:nth-of-type(3)::before {
                color: $orange;
              }

              &:nth-of-type(4)::before {
                color: $grey;
              }
            }
          }
        }

        .col-md-6 > img {
          padding: 15px 20px;
        }

        @media (max-width: 768px) {
          .card {
            border: 2px solid transparent;
            margin: 20px auto;
            background: #fff !important;

            /* h5 {
              text-align: left !important;
            } */
          }
        }

        /* Colored cards  */
        &:nth-of-type(1) {
          .card {
            border-image: linear-gradient(to left bottom, #fff 85%, $one) 1;
            background: repeating-linear-gradient(-85deg,
              $one, $one calc(50% - 1px), #fff 50%, #fff 100%);

            @media (max-width: 768px) {
              background: #fff !important;
              border-image: linear-gradient(to top, #fff 85%, $one) 1 !important;
              .row > div:last-child { background: $one; }
            }
          }
        }

        &:nth-of-type(2) {
          .card {
            border-image: linear-gradient(to right bottom, #fff 85%, $two) 1;
            background: repeating-linear-gradient(85deg,
              $two, $two calc(50% - 1px), #fff 50%, #fff 100%);

            @media (max-width: 768px) {
              background: #fff !important;
              border-image: linear-gradient(to top, #fff 85%, $two) 1 !important;
              .row > div:last-child { background: $two; }
            }
          }
        }

        &:nth-of-type(3) {
          .card {
            border-image: linear-gradient(to left bottom, #fff 85%, $three) 1;
            background: repeating-linear-gradient(-85deg,
              $three, $three calc(50% - 1px), #fff 50%, #fff 100%);

            @media (max-width: 768px) {
              background: #fff !important;
              border-image: linear-gradient(to top, #fff 85%, $three) 1 !important;
              .row > div:last-child { background: $three; }
            }
          }
        }

        &:nth-of-type(4) {
          .card {
            border-image: linear-gradient(to right bottom, #fff 85%, $four) 1;
            background: repeating-linear-gradient(85deg,
              $four, $four calc(50% - 1px), #fff 50%, #fff 100%);

            @media (max-width: 768px) {
              background: #fff !important;
              border-image: linear-gradient(to top, #fff 85%, $four) 1 !important;
              .row > div:last-child { background: $four; }
            }
          }
        }
      }
    }

    .quote {
      &, .container {
        background: #fff3;
        color: #454542;
      }

      .container {
        padding: 30px 70px;
        background: #fff3;
      }

      span.text-center {
        display: block;
      }

      blockquote {
        margin: 0;
      }
    }

    .contribute {
      .list-group-item {
        p {
          font-size: 0.9em;
        }
      }
    }

    .faq h2 {
      margin: 40px 0px;
    }

    .thumbnail img {
      margin: 0;
    }

    .btn {
      border-radius: 0;
    }

    .presentation {
      background: url('../../public/img/roadmap/presentation.jpg') top center;
      background-size: cover;
      min-height: 40em;
      display: flex;
      flex-direction: column;
      align-items: center;
      align-content: center;
      justify-content: center;

      .container {
        align-self: center;
        background: transparent;

        .col-md-4 {
          background: rgba($color: #fff, $alpha: 0.8);
          border-radius: 4px;
          padding: 20px 30px;
          @supports ( (-webkit-backdrop-filter: blur(1px)) or (backdrop-filter: blur(1px)) ) {
            backdrop-filter: blur(5px);
          }
          top: -100px;
        }

        h2 {
          padding: 0 5px;
          font-size: 1.7rem;
        }
      }
    }

    #funding2 {
      position: sticky;
      top: 0;
      margin-top: -550px;
      z-index:2;

      .funding, .col-md-4 {
        transition: max-width 0.5s linear, flex 0.5s linear;
      }
    }

    .funding {
      padding: 30px;
      border: 1px solid #d9d9d9;
      border-top: 5px solid #f67e08;
      background: #fff;

      .progress {
        border-radius: 0;
      }

      .progress-bar {
        color: rgba(0, 0, 0, 0.7);
      }

      .progress-meter {
        min-height: 27px;
        margin-bottom: 15px;
        margin-top: -24px;
      }

      .progress-meter > .meter {
        position: relative;
        float: left;
        min-height: 16px;
        border-width: 0px;
        border-style: solid;
        border-color: rgba(221, 221, 221, 0.5);
      }

      .progress-meter > .meter-right {
        float: right;
        border-right-width: 2px;
      }

      .progress-meter > .meter-right:last-child {
        border-left-width: 0px;
      }

      .progress-meter > .meter > .meter-text {
        position: absolute;
        display: inline-block;
        width: 100%;
        font-size: 0.8em;
        color: #aaa;
        text-align: right;
        padding-right: 2px;
        line-height: 16px;
        margin-top:15px;
      }

      .btn {
        border-radius: 1px;
      }

      .btn-lg {
        padding: 1rem;
        margin: 0px;
      }

      .input-group {
        margin-bottom: 20px;

        .input-group-text,
        .form-control {
          border: 2px solid #eee;
          border-radius: 0;
          height: 50px;
        }

        .input-group-text {
          background: #fff;
        }

        .input-group-prepend {
          margin-right: -2px;
        }
      }

      .col-5 {
        margin-bottom: 20px;
      }

      .col-7, // donations
      .col-5, // donators
      p { // intro
        visibility: visible;
        opacity: 1;
        height: auto;
        transition:
          visibility 0s,
          height 0s,
          opacity 0.25s 0.25s linear !important;
      }

      .meter {
        .label, .date, .money {
          visibility: hidden;
          opacity: 0;
          transition:
            visibility 0s,
            opacity 0.25s linear !important;
        }
      }

      .meter:first-of-type .money {
        visibility: visible;
        opacity: 1;
      }
    }

    .quote {
      p {
        padding-top: 10px;
        text-align: left;
      }

      a {
        color: #454542;
      }

      text-align: center;

      button.btn {
        font-size: 1.3em;
        width: 14em;
        word-break: break-word;
        white-space: normal;

        &::first-line {
          font-size: 1.5em;
        }
      }

      blockquote {
        padding: 0px 15px;

        > span {
          border-right: 0.2em solid #333;
          position: relative;
          display: block;
          padding-right: 1em;

          &:before, &:after {
            content: '« ';
            font-family: 'Sanchez';
            color: #333;
          }

          &:after {
            content: ' »';
          }
        }
      }
    }

    .anchor {
      display:block;
      padding-top:80px;
      margin-top:-80px;
    }
  }

  @media (min-width: 576px) {
    #funding2.scrolling {
      .funding {
        border-top: none;
        padding: 15px 20px 30px;
        flex: 0 0 100%;
        max-width: 100%;
        order: 1
      }

      .col-md-4 { // ghost lead
        flex: 0 0 0%;
        max-width: 0%;
      }

      .col-7, // donations
      .col-5, // donators
      p { // intro
        visibility: hidden;
        margin: 0;
        opacity: 0;
        height: 0;
        transition:
          visibility 0s,
          height 0.5s linear,
          opacity 0.25s 0.75s linear !important;
      }

      .col-7, // donations
      .col-5 { // donators
        transition:
          visibility 0s,
          opacity 0s,
          height 0.75s linear !important;
      }

      .col-7 {
        order: 3;
      }

      .col-5 {
        order: 4;
      }

      .col-12 { // progress bar
        flex: 0 0 75%;
        max-width: 75%;
      }

      .meter {
        min-height: 27px !important;
        transition: min-height 1s 1s linear !important;

        .label, .date, .money {
          visibility: visible;
          opacity: 1;
          transition:
            visibility 0s,
            opacity 0.25s 0.75s linear !important;
        }
      }
    }
  }

  @media (min-width: 768px) {
    #roadmap #funding2 {
      margin-bottom: 30px;
    }
  }

  @media (max-width: 1024px) {
    #roadmap {
      .container.ombre {
        box-shadow: none;
        border: none;
        margin: 0 auto;
      }
    }
  }

  @media (max-width: 768px) {
    #roadmap {
      #funding2 {
        margin-top: 0px;
      }

      .presentation {
        min-height: 20em;

        .row {
          visibility: hidden;
        }
      }
    }
  }

  @media (max-width: 576px) {
    #roadmap {
      .container.ombre,
      .quote .container {
        padding-left: 30px;
        padding-right: 30px;
      }

      #funding2 {
        position: relative;
      }
    }
  }
</style>

<script>
  import {
    BButton,
    BCard,
    BCardBody,
    BCardImg,
    BCardText,
    BCol,
    BContainer,
    BProgress,
    BProgressBar,
    BRow
  } from 'bootstrap-vue'

  import ShareAndFollow from '../components/roadmap/ShareAndFollow.vue'
  import MoreInfos from '../components/roadmap/MoreInfos.vue'
  import DonateForm from '../components/roadmap/DonateForm.vue'
  import Team from '../components/roadmap/Team.vue'

  export default {
    components: {
      ShareAndFollow,
      MoreInfos,
      DonateForm,
      Team,
      BContainer,
      BRow,
      BCol,
      BCardImg,
      BCard,
      BCardBody,
      BCardText,
      BButton,
      BProgress,
      BProgressBar
    },
    created () {
      fetch('https://soutenir.framasoft.org/dons/2020-pt3.json')
        .then(response => response.json())
        .then((data) => {
          this.list = data.list
          this.updateData()
        }).catch((err) => console.error('Unable to load donations', err))
      window.addEventListener('scroll', this.stickyProgressBar)
    },
    mounted () {
      document.body.classList.add('in-roadmap')
      this.stickyProgressBar()
    },
    destroyed () {
      document.body.classList.remove('in-roadmap')
      window.removeEventListener('scroll', this.stickyProgressBar)
    },
    data () {
      return {
        start: '2020-05-26',
        end: '2020-11-26',
        days: 0,
        hours: 0,
        progress: {
          total: 0,
          max: 60000
        },
        steps: [ 10000, 20000, 40000, 60000 ],
        activeStep: 0,
        list: [],
        count: 0,
        fundingClass: 'top',
        milestonesContent: {
          one: {
            title: this.$gettext('Step 1'),
            subtitle: this.$gettext('Global search'),
            date: this.$gettext('(June)'),
            width: '16.66',
            ul: [
              this.$gettext('Search through the fediverse'),
              this.$gettext('Ability to create search indexes'),
              this.$gettext('Instance broadcast messages'),
              this.$gettext('WIP to improve UX/UI')
            ]
          },
          two: {
            title: this.$gettext('Step 2'),
            subtitle: this.$gettext('Moderation'),
            date: this.$gettext('(July)'),
            width: '16.66',
            ul: [
              this.$gettext('Feedback on reports'),
              this.$gettext('Making moderation easier'),
              this.$gettext('Sharing blocklists (spam)'),
              this.$gettext('WIP to improve UX/UI')
            ]
          },
          three: {
            title: this.$gettext('Step 3'),
            subtitle: this.$gettext('Plugins & Playlists'),
            date: this.$gettext('(Aug. - Sept.)'),
            width: '33.33',
            ul: [
              this.$gettext('Online playlists sharing'),
              this.$gettext('Plugins (annotations, etc)'),
              this.$gettext('Promoting plugins'),
              this.$gettext('WIP to improve UX/UI')
            ]
          },
          four: {
            title: this.$gettext('Step 4'),
            subtitle: this.$gettext('Live streaming'),
            date: this.$gettext('(Oct. - Nov.)'),
            width: '33.33',
            ul: [
              this.$gettext('Peer-to-peer (30s - 1min lag)'),
              this.$gettext('No chat, no gifs, no ❤️ 👍'),
              this.$gettext('Post-live publishing'),
              this.$gettext('WIP to improve UX/UI')
            ]
          }
        }
      }
    },

    methods: {
      isBefore (date) {
        return new Date(new Date().toDateString()) < new Date(date)
      },

      isAfter (date) {
        return new Date(new Date().toDateString()) > new Date(date)
      },

      formatDate (value) {
        const options = {
          year: 'numeric',
          month: 'short',
          day: 'numeric'
        }

        return new Intl.DateTimeFormat(this.getShortLocale(), options).format(new Date(value))
      },

      updateData () {
        this.days = Math.round(((new Date()).getTime() - (new Date(this.start)).getTime()) / (1000 * 60 * 60 * 24))
        this.hours = Math.round(((new Date(this.end)).getTime() - (new Date()).getTime()) / (1000 * 60 * 60))
        // Fill donators

        this.count = this.list.length

        for (let i = 0; i < this.list.length; i += 1) {
          if (!this.list[i].monthly && new Date(this.start) < new Date(this.list[i].date.replace(/ /g, 'T'))) {
            this.progress.total += this.list[i].don
          }
        }
      },
      stickyProgressBar () {
        this.fundingClass = (document.body.getBoundingClientRect().top >= -300) ? 'top' : 'scrolling'
      }
    }
  }
</script>
