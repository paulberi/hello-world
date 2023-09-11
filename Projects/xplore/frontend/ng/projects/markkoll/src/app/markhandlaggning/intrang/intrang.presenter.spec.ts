import { testIntrang } from "../../../test/data";
import { MkIntrangPresenter } from "./intrang.presenter";


describe("MkIntrangPresenter", () => {
  const presenter = new MkIntrangPresenter();

  const intrang = testIntrang();
  const ersattning = 3500;

  it("Ska godta ett ifyllt formulär", () => {
    // When
    presenter.initializeForm(intrang, ersattning);

    // Then
    expect(presenter.form.valid).toBeTruthy();
  });

  it("Ska inte gå att spara ett oförändrat formulär", () => {
    // When
    presenter.initializeForm(intrang, ersattning);

    // Then
    expect(presenter.canSave()).toBeFalsy();
  });

  it("Ska gå att spara ett formulär med förändringar", () => {
    // Given
    presenter.initializeForm(intrang, ersattning);

    // When
    presenter.form.controls.ersattning.setValue(2500);

    // Then
    expect(presenter.canSave()).toBeTruthy();
  });

  it("Ska emitta ett event med ersättning när man sparar formuläret", () => {
    // Given
    const spy = jest.spyOn(presenter.onSubmit, "emit");
    presenter.initializeForm(intrang, ersattning);

    // When
    presenter.submit();

    // Then
    expect(spy).toHaveBeenCalledWith(ersattning);
  });

  it("Ska inte godkänna ett formulär utan ersättning", () => {
    // Given
    presenter.initializeForm(intrang, ersattning);

    // When
    presenter.form.controls.ersattning.setValue(null);

    // Then
    expect(presenter.canSave()).toBeFalsy();
  });
});
