import { MkAgareEditComponent } from "./agare-edit.component";
import * as ngMocks from "ng-mocks";
import { MockBuilder, MockRender } from "ng-mocks";
import { ReactiveFormsModule } from "@angular/forms";
import { MkAgareEditModule } from "./agare-edit.module";
import { clickButtonBySelector } from "../../../test/jest-util";
import { testMarkagare } from "../../../test/data";

describe("RedigeraAgareComponent", () => {
  const agare = testMarkagare();
  ngMocks.ngMocks.faster();

  beforeAll(() => {
    return MockBuilder(
      MkAgareEditComponent,
      MkAgareEditModule
    ).keep(ReactiveFormsModule);
  });

  it("should create", () => {
    const fixture = MockRender(MkAgareEditComponent);

    expect(fixture.point.componentInstance)
      .toEqual(expect.any(MkAgareEditComponent));
  });

  it("ska emitta ett event när man trycker på spara", () => {
    // Given
    const fixture = MockRender(MkAgareEditComponent);
    const component = fixture.componentInstance;
    component.agare = agare;

    fixture.detectChanges();
    const spy = jest.spyOn(component.agareChange, "emit");

    // When
    clickButtonBySelector(fixture, ".agare-save");

    // Then
    expect(spy).toHaveBeenCalledWith(agare);
  });
});
