import { TestBed, ComponentFixture, waitForAsync, fakeAsync, tick } from "@angular/core/testing";
import { XpNotFoundComponent } from "./not-found.component";
import { MatIconModule } from "@angular/material/icon";
import { RouterTestingModule } from "@angular/router/testing";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { Router } from "@angular/router";
import { Location } from "@angular/common";
import { By } from "@angular/platform-browser";
import { XpTranslocoTestModule } from "../../../translate/translocoTest.module.translate";


describe("XpNotFoundComponent", () => {
  let fixture: ComponentFixture<XpNotFoundComponent>;
  let component: XpNotFoundComponent;
  let element: HTMLElement;
  let location: Location;
  let router: Router;
  let title: string;
  let linkTitle: string;
  let linkUrl: string;

  beforeEach(waitForAsync(() => {   

    TestBed.configureTestingModule({
      declarations: [
        XpNotFoundComponent
      ],
      imports: [
        MatIconModule,
        XpTranslocoTestModule,
        RouterTestingModule.withRoutes([
          { path: "start", component: XpNotFoundComponent }
        ]),
        HttpClientTestingModule
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(XpNotFoundComponent);
    component = fixture.componentInstance;
    element = fixture.nativeElement;
    router = TestBed.inject(Router);
    location = TestBed.inject(Location);

    title = "Kunde inte hitta sida";
    linkTitle = "Gå till start";
    linkUrl = "/start";
    fixture.detectChanges();
  }));

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeDefined();
  });

  it("Ska ha rubrik med bestämd titel", () => {
    component.title = title;
    fixture.detectChanges();

    const headerElement = element.querySelector("h1");
    expect(headerElement.textContent).toContain(title);
  });

  it("Ska ha knapp med bestämd titel", () => {
    component.linkTitle = linkTitle;
    component.linkUrl = linkUrl;
    fixture.detectChanges();

    const button = element.querySelector("button");
    expect(button.textContent).toContain(linkTitle);
  });

  it("Ska kunna klicka på knapp och navigera till bestämd länk", fakeAsync(() => {
    component.linkTitle = linkTitle;
    component.linkUrl = linkUrl;
    fixture.detectChanges();

    fixture.debugElement.query(By.css("button")).nativeElement.click();
    tick();

    expect(location.path()).toBe(linkUrl);
  }));
});
