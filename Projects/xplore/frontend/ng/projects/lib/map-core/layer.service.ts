import {Injectable} from "@angular/core";

import LayerGroup from "ol/layer/Group";
import Layer from "ol/layer/Layer";
import TileSource from "ol/source/Tile";
import WMSCapabilities from "ol/format/WMSCapabilities";

import {ConfigService, LayerDef, SourceDef} from "../config/config.service";
import {BehaviorSubject, Subject} from "rxjs";
import {WMTSLayerFactory} from "./wmts";
import {ViewService} from "./view.service";
import {TileWmsLayerFactory} from "./tilewms";
import {VectorLayerFactory} from "./vector";
import {StorageItem, StorageService} from "./storage.service";
import {tap} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";
import {LoginService} from "../oidc/login.service";
import TileWMS from "ol/source/TileWMS";
import JSURL from "jsurl/lib/jsurl";
import {ImageWmsLayerFactory} from "./imagewms";

@Injectable({
  providedIn: "root"
})
export class LayerService {
  readonly root = new LayerGroup();
  /** Lager som finns i lagerträdet */
  readonly layers = new LayerGroup();
  /** Interna lager som t.ex. sketchlager, markeringar, etc. */
  readonly internalLayers = new LayerGroup();

  private capabilitiesToCheck = new Map<string, Map<string, LayerDef[]>>();

  private layerChange: Subject<void>;

  private readonly pixelSizeOGC = 0.00028;

