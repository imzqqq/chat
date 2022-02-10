<template>
  <b-card no-body :id="accordionId">

    <b-card-header role="tab">
      <a :href="href" v-b-toggle="accordionBodyId" v-on:click="onLinkClick($event, href)">
        <slot name="title"></slot>
      </a>
    </b-card-header>

    <b-collapse :visible="visible" :accordion="accordionBodyId" :id="accordionBodyId" role="tabpanel">
      <b-card-body>

        <b-card-text>
          <slot></slot>
        </b-card-text>

      </b-card-body>
    </b-collapse>
  </b-card>
</template>

<style lang="scss">
  @import '../scss/_variables';

  .card-header a {
    color: #000;
  }

</style>

<script>
  import { BCollapse, BCard, BCardHeader, BCardBody, BCardText, VBToggle } from 'bootstrap-vue'

  export default {
    data () {
      return {
        visible: false,
        updateVisibilityListener: undefined
      }
    },

    components: {
      BCollapse,
      BCard,
      BCardHeader,
      BCardBody,
      BCardText
    },

    directives: {
      'b-toggle': VBToggle
    },

    props: {
      title: String,
      id: String
    },

    mounted () {
      this.updateVisibility()

      this.updateVisibilityListener = () => this.updateVisibility()

      window.addEventListener('hashchange', this.updateVisibilityListener)
    },

    beforeDestroy: function () {
      window.removeEventListener('hashchange', this.updateVisibilityListener)
    },

    computed: {
      accordionId: function () {
        return this.id.toLowerCase()
          .replace(/[ /'’]/g, '-')
          .replace(/[?!.,]/g, '')
      },

      href: function () {
        return '#' + this.accordionId
      },

      accordionBodyId: function () {
        return this.accordionId + '-body'
      }
    },

    methods: {
      onLinkClick: function (event, href) {
        event.preventDefault()
        history.replaceState({}, '', href)
      },

      updateVisibility: function () {
        this.visible = window.location.hash === this.href
      }
    }
  }
</script>
