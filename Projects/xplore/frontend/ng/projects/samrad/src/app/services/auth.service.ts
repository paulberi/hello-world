import { Injectable } from "@angular/core";
import { OAuthService, AuthConfig } from "angular-oauth2-oidc";
import { LoginService, User } from "../../../../lib/oidc/login.service";
import { Router, UrlTree } from "@angular/router";
import { XpUserService } from "../../../../lib/user/user.service";
import { RoleTypes } from "../models/roleTypes";
import { ConfigService } from "../../../../lib/config/config.service";
import { indexOf } from "@amcharts/amcharts4/.internal/core/utils/Array";

@Injectable({
  providedIn: "root",
})
export class AuthService {
  user: User;
  authConfig: AuthConfig;
  roles: string[] = [];

  constructor(
    private loginService: LoginService,
    private oAuthService: OAuthService,
    private router: Router,
    private xpUserService: XpUserService
  ) {}

  /**
   * Starta inloggningsflöde. Om användaren loggas in korrekt och har en godkänd roll så spara ner den.
   * @returns {Observable<boolean>} True om användaren loggades in och har en godkänd roll, annars False.
   */
  public login(role: string): Promise<boolean | UrlTree> {
    role == RoleTypes.ADMIN ? this.roles.push(RoleTypes.ADMIN) : this.roles.push(RoleTypes.USER);
    return this.loginService
      .loginCodeFlow(ConfigService.authConfig)
      .then((user) => {
        if (user.roles) {
          if (this.isValidRoles(user.roles)) {
            this.user = user;
            this.xpUserService.setUser(user);
            return true;
          } else {
            return this.router.parseUrl("/notauthorized");
          }
        }
        return false;
      });
  }
  private isValidRoles(roles: string[]): boolean {
    if (roles) {
      for (let i = 0; i < roles.length; i++) {
        if (this.roles.includes(roles[i])) {
          return true;
        }
      }
    } else {
      return false;
    }
  }

  public logout() {
    this.xpUserService.setUser(null);
    this.oAuthService.logoutUrl = location.origin;
    this.oAuthService.logOut(false);
  }

  public isLoggedIn(): boolean {
    return (
      this.xpUserService.getUser() != null &&
      this.oAuthService.hasValidAccessToken()
    );
  }
}
