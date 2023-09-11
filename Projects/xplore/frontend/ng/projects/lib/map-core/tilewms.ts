import {ConfigService, LayerDef} from "../config/config.service";
import TileWMS from "ol/source/TileWMS";
import TileLayer from "ol/layer/Tile";
import TileState from "ol/TileState";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {LayerService} from "./layer.service";

@Injectable({
  providedIn: "root"
})
export class TileWmsLayerFactory {
  constructor(private http: HttpClient, private configService: ConfigService) {
  }

  /**
   * Vi använder en custom-funktion för att ladda kartbilder. Som standard ser funktionen ut ungefär så här:
   *
   * image.getImage().src = src;
   *
   * Då har vi ingen möjlighet ett lägga på egna headers, t.ex. Authorization. Den här funktionen använder
   * angular's HttpClient som är konfigurerad att lägga på tokens för utvalda endpoints (se app.module.ts).
   */
  tileLoadFunction(image, src) {
    this.http.get(src, {responseType: "blob"}).subscribe(resp => {
        image.getImage().src = URL.createObjectURL(resp);
      },
      (error) => {
        image.setState(TileState.ERROR);
      });
  }

  create(layerDef: LayerDef, layerService: LayerService) {
    if (layerDef.source.type !== "tilewms") {
      throw new Error("Wrong source type");
    }

    const sourceDef = layerDef.source;

    let hidpi;
    let serverType;

    // Set server type for the servertypes that is supported per OpenLayers documentation
    switch (sourceDef.serverType) {
      case "mapserver":
      case "geoserver":
      case "qgis":
        serverType = sourceDef.serverType;
        hidpi = sourceDef.hidpi !== false;  // Default hidpi to true
        break;

      default:
        hidpi = false;
        serverType = undefined;
    }

    const source = new TileWMS({
      urls: [sourceDef.url],
      serverType: serverType,
      params: {TRANSPARENT: false, ...sourceDef.params},
      tileGrid: this.configService.defaultTileGrid,
      hidpi: hidpi,
      tileLoadFunction: this.tileLoadFunction.bind(this),
      projection: sourceDef.projection
    });

    source.on("tileloaderror", () => {
      if (layerDef.__layerProblemLoading !== true) {
        layerDef.__layerProblemLoading = true;
        layerService.updateLayer(layerDef);
      }
    });

    source.on("tileloadend", () => {
      if (layerDef.__layerProblemLoading !== false) {
        layerDef.__layerProblemLoading = false;
        layerService.updateLayer(layerDef);
      }
    });

    const tileLayer = new TileLayer({source: source});
    tileLayer.set("id", layerDef.id);
    tileLayer.set("layerDef", layerDef);
    tileLayer.setVisible(layerDef.visible);
    return tileLayer;
  }
}
