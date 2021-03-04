<template lang="pug">
.folder-browser
  .left-strip
    .dimensions(v-if="myState.runFinder.dimensions.length")
      h3 Select Run:
      .dimension(v-for="d in myState.runFinder.dimensions" :key="d.heading")
        h4 {{ d.heading }}
        p {{ d.subheading }}

        button.button(
          v-for="option in d.options"
          :key="`${option.title}/${option.value}`"
          :class="{'is-link': myState.activeButtons[d.heading] === option.value }"
          @click="clickedOptionButton(d.heading, option.value)"
        ) {{ option.title }}

  .right-strip.cream
    .stripe(v-if="myState.svnProject")
      .vessel
        .project-bar
          .details
            h2 {{ myState.svnProject.name }}
            p {{ myState.svnProject.description }}

    //- show network errors
    .stripe(v-if="myState.errorStatus")
      .vessel
        .badnews(v-html="myState.errorStatus")

    //- this is the content of readme.md, if it exists
    .stripe
      .vessel
        .readme-header
          .curate-content.markdown(
            v-if="myState.readme"
            v-html="myState.readme"
          )

    //- selected run header
    .stripe
      .vessel(:style="{borderRadius: '10px', marginBottom: '2rem'}")
        .white.call-out-box
          h3.curate-heading At-a-Glance
          p(v-if="!myState.isLoading && !myState.vizes.length") Nothing to show. Select a different service combination.

          .summary-table(v-if="myState.selectedRun && myState.vizes.length")
            .col1
              .tlabel Demand
              .tlabel Fleet size
              .tlabel Mileage
              .tlabel.vspace Revenue distance
              .tlabel.blue Income/day
              .tlabel.blue Expenses/day
              .tlabel.vspace(:class="{'blue': annualIncome() > 0, 'red': annualIncome() < 0}") {{ annualIncome() < 0 ? 'Annual subsidy' : 'Annual revenue' }}
              .tlabel.vspace 95% waiting times &lt;
              b.tlabel Vehicle costs
              .tlabel: b Per day
              input.input(v-model="runCosts.fixedCosts")

            .col2
              .tlabel {{ runHeader.demand.toLocaleString() }} rides
              .tlabel {{ runHeader.fleetSize.toLocaleString() }} vehicles
              .tlabel {{ runHeader.mileage.toLocaleString() }} km
              .tlabel.vspace {{ runHeader.revenueDistance.toLocaleString() }} km
              .tlabel.blue {{ runHeader.incomePerDay.toLocaleString() }} €
              .tlabel.blue {{ expensesPerDay().toLocaleString() }} €
              .tlabel.vspace(:class="{'blue': annualIncome() > 0, 'red': annualIncome() < 0}") {{ annualIncome().toLocaleString() }} €
              .tlabel.vspace {{ (runHeader.serviceQuality / 60.0).toFixed(1) }} min
              .tlabel :
              .tlabel Per km
              input.input(v-model="runCosts.variableCosts")

            .col3(v-if="modeSharePie.data")
              sankey-flipper(:myState="myState")
              //- b Mode Shares
              //- #pie-chart


    //- thumbnails of each viz and image in this folder
    .stripe(v-if="myState.vizes.length")
      .vessel
        h3.curate-heading {{ $t('Analysis')}}

        .curate-content
          .viz-table
            .viz-grid-item(v-for="viz,index in myState.vizes"
                      :key="viz.config"
                      @click="clickedVisualization(index)")

              .viz-frame
                component.viz-frame-component(
                      :is="viz.component"
                      :yamlConfig="viz.config"
                      :fileApi="myState.svnRoot"
                      :subfolder="`${myState.selectedRun}`"
                      :thumbnail="true"
                      :style="{'pointer-events': viz.component==='image-view' ? 'auto' : 'none'}"
                      @title="updateTitle(index, $event)")
                p {{ viz.title }}


</template>

<script lang="ts">
const i18n = {
  messages: {
    en: {
      Analysis: 'Analysis',
      Files: 'Files',
      Folders: 'Folders',
    },
    de: {
      Analysis: 'Ergebnisse',
      Files: 'Dateien',
      Folders: 'Ordner',
    },
  },
}

