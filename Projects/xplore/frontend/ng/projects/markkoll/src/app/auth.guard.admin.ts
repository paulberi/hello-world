import { Injectable } from "@angular/core";
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, CanActivateChild, Router } from "@angular/router";
import { Observable, of } from "rxjs";
import { map } from "rxjs/operators";
import { AuthService } from "./services/auth.service";

@Injectable({
  providedIn: "root"
})
export class AuthGuardAdmin implements CanActivate, CanActivateChild {
  
  constructor(private authService: AuthService, private router: Router) {}
  
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> {
      if (this.authService.isLoggedIn() && this.authService.isAdminAllowed()) {
        return of(true);
      } else {
        return this.authService.login().pipe(map(_ => {
          if (this.authService.isAdminAllowed()) {
            return true;
          }
          return this.router.parseUrl("/");
        }));
      }
    }

  canActivateChild(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.canActivate(next, state);
  }  
}
