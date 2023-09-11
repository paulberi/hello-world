import {Injectable} from "@angular/core";
import {boundingExtent} from "ol/extent";
import Feature from "ol/Feature";
import Draw from "ol/interaction/Draw";
import Modify from "ol/interaction/Modify";
import GeoJSON from "ol/format/GeoJSON";

import {MapService} from "../../../map-core/map.service";
import {LayerService} from "../../../map-core/layer.service";
import {
  ActionMode,
  FeatureSelectionMode,
  MeasureUnit,
  ResetPreviousSelectionOnMode,
  StateService,
  UIStates
} from "../state.service";
import {SelectionService} from "../selection.service";
import {BaseToolService} from "./base-tool.service";
import {FeatureInfo} from "../../../map-core/feature-info.model";
import {RITAD_YTA} from "../../util/fastighet.util";
import {Subject} from "rxjs";
import {StyleService} from "../style.service";
import {switchMap} from "rxjs/operators";
import {SpatialPredicate} from "../sok.service";
import VectorLayer from "ol/layer/Vector";
import {LineString, Polygon} from "ol/geom";
import {ViewService} from "../../../map-core/view.service";

@Injectable({
  providedIn: "root"
})
export class DrawService extends BaseToolService {

  private geoJson = new GeoJSON();

  private uiStates: UIStates;

  private featureDrawn: Feature;

  private selectionSubject = new Subject<Feature>();
  selectionBufferDistance = 0;

  constructor(protected mapService: MapService, protected layerService: LayerService,
              private stateService: StateService, private selectionService: SelectionService,  protected viewService: ViewService) {
    super(mapService, layerService, viewService);

    this.stateService.uiStates.subscribe((uiStates) => {
      if (this.uiStates &&
          this.uiStates.actionMode === uiStates.actionMode && this.uiStates.featureSelectionMode === uiStates.featureSelectionMode) {
        return;
      }

      this.uiStates = uiStates;
      switch (uiStates.actionMode) {
        case ActionMode.Select:
          switch (uiStates.featureSelectionMode) {
            case FeatureSelectionMode.Draw:
              this.addSelectDrawInteraction(uiStates.resetPreviousSelectionOnMode);
              break;
            case FeatureSelectionMode.DrawPolygonIntersection:
              this.addSelectWithGeometryIntersectionInteraction("Polygon");
              break;
            case FeatureSelectionMode.DrawPolygonWithin:
              this.addSelectWithGeometryIntersectionInteraction("Polygon");
              break;
            case FeatureSelectionMode.DrawLineIntersection:
              this.addSelectWithGeometryIntersectionInteraction("LineString");
              break;
            default:
              this.clear();
          }
          break;
        case ActionMode.Add:
          if (uiStates.addMode != null) {
              this.addCreateGeometryInteraction(uiStates.addMode);
          }
           break;
        default:
          this.clear();
      }
    });

    this.selectionSubject.asObservable().pipe(
      switchMap(feature => {
        const predicate = this.uiStates.featureSelectionMode === FeatureSelectionMode.DrawPolygonWithin ? SpatialPredicate.WITHIN : SpatialPredicate.INTERSECTS;
        return this.selectionService.selectFeaturesByPolygon([feature], predicate, this.selectionBufferDistance);
      })
    ).subscribe();

    this.selectionService.clearEgenritatomradeSubject$.subscribe(_ => {
      this._source.clear();
    });
  }

