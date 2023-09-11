import {Injectable} from "@angular/core";
import VectorSource from "ol/source/Vector";
import VectorLayer from "ol/layer/Vector";
import Draw from "ol/interaction/Draw";
import Modify from "ol/interaction/Modify";
import Snap from "ol/interaction/Snap";
import {Circle as CircleStyle, Fill, Stroke, Style} from "ol/style";
import {unByKey} from "ol/Observable";
import Overlay from "ol/Overlay";
import {getArea, getLength} from "ol/sphere";
import {MapService} from "../../../map-core/map.service";
import {LayerService} from "../../../map-core/layer.service";
import { ViewService } from "../../../map-core/view.service";
import { MeasureUnit } from "../state.service";
import { Coordinate } from "ol/coordinate";

export interface Tooltip {
  tooltipElement: Element;
  tooltipOverlay: Overlay;
}

@Injectable()
export class BaseToolService {
  protected _layer: VectorLayer<any>;
  protected _source: VectorSource;
  protected _layerStyle: Style;
  protected _drawStyle: Style;

  protected _draw: Draw;
  protected _snap: Snap;
  protected _modify: Modify;

  overlays: Overlay[] = [];

  helpTooltipElement: HTMLElement;
  helpTooltip: Overlay;

  currentMeasureTooltip: Tooltip;

  mouseposition: Coordinate;
  pointermoveDrawEditListener;

  pointermoveListener;
  measureTooltips: {[id: string]: Tooltip} = {}; // Object that maps feature ids to tooltips

  constructor(protected mapService: MapService, protected layerService: LayerService, protected viewService: ViewService) {

    this._layerStyle = new Style({
      fill: new Fill({
        color: "rgba(255,255,255,0.4)"
      }),
      stroke: new Stroke({
        color: "#3399CC",
        width: 2
      }),
    });
    this._drawStyle = new Style({
      fill: new Fill({
        color: "rgba(255, 255, 255, 0.2)"
      }),
      stroke: new Stroke({
        color: "rgba(0, 0, 0, 0.5)",
        lineDash: [10, 10],
        width: 2
      }),
      image: new CircleStyle({
        radius: 5,
        stroke: new Stroke({
          color: "rgba(0, 0, 0, 0.7)"
        }),
        fill: new Fill({
          color: "rgba(255, 255, 255, 0.2)"
        })
      })
    });
    this._source = new VectorSource();
    this._layer = new VectorLayer({
      source: this._source,
      style: this._layerStyle
    });

    this.layerService.addInternalLayer(this._layer);
    this._layer.setZIndex(6000);

    const esc_key = 27;
    document.addEventListener("keydown", (e) => {
      if (e.keyCode === esc_key && this._draw) {
        this._draw.setActive(false);
        this._draw.setActive(true);

        this.clearMeasureTooltip();
      }
    });
  }

  clear() {
    this.measureTooltips = {};
    this.clearInteractions();
    this._source.clear();
    unByKey(this.pointermoveListener);
    unByKey(this.pointermoveDrawEditListener);

    this.overlays.forEach((overlay) => {
      this.mapService.map.removeOverlay(overlay);
    });
  }

  clearInteractions() {
    this.mapService.removeInteraction(this._draw);
    this.mapService.removeInteraction(this._modify);
    this.mapService.removeInteraction(this._snap);
  }

  clearMeasureTooltip() {
    if (this.currentMeasureTooltip) {
      this.mapService.map.removeOverlay(this.currentMeasureTooltip.tooltipOverlay);
    }
  }

/**
   * Format length output.
   * @param {module:ol/geom/LineString~LineString} line The line.
   * @return {string} The formatted length.
   */
 public formatLength(line) {
  const length = getLength(line, {projection: this.getProjection()});
  let output;
  if (length > 1000) {
    output = (Math.round(length / 1000 * 1000) / 1000) +
      " " + "km";
  } else {
    output = (Math.round(length * 100) / 100) +
      " " + "m";
  }
  return output;
}

private getProjection() {
  return this.viewService.getProjection();
}

/**
 * Format area output.
 * @param {module:ol/geom/Polygon~Polygon} polygon The polygon.
 * @return {string} Formatted area.
 */
public formatArea(polygon, measureUnit) {
  const area = getArea(polygon, {projection: this.getProjection()});
  let output;

  if (measureUnit === MeasureUnit.Auto) {
    if (area > 10_000_000) {
      output = (Math.round(area / 1_000_000 * 1000) / 1000) +
        " " + "km<sup>2</sup>";
    } else if (area > 10_000) {
      output = (Math.round(area / 10_000 * 10) / 10) +
        " " + "ha";
    } else {
      output = (Math.round(area * 100) / 100) +
        " " + "m<sup>2</sup>";
    }
  } else if (measureUnit === MeasureUnit.Meter) {
    if (area > 1_000_000) {
      output = (Math.round(area / 1_000_000 * 1000) / 1000) +
          " " + "km<sup>2</sup>";
    } else {
      output = (Math.round(area * 100) / 100) +
          " " + "m<sup>2</sup>";
    }
  } else if (area > 1_000) {
    output = (Math.round(area / 10_000 * 10) / 10) +
      " " + "ha";
  } else {
    output = (Math.round(area * 100) / 100) +
      " " + "m<sup>2</sup>";
  }
  return output;
}

  protected polygonToMultiPolygon(polygon) {
    return {
      coordinates: [polygon.coordinates],
      type: "MultiPolygon"
    };
  }

  protected createHelpTooltip(message: string) {
    if (this.helpTooltipElement) {
      this.helpTooltipElement.parentNode.removeChild(this.helpTooltipElement);
    }
    this.helpTooltipElement = document.createElement("div");
    this.helpTooltipElement.className = "tooltip";
    this.helpTooltipElement.innerHTML = message;
    this.helpTooltip = new Overlay({
      element: this.helpTooltipElement,
      offset: [15, 0],
      positioning: "center-left"
    });
    this.mapService.map.addOverlay(this.helpTooltip);
    this.overlays.push(this.helpTooltip);

    if (this.pointermoveListener) {
      unByKey(this.pointermoveListener);
    }
    this.pointermoveListener = this.mapService.map.on("pointermove", this.pointerMoveHandler.bind(this));
  }

  protected createMeasureTooltip(): Tooltip {
    const measureTooltipElement = document.createElement("div");
    measureTooltipElement.className = "tooltip tooltip-measure";
    const measureTooltip = new Overlay({
      element: measureTooltipElement,
      offset: [0, -15],
      positioning: "bottom-center",
      stopEvent: false
    });
    this.mapService.map.addOverlay(measureTooltip);
    this.overlays.push(measureTooltip);

    const tooltip = {tooltipElement: measureTooltipElement, tooltipOverlay: measureTooltip};
    this.currentMeasureTooltip = tooltip;
    if (this.pointermoveDrawEditListener) {
      unByKey(this.pointermoveDrawEditListener);
    }
    this.pointermoveDrawEditListener = this.mapService.map.on("pointermove", this.pointerMoveDuringDrawEditHandler.bind(this));
    return tooltip;
  }

  protected pointerMoveHandler(evt) {
    if (evt.dragging) {
      return;
    }
    this.helpTooltip.setPosition(evt.coordinate);
  }

  protected pointerMoveDuringDrawEditHandler(evt) {
    this.mouseposition = evt.coordinate;
  }
}
