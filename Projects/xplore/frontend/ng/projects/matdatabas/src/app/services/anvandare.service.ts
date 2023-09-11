import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Page} from "../common/page";
import {ValidationResult} from "../common/validation-result";

export interface Anvandare {
  id: number;
  namn: string;
  foretag: string;
  aktiv: boolean;
  inloggningsnamn: string;
  behorighet: number;
  defaultKartlagerId: number;
  skickaEpost: boolean;
  epost: string;
  inloggadSenast: string;
  anvandargrupper: number[];
}

@Injectable({
  providedIn: "root"
})
export class AnvandareService {
  constructor(private http: HttpClient) {
  }

  getPage(page: number, size: number, sortProperty: string, sortDirection: string, visaInaktiva: boolean): Observable<Page<Anvandare>> {
    const query = `?page=${page}&size=${size}&sortProperty=${sortProperty}&sortDirection=${sortDirection}&visaInaktiva=${visaInaktiva}`;
    return this.http.get<Page<Anvandare>>("/api/anvandare" + query);
  }

  get(id: number): Observable<Anvandare> {
    return this.http.get<Anvandare>("/api/anvandare/" + id);
  }

  post(anvandare: Anvandare) {
    return this.http.post<Anvandare>("/api/anvandare", anvandare);
  }

  put(id: number, anvandare: Anvandare) {
    return this.http.put<Anvandare>("/api/anvandare/" + id, anvandare);
  }

  delete(id) {
    return this.http.delete("/api/anvandare/" + id);
  }

  namnExists(anvandarnamn: string): Observable<ValidationResult> {
    return this.http.get<ValidationResult>(`/api/validation/anvandare/exists?anvandarnamn=${anvandarnamn}`);
  }

  anonymize(id: number) {
    return this.http.put(`/api/anvandare/${id}/anonymisera`, null);
  }
}
