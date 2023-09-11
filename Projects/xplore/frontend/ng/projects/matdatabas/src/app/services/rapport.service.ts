import { Injectable } from "@angular/core";
import { HttpClient, HttpParams, HttpHeaders, HttpResponse } from "@angular/common/http";
import { DataSerie } from "../granskning/granskning-data.service";
import { Observable } from "rxjs";
import { Gransvarde, GransvardeService } from "./gransvarde.service";
import { MatobjektService } from "./matobjekt.service";
import { ValidationResult } from "../common/validation-result";
import { Page } from "../common/page";

export enum DatumPeriod {
  Months6 = "6m",
  Years1 = "1y",
  Years2 = "2y",
  Years5 = "5y",
  Years10 = "10y",
  All = "all"
}

export const Tidsintervall = [
  { value: "DAGSVIS", displayValue: "Dagsvis, må-fre"},
  { value: "VECKOVIS", displayValue: "Veckovis, måndagar"},
  { value: "MANADSVIS", displayValue: "Månadsvis, första måndag"},
  { value: "KVARTALSVIS", displayValue: "Kvartalsvis, första måndag"},
  { value: "ARSVIS", displayValue: "Årsvis, första måndag"},
];

export interface ListRapport {
  id: number;
  namn: string;
  aktiv: boolean;
  beskrivning: string;
  senastSkickad: string;
}

export interface Rapport {
  rubrik: string;
  information: string;
  lagesbildId: string;
  grafer: RapportGraf[];
}

export interface RapportGraf {
  rubrik: string;
  information: string;
  matningar: DataSerie[];
  referensdata: DataSerie[];
  gransvarden: Gransvarde[];
}

export interface RapportMottagare {
  namn: string;
  epost: string;
}

export interface RapportSettings {
  id: number;
  namn: string;
  aktiv: boolean;
  mejlmeddelande: string;
  beskrivning: string;
  dataperiodFrom: string;
  rorelsereferensdatum: string;
  tidsintervall: string;
  startDatum: string;
  inledningRubrik: string;
  inledningInformation: string;
  lagesbild: number;
  senastSkickad: string;
  skapadDatum: string;
  andradDatum: string;
  rapportGraf: RapportGrafSettings[];
  rapportMottagare: RapportMottagare[];
}

export interface RapportMatdataSettings {
  matobjektId: number;
  matningstypId: number;
}

export interface RapportGransvardeSettings {
  matobjektId: number;
  gransvardeId: number;
}

export interface RapportGrafSettings {
  id: number;
  rubrik: string;
  info: string;
  matningstyper: number[];
  gransvarden: number[];
}

export function grafSettingsEmpty(): RapportGrafSettings {
  return {
    id: null,
    rubrik: "",
    info: "",
    matningstyper: [],
    gransvarden: []
  };
}

@Injectable({
  providedIn: "root",
})

export class RapportService {
  constructor(private http: HttpClient,
    public matobjektService: MatobjektService,
    public gransvardeService: GransvardeService) { }

  getPage(page: number, size: number, sortProperty: string, sortDirection: string, opts = {}):
    Observable<Page<ListRapport>> {
    const params = new HttpParams()
      .append("page", page.toString())
      .append("size", size.toString())
      .append("sortProperty", sortProperty)
      .append("sortDirection", sortDirection);
    return this.http.get<Page<ListRapport>>("/api/rapport", {params: params});
  }

  getRapport(rapportId: number): Observable<Rapport> {
    return this.http.get<Rapport>(`/api/rapport/${rapportId}`);
  }

  getRapportSettings(rapportId: number): Observable<RapportSettings> {
    return this.http.get<RapportSettings>(`/api/rapport/settings/${rapportId}`);
  }

  putRapportSettings(id: number,
    rapportSettings: Partial<RapportSettings>
  ): Observable<RapportSettings> {
    return this.http.put<RapportSettings>(`/api/rapport/settings/${id}`, rapportSettings);
  }

  postRapportSettings(rapportSettings: Partial<RapportSettings>
  ): Observable<RapportSettings> {
    return this.http.post<RapportSettings>("/api/rapport/settings", rapportSettings);
  }

  deleteRapport(id: number): Observable<void> {
    return this.http.delete<void>(`/api/rapport/${id}`);
  }

  rapportNameExists(name: string): Observable<ValidationResult> {
    const params = new HttpParams()
      .append("namn", name);
    return this.http.get<ValidationResult>(`/api/validation/rapport/exists?namn=${name}`);
  }

  private getFileNameFromHttpResponse(httpResponse: HttpResponse<Blob>) {
    const contentDispositionHeader = httpResponse.headers.get("Content-Disposition");
    if (contentDispositionHeader) {
      const result = contentDispositionHeader.split(";")[1].trim().split("=")[1];
      return result.replace(/"/g, "");
    } else {
      return "error.pdf";
    }
  }
}
