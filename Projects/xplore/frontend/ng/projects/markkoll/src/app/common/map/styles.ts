import { asArray } from "ol/color";
import { Coordinate } from "ol/coordinate";
import { LineString, MultiLineString, Point } from "ol/geom";
import Feature from "ol/render/Feature";
import { Circle, Fill, Icon, Stroke, Style } from "ol/style";
import { Avtalsstatus, Avtalstyp, Intrangstyp } from "../../../../../../generated/markkoll-api";

const intrangColor = "#8D08EF";
const intrangSelectedBackgroundColor = "black";

const outreddColor = "#A5A7AC";
const avtalSigneratColor = "#18BC11";
const avtalSkickatColor = "#EDC903";
const avtalJusteratColor = "#EDC903";
const avtalKonfliktColor = "#F45959";
const ejBehandladColor = "#0B87BE";

// Creates a style with an outline of the specified color
// and a transparent fill of the same color.
function makeFastighetStyle(color: string) {
  const strokeColor = asArray(color);
  const fillColor = strokeColor.slice();
  fillColor[3] = 0.1;

  return new Style({
    fill: new Fill({
      color: fillColor
    }),
    stroke: new Stroke({
      color: strokeColor,
      width: 4,
    })
  });
}

const outreddStyle = makeFastighetStyle(outreddColor);
const avtalSigneratStyle = makeFastighetStyle(avtalSigneratColor);
const avtalSkickatStyle = makeFastighetStyle(avtalSkickatColor);
const avtalJusteratStyle = makeFastighetStyle(avtalJusteratColor);
const avtalKonfliktStyle = makeFastighetStyle(avtalKonfliktColor);
const ejBehandlatStyle = makeFastighetStyle(ejBehandladColor);

const defaultStyle = new Style({
  stroke: new Stroke({
    color: "#222222",
    width: 4,
  })
});

export function selectedFastighetStyle(feature: Feature) {
  const outredd = feature.get("outredd");
  const status = feature.get("status");

  if (outredd === 1) { return outreddStyle; }
  if (status === Avtalsstatus.AVTALSIGNERAT) { return avtalSigneratStyle; }
  if (status === Avtalsstatus.PAMINNELSESKICKAD) { return avtalSkickatStyle; }
  if (status === Avtalsstatus.ERSATTNINGUTBETALAS) { return avtalSigneratStyle; }
  if (status === Avtalsstatus.ERSATTNINGUTBETALD) { return avtalSigneratStyle; }
  if (status === Avtalsstatus.AVTALSKICKAT) { return avtalSkickatStyle; }
  if (status === Avtalsstatus.AVTALJUSTERAS) { return avtalJusteratStyle; }
  if (status === Avtalsstatus.AVTALSKONFLIKT) { return avtalKonfliktStyle; }
  if (status === Avtalsstatus.EJBEHANDLAT) { return ejBehandlatStyle; }
  return defaultStyle;
}

export function hanteraIntrangStyle(feature: Feature): Style | Style[] {
  const selected: boolean = feature.get("map_selected") ?? false;
  const intrangstyp: Intrangstyp = feature.get("intrangstyp") ?? "unknown";
  const avtalsTyp: Avtalstyp = feature.get("avtalstyp") ?? "unknown";
  const cutSelected1: boolean = feature.get("cut_selected_1") ?? false;
  const cutSelected2: boolean = feature.get("cut_selected_2") ?? false;

  if (feature.getGeometry().getType() === "Point") {
    switch (intrangstyp) {
      case Intrangstyp.MARKSKAP:
      case Intrangstyp.KABELSKAP:
        return iconStyle(selected ? "../../../assets/skap_selected.svg" : "../../../assets/intrang-skap.svg");
      case Intrangstyp.NATSTATION:
      case Intrangstyp.TEKNIKBOD:
        return iconStyle(selected ? "../../../assets/byggnad_selected.svg" : "../../../assets/intrang-byggnad.svg");
      case Intrangstyp.BRUNN:
        return iconStyle(selected ? "../../../assets/brunn_selected.svg" : "../../../assets/intrang-brunn.svg");
      case Intrangstyp.OKAND:
        return selected ?
          new Style({
            image: new Circle({
              fill: new Fill({
                color: intrangColor
              }),
              stroke: new Stroke({
                color: "black",
                width: 2
              }),
              radius: 14,
            })
          })
          : new Style({
            image: new Circle({
              fill: new Fill({
                color: "blue"
              }),
              radius: 14,
            })
          });
    }
  } else if (feature.getGeometry().getType() === "LineString" ||
             feature.getGeometry().getType() === "MultiLineString") {

    let styles = [];
    const geometry = feature.getGeometry();
    if (cutSelected1) {
      styles.push(
        new Style({
          stroke: new Stroke({
            color: "#02BAAC",
            lineCap: "butt",
            width: 3,
          }),
          zIndex: 600
        }),
        new Style({
          stroke: new Stroke({
            color: "black",
            lineCap: "butt",
            width: 8,
          }),
          zIndex: 500
        })
      )
    }
    if (cutSelected2) {
      styles.push(
        new Style({
          stroke: new Stroke({
            color: "#D8D8D8",
            lineCap: "butt",
            width: 3,
          }),
          zIndex: 600
        }),
        new Style({
          stroke: new Stroke({
            color: "black",
            lineCap: "butt",
            width: 8,
          }),
          zIndex: 500
        })
      )
    }
    if (geometry instanceof LineString) {
      const ls = geometry as LineString;

      ls.forEachSegment((start, end) => {
        const rotation = lineRotation(start, end);
        if (coordinatesEquals(end, ls.getLastCoordinate())) {
          styles.push(endpointStyle(end, -rotation));
        }
        if (coordinatesEquals(start, ls.getFirstCoordinate())) {
          styles.push(endpointStyle(start, -rotation));
        }
      })
    }
    styles = styles.concat(lineStyle(feature, avtalsTyp, selected));
    return styles;
  }

  switch (avtalsTyp) {
    case Avtalstyp.REV:
    case Avtalstyp.ABEL07:
      return selected ? revSelectedStyle : revNotSelectedStyle;

    case Avtalstyp.SERVIS:
      return selected ? servisSelectedStyle : servisNotSelectedStyle;

    case Avtalstyp.INTRANG:
      return selected ? intrangSelectedStyle : intrangNotSelectedStyle;

    default:
      throw new Error("Okänd avtalstyp: " + avtalsTyp);
  }
}