  private addCreateGeometryInteraction(addMode) {
    if (addMode.selectedLayer != null) {
        this.mapService.removeInteraction(this._draw);
        this.mapService.removeInteraction(this._modify);


        this.createHelpTooltip("Klicka för att börja rita");

        const layer = this.layerService.getLayer(addMode.selectedLayer.id) as VectorLayer<any>;
        this._draw = new Draw({
            source: layer.getSource(),
            style: addMode.selectedStyleType ? addMode.selectedStyleType.style : layer.getStyle(),
            type: addMode.selectedGeometryType,
            stopClick: true,
            geometryName: addMode.selectedGeometryName
        });

        this._draw.on("drawstart", (drawstartevt) => {
          const measureTooltip = this.createMeasureTooltip();
          this.measureTooltips[(<any> drawstartevt.feature).ol_uid] = measureTooltip;

          this.helpTooltipElement.classList.add("hidden");
          let tooltipCoord = (<any>drawstartevt).coordinate;

          drawstartevt.feature.getGeometry().on("change", (changeevt) => {
            const geom = changeevt.target;
            let output;
            if (geom instanceof Polygon) {
              output = this.formatArea(geom, MeasureUnit.Auto);
              tooltipCoord = geom.getInteriorPoint().getCoordinates();
            } else if (geom instanceof LineString) {
              output = this.formatLength(geom);
              tooltipCoord = geom.getLastCoordinate();
            }
            if(this.measureTooltips[(<any>drawstartevt.feature).ol_uid]){
              this.measureTooltips[(<any>drawstartevt.feature).ol_uid].tooltipElement.innerHTML = output;
              this.measureTooltips[(<any>drawstartevt.feature).ol_uid].tooltipOverlay.setPosition(tooltipCoord);
            }
          });
        });

        this._draw.on("drawend", (drawendevt) =>  {
            const layer = this.layerService.getLayer(addMode.selectedLayer.id) as VectorLayer<any>;
            const layerSource = layer.getSource();

            let i = 1;
            let unSavedId = "-" + (layerSource.getFeatures().length + i);
            let feature = layerSource.getFeatureById(unSavedId);
            while (feature != null) {
                unSavedId = "-" + (layerSource.getFeatures().length + i);
                feature = layerSource.getFeatureById(unSavedId);
                i++;
            }
            // Nedanstående rad sätter features stil-property för att stilen ska kunna sparas i DB.
            // Vi väljer att spara som JSON.
            if (addMode.selectedStyleType) {
              drawendevt.feature.setStyle((feature, resolution) => {
                    if (feature.get(addMode.selectedLayer.style.styleTypePropertyName) == null) {
                        return null;
                    }
                    return StyleService.createStyle(JSON.parse(feature.get(addMode.selectedLayer.style.styleTypePropertyName)).style);
                });
                drawendevt.feature.set(addMode.selectedLayer.style.styleTypePropertyName, JSON.stringify(StyleService.createStyleDef(addMode.selectedStyleType)));
            }

            drawendevt.feature.setId(unSavedId);
            (<any> drawendevt.feature).startEdit();
            this.mapService.removeInteraction(this._draw);
            this.stateService.setUiStates(
                {
                    valdaFeatures: [new FeatureInfo(drawendevt.feature, layer)],
                    actionMode: null,
                    addMode: {
                        selectedLayer: null,
                        selectedGeometryType: null,
                        selectedGeometryName: null,
                        selectedStyleType: null
                    }
                });
        });
        this.mapService.addInteraction(this._draw);
    }
  }

  private addSelectDrawInteraction(resetPreviousSelectionOnMode: ResetPreviousSelectionOnMode) {
    this.clearInteractions();

    this._draw = new Draw({
      source: this._source,
      style: this._drawStyle,
      type: "Polygon",
      stopClick: true
    });
    this.mapService.addInteraction(this._draw);

    this.createHelpTooltip("Klicka för att börja rita");

    this._draw.on("drawstart", () => {
      this.helpTooltipElement.classList.add("hidden");
      this._source.clear();
      this.selectionService.deselectEgenRitatOmrade();

      if (resetPreviousSelectionOnMode !== ResetPreviousSelectionOnMode.End) {
        this.selectionService.resetSelectedDelomraden();
      }
    });

    this._draw.on("drawend", (evt) =>  {
      this.helpTooltipElement.classList.remove("hidden");

      evt.feature.setId(RITAD_YTA);

      // The next feature added to the drawSource should be the newly drawn polygon
      const geoJson = this.geoJson.writeFeatureObject(evt.feature);
      // Should only be one geometry of the type Polygon, convert it to a MultiPolygon to maintain consistency
      geoJson.geometry = <any> this.polygonToMultiPolygon(geoJson.geometry);
      geoJson.properties = {bbox: boundingExtent((<any> geoJson.geometry).coordinates[0][0])};

      this.selectionService.resetBuffer();
      if (!geoJson) {
        this.selectionService.resetSelectedDelomraden();
      } else {
        this.selectionService.setSelectedDelomraden([<any>geoJson]);
      }
    });
  }

  /**
   * @Input geometryType Supported values are "Polygon" and "LineString"
   */
  private addSelectWithGeometryIntersectionInteraction(geometryType) {
    this.clear();

    this._draw = new Draw({
      source: this._source,
      style: this._drawStyle,
      type: geometryType,
      stopClick: true,
      finishCondition: () => {
        return  geometryType === "Polygon" ? (<Polygon> this.featureDrawn.getGeometry()).getArea() > 0 : (<LineString> this.featureDrawn.getGeometry()).getLength() > 0;
      },
    });
    this._modify = new Modify({source: this._source});

    this.mapService.addInteraction(this._draw);
    this.mapService.addInteraction(this._modify);

    this.createHelpTooltip("Klicka för att börja rita");

    this._draw.on("drawstart", (evt) => {
      this.helpTooltipElement.classList.add("hidden");
      this._source.clear();
      this.selectionService.resetBuffer();

      this.featureDrawn = evt.feature;

      this.selectionService.resetSelectedDelomraden();
    });

    this._draw.on("drawend", () =>  {
      this.helpTooltipElement.classList.remove("hidden");

      this.selectionSubject.next(this.featureDrawn);
    });

    this._modify.on("modifystart", () => {
      this.helpTooltipElement.classList.add("hidden");
    });

    this._modify.on("modifyend", () => {
      this.helpTooltipElement.classList.remove("hidden");

      this.selectionSubject.next(this.featureDrawn);
    });
  }
}
