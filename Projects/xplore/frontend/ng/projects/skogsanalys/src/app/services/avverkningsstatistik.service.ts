import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {publish, refCount, tap} from "rxjs/operators";
import {environment} from "../../environments/environment";
import {SkogligRequest} from "../model/skoglig-request.model";

export interface AvverkningsstatistikResponse {
  json_featuretype: string;
  Avverkningar: Avverkningsomrade[];
  statusMessage: string;
}

interface Avverkningsomrade {
  avverkningsAr: number;
  areallHa: number;
}

@Injectable({
  providedIn: "root"
})
export class AvverkningsstatistikService {

  cache = {};

  constructor(private http: HttpClient) {}

  public get(request: SkogligRequest): Observable<AvverkningsstatistikResponse> {
    const id = request.getId ? request.getId() : null;
    const cached = this.cache[id];
    if (!cached) {
      return this.http.post(environment.avverkningsstatistikUrl, request).pipe(
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
