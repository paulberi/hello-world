import Circle from "ol/style/Circle";
import {Style, Text} from "ol/style";
import Stroke from "ol/style/Stroke";
import Fill from "ol/style/Fill";
import Icon from "ol/style/Icon";
import {
  MatobjektMapinfo,
  TYP_GRUNDVATTENNIVA,
  TYP_INFILTRATION,
  TYP_RORELSE,
  TYP_TUNNELVATTEN,
  TYP_VATTENKEMI, TYP_YTVATTENMATNING
} from "../../services/matobjekt.service";
import {createHatchingPattern} from "../../../../../lib/map-core/style-utils";

export const defaultStyle = createIconStyle();

export const undreGrundvattenmagasinAktiv = createIconStyle("assets/featureIcon/grundvatten-undre-aktiv.svg");
export const undreGrundvattenmagasinInaktiv = createIconStyle("assets/featureIcon/grundvatten-undre-inaktiv.svg");
export const ovreGrundvattenmagasinAktiv = createIconStyle("assets/featureIcon/grundvatten-ovre-aktiv.svg");
export const ovreGrundvattenmagasinInaktiv = createIconStyle("assets/featureIcon/grundvatten-ovre-inaktiv.svg");
export const berggrundmagasinAktiv = createIconStyle("assets/featureIcon/grundvatten-berg-aktiv.svg");
export const berggrundmagasinInaktiv = createIconStyle("assets/featureIcon/grundvatten-berg-inaktiv.svg");
export const portryckAktiv = createIconStyle("assets/featureIcon/grundvatten-portryck-aktiv.svg");
export const portryckInaktiv = createIconStyle("assets/featureIcon/grundvatten-portryck-inaktiv.svg");
export const energibrunnAktiv = createIconStyle("assets/featureIcon/grundvatten-energi-aktiv.svg");
export const energibrunnInaktiv = createIconStyle("assets/featureIcon/grundvatten-energi-inaktiv.svg");

export const pumpgrop = createIconStyle("assets/featureIcon/flode-pumpgrop-aktiv.svg");
export const matdamm = createIconStyle("assets/featureIcon/flode-matdamm-aktiv.svg");
export const provpunkt = createIconStyle("assets/featureIcon/flode-provpunkt-aktiv.svg");

export const vattenkemi = createIconStyle("assets/featureIcon/vattenkemi-aktiv.svg");
export const infiltration = createIconStyle("assets/featureIcon/infiltration-aktiv.svg");
export const rorelseAktiv = createIconStyle("assets/featureIcon/rorelse-aktiv.svg");
export const rorelseInaktiv = createIconStyle("assets/featureIcon/rorelse-inaktiv.svg");

export const ytvattenAktiv = createIconStyle("assets/featureIcon/ytvattenmatning-aktiv.svg");

export const gruppStyleMap = new Map(
  [
    [1, {aktiv: undreGrundvattenmagasinAktiv, inaktiv: undreGrundvattenmagasinInaktiv}],
    [2, {aktiv: ovreGrundvattenmagasinAktiv, inaktiv: ovreGrundvattenmagasinInaktiv}],
    [3, {aktiv: berggrundmagasinAktiv, inaktiv: berggrundmagasinInaktiv}],
    [4, {aktiv: portryckAktiv, inaktiv: portryckInaktiv}],
    [5, {aktiv: energibrunnAktiv, inaktiv: energibrunnInaktiv}],
    [6, {aktiv: pumpgrop}],
    [7, {aktiv: matdamm}],
    [8, {aktiv: provpunkt}],
  ]
);

const typStyleMap = new Map(
  [
    [TYP_INFILTRATION, {aktiv: infiltration}],
    [TYP_RORELSE, {aktiv: rorelseAktiv, inaktiv: rorelseInaktiv}],
    [TYP_VATTENKEMI, {aktiv: vattenkemi}],
    [TYP_YTVATTENMATNING, {aktiv: ytvattenAktiv}]
  ]
);

export interface MdbStyle {
  default: Style;
  selected: Style;
}

