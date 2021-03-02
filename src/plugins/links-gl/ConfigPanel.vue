<template lang="pug">
.config-panel

  //- time-of-day slider
  .panel-item(v-if="useSlider")
    p: b {{ $t('timeOfDay') }}

    button.button.full-width.is-warning.is-loading(v-if="activeColumn < 0"
          aria-haspopup="true" aria-controls="dropdown-menu-column-selector")

    time-slider.time-slider(v-else
      :useRange='false'
      :stops="csvData.header"
      @change='debounceTimeSlider')


  //- Column picker
  .panel-item(v-if="!useSlider")
    p: b {{ $t('selectColumn') }}

    .dropdown.full-width(:class="{'is-active': isButtonActive}")
      .dropdown-trigger
        button.full-width.is-warning.button(
          :class="{'is-loading': activeColumn < 0}"
          aria-haspopup="true" aria-controls="dropdown-menu-column-selector"
          @click="handleClickDropdown" @blur="clearDropdown"
        )

          b {{ buttonTitle }}
          span.icon.is-small
            i.fas.fa-angle-down(aria-hidden="true")

      #dropdown-menu-column-selector.dropdown-menu(role="menu" :style="{'max-height':'16rem', 'overflow-y': 'auto', 'border': '1px solid #ccc'}")
        .dropdown-content
          a.dropdown-item(v-for="column in header"
                          @click="handleSelectColumn(column)") {{ column }}

  //- BANDWIDTHS
  .panel-item
    p: b {{ $t('bandwidths') }}

    .options(style="display: flex; flex-direction:column;")
      input.input(v-model.lazy.number="scaleWidthValue")

  //- COLOR PICKER
  .panel-item(v-if="activeColumn > -1")
    p: b {{ $t('colors') }}
    .dropdown.full-width(:class="{'is-active': isColorButtonActive}")
      .dropdown-trigger
        img.color-button(:src="`/pave/colors/scale-${selectedColorRamp}.png`"
            :style="{'height': '2.3rem', 'width': '100%', 'border-radius': '5px'}"
            @click="() => this.isColorButtonActive = !this.isColorButtonActive"
        )

      #dropdown-menu-color-selector.dropdown-menu(role="menu")
        .dropdown-content(:style="{'padding':'0 0'}")
          a.dropdown-item(v-for="colorRamp in Object.keys(colorRamps)"
                          @click="handleColorRamp(colorRamp)"
                          :style="{'padding': '0.25rem 0.25rem'}")
            img(:src="`/pave/colors/scale-${colorRamp}.png`")
            p(:style="{'lineHeight': '1rem', 'marginBottom':'0.25rem'}") {{ colorRamp }}

</template>

<i18n>
en:
  selectColumn: "Select data column"
  loading: "Loading..."
  bandwidths: "Widths: 1 pixel ="
  timeOfDay: "Time of day"
  colors: "Colors"

de:
  selectColumn: "Datenspalte wählen"
  loading: "Laden..."
  bandwidths: "Linienbreiten: 1 pixel ="
  timeOfDay: "Uhrzeit"
  colors: "Farben"
</i18n>

<script lang="ts">
import { Vue, Component, Watch, Prop } from 'vue-property-decorator'
import { debounce } from 'debounce'

import TimeSlider from './TimeSlider.vue'

@Component({ components: { TimeSlider } })
export default class VueComponent extends Vue {
  // @Prop({ required: true })
  // private darkMode!: boolean

  @Prop({ required: true })
  private activeColumn!: number

  @Prop({ required: true })
  private header!: string[]

  @Prop({ required: true })
  private scaleWidth!: number

  @Prop({ required: true })
  private csvData!: { header: string[] }

  @Prop({ required: true })
  private useSlider!: boolean

  @Prop({ required: true })
  private selectedColorRamp!: string

  private isButtonActive = false
  private isColorButtonActive = false

  private scaleWidthValue = '1000'

  private colorRamps: { [title: string]: { png: string; diff?: boolean } } = {
    viridis: { png: 'scale-viridis.png' },
    inferno: { png: 'scale-inferno.png' },
    bluered: { png: 'scale-salinity.png', diff: true },
    picnic: { png: 'scale-picnic.png' },
  }

  private mounted() {
    this.scaleWidthValue = '' + this.scaleWidth
  }

  @Watch('scaleWidthValue') handleScaleChanged() {
    console.log(this.scaleWidthValue)
    if (isNaN(parseFloat(this.scaleWidthValue))) {
      return
    }
    this.$emit('scale', this.scaleWidthValue)
  }

  @Watch('scaleWidth') gotNewScale() {
    if (this.scaleWidth !== parseFloat(this.scaleWidthValue)) {
      this.scaleWidthValue = '' + this.scaleWidth
    }
  }

  private debounceTimeSlider = debounce(this.changedTimeSlider, 250)
  private changedTimeSlider(value: any) {
    console.log('new slider!', value)
    if (value.length && value.length === 1) value = value[0]

    this.$emit('slider', value)
  }

  private get buttonTitle() {
    if (this.activeColumn === -1) return this.$i18n.t('loading')
    return this.header[this.activeColumn]
  }

  private handleClickDropdown() {
    this.isButtonActive = !this.isButtonActive
  }

  private handleColorRamp(colors: string) {
    console.log(colors)
    this.isColorButtonActive = false
    this.$emit('colors', colors)
  }

  private clearDropdown() {
    console.log('boop')
    this.isButtonActive = false
  }

  private async handleSelectColumn(column: string) {
    this.isButtonActive = false
    await this.$nextTick()
    this.$emit('column', column)
  }
}
</script>

<style scoped lang="scss">
@import '@/styles.scss';

.config-panel {
  display: flex;
  flex-direction: column;
}

.full-width {
  display: block;
  width: 100%;
}

.panel-item {
  margin-bottom: 1rem;
}

p {
  font-size: 0.9rem;
}

.color-button:hover {
  cursor: pointer;
  box-shadow: 0px 0px 3px 3px rgba(128, 128, 128, 0.3);
}

button:hover {
  box-shadow: 0px 0px 3px 3px rgba(128, 128, 128, 0.3);
}

input {
  border: none;
  background-color: var(--bgCream2);
  color: var(--bgDark);
}

#dropdown-menu-color-selector {
  background-color: var(--bgBold);

  p {
    color: #888;
  }
}

@media only screen and (max-width: 640px) {
}
</style>
