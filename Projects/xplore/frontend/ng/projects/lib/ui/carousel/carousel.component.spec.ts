import { ComponentFixture, TestBed, waitForAsync } from "@angular/core/testing";
import { registerLocaleData } from "@angular/common";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { XpCarouselComponent } from "./carousel.component";
import localeSv from "@angular/common/locales/sv";
import { XpTranslocoTestModule } from "../../translate/translocoTest.module.translate";
registerLocaleData(localeSv, "sv");

describe("XpCarouselComponent", () => {
  let component: XpCarouselComponent;
  let fixture: ComponentFixture<XpCarouselComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [XpCarouselComponent],
      imports: [
        NoopAnimationsModule,
        MatButtonModule,
        MatIconModule,
        XpTranslocoTestModule
      ],
    })
    .compileComponents();

    fixture = TestBed.createComponent(XpCarouselComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it("Ska visa komponent", () => {
    expect(component).toBeTruthy();
  });
});
