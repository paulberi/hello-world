import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { TestBed } from "@angular/core/testing";
import { OAuthService } from "angular-oauth2-oidc";

import { XpUserService } from "./user.service";

describe("XpUserService", () => {
  let service: XpUserService;
  let httpMock: HttpTestingController;
  let mockOAuthService: any;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        XpUserService,
        {provide: OAuthService, useValue: mockOAuthService},
    ],
    });
    service = TestBed.inject(XpUserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it("Ska kunna skapas", () => {
    const xpUserService: XpUserService = TestBed.inject(XpUserService);
    expect(xpUserService).toBeTruthy();
  });
});

