import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { OAuthService } from "angular-oauth2-oidc";
import { LoginService } from "../../../../lib/oidc/login.service";
import { Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { XpUserService } from "../../../../lib/user/user.service";
import { ConfigService } from "../../../../lib/config/config.service";
import { RoleType } from "../../../../../generated/markkoll-api";
import { from, Observable, of } from "rxjs";
import { catchError, map, mergeMap } from "rxjs/operators";
import { MkUserService } from "./user.service";

export const RETURN_URL_PROPERTY = "returnUrl";

@Injectable({
  providedIn: "root"
})
export class AuthService {

  constructor(private loginService: LoginService,
              private oAuthService: OAuthService,
              private router: Router,
              private xpUserService: XpUserService,
              private mkUserService: MkUserService
              ) {}


  /**
   * Starta inloggningsflöde. Om användaren loggas in korrekt och har en godkänd roll så spara ner den.
   * @returns {Observable<boolean>} True om användaren loggades in och har en godkänd roll, annars False.
   */
  public login(): Observable<boolean | UrlTree> {
    if (!window.location.pathname.startsWith("/oauthLogin")) {
      sessionStorage.setItem(RETURN_URL_PROPERTY, window.location.pathname);
    }

    const user = from(this.loginService.loginCodeFlow(ConfigService.authConfig));

    return user.pipe(
      mergeMap(keycloakUser => {
        if (!keycloakUser?.claims?.preferred_username) {
          return of(false);
        }
        return this.mkUserService.fetchMarkkollUser(keycloakUser.claims.preferred_username)
          .pipe(
            map(markkollUser => {
              this.xpUserService.setUser(keycloakUser);
              this.mkUserService.setMarkkollUser(markkollUser);
              return true;
          }),
          catchError(_ => {
            return of(this.router.parseUrl("/notauthorized"));
          })
          );
      })
    );
  }

  checkSpecialAuthRoles(roles: string[]): boolean {
    return this.isValidRoles(roles);
  }

  /**
   * Logga ut användaren.
   */
  public logout() {
    this.xpUserService.setUser(null);
    this.loginService.logout();
    sessionStorage.clear();
  }

  /**
   * Kolla om användaren är inloggad.
   */
  public isLoggedIn(): boolean {
    return this.xpUserService.getUser() != null && this.oAuthService.hasValidAccessToken() && this.mkUserService.getMarkkollUser() != null;
  }

  public isAnalysAllowed(): boolean {
    return false;
  }

  public isAdminAllowed(): boolean {
    return this.mkUserService.getMarkkollUser().roles.filter(val => val.roleType === RoleType.KUNDADMIN).length > 0;
  }

  /**
   * Verifiera att användaren har en godkänd roll för att komma åt applikationen.
   * @param {string[]} roles
   */
  private isValidRoles(roles: string[]): boolean {
    if (roles) {
      for (let i = 0; i < roles.length; i++) {
        if (environment.allowedKeyCloakRole.indexOf(roles[i]) >= 0) {
          return true;
        }
      }
    } else {
      return false;
    }
  }

  trueOrRedirectUrl(state: RouterStateSnapshot) {
    if (state.url.startsWith("/oauthLogin")) {
      const path = sessionStorage.getItem(RETURN_URL_PROPERTY);
      if (path && path !== "/oauthLogin") {
        sessionStorage.removeItem(RETURN_URL_PROPERTY);
        return this.router.parseUrl(path);
      } else {
        return this.router.parseUrl("/");
      }
    }

    return true;
   }

}
