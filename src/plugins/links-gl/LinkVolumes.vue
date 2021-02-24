<i18n>
en:
  all: "All"
  colors: "Colors"
  loading: "Loading"
  selectColumn: "Select data column"
  timeOfDay: "Time of Day"

de:
  all: "Alle"
  colors: "Farben"
  loading: "Wird geladen"
  selectColumn: "Datenspalte wählen"
  timeOfDay: "Uhrzeit"
</i18n>

<template lang="pug">
.gl-viz(:class="{'hide-thumbnail': !thumbnail}"
        :style='{"background": urlThumbnail}' oncontextmenu="return false")

  link-gl-layer.anim(v-if="!thumbnail && isLoaded && geojsonFilename"
                :center="center"
                :networkUrl="geojsonFilename"
                :data="csvData"
                :dark="isDarkMode"
                :colors="selectedColorRamp"
  )

  .right-side(v-if="isLoaded && !thumbnail")
    collapsible-panel(:darkMode="isDarkMode" width="250" direction="right")
      .panel-items

        //- heading
        .panel-item
          h3 {{ vizDetails.title }}
          p {{ vizDetails.description }}

          //- label.checkbox
          //-   input(type="checkbox" v-model="showTimeRange")
          //-   | &nbsp;Zeitraum

      //- .panel-items
      //-   h4.heading Linienbreiten
      //-   scale-slider.time-slider(v-if="headers.length > 0"
      //-     :stops='SCALE_STOPS'
      //-     @change='bounceScale')
      //-   label.checkbox
      //-     input(type="checkbox" v-model="showAllRoads")
      //-     | &nbsp;Gesamtes Straßennetz anzeigen


      .panel-items

        //- time-of-day slider
        .panel-item(v-if="vizDetails.useSlider")
          p: b {{ $t('timeOfDay') }}
          p(v-if="csvData.header.length===0"): b {{ `(${$t('loading')}...)` }}
          time-slider.time-slider(v-if="csvData.header.length > 0"
            :useRange='showTimeRange'
            :stops="csvData.header"
            @change='bounceTimeSlider')

        //- button/dropdown for selecting column
        .panel-item(v-if="!vizDetails.useSlider")
          p: b {{ $t('selectColumn') }}
          .dropdown.full-width.is-hoverable
            .dropdown-trigger
              button.full-width.is-warning.button(:class="{'is-loading': csvData.activeColumn < 0}"
                aria-haspopup="true" aria-controls="dropdown-menu-column-selector")

                b {{ buttonTitle }}
                span.icon.is-small
                  i.fas.fa-angle-down(aria-hidden="true")

            #dropdown-menu-column-selector.dropdown-menu(role="menu" :style="{'max-height':'16rem', 'overflow-y': 'auto', 'border': '1px solid #ccc'}")
              .dropdown-content
                a.dropdown-item(v-for="column in csvData.header"
                                @click="handleNewDataColumn(column)") {{ column }}

        .panel-item(v-if="csvData.activeColumn > -1")
          p: b {{ $t('colors') }}
          .dropdown.full-width.is-hoverable
            .dropdown-trigger
              //- button.full-width.button(:class="{'is-loading': csvData.activeColumn < 0}"
              //-   aria-haspopup="true" aria-controls="dropdown-menu-column-selector")

              img(:src="`/pave/colors/scale-${selectedColorRamp}.png`"
                  :style="{'height': '2.3rem', 'width': '100%', 'border-radius': '5px'}")

                //- span {{ selectedColorRamp }}
                //- span.icon.is-small
                //-   i.fas.fa-angle-down(aria-hidden="true")

            #dropdown-menu-color-selector.dropdown-menu(role="menu")
              .dropdown-content(:style="{'padding':'0 0','backgroundColor': '#00000011'}")
                a.dropdown-item(v-for="colorRamp in Object.keys(colorRamps)"
                                @click="clickedColorRamp(colorRamp)"
                                :style="{'padding': '0.25rem 0.25rem'}"
                )
                  img(:src="`/colors/scale-${colorRamp}.png`")
                  p(:style="{'color':'black','lineHeight': '1rem', 'marginBottom':'0.25rem'}") {{ colorRamp }}

        //- .panel-item
        //-   p: b Bandwidths
        //-   input.input

        //- .panel-item
        //-   button.button picker


  .nav(v-if="!thumbnail && myState.statusMessage")
    p.status-message {{ myState.statusMessage }}

