import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Feature } from "ol";
import { Extent } from "ol/extent";
import GeoJSON from "ol/format/GeoJSON";
import WKT from "ol/format/WKT";
import { Geometry } from "ol/geom";
import { fromExtent } from "ol/geom/Polygon";
import LayerGroup from "ol/layer/Group";
import Layer from "ol/layer/Layer";
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import { forkJoin, of } from "rxjs";
import { Observable } from "rxjs/internal/Observable";
import { map } from "rxjs/operators";
import { KundApiService } from "../../../../../generated/markkoll-api";
import { ConfigService, LayerDef, MapConfig, WmsSourceDef } from "../../../../lib/config/config.service";
import { bufferGeometry } from "../../../../lib/map/util/feature.util";
import { environment } from "../../environments/environment";
import { buffertStyle } from "../common/map/styles";
import { createImageWms, createTileGrid, createTileWms, imageLoadFunction, tileLoadFunction } from "../common/map/wms-layer";
import { uuid } from "../model/uuid";
import { ProjektService } from "./projekt.service";

import mapLayerConfig from "../map_config.json";

const geoJsonFormat = new GeoJSON();

@Injectable({ providedIn: "root" })
export class MkMapService {
  mapConfig: MapConfig;
  mapLayerConfig: any;

  constructor(private http: HttpClient,
              private projektService: ProjektService,
              private kundApiService: KundApiService,
              private configService: ConfigService) {
                this.mapConfig = this.configService.config;
                this.mapLayerConfig = mapLayerConfig;
              }

  getFastighetFeatures(wfsUrl: string, projektId: uuid, fastighetId: uuid): Observable<Feature<Geometry>[]> {
    const cqlFilter = `fastighet_id='${fastighetId}' AND projekt_id='${projektId}'`;

    return this.getFeaturesAsGeoJson(wfsUrl, "markkoll:fastighet", cqlFilter)
               .pipe(map(geojson => geoJsonFormat.readFeatures(geojson)));
  }

  /** @deprecated */
  getFeatures(wfsUrl: string, typeName: string, cqlFilter: string, maxFeatures: number = 10000) {
      return this.getFeaturesAsGeoJson(wfsUrl, typeName, cqlFilter, maxFeatures).pipe(map(geojson => {
          return geoJsonFormat.readFeatures(geojson);
      }));
  }

  getProjektExtent(projektId: uuid): Observable<Extent> {
    return this.projektService
               .getProjektMeta(projektId)
               .pipe(map(meta => new GeoJSON().readFeatures(meta)[0].getGeometry().getExtent()));
  }

  getLayer(layerGroup: LayerGroup, id: string): Layer {
    return layerGroup.getLayersArray().find(l => l.get("id") === id);
  }

  private getFeaturesAsGeoJson(wfsUrl: string, typeName: string, cqlFilter: string, maxFeatures: number = 10000) {
    const params = {
        service: "wfs",
        version: "2.0.0",
        request: "GetFeature",
        typeName,
        outputFormat: "application/json",
        cql_filter: cqlFilter,
        count: maxFeatures.toString()
    };

    return this.http.get(wfsUrl, { params });
  }

  /**
   * Intrångslager för ett visst projekt: intrang
   */
  intrangLayer(projektId: uuid, visible: boolean): LayerDef {
    return {
      id: "intrang_projekt",
      title: "Intrång_projekt",
      source: {
        type: "imagewms" as any,
        url: environment.geoserverUrl,
        params: {
          FORMAT: "image/png8",
          LAYERS: "markkoll:intrang_projekt",
          TRANSPARENT: true,
          CQL_FILTER: `projekt_id='${projektId}'`
        },
        hidpi: false
      },
      visible: visible,
      lazyLoad: !visible
    };
  }

  /**
   * Intrångslager med föregående version för ett visst projekt: intrang_previous_version
   */
  intrangPreviousVersionLayer(projektId: uuid, visible: boolean): LayerDef {
    return {
      id: "intrangPreviousVersion",
      title: "Intrång föregående version",
      source: {
        type: "imagewms" as any,
        url: environment.geoserverUrl,
        params: {
          FORMAT: "image/png8",
          LAYERS: "markkoll:intrang_previous_version",
          TRANSPARENT: true,
          CQL_FILTER: `projekt_id='${projektId}'`
        },
        hidpi: false
      },
      visible: visible,
    };
  }

