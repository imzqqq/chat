<template>
  <div>
    <div v-if="stats">
      <h1 v-if="beforeDate">Statistics for {{ beforeDate | formatDate }}</h1>

      <div class="dashboard">

        <div class="block" v-for="card in cards" v-bind:key="card.key">

          <div class="block-text">
            <template v-if="currentInfo && currentInfo.key === card.key">
              <span v-if="card.key === 'totalVideoFilesSize'" class="value">{{ currentInfo.value | formatBytes | asDirection }}</span>
              <span v-else class="value">{{ currentInfo.value | formatNumber | asDirection }}</span>
              <span class="label">{{ currentInfo.date | formatDateShort }}</span>
            </template>
            <template v-else>
              <span v-if="card.key === 'totalVideoFilesSize'" class="value">{{ stats[card.key] | formatBytes }}</span>
              <span v-else class="value">{{ stats[card.key] | formatNumber }}</span>
              <span class="label">{{ card.label }}</span>
            </template>
          </div>

          <trend-chart v-if="statsWeek" :datasets="[statsWeek[card.key]]" padding="50 0 0 0"
                       :interactive="true" @mouse-move="onMouseMove"></trend-chart>

        </div>

      </div>
    </div>

    <div v-else class="empty-state">
      <template v-if="beforeDate">
        <p>Sorry, we don't have any data for {{ beforeDate | formatDate }}</p>
      </template>
      <template v-else>
        <div class="spinner-border text-secondary" role="status">
          <span class="sr-only">Loading...</span>
        </div>
      </template>
    </div>

    <instance-stats-history v-if="statsHistory" :history="statsHistory" class="instance-stats-history"></instance-stats-history>
  </div>
</template>

<style lang="scss" scoped>
  h1,
  p {
    color: rgba(0, 0, 0, 0.5);
    font-size: 18px;
    font-weight: 400;
    margin-bottom: 20px;
  }

  .dashboard {
    display: grid;
    grid-gap: 3vw;
    grid-template-columns: repeat( auto-fit, minmax(200px, 1fr) );

    @media screen and (min-width: 600px) {
      grid-template-columns: repeat(3, 1fr);
    }
  }

  .block {
    position: relative;
    display: inline-flex;
    flex-direction: column;
    justify-content: center;
    border-radius: 10px;
    background-color: white;
    color: gray;
    box-shadow: 1px 1px 1em rgba(0, 0, 0, 0.04);
    text-align: center;
    overflow: hidden;

    height: 125px;
    @media screen and (max-width: 600px) {
      height: 100px;
    }

    .block-text {
      position: absolute;
      top: 5px;
      left: 10px;
      text-align: left;

      .label, .value {
        width: min-content;
        white-space: nowrap;
      }
    }

    .label, .value {
      display: block;
    }

    .label {
      text-transform: uppercase;
      font-size: 0.8em;
    }

    .value {
      font-weight: bold;
      font-size: 1.5em;
    }
  }

  .instance-stats-history {
    margin: 3rem 0;
  }

  .empty-state {
    position: relative;
    top: -20px;
    display: flex;
    flex: 1;
    justify-content: center;
    align-items: center;
  }
</style>

<style lang="scss">
  // trend chart colors
  .vtc {
    --fill-color: #f2690d;
    --line-color: #febc6b;

    .stroke {
      stroke: var(--line-color);
    }

    .fill {
      fill: var(--fill-color);
      opacity: .2;
    }

    .active-line {
      stroke: rgba(0, 0, 0, 0.2);
    }

    .point {
      display: none;
      fill: var(--line-color);
      stroke: var(--line-color);

      &.is-active {
        display: block;
      }
    }
  }
</style>

<script lang="ts">
  import { Component, Vue, Watch } from 'vue-property-decorator'

  import { GlobalStats } from '../../../shared/models/global-stats.model'
  import { GlobalStatsHistory } from '../../../shared/models/global-stats-history'
  import { getGlobalStatsHistory } from '../shared/instance-http'

  import * as bytes from 'bytes'
  import dayjs from 'dayjs'
  import TrendChart from 'vue-trend-chart'
  import InstanceStatsHistory from '@/components/InstanceStatsHistory.vue'

  @Component({
    components: {
      InstanceStatsHistory,
      TrendChart
    },
    filters: {
      asDirection: (value: string) => String(value).startsWith('-')
        ? '▼' + value.replace('-', '')
        : '▲' + value,
      formatNumber: (value: string) => value.toLocaleString(),
      formatBytes: (value: string) => bytes.format(parseInt(value, 10)),
      formatDate: (value: string) => dayjs(value).format('dddd, MMMM D YYYY'),
      formatDateShort: (value: string) => dayjs(value).format('MMM D, YYYY')
    }
  })
  export default class InstanceStats extends Vue {
    beforeDate = ''
    stats: GlobalStats = null
    statsHistory: GlobalStatsHistory = null
    statsWeek: {
      [key: string]: {
        data: {
          key: string // needed duplicate for onMouseMove/currentInfo
          date: string
          value: string
        }
      }
    } = null
    currentInfo: {
      key: string
      date: string
      value: string
    } = null
    cards = [
      { key: 'totalInstances', label: 'Instances' },
      { key: 'totalUsers', label: 'Users' },
      { key: 'totalVideoComments', label: 'Comments' },
      { key: 'totalVideos', label: 'Videos' },
      { key: 'totalVideoViews', label: 'Views' },
      { key: 'totalVideoFilesSize', label: 'Of video files' }
    ]

    @Watch('$route') onRouteChanged(to, from) {
      const newBeforeDate = to.query?.beforeDate as string
      if (newBeforeDate !== this.beforeDate) this.getStats(newBeforeDate)
      this.beforeDate = newBeforeDate
    }

    mounted () {
      this.getStats(this.$route?.query?.beforeDate as string)
    }

    onMouseMove (params: any) {
      if (!params) {
        this.currentInfo = null
        return
      }

      this.currentInfo = {
        key: params.data[0]?.key,
        date: params.data[0]?.date,
        value: params.data[0]?.value
      }
    }

    async getStats (beforeDate?: string) {
      this.beforeDate = beforeDate || this.beforeDate
      const statsHistory = await getGlobalStatsHistory({ beforeDate })

      if (statsHistory.data.length > 0) {
        this.stats = statsHistory?.data[0]?.stats
        this.statsHistory = statsHistory
        this.cards.forEach(card => {
          this.statsWeek = Object.assign(this.statsWeek || {}, { [card.key]: this.setStatsForWeek(card.key) })
        })
      } else {
        this.statsHistory = this.stats = this.statsWeek = null
      }
    }

    private setStatsForWeek (key: string) {
      // compute difference with the previous day for today + 7 previous days
      const last9data = this.statsHistory?.data?.slice(0, 10)
      const last8data = []
      for (let i = 0; i < last9data.length - 1; i++) {
        const next = last9data[ (i + 1) % last9data.length ]
        last8data.push({
          date: last9data[i].date,
          value: last9data[i].stats[key] - next.stats[key]
        })
      }

      return {
        smooth: true,
        fill: true,
        showPoints: true,
        data: last8data.reverse().map(e => ({
          key,
          date: e.date,
          value: e.value
        }))
      }
    }
  }
</script>
