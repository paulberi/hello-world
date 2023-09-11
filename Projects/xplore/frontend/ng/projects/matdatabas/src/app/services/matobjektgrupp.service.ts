import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Page} from "../common/page";
import {map} from "rxjs/operators";
import {ValidationResult} from "../common/validation-result";

export interface ListMatobjektgrupp {
  id: number;
  kategori: number;
  namn: string;
  beskrivning: string;
  antalMatobjekt: number;
}

export interface Matobjektgrupp {
  id: number;
  kategori: number;
  namn: string;
  kartsymbol: number;
  beskrivning: string;
  matobjekt: number[];
}

@Injectable({
  providedIn: "root"
})
export class MatobjektgruppService {
  constructor(private http: HttpClient) {
  }

  getPage(page: number, size: number, sortProperty: string, sortDirection: string, kategori?: number): Observable<Page<ListMatobjektgrupp>> {
    let query = `?page=${page}&size=${size}&sortProperty=${sortProperty}&sortDirection=${sortDirection}`;
    if (kategori != null) {
      query += `&kategori=${kategori}`;
    }
    return this.http.get<Page<ListMatobjektgrupp>>("/api/matobjektgrupp" + query);
  }

  get(id: number): Observable<Matobjektgrupp> {
    return this.http.get<Matobjektgrupp>("/api/matobjektgrupp/" + id);
  }

  post(matobjekt: Matobjektgrupp) {
    return this.http.post<Matobjektgrupp>("/api/matobjektgrupp", matobjekt);
  }

  put(id: number, matobjekt: Matobjektgrupp) {
    return this.http.put<Matobjektgrupp>("/api/matobjektgrupp/" + id, matobjekt);
  }

  delete(id) {
    return this.http.delete("/api/matobjektgrupp/" + id);
  }

  getAll(): Observable<ListMatobjektgrupp[]>  {
    return this.getPage(0, 1000, "namn", "asc").pipe(
      map((page: Page<ListMatobjektgrupp>) => page.content)
    );
  }

  namnExists(namn: string): Observable<ValidationResult> {
    return this.http.get<ValidationResult>(`/api/validation/matobjektgrupp/exists?namn=${namn}`);
  }
}