export function matobjektStyleFn(matobjekt, resolution, selected: boolean, labels: boolean) {
  let styleEntry;
  if (matobjekt.typ === TYP_GRUNDVATTENNIVA || matobjekt.typ === TYP_TUNNELVATTEN) {
    styleEntry = gruppStyleMap.get(matobjekt.kartsymbol);
  } else {
    styleEntry = typStyleMap.get(matobjekt.typ);
  }

  let baseStyle: MdbStyle;
  if (styleEntry != null) {
    baseStyle = matobjekt.aktiv ? styleEntry.aktiv : styleEntry.inaktiv;
  }

  let style;

  if (selected) {
    style = baseStyle != null ? baseStyle.selected : defaultStyle.selected;
  } else {
    style =  baseStyle != null ? baseStyle.default : defaultStyle.default;
  }

  const textObj = style.getText();

  if (textObj != null) {
    if (labels && resolution < 0.7) {
      textObj.setText(matobjekt.namn);
    } else {
      textObj.setText(undefined);
    }
  }

  return style;
}

export function getStyleImageUrl(matobjekt: MatobjektMapinfo) {
  const style = matobjektStyleFn(matobjekt, 0, false, false);
  return style.getImage().getSrc();
}


function createIconStyle(iconUrl?): MdbStyle {
  if (iconUrl == null) {
    return {
      default: new Style({
        image: new Icon({
          anchor: [0.5, 30],
          anchorXUnits: "fraction",
          anchorYUnits: "pixels",
          opacity: 0.5,
          scale: 0.8,
          src: "assets/lib/icons/map_pin.png"
        }),
        text: new Text({
          textAlign: "left",
          offsetX: 10,
          stroke: new Stroke({color: "#ffffff", width: 3}),
          text: ""
        })
      }),
      selected: new Style({
        image: new Icon({
          anchor: [0.5, 30],
          anchorXUnits: "fraction",
          anchorYUnits: "pixels",
          opacity: 0.75,
          scale: 1,
          src: "assets/lib/icons/map_pin.png"
        }),
        text: new Text({
          textAlign: "left",
          offsetX: 10,
          stroke: new Stroke({color: "#ffffff", width: 3}),
          text: ""
        })
      })
    };
  }

  return {
    default: new Style({
      image: new Icon({
        anchor: [0.5, 0.5],
        opacity: 0.5,
        scale: 0.8,
        src: iconUrl
      }),
      text: new Text({
        textAlign: "left",
        offsetX: 10,
        stroke: new Stroke({color: "#ffffff", width: 3}),
        text: ""
        })
    }),
    selected: new Style({
      image: new Icon({
        anchor: [0.5, 0.5],
        opacity: 1,
        scale: 1,
        src: iconUrl
      }),
      text: new Text({
        textAlign: "left",
        offsetX: 10,
        stroke: new Stroke({color: "#ffffff", width: 3}),
        text: ""
      })
    })
  };
}

const pointStyle = new Circle({
  radius: 6,
  fill: new Fill({
    color: "#ff7333"
  }),
  stroke: new Stroke({
    color: "#ffffff",
    width: 1.25
  })
});

export const anlaggningStyle = new Style({
  image: pointStyle,
  stroke: new Stroke({
    color: "rgba(130,130,133,0.8)",
    width: 0.7
  })
});

export const kansligGrundlaggningStyle = new Style({
  image: pointStyle,
  stroke: new Stroke({
    color: "rgba(253,225,154,1.0)",
    width: 1.25
  }),
  fill: new Fill({
    color: "rgba(253,225,154,0.4)"
  })
});

export const influensomradeStyle = new Style({
  image: pointStyle,
  stroke: new Stroke({
    color: "rgba(0,0,0,1.0)",
    lineDash: [5, 10, 20, 10, 5, 10, 35, 10],
    width: 1.5
  }),
});

export const undreGrundvattenmagasinStyle = new Style({
  image: pointStyle,
  fill: new Fill({
    color: createHatchingPattern({
      color: "blue",
      name: "slash",
      thickness: 2
    })
  })
});

export const grundvattenforekomstStyle = new Style({
  image: pointStyle,
  fill: new Fill({
    color: createHatchingPattern({
      color: "gray",
      name: "backslash",
      thickness: 2
    })
  })
});

export const userDefinedLayerStyleMap = new Map([
  ["Anläggning", anlaggningStyle],
  ["Känslig grundläggning", kansligGrundlaggningStyle],
  ["Influensområde", influensomradeStyle],
  ["Undre grundvattenmagasin", undreGrundvattenmagasinStyle],
  ["Grundvattenförekomst", grundvattenforekomstStyle]
]);
