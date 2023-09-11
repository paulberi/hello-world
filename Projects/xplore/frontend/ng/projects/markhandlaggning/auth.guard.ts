import { Injectable } from "@angular/core";
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, CanActivateChild, Router } from "@angular/router";
import { OAuthService } from "angular-oauth2-oidc";
import { Observable, of } from "rxjs";
import { LoginService } from "../lib/oidc/login.service";
import { AuthService } from "./src/app/services/auth.service";
import { environment } from "./src/environments/environment";

@Injectable({
  providedIn: "root"
})
export class AuthGuard implements CanActivate, CanActivateChild {
  constructor(private loginService: LoginService, 
              private authService: AuthService, 
              private outhService: OAuthService, 
              private router: Router) {

  }
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      if (this.authService.user != null && this.outhService.hasValidAccessToken()) {
        return true;
      } else {
        return this.loginService.loginCodeFlow(this.authService.authConfig).then(user => {
          this.authService.user = user;
          if (user.roles) {
            for (let i = 0; i < user.roles.length; i++) {
              if (user.roles[i] === environment.allowedKeyCloakRole) { return true; }
            } 
            return this.router.parseUrl("/notauthorized");
          }
          return false;
        }
        );
      }
    }
    
    canActivateChild(
      next: ActivatedRouteSnapshot,
      state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      return this.canActivate(next, state);
    }
}
