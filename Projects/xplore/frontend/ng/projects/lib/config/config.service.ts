import proj4 from "proj4";
import {get} from "ol/proj";
import {register} from "ol/proj/proj4";
import TileGrid from "ol/tilegrid/TileGrid";
import {Injectable} from "@angular/core";
import {Observable, of} from "rxjs";
import {tap} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";

import {StateService} from "../map/services/state.service";
import {AuthConfig} from "angular-oauth2-oidc";
import ExtractorUtil from "../util/extractor.util";
import {UrlService} from "../map/services/url.service";
import {StorageItem, StorageService} from "../map-core/storage.service";
import {getLayerName} from "../map-core/layer.util";
import {GeometryType} from "../map-core/wfs.source";

export interface MapConfig {
  app: any;
  projectionCode: string;
  proj4Defs: {
    code: string;
    alias?: string;
    projection: string;
    projectionExtent: [number, number, number, number];
  }[];
  extent: [number, number, number, number];
  tileSize: [number, number];
  geolocateOnStartup: boolean;
  center: [number, number];
  zoom: number;
  zoomToFitMinResolution?: number;
  resolutions: number[];
  infoTemplates: any;
  allowedRolesTemplates?: any;
  groups: GroupDef[];
  layers: LayerDef[];
  styles: any;
  backgroundColor: string;
  thumbnailCoordinates?: [number, number];
  checkLayerVisiblity?: boolean;
  contactDef?: ContactDef;
  rightBottomPanelElements?: RightBottomPanelElements
}

export interface RightBottomPanelElements {
  exporteraKarta?: boolean;
  kontakt?: boolean;
  egenHjalp_Path?: "";
  omKarta_Path?: "";
  infoOmFastighetsgranser?: "";
  laddaNedKartmaterial?: LaddaNedKartmaterialInfo[];
}

export interface LaddaNedKartmaterialInfo {
  mapName: "";                 // T ex Skogsmark
  helaSverigeZipFileRasterPath?: "";  // T ex "/storage/bbk/helasverige/BBK_skogsmark_rev2020_raster.zip"
  helaSverigeZipFileVectorPath?: "";  // T ex "/storage/bbk/helasverige/BBK_skogsmark_rev2020_vector.zip"
  title: "";                   // T ex "Skogsmark" (Visas när man hovrar över nedladningslänken)
  lanCatalogPath: "";         // T ex "/storage/msb/skogsmark/lan/"
  kommunCatalogPath: "";      // T ex "/storage/msb/skogsmark/kommun/"
  raster: boolean;            // true om rasterdata är tillgänglig att ladda ner
  vector: boolean;             // true om vectordata är tillgänglig att ladda ner
}

export interface ContactDef {
  customerName?: "";
  address?: "";
  postalCode?: "";
  supportEmail?: "";
  supportTel?: "";
  extraInfo?: "";
}

export interface GroupDef {
  name: string;
  title: string;
  type: string;
  expanded: boolean;
  groups?: GroupDef[];
  capabilities?: CapabilitiesSourceDef;
  filter?: boolean;
  abstract?: {
    text: string;
    attribution: string;
  };
}

export interface MapKeyRow {
  code?: number|string;
  color: string;
  description: string;
}

export interface LayerDef {
  id?: string;
  title: string;
  abstract?: {
    text: string;
    attribution: string;
  };
  infoTemplate?: any;
  group?: string;
  source: SourceDef;
  style?: any;
  visible: boolean;
  opacity?: number;
  zIndex?: number;
  editable?: boolean;
  lazyLoad?: boolean; // Lazyload means that the Openlayers layer is not created until the layer is marked as visible
  checkAccess?: boolean; // Check if we have access before displaying layer
  allowedRoles?: string[]; // Roles that the user have to have to show the layer
  allowedRolesTemplate?: string;
  selectionStyleDelomraden?: string; // Stil som används för vald fastighet när det här lagret är aktivt
  selectionStyleFeatures?: string; // Stil som används för features när det här lagret är aktivt
  mapKey?: {
    title?: string;
    visible?: boolean; // default true
    legendOptions?: string; // default beroende på tema
    resolutionDependent?: boolean; // Default true. Hämta ny legend när man zoomar.
    staticKey?: MapKeyRow[]; // Manuellt skapad nyckel
  };
  __maxScaleDenominator?: number; // Value read from capabilities when loading layer. Max scale at which the layer is visible.
  __minScaleDenominator?: number; // Value read from capabilities when loading layer. Min scale at which the layer is visible.
  __zoomVisible?: boolean; // Tells whether the layer should be visible at the current zoom level
  __checkingAccess?: boolean; // Set after loading configuration to indicate that we are checking if the layer exists on the server
  __layerExists?: boolean; // Set to false after layer has been checked and found missing
  __layerProblemLoading?: boolean; // Set to true if error loading
  __currentResolution?: number;
}