</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from 'vue-property-decorator'
import Papaparse from 'papaparse'
import { ToggleButton } from 'vue-js-toggle-button'
import { debounce } from 'debounce'
import readBlob from 'read-blob'
import { Route } from 'vue-router'
import YAML from 'yaml'
import vuera from 'vuera'
import crossfilter from 'crossfilter2'
import { blobToArrayBuffer, blobToBinaryString } from 'blob-util'
import * as coroutines from 'js-coroutines'

import globalStore from '@/store'
import pako from '@aftersim/pako'
import CollapsiblePanel from '@/components/CollapsiblePanel.vue'
import TimeSlider from '@/plugins/links-gl/TimeSlider.vue'

import {
  ColorScheme,
  FileSystem,
  LegendItem,
  LegendItemType,
  SVNProject,
  VisualizationPlugin,
  LIGHT_MODE,
  DARK_MODE,
} from '@/Globals'

import LinkGlLayer from './LinkLayer'
import HTTPFileSystem from '@/util/HTTPFileSystem'
import { VuePlugin } from 'vuera'
Vue.use(VuePlugin)

interface CSV {
  header: string[]
  headerMax: number[]
  rows: { [id: string]: number[] }
  activeColumn: number
}

@Component({
  components: {
    CollapsiblePanel,
    LinkGlLayer,
    TimeSlider,
    ToggleButton,
  } as any,
})
class MyPlugin extends Vue {
  @Prop({ required: false })
  private fileApi!: FileSystem

  @Prop({ required: false })
  private subfolder!: string

  @Prop({ required: false })
  private yamlConfig!: string

  @Prop({ required: false })
  private thumbnail!: boolean

  private geojsonFilename = ''
  private isButtonActiveColumn = false
  private center = [13.45, 52.53]

  private showTimeRange = false
  private bounceTimeSlider = debounce(this.changedTimeSlider, 200)

  private selectedColorRamp = 'viridis'

  private colorRamps: { [title: string]: { png: string; diff?: boolean } } = {
    viridis: { png: 'scale-viridis.png' },
    // salinity: { png: 'scale-salinity.png' },
    inferno: { png: 'scale-inferno.png' },
    bluered: { png: 'scale-salinity.png', diff: true },
    picnic: { png: 'scale-picnic.png' },
  }

  private vizDetails = {
    title: '',
    description: '',
    csvFile: '',
    csvBase: '',
    useSlider: false,
    shpFile: '',
    dbfFile: '',
    geojsonFile: '',
    projection: '',
    scaleFactor: 1,
    thumbnail: '',
    sum: false,
  }

  public myState = {
    statusMessage: '',
    fileApi: this.fileApi,
    fileSystem: undefined as SVNProject | undefined,
    subfolder: this.subfolder,
    yamlConfig: this.yamlConfig,
    thumbnail: this.thumbnail,
  }

  private csvData: CSV = { header: [], headerMax: [], rows: {}, activeColumn: -1 }

  private globalState = globalStore.state
  private isDarkMode = this.globalState.colorScheme === ColorScheme.DarkMode
  private isLoaded = false

  // this happens if viz is the full page, not a thumbnail on a project page
  private buildRouteFromUrl() {
    const params = this.$route.params
    if (!params.project || !params.pathMatch) {
      console.log('I CANT EVEN: NO PROJECT/PARHMATCH')
      return
    }

    // project filesystem
    const filesystem = this.getFileSystem(params.project)
    this.myState.fileApi = new HTTPFileSystem(filesystem)
    this.myState.fileSystem = filesystem

    // subfolder and config file
    const sep = 1 + params.pathMatch.lastIndexOf('/')
    const subfolder = params.pathMatch.substring(0, sep)
    const config = params.pathMatch.substring(sep)

    this.myState.subfolder = subfolder
    this.myState.yamlConfig = config
  }

