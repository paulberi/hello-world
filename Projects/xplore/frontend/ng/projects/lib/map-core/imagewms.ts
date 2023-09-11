import {ConfigService, LayerDef} from "../config/config.service";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {LayerService} from "./layer.service";
import ImageWMS from "ol/source/ImageWMS";
import ImageLayer from "ol/layer/Image";

@Injectable({
  providedIn: "root"
})
export class ImageWmsLayerFactory {
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
  imageLoadFunction(source: ImageWMS, image, src) {
    this.http.get(src, {responseType: "blob"}).subscribe(resp => {
        image.getImage().src = URL.createObjectURL(resp);
      },
      (error) => {
        source.dispatchEvent("imageloaderror");
      });
  }

  create(layerDef: LayerDef, layerService: LayerService) {
    if (layerDef.source.type !== "imagewms") {
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

    const source = new ImageWMS({
      url: sourceDef.url,
      serverType: serverType,
      params: {TRANSPARENT: false, ...sourceDef.params},
      hidpi: hidpi,
      projection: sourceDef.projection
    });

    source.setImageLoadFunction(this.imageLoadFunction.bind(this, source));

    source.on("imageloaderror", () => {
      if (layerDef.__layerProblemLoading !== true) {
        layerDef.__layerProblemLoading = true;
        layerService.updateLayer(layerDef);
      }
    });

    source.on("imageloadend", () => {
      if (layerDef.__layerProblemLoading !== false) {
        layerDef.__layerProblemLoading = false;
        layerService.updateLayer(layerDef);
      }
    });

    const imageLayer = new ImageLayer({source: source});
    imageLayer.set("id", layerDef.id);
    imageLayer.set("layerDef", layerDef);
    imageLayer.setVisible(layerDef.visible);
    return imageLayer;
  }
}