import { Vue, Component, Watch, Prop } from 'vue-property-decorator'
import markdown from 'markdown-it'
import mediumZoom from 'medium-zoom'
import micromatch from 'micromatch'
import Papaparse from 'papaparse'
import vegaEmbed from 'vega-embed'
import yaml from 'yaml'

import globalStore from '@/store'
import plugins from '@/plugins/pluginRegistry'
import HTTPFileSystem from '@/util/HTTPFileSystem'
import { BreadCrumb, VisualizationPlugin, SVNProject } from '../Globals'
import SankeyFlipper from '@/components/SankeyFlipper.vue'

interface VizEntry {
  component: string
  config: string
  title: string
}

interface IMyState {
  errorStatus: string
  folders: string[]
  files: string[]
  isLoading: boolean
  readme: string
  svnProject: SVNProject | null
  svnRoot?: HTTPFileSystem
  vizes: VizEntry[]
  runFinder: RunFinder
  activeButtons: { [heading: string]: string }
  runLogFolderLookup: { [options: string]: string }
  selectedRun: string
}

interface RunFinder {
  collection?: string
  notes?: string
  dimensions: {
    heading: string
    subheading?: string
    options: { title: string; value: string; image?: string }[]
  }[]
}

@Component({
  components: Object.assign({ SankeyFlipper }, plugins),
  props: {},
  i18n,
})
export default class VueComponent extends Vue {
  private globalState = globalStore.state
  private mdRenderer = new markdown()

  private runLookup: any = {}
  private modeSharePie: any = {}

  private runCosts = {
    fixedCosts: 1,
    variableCosts: 1,
  }

  private runHeader = {
    demand: 1,
    fleetSize: 1,
    mileage: 1,
    revenueDistance: 1,
    incomePerDay: 1,
    serviceQuality: 1,
  }

  private myState: IMyState = {
    errorStatus: '',
    folders: [],
    files: [],
    isLoading: false,
    readme: '',
    svnProject: null,
    vizes: [],
    runFinder: { dimensions: [] },
    activeButtons: {},
    runLogFolderLookup: {},
    selectedRun: '',
  }

  private getFileSystem(name: string) {
    const svnProject: any[] = globalStore.state.svnProjects.filter((a: any) => a.url === name)

    if (svnProject.length === 0) {
      console.log('no such project')
      throw Error
    }

    return svnProject[0]
  }

  private generateBreadcrumbs() {
    if (!this.myState.svnProject) return []
    const crumbs: any[] = []
    globalStore.commit('setBreadCrumbs', crumbs)
    return crumbs

    // const crumbs = [
    //   {
    //     label: 'aftersim',
    //     url: '/',
    //   },
    //   {
    //     label: this.myState.svnProject.name,
    //     url: '/' + this.myState.svnProject.url,
    //   },
    // ]

    // const subfolders = this.myState.subfolder.split('/')
    // let buildFolder = '/'
    // for (const folder of subfolders) {
    //   if (!folder) continue

    //   buildFolder += folder
    //   crumbs.push({
    //     label: folder,
    //     url: '/' + this.myState.svnProject.url + buildFolder,
    //   })
    // }

    // // save them!
    // globalStore.commit('setBreadCrumbs', crumbs)

    // return crumbs
  }

  private mounted() {
    globalStore.commit('setFullScreen', true)
    this.updateRoute()
  }

  private beforeDestory() {
    globalStore.commit('setFullScreen', false)
  }

  private needsInitialRun = true

  private clearState() {
    this.runLookup = {}
    this.modeSharePie = {}
    this.needsInitialRun = true

    this.myState = {
      errorStatus: '',
      folders: [],
      files: [],
      isLoading: false,
      readme: '',
      svnProject: null,
      vizes: [],
      runFinder: { dimensions: [] },
      activeButtons: {},
      runLogFolderLookup: {},
      selectedRun: '',
    }
  }

