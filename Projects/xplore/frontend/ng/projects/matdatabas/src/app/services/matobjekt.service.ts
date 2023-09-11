import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable, of, Subject } from "rxjs";
import { Page } from "../common/page";
import { DefinitionMatningstyp } from "./definitionmatningstyp.service";
import { ValidationResult } from "../common/validation-result";
import { catchError, map, tap } from "rxjs/operators";
import { BifogadFil } from "./bifogadfil.service";
import { getLocalDateISOString } from "../common/date-utils";
import { Larm } from "./larm.service";

export const TYP_GRUNDVATTENNIVA = 0;
export const TYP_INFILTRATION = 1;
export const TYP_RORELSE = 2;
export const TYP_TUNNELVATTEN = 3;
export const TYP_VATTENKEMI = 4;
export const TYP_VADERSTATION = 5;
export const TYP_YTVATTENMATNING = 6;

export const FELKOD_OK = "Ok";
export const FELKOD_HINDER = "Hinder";
export const FELKOD_ANNAT_FEL = "Annat fel";
export const FELKOD_FLODAR = "Flödar";
export const FELKOD_TORR = "Torr";
export const FELKOD_FRUSET = "Fruset";

export const EJ_GRANSKAD = 0;
export const GODKANT = 1;
export const FEL = 2;

export interface ListMatobjekt {
  id: number;
  typ: number;
  namn: string;
  aktiv: boolean;
  fastighet: string;
  lage: string;
}

export interface Matobjekt {
  id: number;
  typ: number;
  namn: string;
  aktiv: boolean;
  kontrollprogram: boolean;
  posN: number;
  posE: number;
  fastighet: string;
  lage: string;
  bifogadBildId: number;
  matobjektgrupper: number[];
  matrundor: MatrundaMap[];
  dokument: number[];
}

export interface MatrundaMap {
  matrundaId: number;
  matningstypId: number;
}

export interface MatobjektMapinfo {
  id: number;
  typ: number;
  typNamn: string;
  namn: string;
  aktiv: boolean;
  posN: number;
  posE: number;
  fastighet: string;
  lage: string;
  kartsymbol: number;
  bifogadBildThumbnail: string;
  matningstypIds: [number];
}

export interface Matningstyp {
  id: number;
  definitionMatningstypId: number;
  matansvarigAnvandargruppId: number;
  matintervallAntalGanger: number;
  matintervallTidsenhet: number;
  paminnelseDagar: number;
  instrument: string;
  granskasAutomatiskt: boolean;
  granskasMin: number;
  granskasMax: number;
  berakningKonstant: number;
  berakningReferensniva: number;
  maxPejlbartDjup: number;
  fixpunkt: string;
  typ: string;
  enhet: string;
  storhet?: string;
  beraknadEnhet?: string;
  beraknadStorhet?: string;
  decimaler: number;
  senasteVarde: string;
  senasteVardeDatum: string;
  aktiv: boolean;
  ejGranskade: number;
}

export interface Handelse {
  id: number;
  benamning: string;
  beskrivning: string;
  foretag: string;
  datum: string;
  bifogadebilder: BifogadFil[];
}

export class SaveHandelse {
  benamning: string;
  beskrivning: string;
  datum: string;
  bifogadebilderIds: number[];
}

export interface Analys {
  id: number;
  analysDatum: string;
  kommentar: string;
  rapportor: string;
  rapporter: BifogadFil[];
  matningar: Matning[];
}

export interface Matning {
  id: number;
  matningstypId: number;
  avlastDatum: string;
  avlastVarde: number;
  inomDetektionsomrade: number;
  beraknatVarde: number;
  status: number;
  kommentar: string;
  felkod: string;
  rapportor: string;
  namn: string;
  enhet: string;
  saved: boolean;
}

export interface MatningSaveResult {
  matning: Matning;
  larm: Larm[];
}


export interface MatningstypMatobjekt {
  matningstypId: number;
  matningstypNamn: string;
  matningstypStorhet: string;
  matningstypAktiv: boolean;
  matobjektId: number;
  matobjektNamn: string;
  matobjektFastighet: string;
  matobjektLage: string;
  matobjektAktiv: boolean;
  aktiv: boolean;
  ejGranskade: number;
}

export interface MatningstypMatobjektFilter {
  /** Begränsa sökningen till dessa id:n */
  includeIds?: number[];
  /** Ta inte med dessa id:n i resultaten */
  excludeIds?: number[];
  /** Ingår i någon av dessa mätobjektgrupper */
  matobjektgrupper?: number[];