  projektintrangBuffert(projektId: uuid, visible: boolean, layer: VectorLayer<any>): void {
    const bufferedFeatures: Feature<Geometry>[] = [];

    const intrang = this.projektService.getProjektintrangWithBuffert(projektId);
    const version = this.projektService.getCurrentVersion(projektId);

    forkJoin([intrang, version]).pipe(
      map(result => {
        layer.set("id", "buffert");
        layer.setVisible(visible);
        layer.setStyle(buffertStyle);

        if (result[0]) {
          const features = new GeoJSON().readFeatures(result[0]);

          features.forEach(feature => {
            const bufferedFeature = bufferGeometry(feature.getGeometry(), result[1].buffert);
            bufferedFeatures.push(new Feature(bufferedFeature));
          });

          const vectorSource = new VectorSource({
            features: bufferedFeatures,
          });

          layer.setSource(vectorSource);
        }
    })).subscribe();
  }

  /**
   * Lager med avtalsstatus för ett visst projekt: avtalsstatus
   */
  avtalsstatusLayer(projektId: uuid): LayerDef {
    return {
      id: "avtalsstatus",
      title: "Avtalsstatus",
      source: {
        type: "imagewms" as any,
        url: environment.geoserverUrl,
        params: {
          FORMAT: "image/png8",
          LAYERS: "markkoll:avtalsstatus",
          TRANSPARENT: true,
          CQL_FILTER: `projekt_id='${projektId}'`
        },
        hidpi: false
      },
      visible: true,
    };
  }

  /**
   * Visar intrång på fastigheter i ett projekt: intrang_fastighet
   */
  intrangFastighetLayer(projektId: string): LayerDef {
    return {
      id: "intrang_fastighet",
      title: "Intrång på fastighet",
      opacity: 1,
      source: {
        type: "imagewms" as any,
        url: environment.geoserverUrl,
        params: {
          FORMAT: "image/png8",
          LAYERS: "markkoll:intrang_fastighet",
          CQL_FILTER: `projekt_id='${projektId}'`,
          TRANSPARENT: true,
        },
      },
      visible: false,
      lazyLoad: true,
    };
  }

    /**
   * Visar intrång på fastigheter i ett projekt: intrang_fastighet
   */
     intrangProjektOversiktLayer(projektId: string): LayerDef {
      return {
        id: "intrang_projekt",
        title: "Intrång på projekt",
        opacity: 1,
        source: {
          type: "imagewms" as any,
          url: environment.geoserverUrl,
          params: {
            FORMAT: "image/png8",
            LAYERS: "markkoll:intrang_projekt_oversikt",
            CQL_FILTER: `projekt_id='${projektId}'`,
            TRANSPARENT: true,
          },
        },
        visible: false,
        lazyLoad: true,
      };
    }

  /**
   * Visar mittpunkten av intrången på en fastighet: intrang_mittpunkt
   */
  intrangMittpunktLayer(
    fastighetsId: string,
    projektId: string,
  ): LayerDef {
    return {
      id: "intrang_mittpunkt",
      title: "Centrum för intrång",
      opacity: 1,
      source: {
        type: "imagewms" as any,
        url: environment.geoserverUrl,
        params: {
          FORMAT: "image/png8",
          LAYERS: "markkoll:intrang_mittpunkt",
          CQL_FILTER: `fastighet_id='${fastighetsId}' AND projekt_id='${projektId}'`,
          TRANSPARENT: true,
        },
      },
      visible: true,
      lazyLoad: true,
    };
  }

  /**
   * Fastighetsyta med gränser i Markkoll: fastighet_yta
   */
  fastighetYtaLayer(
      fastighetsId: string,
      projektId: string,
    ): LayerDef {
      return {
        id: "fastighet_yta",
        title: "Fastighetsgräns",
        opacity: 1,
        source: {
          type: "imagewms" as any,
          url: environment.geoserverUrl,
          params: {
            FORMAT: "image/png8",
            LAYERS: "markkoll:fastighet_yta",
            CQL_FILTER: `fastighet_id='${fastighetsId}' AND projekt_id='${projektId}'`,
            TRANSPARENT: true,
          },
        },
        visible: true,
        lazyLoad: true,
      };
    }

