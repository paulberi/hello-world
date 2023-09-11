import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {publish, refCount, tap} from "rxjs/operators";
import {environment} from "../../environments/environment";
import {SkogligRequest} from "../model/skoglig-request.model";

export interface NmdResponse {
  tallskogHa: number;
  granskogHa: number;
  barrblandskogHa: number;
  lovblandadBarrskogHa: number;
  triviallovHa: number;
  adellovHa: number;
  triviallovAdellovHa: number;
  skogHa: number;
  temporartEjSkogHa: number;
  skogVatmarkHa: number;
  oppenVatmarkHa: number;
  akermarkHa: number;
  ovrigOppenMarkHa: number;
  exploateradMarkHa: number;
  vattenHa: number;
  molnOklassatHa: number;
  Areal_raster_ha: number;
  Area_ha: number;
  referensAr: number;
  raster_status: string;
}

@Injectable({
  providedIn: "root"
})
export class NmdService {

  cache = {};

  constructor(private http: HttpClient) {
  }

  public get(request: SkogligRequest): Observable<NmdResponse> {
    const id = request.getId ? request.getId() : null;
    const cached = this.cache[id];
    if (!cached) {
      return this.http.post(environment.nmdUrl, request).pipe(
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
