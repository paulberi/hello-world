import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { Abonnemang, AbonnemangService as AbonnemangApiService, KundService } from "../../../../../generated/kundconfig-api";

@Injectable({
  providedIn: "root"
})
export class AbonnemangService {
  constructor(private abonnemangApiService: AbonnemangApiService, private kundApiService: KundService ) {}

  addAbonnemang(kundId: string, abonnemang: Abonnemang): Observable<Abonnemang> {
    return this.kundApiService.addAbonnemang(kundId, abonnemang);
  }

  deleteAbonnemang(abonnemangId: string): Observable<void> {
    return this.abonnemangApiService.deleteAbonnemang(abonnemangId);
  }
}
