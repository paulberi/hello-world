import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { Avtalsstatus, Fastighet, FastighetApiService, FastighetDelomradeInfo, Fastighetsfilter, Fastighetsinfo, Geometristatus, ProjektApiService } from "../../../../../generated/markkoll-api";
import { FastighetsProjektInfo } from "../../../../../generated/markkoll-api/model/fastighetsProjektInfo";
import { MkAvtalsfilter } from "../model/avtalsfilter";
import { uuid } from "../model/uuid";
import { FastighetsfilterService } from "./fastighetsfilter.service";

export type FastighetsfilterSelect = "EJ_BEHANDLAT" | "AVTAL_SKICKAT" | "AVTAL_JUSTERAS" |
  "AVTAL_SIGNERAT" | "PAMINNELSE_SKICKAD" | "ERSATTNING_UTBETALAS" | "ERSATTNING_UTBETALD" |
  "AVTALSKONFLIKT" | "FORSENADE" | "KORTA_INTRANG" | "SENAST_UPPDATERADE" | "SKOGSFASTIGHET" |
  "LUFTLEDNING";

export const FastighetsfilterSelect = {
  EJBEHANDLAT: "EJ_BEHANDLAT" as FastighetsfilterSelect,
  AVTALSKICKAT: "AVTAL_SKICKAT" as FastighetsfilterSelect,
  PAMINNELSESKICKAD: "PAMINNELSE_SKICKAD" as FastighetsfilterSelect,
  AVTALJUSTERAS: "AVTAL_JUSTERAS" as FastighetsfilterSelect,
  AVTALSIGNERAT: "AVTAL_SIGNERAT" as FastighetsfilterSelect,
  ERSATTNINGUTBETALAS: "ERSATTNING_UTBETALAS" as FastighetsfilterSelect,
  ERSATTNINGUTBETALD: "ERSATTNING_UTBETALD" as FastighetsfilterSelect,
  AVTALSKONFLIKT: "AVTALSKONFLIKT" as FastighetsfilterSelect,
  FORSENADE: "FORSENADE" as FastighetsfilterSelect,
  KORTAINTRANG: "KORTA_INTRANG" as FastighetsfilterSelect,
  SENASTUPPDATERADE: "SENAST_UPPDATERADE" as FastighetsfilterSelect,
  SKOGSFASTIGHET: "SKOGSFASTIGHET" as FastighetsfilterSelect,
  LUFTLEDNINGAR: "LUFTLEDNING" as FastighetsfilterSelect
};

@Injectable({
  providedIn: "root"
})
export class FastighetService {
  constructor(private projektApiService: ProjektApiService,
    private fastighetsfilterService: FastighetsfilterService,
    private fastighetApiService: FastighetApiService) { }

  fastighetsfilter(filterSelect: FastighetsfilterSelect,
    fastighetsbeteckning: string,
    excludeWithOwners: boolean): Fastighetsfilter {

    return {
      avtalsstatus: this.statusFilter(filterSelect),
      fastighetsbeteckning: fastighetsbeteckning,
      intrangLength: this.intrangLengthFilter(filterSelect),
      showSenastUppdaterade: this.uppdateradeFastigheterFilter(filterSelect),
      excludeWithMarkagare: excludeWithOwners,
      showForsenade: this.forsenadeFilter(filterSelect)
    } as Fastighetsfilter;
  }

  // Alla fastigheter i ett projekt som saknar importerade markägare
  fastighetIdsUnimported(projektId: uuid): Observable<uuid[]> {
    const filter: Fastighetsfilter = {
      excludeWithMarkagare: true,
      excludeOutreddaFastigheter: true
    };

    return this.projektApiService
      .projektFastigheter(projektId, filter)
      .pipe(
        map(fastigheter => fastigheter.map(f => f.id))
      );
  }

  fastighetsinformation(fastighetId: uuid, projektId: uuid): Observable<Fastighetsinfo> {
    return this.fastighetApiService.getFastighetsinfo(fastighetId, projektId);
  }

