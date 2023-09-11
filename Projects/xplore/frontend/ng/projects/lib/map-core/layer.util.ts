import {LayerDef} from "../config/config.service";
import {WfsSource} from "./wfs.source";
import Layer from "ol/layer/Layer";
import LayerGroup from "ol/layer/Group";
import VectorSource from "ol/source/Vector";
import TileWMS from "ol/source/TileWMS";
import ImageWMS from "ol/source/ImageWMS";
import {Extent, extend, createEmpty} from "ol/extent";

export function getLayerName(layerDef: LayerDef): string {
  let name;
  if (layerDef.source.type) {
    const layerType = layerDef.source.type.toLowerCase();
    const source: any = layerDef.source;
    if (layerType === "tilewms") {
      name = source.params.LAYERS;
    } else if (layerType === "wfs") {
      name = source.typeName;
    } else if (layerType === "wmts") {
      name = source.config.layer;
    }
  }

  return name;
}

export function getOgcUrl(layer: Layer): string {
  const source = layer.getSource();
  if (source instanceof WfsSource || source instanceof ImageWMS || source instanceof TileWMS) {
    if (source instanceof TileWMS) {
      return source.getUrls()[0];
    } else {
      return <string> source.getUrl();
    }
  }
  throw new TypeError("Tried to get the WFS url from a non-OGC source");
}

/**
 * Get the extent of a vector layer or a group of vector layers.
 */
export function getExtentFromLayer(layer): Extent {
  if (layer instanceof LayerGroup) {
    let extent = createEmpty();
    layer.getLayers().forEach(l => {
      extent = extend(extent, getExtentFromLayer(l));
    });
    return extent;
  } else if (layer.getSource() instanceof VectorSource) {
    return layer.getSource().getExtent();
  } else if (layer.getSource() instanceof TileWMS) {
    return layer.getExtent();
  } else {
    return createEmpty();
  }
}