  private generateBreadcrumbs() {
    if (!this.myState.fileSystem) return []

    const crumbs = [
      {
        label: this.myState.fileSystem.name,
        url: '/' + this.myState.fileSystem.url,
      },
    ]

    const subfolders = this.myState.subfolder.split('/')
    let buildFolder = '/'
    for (const folder of subfolders) {
      if (!folder) continue

      buildFolder += folder + '/'
      crumbs.push({
        label: folder,
        url: '/' + this.myState.fileSystem.url + buildFolder,
      })
    }

    crumbs.push({
      label: this.vizDetails.title,
      url: '#',
    })

    // save them!
    globalStore.commit('setBreadCrumbs', crumbs)

    return crumbs
  }

  private thumbnailUrl = "url('assets/thumbnail.jpg') no-repeat;"
  private get urlThumbnail() {
    return this.thumbnailUrl
  }

  private getFileSystem(name: string) {
    const svnProject: any[] = globalStore.state.svnProjects.filter((a: any) => a.url === name)
    if (svnProject.length === 0) {
      console.log('no such project')
      throw Error
    }
    return svnProject[0]
  }

  private async getVizDetails() {
    // first get config
    try {
      const text = await this.myState.fileApi.getFileText(
        this.myState.subfolder + '/' + this.myState.yamlConfig
      )
      this.vizDetails = YAML.parse(text)
    } catch (e) {
      console.log('failed')
      // maybe it failed because password?
      if (this.myState.fileSystem && this.myState.fileSystem.need_password && e.status === 401) {
        globalStore.commit('requestLogin', this.myState.fileSystem.url)
      }
    }
    const t = this.vizDetails.title ? this.vizDetails.title : 'Network Links'
    this.$emit('title', t)
  }

  private async buildThumbnail() {
    if (this.thumbnail && this.vizDetails.thumbnail) {
      try {
        const blob = await this.myState.fileApi.getFileBlob(
          this.myState.subfolder + '/' + this.vizDetails.thumbnail
        )
        const buffer = await readBlob.arraybuffer(blob)
        const base64 = this.arrayBufferToBase64(buffer)
        if (base64)
          this.thumbnailUrl = `center / cover no-repeat url(data:image/png;base64,${base64})`
      } catch (e) {
        console.error(e)
      }
    }
  }

  @Watch('globalState.authAttempts') private async authenticationChanged() {
    console.log('AUTH CHANGED - Reload')
    if (!this.yamlConfig) this.buildRouteFromUrl()
    await this.getVizDetails()
  }

  @Watch('globalState.colorScheme') private swapTheme() {
    this.isDarkMode = this.globalState.colorScheme === ColorScheme.DarkMode
  }

  private arrayBufferToBase64(buffer: any) {
    var binary = ''
    var bytes = new Uint8Array(buffer)
    var len = bytes.byteLength
    for (var i = 0; i < len; i++) {
      binary += String.fromCharCode(bytes[i])
    }
    return window.btoa(binary)
  }

  private get buttonTitle() {
    if (this.csvData.activeColumn === -1) return 'Loading...'
    return this.csvData.header[this.csvData.activeColumn]
  }

  private clickedColorRamp(color: string) {
    this.selectedColorRamp = color
    console.log(this.selectedColorRamp)
  }

  private handleNewDataColumn(title: string) {
    const column = this.csvData.header.indexOf(title)
    if (column === -1) return

    // find max value for scaling
    if (!this.csvData.headerMax[column]) {
      let max = 0
      Object.values(this.csvData.rows).forEach(row => {
        max = Math.max(max, row[column])
      })
      if (max) this.csvData.headerMax[column] = max
    }

    // set the new column
    this.csvData.activeColumn = column
    this.isButtonActiveColumn = false
  }

  private findCenter(data: any[]): [number, number] {
    return [13.45, 52.53]
  }

  private async mounted() {
    globalStore.commit('setFullScreen', !this.thumbnail)

    if (!this.yamlConfig) this.buildRouteFromUrl()
    await this.getVizDetails()
    if (this.thumbnail) return

    this.generateBreadcrumbs()

    this.myState.statusMessage = 'Dateien laden...'

    // runs in background
    this.loadCSVFiles()

    // runs in background
    const network = `/${this.myState.subfolder}/${this.vizDetails.geojsonFile}`
    this.geojsonFilename = this.myState.fileApi.cleanURL(network)
    this.center = this.findCenter([])

    this.isLoaded = true
    this.buildThumbnail()

    this.myState.statusMessage = ''
  }

