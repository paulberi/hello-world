import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Observable, of} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {UserService} from "./services/user.service";
import {RETURN_URL_PROPERTY} from "./authentication-interceptor";
import {LoginService, User} from "../../../lib/oidc/login.service";
import {OAuthService} from "angular-oauth2-oidc";
import {HttpErrorResponse} from "@angular/common/http";
import {ConfigService} from "../../../lib/config/config.service";
import { XpUserService } from "../../../lib/user/user.service";

@Injectable({
  providedIn: "root"
})
export class AuthGuard implements CanActivate, CanActivateChild {
  constructor(private router: Router,
              private oauthService: OAuthService,
              private userService: UserService,
              private loginService: LoginService,
              private xpUserService: XpUserService) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.isLoggedIn()) {
      if (this.checkRoles(next, this.xpUserService.getUser())) {
        return this.trueOrRedirectUrl(state);
      }
      return of(this.router.parseUrl("/"));
    } else {
      if (!window.location.pathname.startsWith("/oauthLogin")) {
        sessionStorage.setItem(RETURN_URL_PROPERTY, window.location.pathname);
      }

      return this.loginService.loginCodeFlow(ConfigService.authConfig).then(userClaim => {
        if (this.checkRoles(next, userClaim)) {
          this.xpUserService.setUser(userClaim);
          return true;
        } else {
          return this.router.parseUrl("/notauthorized");
        }
      });
    }
  }

  canActivateChild(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.canActivate(next, state);
  }

  checkRoles(route: ActivatedRouteSnapshot, user: User): boolean {
    let access = false;
    if (route.data.roles && user.roles) {
      route.data.roles.forEach(role => {
        if (user.roles.includes(role)) {
          access = true;
        }
      });
    }
    return access;
  }

  public isLoggedIn(): boolean {
    return this.xpUserService.getUser() != null && this.oauthService.hasValidAccessToken();
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
