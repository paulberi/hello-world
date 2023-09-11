import { Injectable } from "@angular/core";
import { from, Observable } from "rxjs";
import { flatMap } from "rxjs/operators";
import { HaglofApiService, HaglofImportVarningar } from "../../../../../generated/markkoll-api";
import { uuid } from "../model/uuid";
@Injectable({
  providedIn: "root"
})
export class HaglofService {
  constructor(private haglofApiService: HaglofApiService) {}

  importJson(projektId: uuid, jsonFile: File): Observable<HaglofImportVarningar> {
    return from(jsonFile.text())
      .pipe(flatMap(json => this.haglofApiService.haglofImport(projektId, json)));
  }
}
