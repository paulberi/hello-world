import {Injectable} from "@angular/core";
import {Observable, forkJoin, of} from "rxjs";
import {ConfigService} from "../../config/config.service";
import {catchError, map, publish, refCount, tap} from "rxjs/operators";
import {transform} from "ol/proj";
import WKT from "ol/format/WKT";
import {FeatureCollection} from "../../map-core/map.service";
import {GeoJson} from "../../map-core/geojson.util";
import {ErrorHandlingHttpClient} from "../../http/http-client.service";
import Geometry from "ol/geom/Geometry";
import {FeatureInfo} from "../../map-core/feature-info.model";
import {HttpHeaders} from "@angular/common/http";

export interface Place {
  name: string;
  type: string;
  coordinate: number[];
  feature?: any;
  featureInfo?: FeatureInfo;
  extra?: Map<string, string>;
}
export enum SpatialPredicate {
  INTERSECTS,
  WITHIN
}

export enum SokResultStatus {
  ERROR,
  OK,
  TO_MANY_RESULTS
}
export interface SokGeoJsonResult {
  status: SokResultStatus;
  features: GeoJson[];
}

/**
 * SokService exponerar ett interface till söktjänsten i Xplore-plattformen.
 *
 * Metoderna returnerar i regel ett Place, om svaret motsvarar en plats i kartan,
 * t.ex. en adress eller liknande.
 *
 * Ibland returneras också hela GeoJson-objekt, om det är mer feature-liknande objekt
 * man söker efter.
 *
 */
@Injectable({
  providedIn: "root"
})
export class SokService {
  private WKT: WKT = new WKT();

  cache = {};

  constructor(private http: ErrorHandlingHttpClient, private configService: ConfigService) {
  }

  geocode(address: string, location?: any): Observable<Place[]> {
    const url = ConfigService.urlConfig.sokServiceUrl + "/geocode";
    let query = `?q=` + encodeURIComponent(address);

    if (location && location.x && location.y) {
      const transformedLocation = transform([location.x, location.y], this.configService.config.projectionCode, "EPSG:3006");

      query += `&x=${transformedLocation[0]}&y=${transformedLocation[1]}`;
    }

    return this.http.get(url + query).pipe(
      map((result: any[]) => {
          return result.map(item => {
            const coordinate = [item.x, item.y];
            const transformed = transform(coordinate, "EPSG:3006", this.configService.config.projectionCode);
            return {name: item.address, type: item.type, coordinate: transformed};
          });
        }
      ));
  }

  reverseGeocode(coordinate: [number, number]): Observable<Place[]> {
    const url = ConfigService.urlConfig.sokServiceUrl + "/reverse-geocode";

    const transformedInputCoordinate = transform(coordinate, this.configService.config.projectionCode, "EPSG:3006");

    const query = `?x=${transformedInputCoordinate[0]}&y=${transformedInputCoordinate[1]}`;
    return this.http.get(url + query).pipe(
      map((result: any[]) => {
          return result.map(item => {
            const coord = [item.x, item.y];
            const transformed = transform(coord, "EPSG:3006", this.configService.config.projectionCode);
            return {name: item.address, type: item.type, coordinate: transformed};
          });
        }
      ));
  }

  findFastighet(beteckning: string): Observable<Place[]> {
    const url = ConfigService.urlConfig.sokServiceUrl + "/fastighet";
    const query = `?q=` + encodeURIComponent(beteckning);

    const notEmptyFilter = f => f.properties["trakt"].trim() !== "" && f.properties["blockenhet"].trim() !== ""
      && f.properties["objekt_id"].trim() !== "";

    const uniqueFeatures = {};
    const isUniqueFilter = f => {
      const objektId = f.properties["objekt_id"];
      return !objektId || (!uniqueFeatures[objektId] && (uniqueFeatures[objektId] = true));
    };

    return this.http.get(url + query).pipe(
      map((result: any) => result.features.filter(notEmptyFilter)),
      map(features => features.filter(isUniqueFilter)),
      map(features => features.map(f => {
        return {
          name: f.properties["kommunnamn"] + " " + f.properties["trakt"] + " " + f.properties["blockenhet"],
          type: "0000",
          feature: f
        };
      }))
    );
  }

  /**
   * Hämtar alla delområden tillhörande ett gäng fastigheter.
   */
  public getDelomradenForFastigheter(objektIds: string[]): Observable<GeoJson[]> {
    const requests: Observable<GeoJson[]>[] = objektIds.map(id => this.getDelomradenForFastighet(id));
    return forkJoin(requests).pipe(map(delomraden => {
      let response = [];
      delomraden.forEach(d => response = response.concat(d));
      return response;
    }));
  }


