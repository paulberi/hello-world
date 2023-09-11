import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { map } from "rxjs/operators";
import { KundService as KundApiService, Kund, KundInfo } from "../../../../../generated/kundconfig-api";
import { XpPage } from "../../../../lib/ui/paginated-table/page";

@Injectable({
  providedIn: "root"
})
export class KundService {
  constructor(private apiService: KundApiService) { }

  getKundPage(index: number, size: number): Observable<XpPage<Kund>> {
    return this.apiService.getKundPage(index, size).pipe(map(p => p as XpPage<Kund>));
  }

  editKund(kund: Kund): Observable<Kund> {
    return this.apiService.updateKund(kund.id, kund as KundInfo);
  }

  deleteKund(id: string): Observable<void> {
    return this.apiService.deleteKund(id);
  }

  resetGeofenceRules(): Observable<void> {
    return this.apiService.resetGeofenceRules();
  }
}
