import { ReactiveFormsModule } from "@angular/forms";
import { MockBuilder, MockRender, ngMocks } from "ng-mocks";
import { testOmbud } from "../../../test/data";
import { clickButtonBySelector } from "../../../test/jest-util";
import { MkOmbudEditComponent } from "./ombud-edit.component";
import { MkOmbudEditModule } from "./ombud-edit.module";

describe("RedigeraOmbudComponent", () => {
  const ombud = testOmbud();
  ngMocks.faster();

  beforeAll(() => {
    return MockBuilder(
      MkOmbudEditComponent,
      MkOmbudEditModule,
    ).keep(ReactiveFormsModule);
  });

  it("should create", () => {
    const fixture = MockRender(MkOmbudEditComponent);

    expect(fixture.point.componentInstance)
      .toEqual(expect.any(MkOmbudEditComponent));
  });

  it("ska emitta ett event när man trycker på spara", () => {
    // Given
    const fixture = MockRender(MkOmbudEditComponent);
    const component = fixture.componentInstance;
    component.ombud = ombud;
    const spy = jest.spyOn(component.ombudChange, "emit");
    fixture.detectChanges();

    // When
    clickButtonBySelector(fixture, ".ombud-save");

    // Then
    expect(spy).toHaveBeenCalledWith(ombud);
  });

  it("ska emitta ett event när man väljer att ta bort ett ombud", () => {
    // Given
    const fixture = MockRender(MkOmbudEditComponent);
    const component = fixture.componentInstance;
    component.ombud = ombud;
    const spy = jest.spyOn(component.delete, "emit");
    fixture.detectChanges();

    // When
    clickButtonBySelector(fixture, ".ombud-delete");

    // Then
    expect(spy).toHaveBeenCalled();
  });
});
