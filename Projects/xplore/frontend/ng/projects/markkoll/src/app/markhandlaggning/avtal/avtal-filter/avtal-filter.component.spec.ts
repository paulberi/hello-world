import { MockBuilder, MockRender, ngMocks } from "ng-mocks";
import { MkAvtalFilterModule } from "./avtal-filter.module";
import { MkAvtalFilterComponent } from "./avtal-filter.component";
import { ReactiveFormsModule } from "@angular/forms";

describe("MkAvtalFilterComponent", () => {
  ngMocks.faster();

  beforeAll(() => {
    return MockBuilder(
      MkAvtalFilterComponent,
      MkAvtalFilterModule
    ).keep(ReactiveFormsModule);
  });

  it("should create", () => {
    const fixture = MockRender(MkAvtalFilterComponent);

    expect(fixture.point.componentInstance)
    .toEqual(expect.any(MkAvtalFilterComponent));
  });
});
