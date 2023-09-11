import {Injectable} from "@angular/core";

import {combineLatest, Observable, of, zip} from "rxjs";
import {catchError, filter, flatMap, map} from "rxjs/operators";

import TileWMS from "ol/source/TileWMS";
import ImageWMS from "ol/source/ImageWMS";
import VectorLayer from "ol/layer/Vector";
import Layer from "ol/layer/Layer";
import GeoJSON from "ol/format/GeoJSON";
import * as OlExtent from "ol/extent";

import {FeatureInfo} from "../../map-core/feature-info.model";
import {ClickEvent, ClickResult, FeatureCollection, MapService} from "../../map-core/map.service";
import {LayerService} from "../../map-core/layer.service";
import {HttpClient} from "@angular/common/http";
import {ConfigProperty, ConfigService} from "../../config/config.service";
import {TileWmsLayerFactory} from "../../map-core/tilewms";
import {isRitadFeature} from "../util/fastighet.util";
import {getOgcUrl} from "../../map-core/layer.util";
import {SokGeoJsonResult, SokService, SpatialPredicate} from "./sok.service";
import Geometry from "ol/geom/Geometry";
import {ViewService} from "../../map-core/view.service";
import VectorSource from "ol/source/Vector";
import {convertFromEsriXmlToJson} from "../util/esri-xml.util";

// Fungerar som rxjs-operatorn zip men konkatenerar svaren från varje observable.
// Förutsätter således att varje observable returnerar en array.
const flatZip = (...array) => zip(...array).pipe(map(a => [].concat(...a)));

// Hjälpfunktioner för filtrera bland lager.
const isWmsLayer = layer => layer.getSource() instanceof TileWMS || layer.getSource() instanceof ImageWMS;
const isVectorLayer = layer => layer instanceof VectorLayer;
const isQueryable = layer => layer.getVisible() && layer.get("layerDef") && layer.get("layerDef").queryable;
const isMapPin = layer => layer.get("layerDef") && layer.get("layerDef").id === "map_pin";
const isSelectedFeature = layer => layer.get("layerDef") && layer.get("layerDef").id === "selected_features";

/**
 * Tjänst för att hämta objekt i kartan.
 */
@Injectable({
  providedIn: "root"
})
export class QueryService {
  private geoJson = new GeoJSON();

  private minimumZoom;

  constructor(private mapService: MapService, private layerService: LayerService,
              private configService: ConfigService, private viewService: ViewService,
              private http: HttpClient, private tileWmsFactory: TileWmsLayerFactory,
              private sokService: SokService) {
    const prop = configService.getConfigProperty(ConfigProperty.FASTIGHETS_SELECTION_SETTINGS);

    // Zoom-nivå på 6.4 och över verkar vara ungefär då fastighetsytor renderas i kartan.
    // Min-zoom på -5 gör så att man kan välja fastigheter även om man hoppar lite under minsta tänkta zoom (händer ibland) i appen.
    const ZOOM_MIN_DEFAULT = -5;
    this.minimumZoom = !prop || prop.minimumZoom == null ? ZOOM_MIN_DEFAULT : prop.minimumZoom;
  }

  getInfoClick(): Observable<any> {
    return combineLatest(this.getInternalLayersClick(), this.getInfoClickInternal()).pipe(
      filter(([fc, fi]) => fc.click === fi.click),
      map(([fc, fi]) => [fc.fc, fi.fi, fc.click])
    );
  }

