<template>
  <div class="interface-language-dropdown">
    <img :title="imgTitle" v-on:click="toggleShow()" class="interface-language" src="/img/interface-languages.svg" alt="Change interface language">

    <div v-if="showMenu" class="menu">
      <a v-for="(lang, locale) in availableLanguages" :key="locale" :href="buildLanguageRoute(locale)" class="menu-item">
        {{lang}}
      </a>

      <hr />

      <a class="menu-item add-your-language" target="_blank" href="https://weblate.framasoft.org/projects/tube-search-index/client/">
        Translate
      </a>
    </div>
  </div>
</template>

<style lang="scss">
  @import '../scss/_variables';

  .interface-language-dropdown {
    img {
      cursor: pointer;
      opacity: .7;
    }

    .menu {
      background-color: #fff;
      background-clip: padding-box;
      border: 1px solid rgba(0,0,0,.15);
      border-radius: .25rem;
      color: #212529;
      cursor: pointer;
      display: flex;
      flex-direction: column;
      padding: .5rem 0;
      position: absolute;
      text-align: left;
      font-size: 14px;

      a {
        text-decoration: none;
      }

      hr {
        border: 0;
        border-top: 1px solid $grey;
        width: calc(100% - 20px);
        margin: 10px;
      }

      .add-your-language {
        font-weight: bold;
      }
    }

    .menu-item {
      color: #212529;
      padding: .25rem 1.5rem;
      transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;
    }

    .menu-item:hover {
      background-color: #F4F6F6;
      cursor: pointer;
    }
  }
</style>

<script lang="ts">
  import { useGettext } from '@jshmrtn/vue3-gettext'
  import { defineComponent } from 'vue'

  export default defineComponent({
    data () {
      return {
        showMenu: false
      }
    },

    computed: {
      imgTitle () {
        return this.$gettext('Change interface language')
      },

      availableLanguages (): { [id: string]: string } {
        const { available } = useGettext()

        return available
      }
    },

    methods: {
      toggleShow () {
        this.showMenu = !this.showMenu;
      },

      buildLanguageRoute(locale: string) {
        const paths = this.$route.fullPath.split('/')

        if (paths.length > 0 && this.availableLanguages.hasOwnProperty(paths[0])) {
          return "/" + locale + "/" + paths.slice(1).join("/")
        } else {
          return "/" + locale + this.$route.fullPath
        }
      }
    }
  })
</script>