  @Watch('$route') async updateRoute() {
    if (!this.$route.name) return

    const svnProject = this.getFileSystem(this.$route.name)

    if (svnProject !== this.myState.svnProject) {
      console.log('clearning')
      this.clearState()
      this.myState.svnRoot = new HTTPFileSystem(svnProject)
      await this.buildRunFinder()
    }

    this.myState.svnProject = svnProject
    if (!this.myState.svnProject) return

    this.myState.svnRoot = new HTTPFileSystem(this.myState.svnProject)

    // is the specific run on the URL?
    if (this.$route.params.pathMatch) {
      this.myState.selectedRun = this.$route.params.pathMatch
    } else {
      this.myState.selectedRun = ''
      this.setInitialRun()
    }

    console.log({ selectedRun: this.myState.selectedRun })

    this.generateBreadcrumbs()

    // this happens async
    if (this.needsInitialRun) this.setInitialRun()

    if (this.myState.selectedRun) {
      await this.fetchFolderContents()
      this.showRunHeader()
    }
  }

  private async loadRunLog() {
    if (!this.myState.svnRoot) return

    this.myState.vizes = []

    let allRuns: any[] = []
    try {
      const csvFile = 'run-log.csv'
      const rawCSV = await this.myState.svnRoot.getFileText('/' + csvFile)
      this.runLookup = {}

      const runLog = Papaparse.parse(rawCSV, {
        header: true,
        dynamicTyping: true,
        skipEmptyLines: true,
        // delimiter: ';',
      })
      allRuns = runLog.data as any[]
    } catch (e) {
      console.log(e)
      this.myState.errorStatus = `NO RUN LOG! Add 'run-log.csv' to SVN ${this.$route.path} !`
    }

    // build lookup from the existing options -- start w/foldername and columns past 8
    allRuns.forEach(run => {
      let uniqueId = ''
      Object.values(run)
        .slice(10)
        .forEach(value => {
          if (value) uniqueId += `-${value}`
        })
      this.myState.runLogFolderLookup[uniqueId.slice(1)] = run.folder
      this.runLookup[run.folder] = run
    })
  }

  private async buildRunFinder() {
    if (!this.myState.svnRoot) return

    this.myState.isLoading = true

    await this.loadRunLog()

    const fname = 'run-lookup.yaml'
    const runYaml = yaml.parse(await this.myState.svnRoot.getFileText(fname))
    this.myState.runFinder = runYaml
  }

  private setInitialRun() {
    this.needsInitialRun = false
    const initialRun = this.runLookup[this.myState.selectedRun]
    for (const dimension of this.myState.runFinder.dimensions) {
      Vue.set(
        this.myState.activeButtons,
        dimension.heading,
        initialRun ? initialRun[dimension.heading] : dimension.options[0].value
      )
    }
    this.buildRunIdFromButtonSelections()
  }

  private clickedOptionButton(heading: string, option: string) {
    if (option === this.myState.activeButtons[heading]) return

    this.myState.activeButtons[heading] = option
    this.buildRunIdFromButtonSelections()
  }

  private buildRunIdFromButtonSelections() {
    if (!this.myState.svnProject) return

    const run = this.myState.runFinder.dimensions
      .map(d => {
        return this.myState.activeButtons[d.heading]
      })
      .join('-')

    const folder = this.myState.runLogFolderLookup[run]

    if (folder) {
      const path = `/${this.myState.svnProject.url}/${folder}`
      if (path !== this.$route.path) this.$router.replace(path)
    } else {
      this.myState.selectedRun = ''
      this.myState.vizes = []
    }
  }

  private expensesPerDay() {
    return Math.round(
      this.runHeader.fleetSize * this.runCosts.fixedCosts +
        this.runHeader.mileage * this.runCosts.variableCosts
    )
  }

  private revenuePerDay() {
    return this.runHeader.incomePerDay - this.expensesPerDay()
  }

  private annualIncome() {
    return this.revenuePerDay() * 365.0
  }

  private showRunHeader() {
    console.log('showRunHeader', this.myState.selectedRun)

    // get the run details
    const run = this.runLookup[this.myState.selectedRun]
    if (!run) return

    const incomePerDay =
      run.calcDemand * run.userCostFixed + run.calcRevenueDistance * run.userCostPerKm

    this.runHeader = {
      demand: 0 + run.calcDemand,
      fleetSize: 0 + run.calcFleetSize,
      mileage: Math.round(0 + run.calcMileage),
      revenueDistance: Math.round(0 + run.calcRevenueDistance),
      incomePerDay: Math.round(0 + incomePerDay),
      serviceQuality: 0 + run.calcServiceLevel,
    }

    this.runCosts = {
      fixedCosts: run.calcFixedCosts,
      variableCosts: run.calcVariableCosts,
    }

    this.buildModeSharePieChart()
  }

