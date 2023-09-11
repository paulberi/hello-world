import GeoJSONFormat from "ol/format/GeoJSON";
import Layer from "ol/layer/Layer";
import Feature from "ol/Feature";
import {GeoJson} from "./geojson.util";

const reader = new GeoJSONFormat();

export class FeatureInfo {
  feature: Feature;
  layer: Layer;

  constructor(feature: Feature, layer: Layer) {
    this.feature = feature;
    this.layer = layer;
  }

  static fromGeoJson(geojson: GeoJson, layer: Layer) {
    const feature = (<any>reader).readFeature(geojson);
    return new FeatureInfo(feature, layer);
  }
}