export const buffertStyle = new Style({
  fill: new Fill({
    color: "rgba(248, 35, 150, 0.2)",
  })
});

export const intrangNotSelectedStyle = [
  new Style({
    stroke: new Stroke({
      color: "rgba(255, 255, 255)",
      width: 8,
    }),
    zIndex: 100
  }),
  new Style({
    stroke: new Stroke({
      color: intrangColor,
      width: 5
    }),
    zIndex: 200
  })
];

export const intrangSelectedStyle = [
  new Style({
    stroke: new Stroke({
      color: intrangSelectedBackgroundColor,
      width: 8,
    }),
    zIndex: 100
  }),
  new Style({
    stroke: new Stroke({
      color: intrangColor,
      width: 5
    }),
    zIndex: 200
  })
];

export const revNotSelectedStyle = [
  new Style({
    stroke: new Stroke({
      color: "white",
      width: 8,
    }),
    zIndex: 100
  }),
  new Style({
    stroke: new Stroke({
      color: intrangColor,
      lineDash: [0.1, 7],
      lineCap: "round",
      width: 5
    }),
    zIndex: 200
  })
];

export const revSelectedStyle = [
  new Style({
    stroke: new Stroke({
      color: intrangSelectedBackgroundColor,
      width: 8
    }),
    zIndex: 100
  }),
  new Style({
    stroke: new Stroke({
      color: intrangColor,
      lineDash: [0.1, 7],
      lineCap: "round",
      width: 5
    }),
    zIndex: 200
  })
];

export const servisNotSelectedStyle = [
  new Style({
    stroke: new Stroke({
      color: "white",
      lineCap: "butt",
      width: 8,
    }),
    zIndex: 100
  }),
  new Style({
    stroke: new Stroke({
      color: intrangColor,
      lineDash: [8, 4],
      lineCap: "butt",
      width: 5
    }),
    zIndex: 200
  })
];

export const servisSelectedStyle = [
  new Style({
    stroke: new Stroke({
      color: intrangSelectedBackgroundColor,
      lineCap: "butt",
      width: 8,
    }),
    zIndex: 100
  }),
  new Style({
    stroke: new Stroke({
      color: intrangColor,
      lineDash: [8, 4],
      lineCap: "butt",
      width: 5
    }),
    zIndex: 200
  })
];

export function iconStyle(imagePath: string) {
  return new Style({
    image: new Icon({
      src: imagePath,
      scale: 0.7,
    }),
    zIndex: 300
  });
}

function endpointStyle(coord: Coordinate, rotation: number): Style {
  return new Style({
    zIndex: 300,
    geometry: new Point(coord),
    image: new Icon({
      src: '../../../assets/lila_tipp.png',
      scale: 1.5,
      anchor: [0.5, 0.5],
      rotateWithView: true,
      rotation: rotation,
    }),
  })
}

function lineStyle(feature: Feature, avtalsTyp: Avtalstyp, selected: boolean): Style[] {
  switch (avtalsTyp) {
    case Avtalstyp.REV:
    case Avtalstyp.ABEL07:
      return selected ? revSelectedStyle : revNotSelectedStyle;

    case Avtalstyp.SERVIS:
      return selected ? servisSelectedStyle : servisNotSelectedStyle;

    case Avtalstyp.INTRANG:
      return selected ? intrangSelectedStyle : intrangNotSelectedStyle;

    default:
      throw new Error("Okänd avtalstyp: " + avtalsTyp);
  }
}

function coordinatesEquals(c1: Coordinate, c2: Coordinate) {
  return (c1[0] == c2[0]) && (c1[1] == c2[1]);
}

function lineRotation(c1: Coordinate, c2: Coordinate) {
  const dx = c2[0] - c1[0];
  const dy = c2[1] - c1[1];
  return Math.atan2(dy, dx);
}