  /** filtrera mätningar mellan dessa datum. Sätt till null om du inte vill ha något min- eller max-datum */
  matningFromDatum?: string;
  matningToDatum?: string;

  /** Ingår i denna mätrunda */
  matrunda?: number;
  matobjektIds?: number[];
  matobjektNamn?: string;
  matobjektTyp?: number;
  fastighet?: string;
  matansvarigAnvandargruppIds?: number[];
  onlyAktiva?: boolean;
  onlyAktivaMatobjekt?: boolean;
  excludeAutomatiska?: boolean;
  matningStatus?: number;
}

export interface MatningFilter {
  fromDatum?: string;
  tomDatum?: string;
  status?: number;
}

export interface ReviewMatning {
  status?: number;
  avlastVarde?: number;
  kommentar?: string;
  operation?: number;
}

export interface ImportMatning {
  matobjekt: string;
  matningstyp: string;
  storhet?: string;
  instrument: string;
  avlastDatum: string;
  avlastVarde: string;
  beraknatVarde: string;
  enhetAvlast: string;
  enhetBeraknat: string;
  felkod: string;
  kommentar: string;
  inomDetektionsomrade: string;
  matobjektId: number;
  matningstypId: number;
  importFel: ImportError[];
}
export interface ImportError {
  property: string;
  error: string;
}

@Injectable({
  providedIn: "root"
})
export class MatobjektService {
  public matningstypIdUpdated: Subject<number> = new Subject();

  constructor(private http: HttpClient) {
  }

  getPage(page: number, size: number, sortProperty: string, sortDirection: string,
    filter?: MatningstypMatobjektFilter): Observable<Page<ListMatobjekt>> {
    let params = new HttpParams();

    if (page != null) {
      params = params.append("page", String(page));
    }
    if (size != null) {
      params = params.append("size", String(size));
    }
    if (sortProperty != null) {
      params = params.append("sortProperty", sortProperty);
    }
    if (sortDirection != null) {
      params = params.append("sortDirection", sortDirection);
    }

    return this.http.post<Page<ListMatobjekt>>("/api/matobjekt/page", filter, { params });
  }

  get(id: number): Observable<Matobjekt> {
    return this.http.get<Matobjekt>("/api/matobjekt/" + id);
  }

  post(matobjekt: Matobjekt) {
    return this.http.post<Matobjekt>("/api/matobjekt", matobjekt);
  }

  put(id: number, matobjekt: Matobjekt) {
    return this.http.put<Matobjekt>("/api/matobjekt/" + id, matobjekt);
  }

  delete(id) {
    return this.http.delete("/api/matobjekt/" + id);
  }

  canDelete(id: number): Observable<boolean> {
    return this.http.options("/api/matobjekt/" + id, { observe: "response" }).pipe(
      map(reponse => reponse.headers.has("Allow") && reponse.headers.get("Allow").includes("DELETE"))
    );
  }

  getMatobjektNamn(q: string, typ?: number): Observable<string[]> {
    let query = `?q=${q}`;

    if (typ) {
      query += `&typ=${typ}`;
    }

    return this.http.get<string[]>("/api/matobjekt/namn" + query);
  }

  getMatobjektNamnById(id: number): Observable<string> {
    return this.http.get<string>("/api/matobjekt/" + id + "/namn");
  }

  getMatobjektIdByNamn(namn: string): Observable<number> {
    return this.http.get<number>(`/api/matobjekt/id?namn=${namn}`);
  }

  getMatobjektFastigheter(q: string): Observable<string[]> {
    return this.http.get<string[]>(`/api/matobjekt/fastigheter?q=${q}`);
  }

  getMatningstyper(id: number): Observable<Matningstyp[]> {
    return this.http.get<Matningstyp[]>("/api/matobjekt/" + id + "/matningstyper");
  }

  getMatningstyp(matobjektId: number, matningstypId: number): Observable<Matningstyp> {
    return this.http.get<Matningstyp>("/api/matobjekt/" + matobjektId + "/matningstyper/" + matningstypId);
  }

  getSenasteMatningar(matobjektId: number): Observable<Map<string, Matning>> {
    return this.http.get<Map<string, Matning>>("/api/matobjekt/" + matobjektId + "/senastematningar");
  }

  postMatningstyp(matobjektId: number, definitionMatningstypId: number, matningstyp: Partial<Matningstyp>) {
    return this.http.post<Matningstyp>("/api/matobjekt/" + matobjektId + "/matningstyper?definitionMatningstypId="
      + definitionMatningstypId, matningstyp);
  }

