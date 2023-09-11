import { TestBed, ComponentFixture, waitForAsync } from "@angular/core/testing";
import { RouterTestingModule } from "@angular/router/testing";
import { AppComponent } from "./app.component";
import { XpTranslocoTestModule } from "../../../lib/translate/translocoTest.module.translate";
import { MkUpToDateModule } from "./common/up-to-date/up-to-date.module";

describe("AppComponent", () => {
  let fixture: ComponentFixture<AppComponent>;
  let component: AppComponent;
  let element: HTMLElement;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent
      ],
      imports: [
        RouterTestingModule,
        MkUpToDateModule,
        XpTranslocoTestModule,
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    element = fixture.nativeElement;
  }));

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeDefined();
  });

  it("Ska ha router-outlet element", () => {
    const routerOutletElement = element.querySelector("router-outlet");
    expect(routerOutletElement).toBeTruthy();
  });
});
