const config: any = {
  projects: [
    {
      name: 'PAVE',
      url: 'pave',
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
      thumbnail: '/thumb-localfiles.png',
    },
  ],
}

export default config