export type SourceDef = WmsSourceDef | VectorSourceDef | WfsSourceDef | WmtsSourceDef | ImageSourceDef;

export interface WmsSourceDef {
  type: "tilewms";
  url?: string;
  projection?: string;
  serverType?: string;
  params: {
    "LAYERS"?: string;
    "FORMAT"?: string;
    "TRANSPARENT"?: boolean;
    "VERSION"?: string;
    "TILED"?: string;
    "STYLES"?: string;
    "CQL_FILTER"?: string;
  };
  hidpi?: boolean;
  discardLowResolutionTiles?: boolean;
}

export interface VectorSourceDef {
  type: "vector";
  url: string;
}

export interface WfsSourceDef {
  type: "wfs";
  url?: string;
  typeName: string;
  canClear?: boolean; // Tillåter funktionen att rensa hela lagret på features

  // Används om lagret är av den generella typen Geometry och man vill ange några specialtyper (så som Text)
  // som ska läggas till utöver default. Default finns typerna punkt, linje och polygon.
  customGeometryTypes?: GeometryType[];
}

export interface WmtsSourceDef {
  type: "wmts";
  url?: string;
  capabilitiesUrl: string;
  config: {
    layer: string;
  };
  ignoreHost?: boolean;
}

export interface CapabilitiesSourceDef {
  url: string;
}

export interface ImageSourceDef {
  type: "imagewms";
  url?: string;
  projection?: string;
  serverType?: string;
  params: {
    "LAYERS"?: string;
    "FORMAT"?: string;
    "TRANSPARENT"?: boolean;
    "VERSION"?: string;
    "STYLES"?: string;
    "CQL_FILTER"?: string;
  };
  hidpi?: boolean;
  discardLowResolutionTiles?: boolean;
}

export interface AppConfig {
  appName: string;
  configuration?: string;
  fastighetsSelection?: boolean;
  layerEditing?: boolean;
  exportCopyrightText?: string;
  multiSelect?: boolean;
  fastighetsNavigering?: boolean;
  selectFastighetOnlyIfLayerIsActive?: boolean;
}

export interface Query {
  cfg?: string;
  x?: number;
  y?: number;
  zoom?: number;

  // layers är gamla namnet på sparad lagerkonfig, nya är lay för att det ska gå att hantera gamla länkar
  layers?: any;
  lay?: any;
}

export interface UrlConfig {
  authUrl?: string;
  defaultConfigurationUrl?: string;
  configurationUrl: string;
  wmsUrl?: string;
  sokServiceUrl?: string;
  ownerListUrl?: string;
}

export enum PlaceMode {
  FASTIGHET = "Fastighet",
  ADDRESS = "Address",
  ORT = "Ort",
  KOMMUN = "Kommun",
  TATORT = "Tätort",
  KOORDINAT = "Koordinat"
}

@Injectable({
  providedIn: "root"
})
export class ConfigService {
  static appConfig: AppConfig;
  static urlConfig: UrlConfig;
  static authConfig: AuthConfig;

  query: Query = {};
  config: MapConfig;
  helpFile: string;
  aboutFile: string;
  infoFastighetsgransFile: string;
  defaultTileGrid;

  constructor(private http: HttpClient, private stateService: StateService, private urlService: UrlService,
              private storageService: StorageService) {
  }

  public static setAppConfig(appConfig: AppConfig, authConfig: AuthConfig, urlConfig: UrlConfig) {
    this.appConfig = appConfig;
    this.authConfig = authConfig;
    this.urlConfig = urlConfig;
  }

