import { registerLocaleData } from "@angular/common";
import { ComponentFixture, TestBed, waitForAsync } from "@angular/core/testing";
import { MatIconModule } from "@angular/material/icon";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { XpTranslocoTestModule } from "../../translate/translocoTest.module.translate";
import { XpVerticalTabsComponent } from "./vertical-tabs.component";
import localeSv from "@angular/common/locales/sv";
import { MatButtonToggleModule } from "@angular/material/button-toggle";
registerLocaleData(localeSv, "sv");

describe("XpVerticalTabsComponent", () => {
  let component: XpVerticalTabsComponent;
  let fixture: ComponentFixture<XpVerticalTabsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [XpVerticalTabsComponent],
      imports: [
        MatButtonToggleModule,
        NoopAnimationsModule,
        MatIconModule,
        XpTranslocoTestModule
      ],
    })
    .compileComponents();

    fixture = TestBed.createComponent(XpVerticalTabsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));


  it("Ska skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});

