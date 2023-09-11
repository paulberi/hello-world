import { ComponentFixture, TestBed } from "@angular/core/testing";
import { TranslocoTestingModule } from "@ngneat/transloco";
import { MkAvtalGenerateButtonComponent } from "./avtal-generate-button.component";

describe("AvtalGenerateButtonComponent", () => {
  let component: MkAvtalGenerateButtonComponent;
  let fixture: ComponentFixture<MkAvtalGenerateButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MkAvtalGenerateButtonComponent],
      imports: [TranslocoTestingModule]
    }).compileComponents();

    fixture = TestBed.createComponent(MkAvtalGenerateButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", async () => {
    await fixture.whenStable();
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });
});