  constructor(private configService: ConfigService, private viewService: ViewService, private wmtsFactory: WMTSLayerFactory,
              private tileWmsFactory: TileWmsLayerFactory, private vectorLayerFactory: VectorLayerFactory,
              private imageWmsLayerFactory: ImageWmsLayerFactory,
              private storageService: StorageService, private http: HttpClient,
              private loginService: LoginService) {
    this.root.getLayers().extend([this.layers, this.internalLayers]);

    const layers = this.configService.config.layers.reverse();

    const checkLayerVisiblity = this.configService.config.checkLayerVisiblity;

    // Läs alla lagr, sätt z värde och kolla om vi behöver kontrollera om vi kan visa lagret innan det kommer i lagerlistan

    for (let z = 0; z < layers.length; z++) {
      const layerDef = layers[z];

      layerDef.zIndex = z;

      let rolesOk: boolean;

      const allowedRoles = [];

      if (layerDef.allowedRoles) {
        allowedRoles.push(...layerDef.allowedRoles);
      }

      if (layerDef.allowedRolesTemplate) {
        allowedRoles.push(...this.configService.config.allowedRolesTemplates[layerDef.allowedRolesTemplate]);
      }

      if (allowedRoles.length > 0) {
        rolesOk = false;

        const userRoles = loginService.getRoles();

        for (const allowedRole of allowedRoles) {
          if (userRoles != null && userRoles.includes(allowedRole)) {
            rolesOk = true;
          }
        }
      } else {
        rolesOk = true;
      }

      if (!rolesOk) {
        layerDef.__checkingAccess = false;
        layerDef.__layerExists = false;
      } else {
        let addTocapabilitiesToCheck = false;

        if (layerDef.checkAccess && layerDef.source != null && layerDef.source.type != null && layerDef.source.url != null) {
          layerDef.__checkingAccess = true;
          addTocapabilitiesToCheck = true;
        } else {
          this.createLayer(layerDef);
          this.storeLayer(layerDef);

          if (checkLayerVisiblity == null || checkLayerVisiblity === true) {
            addTocapabilitiesToCheck = true;
          }
        }

        if (addTocapabilitiesToCheck) {
          const type = layerDef.source.type;
          const url = layerDef.source.url;

          if (!this.capabilitiesToCheck.has(type)) {
            this.capabilitiesToCheck.set(type, new Map());
          }

          const typeMap = this.capabilitiesToCheck.get(type);

          if (!typeMap.has(url)) {
            typeMap.set(url, []);
          }

          typeMap.get(url).push(layerDef);
        }
      }
    }

    // Starta kontroll mot capabilities för dom lager som ska kontrolleras (görs asynkront)
    for (const typeToAdd of this.capabilitiesToCheck.keys()) {
      switch (typeToAdd) {
        case "wfs": {
          for (const [url, layerDefArray] of this.capabilitiesToCheck.get(typeToAdd)) {
            const capabilitiesUrl = url + "?service=wfs&version=2.0.0&request=GetCapabilities";

            this.http.get(capabilitiesUrl, {responseType: "text"}).subscribe(caps => {
              const parser = new DOMParser();
              const xmlDoc = parser.parseFromString(caps, "text/xml");

              const nameNodes = xmlDoc.querySelectorAll("FeatureTypeList FeatureType Name");

              const foundFeatureTypes = new Set<string>();

              for (let i = 0; i < nameNodes.length; i++) {
                foundFeatureTypes.add(nameNodes[i].innerHTML);
              }

              for (const layerDef of layerDefArray) {
                if (layerDef.checkAccess) {
                  if (layerDef.source.type === "wfs") {
                    if (foundFeatureTypes.has(layerDef.source.typeName)) {
                      this.createLayer(layerDef);
                      this.storeLayer(layerDef);
                      layerDef.__checkingAccess = false;
                      layerDef.__layerExists = true;
                    } else {
                      console.log("Not found in capabilities, removed from layer list: ", layerDef);
                      layerDef.__layerExists = false;
                      layerDef.__checkingAccess = false;
                    }
                  }
                }
              }

              this.recalculateVisibility();
              this.layerChange.next();
            });
          }
          break;
        }

        case "imagewms":
        case "tilewms": {
          for (const [url, layerDefArray] of this.capabilitiesToCheck.get(typeToAdd)) {
            const capabilitiesUrl = url + (url.endsWith("?") ? "" : "?") + "service=wms&version=1.3.0&request=GetCapabilities";
            const parser = new WMSCapabilities();
            this.http.get(capabilitiesUrl, {responseType: "text"}).subscribe(caps => {
              const capabilities = parser.read(caps);
              const capLayers = capabilities.Capability.Layer.Layer;

              const found = new Map<string, any>();

              capLayers.forEach(capLayer => {
                found.set(capLayer.Name, {
                  MinScaleDenominator: capLayer.MinScaleDenominator,
                  MaxScaleDenominator: capLayer.MaxScaleDenominator
                });

              });

              for (const layerDef of layerDefArray) {
                if (layerDef.source.type === "tilewms" || layerDef.source.type === "imagewms") {
                  let layersInDef = [];

                  if (layerDef.source.params.LAYERS) {
                    layersInDef = layerDef.source.params.LAYERS.split(",");
                  }

                  let someLayersIsFound = false;

                  let maxScaleDenominator = null;
                  let minScaleDenominator = null;

                  let firstFound = true;

                  for (const layerInDef of layersInDef) {
                    if (found.has(layerInDef)) {
                      someLayersIsFound = true;

                      const layerScaleDenominators = found.get(layerInDef);

                      if (firstFound) {
                        maxScaleDenominator = layerScaleDenominators.MaxScaleDenominator;
                        minScaleDenominator = layerScaleDenominators.MinScaleDenominator;

                        firstFound = false;
                      } else {
                        // Find hightest max- and lowest minScaleDenominator among composing layers.
                        // null equals unlimitied
                        if (maxScaleDenominator != null && layerScaleDenominators.MaxScaleDenominator != null) {
                          maxScaleDenominator = Math.max(maxScaleDenominator, layerScaleDenominators.MaxScaleDenominator);
                        } else {
                          maxScaleDenominator = null;
                        }

                        if (minScaleDenominator != null && layerScaleDenominators.MinScaleDenominator != null) {
                          minScaleDenominator = Math.min(minScaleDenominator, layerScaleDenominators.MinScaleDenominator);
                        } else {
                          minScaleDenominator = null;
                        }
                      }
                    }
                  }

                  if (someLayersIsFound) {
                    // Set min- and maxScaleDenominator for layer if any were found in the capabilities of composing layers.
                    if (maxScaleDenominator != null) {
                      layerDef.__maxScaleDenominator = maxScaleDenominator;
                    }
                    if (minScaleDenominator != null) {
                      layerDef.__minScaleDenominator = minScaleDenominator;
                    }

                    if (layerDef.checkAccess) {
                      this.createLayer(layerDef);
                      this.storeLayer(layerDef);
                      layerDef.__layerExists = true;
                      layerDef.__checkingAccess = false;
                    } else {
                      // Layers where created earlier
                    }

                    this.updateScaleDenominatorInLayer(layerDef);
                  } else {
                    if (layerDef.checkAccess) {
                      console.log("Not found in capabilities, removed from layer list: ", layerDef);
                      layerDef.__checkingAccess = false;
                      layerDef.__layerExists = false;
                    }
                  }
                }
              }

              this.recalculateVisibility();
              this.layerChange.next();

            });
          }
          break;
        }

        default: {
          for (const [url, layerDefArray] of this.capabilitiesToCheck.get(typeToAdd)) {
            for (const layerDef of layerDefArray) {
              if (layerDef.checkAccess) {
                layerDef.__checkingAccess = false;
                layerDef.__layerExists = true;
                this.createLayer(layerDef);
                this.storeLayer(layerDef);

                this.updateScaleDenominatorInLayer(layerDef);
              }
            }
          }
        }
      }
    }

    // For groups with a dynamic layer list, create layerdefs from the getcapabilities response
    const groups = this.configService.config.groups;
    for (let z = 0; z < groups.length; z++) {
      const groupDef = groups[z];
      if (groupDef.capabilities) {
        const url = groupDef.capabilities.url + "?service=wms&version=1.3.0&request=GetCapabilities";
        const parser = new WMSCapabilities();
        this.http.get(url, {responseType: "text"}).pipe(tap(caps => {
          const capabilities = parser.read(caps);
          const capLayers = capabilities.Capability.Layer.Layer;

          capLayers.forEach(layer => {
            const sourceDef: SourceDef = {
              type: "tilewms",
              url: groupDef.capabilities.url,
              params: {
                LAYERS: layer.Name,
                FORMAT: "image/png",
                TRANSPARENT: true
              }
            };

            const layerDef: LayerDef = {
              id: layer.Title + "." + Math.floor(1_000_000 * Math.random()),
              title: layer.Title,
              infoTemplate: "",
              group: groupDef.name,
              source: sourceDef,
              visible: false,
              opacity: 100,
              zIndex: layers.length + z,
              lazyLoad: true
            };

            layerDef.visible = configService.getLayerVisibleInQuery(layerDef, layerDef.visible);

            if (layer.MaxScaleDenominator != null) {
              layerDef.__maxScaleDenominator = layer.MaxScaleDenominator;
            }
            if (layer.MinScaleDenominator != null) {
              layerDef.__minScaleDenominator = layer.MinScaleDenominator;
            }

            if (layerDef.visible) {
              this.createLayer(layerDef);
            }

            this.updateScaleDenominatorInLayer(layerDef);

            this.configService.config.layers.push(layerDef);
          });

          this.recalculateVisibility();
          this.layerChange.next();
        })).subscribe();
      }
    }

    this.layerChange = new BehaviorSubject<void>(null);

    // Update layer visibility due to zoom level changes
    this.viewService.viewResolutionChanged$.subscribe(() => {
      this.recalculateVisibility();
    });
  }

