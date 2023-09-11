import { Injectable } from "@angular/core";
import Modify, { ModifyEvent } from "ol/interaction/Modify";
import Snap from "ol/interaction/Snap";
import PointerInteraction from "ol/interaction/Pointer";
import Translate, { TranslateEvent } from "ol/interaction/Translate";
import MultiPolygon from "ol/geom/MultiPolygon";
import MultiLineString from "ol/geom/MultiLineString";
import MultiPoint from "ol/geom/MultiPoint";
import { Fill, Stroke, Style, Circle } from "ol/style";
import { LineString, Polygon } from "ol/geom";
import Collection from "ol/Collection";

import { MapService } from "../../../map-core/map.service";
import { LayerService } from "../../../map-core/layer.service";

import { BaseToolService } from "./base-tool.service";
import { StyleService } from "../style.service";
import { FeatureInfo } from "../../../map-core/feature-info.model";
import { ConfigService } from "../../../config/config.service";
import { ViewService } from "../../../map-core/view.service";
import { MeasureUnit } from "../state.service";

interface EditInstance {
  feature: any;
  interactions: PointerInteraction[];
}

@Injectable({
  providedIn: "root"
})
export class EditService extends BaseToolService {
  private editMap: Map<string, EditInstance>;

  private readonly _pointStyle: Style;
  private readonly _polygonStyle: Style[];

  constructor(protected mapService: MapService, protected layerService: LayerService, private configService: ConfigService, viewService: ViewService) {
    super(mapService, layerService, viewService);
    this.editMap = new Map<string, EditInstance>();

    this._pointStyle = new Style({
      image: new Circle({
        radius: 7,
        fill: new Fill({
          color: "rgb(19, 152, 252)"
        }),
        stroke: new Stroke({
          color: "white",
          width: 2
        })
      })
    });

    this._polygonStyle = [
      new Style({
        stroke: new Stroke({
          color: "rgb(19, 152, 252)",
          width: 4
        }),
        zIndex: 100
      }),
      new Style({
        stroke: new Stroke({
          color: "white",
          width: 4,
          lineDash: [20, 28]
        }),
        zIndex: 100
      }),
    ];
  }

  /**
   * @Input feature: openlayers feature
   * */
  public startEdit(featureInfo: FeatureInfo) {
    const feature = featureInfo.feature;
    let styleDef = [];
    if (featureInfo.layer.get("layerDef").style) {
      if (typeof featureInfo.layer.get("layerDef").style === "string") {
        styleDef = this.configService.config.styles[featureInfo.layer.get("layerDef").style][0];
      } else if (typeof featureInfo.layer.get("layerDef").style === "object"
        && featureInfo.layer.get("layerDef").style.styleTypePropertyName) {
        styleDef = [JSON.parse(feature.get(featureInfo.layer.get("layerDef").style.styleTypePropertyName)).style];
      }
    }

    let style = StyleService.createStyleArray(styleDef);

    this.clearInteractions();

    if (this.editMap.has((<any>feature).ol_uid)) {
      return;
    }

    const collection = new Collection([feature]);
    const translate = new Translate({ features: collection });
    const modify = new Modify({ features: collection });
    const snap = new Snap({ features: collection });

    // Highlights the feature
    const geometry = feature.getGeometry();
    switch (true) {
      case geometry instanceof MultiPoint:
        style.unshift(this._pointStyle);
        feature.setStyle(style);
        break;
      case geometry instanceof Polygon:
      case geometry instanceof MultiPolygon:
      case geometry instanceof LineString:
      case geometry instanceof MultiLineString:
        style = style.concat(this._polygonStyle);
        feature.setStyle(style);
        break;
    }

    modify.on("modifystart", (modifystartevt:ModifyEvent) => {
      this.setupMeasureTooltip(modifystartevt);
    });

    translate.on("translatestart", (translatestartevt:TranslateEvent) => {
      this.setupMeasureTooltip(translatestartevt);
    });

    modify.on("modifyend", (modifyendevt:ModifyEvent) => {
      this.hideMeasureTooltips(modifyendevt);
    });

    translate.on("translateend", (translateendevt:TranslateEvent) => {
      this.hideMeasureTooltips(translateendevt);
    });

    this.mapService.addInteraction(translate);
    this.mapService.addInteraction(modify);
    this.mapService.addInteraction(snap);

    const interactions = [translate, modify, snap];
    this.editMap.set((<any>feature).ol_uid, { feature: feature, interactions: interactions });
  }

  /**
   * @Input uid: ol_uid from a openlayers feature object
   * */
  public finishEdit(uid: string) {
    if (this.editMap.has(uid)) {
      const edit = this.editMap.get(uid);
      this.editMap.delete(uid);

      edit.interactions.forEach((interaction) => {
        this.mapService.removeInteraction(interaction);
      });
      edit.feature.setStyle(null);
      this.clear();
    }
  }

  private hideMeasureTooltips(evt:ModifyEvent | TranslateEvent ){
    evt.features.getArray().map(f=>{
      this.measureTooltips[(<any>f).ol_uid].tooltipElement.classList.add("hidden");
    });
  }

  private setupMeasureTooltip(evt){
    const measureTooltip = this.createMeasureTooltip();
    evt.features.getArray().map(f=>{
      this.measureTooltips[(<any>f).ol_uid] = measureTooltip;
    });
    
    evt.features.getArray().forEach(f =>
      f.getGeometry().on("change", (changeevent) => {
        const geom = changeevent.target;
        let output;
        if (geom instanceof Polygon || geom instanceof MultiPolygon) {
          output = this.formatArea(geom, MeasureUnit.Auto);
        } else if (geom instanceof LineString || geom instanceof MultiLineString) {
          output = this.formatLength(geom);
        }

        //if this is the feature whose id is used as id for the tooltip
        if(output && this.measureTooltips[(<any>f).ol_uid]){
          this.measureTooltips[(<any>f).ol_uid].tooltipElement.classList.remove("hidden");
          this.measureTooltips[(<any>f).ol_uid].tooltipElement.innerHTML = output;
          this.measureTooltips[(<any>f).ol_uid]?.tooltipOverlay.setPosition(this.mouseposition);
        }
      }));
    }
  }
    