import { HttpClientTestingModule } from "@angular/common/http/testing";
import { NO_ERRORS_SCHEMA } from "@angular/core";
import { ComponentFixture, TestBed, waitForAsync } from "@angular/core/testing";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { By } from "@angular/platform-browser";
import { MatDividerModule } from "@angular/material/divider";
import { MatMenuModule } from "@angular/material/menu";
import { MatIconModule } from "@angular/material/icon";
import { MatToolbarModule } from "@angular/material/toolbar";
import { XpLayoutComponent } from "./layout.component";
import { XpTranslocoTestModule } from "../../translate/translocoTest.module.translate";
import { mockMenuItems } from "./layout.mock";

describe("XpLayoutComponent", () => {
  let component: XpLayoutComponent;
  let fixture: ComponentFixture<XpLayoutComponent>;
  let element: HTMLElement;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        declarations: [XpLayoutComponent],
        imports: [
          NoopAnimationsModule,
          MatToolbarModule,
          MatIconModule,
          MatMenuModule,
          MatDividerModule,
          HttpClientTestingModule,
          XpTranslocoTestModule,
        ],
        schemas: [NO_ERRORS_SCHEMA],
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(XpLayoutComponent);
    component = fixture.debugElement.componentInstance;
    element = fixture.debugElement.nativeElement;

    component.menuItems = mockMenuItems;
    component.appName = "Appnamn";
    component.loggedInUserInfo = {
      id: "",
      fornamn: "Test",
      efternamn: "Testsson",
      email: "test@test",
      kund: "Kund"
    };
    fixture.detectChanges();
  });

  it("Ska kunna skapa komponent", waitForAsync(() => {
      expect(component).toBeTruthy();
    })
  );

  it("Ska visa logotyp och titel", () => {
    const logo = fixture.debugElement.query(By.css("#logo"));
    const titel = fixture.debugElement.query(By.css("#app-name"));

    expect(titel).not.toBeNull();
    expect(logo).toBeTruthy();
  });

  it("Ska fälla ner menyn vid klick på menyknappen", () => {
    fixture.debugElement
      .query(By.css(".mat-menu-trigger"))
      .triggerEventHandler("click", {});
    fixture.detectChanges();

    expect(
      fixture.debugElement.query(By.css(".mat-menu-trigger")).attributes[
        "aria-expanded"
      ]
    ).toEqual("true");
  });

  it("Ska skicka event vid klick på Logga ut", () => {
    const spy = jest.spyOn(component.logoutClick, "emit");

    fixture.debugElement
      .query(By.css(".mat-menu-trigger"))
      .triggerEventHandler("click", {});
    fixture.debugElement
      .query(By.css("#logout-button"))
      .triggerEventHandler("click", {});

    expect(spy).toHaveBeenCalledTimes(1);
  });

  it("Ska skicka event vid klick på Hjälp", () => {
    const spy = jest.spyOn(component.helpClick, "emit");

    fixture.debugElement
      .query(By.css(".mat-menu-trigger"))
      .triggerEventHandler("click", {});
    fixture.debugElement
      .query(By.css("#help-button"))
      .triggerEventHandler("click", {});

    expect(spy).toHaveBeenCalledTimes(1);
  });

  it("Ska visa menyn om det finns menydata", () => {
    const menu = fixture.debugElement.query(By.css("nav#main-menu"));
    expect(menu).toBeTruthy();
  });

  it("Ska ta bort menyn om det inte finns något menydata", () => {
    component.menuItems = undefined;
    fixture.detectChanges();
    const menu = fixture.debugElement.query(By.css("#main-menu"));

    expect(menu).toBeFalsy();
  });

  it("Ska visa titel för varje huvudsida i menyn", () => {
    const menuItems = fixture.debugElement.queryAll(By.css("#main-menu .menu-item"));

    menuItems.forEach((el, index) => {
      const elementTitle = el.nativeElement
        .querySelector("span")
        .textContent.trim();
      expect(elementTitle).toEqual(component.menuItems[index].title);
    });
  });

  it("Ska visa ikon för varje huvudsida i menyn", () => {
    const menuItems = fixture.debugElement.queryAll(By.css("#main-menu .menu-item"));
    const matIconTagName = "MAT-ICON";

    menuItems.forEach((el, index) => {
      const nativeElement = el.query(By.css("mat-icon")).nativeElement;
      expect(nativeElement.textContent).toBe(component.menuItems[index].icon);
      expect(nativeElement.tagName).toBe(matIconTagName);
    });
  });
});
