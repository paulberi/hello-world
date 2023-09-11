import { testAvtalskartaInteMittlinjeredovisad, testAvtalskartaLineString, testAvtalskartaMultiLineString } from "../../../test/data";
import { MkAvtalMap } from "../../model/avtalskarta";
import { MkSamfallighetPresenter } from "./samfallighet.presenter";

describe(MkSamfallighetPresenter.name, () => {
  const presenter = new MkSamfallighetPresenter();

  it("Ska vara mittlinjeredovisad om det finns områden av typen LineString", () => {
    // Given
    const avtalskarta: MkAvtalMap = testAvtalskartaLineString;

    // When
    const isMittLinjeredovisad = presenter.isMittlinjeRedovisad(avtalskarta);

    // Then
    expect(isMittLinjeredovisad).toBeTruthy();
  });

  it("Ska vara mittlinjeredovisad om det finns områden av typen MultiLineString", () => {
    // Given
    const avtalskarta: MkAvtalMap = testAvtalskartaMultiLineString;

    // When
    const isMittLinjeredovisad = presenter.isMittlinjeRedovisad(avtalskarta);

    // Then
    expect(isMittLinjeredovisad).toBeTruthy();
  });

  it("Ska inte vara mittlinjeredovisad för andra geometrityper", () => {
    // Given
    const avtalskarta: MkAvtalMap = testAvtalskartaInteMittlinjeredovisad;

    // When
    const isMittLinjeredovisad = presenter.isMittlinjeRedovisad(avtalskarta);

    // Then
    expect(isMittLinjeredovisad).not.toBeTruthy();
  });
});