  private beforeDestroy() {
    globalStore.commit('setFullScreen', false)
    this.$store.commit('setFullScreen', false)
  }

  // private handleClickColumnSelector() {
  //   console.log('click!')
  //   this.isButtonActiveColumn = !this.isButtonActiveColumn
  // }

  private async loadCSVFiles() {
    console.log('loading CSV files')

    const csvFilename = this.myState.fileApi.cleanURL(
      `${this.myState.subfolder}/${this.vizDetails.csvFile}`
    )

    let globalMax = 0

    try {
      Papaparse.parse(csvFilename, {
        fastMode: true,
        download: true,
        skipEmptyLines: true,
        dynamicTyping: true,
        complete: (results: { data: any[] }) => {
          console.log('parsing')

          // create object with link-id as lookup-key
          const allLinks: { [id: string]: number[] } = {}

          // coroutine to not kill browser
          coroutines
            .forEachAsync(results.data.splice(1), (link: any) => {
              const key = link[0].toString()
              if (this.vizDetails.useSlider) {
                const entries = link.slice(1) // skip first element (contains link-id)
                const total = entries.reduce((a: number, b: number) => a + b, 0)
                globalMax = Math.max(globalMax, total)
                allLinks[key] = [total, ...entries] // total comes first
              } else {
                allLinks[key] = link.slice(1) // skip first element (contains link-id)
              }
            })
            .then(result => {
              const header = results.data[0].slice(1) as string[]
              if (this.vizDetails.useSlider) header.unshift(`${this.$t('all')}`)

              this.finishedLoadingCSVs(header, allLinks, globalMax)
            })
        },
      })
    } catch (e) {
      console.error(e)
      this.myState.statusMessage = '' + e
    }
  }

  private finishedLoadingCSVs(
    header: string[],
    allLinks: { [id: string]: number[] },
    globalMax: number
  ) {
    const cleanHeaders = header.map(h => h.replace(':00:00', ''))

    this.csvData = {
      header: cleanHeaders,
      headerMax: this.vizDetails.useSlider
        ? new Array(this.csvData.header.length).fill(globalMax)
        : [],
      rows: allLinks,
      activeColumn: -1,
    }

    console.log({ csvData: this.csvData })
    // need to do this! select first entry
    this.handleNewDataColumn(this.csvData.header[0])
  }

  private changedTimeSlider(value: any) {
    console.log('new slider!', value)
    if (value.length && value.length === 1) value = value[0]

    this.handleNewDataColumn(value)

    // this.currentTimeBin = value
    // const widthFactor = this.WIDTH_SCALE * this.currentScale

    // if (this.showTimeRange == false) {
    //   this.map.setPaintProperty('my-layer', 'line-width', [
    //     '*',
    //     widthFactor,
    //     ['abs', ['get', value]],
    //   ])
    //   this.map.setPaintProperty('my-layer', 'line-offset', [
    //     '*',
    //     0.5 * widthFactor,
    //     ['abs', ['get', value]],
    //   ])

    //   // this complicated mess is how MapBox deals with conditionals. Yuck!
    //   // #ff0 -- yellow hover
    //   // #8ca -- null, no data
    //   // #55b -- bluish/purple, link volume bandwidth
    //   // #900 -- deep red, diff volume positive
    //   // #5f5 -- bright light green, diff volume negative

    //   this.map.setPaintProperty('my-layer', 'line-color', [
    //     'case',
    //     ['boolean', ['feature-state', 'hover'], false],
    //     '#ff0',
    //     ['==', ['get', value], null],
    //     '#8ca',
    //     ['<', ['get', value], 0],
    //     '#5f5',
    //     this.vizDetails.csvFile2 ? '#900' : '#55b',
    //   ])

    //   const filter = this.showAllRoads ? null : ['!=', ['get', this.currentTimeBin], null]
    //   this.map.setFilter('my-layer', filter)
    // } else {
    //   const sumElements: any = ['+']

    //   // build the summation expressions: e.g. ['+', ['get', '1'], ['get', '2']]
    //   let include = false
    //   for (const header of this.headers) {
    //     if (header === value[0]) include = true

    //     // don't double-count the total
    //     if (header === this.TOTAL_MSG) continue

    //     if (include) sumElements.push(['get', header])

    //     if (header === value[1]) include = false
    //   }

    //   this.map.setPaintProperty('my-layer', 'line-width', ['*', widthFactor, sumElements])
    //   this.map.setPaintProperty('my-layer', 'line-offset', ['*', 0.5 * widthFactor, sumElements])
    //   this.map.setPaintProperty('my-layer', 'line-color', [
    //     'case',
    //     ['boolean', ['feature-state', 'hover'], false],
    //     '#0f6',
    //     ['==', sumElements, null],
    //     '#8ca',
    //     ['<', sumElements, 0],
    //     '#fc0',
    //     '#559',
    //   ])
    // }
  }
}