  putMatningstyp(matobjektId: number, matningstypId: number, matningstyp: Partial<Matningstyp>) {
    return this.http.put("/api/matobjekt/" + matobjektId + "/matningstyper/" + matningstypId, matningstyp);
  }

  deleteMatningstyp(matobjektId: number, matningstypId: number) {
    return this.http.delete("/api/matobjekt/" + matobjektId + "/matningstyper/" + matningstypId);
  }

  canDeleteMatningstyp(matobjektId: number, matningstypId: number): Observable<boolean> {
    return this.http.options("/api/matobjekt/" + matobjektId + "/matningstyper/" + matningstypId, { observe: "response" }).pipe(
      map(reponse => reponse.headers.has("Allow") && reponse.headers.get("Allow").includes("DELETE"))
    );
  }

  getDefinitionMatningstyper(id: number): Observable<DefinitionMatningstyp[]> {
    return this.http.get<DefinitionMatningstyp[]>("/api/matobjekt/" + id + "/definitionmatningstyper");
  }

  getHandelser(id: number, page: number, size: number, sortProperty: string, sortDirection: string): Observable<Page<Handelse>> {
    const params = new HttpParams()
      .append("page", page.toString())
      .append("size", size.toString())
      .append("sortProperty", sortProperty)
      .append("sortDirection", sortDirection);
    return this.http.get<Page<Handelse>>("/api/matobjekt/" + id + "/handelser", { params: params });
  }

  getHandelse(id: number, handelseId: number): Observable<Handelse> {
    return this.http.get<Handelse>("/api/matobjekt/" + id + "/handelser/" + handelseId);
  }

  addHandelse(id: number, handelse: SaveHandelse): Observable<Handelse> {
    return this.http.post<Handelse>("/api/matobjekt/" + id + "/handelser", handelse);
  }

  updateHandelse(id: number, handelseId: number, handelse: SaveHandelse): Observable<Handelse> {
    return this.http.put<Handelse>("/api/matobjekt/" + id + "/handelser/" + handelseId, handelse);
  }

  deleteHandelse(id: number, handelseId: number) {
    return this.http.delete("/api/matobjekt/" + id + "/handelser/" + handelseId);
  }

  namnExists(namn: string): Observable<ValidationResult> {
    return this.http.get<ValidationResult>(`/api/validation/matobjekt/exists?namn=${namn}`);
  }

  postVattenkemi(matobjektId: number, vattenkemi: Analys) {
    return this.http.post<Analys>(`/api/matobjekt/${matobjektId}/vattenkemi`, vattenkemi);
  }

  putVattenkemi(matobjektId: number, vattenkemiId: number, vattenkemi: Analys) {
    return this.http.put<Analys>(`/api/matobjekt/${matobjektId}/vattenkemi/${vattenkemiId}`, vattenkemi);
  }

  getVattenkemi(matobjektId: number, vattenkemiId: number) {
    return this.http.get(`/api/matobjekt/${matobjektId}/vattenkemi/${vattenkemiId}`);
  }

  postMatning(matobjektId: number, matningstypId: number, matning: Matning) {
    return this.http.post<MatningSaveResult>(`/api/matobjekt/${matobjektId}/matningstyper/${matningstypId}/matningar`, matning)
      .pipe(tap(ev => this.matningstypIdUpdated.next(matningstypId)));
  }

  putMatning(matobjektId: number, matningstypId: number, matningId: number, matning: Matning) {
    return this.http.put<MatningSaveResult>(`/api/matobjekt/${matobjektId}/matningstyper/${matningstypId}/matningar/${matningId}`, matning)
      .pipe(tap(ev => this.matningstypIdUpdated.next(matningstypId)));
  }

  deleteMatningar(matningar: number[]) {
    const params = new HttpParams().append("id", matningar.toString());
    return this.http.delete<void>(`/api/matningar/delete`, { params: params});
  }

  reviewMatning(matobjektId: number, matningstypId: number, matningId: number, matning: ReviewMatning) {
    return this.http.put<Matning>(`/api/matobjekt/${matobjektId}/matningstyper/${matningstypId}/matningar/${matningId}/review`, matning)
      .pipe(tap(ev => this.matningstypIdUpdated.next(matningstypId)));
  }

  getMatning(matobjektId: number, matningstypId: number, matningId: number) {
    return this.http.get<Matning>(`/api/matobjekt/${matobjektId}/matningstyper/${matningstypId}/matningar/${matningId}`);
  }
  getVattenkemiPage(matobjektId: number, page: number, size: number,
    sortProperty: string, sortDirection: string): Observable<Page<Analys>> {
    const params = new HttpParams()
      .append("page", page.toString())
      .append("size", size.toString())
      .append("sortProperty", sortProperty)
      .append("sortDirection", sortDirection);
    return this.http.get<Page<Analys>>("/api/matobjekt/" + matobjektId + "/vattenkemi", { params: params });
  }

