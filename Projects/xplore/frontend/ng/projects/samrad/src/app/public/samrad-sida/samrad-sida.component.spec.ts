import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { RouterTestingModule } from "@angular/router/testing";
import { SamradSidaComponent } from "./samrad-sida.component";
import { XpTranslocoTestModule } from "../../../../../lib/translate/translocoTest.module.translate";

describe("SamradSidaComponent", () => {
  let component: SamradSidaComponent;
  let fixture: ComponentFixture<SamradSidaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        XpTranslocoTestModule,
      ],
      declarations: [SamradSidaComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SamradSidaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
