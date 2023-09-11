import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Page} from "../common/page";
import {MatningstypMatobjekt, MatningstypMatobjektFilter} from "./matobjekt.service";

export const LARM = 0;
export const KVITTERAT = 1;

export interface Larm {
  id: number;
  matobjektNamn: string;
  matningstypNamn: string;
  matningstypId: number;
  matningstypDecimaler: number;
  matobjektTyp: string;
  matobjektFastighet: string;
  avlastDatum: string;
  varde: number;
  enhet: string;
  larmnivaNamn: string;
  typAvKontroll: number;
  gransvarde: number;
  anvandargruppNamn: string;
  status: number;
  kvitteradDatum: string;
  kvitteradAvId: number;
  kvitteradAv: string;
}

@Injectable({
  providedIn: "root"
})
export class LarmService {

  constructor(private http: HttpClient) { }

  getLarmPage(page: number, size: number, sortProperty: string, sortDirection: string, larmStatus: number,
              egnaLarm: boolean, larmTillAnvandargruppIds: number[], larmniva: number, filter?: MatningstypMatobjektFilter):
    Observable<Page<Larm>> {
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
    if (larmStatus != null) {
      params = params.append("larmStatus", String(larmStatus));
    }
    if (egnaLarm != null) {
      params = params.append("egnaLarm", String(egnaLarm));
    }
    if (larmTillAnvandargruppIds != null) {
      for (const id of larmTillAnvandargruppIds) {
        params = params.append("larmTillAnvandargruppIds", id.toString());
      }
    }
    if (larmniva != null) {
      params = params.append("larmniva", String(larmniva));
    }
    return this.http.post<Page<Larm>>("/api/larm", filter, {params});
  }

  kvittera(larm: number[]) {
    let params = new HttpParams();
    for (const id of larm) {
      params = params.append("id", id.toString());
    }
    return this.http.post(`/api/larm/kvittera`, null, {params: params});
  }
}
