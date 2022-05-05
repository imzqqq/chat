<template>
  <header id="main-header">
    <div>

      <b-navbar toggleable="lg">
        <b-navbar-brand to="/">
          <img alt="Tube" :src="buildImgUrl('brand.png')">

          <div class="support">
            <translate>developed by</translate>

            <img alt="Framasoft" :src="buildImgUrl('framasoft-logo-text-small.png')">
          </div>
        </b-navbar-brand>

        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

        <b-collapse id="nav-collapse" is-nav>
          <b-navbar-nav class="ml-auto">

            <b-nav-item :to="buildRoute('/')" exact>
              <translate>Home</translate>
            </b-nav-item>

            <!-- <b-nav-item class="bold" :to="buildRoute('/roadmap')" exact>
              <translate>Roadmap</translate>
            </b-nav-item> -->

            <b-nav-item class="bold" :to="buildRoute('/instances')">
              <translate>Create an account</translate>
            </b-nav-item>

            <b-nav-item :to="buildRoute('/news')">
              <translate>News</translate>
            </b-nav-item>

            <b-nav-item :to="buildRoute('/help')">
              <translate>Help</translate>
            </b-nav-item>

            <b-nav-item href="https://tube.docs.imzqqq.top/contribute-getting-started" target="_blank" rel="noopener noreferrer">
              <translate>Contribute</translate>
            </b-nav-item>

            <b-nav-item href="https://github.com/Chocobozzz/Tube" target="_blank" rel="noopener noreferrer">
              <translate>Git</translate>
            </b-nav-item>

            <I18n/>
          </b-navbar-nav>
        </b-collapse>
      </b-navbar>
    </div>
  </header>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';
  @import '../scss/_mixins.scss';

  #main-header {

    header {
      margin-bottom: 30px;
    }

    .navbar-collapse {
      &.collapsing,
      &.show {
        padding-top: 20px;

        .nav-item {
          margin-right: 0;
          margin-top: 5px;
        }
      }
    }

    .navbar-brand {
      @include disable-default-a-behaviour;

      color: #000;
      font-family: 'Proza Libre', sans-serif;
      font-size: 10px;

      img {
        max-height: 35px;
      }

      .support img {
        vertical-align: initial;
        margin-left: 3px;
        height: 7px;
      }
    }

    nav {
      min-height: 60px;
      background-color: #fff;
      box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.2);
    }

    .navbar-nav {
      align-items: center;
    }

    .nav-item {
      margin-right: 25px;
    }

    .nav-link {
      color: #000 !important;
      padding: 0 !important;
      font-family: 'Proza Libre', sans-serif;

      &:hover:not(.router-link-active) {
        opacity: 0.8;
      }
    }

    .bold {
      font-weight: $font-semibold;
    }

    .router-link-active:not(.navbar-brand) {
      border-bottom: 3px solid $orange;
      margin-bottom: -3px;
    }
  }
</style>

<script>
  import I18n from './I18n.vue'
  import { BCollapse, BNavbar, BNavbarNav, BNavbarBrand, BNavItem, BNavbarToggle } from 'bootstrap-vue'

  export default {
    components: {
      I18n,
      BCollapse,
      BNavbar,
      BNavbarBrand,
      BNavItem,
      BNavbarNav,
      BNavbarToggle
    }
  }
</script>
