<template>
  <div class="row">
    <div class="chart col-md-6" v-for="chart in charts" :key="chart.id">
      <la-cartesian autoresize :bound="[0]" :data="chart.data">
        <la-area color="#febc6b" fill-color="#fde1ce" :width="2" dot curve prop="value1" :label="chart.label1"></la-area>
        <la-area v-if="chart.label2" color="#83d7db" fill-color="#b1eef0" dot curve prop="value2" :label="chart.label2"></la-area>
        <la-area v-if="chart.label3" color="#6bc3e7" dot curve prop="value3" :label="chart.label3"></la-area>

        <la-x-axis v-bind:fontSize="11" prop="date" :interval="interval"></la-x-axis>
        <la-y-axis v-bind:fontSize="11" :format="formatNumber" :interval="interval"></la-y-axis>
        <la-tooltip></la-tooltip>
        <la-legend v-if="chart.label2" selectable ></la-legend>
        <la-legend v-else></la-legend>
      </la-cartesian>
    </div>
  </div>
</template>

<style lang="scss">
  .chart {
    margin-top: 50px;

    svg g line {
      display: none;
    }
  }
</style>

<script lang="ts">
  import { Component, Vue, Prop } from 'vue-property-decorator'

  import { Area, Cartesian, Legend, Tooltip, XAxis, YAxis } from 'laue'
  import { GlobalStats } from '../../../shared/models/global-stats.model'
  import { GlobalStatsHistory } from '../../../shared/models/global-stats-history'

  @Component({
    components: {
      LaCartesian: Cartesian,
      LaArea: Area,
      LaXAxis: XAxis,
      LaYAxis: YAxis,
      LaTooltip: Tooltip,
      LaLegend: Legend
    }
  })
  export default class InstanceStatsHistory extends Vue {
    @Prop(Object) readonly history: GlobalStatsHistory | undefined
    charts: any[] = []
    total!: number

    mounted () {
      const data = this.history.data.reverse()

      this.charts = [
        this.chartBuilder([ 'Instances' ], data, [ 'totalInstances' ]),
        this.chartBuilder([ 'Videos' ], data, [ 'totalVideos' ]),

        this.chartBuilder([ 'Users', 'Active users' ], data, [ 'totalUsers', 'totalMonthlyActiveUsers' ]),
        this.chartBuilder([ 'Comments' ], data, [ 'totalVideoComments' ]),

        this.chartBuilder([ 'Files Size' ], data, [ 'totalVideoFilesSize' ]),
        this.chartBuilder([ 'Views' ], data, [ 'totalVideoViews' ])
      ]

      this.total = data.length
    }

    formatNumber (value: number) {
      const dictionary = [
        { max: 1000, type: '' },
        { max: 1000000, type: 'K' },
        { max: 1000000000, type: 'M' },
        { max: 1000000000000, type: 'G' },
        { max: 1000000000000000, type: 'T' },
      ]

      const format = dictionary.find(d => value < d.max) || dictionary[dictionary.length - 1]
      const calc = Math.floor(value / (format.max / 1000))

      return `${calc}${format.type}`
    }

    interval (i: number) {
      const points = Math.ceil(this.total/4)

      return i === 0 || i === this.total - 1 || i % points === 0
    }

    private chartBuilder (labels: string[], data: { date: string, stats: GlobalStats }[], keys: (keyof GlobalStats)[]) {
      const result: any = {}
      let i = 1

      for (const label of labels) {
        Object.assign(result, {
          ['label' + i]: label,
        })

        i++
      }

      return Object.assign(result, {
        data: data.map(d => {
          const base = {
            date: new Date(d.date).toLocaleDateString()
          }

          let i = 1

          for (const key of keys) {
            Object.assign(base, {
              ['value' + i]: d.stats[key]
            })

            i++
          }

          return base
        })
      })
    }
  }
</script>