  public setMapConfig(mapConfig: MapConfig) {
    this.config = mapConfig;

    this.stateService.setUiStates(this.config.app.uistates);

    this.config.proj4Defs.forEach(def => {
      proj4.defs(def.code, def.projection);
      if (def.alias) {
        proj4.defs(def.alias, proj4.defs(def.code));
      }
    });

    register(proj4);

    this.config.proj4Defs.forEach(def => {
      get(def.code).setExtent(def.projectionExtent);
    });

    this.defaultTileGrid = new TileGrid({
      extent: this.config.extent,
      resolutions: this.config.resolutions,
      tileSize: this.config.tileSize
    });

    const defaultOwsUrl = ExtractorUtil.extractStringValue(ConfigProperty.DEFAULT_OWS_URL, this.config);
    const queryConfiguredBackgroundLayer = this.getQueryConfiguredBackgroundLayer();

    let configuredVisibleBackgroundLayer: LayerDef;
    let configuredVisibleBackgroundLayerNotNeeded = false;

    // Sätt standardvärden
    if (this.config.zoomToFitMinResolution == null) {
      this.config.zoomToFitMinResolution = 0.15;
    }

    this.config.layers.forEach(layer => {
      if (layer.id == null) {
        layer.id = layer.title + "." + Math.floor(1_000_000 * Math.random());
      }
      if (layer.opacity == null) {
        layer.opacity = 100;
      }
      if (layer.source.type === "tilewms" || layer.source.type === "imagewms") {
        if (layer.source.url == null) {
          layer.source.url = ConfigService.urlConfig.wmsUrl;
        }
        if (layer.source.serverType === undefined) {
          layer.source.serverType = "geoserver";
        }
      } else if (layer.source.type === "wfs") {
          if (layer.source.url == null && defaultOwsUrl) {
            layer.source.url = defaultOwsUrl;
        }
      }

      if (this.query.layers) { // Old saved format
        if (queryConfiguredBackgroundLayer && layer.group === "background") {
          layer.visible = false;
        }

        if (this.layerConfiguredVisibleOnStartup(layer)) {
          layer.visible = true;
        }
      } else { // New saved format
        // Spara ett synligt bakgrundlager (från config-filens inställning
        // så att vi kan sätta tillbaks det som synligt om den sparade konfiguration
        // inte matchar något bakgrundslager
        if (layer.group === "background" && layer.visible) {
          configuredVisibleBackgroundLayer = layer;
        }

        // Skriv över visible om vi har sparat en lagerkonfiguration
        layer.visible = this.getLayerVisibleInQuery(layer, layer.visible);

        if (layer.visible && layer.group === "background") {
          configuredVisibleBackgroundLayerNotNeeded = true;
        }
      }

      // Antingen är infoTemplate ett korrekt format objekt eller en sträng som refererar till
      // en global konfiguration.
      if (typeof layer.infoTemplate === "string") {
        layer.infoTemplate = this.config.infoTemplates[layer.infoTemplate];
      }
    });

    if (!configuredVisibleBackgroundLayerNotNeeded) {
      if (configuredVisibleBackgroundLayer) {
        configuredVisibleBackgroundLayer.visible = true;
      }
    }

    const defaultIconsUrl = ExtractorUtil.extractStringValue("app.defaultUrls.icons.url", this.config);
    if (defaultIconsUrl) {
      for (const key in this.config.styles) {
        if (this.config.styles.hasOwnProperty(key) && this.config.styles[key][0][0].hasOwnProperty("icon")) {
          this.config.styles[key][0][0].icon.src = defaultIconsUrl + "/" + this.config.styles[key][0][0].icon.src;
        }
      }
    }
  }

  getQueryConfiguredBackgroundLayer(): string {
    if (!this.query || !this.query.layers) {
      return null;
    }

    const queryLayers = this.query.layers;
    let bgLayerName;

    if (queryLayers && Object.keys(queryLayers).length) {
      const bgLayer = this.config.layers
        .filter(l => l.group === "background")
        .find(layer => queryLayers[getLayerName(layer)]);

      if (bgLayer) {
        bgLayerName = getLayerName(bgLayer);
      }
    }

    return bgLayerName;
  }

  getLayerVisibleInQuery(layerDef, defaultVisible: boolean): boolean {
    let queryLayers = null;
    if (this.query && this.query.lay
      && Object.keys(this.query.lay).length > 0) {
      queryLayers = this.query.lay;
    }

    if (queryLayers) {
      if (queryLayers[layerDef.group]) {
        if (queryLayers[layerDef.group].indexOf(layerDef.title) === -1) {
          return false;
        } else {
          return true;
        }
      } else {
        return false;
      }
    } else {
      return defaultVisible;
    }
  }

  layerConfiguredVisibleOnStartup(layer: LayerDef): boolean {
    const queryLayers = this.query.layers;
    const layerMatchPrefix = ExtractorUtil.extractStringValue(ConfigProperty.LAYER_MATCH_PREFIX, this.config);
    const name = getLayerName(layer);

    return name && (
      (layerMatchPrefix && queryLayers[name.split(layerMatchPrefix).pop()]) ||
      queryLayers[name.split(":").pop()] ||
      queryLayers[name]
    );
  }

  getHelpFile(claims): Observable<string> {
    if (!this.helpFile) {
      // The c=1 is a workaround for an incorrect cache header problem that has been fixed
      // but to get around badly cached assets in client's browsers this was added.
      return this.http.get("assets/help/help.html?c=1", {responseType: "text"}).pipe(
        tap(response => this.helpFile = response));
    } else {
      return of(this.helpFile);
    }
  }