  /**
   * Standardskikt från Metria Maps: Europa och MetriaBakgrundEnkel.
   * Visa ett urval av fastighetsytor genom att skicka med ett extent samt fastighetId för att tona ner omkringliggande fastigheter.
   */
  defaultBackgroundLayer(fastighetsytorExtent?: Extent, fastighetId?: string): LayerDef {
    let layerDefOriginal = this.mapLayerConfig.layers.find(l => l.id === "defaultBackground") as LayerDef;
    let layerDef = {...layerDefOriginal};

    if (fastighetsytorExtent && fastighetId) {
      layerDef.source.type = "imagewms";
      if ("params" in layerDef.source) {
        layerDef.source.params.LAYERS = layerDef.source.params.LAYERS + ",Fastighetsytor,Fastighetsytor";
        layerDef.source.params.STYLES = ",,fastighetsytor_tonad,fastigheter_urval";
        const filterWkt = new WKT().writeGeometry(fromExtent(fastighetsytorExtent));
        layerDef.source.params.CQL_FILTER = `INCLUDE;INCLUDE;${encodeURIComponent(`objekt_id NOT IN('${fastighetId}');INTERSECTS(geom, ${filterWkt})`)}`;
      }
    }

    return layerDef;
  }

  /**
   * Mittlinjeredovisade samfälligheter från Metria Maps: Mittlinjeredovisadesamfalligheter.
   */
   mittlinjeredovisadesamfLayer(): LayerDef {
    return {
      id: "mittlinjeredovisadesamfLayer",
      title: "Mittlinjeredovisadesamfalligheter",
      source: {
        type: "imagewms" as any,
        url: environment.metriaMapsUrl,
        params: {
          FORMAT: "image/png",
          LAYERS: "Mittlinjeredovisadesamfalligheter",
          TRANSPARENT: true,
        },
        hidpi: true
      } as WmsSourceDef,
      visible: false
    };
  }

  indataLayerDef(projektId: uuid): LayerDef {
    return {
      id: "indata",
      title: "Indata",
      source: {
        type: "imagewms" as any,
        url: environment.geoserverUrl,
        params: {
          FORMAT: "image/png",
          LAYERS: "markkoll:indata_view",
          TRANSPARENT: true,
          CQL_FILTER: `projekt_id='${projektId}'`
        },
        hidpi: true
      } as WmsSourceDef,
      visible: false
    };
  }

  getLayerDef(layerId: string, visible: boolean): LayerDef {
    let layer = this.mapLayerConfig.layers.find(l => l.id === layerId) as LayerDef;
    layer = this.setLayerDefaults(layer);
    layer.visible = visible;
    return layer;
  }

  getLayerDefs(group: string): LayerDef[] {
    const layers = this.mapLayerConfig.layers.filter(l => l.group === group) as LayerDef[];
    return layers.map(l => this.setLayerDefaults(l));
  }

  private setLayerDefaults(layerDef: LayerDef): LayerDef {
    if (layerDef.source.url == undefined || layerDef.source.url.length === 0) {
      layerDef.source.url = environment.metriaMapsUrl;
    }
    return layerDef;
  }

  /**
   * Omvandla LayerDef till ett openlayers-lager.
   */
  createLayer(layerDef: LayerDef): Layer {
    let layer: Layer;
    const sourceDef = layerDef.source as WmsSourceDef;
    if (layerDef.source.type === "tilewms") {
      const grid = createTileGrid(this.mapConfig.extent, this.mapConfig.resolutions, this.mapConfig.tileSize);

      layer = createTileWms(sourceDef.url, sourceDef.params, grid, tileLoadFunction(this.http), {hidpi: sourceDef.hidpi});
    } else {
      layer = createImageWms(sourceDef.url, sourceDef.params, imageLoadFunction(this.http), {hidpi: sourceDef.hidpi});
    }

    if (layerDef.id == null) {
      layerDef.id = layerDef.title + "-" + this.generateId();
    }

    if (layerDef.opacity == null) {
      layerDef.opacity = 1;
    }

    layer.setOpacity(layerDef.opacity);
    layer.setZIndex(layerDef.zIndex);
    layer.setVisible(layerDef.visible);
    layer.set("id", layerDef.id);
    layer.set("title", layerDef.title);
    layer.set("source_type", layerDef.source.type);

    return layer;
  }
  /**
   * Skapar upp Layer för en LayerDef[]
   */
  createLayers(layerDefs: LayerDef[]): Layer[] {
    const layers: Layer[] = [];
    layerDefs.forEach(layerDef => layers.push(this.createLayer(layerDef)));
    return layers;
  }

  /**
   * Generera unikt Id för karta
   */
  generateId(): string {
    return Math.floor(1_000_000 * Math.random()).toString();
  }

  /**
   * Lista med bakgrundslager som Samråd-admin använder som standard.
   */
   getSamradAdminBackgroundLayers(): Layer[] {
    const backgroundLayers: Layer[] = [];
    backgroundLayers.push(this.createLayer(this.defaultBackgroundLayer()));
    return backgroundLayers;
  }
}

