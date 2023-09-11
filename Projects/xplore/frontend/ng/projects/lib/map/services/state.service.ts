import {Injectable} from "@angular/core";
import {BehaviorSubject, Subject} from "rxjs";
import {FeatureInfo} from "../../map-core/feature-info.model";
import {LayerDef} from "../../config/config.service";
import {GeometryType} from "../../map-core/wfs.source";
import {GeoJson} from "../../map-core/geojson.util";
import {Style} from "ol/style";
import {deepEqual} from "../../util/deepEqual.util";

export enum ActionMode {
  Select = "select",
  Measure = "measure",
  Add = "add",
  Edit = "edit"
}

export enum BackgroundAction {
    Track = "track"
}

export enum FeatureSelectionMode {
  Click = "click",
  Draw = "draw",
  DrawPolygonIntersection = "drawpolygonintersection",
  DrawPolygonWithin = "drawpolygonwithin",
  DrawLineIntersection = "drawlineintersection"
}

export enum ResetPreviousSelectionOnMode {
  Beginning = "beginning",
  End = "end"
}

export enum MeasureMode {
  Distance = "distance",
  Area = "area"
}

export enum MeasureUnit {
  Hectare = "hectare",
  Meter = "meter",
  Auto = "auto"
}

export enum EditMode {
  Geometry = "editGeometry"
}

export interface UIStates {
  actionMode: ActionMode;
  valjHelaFastigheter: boolean;
  featureSelectionMode: FeatureSelectionMode;
  resetPreviousSelectionOnMode: ResetPreviousSelectionOnMode;
  measureMode: MeasureMode;
  editMode: EditMode;
  visaFastighetsgranser: boolean;
  grupperaFastigheter: boolean;
  inverteraFastigheter: boolean;
  unionDelomraden: boolean;
  fastighetsgranserOpacity: number;
  valdaDelomraden: GeoJson[];
  valdaFeatures: FeatureInfo[];
  backgroundAction: BackgroundAction;
  addMode: AddMode;
  measureUnit: MeasureUnit;
}

export interface StyleType {
    type: GeometryType;
    style: Style;
}

export interface AddMode {
    selectedLayer: LayerDef;
    selectedGeometryType: GeometryType;
    selectedGeometryName: string;
    selectedStyleType?: StyleType;
}

@Injectable({
  providedIn: "root"
})
export class StateService {
  private states: UIStates = {
    actionMode: ActionMode.Select,
    featureSelectionMode: FeatureSelectionMode.Click,
    resetPreviousSelectionOnMode: ResetPreviousSelectionOnMode.Beginning,
    valjHelaFastigheter: true,
    measureMode: MeasureMode.Distance,
    measureUnit: MeasureUnit.Meter,
    editMode: EditMode.Geometry,
    visaFastighetsgranser: false,
    grupperaFastigheter: false,
    inverteraFastigheter: false,
    unionDelomraden: false,
    fastighetsgranserOpacity: 0.4,
    valdaDelomraden: [],
    valdaFeatures: [],
    backgroundAction: null,
    addMode: {
      selectedLayer: null,
      selectedGeometryType: null,
      selectedGeometryName: null,
      selectedStyleType: null
    }
  };

  uiStates: Subject<UIStates> = new BehaviorSubject(this.states);
  partialUiStates: Subject<Partial<UIStates>> = new BehaviorSubject({});

  constructor() {
  }

  public setUiStates(updatedStates: Partial<UIStates>) {
    const newStates = {...this.states, ...updatedStates};
    if (!deepEqual(this.states, newStates)) {
      this.states = newStates;
      this.uiStates.next(this.states);
      this.partialUiStates.next(updatedStates);
    }
  }

  public getUiStates(): UIStates {
    return Object.assign({}, this.states);
  }
}