  addInternalLayer(layer) {
    // Lite hackigt men bör inte orsaka problem...
    if (!layer.getZIndex()) {
      layer.setZIndex(5000);
    }
    this.internalLayers.getLayers().push(layer);
  }

  removeInternalLayer(layer) {
    this.internalLayers.getLayers().remove(layer);
  }


  getLayerChange() {
    return this.layerChange.asObservable();
  }

  updateLayer(layerDef: LayerDef) {
    let foundLayer = false;
    this.layers.getLayers().forEach(layer => {
      if (layer.get("id") === layerDef.id) {
        layer.setVisible(layerDef.visible);
        layer.setOpacity(layerDef.opacity);
        layer.setZIndex(layerDef.zIndex);

        this.storeLayer(layerDef);

        foundLayer = true;
      }
    });

    if (!foundLayer && layerDef.lazyLoad) {
      this.createLayer(layerDef);
      this.storeLayer(layerDef);

      this.updateScaleDenominatorInLayer(layerDef);
    }

    this.layerChange.next();
  }

  private createLayer(layerDef: LayerDef) {
    switch (layerDef.source.type) {
        case "tilewms": {
          this.addLayer(this.tileWmsFactory.create(layerDef, this), layerDef.zIndex);
          break;
        }
        case "wfs":
        case "vector": {
          this.addLayer(this.vectorLayerFactory.create(layerDef, this), layerDef.zIndex);
          break;
        }
        case "wmts": {
          this.wmtsFactory.createAsync(layerDef, this).subscribe(layer => {
            this.addLayer(layer, layerDef.zIndex);
          });
          break;
        }
        case "imagewms": {
          this.addLayer(this.imageWmsLayerFactory.create(layerDef, this), layerDef.zIndex);
          break;
        }
      }
  }

  private addLayer(layer, zIndex) {
    layer.setZIndex(zIndex);
    this.layers.getLayers().push(layer);
  }

  private storeLayer(layerDef: LayerDef) {
    const params = this.storageService.getItem(StorageItem.QUERY_PARAMETERS, {});

    if (!params.lay) {
      params.lay = {};
    }

    if (params.layers) {
      delete params.layers; // Remove old format
    }

    if (layerDef.visible) {
      if (!params.lay[layerDef.group]) {
        params.lay[layerDef.group] = [];
      }

      const paramGroup = params.lay[layerDef.group];

      if (paramGroup.indexOf(layerDef.title) === -1) {
        paramGroup.push(layerDef.title);
      }
    } else {
      if (params.lay[layerDef.group]) {
        const paramGroup = params.lay[layerDef.group];

        const index = paramGroup.indexOf(layerDef.title, 0);
        if (index > -1) {
          paramGroup.splice(index, 1);
        }

        if (paramGroup.length === 0 ) {
          delete params.lay[layerDef.group];
        }
      }
    }

    this.storageService.setItem(StorageItem.QUERY_PARAMETERS, params);
  }

