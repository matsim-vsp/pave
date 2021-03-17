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
      name: '1. Robotaxi',
      url: '1-robotaxi',
      description: 'Automated, networked taxi service',
      svn:
        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website/1-robotaxi',
      need_password: false,
      thumbnail:
        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website/images/vehicles/' +
        'Robotaxi_Taxi.jpg',
    },
    {
      name: '2. Pooling',
      url: '2-pooling',
      description: 'Shared taxi service',
      // svn: 'http://localhost:8000/2-pooling',
      svn:
        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website/2-pooling',
      need_password: false,
      thumbnail:
        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website/images/vehicles/' +
        'RoboShuttle.jpg',
    },
    {
      name: '3. Combined',
      url: '3-combined',
      description: 'Automated, networked taxi service with shared pooling',
      svn:
        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website/3-combined',
      need_password: false,
      thumbnail:
        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website/images/vehicles/' +
        'Taxi_Und_Shuttle.jpg',
    },
    {
      name: '4. Ban Cars',
      url: '4-ban-cars',
      description: 'No private cars inside the service area',
      svn:
        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website/4-ban-cars',
      need_password: false,
      thumbnail:
        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website/images/vehicles/' +
        'Taxi_Und_Shuttle.jpg',
    },
    // {
    //   name: 'local',
    //   url: 'local',
    //   description: 'local',
    //   svn: 'http:localhost:8000/pave/2-pooling',
    //   need_password: false,
    //   thumbnail: 'thumb-robots.jpg',
    // },
  ],
}

export default config
