import {ConfigService, LayerDef, SourceDef} from "../config/config.service";
import VectorSource from "ol/source/Vector";
import VectorLayer from "ol/layer/Vector";
import GeoJSON from "ol/format/GeoJSON";
import {StyleService} from "../map/services/style.service";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {WfsSource} from "./wfs.source";
import {ViewService} from "./view.service";
import {LayerService} from "./layer.service";

@Injectable({
  providedIn: "root"
})
export class VectorLayerFactory {
  private geojson = new GeoJSON({extractGeometryName: true});

  constructor(private configService: ConfigService, private viewService: ViewService, private http: HttpClient) {
  }

  create(layerDef: LayerDef, layerService: LayerService) {
    const source = this.makeSource(layerDef, layerService);
    const layer = new VectorLayer({source: source, visible: layerDef.visible});

    if (layerDef.style) {
      if (layerDef.style.styleTypePropertyName != null) {
          layer.setStyle((feature, resolution) => {
            if (feature.get(layerDef.style.styleTypePropertyName) == null) {
              return null;
            }
            return StyleService.createStyle(JSON.parse(feature.get(layerDef.style.styleTypePropertyName)).style);
          });
      } else {
        const styleDef = this.configService.config.styles[layerDef.style][0];
        if (styleDef) {
          layer.setStyle(StyleService.createStyleArray(styleDef));
        } else {
          console.log("Okänd stil " + layerDef.style);
        }
      }
    }

    layer.set("id", layerDef.id);
    layer.set("layerDef", layerDef);
    return layer;
  }

  private makeSource(layerDef: LayerDef, layerService: LayerService) {
    const sourceDef = layerDef.source;

    switch (sourceDef.type) {
      case "wfs":
        return new WfsSource(this.http, layerDef, layerService, {
          url: sourceDef.url,
          typeName: sourceDef.typeName,
          projection: this.viewService.view.getProjection(),
          canClear: sourceDef.canClear
        });
      case "vector": {
        return new VectorSource({
          format: this.geojson,
          url: sourceDef.url
        });
      }
      default: {
        throw new Error("Wrong source type");
      }
    }
  }
}
