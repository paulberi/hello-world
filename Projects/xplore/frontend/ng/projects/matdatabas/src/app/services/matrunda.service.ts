import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Page} from "../common/page";
import {ValidationResult} from "../common/validation-result";
import {map} from "rxjs/operators";
import {Matning} from "./matobjekt.service";

export interface ListMatrunda {
  id: number;
  namn: string;
  beskrivning: string;
  aktiv: boolean;
  antalMatobjekt: number;
}

export interface Matrunda {
  id: number;
  namn: string;
  beskrivning: string;
  aktiv: boolean;
  matningstyper: MatrundaMatningstyp[];
}

export interface MatrundaMatningstyp {
  matningstypId: number;
  ordning: number;
}

export interface RapporteringMatningstyp {
  matobjektId: number;
  matningstypId: number;
  matobjekt: string;
  matningstyp: string;
  berakningstyp?: string;
  lage: string;
  fastighet: string;
  bifogadBildId: number;
  bifogadBildLink: string;
  bifogadBildThumbnail: string;
  enhet: string;
  storhet?: string;
  automatiskInrapportering: boolean;
  instrument: string;
  varde1: string;
  varde2: string;
  varde3: string;
  status1: number;
  status2: number;
  status3: number;
  maxPejlbartDjup: number;
  fixpunkt: string;
  senasteAvlastDatum: string;
}

@Injectable({
  providedIn: "root"
})
export class MatrundaService {

  constructor(private http: HttpClient) {
  }

  getPage(page: number, size: number, sortProperty: string, sortDirection: string, opts = {onlyAktiva: false}):
    Observable<Page<ListMatrunda>> {
    const params = new HttpParams()
      .append("page", page.toString())
      .append("size", size.toString())
      .append("sortProperty", sortProperty)
      .append("sortDirection", sortDirection)
      .append("onlyAktiva", opts.onlyAktiva ? "true" : "false");
    return this.http.get<Page<ListMatrunda>>("/api/matrunda", {params: params});
  }

  getAll(opts = {onlyAktiva: false}): Observable<ListMatrunda[]>  {
    return this.getPage(0, 1000, "namn", "asc", opts).pipe(
      map((page: Page<ListMatrunda>) => page.content)
    );
  }

  get(id: number): Observable<Matrunda> {
    return this.http.get<Matrunda>("/api/matrunda/" + id);
  }

  post(matrunda: Matrunda) {
    return this.http.post<Matrunda>("/api/matrunda", matrunda);
  }

  put(id: number, matrunda: Matrunda) {
    return this.http.put<Matrunda>("/api/matrunda/" + id, matrunda);
  }

  delete(id) {
    return this.http.delete("/api/matrunda/" + id);
  }

  namnExists(namn: string): Observable<ValidationResult> {
    return this.http.get<ValidationResult>(`/api/validation/matrunda/exists?namn=${namn}`);
  }

  getMatrundaMatningstyper(matrundaId: number, startDate?: string): Observable<RapporteringMatningstyp[]> {
    let params = new HttpParams();
    if (startDate != null) {
      params = params.append("startDate", startDate);
    }
    return this.http.get<RapporteringMatningstyp[]>("/api/matrunda/" + matrundaId + "/matningstyper", {params: params});
  }

  getMatrundaFromMatobjektMatningstyper(matobjektId: number, startDate?: string): Observable<RapporteringMatningstyp[]> {
    let params = new HttpParams();
    if (startDate != null) {
      params = params.append("startDate", startDate);
    }
    return this.http.get<RapporteringMatningstyp[]>("/api/matrunda/matobjekt/" + matobjektId + "/matningstyper", {params: params});
  }

  getSenasteMatningar(matrundaId: number): Observable<Map<string, Matning>> {
    const params = new HttpParams();
    return this.http.get<Map<string, Matning>>("/api/matrunda/" + matrundaId + "/senastematningar", {params: params});
  }
}
