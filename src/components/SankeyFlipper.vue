<template lang="pug">
.sankey-flipper(v-if="activePlot")
  .diagram
    h4 {{ `${this.selectedPlotNumber + 1}. ${activePlot.title}` }}

    sankey-diagram(v-if="selectedPlotNumber > -1"
      :yamlConfig="activePlot.config"
      :fileApi="myState.svnRoot"
      :subfolder="`${myState.selectedRun}`"
      :thumbnail="true"
      :style="{'pointer-events': 'none'}"
      :flipperID="'sankey-flipper'"
    )

  .butdtons
    button.button.is-link(@click="switchPlot(1)")
      //- span Next
      span.icon.is-small: i.fas.fa-arrow-right

    button.button.is-link(@click="switchPlot(-1)")
      span.icon.is-small: i.fas.fa-arrow-left
      //- span Prev



</template>

<script lang="ts">
'use strict'

import { Vue, Component, Prop, Watch } from 'vue-property-decorator'

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

@Component({ components: { SankeyDiagram } })
class MyComponent extends Vue {
  @Prop({ required: true })
  private myState!: {
    svnProject: SVNProject
    svnRoot: HTTPFileSystem
    vizes: VizEntry[]
    selectedRun: string
  }
  private globalState = globalStore.state

  private vizDetails: SankeyYaml = { csv: '', title: '', description: '' }

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
    console.log('FLIPPER ----------')
    if (this.myState.vizes)
      this.allSankeys = this.myState.vizes.filter(viz => viz.component === 'sankey-diagram')
    this.selectedPlotNumber = 0
  }
}

export default MyComponent
</script>

<style scoped lang="scss">
@import '@/styles.scss';
.sankey-flipper {
  display: flex;
  flex-direction: row;
}

.diagram {
  display: inline-block;
  width: 50%;
  height: 25rem;
  padding: 1rem 0rem;
  background-color: white;
  text-align: center;
}

.butdtons {
  margin-left: 0.5rem;
  display: flex;
  flex-direction: column;
}

.button {
  margin-bottom: 0.25rem;
}

@media only screen and (max-width: 40em) {
}
</style>
