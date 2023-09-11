import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";
import {Observable} from "rxjs";
import {Page} from "../common/page";
import {ValidationResult} from "../common/validation-result";

export enum Berakningstyp {
  NIVA_VATTEN_LUFTTRYCK = "NIVA_VATTEN_LUFTTRYCK",
  NIVA_NEDMATNING = "NIVA_NEDMATNING",
  NIVA_PORTRYCK = "NIVA_PORTRYCK",
  SATTNING = "SATTNING",
  LUFTTRYCK_PER_TIMME = "LUFTTRYCK_PER_TIMME",
  INFILTRATION_MOMENTANT_FLODE = "INFILTRATION_MOMENTANT_FLODE",
  INFILTRATION_MEDEL_FLODE = "INFILTRATION_MEDEL_FLODE",
  TUNNELVATTEN_MOMENTANT_FLODE = "TUNNELVATTEN_MOMENTANT_FLODE",
  TUNNELVATTEN_MEDEL_FLODE = "TUNNELVATTEN_MEDEL_FLODE"
}

export class DefinitionMatningstyp {
  id: number;
  matobjektTyp: number;
  berakningstyp?: string;
  namn: string;
  storhet: string;
  enhet: string;
  decimaler: number;
  beraknadStorhet: string;
  beraknadEnhet: string;
  beskrivning: string;
  automatiskInrapportering: boolean;
  automatiskGranskning: boolean;

  isNivaVattenOchLufttryck() {
    return this.berakningstyp === Berakningstyp.NIVA_VATTEN_LUFTTRYCK;
  }

  isNivaNedmatning() {
    return this.berakningstyp === Berakningstyp.NIVA_NEDMATNING;
  }

  isNivaPortryck() {
    return this.berakningstyp === Berakningstyp.NIVA_PORTRYCK;
  }

  isSattning() {
    return this.berakningstyp === Berakningstyp.SATTNING;
  }

  isVattenkemi() {
    return this.matobjektTyp === 4;
  }
}

@Injectable({
  providedIn: "root"
})
export class DefinitionmatningstypService {
  constructor(private http: HttpClient) {
  }

  findByMatobjektTyp(matobjektTyp: number, page: number, size: number, sortProperty: string, sortDirection: string): Observable<Page<DefinitionMatningstyp>> {
    const query = `?matobjektTyp=${matobjektTyp}&page=${page}&size=${size}&sortProperty=${sortProperty}&sortDirection=${sortDirection}`;
    return this.http.get<Page<DefinitionMatningstyp>>("/api/definitionmatningstyp" + query).pipe(
      map(response => {
        response.content = response.content.map(d => Object.assign(new DefinitionMatningstyp(), d));
        return response;
      })
    );
  }

  get(id: number) {
    return this.http.get("/api/definitionmatningstyp/" + id).pipe(
      map(response => {
        const definition = new DefinitionMatningstyp();
        return Object.assign(definition, response);
      })
    );
  }

  put(id: number, save: Partial<DefinitionMatningstyp>) {
    return this.http.put("/api/definitionmatningstyp/" + id, save);
  }

  post(save: Partial<DefinitionMatningstyp>) {
    return this.http.post("/api/definitionmatningstyp", save).pipe(
      map(response => {
        const definition = new DefinitionMatningstyp();
        return Object.assign(definition, response);
      })
    );
  }

  delete(id: number) {
    return this.http.delete("/api/definitionmatningstyp/" + id);
  }

  canDelete(id: number): Observable<boolean> {
    return this.http.options("/api/definitionmatningstyp/" + id, {observe: "response"}).pipe(
      map(reponse => reponse.headers.has("Allow") && reponse.headers.get("Allow").includes("DELETE"))
    );
  }

  namnExists(namn: string, matobjektTyp: number): Observable<ValidationResult> {
    return this.http.get<ValidationResult>(`/api/validation/definitionmatningstyp/exists?namn=${namn}&matobjektTyp=${matobjektTyp}`);
  }
}
