import { Injectable } from "@angular/core";
import {MatningstypMatobjektFilter} from "./matobjekt.service";
import {Observable} from "rxjs";
import {Page} from "../common/page";
import {HttpClient, HttpParams} from "@angular/common/http";
export interface Paminnelse {
  matningstypId: number;
  matningstypNamn: string;
  matobjektId: number;
  matobjektNamn: string;
  matobjektTyp: string;
  matobjektFastighet: string;
  matobjektLage: string;
  avlastDatum: string;
  antalGanger: number;
  tidsenhet: number;
  forsenadDagar: number;
}

@Injectable({
  providedIn: "root"
})
export class PaminnelseService {

  constructor(private http: HttpClient) { }

  getPaminnelsePage(page: number, size: number, sortProperty: string, sortDirection: string,
                    onlyForsenade: boolean, filter?: MatningstypMatobjektFilter):
    Observable<Page<Paminnelse>> {
    let params = new HttpParams();

    if (page != null) {
      params = params.append("page", String(page));
    }
    if (size != null) {
      params = params.append("size", String(size));
    }
    if (sortProperty != null) {
      params = params.append("sortProperty", sortProperty);
    }
    if (sortDirection != null) {
      params = params.append("sortDirection", sortDirection);
    }
    if (onlyForsenade != null) {
      params = params.append("onlyForsenade", String(onlyForsenade));
    }

    return this.http.post<Page<Paminnelse>>("/api/paminnelser", filter, {params});
  }

}