  getOwnHelpFile(claims): Observable<string> {
    if (!this.helpFile) {
      // The c=1 is a workaround for an incorrect cache header problem that has been fixed
      // but to get around badly cached assets in client's browsers this was added.
      if ( this.config.app.rightBottomPanelElements !== undefined) {
        if (this.config.app.rightBottomPanelElements.egenHjalp_Path !== undefined && this.config.app.rightBottomPanelElements.egenHjalp_Path !== "") {
          return this.http.get(this.config.app.rightBottomPanelElements.egenHjalp_Path + "?c=1", {responseType: "text"}).pipe(
            tap(response => this.helpFile = response));
        }
      }
    } else {
      return of(this.helpFile);
    }
  }

  getAboutFile(claims): Observable<string> {
    if (!this.aboutFile) {
      // The c=1 is a workaround for an incorrect cache header problem that has been fixed
      // but to get around badly cached assets in client's browsers this was added.
      if ( this.config.app.rightBottomPanelElements !== undefined) {
        if ( this.config.app.rightBottomPanelElements.omKarta_Path !== undefined && this.config.app.rightBottomPanelElements.omKarta_Path !== "") {
          return this.http.get(this.config.app.rightBottomPanelElements.omKarta_Path + "?c=1", {responseType: "text"}).pipe( tap(response => this.aboutFile = response));
        }
      }
    } else {
      return of(this.aboutFile);
    }
  }

  getInfoFastighetsgransFile(claims): Observable<string> {
    // The c=1 is a workaround for an incorrect cache header problem that has been fixed
    // but to get around badly cached assets in client's browsers this was added.
    if (!this.infoFastighetsgransFile) {
        return this.http.get("assets/lib/info-fastighetsgrans/info-fastighetsgrans.html?c=1", {responseType: "text"}).pipe(
          tap(response => this.infoFastighetsgransFile = response));
    } else {
      return of(this.infoFastighetsgransFile);
    }
  }

  getOwnInfoFastighetsgransFile(claims): Observable<string> {
    // The c=1 is a workaround for an incorrect cache header problem that has been fixed
    // but to get around badly cached assets in client's browsers this was added.
    if (!this.infoFastighetsgransFile) {
      if ( this.config.app.rightBottomPanelElements !== undefined) {
        if ( this.config.app.rightBottomPanelElements.infoOmFastighetsgranser !== undefined && this.config.app.rightBottomPanelElements.infoOmFastighetsgranser !== "") {
          return this.http.get(this.config.app.rightBottomPanelElements.infoOmFastighetsgranser + "?c=1", {responseType: "text"}).pipe(
            tap(response => this.infoFastighetsgransFile = response));
        }
      }
    } else {
      return of(this.infoFastighetsgransFile);
    }
  }
  getConfigProperty(configProperty: ConfigProperty) {
    return (<any>ExtractorUtil.extractObject(configProperty, this.config));
  }

  getProperty(property: string, fallback?: Object): Object {
    return ExtractorUtil.extractObject(property, this.config, fallback);
  }

  getStringProperty(property: string, fallback?: string): string {
    return ExtractorUtil.extractStringValue(property, this.config, fallback);
  }

  loadInitialParameters(): void {
    const params: any = this.storageService.getItem(StorageItem.QUERY_PARAMETERS, {});

    this.query = {};
    this.query.lay = {};

    if (params.cfg) {
      ConfigService.appConfig.configuration = params.cfg;
    }
    if (params.x) {
      this.query.x = +params.x;
    }
    if (params.y) {
      this.query.y = +params.y;
    }
    if (params.zoom) {
      this.query.zoom = +params.zoom;
    }
    if (params.lay) {
      this.query.lay = params.lay;
    }
    if (params.layers) {
      this.query.layers = params.layers;
    }
  }

  getInitialLocation(): any {
    return {
      x: this.query.x,
      y: this.query.y,
      zoom: this.query.zoom
    };
  }
}

export enum ConfigProperty {
  NAVIGATION_TOOLBAR = "app.components.navigation-toolbar",
  FSOK_PANEL = "app.components.fsok-panel",
  LAYER_MATCH_PREFIX = "app.layerMatchPrefix",
  EXTENT = "extent",
  DEFAULT_URLS = "app.defaultUrls",
  DEFAULT_OWS_URL = "app.defaultUrls.ows.url",
  FASTIGHETS_SELECTION_SETTINGS = "app.selectionSettings.fastigheter",
  FASTIGHETS_NAVIGERING = "app.fastighetsNavigering"
}

