import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import {
  KundRsp as Kund,
  ProjektRsp as Projekt,
} from "../../../../../generated/samrad-api";
import { KundService } from "../../../../../generated/samrad-api/api/kund.service";
import { ProjektService } from "../../../../../generated/samrad-api/api/projekt.service";

@Injectable({
  providedIn: "root",
})
export class SamradPublicService {
  constructor(
    private kundService: KundService,
    private projektService: ProjektService
  ) {}

  getKundBySlug$(slug: string): Observable<Kund> {
    return this.kundService.getKundWithSlug(slug);
  }

  getSamrad$(kundId: string, projektId: string): Observable<Projekt> {
    return this.projektService.getProjektWithIdorSlug(kundId, projektId);
  }

  getSamradList$(kundId: string): Observable<Projekt[]> {
    return this.projektService.listProjekt(kundId);
  }
}
