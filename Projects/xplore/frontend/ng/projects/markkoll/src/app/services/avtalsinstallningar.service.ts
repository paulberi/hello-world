import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Avtalsinstallningar, ProjektApiService } from "../../../../../generated/markkoll-api";
import { uuid } from "../model/uuid";

@Injectable({
  providedIn: "root"
})
export class AvtalsinstallningarService {
  constructor(private projektApiService: ProjektApiService) {}

  getAvtalsinstallningar(projektId: uuid): Observable<Avtalsinstallningar> {
    return this.projektApiService.getAvtalsinstallningar(projektId);
  }

  updateAvtalsinstallningar(projektId: uuid, avtalsinstallningar: Avtalsinstallningar): Observable<void> {
    return this.projektApiService.updateAvtalsinstallningar(projektId, avtalsinstallningar);
  }
}