// !register plugin!
globalStore.commit('registerPlugin', {
  kebabName: 'links-gl',
  prettyName: 'Links',
  description: 'Network link attributes',
  filePatterns: ['viz-gl-link*.y?(a)ml'],
  component: MyPlugin,
} as VisualizationPlugin)

export default MyPlugin
</script>

<style scoped lang="scss">
@import '~vue-slider-component/theme/default.css';
@import '@/styles.scss';

.gl-viz {
  display: grid;
  pointer-events: none;
  min-height: $thumbnailHeight;
  background: url('assets/thumbnail.jpg') no-repeat;
  background-size: cover;
  grid-template-columns: auto 1fr min-content;
  grid-template-rows: auto 1fr auto;
  grid-template-areas:
    'leftside    .        .'
    '.           .        .'
    '.           .  rightside';
}

.gl-viz.hide-thumbnail {
  background: none;
}

.nav {
  z-index: 5;
  grid-column: 1 / 4;
  grid-row: 1 / 4;
  display: flex;
  flex-direction: row;
  margin: auto auto;
  background-color: #00000080;
  padding: 0.25rem 3rem;

  a {
    font-weight: bold;
    color: white;
    text-decoration: none;

    &.router-link-exact-active {
      color: white;
    }
  }

  p {
    margin: auto 0.5rem auto 0;
    padding: 0 0;
    color: white;
  }
}

.legend-block {
  margin-top: 2rem;
}

.status-message {
  padding: 0rem 0;
  font-size: 1.5rem;
  line-height: 3.25rem;
  font-weight: bold;
}

.big {
  padding: 0rem 0;
  // margin-top: 1rem;
  font-size: 2rem;
  line-height: 3.75rem;
  font-weight: bold;
}

.left-side {
  display: flex;
  flex-direction: column;
  grid-area: leftside;
  background-color: var(--bgPanel);
  box-shadow: 0px 2px 10px #22222266;
  font-size: 0.8rem;
  pointer-events: auto;
  margin: 2rem 0 3rem 0;
}

.right-side {
  display: flex;
  flex-direction: row;
  position: absolute;
  top: 0rem;
  bottom: 0rem;
  right: 0;
  margin: 8rem 0 5rem 0;
  background-color: var(--bgPanel);
  box-shadow: 0px 2px 10px #22222266;
  font-size: 0.8rem;
  pointer-events: auto;
}

.playback-stuff {
  flex: 1;
}

.anim {
  z-index: -1;
  grid-column: 1 / 3;
  grid-row: 1 / 7;
  pointer-events: auto;
}

.panel-items {
  margin: 0.5rem 0.5rem;
}

.panel-item {
  margin-bottom: 1rem;

  h3 {
    line-height: 1.7rem;
    margin-bottom: 0.5rem;
  }

  p {
    font-size: 0.9rem;
  }
}

input {
  border: none;
  background-color: var(--bgCream2);
  color: var(--bgDark);
}

.row {
  display: 'grid';
  grid-template-columns: 'auto 1fr';
}

label {
  margin: auto 0 auto 0rem;
  text-align: 'left';
}

.toggle {
  margin-bottom: 0.25rem;
  margin-right: 0.5rem;
}

.full-width {
  display: block;
  width: 100%;
}

@media only screen and (max-width: 640px) {
  .nav {
    padding: 0.5rem 0.5rem;
  }

  .right-side {
    font-size: 0.7rem;
  }

  .big {
    padding: 0 0rem;
    margin-top: 0.5rem;
    font-size: 1.3rem;
    line-height: 2rem;
  }
}
</style>
