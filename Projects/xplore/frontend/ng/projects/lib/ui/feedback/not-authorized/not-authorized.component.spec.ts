import { HttpClientModule } from "@angular/common/http";
import { TestBed, ComponentFixture, waitForAsync, getTestBed } from "@angular/core/testing";
import { By } from "@angular/platform-browser";
import { RouterTestingModule } from "@angular/router/testing";
import { LoginService } from "../../../oidc/login.service";
import { XpTranslocoTestModule } from "../../../translate/translocoTest.module.translate";
import { XpNotAuthorizedComponent } from "./not-authorized.component";

describe("XpNotAuthorizedComponent", () => {
  let mockLoginService: any;
  let fixture: ComponentFixture<XpNotAuthorizedComponent>;
  let component: XpNotAuthorizedComponent;
  let element: HTMLElement;

  beforeEach(waitForAsync(() => {
    mockLoginService = {
      logout: jest.fn()
    };

    TestBed.configureTestingModule({
      declarations: [
        XpNotAuthorizedComponent
      ],
      imports: [
        RouterTestingModule,
        HttpClientModule,
        XpTranslocoTestModule,
      ],
      providers: [
        { provide: LoginService, useValue: mockLoginService }
      ]
    }).compileComponents();


    fixture = TestBed.createComponent(XpNotAuthorizedComponent);
    component = fixture.componentInstance;
    element = fixture.nativeElement;
  }));

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeDefined();
  });

  it("Ska ha rubrik h1", () => {
    const headerElement = element.querySelector("h1");
    expect(headerElement).not.toBeNull();
  });

  it("Ska ha knapp med texten Logga ut", (done) => {
    expect.assertions(1);

    fixture.whenStable().then(() => {
      fixture.detectChanges();

      const button = element.querySelector("button");
      expect(button.textContent).toContain("xp.common.logOut");

      done();
    });
  });

  it("Ska anropa AuthService.logout() vid klick på knapp", () => {
    fixture.debugElement.query(By.css("button")).triggerEventHandler("click", {});

    expect(mockLoginService.logout).toHaveBeenCalledWith();
  });
});
