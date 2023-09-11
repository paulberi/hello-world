import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {publish, refCount, tap} from "rxjs/operators";
import {environment} from "../../environments/environment";
import {SkogligRequest} from "../model/skoglig-request.model";

export interface Skyddsform {
  skyddsform: string;
}

export interface SkyddatOmrade {
  skyddsform: string;
  id: string;
  namn: string;
  areaHa: number;
  url?: string;
}

export interface TotalAreaSkyddsform {
  skyddsform: string; 
  areaHa: number; 
}

export interface SkyddadeOmradenResponse {
  skyddadeOmraden: SkyddatOmrade[];
  kontrolleradeSkyddsformer: Skyddsform[];
  totalareaSkyddsform: TotalAreaSkyddsform[];
  totalareaSkyddadeOmraden : number; 
}

@Injectable({
  providedIn: "root"
})
export class SkyddadeOmradenService {

  cache = {};

  constructor(private http: HttpClient) {
  }

  // Sends the polygon to the backend service returns an observable
  public get(request: SkogligRequest): Observable<SkyddadeOmradenResponse> {
    const id = request.getId ? request.getId() : null;
    const cached = this.cache[id];
    if (!cached) {
      return this.http.post(environment.skyddadeOmradenUrl, request).pipe(
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
