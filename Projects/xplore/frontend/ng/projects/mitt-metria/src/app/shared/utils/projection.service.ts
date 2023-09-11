import { Injectable } from "@angular/core";
import { transform } from "ol/proj";
import { register } from 'ol/proj/proj4'
import proj4 from 'proj4';

@Injectable({
  providedIn: "root"
})
export class ProjectionService {

  constructor() { }

  transformProjection(coordinates: number[], srcProj: string, destProj: string): number[] | null {
    try {
      proj4.defs("EPSG:3006", "+proj=utm +zone=33 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs +axis=neu");
      register(proj4);
      return transform(coordinates, srcProj, destProj);
    } catch {
      return null;
    }
  }
}
