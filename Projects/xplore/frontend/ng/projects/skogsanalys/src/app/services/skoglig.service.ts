import {Injectable} from "@angular/core";
import {SkogligRequest} from "../model/skoglig-request.model";
import {ActionMode, FeatureSelectionMode, StateService} from "../../../../lib/map/services/state.service";
import MultiPolygon from "ol/geom/MultiPolygon";
import WKT from "ol/format/WKT";
import {Polygon} from "ol/geom";

@Injectable({
  providedIn: "root"
})
export class SkogligService {
  private WKT: WKT = new WKT();

  constructor(public stateService: StateService) {}

  public getSkogligRequest(): SkogligRequest {
    switch (this.stateService.getUiStates().actionMode) {
      case ActionMode.Select: {
        switch (this.stateService.getUiStates().featureSelectionMode) {
          case FeatureSelectionMode.Click: {
            return this.getRequestWithDelomrade();
          }
          case FeatureSelectionMode.Draw: {
            return this.getRequestWithWkt();
          }
        }
      }
    }
  }

  private getSelectedFeatures(): any[] {
    return this.stateService.getUiStates().valdaDelomraden;
  }

  private getRequestWithWkt(): SkogligRequest {
    const req = new MultiPolygon([]);
    this.getSelectedFeatures().forEach(f => req.appendPolygon(new Polygon(f.geometry.coordinates)));

    const wkt = this.WKT.writeGeometry(req);
    return {
      wkt: wkt,
      getId: () => wkt
    };
  }

  private getRequestWithDelomrade(): SkogligRequest {
    const selectedFeatures = this.getSelectedFeatures();

    const delomradenExtId = [];

    for (let index = 0; index < selectedFeatures.length; index++) {
      delomradenExtId.push("'" + selectedFeatures[index].properties.externid + "'");
    }
    const delomradenExtString = delomradenExtId.join(",");

    return {
      delomrade: delomradenExtString,
      getId: () => delomradenExtString
    };
  }
}
