import { ComponentFixture, TestBed } from "@angular/core/testing";
import { RouterTestingModule } from "@angular/router/testing";
import { OAuthModule, OAuthStorage } from "angular-oauth2-oidc";
import { XpErrorService } from "../../../../lib/error/error.service";
import { XpTranslocoTestModule } from "../../../../lib/translate/translocoTest.module.translate";
import { XpUserService } from "../../../../lib/user/user.service";
import { AppShellComponent } from "./app-shell.component";
import { MockService } from "ng-mocks";
import { ApolloModule } from "apollo-angular";
import { XpLayoutV2Module } from "../../../../lib/ui/layout-v2/layout-v2.module";
import { MatDialogModule } from "@angular/material/dialog";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { AuthService } from "../shared/data-access/auth/auth.service";
import { AuthenticateGQL, LogoutGQL } from "../shared/data-access/auth/auth.admin.generated";
import { CartService } from "../../../../lib/vendure/cart/cart.service";

describe("AppShellComponent", () => {
  let fixture: ComponentFixture<AppShellComponent>;
  let component: AppShellComponent;

  let mockLogoutGQL: any;
  let mockXpErrorService: any;
  let mockAuthService: any;
  let mockXpUserService: any;
  let mockAuthenticateGQL: any;
  let mockAuthStorage: any;
  let mockCartService: any;

  beforeEach(async () => {
    mockAuthService = MockService(AuthService, {
      hasAccess: jest.fn()
    });

    await TestBed.configureTestingModule({
      declarations: [AppShellComponent],
      imports: [
        ApolloModule,
        RouterTestingModule,
        HttpClientTestingModule,
        MatDialogModule,
        XpLayoutV2Module,
        XpTranslocoTestModule,
        OAuthModule.forRoot(),
      ],
      providers: [
        { provide: XpErrorService, useValue: mockXpErrorService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: XpUserService, useValue: mockXpUserService },
        { provide: AuthenticateGQL, useValue: mockAuthenticateGQL },
        { provide: OAuthStorage, useValue: mockAuthStorage },
        { provide: LogoutGQL, useValue: mockLogoutGQL },
        { provide: CartService, useValue: mockCartService },
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AppShellComponent);
    component = fixture.componentInstance;
  });

  it("Ska kunna skapa komponent", () => {
    expect(component).toBeTruthy();
  });

  it("Ska visa besällningsfliken om användaren har rollen 'metria_saljare'", () => {
    //Given
    mockAuthService.hasAccess.mockReturnValue(true);

    //When
    component.setupMenu();

    //Then
    expect(component.menuItems).toEqual(
      expect.arrayContaining([
        expect.objectContaining({ title: "Beställningar" })
      ])
    );
  });
});
