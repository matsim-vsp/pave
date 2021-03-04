<template lang="pug">
.sankey-flipper(v-if="activePlot")
  .diagram
    .pie(v-show="activePlot.piechart" id="pie-chart")

    sankey-diagram(v-if="selectedPlotNumber > -1 && !activePlot.piechart"
      :yamlConfig="activePlot.config"
      :fileApi="myState.svnRoot"
      :subfolder="`${myState.selectedRun}`"
      :thumbnail="true"
      :flipperID="'sankey-flipper'"
    )

  h4 {{ `${this.selectedPlotNumber + 1}. ${activePlot.title}` }}

  .buttons-prev-next
    button.button.is-outlined(@click="switchPlot(-1)")
      span.icon.is-small: i.fas.fa-arrow-left
      span Prev
    button.button.is-outlined(@click="switchPlot(1)")
      span Next
      span.icon.is-small: i.fas.fa-arrow-right

</template>

<script lang="ts">
'use strict'

import { Vue, Component, Prop, Watch } from 'vue-property-decorator'
import vegaEmbed from 'vega-embed'

import globalStore from '@/store'
import { SVNProject } from '@/Globals'
import HTTPFileSystem from '@/util/HTTPFileSystem'
import SankeyDiagram from '@/plugins/sankey/SankeyDiagram.vue'

interface SankeyYaml {
  csv: string
  title?: string
  description?: string
}

interface VizEntry {
  component: string
  config: string
  title: string
}

// JavaScript is a cruel joke.
// https://stackoverflow.com/questions/4467539/javascript-modulo-gives-a-negative-result-for-negative-numbers
// @ts-ignore
Number.prototype.mod = function(n) {
  // @ts-ignore
  return ((this % n) + n) % n
}

const embedOptions = {
  actions: false,
  hover: true,
  padding: { top: 0, left: 5, right: 5, bottom: 0 },
}

@Component({ components: { SankeyDiagram } })
class MyComponent extends Vue {
  @Prop({ required: true })
  private modeSharePie!: any

  @Prop({ required: true })
  private myState!: {
    svnProject: SVNProject
    svnRoot: HTTPFileSystem
    vizes: VizEntry[]
    selectedRun: string
  }

  private allSankeys: any[] = []
  private selectedPlotNumber = -1

  private get activePlot() {
    return this.allSankeys[this.selectedPlotNumber]
  }

  private switchPlot(next: number) {
    // @ts-ignore
    this.selectedPlotNumber = (this.selectedPlotNumber + next).mod(this.allSankeys.length)
  }

  public mounted() {
    const vizes: any[] = []

    if (this.modeSharePie.description) {
      vizes.push({ piechart: true, title: this.modeSharePie.description })
      vegaEmbed(`#pie-chart`, this.modeSharePie, embedOptions)
    }

    if (this.myState.vizes) {
      const sankeys = this.myState.vizes.filter(viz => viz.component === 'sankey-diagram')
      vizes.push(...sankeys)
    }
    this.allSankeys = vizes
    this.selectedPlotNumber = 0
  }
}

export default MyComponent
</script>

<style scoped lang="scss">
@import '@/styles.scss';
.sankey-flipper {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.diagram {
  padding: 0rem 0rem;
  background-color: white;
  text-align: center;
  margin: auto 0;
}

h4 {
  text-align: center;
  font-size: 0.9rem;
  color: #444;
}

.buttons-prev-next {
  display: flex;
  flex-direction: row;
  margin: 1rem auto 0 auto;
}

.button {
  margin-right: 0.25rem;
}

@media only screen and (max-width: 40em) {
}
</style>
