<i18n>
messages:
  en:
    title: "hello world"
  de:
    title: "Hallo liebe"
</i18n>

<template lang="pug">
.gl-viz(:class="{'hide-thumbnail': !thumbnail}"
        :style='{"background": urlThumbnail}' oncontextmenu="return false")

  link-gl-layer.anim(v-if="!thumbnail && isLoaded && geojsonFilename"
                :center="center"
                :networkUrl="geojsonFilename"
                :csvData="csvData.rows"
                :csvColumn="csvData.activeColumn"
  )

  .left-side(v-if="isLoaded && !thumbnail")
    //- collapsible-panel(:darkMode="true" width="250" direction="left")
    //-   .panel-items
    //-     p.big.xtitle {{ vizDetails.title }}
    //-     p {{ vizDetails.description }}

  .right-side(v-if="isLoaded && !thumbnail")
    collapsible-panel(:darkMode="true" width="250" direction="right")
      .panel-items

        .panel-item
          h3 {{ vizDetails.title }}
          p {{ vizDetails.description }}

        .panel-item
          .dropdown.full-width.is-hoverable
            .dropdown-trigger
              button.full-width.is-warning.button(:class="{'is-loading': csvData.activeColumn < 0}"
                aria-haspopup="true" aria-controls="dropdown-menu-column-selector")

                span {{ buttonTitle }}
                span.icon.is-small
                  i.fas.fa-angle-down(aria-hidden="true")

            #dropdown-menu-column-selector.dropdown-menu(role="menu")
              .dropdown-content
                a.dropdown-item(v-for="column in csvData.header"
                                @click="clickedColumn(column)") {{ column }}

        //- .panel-item
        //-   p.speed-label Aggregate

  .nav(v-if="!thumbnail && myState.statusMessage")
    p.status-message {{ myState.statusMessage }}

</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from 'vue-property-decorator'
import Papaparse from 'papaparse'
import VueSlider from 'vue-slider-component'
import { ToggleButton } from 'vue-js-toggle-button'
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
import { parseNumbers } from 'xml2js/lib/processors'
Vue.use(VuePlugin)

interface CSV {
  header: string[]
  rows: { [id: string]: number[] }
  activeColumn: number
}

