import { Injectable } from "@angular/core";
import { Extent } from "ol/extent";
import GeoJSON from "ol/format/GeoJSON";
import WKT from "ol/format/WKT";
import { fromExtent } from "ol/geom/Polygon";
import LayerGroup from "ol/layer/Group";
import Layer from "ol/layer/Layer";
import {
  ConfigService,
  LayerDef,
  MapConfig,
  WmsSourceDef,
} from "../../../../lib/config/config.service";
import { environment } from "../../environments/environment";
import {
  createImageWms,
  createTileGrid,
  createTileWms,
  imageLoadFunction,
  tileLoadFunction,
} from "../utils/map/wms-layer";

import { HttpClient } from "@angular/common/http";
import { uuid } from "../models/uuid";
import { SamradService } from "./samrad-admin.service";

const geoJsonFormat = new GeoJSON();

@Injectable({
  providedIn: "root",
})
export class SrMapService {
  mapConfig: MapConfig;
  mapLayerConfig: any;

  constructor(private configService: ConfigService, private http: HttpClient, private samradService: SamradService) {
    this.mapConfig = this.configService.config;
  }

  getIntrangFeatures(wfsUrl: string, projektId: uuid): any {
    const cqlFilter = `projekt_id='${projektId}'`;
    return this.getFeaturesAsGeoJson(wfsUrl, "markkoll:indata_view", cqlFilter)
  }

  getLayer(layerGroup: LayerGroup, id: string): Layer {
    return layerGroup.getLayersArray().find((l) => l.get("id") === id);
  }

  public getFeaturesAsGeoJson(wfsUrl: string, typeName: string, cqlFilter: string, maxFeatures: number = 10000) {
    const params = {
        service: "wfs",
        version: "2.0.0",
        request: "GetFeature",
        typeName,
        outputFormat: "application/json",
        cql_filter: cqlFilter,
        count: maxFeatures.toString()
    };

    return this.http.get(wfsUrl, { params })
  }


  getGeoJSON(projektId: uuid) {
    const params = {
      service: "wfs",
      version: "2.0.0",
      request: "GetFeature",
      typeName: "mapCMS:mapcms_geometrier",
      outputFormat: "application/json",
      CQL_FILTER: `projekt_id='${projektId}'`
  };

    return this.http.get(environment.geoserverwfsUrl, {params})
  }

  /**
   * Standardskikt från Metria Maps: Europa och MetriaBakgrundEnkel.
   * Visa ett urval av fastighetsytor genom att skicka med ett extent samt fastighetId för att tona ner omkringliggande fastigheter.
   */
  defaultBackgroundLayer(
    fastighetsytorExtent?: Extent,
    fastighetId?: string
  ): LayerDef {
    let wmsType = "tilewms";
    let layers = "Europa,MetriaBakgrundEnkel";
    let styles = null;
    let filterWkt = null;
    let cqlFilter = null;

    if (fastighetsytorExtent && fastighetId) {
      wmsType = "imagewms";
      layers = layers + ",Fastighetsytor,Fastighetsytor";
      styles = ",,fastighetsytor_tonad,fastigheter_urval";
      filterWkt = new WKT().writeGeometry(fromExtent(fastighetsytorExtent));
      cqlFilter = `INCLUDE;INCLUDE;${encodeURIComponent(
        `objekt_id NOT IN('${fastighetId}');INTERSECTS(geom, ${filterWkt})`
      )}`;
    }

    return {
      id: "defaultBackground",
      title: "Bakgrundskarta",
      group: "background",
      source: {
        type: wmsType as any,
        url: environment.metriaMapsUrl,
        params: {
          FORMAT: "image/png",
          LAYERS: layers,
          STYLES: styles,
          CQL_FILTER: cqlFilter,
          TRANSPARENT: true,
        },
        hidpi: true,
      } as WmsSourceDef,
      visible: true,
    };
  }

  /**
   * Ortofoto från Metria Maps: orto_farg_kombinerad.
   * Kan inte använda Europaskiktet pga att jpeg inte stödjer transparens.
   */
  ortoBackgroundLayer(): LayerDef {
    return {
      id: "ortoBackground",
      title: "Ortofoto",
      group: "background",
      source: {
        type: "tilewms" as any,
        url: environment.metriaMapsUrl,
        params: {
          FORMAT: "image/jpeg",
          LAYERS: "orto_farg_kombinerad",
          TRANSPARENT: false,
        },
        hidpi: true,
      } as WmsSourceDef,
      visible: false,
    };
  }

  /**
   * Intrångslager för ett visst projekt: intrang
   */
  intrangLayer(projektId: uuid, visible: boolean): LayerDef {
    return {
      id: "mapcms_intrang_projekt",
      title: "Intrång i MapCMS projekt",
      source: {
        type: "imagewms" as any,
        url: environment.geoserverUrl,
        params: {
          FORMAT: "image/png8",
          LAYERS: "mapCMS:mapcms_intrang_projekt",
          TRANSPARENT: true,
          CQL_FILTER: `projekt_id='${projektId}'`
        },
        hidpi: false
      },
      visible: visible,
      lazyLoad: !visible
    };
  }


  getLayerDef(layerId: string, visible: boolean): LayerDef {
    let layer = this.mapLayerConfig.layers.find(
      (l) => l.id === layerId
    ) as LayerDef;
    layer = this.setLayerDefaults(layer);
    layer.visible = visible;
    return layer;
  }

  getLayerDefs(group: string): LayerDef[] {
    const layers = this.mapLayerConfig.layers.filter(
      (l) => l.group === group
    ) as LayerDef[];
    return layers.map((l) => this.setLayerDefaults(l));
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
      const grid = createTileGrid(
        this.mapConfig.extent,
        this.mapConfig.resolutions,
        this.mapConfig.tileSize
      );

      layer = createTileWms(
        sourceDef.url,
        sourceDef.params,
        grid,
        tileLoadFunction(this.http),
        { hidpi: sourceDef.hidpi }
      );
    } else {
      layer = createImageWms(
        sourceDef.url,
        sourceDef.params,
        imageLoadFunction(this.http),
        { hidpi: sourceDef.hidpi }
      );
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
    layerDefs.forEach((layerDef) => layers.push(this.createLayer(layerDef)));
    return layers;
  }

  /**
   * Generera unikt Id för karta
   */
  generateId(): string {
    return Math.floor(1_000_000 * Math.random()).toString();
  }

  /**
   * Lista med bakgrundslager som Markkoll använder som standard.
   */
  getBackgroundLayers(): Layer[] {
    const backgroundLayers: Layer[] = [];
    backgroundLayers.push(this.createLayer(this.defaultBackgroundLayer()));
    backgroundLayers.push(this.createLayer(this.ortoBackgroundLayer()));
    return backgroundLayers;
  }
}