  private async buildModeSharePieChart() {
    console.log('pie chart')
    if (!this.myState.svnRoot) return

    const modeStats = this.myState.files.filter(a => a.endsWith('.modestats.txt'))

    if (!modeStats.length) return

    const fname = `/${this.myState.selectedRun}/${modeStats[0]}`
    const modeshareText = await this.myState.svnRoot.getFileText(fname)

    const parsed = Papaparse.parse(modeshareText, {
      header: true,
      dynamicTyping: true,
      skipEmptyLines: true,
    })

    const modeShares: any = parsed.data[parsed.data.length - 1]
    delete modeShares.Iteration
    delete modeShares.freight

    const vegaValues: any[] = []
    for (const key of Object.keys(modeShares)) {
      const share = Math.floor(1000 * modeShares[key]) / 10.0
      vegaValues.push({
        category: key,
        value: modeShares[key],
        label: `${key}: ${share}`,
      })
    }

    this.modeSharePie = {
      $schema: 'https://vega.github.io/schema/vega-lite/v4.json',
      description: 'Mode Share Summary',
      data: {
        values: vegaValues,
      },
      background: 'white',
      // mark: { type: 'arc', innerRadius: 25 },
      encoding: {
        theta: { field: 'value', type: 'quantitative', stack: true },
        color: { field: 'category', type: 'nominal', legend: null },
      },
      layer: [
        {
          mark: { type: 'arc', innerRadius: 25, outerRadius: 80 },
        },
        {
          mark: { type: 'text', radius: 108, fontWeight: 'bold', fontSize: 14 },
          encoding: {
            text: { field: 'label', type: 'nominal' },
          },
        },
      ],
      view: { stroke: null },
    }

    const embedOptions = {
      actions: false,
      hover: true,
      padding: { top: 5, left: 5, right: 5, bottom: 5 },
    }

    // vegaEmbed(`#pie-chart`, this.modeSharePie, embedOptions)
  }

  private clickedVisualization(vizNumber: number) {
    const viz = this.myState.vizes[vizNumber]

    // special case: images don't click thru
    if (viz.component === 'image-view') return

    if (!this.myState.svnProject) return

    const path = `/v/${viz.component}/${this.myState.svnProject.url}/${this.myState.selectedRun}/${viz.config}`
    console.log({ path })
    this.$router.push({ path })
  }

  private updateTitle(viz: number, title: string) {
    this.myState.vizes[viz].title = title
  }

  @Watch('globalState.colorScheme') swapColors() {
    // medium-zoom freaks out if color theme is swapped.
    // so let's reload images just in case.
    this.fetchFolderContents()
  }

  @Watch('globalState.authAttempts') authenticationChanged() {
    console.log('AUTH CHANGED - Reload')
    this.updateRoute()
  }

  @Watch('myState.files') async filesChanged() {
    // clear visualizations
    this.myState.vizes = []
    if (this.myState.files.length === 0) return

    await this.showReadme()

    this.buildShowEverythingView()

    // make sure page is rendered before we attach zoom semantics
    await this.$nextTick()
    mediumZoom('.medium-zoom', {
      background: '#444450',
    })
  }

  private async showReadme() {
    const readme = 'readme.md'

    // readme is just per ober-alt for PAVE

    if (!this.myState.svnRoot) return
    try {
      const text = await this.myState.svnRoot.getFileText(readme)
      this.myState.readme = this.mdRenderer.render(text)
    } catch (e) {
      console.warn('couldnt find readme')
      // no readme? oh well
    }
  }

  private buildShowEverythingView() {
    // loop on each viz type
    for (const viz of this.globalState.visualizationTypes.values()) {
      // filter based on file matching
      const matches = micromatch(this.myState.files, viz.filePatterns)

      for (const file of matches) {
        // add thumbnail for each matching file
        this.myState.vizes.push({ component: viz.kebabName, config: file, title: '◆' })
      }
    }
  }

