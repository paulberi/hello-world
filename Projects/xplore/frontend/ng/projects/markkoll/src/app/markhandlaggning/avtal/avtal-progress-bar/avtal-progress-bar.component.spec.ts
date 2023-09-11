import { ComponentFixture, TestBed } from "@angular/core/testing";
import { MatProgressBar } from "@angular/material/progress-bar";
import { MockComponent } from "ng-mocks";

import { MkAvtalProgressBarComponent } from "./avtal-progress-bar.component";

describe("AvtalsstatusProgressBarComponent", () => {
  let component: MkAvtalProgressBarComponent;
  let fixture: ComponentFixture<MkAvtalProgressBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        MkAvtalProgressBarComponent,
        MockComponent(MatProgressBar)
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MkAvtalProgressBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
