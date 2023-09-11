import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Page} from "../common/page";

export class SystemloggSearchParams {
  fromDatum: string;
  tomDatum: string;
  handelse = -1;

  isFromDatum(): boolean {
    return this.fromDatum != null && this.fromDatum.length > 0;
  }

  isTomDatum(): boolean {
    return this.tomDatum != null && this.tomDatum.length > 0;
  }

  isHandelse(): boolean {
    return this.handelse != null && this.handelse >= 0;
  }
}

export interface Systemlogg {
  beskrivning: string;
  handelse: number;
  anvandarnamn: string;
  datum: string;
}

@Injectable({
  providedIn: "root"
})
export class SystemloggService {

  constructor(private http: HttpClient) {
  }

  getPage(page: number, size: number, sortProperty: string, sortDirection: string,
          searchParams: SystemloggSearchParams): Observable<Page<Systemlogg>> {
    let params = new HttpParams()
      .append("page", page.toString())
      .append("size", size.toString())
      .append("sortProperty", sortProperty)
      .append("sortDirection", sortDirection);
    if (searchParams.isFromDatum()) {
      params = params.append("fromDatum", searchParams.fromDatum);
    }
    if (searchParams.isTomDatum()) {
      params = params.append("tomDatum", searchParams.tomDatum);
    }
    if (searchParams.isHandelse()) {
      params = params.append("handelse", searchParams.handelse.toString());
    }
    return this.http.get<Page<Systemlogg>>("/api/systemlogg", {params: params});
  }

}
