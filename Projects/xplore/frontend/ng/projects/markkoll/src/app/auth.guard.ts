import { Injectable } from "@angular/core";
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, CanActivateChild, Router } from "@angular/router";
import { Observable, of } from "rxjs";
import { AuthService } from "./services/auth.service";

@Injectable({
  providedIn: "root"
})
export class AuthGuard implements CanActivate, CanActivateChild {
  constructor(private authService: AuthService) {

  }
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> {
      if (this.authService.isLoggedIn()) {
        return of(this.authService.trueOrRedirectUrl(state));
      } else {
        return this.authService.login();
      }
    }

  canActivateChild(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.canActivate(next, state);
  }  
}
