<template>
  <div>

    <div class="top-text" v-html="topTextHtml"></div>

    <div class="row">
      <div class="col-12 mb-2">
        <input v-model="searchTerm" type="text" class="form-control form-control-sm" placeholder="Search among instances">
      </div>

      <!-- TODO: implement selects
      <div class="col-12 col-sm-3 mb-2">
        <select class="custom-select custom-select-sm">
          <option>Category</option>
        </select>
      </div>
      <div class="col-12 col-sm-2 mb-2">
        <select class="custom-select custom-select-sm">
          <option>Language</option>
        </select>
      </div>
      -->
    </div>

    <vue-good-table
      mode="remote"
      :columns="columns"
      :rows="rows"
      :lineNumbers="false"
      :totalRows="totalRecords"

      :pagination-options="paginationOptions"
      :search-options="{
        enabled: true,
        externalQuery: searchTerm
      }"

      @on-search="onSearch"
      @on-page-change="onPageChange"
      @on-sort-change="onSortChange"
      @on-per-page-change="onPerPageChange"

      styleClass="table table-stripped"
    >
      <div slot="emptystate">
        <span v-if="totalRecords === 0">No results.</span>
        <span v-else>Loading...</span>
      </div>

      <template slot="table-row" slot-scope="props">
        <span v-if="props.column.field === 'name'" :title="getInstanceNameTitle(props.row)">{{ props.row.name }}</span>

        <a v-else-if="props.column.field === 'host'" :href="getUrl(props.row.host)" target="_blank">{{ props.row.host }}</a>

        <div v-else-if="props.column.field === 'version'"  class="version">
          <code :title="props.row.version">{{ props.row.version }}</code>
        </div>

        <div class="text-center" v-else-if="props.column.field === 'signupAllowed'">
          <span class="check-mark" v-if="props.row.signupAllowed">&#x2714;</span>
          <span v-else>&#x274C;</span>
        </div>

        <div class="text-center" v-else-if="props.column.field === 'liveEnabled'">
          <span class="check-mark" v-if="props.row.liveEnabled">&#x2714;</span>
          <span v-else>&#x274C;</span>
        </div>

        <div class="text-center country-flag" v-else-if="props.column.field === 'country'" :title="props.row.country">
          <country-flag v-if="props.row.country" :country="props.row.country"/>
        </div>

        <div
          v-else-if="props.column.field === 'health'" class="icon-cell"
          :title="'In recent days, the instance was available ' + props.row.health + '% of the time.'"
        >
          <font-awesome-icon class="health-icon"
                             :icon="getIcon(props.row.health)" :style="{ color: getIconColor(props.row.health) }"
          ></font-awesome-icon>
        </div>

        <span v-else>
          {{ props.formattedRow[props.column.field] }}
        </span>
      </template>
    </vue-good-table>
  </div>
</template>

