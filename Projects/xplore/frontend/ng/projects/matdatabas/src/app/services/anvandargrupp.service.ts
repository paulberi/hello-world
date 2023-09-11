import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Page} from "../common/page";
import {map} from "rxjs/operators";
import {Anvandare} from "./anvandare.service";
import {ValidationResult} from "../common/validation-result";
import {Larm} from "./larm.service";

export interface Anvandargrupp {
  id: number;
  namn: string;
  beskrivning: string;
  antalAnvandare: number;
}

export interface Matansvar {
  matobjekt: string;
  matobjektId: number;
  matningstyp: string;
  fastighet: string;
}

@Injectable({
  providedIn: "root"
})
export class AnvandargruppService {
  constructor(private http: HttpClient) {
  }

  getPage(page: number, size: number, sortProperty: string, sortDirection: string): Observable<Page<Anvandargrupp>> {
    const query = `?page=${page}&size=${size}&sortProperty=${sortProperty}&sortDirection=${sortDirection}`;
    return this.http.get<Page<Anvandargrupp>>("/api/anvandargrupp" + query);
  }

  get(id: number): Observable<Anvandargrupp> {
    return this.http.get<Anvandargrupp>("/api/anvandargrupp/" + id);
  }

  getMatansvar(id: number, page: number, size: number, sortProperty: string, sortDirection: string): Observable<Page<Matansvar>> {
    const query = `?page=${page}&size=${size}&sortProperty=${sortProperty}&sortDirection=${sortDirection}`;
    return this.http.get<Page<Matansvar>>("/api/anvandargrupp/" + id + "/matansvar" + query);
  }

  getAnvandare(id: number, page: number, size: number, sortProperty: string, sortDirection: string): Observable<Page<Anvandare>> {
    const query = `?page=${page}&size=${size}&sortProperty=${sortProperty}&sortDirection=${sortDirection}`;
    return this.http.get<Page<Anvandare>>("/api/anvandargrupp/" + id + "/anvandare" + query);
  }

  put(id: number, anvandargrupp: Anvandargrupp) {
    return this.http.put("/api/anvandargrupp/" + id, anvandargrupp);
  }

  post(anvandargrupp: Anvandargrupp) {
    return this.http.post<Anvandargrupp>("/api/anvandargrupp", anvandargrupp);
  }

  delete(id: number) {
    return this.http.delete("/api/anvandargrupp/" + id);
  }

  canDelete(id: number): Observable<boolean> {
    return this.http.options("/api/anvandargrupp/" + id, {observe: "response"}).pipe(
      map(reponse => reponse.headers.has("Allow") && reponse.headers.get("Allow").includes("DELETE"))
    );
  }

  getAll(): Observable<Anvandargrupp[]>  {
    return this.getPage(0, 1000, "namn", "asc").pipe(
      map((page: Page<Anvandargrupp>) => page.content)
    );
  }

  namnExists(namn: string): Observable<ValidationResult> {
    return this.http.get<ValidationResult>(`/api/validation/anvandargrupp/exists?namn=${namn}`);
  }
}
