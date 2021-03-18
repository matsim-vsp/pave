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
      name_de: '1. Robotaxi',
      url: '1-robotaxi',
      description: 'Automated taxi service',
      description_de: 'Fahrerloser Taxi-Service',
      svn:
        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website/1-robotaxi',
      need_password: false,
      thumbnail:
        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website/images/vehicles/' +
        'Robotaxi_Taxi.jpg',
    },
    {
      name: '2. Pooling',
      name_de: '2. Pooling',
      url: '2-pooling',
      description: 'Automated, pooled shuttle service',
      description_de: 'Fahrerloser Shuttle-Service',
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
      name_de: '3. Kombiniert',
      url: '3-combined',
      description: 'Both service forms available',
      description_de: 'Beide Bedienformen verfügbar',
      svn:
        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website/3-combined',
      need_password: false,
      thumbnail:
        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website/images/vehicles/' +
        'Taxi_Und_Shuttle.jpg',
    },
    {
      name: '4. Ban Cars',
      name_de: '4. PKW-Verbot',
      url: '4-ban-cars',
      description: 'No private cars inside the service area',
      description_de: 'Keine privaten Autos im Bediengebiet',
      svn:
        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website/4-ban-cars',
      need_password: false,
      thumbnail:
        'https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/website/images/vehicles/' +
        'ban-cars.jpg',
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
