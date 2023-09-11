import { Injectable } from "@angular/core";
import { AvtalApiService, FiberVarderingConfig, FiberVarderingsprotokoll, FiberVarderingsprotokollApiService } from "../../../../../generated/markkoll-api";
import { Observable } from "rxjs";
import { uuid } from "../model/uuid";
import { MkFiberVarderingService } from "./vardering-fiber.service";


@Injectable({
  providedIn: "root"
})
export class MkFiberVarderingsprotokollService {

  constructor(private avtalApiService: AvtalApiService,
    private fiberVarderingsprotokollApiService: FiberVarderingsprotokollApiService) { }

  getFiberVarderingsprotokollWithAvtalId(projektId, avtalId): Observable<FiberVarderingsprotokoll> {
    return this.avtalApiService.getFiberVarderingsprotokollWithAvtalId(projektId, avtalId);
  }

  getFiberVarderingConfig(projektId: uuid, vpId: uuid): Observable<FiberVarderingConfig> {
    return this.fiberVarderingsprotokollApiService.getFiberVarderingConfig(projektId, vpId);
  }

  updateVp(projektId, vp: FiberVarderingsprotokoll): Observable<void> {
    return this.fiberVarderingsprotokollApiService.updateFiberVarderingsprotokoll(projektId, vp.id, vp);
  }
}