  private getInternalLayersClick(): Observable<any> {
    return this.mapService.click$.pipe(
      map((click: ClickEvent) => {
        const fc: FeatureCollection = {features: []};
        this.layerService.internalLayers.getLayers().getArray()
          .filter(isVectorLayer)
          .filter(isQueryable)
          .filter(isSelectedFeature)
          .forEach(layer => {
            const pixel = this.mapService.map.getPixelFromCoordinate(click.coordinate);
            const features = this.mapService.map.getFeaturesAtPixel(pixel, {layerFilter: l => l === layer}) || [];
            const geojsonFeatures = features
              .map(f => this.geoJson.writeFeatureObject(f))
              .filter(f => isRitadFeature(f));
            fc.features.push(...geojsonFeatures);
          });
        return {fc: fc, click: click};
      })
    );
  }
  /**
   * Returnerar en observable som för varje klick i kartan returnerar funna features.
   *
   * Kollar både i vektorlager och WMS-lager och respekterar "queryable"-inställningen i
   * LayerDef för lagret.
   *
   * Funna features markeras också ut i kartan.
   */
  private getInfoClickInternal(): Observable<ClickResult> {
    // Kollar i vektorlager om pixeln man klickat på innehåller några features. Kan vara flera.
    const vectorFeatures: Observable<ClickResult> = this.mapService.click$.pipe(
      map((click: ClickEvent) => {
        const results: FeatureInfo[] = [];
        this.layerService.layers.getLayers().getArray()
          .filter(isQueryable)
          .filter(isVectorLayer)
          .forEach(layer => {
            const pixel = this.mapService.map.getPixelFromCoordinate(click.coordinate);
            const features = this.mapService.map.getFeaturesAtPixel(pixel, {layerFilter: l => l === layer}) || [];
            const featureInfos = features.map(feature =>
              new FeatureInfo(feature, <Layer> layer));
            results.push(...featureInfos);
          });
        return {fi: results, click: click} as ClickResult;
      })
    );

    // Kollar i WMS-lager med hjälp av GetFeatureInfo om man klickat på några features.
    const wmsFeatures: Observable<ClickResult> = this.mapService.click$.pipe(
      flatMap(click => {
        const queries = this.layerService.layers.getLayers().getArray()
          .filter(isQueryable)
          .filter(isWmsLayer)
          .map((layer: any) => {
            let infoFormat = "application/json";
            let removeLayersFromURL = false;

            if (layer.get("layerDef") && layer.get("layerDef").source) {
              switch (layer.get("layerDef").source.serverType) {
                case "arcgis":
                  infoFormat = "application/vnd.esri.wms_raw_xml";

                  // acgis struntar i LAYERS parametern, den tittar bara på QUERY_LAYERS.
                  // Ta bort LAYERS från getFeturInfoURl:en så att vi håller ner storleken på anropen för att
                  // inte överskrida max URL längd hos vissa kartservicar
                  removeLayersFromURL = true;
                  break;
              }
            }

            const queryOpts = {
              "INFO_FORMAT": infoFormat,
              "FEATURE_COUNT": 5
            };

            if (removeLayersFromURL) {
              queryOpts["LAYERS"] = "";
            }

            const url = layer.getSource().getFeatureInfoUrl(click.coordinate, click.resolution, click.projection, queryOpts);
            return this.http.get(url, {observe: "response", responseType: "text"}).pipe(
              map(resp => {
                const contentType = resp.headers.get("Content-Type");

                let json;

                if (contentType.substr(0,32) === "application/vnd.esri.wms_raw_xml") {
                  json = convertFromEsriXmlToJson(resp.body);
                } else {
                  json = resp.body;
                }

                let fc: FeatureCollection;
                try {
                   fc = JSON.parse(json);
                } catch (e) {
                  console.error("Error converting feature to geojson", url, resp.body, json);
                  return [];
                }

                return fc.features.map(geojson => FeatureInfo.fromGeoJson(geojson, layer));
              }),
              catchError((err, caught) => {
                console.error("Error parsing feature", url, err);
                return of([]);
              })
            );
          });

        const result = queries.length === 0 ? of([]) : flatZip(...queries);
        return result.pipe(
          map(features => ({fi: features, click: click}))
        );
      })
    );

    const mapPinFeature: Observable<ClickResult> = this.mapService.click$.pipe(
      map((click: ClickEvent) => {
        const results: FeatureInfo[] = [];
        this.layerService.internalLayers.getLayers().getArray()
          .filter(isVectorLayer)
          .filter(isQueryable)
          .filter(isMapPin)
          .forEach(layer => {
            const pixel = this.mapService.map.getPixelFromCoordinate(click.coordinate);
            const features = this.mapService.map.getFeaturesAtPixel(pixel, {layerFilter: l => l === layer}) || [];
            const featureInfos = features.map(feature =>
              new FeatureInfo(feature, <Layer> layer));
            results.push(...featureInfos);
          });
        return {fi: results, click: click};
      })
    );

    return zip(vectorFeatures, wmsFeatures, mapPinFeature).pipe(
      map(features => ({fi: [].concat(features[0].fi).concat(features[1].fi).concat(features[2].fi), click: features[0].click}))
    );
  }


