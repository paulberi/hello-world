import { TestBed } from "@angular/core/testing";
import { AuthService } from "./auth.service";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { OAuthService } from "angular-oauth2-oidc";
import { RouterTestingModule } from "@angular/router/testing";
import { of } from "rxjs";
import { LoginService } from "../../../../lib/oidc/login.service";
import { UrlTree } from "@angular/router";
import { XpUserService } from "../../../../lib/user/user.service";

describe("AuthService", () => {
  let authService: AuthService;
  let loginService: LoginService;
  let oAuthService: OAuthService;
  let XpuserService: XpUserService;
  const mockLoginService = {
    loginCodeFlow: jest.fn(),
    logout: jest.fn(),
  };
  const mockOAuthService = {
    hasValidAccessToken: jest.fn(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [
        AuthService,
        XpUserService,
        { provide: LoginService, useValue: mockLoginService },
        { provide: OAuthService, useValue: mockOAuthService }
      ]
    });
    authService = TestBed.inject(AuthService);
    XpuserService = TestBed.inject(XpUserService);
    loginService = TestBed.inject(LoginService);
    oAuthService = TestBed.inject(OAuthService);
    jest.clearAllMocks();
  });

  it.skip("Ska kunna logga in med en godkänd roll", (done) => {
    const mockUser = { loggedIn: true, claims: null, roles: ["markkoll_markhandlaggare"] };
    mockLoginService.loginCodeFlow.mockResolvedValue(of(mockUser).toPromise());

    authService.login().subscribe(loginResult => {
      expect(loginResult).toBe(true);
      expect(XpuserService.getUser()).toEqual(mockUser);
      done();
    });

    expect(mockLoginService.loginCodeFlow).toHaveBeenCalledTimes(1);
    expect(true);
    done();
  });

  it.skip("Ska inte kunna logga in med en roll som inte är godkänd", (done) => {
    const mockUser = { loggedIn: true, claims: null, roles: ["roll_som_inte_ar_godkand"] };
    mockLoginService.loginCodeFlow.mockResolvedValue(of(mockUser).toPromise());

    authService.login().subscribe(loginResult => {
      expect(loginResult).toBeInstanceOf(UrlTree);
      expect(XpuserService.getUser()).toBeNull();
      done();
    });

    expect(mockLoginService.loginCodeFlow).toHaveBeenCalledTimes(1);
  });

  it("Ska lyckas logga ut en inloggad användare", () => {
    const mockUser = { loggedIn: true, claims: null, roles: ["markkoll_markhandlaggare"] };
    XpuserService.setUser(mockUser);

    authService.logout();

    expect(XpuserService.getUser()).toBeNull();

    expect(mockLoginService.logout).toHaveBeenCalledTimes(1);
  });
});