  private async fetchFolderContents() {
    if (!this.myState.svnRoot) return []

    this.myState.isLoading = true
    this.myState.errorStatus = ''
    if (this.myState.files.length) this.myState.files = []

    try {
      const folderContents = await this.myState.svnRoot.getDirectory(`/${this.myState.selectedRun}`)

      // hide dot folders
      const folders = folderContents.dirs.filter(f => !f.startsWith('.')).sort()
      const files = folderContents.files.filter(f => !f.startsWith('.')).sort()

      this.myState.errorStatus = ''
      this.myState.folders = folders
      this.myState.files = files
    } catch (e) {
      if (this.myState.selectedRun) return

      // Bad things happened! Tell user
      console.log('BAD PAGE')
      console.log({ eeee: e })

      this.myState.folders = []
      this.myState.files = []

      this.myState.errorStatus = '<h3>'
      if (e.status) this.myState.errorStatus += `${e.status} `
      if (e.statusText) this.myState.errorStatus += `${e.statusText}`
      if (this.myState.errorStatus === '<h3>') this.myState.errorStatus += 'Error'
      this.myState.errorStatus += `</h3>`
      if (e.url) this.myState.errorStatus += `<p>${e.url}</p>`
      if (e.message) this.myState.errorStatus += `<p>${e.message}</p>`
      if (this.myState.errorStatus === '<h3>Error</h3>') this.myState.errorStatus = '' + e

      if (this.myState.svnProject) {
        this.myState.errorStatus += `<p><i>${this.myState.svnProject.svn}</i></p>`
      }

      // maybe it failed because password?
      if (this.myState.svnProject && this.myState.svnProject.need_password && e.status === 401) {
        globalStore.commit('requestLogin', this.myState.svnProject.url)
      }
    } finally {
      this.myState.isLoading = false
    }
  }
}
</script>

<style scoped lang="scss">
@import '@/styles.scss';

.folder-browser {
  display: grid;
  grid-template-columns: 18rem 1fr;
  grid-template-rows: 100%;
}

.left-strip {
  background-color: var(--text);
  color: var(--bgPanel);
  padding: 0 2rem;
}

.right-strip {
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  background-color: var(--bg);
}

.vessel {
  margin: 0 auto;
  padding: 0rem 3rem 0.5rem 3rem;
  max-width: $sizeVessel;
  transition: padding 0.2s ease-in-out;
}

.performance {
  max-width: 56rem;
  padding: 0 1rem 1rem 1rem;
}

.white {
  background-color: var(--bgBold);
}

.cream {
  background-color: var(--bgCream);
}

.cream2 {
  background-color: var(--bgCream2);
}

h3,
h4 {
  margin-top: 2rem;
  margin-bottom: 0.5rem;
}

h2 {
  font-size: 1.8rem;
}

.badnews {
  border-left: 3rem solid #af232f;
  margin: 0rem 0rem;
  padding: 0.5rem 0rem;
  background-color: #ffc;
  color: $matsimBlue;
}

.viz-table {
  display: grid;
  grid-gap: 2rem;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  list-style: none;
}

.viz-grid-item {
  z-index: 1;
  text-align: center;
  margin: 0 0;
  padding: 0 0;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  vertical-align: top;
  background-color: var(--bgBold);
  border: var(--borderThin);
  border-radius: 16px;
}

.viz-frame {
  z-index: 1;
  flex: 1;
  border-radius: 16px;
  overflow: hidden;
  display: flex;
  flex-direction: column;

  p {
    margin: auto 0 auto 0;
    background-color: var(--bgBold);
    font-size: 1rem;
    font-weight: bold;
    line-height: 1.2rem;
    padding: 1rem 0.5rem;
    color: var(--text);
    word-wrap: break-word;
  }
}

.viz-frame:hover {
  box-shadow: var(--shadowMode);
  transition: box-shadow 0.1s ease-in-out;
}

.viz-frame-component {
  background-color: white;
}

.logo {
  margin-left: auto;
}

.folder-table {
  display: grid;
  row-gap: 0rem;
  column-gap: 1rem;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  list-style: none;
  margin-bottom: 0px;
  padding-left: 0px;
}