  getLayer(layerId): Layer {
    return <Layer> this.layers.getLayers().getArray().find(layer => layer.get("id") === layerId);
  }

  findLayer(layerId): Layer {
    return this.findLayerHelper(this.layers.getLayers(), layerId);
  }

  private findLayerHelper(layers: any, layerId): Layer {
    for (const layer of layers.getArray()) {
      if (layer.get("id") === layerId) {
        return layer;
      }

      if (layer.getLayers) {
        const val = this.findLayerHelper(layer.getLayers(), layerId);

        if (val !== undefined) {
          return val;
        }
      }
    }

    return undefined;
  }

  getLayerByLabel(layerLabel) {
    return this.layers.getLayers().getArray().find(layer => layer.get("label") === layerLabel);
  }

  /**
   * Hämta url:en för tilen som motsvarar inskickad koordinat, upplösning.
   */
  getTileUrl(layerDef: LayerDef, coordinate, resolution) {
    const source = <TileWMS>this.getLayer(layerDef.id).getSource();

    const tileGrid = source.getTileGrid();
    const tileCoord = tileGrid.getTileCoordForCoordAndResolution(coordinate, resolution);
    const urlFunction = source.getTileUrlFunction();

    const projection = source.getProjection() || this.viewService.view.getProjection();

    return urlFunction.bind(source)(tileCoord, 1, projection);
  }

  bindToMap(map) {
    map.setLayerGroup(this.root);
  }

  getActiveLayersConfiguration(): LayerDef[] {
    return this.layers.getLayers().getArray()
      .filter(l => l.get("visible"))
      .map(l => l.get("layerDef"));
  }

  getActiveLayersConfigurationAsJsUrl(): string {
    const layers = this.getActiveLayersConfiguration();

    const groups = new Map<string, string[]>();

    for (const layer of layers) {
      let g = groups.get(layer.group);

      if (!g) {
        g = [];
        groups.set(layer.group, g);
      }

      g.push(layer.title);
    }

    const jsonObject = {};
    groups.forEach(((value, key) => {
      jsonObject[key] = value;
    }));

    return JSURL.stringify(jsonObject);
  }

  /**
   * Checks if current scale is within the min- and maxScaleDenominators of given layerDefs and sets zoomVisible accordingly.
   * @private
   */
  private recalculateVisibility() {
    if (this.configService.config.checkLayerVisiblity === false) {
      return;
    }

    const resolution = this.viewService.view.getResolution();
    const mapScale = (resolution) / this.pixelSizeOGC;

    let layersUpdated = false;

    for (const layerDef of this.configService.config.layers) {
      const layer = this.getLayer(layerDef.id);

      let scale = mapScale;

      if (layer) {
        const source = layer.getSource();

        if (source) {
          if (source instanceof TileSource) {
            const tileGrid = source.getTileGrid();

            const z = tileGrid.getZForResolution(resolution);
            const tileResolution = tileGrid.getResolution(z);

            if (layerDef.__currentResolution == null || layerDef.__currentResolution !== tileResolution) {
              layerDef.__currentResolution = tileResolution;
            }

            const tileScale = tileResolution / this.pixelSizeOGC;

            scale = tileScale;
          }
        }
      }

      let visible = true;

      if (layerDef.__minScaleDenominator != null) {
        visible = layerDef.__minScaleDenominator <= scale && visible;
      }

      if (layerDef.__maxScaleDenominator != null) {
        visible = layerDef.__maxScaleDenominator >= scale && visible;
      }

      if (visible !== layerDef.__zoomVisible) {
        layerDef.__zoomVisible = visible;

        layersUpdated = true;
      }
    }

    if (layersUpdated) {
      this.layerChange.next();
    }
  }

  private updateScaleDenominatorInLayer(layerDef: LayerDef) {
    return;
    // Uppdateringen av resolution i openlyaers-lagren verkar inte helt
    // tillförligtlig, ta bort tills det är testat mer
  //   if (this.configService.config.checkLayerVisiblity === false) {
  //     return;
  //   }
  //
  //   const layer = this.findLayer(layerDef.id);
  //
  //   if (layer) {
  //     if (layerDef.__maxScaleDenominator != null) {
  //       layer.setMaxResolution(layerDef.__maxScaleDenominator * this.pixelSizeOGC);
  //     }
  //
  //     if (layerDef.__minScaleDenominator != null) {
  //       layer.setMinResolution(layerDef.__minScaleDenominator * this.pixelSizeOGC);
  //     }
  //   }
  }
}



