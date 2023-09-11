import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { map, mergeMap, tap, toArray } from "rxjs/operators";
import { KundApiService, Ledningsagare } from "../../../../../generated/markkoll-api";
import { uuid } from "../model/uuid";

@Injectable({
  providedIn: 'root'
})
export class MkLedningsagareService {
  constructor(private kundApiService: KundApiService) {}

  addLedningsagare(namn: string, kundId: string): Observable<Ledningsagare> {
    return this.kundApiService.addLedningsagare(kundId, namn);
  }

  deleteLedningsagare(ledningsagareId: uuid, kundId: string): Observable<void> {
    return this.kundApiService.deleteLedningsagare(kundId, ledningsagareId);
  }

  getLedningsagare(kundId: string): Observable<Ledningsagare[]> {
    return kundId ? this.kundApiService.getLedningsagare(kundId) : of([]);
  }

  getLedningsagareNamn(kundId: string): Observable<string[]> {
    return this.getLedningsagare(kundId)
      .pipe(
        mergeMap(ag => ag),
        map(ag => ag.namn),
        toArray(),
    );
  }
}