.folder {
  cursor: pointer;
  display: flex;
  flex-direction: column;
  background-color: var(--bgBold);
  margin: 0.25rem 0rem;
  padding: 0.75rem 1rem;
  border-radius: 8px;
}

.folder:hover {
  background-color: var(--bgHover);
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.08), 0 3px 10px 0 rgba(0, 0, 0, 0.08);
  transition: box-shadow 0.1s ease-in-out;
}

.project-bar {
  display: flex;
  margin-bottom: 0rem;
  padding: 1rem 0 0 0;
  z-index: 10000;
}

.project-bar p {
  margin-top: -0.25rem;
}

.fade {
  opacity: 0.6;
}

.file-table {
  display: grid;
  row-gap: 0rem;
  column-gap: 1rem;
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.file {
  word-break: break-all;
  line-height: 1rem;
  margin-bottom: 0.5rem;
}

.markdown {
  padding: 1rem 0rem;
}

.curated-sections {
  display: flex;
  flex-direction: column;
}

.curate-heading {
  padding: 1rem 0rem 0 0;
  margin: 0rem 0rem;
}

.call-out-box {
  padding: 0.5rem 1rem 0 1rem;
  border-radius: 10px;
}

.readme-header {
  font-size: 1rem;
  padding-bottom: 1rem;
}

h3.curate-heading {
  font-size: 1.8rem;
  font-weight: bold;
  color: var(--textFancy);
  margin-bottom: 0.5rem;
}

.curate-content {
  padding: 1rem 0rem;
  margin: 0rem 0rem;
}

.dimensions {
  top: 4.5rem;
  position: sticky;
  margin: 1rem 0 0rem 0;
  display: grid;
  gap: 2rem;

  h3 {
    margin: 0 0;
    padding: 0 0;
  }
}

.dimension {
  // border: var(--borderThin);
  grid-column: 1 / 2;
  // padding: 0.5rem 0.5rem 1rem 0.5rem;
  // margin: 0.5rem 0.25rem;
}

.dimension h4 {
  margin: 0.5rem 0 0 0;
  line-height: 1.2rem;
  flex: 1;
  font-size: 1.1rem;
  font-weight: bold;
  color: var(--bgCream);
}

.dimension p {
  font-size: 0.8rem;
  margin-bottom: 0.5rem;
}

.dimension .button {
  margin: 0.1rem 0;
  font-size: 0.8rem;
  width: 100%;
}

.summary-table {
  margin: 1rem 0;
  display: flex;
  flex-direction: row;

  .col1 {
    display: flex;
    flex-direction: column;
    width: min-content;
    text-align: right;
    margin-bottom: 1rem;
  }

  .col2 {
    flex-direction: column;
    margin-left: 0.5rem;
    width: min-content;

    .tlabel {
      margin-left: auto;
      font-weight: bold;
    }
  }

  .col3 {
    flex: 1;
    padding: 0.5rem 0.5rem;
  }
}

.blue {
  color: var(--link);
}

.red {
  color: $tuRed;
}

.tlabel {
  width: max-content;
  margin: 0 0;
  padding: 0 0;
}

.vspace {
  padding-bottom: 1rem;
}

@media only screen and (max-width: 50em) {
  .viz-table {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .folder-table {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .file-table {
    display: grid;
    grid-gap: 0rem;
    grid-template-columns: 1fr;
  }
}

@media only screen and (max-width: 40em) {
  .vessel {
    padding: 0 1rem 0 1rem;
  }

  .folder-table {
    display: grid;
    grid-gap: 0rem;
    grid-template-columns: 1fr;
  }

  .viz-table {
    display: grid;
    grid-gap: 2rem;
    grid-template-columns: 1fr;
  }

  .viz-frame {
    p {
      font-size: 1rem;
    }
  }

  .curate-heading {
    border-bottom: none;
    padding: 1rem 0rem;
  }

  h3.curate-heading {
    padding-top: 1rem;
    font-weight: bold;
  }

  .curate-content {
    border-bottom: none;
    padding-top: 0rem;
  }

  .file {
    font-size: 0.8rem;
  }

  .logo {
    display: none;
  }
}
</style>
