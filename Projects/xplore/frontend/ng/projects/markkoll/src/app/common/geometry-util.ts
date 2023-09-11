import { Coordinate } from "ol/coordinate";
import { Geometry } from "ol/geom";
import GeoJSON from "ol/format/GeoJSON";
import { Feature } from "ol";
import { IntrangOption } from "../markhandlaggning/kartverktyg/meny/hantera-intrang/verktyg/intrang-form/intrang-form.component";

export enum GeometriTyp {
  PUNKT, LINJE
}

// Sammanfoga en uppsättning av LineStrings (t.ex. en MultiLineStrings) till en enda LineString
export function mergeLines(lines: Coordinate[][]): Coordinate[] {
  if (lines?.length == 0) {
    return [];
  }

  let linesCopy = lines.slice(1);
  let lineOut = lines[0];

  while (linesCopy.length > 0) {
    let lineOutTmp = [];
    let d: number;
    let closestDistance = Number.POSITIVE_INFINITY;
    let closestDistanceLineIndex = 0;

    for (let i = 0; i < linesCopy.length; i++) {
      let line = linesCopy[i];
      d = distance(line[0], lineOut[0]);
      if (d < closestDistance) {
        closestDistance = d;
        closestDistanceLineIndex = i;

        lineOutTmp = line.slice().reverse().concat(lineOut);
      }

      d = distance(line[0], lineOut[lineOut.length - 1]);
      if (d < closestDistance) {
        closestDistance = d;
        closestDistanceLineIndex = i;

        lineOutTmp = lineOut.concat(line);
      }

      d = distance(line[line.length - 1], lineOut[0]);
      if (d < closestDistance) {
        closestDistance = d;
        closestDistanceLineIndex = i;

        lineOutTmp = line.concat(lineOut);
      }

      d = distance(line[line.length - 1], lineOut[lineOut.length - 1]);
      if (d < closestDistance) {
        closestDistance = d;
        closestDistanceLineIndex = i;

        lineOutTmp = lineOut.concat(line.slice().reverse());
      }
    }

    linesCopy.splice(closestDistanceLineIndex, 1);
    lineOut = lineOutTmp;
  }

  return lineOut;
}

export function getGeometrityp(geometry: Geometry): GeometriTyp {
  const typ = geometry.getType();
  switch (typ) {
    case "Point":
      return GeometriTyp.PUNKT;
    case "LineString":
    case "MultiLineString":
      return GeometriTyp.LINJE;
    default:
      throw new Error("Okänd geometrityp: " + typ);
  }
}

export function writeGeoJson(geometry: Geometry): string {
  const geoJson = new GeoJSON().writeGeometry(geometry);
  const geoObj = JSON.parse(geoJson);
  /* Har inte hittat något sätt att få med CRS när man skriver ett GeoJSON-objekt, så det får vi
  bifoga själva */
  geoObj.crs = {
    type: "name",
    properties: {
      name: "EPSG:3006"
    }
  }

  return JSON.stringify(geoObj);
}

export function convertFeature(id: string, feature: Feature<Geometry>): IntrangOption {
  const properties = feature.getProperties();
  return {
    id: id,
    geom: writeGeoJson(feature.getGeometry()),
    intrangstyp: properties["intrangstyp"],
    subtyp: properties["intrangsSubtyp"],
    avtalstyp: properties["avtalstyp"],
    geometriTyp: getGeometrityp(feature.getGeometry())
  }
}

function distance(c1: Coordinate, c2: Coordinate) {
  const dx = c1[0] - c2[0];
  const dy = c1[1] - c2[1];

  return Math.sqrt(dx * dx + dy * dy);
}
