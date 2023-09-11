import { Injectable } from "@angular/core";
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, CanActivateChild, Router } from "@angular/router";
import { Observable, of } from "rxjs";
import { map } from "rxjs/operators";
import { environment } from "../environments/environment";
import { AuthService } from "./services/auth.service";

@Injectable({
  providedIn: "root"
})
export class AuthGuardAnalys implements CanActivate, CanActivateChild {
  constructor(private authService: AuthService, private router: Router) {

  }
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> {
    if (this.authService.isLoggedIn() && this.authService.isAnalysAllowed()) {
      return of(true);
    } else {
      return this.authService.login().pipe(map(_ => {
        if (this.authService.isAnalysAllowed()) {
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
