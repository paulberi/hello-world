import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Beredare, ProjektApiService } from "../../../../../generated/markkoll-api";
import { uuid } from "../model/uuid";

@Injectable({
  providedIn: "root"
})
export class BeredareService {
  constructor(private projektApiService: ProjektApiService) {}

  getBeredare(projektId: uuid): Observable<Beredare> {
    return this.projektApiService.getBeredare(projektId);
  }

  editBeredare(beredare: Beredare, projektId: uuid): Observable<void> {
      return this.projektApiService.editBeredare(projektId, beredare);
  }
}
