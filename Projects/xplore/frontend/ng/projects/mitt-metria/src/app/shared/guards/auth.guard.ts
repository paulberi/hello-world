import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { reset } from 'ol/transform';
import { Observable } from 'rxjs';
import { ConfigService } from '../../../../../lib/config/config.service';
import { LoginService } from '../../../../../lib/oidc/login.service';
import { XpUserService } from '../../../../../lib/user/user.service';
import { AuthService } from '../data-access/auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanActivateChild {
  constructor(private authService: AuthService,
    private router: Router, private loginService: LoginService,
    private xpUserService: XpUserService,
  ) {

  }
  async canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Promise<boolean | UrlTree> {

    if (!window.location.pathname.startsWith("/oauthLogin")) {
      sessionStorage.setItem("redirectURL", state.url);
    }

    if (this.authService.isLoggedIn()) {
      return true;
    } else {
      return this.loginService.loginCodeFlow(ConfigService.authConfig).then((user) => {
        if (user.loggedIn) {
          if (user.claims.preferred_username) {
            // isLoggedInVendure sets xp user as well
            return this.authService.isLoggedInVendure(user).then(res => {
              if (res) {
                return this.router.parseUrl(sessionStorage.getItem("redirectURL"))
              } else {
                // loginVendure sets xp user as well
                return this.authService.loginVendure(user).then(res => {
                  if (res) {
                    return this.router.parseUrl(sessionStorage.getItem("redirectURL"))
                  } else {
                    return this.router.parseUrl("/notauthorized")
                  }
                })
              }
            }
            );
          } else {
            return this.router.parseUrl("/notauthorized")
          }
        } else {
          return false;
        }
      })
    }
  }

  canActivateChild(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.canActivate(next, state);
  }
}
