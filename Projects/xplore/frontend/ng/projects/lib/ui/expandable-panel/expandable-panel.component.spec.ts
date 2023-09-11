import { registerLocaleData } from "@angular/common";
import { ComponentFixture, TestBed, waitForAsync } from "@angular/core/testing";
import { MatIconModule } from "@angular/material/icon";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { XpTranslocoTestModule } from "../../translate/translocoTest.module.translate";
import { XpExpandablePanelComponent } from "./expandable-panel.component";
import localeSv from "@angular/common/locales/sv";
registerLocaleData(localeSv, "sv");

describe("XpExpandablePanelComponent", () => {
  let component: XpExpandablePanelComponent;
  let fixture: ComponentFixture<XpExpandablePanelComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [XpExpandablePanelComponent],
      imports: [
        NoopAnimationsModule,
        MatIconModule,
        XpTranslocoTestModule
      ],
    })
    .compileComponents();

    fixture = TestBed.createComponent(XpExpandablePanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));


  it("Ska skapa komponent", () => {
    expect(component).toBeTruthy();
  });
});

