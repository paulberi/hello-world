import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable, of} from "rxjs";
import {publish, refCount, tap} from "rxjs/operators";
import {SkogligRequest} from "../model/skoglig-request.model";

export interface SkogligaGrunddataResponse {
  areaHa: number;
  areaProduktivHa: number;
  areaSkogHa: number;
  volSum: number;
  volMedel: number;
  gyMedel: number;
  hgvMedel: number;
  dgvMedel: number;
  lov: boolean;
  referensAr: number;
  referensArIntervall: string;
  volSumExklAvv: number;
  volMedelExklAvv: number;
  hgvMedelExklAvv: number;
  gyMedelExklAvv: number;
  dgvMedelExklAvv: number;
}

@Injectable({
  providedIn: "root"
})
export class SkogligaGrunddataService {

  cache = {};

  constructor(private http: HttpClient) {
  }

  public get(request: SkogligRequest): Observable<SkogligaGrunddataResponse> {
    const id = request?.getId ? request.getId() : null;
    const cached = this.cache[id];
    if (!cached) {
      return this.http.post(environment.skogligaGrunddataUrl, request).pipe(
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