  analysExists(matobjektId: number, analysDatum: string): Observable<ValidationResult> {
    return this.http.get<ValidationResult>(`/api/validation/matobjekt/analys/exists?matobjektId=${matobjektId}&analysDatum=${analysDatum}`);
  }

  getEjGranskadeMatningarForMatningstyp(matningstypId: number): Observable<number> {
    return this.http.get<number>(`/api/matobjekt/${matningstypId}/ogranskade`).pipe(
      map(result => result["count"]),
      catchError(err => of(undefined))
    );
  }

  getMatningstypMatobjekt(matningstypId: number): Observable<MatningstypMatobjekt> {
    const filter = { includeIds: [matningstypId] };
    return this.getMatningstypMatobjektUnpaged(filter).pipe(map(page => page.content[0]));
  }

  getMatningstypMatobjektUnpaged(filter?: MatningstypMatobjektFilter): Observable<Page<MatningstypMatobjekt>> {
    return this.getMatningstypMatobjektPage(null, null, null, null, filter);
  }

  getMatningstypMatobjektPage(page: number, size: number, sortProperty: string, sortDirection: string, filter?: MatningstypMatobjektFilter):
    Observable<Page<MatningstypMatobjekt>> {
    let params = new HttpParams();

    if (page != null) {
      params = params.append("page", String(page));
    }
    if (size != null) {
      params = params.append("size", String(size));
    }
    if (sortProperty != null) {
      params = params.append("sortProperty", sortProperty);
    }
    if (sortDirection != null) {
      params = params.append("sortDirection", sortDirection);
    }

    return this.http.post<Page<MatningstypMatobjekt>>("/api/matobjekt/matningstyper", filter, { params });
  }

  getMatningPage(matobjektId: number, matningstypId: number, page: number, size: number, sortProperty: string,
    sortDirection: string, filter?: MatningFilter):
    Observable<Page<Matning>> {
    let params = new HttpParams();

    if (page != null) {
      params = params.append("page", String(page));
    }
    if (size != null) {
      params = params.append("size", String(size));
    }
    if (sortProperty != null) {
      params = params.append("sortProperty", sortProperty);
    }
    if (sortDirection != null) {
      params = params.append("sortDirection", sortDirection);
    }

    if (filter) {
      if (filter.fromDatum != null) {
        params = params.append("fromDatum", filter.fromDatum);
      }
      if (filter.tomDatum != null) {
        params = params.append("tomDatum", filter.tomDatum);
      }
      if (filter.status != null) {
        params = params.append("status", String(filter.status));
      }
    }
    return this.http.get<Page<Matning>>(`/api/matobjekt/${matobjektId}/matningstyper/${matningstypId}/matningar`,
      { params: params });
  }

  // TODO: flytta till matningstyper-service eller likn.
  getMatningDataSeries(matningstypId: number, fromDatum?: Date, sattningReferensdatum?: Date,
                       filterGodkanda?: boolean, filterFelkodOk?: boolean) {
    let params = new HttpParams();
    if (fromDatum != null) {
      params = params.append("fromDatum", getLocalDateISOString(fromDatum));
    }
    if (sattningReferensdatum != null) {
      params = params.append("sattningReferensdatum", getLocalDateISOString(sattningReferensdatum));
    }
    if (filterGodkanda) {
      params = params.append("filterGodkanda", "true");
    }
    if (filterFelkodOk) {
      params = params.append("filterFelkodOk", "true");
    }

    return this.http.get(`/api/matningstyper/${matningstypId}/matningDataSeries`, { params: params });
  }

  // TODO: flytta till matningstyper-service eller likn.
  getLarmDetaljer(matningstypId: number): Observable<Larm[]> {
    return this.http.get<Larm[]>(`/api/matningstyper/${matningstypId}/larmdetaljer`);
  }

  godkann(matningar: number[]) {
    return this.http.post(`/api/matningar/godkann`, "id=" + matningar.join(","),
      { headers: { "content-type": "application/x-www-form-urlencoded" } });
  }

  getMatobjektMapinfo(filter?: MatningstypMatobjektFilter): Observable<MatobjektMapinfo[]> {
    return this.http.post<MatobjektMapinfo[]>("/api/matobjekt/mapinfo", filter);
  }

}
