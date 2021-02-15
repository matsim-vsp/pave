const config: any = {
  projects: [
    //    {
    //      name: 'Sample Visualizations',
    //      url: 'example',
    //      description: 'A few visualizations of public datasets.',
    //      svn:
    //        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/viz-examples/examples',
    //      need_password: false,
    //      thumbnail: '/thumb-examples.jpg',
    //    },
    //    {
    //      name: 'Gladbeck',
    //      url: 'gladbeck',
    //      description: 'AVÖV Projekt: Gladbeck NRW',
    //      svn:
    //        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/gladbeck/avoev',
    //      need_password: false,
    //      thumbnail: '/thumb-gladbeck.jpg',
    //    },
    {
      name: 'PAVE',
      url: 'runs',
      description: 'Simulation results',
      svn:
        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website',
      need_password: false,
      thumbnail: '/thumb-chart.jpg',
    },
    {
      name: 'Local Files',
      url: 'local',
      description: 'Run scripts/serve.py to browse local files on your PC',
      svn: 'http://localhost:8000/data',
      need_password: false,
      thumbnail: '/thumb-localfiles.jpg',
    },
  ],
}

export default config
