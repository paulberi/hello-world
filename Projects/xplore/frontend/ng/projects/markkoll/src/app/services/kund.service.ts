import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { flatMap } from "rxjs/operators";
import { Dokumentmall, DokumentmallApiService, FiberVarderingConfig, FiberVarderingConfigNamnAgare, KundApiService } from "../../../../../generated/markkoll-api";
import { FiberVarderingConfigCreateData } from "../admin/ersattning-fiber/ersattning-fiber.presenter";
import { uuid } from "../model/uuid";
import { MkUserService } from "./user.service";

@Injectable({
  providedIn: "root"
})
export class KundService {
  constructor(private dokumentmallApiService: DokumentmallApiService,
              private kundApiService: KundApiService,
              private mkUserService: MkUserService) {}

  getDokumentmallar(): Observable<Dokumentmall[]> {
    return this.mkUserService.getMarkkollUser$()
      .pipe(flatMap(user => this.dokumentmallApiService.getKundDokumentmallar(user.kundId)));
  }

  getFiberVarderingConfigForKund(kundId: string): Observable<FiberVarderingConfig> {
    return this.kundApiService.getDefaultFiberVarderingConfigForKund(kundId);
  }

  getAllFiberVarderingConfigsForKund(kundId: string): Observable<FiberVarderingConfigNamnAgare[]> {
    return this.kundApiService.getAllFiberVarderingConfigsForKund(kundId);
  }

  updateFiberVarderingConfigForKund(kundId: string, fiberConfig: FiberVarderingConfig): Observable<FiberVarderingConfig> {
    return this.kundApiService.updateDefaultFiberVarderingConfigForKund(kundId, fiberConfig);
  }

  createFiberVarderingConfig(kundId: string, data: FiberVarderingConfigCreateData) {
    return this.kundApiService.createFiberVarderingConfig(kundId, data);
  }

  updateFiberVarderingConfig(kundId: string, fiberConfig: FiberVarderingConfigNamnAgare): Observable<FiberVarderingConfigNamnAgare> {
    return this.kundApiService.updateFiberVarderingConfig(kundId, fiberConfig);
  }
  
  deleteFiberVarderingConfig(kundId: string, fiberConfigId: uuid): Observable<void> {
    return this.kundApiService.deleteFiberVarderingConfig(kundId, fiberConfigId);
  }

}
