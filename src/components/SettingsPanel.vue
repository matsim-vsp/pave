<i18n>
en:
  show-hide: 'Show/Hide:'
  Vehicles: Vehicles
  Routes: Routes
  Requests: DRT Requests
de:
  show-hide: 'Ein-/Ausblenden'
  Vehicles: Fahrzeuge
  Routes: Routen
  Requests: DRT Anfragen
</i18n>

<template lang="pug">
.settings-panel-content
  h4 {{ $t('show-hide') }}

  .row(:key="label" v-for="label,i in Object.keys(items)")
    toggle-button.toggle(
      :width="40"
      :value="items[label]"
      :labels="false"
      :color="{checked: '#4b7cc4', unchecked: '#222'}"
      @change="$emit('click',i)")
    label {{ $t(label) }}

</template>

<script lang="ts">
import { Vue, Component, Watch, Prop } from 'vue-property-decorator'
import { ToggleButton } from 'vue-js-toggle-button'

@Component({
  components: {
    ToggleButton,
  },
})
export default class VueComponent extends Vue {
  @Prop({ required: true })
  private items!: { [label: string]: boolean }
}
</script>

<style scoped lang="scss">
.settings-panel-content {
  margin: 2rem 0.25rem 0 0.25rem;
}

h4 {
  font-weight: bold;
  font-size: 0.8rem;
  margin-bottom: 0.5rem;
  color: var(--textBold);
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
</style>
