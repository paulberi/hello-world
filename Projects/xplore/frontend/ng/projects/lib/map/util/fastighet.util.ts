import Feature from 'ol/Feature';
import {GeoJson} from "../../map-core/geojson.util";

export enum FeatureType {
  FASTIGHET,
  FASTIGHETSDELOMRADE,
  RITAD_YTA
}

export const RITAD_YTA = "RITAD_YTA";

export function createFeatureName(feature: GeoJson, type?: FeatureType): string {
  if (feature.properties && feature.properties["kategori"] === "Kommun") {
    return createPrettyKommunnamn(feature.properties["kommunnamn"]);
  }

  switch (type) {
    case FeatureType.FASTIGHET: {
      if (feature.properties["kommunnamn"] == null || feature.properties["trakt"] == null || feature.properties["blockenhet"] == null) {
        return feature.properties["externid"];
      }

      return feature.properties["kommunnamn"] + " " + feature.properties["trakt"] + " " + feature.properties["blockenhet"];
    }
    case FeatureType.FASTIGHETSDELOMRADE: {
      if (feature.properties["kommunnamn"] == null || feature.properties["trakt"] == null || feature.properties["blockenhet"] == null) {
        return feature.properties["externid"];
      }

      return feature.properties["kommunnamn"] + " " + feature.properties["trakt"] + " " + feature.properties["blockenhet"] +
        ((feature.properties.omrnr > 0) ? (">" + feature.properties.omrnr) : "");
    }
    case FeatureType.RITAD_YTA: {
      return "EGENRITAT OMRÅDE";
    }
  }

  return "";
}
export function sortFeatures(features: GeoJson[]) {
  features.sort((first, second) => {
    if (isRitadFeature(first) || isRitadFeature(second)) {
      return 0;
    }
    let comp = first.properties["kommunnamn"].localeCompare(second.properties["kommunnamn"], "sv");
    if (comp === 0) {
      comp = first.properties["trakt"].localeCompare(second.properties["trakt"], "sv");
    }
    if (comp === 0) {
      const firstBEs = first.properties["blockenhet"].split(":");
      const secondBEs = second.properties["blockenhet"].split(":");
      const firstBlock = firstBEs.length === 2 ? firstBEs[0] : "";
      const firstEnhet = firstBEs.length === 2 ? firstBEs[1] : firstBEs[0];
      const secondBlock = secondBEs.length === 2 ? secondBEs[0] : "";
      const secondEnhet = secondBEs.length === 2 ? secondBEs[1] : secondBEs[0];
      if (isNum(firstBlock) && isNum(secondBlock)) {
        comp = parseInt(firstBlock, 10) - parseInt(secondBlock, 10);
      } else {
        comp = firstBlock.localeCompare(secondBlock, "sv");
      }
      if (comp === 0) {
        if (isNum(firstEnhet) && isNum(secondEnhet)) {
          comp = parseInt(firstEnhet, 10) - parseInt(secondEnhet, 10);
        } else {
          comp = firstEnhet.localeCompare(secondEnhet, "sv");
        }
      }
    }
    if (comp === 0) {
      comp = first.properties.omrnr - second.properties.omrnr;
    }
    return comp;
  });
}

export function isRitadFeature(feature: GeoJson): boolean {
  return feature.id === RITAD_YTA;
}

export function isFastighet(feature: Feature): boolean {
  return feature.getId() && String(feature.getId()).startsWith("Fastighetsytor_WFS");
}

function isNum(value: any): boolean {
  return !isNaN(parseInt(value, 10));
}

/**
 * Adds a 's' after the kommunnamn when the kommunnamn ends with a consonant that isn't either 's' or 'x'.
 */
function createPrettyKommunnamn(kommunnamn: string): string {
  if (/.+[bcdfghjklmnpqrtvwz]$/.test(kommunnamn)) {
    kommunnamn += "s";
  }
  return kommunnamn + " kommun";
}

