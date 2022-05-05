<template>
  <div id="app">
    <header-component></header-component>

    <router-view></router-view>

    <footer-component></footer-component>
  </div>
</template>

<script>
  import HeaderComponent from './components/Header.vue'
  import FooterComponent from './components/Footer.vue'

  export default {
    name: 'app',
    components: {
      HeaderComponent,
      FooterComponent
    },
    metaInfo: function () {
      return {
        title: 'JoinTube',
        titleTemplate: '%s | JoinTube',
        meta: [
          {
            vmid: 'description',
            name: 'description',
            content: this.$gettext('A free software to take back control of your videos! With more than 100 000 hosted videos, viewed more than 6 millions times and 20 000 users, Tube is the decentralized free software alternative to videos platforms developed by Framasoft')
          }
        ]
      }
    },

    created () {
      document.documentElement.lang = this.$language.current.replace('_', '-')
    }
  }
</script>
