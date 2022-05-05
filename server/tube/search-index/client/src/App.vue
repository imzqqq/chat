<template>
  <div>
    <router-view id="main-container" class="container"/>

    <my-footer></my-footer>
  </div>
</template>

<script lang="ts">
  import { defineComponent } from 'vue'

  import Footer from './components/Footer.vue'

  export default defineComponent({
    components: {
      'my-footer': Footer
    },

    data () {
      return {
        title: process.env.VUE_APP_TITLE
      }
    }
  })
</script>

<style lang="scss">

</style>