  /**
   * Returnerar en lista med funna features som ligger inom en polygon.
   *
   * Kollar både i vektorlager och respekterar "queryable"-inställningen i
   * LayerDef för lagret. För tillfället finns det inget stöd för polygon-selektering i
   * WMS-lager.
   *
   * Optionellt kan geometrin buffras (meter)
   *
   * @param geometry openlayers Feature
   * @param bufferDistance bufferavstånd i meter
   */
  getFeatureInfosByPolygon(geometry: Geometry): FeatureInfo[] {
    const extent = geometry.getExtent();
    const results: FeatureInfo[] = [];
    this.layerService.layers.getLayers().getArray()
      .filter(isQueryable)
      .filter(isVectorLayer)
      .forEach(layer => {
        const features = (<VectorSource> (<Layer> layer).getSource()).getFeatures();

        features.forEach((feat) => {
          if (!OlExtent.intersects(extent, feat.getGeometry().getExtent())) {
            return;
          }

          const featureInfo = new FeatureInfo(feat, <Layer> layer);
          results.push(featureInfo);
        });
      });

    return results;
  }

  /**
   * Returnerar ett objekt med alla funna fastigheter och features som ligger inom/berör en geometri.
   * Optionellt kan geometrin buffras (meter)
   *
   * @param geometry openlayers Feature
   * @param predicate intersects, within...
   */
  getFeaturesByPolygon(geometry: Geometry, predicate: SpatialPredicate)
    : Observable<{sokGeoJsonResult?: SokGeoJsonResult, featureInfos: FeatureInfo[]}> {
    const featureInfos = this.getFeatureInfosByPolygon(geometry);

    if (this.viewService.getCurrentZoom() >= this.minimumZoom && this.shouldFastighetsytorBeSelected()) {
      return this.sokService.findFastigheterByGeometry(geometry, predicate).pipe(
        map(sokGeoJsonResult => {
          return ({sokGeoJsonResult: sokGeoJsonResult, featureInfos: featureInfos});
        })
      );
    }

    return of({featureInfos: featureInfos});
  }

  private shouldFastighetsytorBeSelected(): boolean {
    if (ConfigService.appConfig.fastighetsSelection) {
      if (ConfigService.appConfig.selectFastighetOnlyIfLayerIsActive) {
        const fastighetsytor = this.layerService.getLayer("fastighetsytor");
        if (fastighetsytor) {
            return fastighetsytor.get("visible");
        }
      }
      return true;
    }
    return false;
  }

  /**
   * Gör ett WFS anrop mot url:en som requestLayers source går mot.
   *
   * @param requestLayer antingen ett WMS- eller WFS-lager
   * @param featureTypes de typer som ska sökas på
   * @param query ett CQL-filter
   */
  wfsQuery(requestLayer: Layer, featureTypes: string[], query: string): Observable<FeatureInfo[]> {
    const url = getOgcUrl(requestLayer);
    const queryParams = {
      "service": "WFS",
      "version": "2.0.0",
      "request": "GetFeature",
      "typeName": featureTypes.join(","),
      "cql_filter": query,
      "outputFormat": "json",
      "srsName": this.configService.config.projectionCode,
      "count": "100"
    };
    return this.http.get(url, { params: queryParams}).pipe(
      map(featureCollection => featureCollection["features"]),
      map(features => features.map(f => FeatureInfo.fromGeoJson(f, requestLayer)))
    );
  }
}


