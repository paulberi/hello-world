import { HttpClientTestingModule } from "@angular/common/http/testing";
import { CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";
import { ComponentFixture, TestBed, waitForAsync } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";

import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { of } from "rxjs";
import { XpUserService } from "../user.service";

import { XpUserProfileComponent } from "./user-profile.component";
import { XpTranslocoTestModule } from "../../translate/translocoTest.module.translate";

describe("UserProfileComponent", () => {
  let component: XpUserProfileComponent;
  let fixture: ComponentFixture<XpUserProfileComponent>;
  let element: HTMLElement;
  let mockXpUserService: any;

  beforeEach(waitForAsync(() => {
    mockXpUserService = {
      getUser$: jest.fn(_ => of({
        loggedIn: true,
        claims: {name: "Test Testsson", given_name: "Test", family_name: "Testsson", email: "test@test"},
        roles: ["markkoll_markagare"]
      })),
      updateKeyCloakEmail$: jest.fn(_ => of(true)),
      updateKeyCloakPassword$: jest.fn(_ => of(true))
    };
    TestBed.configureTestingModule({
      declarations: [ XpUserProfileComponent ],
      imports: [
        NoopAnimationsModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        ReactiveFormsModule,
        HttpClientTestingModule,
        XpTranslocoTestModule
      ],
      providers: [
        {provide: XpUserService, useValue: mockXpUserService}
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
    .compileComponents();
  }));


  beforeEach(() => {
    fixture = TestBed.createComponent(XpUserProfileComponent);
    component = fixture.componentInstance;
    element = fixture.nativeElement;
    fixture.autoDetectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it("Ska ha 2 formulär", () => {
    const forms = element.querySelector("form");
    expect(forms.length === 2);
  });

  it("Ska visa användarens epost i formuläret", () => {
    let user;
    mockXpUserService.getUser$().subscribe(res => {
      user = res;
    });
    const inputs = element.querySelectorAll("input");

    expect(inputs[0].value).toBe(user.claims.email);
  });

  it("Ska skicka request vid klick på \"Spara\"", () => {
    const button = element.querySelectorAll("button");
    button[0].click();

    expect(mockXpUserService.updateKeyCloakEmail$).toBeCalledTimes(1);
  });

  it("Ska skicka request vid klick på \"Byt lösenord\"", () => {
    const button = element.querySelectorAll("button");
    const inputs = element.querySelectorAll("input");
    inputs[1].value = "lösenord";
    inputs[2].value = "minst16teckenlångt";
    inputs[3].value = "minst16teckenlångt";

    button[4].disabled = false;
    button[4].click();

    expect(mockXpUserService.updateKeyCloakPassword$).toHaveBeenCalledTimes(1);
  });
});
