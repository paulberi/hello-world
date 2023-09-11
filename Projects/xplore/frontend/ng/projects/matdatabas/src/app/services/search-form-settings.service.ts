import {Injectable} from "@angular/core";
import Polygon from "ol/geom/Polygon";
import {GeoJSON} from "ol/format";
import {ListMatobjektgrupp} from "./matobjektgrupp.service";

@Injectable({
  providedIn: "root"
})
export class SearchFormSettingsService {
  private searchFormKey = "search-form-values";
  private searchFormPolygonKey = "search-form-polygon";
  private searchFormMatningstypSelectionKey = "search-form-matningstyp-selection";

  getStoredSearchForm(): {form: any, selectedMatobjektgrupper: ListMatobjektgrupp[]} {
    return JSON.parse(sessionStorage.getItem(this.searchFormKey));
  }

  putStoredSearchForm(value: any, selectedMatobjektgrupper: ListMatobjektgrupp[]) {
    sessionStorage.setItem(this.searchFormKey, JSON.stringify({form: value, selectedMatobjektgrupper: selectedMatobjektgrupper}));
  }

  putStoredSearchPolygon(value: Polygon) {
    if (value == null) {
      sessionStorage.removeItem(this.searchFormPolygonKey);
      return;
    }

    const parser = new GeoJSON();
    const geojson = parser.writeGeometryObject(value);
    sessionStorage.setItem(this.searchFormPolygonKey, JSON.stringify(geojson));
  }

  getStoredSearchPolygon(): Polygon {
    const stored = sessionStorage.getItem(this.searchFormPolygonKey);

    if (stored == null || stored === "") {
      return null;
    }

    const parser = new GeoJSON();
    const polygon = parser.readGeometry(JSON.parse(stored));

    return <Polygon>polygon;
  }

  putStoredMatningstypSelection(value: number[]) {
    sessionStorage.setItem(this.searchFormMatningstypSelectionKey, JSON.stringify(value));
  }

  getStoredMatningstypSelection(): number[] {
    return JSON.parse(sessionStorage.getItem(this.searchFormMatningstypSelectionKey));
  }
}
