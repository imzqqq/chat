<template>
  <div id="i18n">
    <b-dropdown right>
      <template slot="button-content">
        <div :title="getButtonTitle()">
          <img :src="buildImgUrl('language.png')" alt=""/>

          <div class="text">
            <translate>Languages</translate>
            <span class="caret"></span>
          </div>
        </div>
      </template>

      <b-dropdown-item v-for="(lang, locale) in $language.available" :key="locale" :href="buildLocaleLink(locale)">
        {{ lang }}
      </b-dropdown-item>

      <b-dropdown-divider></b-dropdown-divider>

      <b-dropdown-item class="translate" target="_blank" rel="noopener noreferrer" href="https://weblate.framasoft.org/projects/joinpeertube/main/">
        <icon-add></icon-add>
        <span v-translate>Translate</span>
      </b-dropdown-item>
    </b-dropdown>
  </div>
</template>

<style lang="scss">
  #i18n {
    img {
      max-height: 25px;
    }

    .text {
      font-size: 11px;
    }

    .dropdown {
      button {
        height: 60px;
        color: #000;
        background-color: #fff;
        border: none;
      }

      .dropdown-toggle::after {
        display: none;
      }

      .translate svg {
        width: 20px;
        margin-right: 5px;
        vertical-align: top;
      }
    }
  }
</style>

<script>
  import { BDropdown, BDropdownDivider, BDropdownItem } from 'bootstrap-vue'
  import IconAdd from './icons/IconAdd'

  export default {
    components: {
      BDropdown,
      BDropdownDivider,
      BDropdownItem,

      IconAdd
    },

    methods: {
      buildLocaleLink (locale) {
        return process.env.BASE_URL + `${locale}/`
      },

      getButtonTitle () {
        return this.$gettext('Change interface language')
      }
    }
  }
</script>
