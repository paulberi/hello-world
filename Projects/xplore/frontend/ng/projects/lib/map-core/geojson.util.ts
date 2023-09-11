// This file contains utility functions for GeoJson objects.

import {createEmpty, extend} from "ol/extent";

export interface GeoJsonGeometry {
  type: string;
  coordinates: any;
}

export interface GeoJson {
  id: string; /* this is a geoserver extension */
  type: string;
  geometry: GeoJsonGeometry;
  geometry_name?: string;
  bbox?: [number, number, number, number];
  properties?: any;
}

export function containsFeature(features: GeoJson[], feature: GeoJson): boolean {
  return features.find(f => f.id === feature.id) !== undefined;
}

export function extendBoundingBox(features: GeoJson[]) {
  let bbox = createEmpty();
  features.forEach(feature => bbox = extend(bbox, feature.bbox));
  return bbox;
}
