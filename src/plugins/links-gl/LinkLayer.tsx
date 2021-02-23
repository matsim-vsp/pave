import React, { useState, useMemo, useEffect } from 'react'
import { StaticMap } from 'react-map-gl'
import DeckGL from '@deck.gl/react'
import { render } from 'react-dom'
import { GeoJsonLayer } from '@deck.gl/layers'
import { scaleLinear, scaleThreshold } from 'd3-scale'

import { MAP_STYLES } from '@/Globals'

const MAPBOX_TOKEN =
  'pk.eyJ1IjoidnNwLXR1LWJlcmxpbiIsImEiOiJjamNpemh1bmEzNmF0MndudHI5aGFmeXpoIn0.u9f04rjFo7ZbWiSceTTXyA'

const colorRange = [
  [1, 152, 189],
  [73, 227, 206],
  [216, 254, 181],
  [254, 237, 177],
  [254, 173, 84],
  [209, 55, 78],
]

const COLOR_SCALE = scaleThreshold()
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
  ] as any)

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

export default function Component({
  networkUrl = '',
  csvData = {} as { [id: string]: number[] },
  csvColumn = -1,
  colTitle = '',
  center = [],
  dark = false,
}) {
  const [hoverInfo, setHoverInfo] = useState({})

  const [lon, lat] = center
  const initialView = Object.assign(INITIAL_VIEW_STATE, { longitude: lon, latitude: lat })

  const getLineColor = (feature: any) => {
    const id = feature.properties.id
    const row = csvData[id]

    if (!row) return [212, 212, 192]
    return COLOR_SCALE(row[csvColumn])
  }

  const getLineWidth = (feature: any) => {
    return 40
  }

  function handleClick() {
    console.log('click!')
  }

  function renderTooltip({ hoverInfo }: any) {
    const { object, x, y } = hoverInfo
    if (!object) return null

    const id = object.properties?.id
    const row = csvData[id]
    if (!row) return null

    const value: any = row[csvColumn]
    if (value === undefined) return null

    return (
      <div
        className="tooltip"
        style={{
          backgroundColor: dark ? '#445' : 'white',
          color: dark ? 'white' : '#222',
          padding: '1rem 1rem',
          position: 'absolute',
          left: x + 4,
          top: y - 80,
          boxShadow: '0px 2px 10px #22222266',
        }}
      >
        <big>
          <b>{colTitle}</b>
        </big>
        <p>{value}</p>
      </div>
    )
  }

  const layers = [
    new GeoJsonLayer({
      id: 'linkGeoJson',
      data: networkUrl,
      filled: false,
      lineWidthMinPixels: 0.5,
      pickable: true,
      stroked: false,
      autoHighlight: true,
      highlightColor: [255, 255, 255], // [64, 255, 64],
      parameters: {
        depthTest: false,
      },

      getLineColor,
      getLineWidth,
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
      initialViewState={initialView}
      controller={true}
      pickingRadius={5}
      getCursor={() => 'pointer'}
      onClick={handleClick}
    >
      {
        /*
        // @ts-ignore */
        <StaticMap
          reuseMaps
          mapStyle={dark ? MAP_STYLES.dark : MAP_STYLES.light}
          preventStyleDiffing={true}
          mapboxApiAccessToken={MAPBOX_TOKEN}
        />
      }
      {renderTooltip({ hoverInfo })}
    </DeckGL>
  )
}