@Component({
  components: {
    CollapsiblePanel,
    LinkGlLayer,
    VueSlider,
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

  private vizDetails = {
    title: '',
    description: '',
    csvFile: '',
    csvFile2: '',
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
    colorScheme: ColorScheme.DarkMode,
    fileApi: this.fileApi,
    fileSystem: undefined as SVNProject | undefined,
    subfolder: this.subfolder,
    yamlConfig: this.yamlConfig,
    thumbnail: this.thumbnail,
  }

  private csvData: CSV = { header: [], rows: {}, activeColumn: -1 }

  private globalState = globalStore.state
  private isDarkMode = this.myState.colorScheme === ColorScheme.DarkMode
  private isLoaded = true

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

  @Watch('state.colorScheme') private swapTheme() {
    this.isDarkMode = this.myState.colorScheme === ColorScheme.DarkMode
    // this.updateLegendColors()
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

  private clickedColumn(title: string) {
    const column = this.csvData.header.indexOf(title)
    if (column > -1) this.csvData.activeColumn = column
    this.isButtonActiveColumn = false
  }

  private updateLegendColors() {
    // const theme = this.myState.colorScheme == ColorScheme.LightMode ? LIGHT_MODE : DARK_MODE
    // this.legendBits = [
    //   { label: 'susceptible', color: theme.susceptible },
    //   { label: 'latently infected', color: theme.infectedButNotContagious },
    //   { label: 'contagious', color: theme.contagious },
    //   { label: 'symptomatic', color: theme.symptomatic },
    //   { label: 'seriously ill', color: theme.seriouslyIll },
    //   { label: 'critical', color: theme.critical },
    //   { label: 'recovered', color: theme.recovered },
    // ]
  }

  private get textColor() {
    const lightmode = {
      text: '#3498db',
      bg: '#eeeef480',
    }

    const darkmode = {
      text: 'white',
      bg: '#181518aa',
    }

    return this.myState.colorScheme === ColorScheme.DarkMode ? darkmode : lightmode
  }

  private findCenter(data: any[]): [number, number] {
    return [13.4, 52.5]
    // let prop = '' // get first property only
    // for (prop in this.aggregations) break

    // const xcol = this.aggregations[prop][0]
    // const ycol = this.aggregations[prop][1]

    // let x = 0
    // let y = 0

    // let count = 0
    // for (let i = 0; i < data.length; i += 64) {
    //   count++
    //   x += data[i][xcol]
    //   y += data[i][ycol]
    // }
    // x = x / count
    // y = y / count

    // return [x, y]
  }

  private center = [0, 0]

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
    this.geojsonFilename = `${this.myState.fileSystem?.svn}/${this.myState.subfolder}/${this.vizDetails.geojsonFile}`
    this.center = this.findCenter([])

    this.isLoaded = true
    this.buildThumbnail()

    console.log('DRT Anfragen sortieren...')
    this.myState.statusMessage = 'DRT Anfragen sortieren...'
    // this.handleOrigDest(Object.keys(this.aggregations)[0]) // origins

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
    console.log('loading files')
    let csvData: any = []
    let csvBase: any = []

    const csvFilename = this.myState.fileApi.cleanURL(
      `${this.myState.subfolder}/${this.vizDetails.csvFile}`
    )

    try {
      Papaparse.parse(csvFilename, {
        fastMode: true,
        download: true,
        skipEmptyLines: true,
        dynamicTyping: true,
        complete: (results: { data: any[] }) => {
          console.log('parsing')
          const rows: { [id: string]: number[] } = {}
          results.data.slice(1).forEach(a => {
            rows[a[0].toString()] = a.slice(1)
          })
          this.csvData = { header: results.data[0].slice(1), rows, activeColumn: 0 }
          console.log({ csvData: this.csvData })
        },
      })
    } catch (e) {
      console.error(e)
      this.myState.statusMessage = '' + e
    }
  }

  private rotateColors() {
    this.myState.colorScheme =
      this.myState.colorScheme === ColorScheme.DarkMode
        ? ColorScheme.LightMode
        : ColorScheme.DarkMode
    localStorage.setItem('plugin/agent-animation/colorscheme', this.myState.colorScheme)
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
    '.     .        .'
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
  // border: 1px solid $matsimBlue;
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

.speed-block {
  margin-top: 1rem;
}

.legend-block {
  margin-top: 2rem;
}

.speed-slider {
  flex: 1;
  width: 100%;
  margin: 0rem 0.5rem 0.25rem 0.25rem;
  font-weight: bold;
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
  grid-area: leftside;
  background-color: $steelGray;
  box-shadow: 0px 2px 10px #11111188;
  color: white;
  display: flex;
  flex-direction: column;
  font-size: 0.8rem;
  pointer-events: auto;
  margin: 2rem 0 3rem 0;
}

.right-side {
  position: absolute;
  top: 0rem;
  bottom: 0rem;
  right: 0;
  margin: 6rem 0 5rem 0;
  background-color: $steelGray;
  box-shadow: 0px 2px 10px #111111ee;
  color: white;
  display: flex;
  flex-direction: row;
  font-size: 0.8rem;
  pointer-events: auto;
}

.playback-stuff {
  flex: 1;
}

.bottom-area {
  display: flex;
  flex-direction: row;
  margin-bottom: 2rem;
  grid-area: playback;
  padding: 0rem 1rem 1rem 2rem;
  pointer-events: auto;
}

.settings-area {
  z-index: 20;
  pointer-events: auto;
  background-color: $steelGray;
  color: white;
  font-size: 0.8rem;
  padding: 0.25rem 0;
  margin: 1.5rem 0rem 0 0;
}

.anim {
  background-color: #181919;
  z-index: -1;
  grid-column: 1 / 3;
  grid-row: 1 / 7;
  pointer-events: auto;
}

.speed-label {
  font-size: 0.8rem;
  font-weight: bold;
}

p.speed-label {
  margin-bottom: 0.25rem;
}

.tooltip {
  padding: 5rem 5rem;
  background-color: #ccc;
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
  background-color: #235;
  color: #ccc;
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
