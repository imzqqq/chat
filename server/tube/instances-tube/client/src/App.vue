<template>
  <div>
    <header>
      <b-navbar toggleable="lg" variant="light">
        <div class="container">
        <b-navbar-brand to="/">
          <span v-html="title" v-once></span>
        </b-navbar-brand>

        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

        <b-collapse id="nav-collapse" is-nav>
          <b-navbar-nav class="ml-auto">

            <b-nav-item to="/instances" exact>
              Instances
            </b-nav-item>

            <b-nav-item to="/instances/stats">
              Statistics
            </b-nav-item>

            <b-nav-item to="/instances/add" exact>
              Add
            </b-nav-item>

            <b-nav-item href="mailto:rt+abuse-tube@framasoft.org" target="_blank">
              Report an instance
            </b-nav-item>
          </b-navbar-nav>
        </b-collapse>
        </div>
      </b-navbar>
    </header>

    <router-view id="main-container" class="container"/>
  </div>
</template>

<script>
import { BCollapse, BNavbar, BNavbarNav, BNavbarBrand, BNavItem, BNavbarToggle } from 'bootstrap-vue'

export default {
  components: {
    BCollapse,
    BNavbar,
    BNavbarBrand,
    BNavItem,
    BNavbarNav,
    BNavbarToggle
  },

  data () {
    return {
      title: process.env.VUE_APP_TITLE
    }
  }
}
</script>

<style lang="scss">
  @import '../node_modules/bootstrap/scss/bootstrap';

  body {
    background-color: #f8f9fa;
  }

  #main-container {
    margin-top: 20px;
    flex: 1;
    display: flex;
    flex-direction: column;
    padding-bottom: 20px;
  }
</style>

<style lang="scss" scoped>
  a {
    outline: 0;
    font-weight: 600;
    opacity: 0.7;

    &.router-link-active {
      opacity: 1;
    }
  }
</style>
