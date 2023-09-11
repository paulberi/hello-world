import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Page} from "../common/page";

export interface Matningslogg {
  beskrivning: string;
  handelse: number;
  loggatAvId: number;
  loggatDatum: string;
}

@Injectable({
  providedIn: "root"
})
export class MatningsloggService {

  constructor(private http: HttpClient) {
  }

  getPage(matningId: number, page: number, size: number, sortProperty: string,
          sortDirection: string): Observable<Page<Matningslogg>> {
    const params = new HttpParams()
      .append("page", page.toString())
      .append("size", size.toString())
      .append("sortProperty", sortProperty)
      .append("sortDirection", sortDirection);

    return this.http.get<Page<Matningslogg>>(`/api/matningslogg/${matningId}`, {params: params});
  }

}
