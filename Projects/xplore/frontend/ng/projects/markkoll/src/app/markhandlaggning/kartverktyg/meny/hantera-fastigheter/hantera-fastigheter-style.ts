
import {asArray} from "ol/color";
import Feature from "ol/render/Feature";
import {Fill, Stroke, Style} from "ol/style";

const addedColor = "#18BC11";
const removedColor = "#F45959";
const currentColor = "#0B87BE";

const addedFastighetStyle = makeFastighetStyle(addedColor);
const removedFastighetStyle = makeFastighetStyle(removedColor);
const currentFastighetStyle = makeFastighetStyle(currentColor);

export function hanteraFastighetStyle(feature: Feature) {
  const status = feature.get("map_tool_selected_status") as string;
  
  if (status === "added") return addedFastighetStyle;
  if (status === "removed") return removedFastighetStyle;
  if (status === "current") return currentFastighetStyle;
  return new Style({
    stroke: new Stroke({
      color: "rgba(0, 0, 0, 0)"
    })
  });
}

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