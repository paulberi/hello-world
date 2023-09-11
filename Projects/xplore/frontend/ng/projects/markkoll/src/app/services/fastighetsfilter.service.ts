import { Injectable } from "@angular/core";
import { Avtalsstatus, Fastighetsfilter } from "../../../../../generated/markkoll-api";
import { MkAvtalsfilter } from "../model/avtalsfilter";

@Injectable({
  providedIn: "root"
})
export class FastighetsfilterService {
  getFastighetsfilter(filter: MkAvtalsfilter, excludeWithOwners: boolean): Fastighetsfilter {
    return {
      avtalsstatus: this.avtalsstatusFilter(filter),
      fastighetsbeteckning: filter?.search,
      fastighetsIds: filter?.registerenhetsIds,
      intrangLength: filter?.status === "KORTA_INTRANG" ? 10. : null,
      showSenastUppdaterade: filter?.status === "SENAST_UPPDATERADE",
      excludeWithMarkagare: excludeWithOwners,
      showForsenade: filter?.status === "FORSENADE",
      skogsfastighet: filter?.status === "SKOGSFASTIGHET",
      luftledningar: filter?.status === "LUFTLEDNING"
    } as Fastighetsfilter;
  }

  private avtalsstatusFilter(filter: MkAvtalsfilter): Avtalsstatus {
    switch (filter?.status) {
      case Avtalsstatus.AVTALJUSTERAS:
      case Avtalsstatus.AVTALSIGNERAT:
      case Avtalsstatus.ERSATTNINGUTBETALAS:
      case Avtalsstatus.ERSATTNINGUTBETALD:
      case Avtalsstatus.AVTALSKICKAT:
      case Avtalsstatus.PAMINNELSESKICKAD:
      case Avtalsstatus.AVTALSKONFLIKT:
      case Avtalsstatus.EJBEHANDLAT:
        return filter.status;
      default:
        return null;
    }
  }
}