<style lang="scss">
  th {
    cursor: pointer;
  }

  td, th {
    font-size: 0.8em;
    vertical-align: middle !important;
  }

  .emptystate { text-align: center; }

  .text-end { text-align: end; }
  .text-center { text-align: center; }

  .name, .host {
    max-width: 170px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .name, .host, .version {
    padding-right: 5px !important;
  }

  .version {
    max-width: 80px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .icon-cell {
    padding: 0 !important;
    text-align: center !important;
    vertical-align: middle !important;

    .health-icon {
      width: 20px !important;
      height: 20px !important;
    }
  }

  .country-flag {
    max-height: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .top-text {
    font-size: 14px;
    margin-bottom: 20px;
  }

  a,
  a:hover {
    color: #e85d00;
  }

  table th,
  .vgt-wrap__footer {
    border: none !important;
    background: none !important;
  }
</style>

<script lang="ts">
  import { Vue, Component } from 'vue-property-decorator'

  import { Instance } from '../../../shared/models/instance.model'
  import { getConfig } from '../shared/config'
  import { listInstances } from '../shared/instance-http'
  import faSmile from '@fortawesome/fontawesome-free-solid/faSmile'
  import faMeh from '@fortawesome/fontawesome-free-solid/faMeh'
  import faFrown from '@fortawesome/fontawesome-free-solid/faFrown'
  import debounce from 'lodash/debounce'

  @Component
  export default class InstanceList extends Vue {
    columns = [
      {
        label: 'Name',
        field: 'name',
        sortable: true,
        tdClass: 'name'
      },
      {
        label: 'URL',
        field: 'host',
        sortable: true,
        tdClass: 'host'
      },
      {
        label: 'Version',
        field: 'version',
        sortable: true,
        tdClass: 'version'
      },
      {
        label: 'Following',
        field: 'totalInstanceFollowing',
        sortable: true,
        type: 'number'
      },
      {
        label: 'Followers',
        field: 'totalInstanceFollowers',
        sortable: true,
        type: 'number'
      },
      {
        label: 'Local videos',
        field: 'totalLocalVideos',
        sortable: true,
        type: 'number'
      },
      {
        label: 'Total videos',
        field: 'totalVideos',
        sortable: true,
        type: 'number'
      },
      {
        label: 'Users',
        field: 'totalUsers',
        sortable: true,
        type: 'number'
      },
      {
        label: 'Signup',
        field: 'signupAllowed',
        type: 'boolean',
        sortable: true
      },
      {
        label: 'Live',
        field: 'liveEnabled',
        type: 'boolean',
        sortable: true
      },
      {
        label: 'Location',
        field: 'country',
        type: 'string',
        sortable: true
      },
      {
        label: 'Health',
        field: 'health',
        sortable: true,
        type: 'number'
      }
    ]

    rows: Instance[] = []
    totalRecords: number | null = null

    perPage = 10

    searchTerm = ''
    category: string

    paginationOptions = {
      enabled: true,
      perPage: this.perPage
    }

    onSearch = debounce(this.onSearchImpl.bind(this), 500)

    topTextHtml = ''

    private search = ''
    private sort = '-createdAt'
    private currentPage = 1

    async mounted () {
      this.setTableFromQueryParams()

      this.loadData()

      const config = await getConfig()
      this.topTextHtml = config.instanceClientWarning
    }

    getUrl (host: string) {
      return 'https://' + host
    }

    getIcon (health: number) {
      if (health > 90) return faSmile
      if (health > 50) return faMeh
      return faFrown
    }

    getIconColor (health: number) {
      if (health > 98) return '#08cd36'
      if (health > 90) return '#81c307'
      if (health > 70) return '#cfb11b'
      if (health > 50) return '#e67a3c'
      if (health > 20) return '#e7563c'

      return '#f44141'
    }

    getInstanceNameTitle (instance: Instance) {
      return instance.name + '\nAdded on ' + new Date(instance.createdAt).toLocaleString()
    }

    onPageChange (params: { currentPage: number }) {
      this.currentPage = params.currentPage
      this.setQueryParam('page', this.currentPage)
      this.loadData()
    }

    onPerPageChange (params: { currentPerPage: number }) {
      this.perPage = params.currentPerPage
      this.currentPage = 1
      this.setQueryParam('page', this.currentPage)
      this.loadData()
    }

    onSortChange (params: any) {
      const newSort = params[0]

      this.sort = newSort.type === 'asc' ? '' : '-'
      this.sort += newSort.field

      this.setQueryParam('sort', this.sort)

      this.loadData()
    }

    private setQueryParam (name: string, value: string | number) {
      let query = { ...this.$route.query }

      if (value) query = Object.assign(query, { [name]: value })
      else delete query[name]

      this.$router.push({
        path: this.$route.path,
        query
      })
    }

    private setTableFromQueryParams () {
      for (var key in this.$route.query){
        if (key === 'sort') {
          this.sort = this.$route.query[key] as string
        } else if (key === 'page') {
          this.currentPage = +this.$route.query[key]
        } else if (key === 'search') {
          this.search = this.searchTerm = this.$route.query[key] as string
        }
      }
    }

    private onSearchImpl (params: { searchTerm: string }) {
      this.search = params.searchTerm
      this.setQueryParam('search', this.search)
      this.loadData()
    }

    private async loadData () {
      const response = await listInstances({
        page: this.currentPage,
        perPage: this.perPage,
        sort: this.sort,
        search: this.search
      })

      this.rows = response.data
      this.totalRecords = response.total
    }
  }
</script>