  projektFastigheter(projektId: uuid,
    fastighetsfilter: Fastighetsfilter): Observable<Fastighet[]> {

    return this.projektApiService.projektFastigheter(projektId, fastighetsfilter);
  }

  setFastighetStatus(projektId: uuid, fastighetId: uuid, avtalsstatus: Avtalsstatus): Observable<Avtalsstatus> {
    return this.projektApiService.setFastighetStatus(projektId, fastighetId, avtalsstatus);
  }

  /** @deprecated */
  _setFastighetStatusForSelection(projektId: uuid,
    avtalsstatus: Avtalsstatus,
    filter: Fastighetsfilter): Observable<number> {
    return this.projektApiService.setFastighetsStatusForSelection(projektId, avtalsstatus, filter);
  }

  setFastighetStatusForSelection(projektId: uuid,
    avtalsstatus: Avtalsstatus,
    filterSelection: MkAvtalsfilter): Observable<number> {
    const filter = this.fastighetsfilterService.getFastighetsfilter(filterSelection, false);

    return this.projektApiService.setFastighetsStatusForSelection(projektId, avtalsstatus, filter);
  }

  setFastighetProjektInfo(fastighetId: uuid, projektId: uuid, info: FastighetsProjektInfo): Observable<number> {
    return this.fastighetApiService.setFastighetProjektInfo(fastighetId, projektId, info);
  }

  resetGeometristatus(projektId: uuid, fastighetId: uuid): Observable<Geometristatus> {
    return this.projektApiService.resetGeometryStatus(projektId, fastighetId)
      .pipe(map(() => Geometristatus.OFORANDRAD));
  }

  removeFastighet(projektId: uuid, fastighetId: uuid): Observable<void> {
    return this.projektApiService.removeFastighet(projektId, fastighetId);
  }

  fetchDelomradenForFastighet(fastighetId: uuid): Observable<FastighetDelomradeInfo[]> {
    return this.fastighetApiService.fetchDelomradenForFastighet(fastighetId);
  }

  private statusFilter(fastighetsfilter: FastighetsfilterSelect): Avtalsstatus {
    switch (fastighetsfilter) {
      case FastighetsfilterSelect.AVTALJUSTERAS:
        return Avtalsstatus.AVTALJUSTERAS;

      case FastighetsfilterSelect.AVTALSIGNERAT:
        return Avtalsstatus.AVTALSIGNERAT;

      case FastighetsfilterSelect.AVTALSKICKAT:
        return Avtalsstatus.AVTALSKICKAT;

      case FastighetsfilterSelect.AVTALSKONFLIKT:
        return Avtalsstatus.AVTALSKONFLIKT;

      case FastighetsfilterSelect.ERSATTNINGUTBETALD:
        return Avtalsstatus.ERSATTNINGUTBETALD;

      case FastighetsfilterSelect.ERSATTNINGUTBETALAS:
        return Avtalsstatus.ERSATTNINGUTBETALAS;

      case FastighetsfilterSelect.PAMINNELSESKICKAD:
        return Avtalsstatus.PAMINNELSESKICKAD;

      default:
        return null;
    }
  }

  private intrangLengthFilter(fastighetsfilter: FastighetsfilterSelect): number {
    if (fastighetsfilter === FastighetsfilterSelect.KORTAINTRANG) {
      return 10.;
    } else {
      return null;
    }
  }

  private uppdateradeFastigheterFilter(fastighetsfilter: FastighetsfilterSelect): boolean {
    if (fastighetsfilter === FastighetsfilterSelect.SENASTUPPDATERADE) {
      return true;
    } else {
      return false;
    }
  }

  private forsenadeFilter(fastighetsfilter: FastighetsfilterSelect): boolean {
    if (fastighetsfilter === FastighetsfilterSelect.FORSENADE) {
      return true;
    } else {
      return false;
    }
  }

}
