import VectorSource from "ol/source/Vector";
import {bbox} from "ol/loadingstrategy";
import GeoJSON from "ol/format/GeoJSON";
import WFS from "ol/format/WFS";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, switchMap, tap} from "rxjs/operators";
import {Observable, of} from "rxjs";
import {FeatureInfo} from "./feature-info.model";
import "./editable-feature";
import {LayerDef} from "../config/config.service";
import {LayerService} from "./layer.service";
import Feature from "ol/Feature";

interface WfsSourceOpts {
  url: string;
  typeName: string;
  projection;
  canClear: boolean;
  [propName: string]: any;
}

interface WfsTransactionResponse {
  transactionSummary: {
    totalDeleted: number;
    totalInserted: number;
    totalUpdated: number;
  };
  insertIds: string[];
}

export class FeatureTypeDescription {
    geometryType: GeometryType;
    geometryName: string;
}

export enum GeometryType {
    Polygon = "Polygon",
    Point = "Point",
    Line = "LineString",
    MultiPolygon = "MultiPolygon",
    MultiPoint = "MultiPoint",
    MultiLine = "MultiLineString",
    Geometry = "Geometry",
    Text = "Text",
    Square = "Square",
    MapNeedle = "MapNeedle",
    ArrowRightUp = "ArrowRightUp",
    ArrowLeftDown = "ArrowLeftDown"
}

/**
 * WfsSource extends the built-in VectorSource to include WFS read & write support.
 */
export class WfsSource extends VectorSource {
  private readonly http: HttpClient;
  private readonly url: string;
  private readonly typeName: string;
  private readonly projectionCode;
  private readonly wfsFormat;
  private readonly canClear;

  constructor(http, layerDef: LayerDef, layerService: LayerService, opts: WfsSourceOpts) {
    super({
      format: new GeoJSON({extractGeometryName: true}),
      strategy: bbox,
      ...opts
    });
    this.http = http;
    this.url = opts.url;
    this.typeName = opts.typeName;
    this.canClear = opts.canClear;

    // Make sure that the coordinate order of the features we send are understood.
    // See https://docs.geoserver.org/latest/en/user/services/wfs/basics.html#axis-ordering
    // for the assumptions that GeoServer makes.
    if (opts.projection.getAxisOrientation() === "neu" && !opts.projection.getCode().startsWith("urn:ogc:def:crs") ) {
      this.projectionCode = "urn:ogc:def:crs" + ":" + opts.projection.getCode();
    } else {
      this.projectionCode = opts.projection.getCode();
    }

    this.wfsFormat = new WFS();

    // We use angular's http client when making the requests to automatically
    // add OIDC token if necessary.
    super.setLoader((extent, resolution, projection) => {
        const extentProjection = projection.getCode();
        const url = this.url + "?service=WFS&version=1.1.0&request=GetFeature&typeName= " +
          this.typeName + "&outputFormat=application/json&srsname=" + this.projectionCode + "&BBOX=" + extent.join(",") + "," + extentProjection;
        http.get(url)
          .subscribe(resp => {
            super.addFeatures(<Feature[]> super.getFormat().readFeatures(resp));
            if (layerDef.__layerProblemLoading !== false) {
              layerDef.__layerProblemLoading = false;
              layerService.updateLayer(layerDef);
            }
          }, () => {
            super.removeLoadedExtent(extent);
            if (layerDef.__layerProblemLoading !== true) {
              layerDef.__layerProblemLoading = true;
              layerService.updateLayer(layerDef);
            }
          });
      },
    );
  }

  getFeatureTypeDescription(): Observable<FeatureTypeDescription> {
      const url = this.url + "?service=WFS&version=1.1.0&request=DescribeFeatureType&typeName=" + this.typeName + "&outputFormat=application/json";
      return this.http.get<any>(url).pipe(
          map(featureTypDescription => {
              let geometryType = null;
              let geometryName = "";
              const styleType = null;
              featureTypDescription.featureTypes[0].properties.forEach((property) => {
                  if (property.type.startsWith("gml:")) {
                      geometryType = property.localType;
                      geometryName = property.name;
                  }
              });
              return {geometryType: geometryType, geometryName: geometryName, styleType: styleType};
          }));
  }

