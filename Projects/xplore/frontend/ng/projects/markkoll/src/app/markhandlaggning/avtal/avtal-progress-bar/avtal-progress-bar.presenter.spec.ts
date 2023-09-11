import { Avtalsstatus } from "../../../../../../../generated/markkoll-api";
import { MkAvtalProgressBarPresenter } from "./avtal-progress-bar.presenter"

describe("AvtalsstatusProgressBarPresenter", () => {
  let presenter: MkAvtalProgressBarPresenter = new MkAvtalProgressBarPresenter();

  it("Ska visa tom mätare för Avtalskonflikt", () => {
    const value = presenter.avtalsstatusValue(Avtalsstatus.AVTALSKONFLIKT);

    expect(value).toEqual(0.);
  });

  it("Ska visa tom mätare för Ej Behandlat", () => {
    const ejBehandlat = presenter.avtalsstatusValue(Avtalsstatus.EJBEHANDLAT);

    expect(ejBehandlat).toEqual(0.);
  });

  it("Ska visa full mätare för Ersättning utbetald", () => {
    const ersattningUtbetald = presenter.avtalsstatusValue(Avtalsstatus.ERSATTNINGUTBETALD);

    expect(ersattningUtbetald).toEqual(100.);
  });

  it("Ska ha korrekt rangordnade statusar", () => {
    const ejBehandlat = presenter.avtalsstatusValue(Avtalsstatus.EJBEHANDLAT);
    const avtalSkickat = presenter.avtalsstatusValue(Avtalsstatus.AVTALSKICKAT);
    const avtalJusteras = presenter.avtalsstatusValue(Avtalsstatus.AVTALJUSTERAS);
    const avtalSignerat = presenter.avtalsstatusValue(Avtalsstatus.AVTALSIGNERAT);

    expect(ejBehandlat).toBeLessThan(avtalSkickat);
    expect(avtalSkickat).toBeLessThan(avtalJusteras);
    expect(avtalJusteras).toBeLessThan(avtalSignerat);
  });
});
