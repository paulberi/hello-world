import { MkAvtalFilterPresenter } from "./avtal-filter.presenter";

describe("AvtalsfilterPresenter", () => {
  const avtalsfilterPresenter = new MkAvtalFilterPresenter();
  const filter = {
    search: "HÖLJES 1:23",
    status: "Avtal skickat"
  };

  it("Ska emitta när man ändrar i sökfältet", () => {
    // Given
    avtalsfilterPresenter.initializeForm(filter);
    const spy = jest.spyOn(avtalsfilterPresenter.filterChange, "emit");
    const value = "test";

    // When
    avtalsfilterPresenter.form.controls.searchControl.setValue(value);

    // Then
    expect(spy).toHaveBeenCalledWith({...filter, ...{ search: value }});
  });

  it("Ska emitta när man ändrar i dropdownen", () => {
    // Given
    avtalsfilterPresenter.initializeForm(filter);
    const spy = jest.spyOn(avtalsfilterPresenter.filterChange, "emit");
    const value = "test";

    // When
    avtalsfilterPresenter.form.controls.statusControl.setValue(value);

    // Then
    expect(spy).toHaveBeenCalledWith({...filter, ...{ status: value }});
  });
});
