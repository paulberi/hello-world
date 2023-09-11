import { Avtalsstatus } from "../../../../../../../generated/markkoll-api";

export class MkAvtalProgressBarPresenter {
  avtalsstatusValue(status: Avtalsstatus): number {
    return this.avtalsstatusRank(status) / this.avtalsstatusRank(Avtalsstatus.ERSATTNINGUTBETALD) * 100.;
  }

  private avtalsstatusRank(status: Avtalsstatus): number {
    switch (status) {
      case Avtalsstatus.EJBEHANDLAT:
        return 0;
      case Avtalsstatus.AVTALSKICKAT:
        return 1;
      case Avtalsstatus.PAMINNELSESKICKAD:
        return 1;
      case Avtalsstatus.AVTALJUSTERAS:
        return 2;
      case Avtalsstatus.AVTALSIGNERAT:
        return 3;
      case Avtalsstatus.ERSATTNINGUTBETALAS:
        return 3;
      case Avtalsstatus.ERSATTNINGUTBETALD:
        return 4;
      case Avtalsstatus.AVTALSKONFLIKT:
      default:
        return 0;
    }
  }
}