  /**
   * Hämtar alla delområden som tillhör en enskild fastighet.
   */
  public getDelomradenForFastighet(objektId: string): Observable<GeoJson[]> {
    const url = ConfigService.urlConfig.sokServiceUrl + "/fastighet";
    const query = "?q=" + objektId;
    const cached = this.cache[objektId];
    if (!cached) {
      return this.http.get<FeatureCollection>(url + query).pipe(
        tap(result => {
          this.cache[objektId] = result.features;
        }),
        map(result => result.features),
        publish(),
        refCount()
      );
    } else {
      return of(cached);
    }
  }

  public getAntalDelomradenForFastighet(objektId: string): number {
    if (this.cache[objektId]) {
      return this.cache[objektId].length;
    }
    return null;
  }

  findFastigheterByGeometry(geometry: Geometry, predicate: SpatialPredicate, bufferDistance?: number): Observable<SokGeoJsonResult> {
    const url = ConfigService.urlConfig.sokServiceUrl + "/fastighet/geometry";

    const transformedGeometry = geometry.clone().transform(this.configService.config.projectionCode, "EPSG:3006");
    let query = "q=" + this.WKT.writeGeometry(transformedGeometry);

    if (predicate === SpatialPredicate.INTERSECTS) {
      query += "&o=INTERSECTS";
    } else if (predicate === SpatialPredicate.WITHIN) {
      query += "&o=WITHIN";
    }

    if (bufferDistance) {
      query += "&b=" + bufferDistance;
    }

    const headers = new HttpHeaders()
      .set("Content-Type", "application/x-www-form-urlencoded");

    return this.http.post(url, query, {headers: headers}).pipe(
      catchError(() => of({status: SokResultStatus.ERROR, features: []})),
      map((result) => {
        let status: SokResultStatus;

        if (result.numberMatched > result.numberReturned)  {
          status = SokResultStatus.TO_MANY_RESULTS;
        } else {
          status = SokResultStatus.OK;
        }

        const features: GeoJson[] = result.features;

        return {status: status, features: features};
      }));
  }

  findOrter(query: string, location?: any): Observable<Place[]> {
    const url = ConfigService.urlConfig.sokServiceUrl + "/ort";
    const parts = query.split(",");

    const ort = encodeURIComponent(parts[0]);
    const kommun = parts.length > 1 ? encodeURIComponent(parts[1].trim()) : undefined;
    const lan = parts.length > 2 ? encodeURIComponent(parts[2].trim()) : undefined;

    query = "?q=" + ort;

    if (kommun) {
      query += "&k=" + kommun;
    }

    if (lan) {
      query += "&l=" + lan;
    }

    if (location && location.x && location.y) {
      const transformedLocation = transform([location.x, location.y], this.configService.config.projectionCode, "EPSG:3006");

      query += `&x=${transformedLocation[0]}&y=${transformedLocation[1]}`;
    }

    return this.http.get(url + query).pipe(
      map((result: any[]) => {
          return result.map(item => {
            const coordinate = [item.x, item.y];
            const transformed = transform(coordinate, "EPSG:3006", this.configService.config.projectionCode);
            return {
              name: item.ort,
              type: item.typ,
              coordinate: transformed,
              extra: new Map([["kommun", item.kommun], ["län", item.lan]])
            };
          });
        }
      )
    );
  }

  findTatorter(query: string, location?: any): Observable<Place[]> {
    const url = ConfigService.urlConfig.sokServiceUrl + "/tatort";
    const parts = query.split(",");

    const ort = encodeURIComponent(parts[0]);
    const kommun = parts.length > 1 ? encodeURIComponent(parts[1].trim()) : undefined;
    const lan = parts.length > 2 ? encodeURIComponent(parts[2].trim()) : undefined;

    query = "?q=" + ort;
    query += kommun ? "&k=" + kommun : "";
    query += lan ? "&l=" + lan : "";

    if (location && location.x && location.y) {
      const transformedLocation = transform([location.x, location.y], this.configService.config.projectionCode, "EPSG:3006");

      query += `&x=${transformedLocation[0]}&y=${transformedLocation[1]}`;
    }

    return this.http.get(url + query).pipe(
      map((result: any[]) => {
          return result.map(item => {
            const coordinate = [item.x, item.y];
            const transformed = transform(coordinate, "EPSG:3006", this.configService.config.projectionCode);
            return {
              name: item.ort,
              type: item.typ,
              coordinate: transformed,
              extra: new Map([["kommun", item.kommun], ["län", item.lan]])
            };
          });
        }
      )
    );
  }

  findKommuner(query: string): Observable<Place[]> {
    const url = ConfigService.urlConfig.sokServiceUrl + "/kommun";
    const parts = query.split(",");

    const kommun = encodeURIComponent(parts[0]);
    const lan = parts.length > 1 ? encodeURIComponent(parts[1].trim()) : undefined;

    query = "?q=" + kommun;
    query += lan ? "&l=" + lan : "";

    return this.http.get(url + query).pipe(
      map((fc: any) => fc.features.map(f => {
        return {
          name: f.properties["kommunnamn"],
          type: "KOMMUN",
          feature: f,
          extra: new Map([["län", f.properties["lansnamn"]]])
        };
      }))
    );
  }
}
