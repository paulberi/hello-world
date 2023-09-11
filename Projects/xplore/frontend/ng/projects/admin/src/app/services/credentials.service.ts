import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { FastighetsokAuth } from "../../../../../generated/kundconfig-api";
import { MetriaMapsAuth } from "../../../../../generated/kundconfig-api";
import { KundService } from "../../../../../generated/kundconfig-api";

@Injectable({
  providedIn: "root"
})
export class CredentialsService {
  constructor(private apiService: KundService) { }

  getMetriaMapsAuth(kundId: string): Observable<MetriaMapsAuth> {
    return this.apiService.getMetriaMapsAuth(kundId);
  }

  editMetriaMapsAuth(kundId: string, auth: MetriaMapsAuth): Observable<void> {
    return this.apiService.editMetriaMapsAuth(kundId, auth);
  }

  resetMetriaMapsAuth(authId: string, kundId: string): Observable<void> {
    const auth: MetriaMapsAuth = {
      id: authId,
      username: null,
      password: null,
      kundId: kundId,
    };

    return this.editMetriaMapsAuth(kundId, auth);
  }

  getFastighetsokAuth(kundId: string): Observable<FastighetsokAuth> {
    return this.apiService.getFastighetsokAuth(kundId);
  }

  editFastighetsokAuth(kundId: string, auth: FastighetsokAuth): Observable<void> {
    return this.apiService.editFastighetsokAuth(kundId, auth);
  }

  resetFastighetsokAuth(authId: string, kundId: string): Observable<void> {
    const auth: FastighetsokAuth = {
      id: authId,
      username: null,
      password: null,
      kundmarke: null,
      kundId: kundId,
    };

    return this.editFastighetsokAuth(kundId, auth);
  }
}
