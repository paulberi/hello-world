import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Observable, of} from "rxjs";
import {UserService} from "./services/user.service";
import {catchError, map} from "rxjs/operators";
import {RETURN_URL_PROPERTY} from "../../../lib/http/authentication-interceptor";
import {LoginService} from "../../../lib/oidc/login.service";
import {OAuthService} from "angular-oauth2-oidc";
import {HttpErrorResponse} from "@angular/common/http";
import {ConfigService} from "../../../lib/config/config.service";

@Injectable({
  providedIn: "root"
})
export class AuthGuard implements CanActivate, CanActivateChild {
  constructor(private router: Router,
              private oauthService: OAuthService,
              private userDetailsService: UserService,
              private loginService: LoginService) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.userDetailsService.userDetails != null) {
      return this.trueOrRedirectUrl(state);
    } else {
      if (this.loginService.specialAuth) {
        return this.userDetailsService.loadUserDetails().pipe(map(() => {
            return this.trueOrRedirectUrl(state);
          }), catchError((err: HttpErrorResponse) => {
            if (err.status === 403) {
              return of(this.router.parseUrl("/notauthorized"));
            }

            throw err;
          })
        ).toPromise();
      } else {
        if (!window.location.pathname.startsWith("/oauthLogin")) {
          sessionStorage.setItem(RETURN_URL_PROPERTY, window.location.pathname);
        }

        return this.loginService.loginCodeFlow(ConfigService.authConfig).then(userClaim => {
          return this.userDetailsService.loadUserDetails().pipe(map(() => {
              return this.trueOrRedirectUrl(state);
            }), catchError((err: HttpErrorResponse) => {
              if (err.status === 403) {
                return of(this.router.parseUrl("/notauthorized"));
              }

              throw err;
            })
          ).toPromise();
        });
      }
    }
  }

  canActivateChild(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.canActivate(next, state);
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
