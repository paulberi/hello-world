import { ComponentFixture, TestBed, waitForAsync } from "@angular/core/testing";
import { MatDialogModule } from "@angular/material/dialog";
import { RouterTestingModule } from "@angular/router/testing";
import { of } from "rxjs";
import { AuthService } from "../services/auth.service";

import { AppShellComponent } from "./app-shell.component";
import { ProfileComponent } from "./profile/profile.component";
import { CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";
import { MatFormFieldModule } from "@angular/material/form-field";
import { ReactiveFormsModule } from "@angular/forms";
import { MatInputModule } from "@angular/material/input";
import { XpUserProfileComponent } from "../../../../lib/user/user-profile/user-profile.component";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { XpTranslocoTestModule } from "../../../../lib/translate/translocoTest.module.translate";
import { XpErrorService } from "../../../../lib/error/error.service";
import { MockService } from "ng-mocks";
import { MkUserService } from "../services/user.service";

describe("AppShellComponent", () => {
  let mockAuthService: any;
  let mockMkUserService: MkUserService;
  let mockXpErrorService: any;
  let component: AppShellComponent;
  let fixture: ComponentFixture<AppShellComponent>;

  beforeEach(waitForAsync(() => {
    mockAuthService = MockService(AuthService, {
      logout: jest.fn(),
      isAnalysAllowed: jest.fn(),
      isAdminAllowed: jest.fn()
    });

    mockMkUserService = MockService(MkUserService, {
      getMarkkollUser$: jest.fn().mockReturnValue(of({}))
    });
    
    mockXpErrorService = {
      setErrorMap: jest.fn(),
    };
    TestBed.configureTestingModule({
      declarations: [ AppShellComponent, ProfileComponent, XpUserProfileComponent],
      imports: [
        MatFormFieldModule,
        ReactiveFormsModule,
        MatInputModule,
        MatDialogModule,
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([]),
        XpTranslocoTestModule,        
      ],
      providers: [
        {
          provide: XpErrorService, useValue: mockXpErrorService,
        },
        {
          provide: AuthService, useValue: mockAuthService,
        }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
    .compileComponents();
  }));

  beforeEach(waitForAsync(() => {
    fixture = TestBed.createComponent(AppShellComponent);
    component = fixture.componentInstance;
    component.markkollUser = of({id: "test@test.se", fornamn: "Test", efternamn: "Testsson", email: "test@test", kundId: "KundID"});
    fixture.detectChanges();
  }));

  it("Ska kunna skapa komponent", () => {
    mockAuthService.isAnalysAllowed.mockReturnValue(false);
    expect(component).toBeDefined();
  });

  it("Ska kunna skapa komponent med analysmodulen aktiv", () => {
    mockAuthService.isAnalysAllowed.mockReturnValue(true);
    expect(component).toBeDefined();
  });
});
