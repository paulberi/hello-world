import {MockBuilder, MockRender, ngMocks} from "ng-mocks";
import {XpPrefixedSelectionComponent} from "./prefixed-selection.component";
import {XpPrefixedSelectionModule} from "./prefixed-selection.module";
import {ReactiveFormsModule} from "@angular/forms";

describe("XpPrefixedSelectionComponent", () => {
  ngMocks.faster();

  beforeAll(() => {
    return MockBuilder(
      XpPrefixedSelectionComponent,
      XpPrefixedSelectionModule
    ).keep(ReactiveFormsModule);
  });

  it("Should create", () => {
    const fixure = MockRender(XpPrefixedSelectionComponent);

    expect(fixure.point.componentInstance).toEqual(expect.any(XpPrefixedSelectionComponent));
  });
});
