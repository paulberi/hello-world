import {Injectable} from "@angular/core";
import Draw from "ol/interaction/Draw";
import Modify from "ol/interaction/Modify";
import Snap from "ol/interaction/Snap";
import {unByKey} from "ol/Observable";

import {LineString, Polygon} from "ol/geom";

import {MapService} from "../../../map-core/map.service";
import {LayerService} from "../../../map-core/layer.service";
import {ActionMode, MeasureMode, MeasureUnit, StateService, UIStates} from "../state.service";
import {BaseToolService, Tooltip} from "./base-tool.service";
import {ViewService} from "../../../map-core/view.service";

@Injectable({
  providedIn: "root"
})
export class MeasureService extends BaseToolService {
  

  private uiStates: UIStates;

  constructor(protected mapService: MapService, protected layerService: LayerService,
              protected stateService: StateService, protected viewService: ViewService) {
    super(mapService, layerService, viewService);

    this.uiStates = stateService.getUiStates();

    this.stateService.uiStates.subscribe((uiStates) => {
      if (this.uiStates.actionMode === uiStates.actionMode && this.uiStates.measureMode === uiStates.measureMode) {
        return;
      }

      this.clearMeasureTooltip();

      this.uiStates = uiStates;
      if (uiStates.actionMode === ActionMode.Measure) {
        if (uiStates.measureMode === MeasureMode.Area) {
            this.addInteraction("Polygon", uiStates.measureUnit);
        } else if (uiStates.measureMode === MeasureMode.Distance) {
            this.addInteraction("LineString");
        }
      } else {
        this.clear();
      }
    });
  }

  addInteraction(type, measureUnit = MeasureUnit.Meter) {
    this.clearInteractions();

    this._draw = new Draw({
      source: this._source,
      style: this._drawStyle,
      type: type,
      stopClick: true
    });
    this._modify = new Modify({source: this._source});
    this._snap = new Snap({source: this._source});

    this.mapService.addInteraction(this._draw);
    this.mapService.addInteraction(this._modify);
    this.mapService.addInteraction(this._snap);

    this.createHelpTooltip("Klicka för att börja rita");

    if (this.pointermoveListener) {
      unByKey(this.pointermoveListener);
    }
    this.pointermoveListener = this.mapService.map.on("pointermove", this.pointerMoveHandler.bind(this));

    this._draw.on("drawstart", (evt) => {
      const measureTooltip = this.createMeasureTooltip();
      this.measureTooltips[(<any> evt.feature).ol_uid] = measureTooltip;

      this.helpTooltipElement.classList.add("hidden");
      let tooltipCoord = (<any>evt).coordinate;

      evt.feature.getGeometry().on("change", (evt2) => {
        const geom = evt2.target;
        let output;
        if (geom instanceof Polygon) {
          output = this.formatArea(geom, measureUnit);
          tooltipCoord = geom.getInteriorPoint().getCoordinates();
        } else if (geom instanceof LineString) {
          output = this.formatLength(geom);
          tooltipCoord = geom.getLastCoordinate();
        }
        this.measureTooltips[(<any>evt.feature).ol_uid].tooltipElement.innerHTML = output;
        this.measureTooltips[(<any>evt.feature).ol_uid].tooltipOverlay.setPosition(tooltipCoord);
      });
    });

    this._draw.on("drawend", (evt) =>  {
      this.helpTooltipElement.classList.remove("hidden");
      this.currentMeasureTooltip = null;

      this.measureTooltips[(<any>evt.feature).ol_uid].tooltipElement.className = "tooltip tooltip-static";
      this.measureTooltips[(<any>evt.feature).ol_uid].tooltipOverlay.setOffset([0, -7]);
    });

    this._modify.on("modifystart", () => {
      this.helpTooltipElement.classList.add("hidden");
    });

    this._modify.on("modifyend", () => {
      this.helpTooltipElement.classList.remove("hidden");
    });
  }

  clear() {
    super.clear();
    this.measureTooltips = {};
  }

  clearDrawings() {
    this._source.clear();

    this.overlays.forEach((overlay) => {
      if (this.currentMeasureTooltip && overlay === this.currentMeasureTooltip.tooltipOverlay ||
          overlay === this.helpTooltip) {
        return;
      }
      this.mapService.map.removeOverlay(overlay);
    });
  }

  


}
