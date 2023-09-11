import { Injectable } from "@angular/core";
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  UrlTree,
  CanActivateChild,
} from "@angular/router";
import { from, Observable, of } from "rxjs";
import { RoleTypes } from "./models/roleTypes";
import { AuthConfigService } from "./services/auth-config.service";
import { AuthService } from "./services/auth.service";

@Injectable({
  providedIn: "root",
})
export class UserAuthGuard implements CanActivate, CanActivateChild {
  constructor(
    private authService: AuthService,
    private authConfigService: AuthConfigService
  ) {}
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
  | Observable<boolean | UrlTree>
  | Promise<boolean | UrlTree>
  | boolean
  | UrlTree {
    return this.authConfigService.publicAuthConfig().then(() => {
      if (this.authService.isLoggedIn()) {
        return true;
      } else {
        return this.authService.login(RoleTypes.USER);
      }
    });
  }

  canActivateChild(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    return this.canActivate(next, state);
  }
}
