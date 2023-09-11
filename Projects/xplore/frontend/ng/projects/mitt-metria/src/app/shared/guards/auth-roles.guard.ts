import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from '../../../../../lib/oidc/login.service';
import { AuthService } from '../data-access/auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthRolesGuard implements CanActivate, CanActivateChild {

  constructor(private authService: AuthService,
    private router: Router) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      if (this.authService.isLoggedIn() && this.authService.hasAccess(route.data.roles)) {
        return true;
      } else {
        return this.router.parseUrl("/notauthorized");
      }
  }

  canActivateChild(
    childRoute: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.canActivate(childRoute, state);
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
}
