<i18n>
en:
  Analysis: 'Analysis'
  Files: 'Files'
  Folders: 'Folders'
de:
  Analysis: 'Ergebnisse'
  Files: 'Dateien'
  Folders: 'Ordner'
</i18n>

<template lang="pug">
.folder-browser
  .dimensions(v-if="dimensions.length")
    h5: b Filters:
    .dimension(v-for="d in dimensions" :key="d.heading")
      h4 {{ d.heading }}
      p {{ d.subheading }}

      .xbuttons
        button.button(
          v-for="option in d.options"
          :key="`${option.title}/${option.value}`"
          :class="{'is-link': activeButtons[d.heading] === option.value }"
          @click="clickedCollectionButton(d.heading, option.value)"
        ) {{ option.title }}

  //- .filtered-folder-buttons
  //-   h3 Runs matching filters
  //-   .filter-folder(v-for="run in filteredFolders" :key="run.folder")
  //-     button.button.is-small(
  //-       :class="{'is-link': run.folder === selectedRun }"
  //-       @click="selectRun(run.folder)"
  //-     ) {{ run.folder }}

</template>

<script lang="ts">
import { Vue, Component, Watch, Prop } from 'vue-property-decorator'
import Papaparse from 'papaparse'
import yaml from 'yaml'

import HTTPFileSystem from '@/util/HTTPFileSystem'

interface RunFinder {
  collection?: string
  notes?: string
  dimensions: {
    heading: string
    subheading?: string
    options: { title: string; value: string; image?: string }[]
  }[]
}

@Component
export default class VueComponent extends Vue {
  @Prop({ required: true }) private svnRoot!: HTTPFileSystem
  @Prop({ required: true }) private subfolder!: string
  @Prop({ required: true }) private selectedRun!: string

  private activeButtons: { [heading: string]: string } = {}

  private filteredFolders: string[] = []
  private allRunsInLogFile: any[] = []

  private runFinder: RunFinder = { dimensions: [] }

  private async mounted() {
    await this.buildRunFinder()
    await this.loadRunLog()
    this.filterFolders()
  }

  private filterFolders() {
    // console.log('hiii')
    const activeKeys = Object.keys(this.activeButtons).filter(b => {
      return this.activeButtons[b].length
    })
    // console.log(activeKeys)
    if (!activeKeys.length) {
      this.filteredFolders = this.allRunsInLogFile
    } else {
      // console.log('gonna do the thing')
      // for each activated button, filter out any runs that
      // either don't have that button or that have a different setting for it
      const matches: any = []
      this.allRunsInLogFile.forEach(run => {
        let itMatchesEverything = true
        for (const key of activeKeys) {
          // console.log(this.activeButtons[key])
          if (!run[key] || run[key] !== this.activeButtons[key]) {
            itMatchesEverything = false
          }
        }
        // the button matched!
        if (itMatchesEverything) matches.push(run)
      })
      this.filteredFolders = matches
    }

    this.$emit('filteredFolders', this.filteredFolders)
  }

  private get dimensions() {
    return this.runFinder.dimensions
  }

  private async loadRunLog() {
    if (!this.svnRoot) return

    const csvFile = 'run-log.csv'
    const rawCSV = await this.svnRoot.getFileText(this.subfolder + '/' + csvFile)

    const runLog = Papaparse.parse(rawCSV, {
      header: true,
      dynamicTyping: true,
      skipEmptyLines: true,
    })

    this.allRunsInLogFile = runLog.data
    // this.allRunsMetaData = runLog.meta
  }

  private async buildRunFinder() {
    if (!this.svnRoot) return

    const runLookupFile = 'run-lookup.yaml'
    const runYaml = yaml.parse(await this.svnRoot.getFileText(this.subfolder + '/' + runLookupFile))

    this.runFinder = runYaml
    this.buildRunSelectionButtons()
    // this.buildRunIdFromButtonSelections()
  }

  private buildRunSelectionButtons() {
    for (const dimension of this.runFinder.dimensions) {
      Vue.set(this.activeButtons, dimension.heading, '') // dimension.options[0].value)
    }
  }

  private clickedCollectionButton(heading: string, option: string) {
    // console.log(heading, option)
    if (option === this.activeButtons[heading]) {
      this.activeButtons[heading] = ''
    } else {
      this.activeButtons[heading] = option
    }

    this.filterFolders()
  }
}
</script>

<style scoped lang="scss">
@import '@/styles.scss';

.folder-browser {
  background-color: var(--bgBold);
}

.vessel {
  margin: 0 auto;
  padding: 0rem 3rem 1rem 3rem;
  max-width: $sizeVessel;
}

.white {
  background-color: var(--bgBold);
}

.cream {
  background-color: var(--bgCream);
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
    /* Required for text-overflow to do anything */
    // text-overflow: ellipsis;
    // white-space: nowrap;
    // overflow: hidden;
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
  margin-bottom: 1rem;
  padding: 2rem 0 0 0;
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
  padding: 0rem 0rem;
  margin: 0rem 0rem;
}

.readme-header {
  font-size: 1.1rem;
  padding-bottom: 1rem;
}

h3.curate-heading {
  font-size: 1.8rem;
  font-weight: bold;
  color: var(--textFancy);
  padding-top: 0.5rem;
  margin-top: 0rem;
}

.curate-content {
  padding: 1rem 0rem;
  margin: 0rem 0rem;
}

.dimensions {
  margin: 2rem 0 1rem 0;
  display: flex;
  flex-direction: column;
}

.dimension {
  margin-bottom: 1rem;
  // border: var(--borderThin);
  // padding: 0.5rem 0.5rem 1rem 0.5rem;
  // margin: 0.5rem 0.25rem;
}

.dimension h4 {
  margin: 0.5rem 0 0 0;
  line-height: 1.2rem;
  flex: 1;
  font-size: 1.1rem;
  font-weight: bold;
  color: var(--textFancy);
}

.dimension p {
  font-size: 0.8rem;
  margin-bottom: 0.5rem;
}

.dimension .button {
  margin: 0.1rem 0;
  font-size: 0.8rem;
}

.xbuttons {
  align-items: unset;
  display: flex;
  flex-direction: column;
  margin-right: auto;
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
