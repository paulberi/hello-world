import { ReactiveFormsModule } from "@angular/forms";
import { MockBuilder, MockRender, ngMocks } from "ng-mocks";
import { testOmbud } from "../../../test/data";
import { clickButtonBySelector } from "../../../test/jest-util";
import { MkOmbudAddComponent } from "./ombud-add.component";
import { MkOmbudAddModule } from "./ombud-add.module";

describe("AddOmbudComponent", () => {
  const ombud = testOmbud();
  ngMocks.faster();

  beforeAll(() => {
    return MockBuilder(
      MkOmbudAddComponent,
      MkOmbudAddModule,
    ).keep(ReactiveFormsModule);
  });

  it("should create", () => {
    const fixture = MockRender(MkOmbudAddComponent);

    expect(fixture.point.componentInstance)
      .toEqual(expect.any(MkOmbudAddComponent));
  });

  it("ska emitta ett event när man trycker på spara", () => {
    // Given
    const fixture = MockRender(MkOmbudAddComponent);
    const component = fixture.componentInstance;
    component.ombud = ombud;

    const spy = jest.spyOn(component.ombudChange, "emit");
    fixture.detectChanges();

    // When
    clickButtonBySelector(fixture, ".ombud-save");

    // Then
    expect(spy).toHaveBeenCalledWith(ombud);
  });

  it("ska emitta ett event när man trycker på avbryt", () => {
    // Given
    const fixture = MockRender(MkOmbudAddComponent);
    const component = fixture.componentInstance;

    const spy = jest.spyOn(component.cancel, "emit");
    fixture.detectChanges();

    // When
    clickButtonBySelector(fixture, ".ombud-cancel");

    // Then
    expect(spy).toHaveBeenCalled();
  });
});
