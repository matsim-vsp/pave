<i18n>
en:
  head1: 'PAVE: Potential of automated vehicle systems'
  desc1: 'Automated and connected driving is expected to have considerable potential for road traffic, especially with regard to increasing traffic safety, reducing consumption and emissions, improving traffic efficiency in the medium term, and sustainably strengthening Germany as a location for business and innovation.'
  head2: 'The concept of Mobility-on-Demand'
  desc2: 'The short video and interactive animation below illustrate what PAVE is all about.'
  head3: 'Explore model results'
  desc3: 'Click on one of the service scenarios below to explore the technical outputs:'
  more-info: 'For more information:'
  video: 'Video: What is PAVE?'
  model: 'Interactive model simulation'
  projMainPage: 'Project main page'
  gdpr: 'No personal data stored or collected'
  matsim: 'All simulations conducted using the MATSim simulation framework:'
de:
  head1: 'PAVE: Potentiale automatisierter Verkehrssysteme'
  desc1: 'Durch „Automatisiertes und vernetztes Fahren“ wird ein erhebliches Potential für den Straßenverkehr erwartet, insbesondere hinsichtlich „der Steigerung der Verkehrssicherheit, der Verbrauchs- und Emissionsreduzierung, einer mittelfristigen Verbesserung der Verkehrseffizienz und der nachhaltigen Stärkung des Wirtschafts- und Innovationsstandorts Deutschland“.'
  head2: 'Das Konzept hinter Mobility-on-Demand'
  desc2: 'Hier finden Sie ein Video und eine interaktive Animation zu der Funktionsweise von automatisierten Mobilitätsdiensten'
  head3: 'Simulationsergebnisse'
  desc3: 'Klicken Sie auf eine der Schaltflächen um Ergebnisse des jeweiligen Szenarios zu entdecken:'
  video: 'Video: Was steckt hinter PAVE?'
  model: 'Interaktive Simulation'
  more-info: 'Weitere Informationen:'
  projMainPage: 'Projekt Webseite'
  gdpr: 'Keine Speicherung oder Sammlung von persönlichen Daten'
  matsim: 'Alle Simulationen wurden mit dem MATSim Framework durchgeführt'

</i18n>

<template lang="pug">
#home

  .banner
    h2 {{ $t('pave-project') }}
    h3 VSP / Technische Universität Berlin

  .page-area
    colophon.colophon

    .zcontent
      .main
        .right

        h2 {{ $t('head1') }}
        p {{ $t('desc1') }}

        h2 {{ $t('head2') }}
        p {{ $t('desc2') }}

        .big-thumbs
          .project
            iframe.yt-size(width="100%" src="https://www.youtube-nocookie.com/embed/lE9us7EoaLc" frameborder="2" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen)
            .desc
              p {{ $t('video') }}
          .project
            router-link(:to="'/v/vehicle-animation/1-robotaxi/viz-vehicles-1.yaml'")
              img.clip-it(src="/pave/thumb-drt.jpg")
            .desc
              p {{ $t('model') }}

        h2 {{ $t('head3') }}
        p {{ $t('desc3') }}

        svn-projects.gap

        h2 {{ $t('more-info') }}

        p.with-space {{ $t('projMainPage') }}
        a(href="https://pave-your-way.de" target="_blank") https://pave-your-way.de

        p.with-space {{ $t('matsim') }}
        a(href="https://matsim.org" target="_blank") https://matsim.org

</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from 'vue-property-decorator'
import MarkdownIt from 'markdown-it'

import Colophon from '@/components/Colophon.vue'
import VizCard from '@/components/VizCard.vue'
import SvnProjects from '@/components/SVNProjects.vue'

import globalStore from '@/store'

const SVN_ROOT =
  'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website'

@Component({
  components: { Colophon, SvnProjects, VizCard },
})
class MyComponent extends Vue {
  private state = globalStore.state
  private readme = ''
  private markdownParser = new MarkdownIt()

  private async mounted() {
    const crumbs: any[] = []
    globalStore.commit('setBreadCrumbs', crumbs)

    try {
      const readmeURL = `${SVN_ROOT}/readme.md`
      const readmeMd = await fetch(readmeURL).then(r => r.text())
      this.readme = this.markdownParser.render(readmeMd)
    } catch (e) {
      // no readme? oh well
    }
  }
}
export default MyComponent
</script>

