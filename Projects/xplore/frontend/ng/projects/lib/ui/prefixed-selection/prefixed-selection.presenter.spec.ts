import {XpPrefixedSelectionPresenter} from "./prefixed-selection.presenter";
import {XpSelectOption} from "../model/selectOption";

describe("PrefixedSelectionPresenter", () => {
  const prefixedSelectionPresenter = new XpPrefixedSelectionPresenter();

  it("Ska emmita när man ändrar i dropdown", () => {
    // Given
    prefixedSelectionPresenter.initializeForm("", false);
    const spy = jest.spyOn(prefixedSelectionPresenter.selectionChange, "emit");
    const value = "IB";

    // When
    prefixedSelectionPresenter.form.controls.selectionControl.setValue(value);

    // Then
    expect(spy).toHaveBeenCalledWith(value);
  });
});
