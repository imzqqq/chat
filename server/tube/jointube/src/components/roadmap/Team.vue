<template>
  <section>
    <b-container class="ombre">
      <b-row id="team">
        <b-col cols="12">
          <h3 class="section-title mt-5">
            <div v-translate>
              Who are we?
            </div>
            <div class="border-title"></div>
          </h3>
        </b-col>
        <b-col lg="8">
          <p class="text-center">
            <img
              :src="buildImgUrl('roadmap/biglogo-notxt.png')"
              class="rounded-circle border w-50"
            />
          </p>

          <p v-translate>
            <strong>Framasoft</strong> (that's us!) is a French non-for-profit organization created in 2004,
            dedicated <strong>to popular education</strong> on digital matters.
            Our small association (under 40 members, under 10 employees) is known to have made the
            <a href="https://degooglisons-internet.org">"De-google-ify the Internet" project</a>, offering 34 ethical and alternative online tools.
          </p>

          <p v-translate>
            Our association creates digital tools (services, courses and resources, software) to equip the people who participate in a
            "contribution society". Amongst these, we lead the development of federated software Tube and Mobilizon
            (an upcoming alternative to Facebook events, pages and groups).
          </p>

          <p v-translate>
            Recognised as being of general interest, over 95% of our association's
            budget comes from donations made by people who support and trust us.
          </p>

          <p>
            <a href="https://framasoft.org" v-translate>
              Learn more about Framasoft
            </a>
          </p>
        </b-col>
        <b-col lg="4">
          <h4 v-translate>Guest designer</h4>
          <p v-translate>
            Throughout this development period we will be improving the user experience and interface
            of Tube to provide more clarity and power at every level of use.
          </p>

          <b-card>
            <b-card-body>
              <h4>Marie-Cécile Godwin Paccard</h4>

              <img
                :src="buildImgUrl('roadmap/mcgp.jpg')"
                class="rounded-circle d-block mx-auto"
                alt=""
              />
              <p v-translate>
                « Independent designer, UX researcher who seeks to acquire a thorough and deep understanding
                of uses and design user-friendly, ethical and inclusive tools. »
              </p>
              <p>
                <a href="https://mcgodwin.com">mcgodwin.com</a>
              </p>
            </b-card-body>
          </b-card>
        </b-col>
      </b-row>
    </b-container>
  </section>
</template>

<style lang="scss">
  #team {
    .card {
      margin-bottom: 30px;
      border-radius: 0;

      img {
        max-width: 128px;
        margin-right: 20px;
      }
    }

    .card-body > .card-body {
      padding: 10px;
    }
  }
</style>

<script>
  import {
    BCard,
    BCardBody,
    BContainer,
    BCol,
    BRow
  } from 'bootstrap-vue'

  export default {
    components: {
      BCard,
      BCardBody,
      BContainer,
      BCol,
      BRow
    }
  }
</script>
