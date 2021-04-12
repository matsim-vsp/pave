<i18n>
en:
  paveproject: 'PAVE Project Team'
  contact: 'Contact'
  partners: 'Project Partners'
  gdpr: 'No personal data stored or collected'
  imprint: 'Legal notice'
de:
  gdpr: 'Keine Speicherung oder Sammlung von persönlichen Daten'
  imprint: 'Impressum'

</i18n>

<template lang="pug">
#colophon-content
  .logo(style="width: 25%; margin-left: -1rem;")
    img(src="@/assets/images/8-pave.png")

  h4 Consortium

  .logos

    .logo
      img(src="@/assets/images/2-univ-magdeburg.png")

    .logo
      img(src="@/assets/images/3-iav.png")

    .logo
      img(src="@/assets/images/6-ina-logo.png")

    .logo
      img(src="@/assets/images/4-bosch.png")

  .logos

    .logo
      a(href='https://www.tu.berlin' target="_blank")
        img(src="@/assets/images/tu-logo.png")

    .logo
      a(href='https://www.vsp.tu-berlin.de' target="_blank")
        img(src="@/assets/images/vsp-logo-300dpi.png")

    .logo(style="width: 75%")
      img(src="@/assets/images/5-carl-benz.png")

    .bmvi
      a(href='https://www.bmvi.de' target="_blank")
        img(width="90%" src="@/assets/images/logo-bmvi.png")

  h4 Contact

  p.gdpr: a(href="mailto:sekretariat@vsp.tu-berlin.de") Email VSP TU Berlin

  p.gdpr GDPR: {{ $t('gdpr')}}

  p.gdpr: router-link(to="/imprint") {{ $t('imprint')}} <!--TODO: Change href link-->

</template>

<script lang="ts">
export default {
  name: 'Colophon',
  components: {},
}
</script>

<style scoped lang="scss">
@import '@/styles.scss';

#colophon-content {
  display: flex;
  flex-direction: column;
  padding: 0rem 0rem;
  font-size: 2rem;
  width: 100%;
  color: #335;
}

h4 {
  border-bottom: 1px solid #ccc;
}

.logos {
  margin: 0 0;
  display: grid;
  gap: 6rem;
  grid-template-columns: 1fr 1fr 1fr 1fr;
  flex-direction: row;
}

.logo {
  margin: auto auto;
  width: 100%;
}

.bmvi {
  margin: 0 0;
}

.colophon .professor {
  width: max-content;
  margin: 1rem 0 1rem auto;
  font-weight: bold;
  color: $bannerHighlight;
}

.colophon .team {
  margin-top: 1.5rem;
  margin-bottom: 0.25rem;
  font-weight: bold;
  color: $bannerHighlight;
}

.colophon a {
  color: #55a;
}

.nonbreak {
  width: max-content;
}

.logo {
  margin-bottom: 2rem;
}

.gdpr {
  margin-top: 1rem;
  font-size: 1.2rem;
}

@media only screen and (max-width: 640px) {
}
</style>
