// This file contains utility functions for openlayers feature objects.

import Feature from 'ol/Feature';
import {createEmpty, extend, Extent} from "ol/extent";
import Geometry from 'ol/geom/Geometry';
import LinearRing from 'ol/geom/LinearRing';
import {LineString, MultiLineString, MultiPoint, MultiPolygon, Point, Polygon} from 'ol/geom';

import OL3Parser from "jsts/org/locationtech/jts/io/OL3Parser";
import {BufferOp, BufferParameters} from 'jsts/org/locationtech/jts/operation/buffer';
import {RelateOp} from 'jsts/org/locationtech/jts/operation/relate';

export function calculateExtent(features: Feature[]): Extent {
  const extent = createEmpty();
  features.forEach(f => extend(extent, f.getGeometry().getExtent()));
  return extent;
}


/**
 * Buffers the given OL geometry by the given distance (in meters)
 */
export function bufferGeometry(geometry: Geometry, distance: number) {
  let parser: OL3Parser;
  parser = new OL3Parser();
  // Injecting the geometry classes/objects is necessary as explained in https://github.com/bjornharrtell/jsts/issues/332
  parser.inject(Point, LineString, LinearRing, Polygon, MultiPoint, MultiLineString, MultiPolygon);

  const jstsGeom = parser.read(geometry);
  const bufferParameters = new BufferParameters(3, BufferParameters.CAP_ROUND, BufferParameters.JOIN_ROUND, 3);

  return parser.write(BufferOp.bufferOp(jstsGeom, distance, bufferParameters));
}

 export function objectContainsObject(firstObject: Geometry, secondObject: Geometry) {
  let parser: OL3Parser;
  parser = new OL3Parser();
  // Injecting the geometry classes/objects is necessary as explained in https://github.com/bjornharrtell/jsts/issues/332
  parser.inject(Point, LineString, LinearRing, Polygon, MultiPoint, MultiLineString, MultiPolygon);

  const jstsGeom = parser.read(firstObject);
  const jstsGeom2 = parser.read(secondObject);

  return RelateOp.contains(jstsGeom, jstsGeom2) || RelateOp.contains(jstsGeom2, jstsGeom);
}


export function intersects(firstObject: Geometry, secondObject: Geometry) {
  let parser: OL3Parser;
  parser = new OL3Parser();
  // Injecting the geometry classes/objects is necessary as explained in https://github.com/bjornharrtell/jsts/issues/332
  parser.inject(Point, LineString, LinearRing, Polygon, MultiPoint, MultiLineString, MultiPolygon);

  const jstsGeom = parser.read(firstObject);
  const jstsGeom2 = parser.read(secondObject);

  return RelateOp.intersects(jstsGeom, jstsGeom2);
}
