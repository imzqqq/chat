<template>

  <div class="pagination" v-if="searchDone">
    <router-link class="previous" v-bind:class="{ 'none-opacity': modelValue === 1 }" :to="{ query: buildPageUrlQuery(modelValue - 1) }">
      <translate>Previous page</translate>
    </router-link>

    <div class="pages">
      <template v-for="page in pages">

        <router-link v-if="page !== modelValue" class="go-to-page" :to="{ query: buildPageUrlQuery(page) }" :key="page">
          {{ page }}
        </router-link>

        <div v-else class="current">{{ page }}</div>

      </template>
    </div>

    <router-link class="next" v-bind:class="{ 'none-opacity': modelValue >= maxPage }" :to="{ query: buildPageUrlQuery(+modelValue + 1) }">
      <translate>Next page</translate>
    </router-link>
  </div>

</template>

<style lang="scss">
  @import '../scss/_variables';

  .pagination {
    margin-top: 50px;
    display: flex;
    justify-content: center;

    .pages {
      display: flex;

      * {
        margin: 0 5px;
      }
    }

    a {
      color: $orange;
      text-decoration: none;

      &:hover {
        text-decoration: underline;
      }
    }

    .previous {
      margin-right: 10px;
    }

    .next {
      margin-left: 10px;
    }

    @media screen and (max-width: $small-view) {
      font-size: 14px;
    }
  }

</style>

<script lang="ts">
  import { defineComponent } from 'vue'

  export default defineComponent({
    props: {
      maxPage: Number,
      searchDone: Boolean,
      modelValue: Number,
      pages: Array as () => number[]
    },

    methods: {
      buildPageUrlQuery (page: number) {
        const query = this.$route.query

        return Object.assign({}, query, { page })
      },
    }
  })
</script>