<style scoped lang="scss">
@import '@/styles.scss';

.gap {
  margin-top: 2rem;
  margin-bottom: 2rem;
}

.zcontent {
  flex: 1;
  padding: 1rem 3rem 5rem 3rem;
  display: flex;
  width: 100%;
}

.main {
  margin: 0 auto;
}

.main h2 {
  padding-top: 2rem;
  margin-bottom: 1rem;
}

.big-thumbs {
  margin-top: 2rem;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
}

.clip-it {
  width: 100%;
  height: 15rem;
  object-fit: cover;
}

.yt-size {
  height: 15rem;
}

.project {
  flex: 1;
  cursor: pointer;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  background-color: var(--bgBold);
  border-radius: 16px;
}

.desc h3 {
  color: var(--text);
  margin: 0.5rem 0;
  // margin-bottom: 0.25rem;
}

.project .desc {
  padding: 0.5rem 1rem 1rem 1rem;
}

.project:hover {
  // background-color: var(--bgHover);
  box-shadow: var(--shadowMode);
}

.banner {
  grid-row: 1 / 2;
  grid-column: 1 / 2;
  display: flex;
  flex-direction: column;
  padding: 6rem 3rem 1rem 3rem;
  background-color: #181a1b;
  color: #f54f5f;
  background: url(../assets/images/banner.jpg);
  background-repeat: no-repeat;
  background-size: cover;
}

.banner h2 {
  margin-bottom: 0rem;
  font-size: 1.6rem;
  line-height: 1.7rem;
  background-color: #181a1b;
  margin-right: auto;
  padding: 0 0.5rem;
}

.banner h3 {
  font-size: 1.3rem;
  font-weight: normal;
  margin-bottom: 0;
  margin-right: auto;
  line-height: 1.4rem;
  padding: 0.25rem 0.5rem;
  background-color: #181a1b;
  // width: max-content;
}

a {
  font-size: 1.1rem;
  color: var(--link);
}

.readme {
  margin-top: 1rem;
  margin-bottom: 3rem;
  flex: 1;
  color: var(--text);
}

h2.readme {
  padding-top: 0;
}

.with-space {
  margin-top: 0.5rem;
}

.main h1 {
  margin-top: 1rem;
  font-weight: bold;
  font-size: 3rem;
  color: var(--text);
}

.main h2 {
  margin-top: 1rem;
  font-weight: normal;
  color: var(--textFancy);
}

.viz-cards {
  padding-bottom: 1rem;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(16rem, 1fr));
  gap: 2rem;
}

.one-viz {
  margin-bottom: 1rem;
}

.preamble {
  display: flex;
}

.top {
  margin-top: 1rem;
}

.colophon {
  grid-row: 1 / 3;
  grid-column: 2 / 3;

  padding: 2rem 2rem 1rem 5rem;
  text-align: right;
  font-size: 0.85rem;
  background-color: white;
}

.main {
  max-width: 64rem;
}

.main .top a {
  font-size: 0.9rem;
}

.page-area {
  grid-row: 2 / 3;
  grid-column: 1 / 2;
  display: flex;
  flex-direction: row-reverse;
}

.headline {
  font-size: 2rem;
  line-height: 2.7rem;
  padding: 1rem 0;
  color: $themeColor;
}

#app .footer {
  color: #222;
  background-color: white;
  text-align: center;
  padding: 2rem 0.5rem 3rem 0.5rem;
}

.footer a {
  color: $matsimBlue;
}

.footer img {
  margin: 1rem auto;
  padding: 0 1rem;
}

.tu-logo {
  margin-top: -4rem;
  text-align: right;
  margin-right: 2rem;
}

.img-logo {
  height: 8rem;
}

@media only screen and (max-width: 640px) {
  .banner {
    padding: 2rem 1rem;
  }

  .zcontent {
    padding: 2rem 1rem 8rem 1rem;
    flex-direction: column-reverse;
  }

  .colophon {
    display: none;
  }

  .headline {
    padding: 0rem 0rem 1rem 0rem;
    font-size: 1.5rem;
    line-height: 1.8rem;
  }

  .tu-logo {
    margin-top: -2rem;
    text-align: right;
    margin-right: 0.5rem;
  }

  .img-logo {
    height: 4rem;
  }
}
</style>