  removeAllFeaturesFromRemote(): Observable<any> {
      if (this.canClear) {
          // Vi behöver hämta samtliga features direkt ifrån wfs-tjänsten då openlayers Vector.getFeatures() endast innehåller de features som man har sett i kartan.
          // Därför behöver vi först hämta ut alla features ifrån wfs-tjänsten för att sedan ta bort dem mha syncRemote.
          const url = this.url + "?service=WFS&version=1.1.0&request=GetFeature&typeName=" + this.typeName + "&outputFormat=application/json";
          return this.http.get<any>(url).pipe(
              switchMap(featureCollection => {
                  return this.syncRemote([], [], featureCollection.features != null ? featureCollection.features.map(feature => FeatureInfo.fromGeoJson(feature, null).feature) : []).pipe(map(wfsTransactionResponse => {
                      // Vi kan använda getFeatures i detta läge då vi endast är intresserade av de features som laddats in i openlayers vektorlager.
                      // De features som har laddats i lagret kan eventuellt vara öppna i till exempel en feature-info-panel och behöver tas bort därifrån också.
                      const result = {wfsTransactionResponse: wfsTransactionResponse, loadedFeatures: super.getFeatures()};
                      super.clear();
                      return result;
                  }));
              })
          );
      }
  }

  private syncRemote(inserts: any[], updates: any[], deletes: any[]): Observable<WfsTransactionResponse> {
    if (inserts.length == 0 && updates.length == 0 && deletes.length == 0) {
      return of({
        transactionSummary: {
          totalDeleted: 0,
          totalInserted: 0,
          totalUpdated: 0,
        },
        insertIds: []
      });
    }

    const featurePrefix = this.typeName.substring(0, this.typeName.indexOf(":"));
    const featureType = this.typeName.substring(this.typeName.indexOf(":") + 1);

    console.debug("wfs-source: prefix: ", featurePrefix);
    console.debug("wfs-source: type: ", featureType);
    console.debug("wfs-source: projection: ", this.projectionCode);

    const transaction = this.wfsFormat.writeTransaction(inserts, updates, deletes, {
      featureNS: featurePrefix,
      featurePrefix: featurePrefix,
      featureType: featureType,
      srsName: this.projectionCode
    });

    console.debug("wfs-source: sending transaction: ", transaction);

    const headers = new HttpHeaders()
      .set("Content-Type", "application/xml");

    return this.http.post(this.url, transaction.outerHTML, {
      headers: headers,
      responseType: "text",
    }).pipe(
      tap(response => console.debug("wfs-source: transaction complete: ", response)),
      map(response => {
        console.debug("wfs-source: unparsed result");
        console.debug(response);
        return this.wfsFormat.readTransactionResponse(response);
      }),
      tap(result => console.debug("wfs-source: transaction result parsed: ", result))
    );
  }

  /**
   * Sends a WFS-T request to remove the provided features from the remote.
   * The features are removed from the local source as well.
   *
   * The features are assumed to exist in the local source.
   */
  removeFromRemote(features: any[]): Observable<WfsTransactionResponse> {
    // Only send features that are actually persisted to the remote.
    // When the request succeeds we will remove all features from
    // the local source, however.
    const deletes = features.filter(f => f.isPersisted());
    return this.syncRemote([], [], deletes)
      .pipe(
        tap(() => features.forEach(f => super.removeFeature(f)))
      );
  }

  /**
   * Sends a WFS-T request to insert or update the provided features.
   * The ID of the inserted features are updated according to the
   * response from the server.
   *
   * The features are assumed to exist in the local source.
   */
  syncWithRemote(features): Observable<WfsTransactionResponse> {
    const inserts = features.filter(f => !f.isPersisted());
    const updates = features.filter(f => f.isPersisted());

    return this.syncRemote(inserts, updates, []).pipe(
      tap(response => {
        if (response.insertIds.length == 1 && response.insertIds[0] == "none") {
          return;
        }
        for (let i = 0; i < response.insertIds.length; i++) {
          inserts[i].setStyle(null);
          inserts[i].setId(response.insertIds[i]);
        }
      })
    );
  }
}
