<i18n>
en:
  paveproject: 'PAVE Project Team'
  contact: 'Contact'
  partners: 'Project Partners'
</i18n>

<template lang="pug">
#colophon-content
  .logo(style="width: 100%;")
    a(href='https://www.tu.berlin' target="_blank")
      img(src="@/assets/images/8-pave.png")

  .logo(style="width: 100%;")
    a(href='https://www.tu.berlin' target="_blank")
      img(src="@/assets/images/tu-logo.png")

  .logo(style="width: 100%;")
    a(href='https://www.vsp.tu-berlin.de' target="_blank")
      img(src="@/assets/images/vsp-logo-300dpi.png")

  .logo(style="width: 100%;")
    img(src="@/assets/images/2-univ-magdeburg.png")

  .logo(style="width: 100%;")
    img(src="@/assets/images/3-iav.png")

  .logo(style="width: 100%;")
    img(src="@/assets/images/4-bosch.png")

  .logo(style="width: 100%;")
    img(src="@/assets/images/5-carl-benz.png")

  .logo(style="width: 100%;")
    img(src="@/assets/images/6-ina-logo.png")

  a(href='https://www.bmvi.de' target="_blank")
    img(src="@/assets/images/logo-bmvi.png")

  //- .team PAVE Team, VSP
  //- .professor Kai Nagel, Professor

  //- p Tilmann Schlenther
  //- p Billy Charlton

  .team Contact

  p.nonbreak: a(href="mailto:sekretariat@vsp.tu-berlin.de") Email

  .team GDPR
  p No personal data collected.
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
  padding-top: 2rem;
  width: 16rem;
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

.colophon p {
  margin-bottom: 0px;
}

.colophon a {
  font-size: 0.85rem;
}

.partners {
  margin-bottom: 2rem;

  p {
    line-height: 1rem;
    padding-top: 0.75rem;
  }
}

.nonbreak {
  width: max-content;
  margin-left: auto;
}

.logo {
  margin-bottom: 2rem;
}

@media only screen and (max-width: 640px) {
}
</style>
