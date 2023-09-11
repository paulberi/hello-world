import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {publish, refCount, tap} from "rxjs/operators";
import {environment} from "../../environments/environment";
import {SkogligRequest} from "../model/skoglig-request.model";

export interface HuggningsklasserResponse {
  medel: number;
  referensAr: number;
  referensArIntervall: string;
  lov: boolean;
  arealUngskogHaExklAvv: number;
  arealGallringsskogHaExklAvv: number;
  arealSlutavverkningsskogHaExklAvv: number;
  volUngskogExklAvv: number;
  volGallringsskogExklAvv: number;
  volSlutavverkningsskogExklAvv: number;
}

export interface HuggningsklasserV2Response {
  medel: number;
  referensAr: number;
  lov: boolean;
  arealUngskogHaExklAvv: number;
  arealGallringsskogHaExklAvv: number;
  arealSlutavverkningsskogHaExklAvv: number;
  referensArIntervall: string;
  volUngskogExklAvv: number;
  volGallringsskogExklAvv: number;
  volSlutavverkningsskogExklAvv: number;
  Region: string;
  areal0to3HaExklAvv: number;
  areal3to10HaExklAvv: number;
  areal10to15HaExklAvv: number;
  areal15to20HaExklAvv: number;
  areal20to25HaExklAvv: number;
  arealGe25HaExklAvv: number;
  vol0to3ExklAvv: number;
  vol3to10ExklAvv: number;
  vol10to15ExklAvv: number;
  vol15to20ExklAvv: number;
  vol20to25ExklAvv: number;
  volGe25ExklAvv: number;
}

@Injectable({
  providedIn: "root"
})
export class HuggningsklasserService {

  cache = {};

  constructor(private http: HttpClient) {
  }

  // Sends the polygon to the backend service returns an observable
  public get(request: SkogligRequest): Observable<HuggningsklasserResponse> {
    const id = request.getId ? request.getId() : null;
    const cached = this.cache[id];
    if (!cached) {
      return this.http.post(environment.huggningsklasserUrl, request).pipe(
        tap((result: any) => {
          if (id) {
            this.cache[id] = result;
          }
        }),
        publish(),
        refCount()
      );
    } else {
      return of(cached);
    }

  }

}
