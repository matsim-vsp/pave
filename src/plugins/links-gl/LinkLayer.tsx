import React, { useState, useMemo, useEffect } from 'react'
import DeckGL from '@deck.gl/react'
import { GeoJsonLayer } from '@deck.gl/layers'
import { scaleLinear, scaleThreshold } from 'd3-scale'
import { StaticMap } from 'react-map-gl'
import colormap from 'colormap'

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

const SCALED_COLORS = scaleThreshold()
  .domain([0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9])
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
  data = {} as {
    rows: { [id: string]: number[] }
    header: string[]
    headerMax: number[]
    activeColumn: number
  },
  center = [],
  dark = false,
  maxWidth = '',
  colors = 'viridis',
}) {
  const { rows, header, headerMax, activeColumn } = data
  const [lon, lat] = center

  const initialView = Object.assign(INITIAL_VIEW_STATE, { longitude: lon, latitude: lat })
  const [hoverInfo, setHoverInfo] = useState({})
  const maxWidthValue = parseFloat(maxWidth)

  // const [colorInfo, setColorInfo] = useState(() => createColorRamp(colors))

  const builtColors = colormap({
    colormap: colors,
    nshades: 20,
    format: 'rba',
  }).map((a: number[]) => [a.slice(0, 3)])

  const fetchColor = scaleThreshold()
    .domain(new Array(20).fill(0).map((v, i) => 0.05 * i))
    .range(builtColors)
  // .range(dark ? builtColors : builtColors.reverse())

  // this assumes that zero means hide the link. This may not be generic enough
  const colorPaleGrey = dark ? [80, 80, 80, 40] : [212, 212, 212, 40]
  const colorInvisible = [0, 0, 0, 0]

  const getLineColor = (feature: any) => {
    const id = feature.properties.id
    const row = rows[id]

    if (!row) return colorInvisible

    const value = row[activeColumn]
    if (!value) return colorInvisible

    // const scaledValue = value / headerMax[activeColumn]
    const scaledValue = Math.log(value) / Math.log(headerMax[activeColumn])
    if (scaledValue < 0.0001) return colorPaleGrey

    return fetchColor(scaledValue)
  }

  // Line Widths:
  // --> 2 pixels if no line width at all
  // --> Scaled up to 50 pixels, scaled vs. maxWidth
  const getLineWidth = (feature: any) => {
    if (!maxWidthValue) return 0

    const id = feature.properties.id
    const row = rows[id]

    if (!row) return 0

    const value = row[activeColumn]

    const scaledValue = (50.0 * value) / maxWidthValue
    return Math.min(scaledValue, 100)
  }

  function handleClick() {
    console.log('click!')
  }

  function renderTooltip({ hoverInfo }: any) {
    const { object, x, y } = hoverInfo
    if (!object) return null

    const id = object.properties?.id
    const row = rows[id]
    if (!row) return null

    const value: any = row[activeColumn]
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
          <b>{header[activeColumn]}</b>
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
      lineWidthUnits: 'pixels',
      lineWidthMinPixels: 2,
      pickable: true,
      stroked: false,
      opacity: 0.7,
      autoHighlight: true,
      highlightColor: [255, 128, 255], // [64, 255, 64],
      parameters: {
        depthTest: false,
      },

      getLineColor,
      getLineWidth,
      onHover: setHoverInfo,

      updateTriggers: {
        getLineColor: { dark, colors, activeColumn },
        getLineWidth: { maxWidth, activeColumn },
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
