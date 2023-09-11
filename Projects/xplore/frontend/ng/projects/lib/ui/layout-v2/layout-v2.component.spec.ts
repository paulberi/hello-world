import { HttpClientTestingModule } from "@angular/common/http/testing";
import { ComponentFixture, TestBed, waitForAsync } from "@angular/core/testing";
import { By } from "@angular/platform-browser";
import { XpLayoutV2Component } from "./layout-v2.component";
import { XpTranslocoTestModule } from "../../translate/translocoTest.module.translate";
import { mockMenuIcons, mockMenuItemsV2 } from "./layout-v2.mock";
import { XpLayoutV2Module } from "./layout-v2.module";
import { RouterTestingModule } from "@angular/router/testing";
import { BrowserAnimationsModule, NoopAnimationsModule } from "@angular/platform-browser/animations";
import { of } from "rxjs";

describe("XpLayoutV2Component", () => {
  let component: XpLayoutV2Component;
  let fixture: ComponentFixture<XpLayoutV2Component>;
  let element: HTMLElement;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        declarations: [XpLayoutV2Component],
        imports: [
          BrowserAnimationsModule,
          HttpClientTestingModule,
          NoopAnimationsModule,
          RouterTestingModule,
          XpLayoutV2Module,
          XpTranslocoTestModule,
        ],
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(XpLayoutV2Component);
    component = fixture.debugElement.componentInstance;
    element = fixture.debugElement.nativeElement;

    component.menuItems = mockMenuItemsV2;
    component.menuIcons = mockMenuIcons;
    component.appName = "Appnamn";
    component.cartBadgeNumber = of(0);
    fixture.detectChanges();
  });

  it("Ska kunna skapa komponent", waitForAsync(() => {
    expect(component).toBeTruthy();
  }));
    
   it("Ska skicka event vid klick på Min Profil", () => {
    const spy = jest.spyOn(component.profileClick, "emit");

    const matMenu = fixture.debugElement.query(By.css(".mat-menu-trigger")).nativeElement;
    matMenu.click();

    const profileButton = fixture.debugElement.query(By.css("#profile-button")).nativeElement;
    profileButton.click();

    expect(spy).toHaveBeenCalledTimes(1);
  });
 
   it("Ska skicka event vid klick på Logga ut", () => {
     const spy = jest.spyOn(component.logoutClick, "emit");

     const matMenu = fixture.debugElement.query(By.css(".mat-menu-trigger")).nativeElement;
     matMenu.click();

     const logoutButton = fixture.debugElement.query(By.css("#logout-button")).nativeElement;
     logoutButton.click();
 
     expect(spy).toHaveBeenCalledTimes(1);
   });
 
   it("Ska skicka event vid klick på menyikoner", () => {
     const spy = jest.spyOn(component.menuIconClick, "emit");
     let menuIcons = fixture.debugElement.queryAll(By.css(".menu-icons button"));
     menuIcons.pop();

     menuIcons[0].nativeElement.click();
 
     expect(spy).toHaveBeenCalledTimes(1);
   });

  it("Ska visa titel för varje sida i menyn", () => {
    const menuItems = fixture.debugElement.queryAll(By.css(".menu li"));

    menuItems.forEach((el, index) => {
      const elementTitle = el.nativeElement
        .querySelector("a")
        .textContent.trim();
      expect(elementTitle).toEqual(component.menuItems[index].title);
    });
  });

  it("Ska visa ikon för varje menyikoner i menyn", () => {
    let menuIcons = fixture.debugElement.queryAll(By.css(".menu-icons button"));
    menuIcons.pop();

    menuIcons.forEach((el, index) => {
      const elementIcon = el.nativeElement
        .querySelector("mat-icon")
        .textContent.trim();
      expect(elementIcon).toEqual(component.menuIcons[index].icon);
    });
  });
});
