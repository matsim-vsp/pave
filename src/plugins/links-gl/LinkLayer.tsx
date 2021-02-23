import React, { useState, useMemo, useEffect } from 'react'
import { InteractiveMap } from 'react-map-gl'
import { AmbientLight, PointLight, LightingEffect } from '@deck.gl/core'
import DeckGL from '@deck.gl/react'
import { render } from 'react-dom'
import { GeoJsonLayer } from '@deck.gl/layers'
import { scaleLinear, scaleThreshold } from 'd3-scale'

const MAPBOX_TOKEN =
  'pk.eyJ1IjoidnNwLXR1LWJlcmxpbiIsImEiOiJjamNpemh1bmEzNmF0MndudHI5aGFmeXpoIn0.u9f04rjFo7ZbWiSceTTXyA'

const MAP_STYLE = 'mapbox://styles/vsp-tu-berlin/ckeetelh218ef19ob5nzw5vbh'
// dark 'mapbox://styles/vsp-tu-berlin/ckek59op0011219pbwfar1rex'
// light 'mapbox://styles/vsp-tu-berlin/ckeetelh218ef19ob5nzw5vbh'
// 'mapbox://styles/mapbox/light-v9', // 'mapbox://styles/mapbox/dark-v9'

export const colorRange = [
  [1, 152, 189],
  [73, 227, 206],
  [216, 254, 181],
  [254, 237, 177],
  [254, 173, 84],
  [209, 55, 78],
]

export const COLOR_SCALE = scaleThreshold()
  .domain([0, 1, 2, 4, 7, 15, 30, 100, 200, 500, 1000])
  // .domain([0, 4, 8, 12, 20, 32, 52, 84, 136, 220])
  .range([
    [26, 152, 80],
    [102, 189, 99],
    [166, 217, 106],
    [217, 239, 139],
    [255, 255, 191],
    [254, 224, 139],
    [253, 174, 97],
    [244, 109, 67],
    [215, 48, 39],
    [168, 0, 0],
  ])

const WIDTH_SCALE = scaleLinear()
  .clamp(true)
  .domain([0, 200])
  .range([10, 2000])

const INITIAL_VIEW_STATE = {
  latitude: 38,
  longitude: -100,
  zoom: 10,
  // minZoom: 2,
  // maxZoom: 8,
}

// function aggregateAccidents(accidents) {
//   const incidents = {}
//   const fatalities = {}

//   if (accidents) {
//     accidents.forEach(a => {
//       const r = (incidents[a.year] = incidents[a.year] || {})
//       const f = (fatalities[a.year] = fatalities[a.year] || {})
//       const key = getKey(a)
//       r[key] = a.incidents
//       f[key] = a.fatalities
//     })
//   }
//   return { incidents, fatalities }
// }

function renderTooltip({ object }: any) {
  if (!object || !object.position || !object.position.length) {
    return null
  }

  const lat = object.position[1]
  const lng = object.position[0]
  const count = object.points.length

  return {
    text: `\
    ${count} Pickups
    latitude: ${Number.isFinite(lat) ? lat.toFixed(6) : ''}
    longitude: ${Number.isFinite(lng) ? lng.toFixed(6) : ''}
    `,
  }
}

// function renderTooltip({ fatalities, incidents, year, hoverInfo }) {
//   const { object, x, y } = hoverInfo

//   if (!object) {
//     return null
//   }

//   const props = object.properties
//   const key = getKey(props)
//   const f = fatalities[year][key]
//   const r = incidents[year][key]

//   const content = r ? (
//     <div>
//       <b>{f}</b> people died in <b>{r}</b> crashes on{' '}
//       {props.type === 'SR' ? props.state : props.type}-{props.id} in <b>{year}</b>
//     </div>
//   ) : (
//     <div>
//       no accidents recorded in <b>{year}</b>
//     </div>
//   )

//   return (
//     <div className="tooltip" style={{ left: x, top: y }}>
//       <big>
//         {props.name} ({props.state})
//       </big>
//       {content}
//     </div>
//   )
// }

export default function App({
  mapStyle = MAP_STYLE,
  networkUrl = '',
  csvData = {} as { [id: string]: number[] },
  csvColumn = -1,
  center = [],
}) {
  const [hoverInfo, setHoverInfo] = useState({})

  const [lon, lat] = center
  const initialView = Object.assign(INITIAL_VIEW_STATE, { longitude: lon, latitude: lat })

  const getLineColor = (feature: any) => {
    const id = feature.properties.id
    const row = csvData[id]
    // console.log(row)
    if (!row) return [212, 212, 192]
    return COLOR_SCALE(row[csvColumn])
  }

  const getLineWidth = (feature: any) => {
    return 50
    // if (!incidents[year]) {
    //   return 10
    // }
    // const key = getKey(f.properties)
    // const incidentsPer1KMile = ((incidents[year][key] || 0) / f.properties.length) * 1000
    // return WIDTH_SCALE(incidentsPer1KMile)
  }

  const layers = [
    new GeoJsonLayer({
      id: 'linkGeoJson',
      data: networkUrl,
      stroked: false,
      filled: false,
      lineWidthMinPixels: 0.5,
      parameters: {
        depthTest: false,
      },

      getLineColor,
      getLineWidth,
      pickable: true,
      onHover: setHoverInfo,

      updateTriggers: {
        getLineColor: { csvColumn },
        // getLineWidth: { csvColumn },
      },

      transitions: {
        getLineColor: 500,
        getLineWidth: 500,
      },
    }),
  ]

  return (
    /*
    //@ts-ignore */
    <DeckGL
      layers={layers}
      effects={[lightingEffect]}
      initialViewState={initialView}
      controller={true}
      getTooltip={renderTooltip}
    >
      {
        /*
        // @ts-ignore */
        <InteractiveMap
          reuseMaps
          mapStyle={mapStyle}
          preventStyleDiffing={true}
          mapboxApiAccessToken={MAPBOX_TOKEN}
        />
      }
      {renderTooltip({ year: 2015, hoverInfo })}
    </DeckGL>
  )
}

const ambientLight = new AmbientLight({
  color: [255, 255, 255],
  intensity: 1.0,
})

const pointLight1 = new PointLight({
  color: [255, 255, 255],
  intensity: 0.8,
  position: [-0.144528, 49.739968, 80000],
})

const pointLight2 = new PointLight({
  color: [255, 255, 255],
  intensity: 0.8,
  position: [-3.807751, 54.104682, 8000],
})

const lightingEffect = new LightingEffect({ ambientLight, pointLight1, pointLight2 })

const material = {
  ambient: 0.64,
  diffuse: 0.6,
  shininess: 32,
  specularColor: [51, 51, 51],
}
