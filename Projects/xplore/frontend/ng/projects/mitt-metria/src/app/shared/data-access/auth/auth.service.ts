import { Injectable } from "@angular/core";
import { OAuthService, OAuthStorage } from "angular-oauth2-oidc";
import { first, map } from 'rxjs/operators'
import { LoginService, User } from "../../../../../../lib/oidc/login.service";
import { XpUserService } from "../../../../../../lib/user/user.service";
import { roles } from "../../utils/roles";
import { ActiveAdministratorGQL } from "./auth.admin.generated";
import { ActiveCustomerGQL } from "./auth.shop.generated";
import { AuthenticateGQL as AuthenticateAdminGQL } from './auth.admin.generated';
import { AuthenticateGQL as AuthenticateUserGQL } from './auth.shop.generated';
import { LogoutGQL as LogoutAdminGQL } from './auth.admin.generated';
import { LogoutGQL as LogoutUserGQL } from './auth.shop.generated';
import { Apollo } from 'apollo-angular';
import { AuthenticationResult as AdminAuthenticateResult } from '../../../../../../../generated/graphql/admin/admin-api-types';
import { AuthenticationResult as UserAuthenticateResult } from '../../../../../../../generated/graphql/shop/shop-api-types'

@Injectable({
  providedIn: "root"
})
export class AuthService {

  constructor(private loginService: LoginService,
    private oAuthService: OAuthService,
    private xpUserService: XpUserService,
    private activeAdministratorGQL: ActiveAdministratorGQL,
    private activeCustomerGQL: ActiveCustomerGQL,
    private authenticateAdminGQL: AuthenticateAdminGQL,
    private authenticateUserGQL: AuthenticateUserGQL,
    private logoutAdminGQL: LogoutAdminGQL,
    private logoutUserGQL: LogoutUserGQL,
    private authStorage: OAuthStorage,
    private apollo: Apollo,
  ) { }

  /**
   * Logga ut användaren.
   */
  public logout() {
    this.xpUserService.setUser(null);
    this.loginService.logout();
    sessionStorage.clear();
  }

  /**
   * Kolla om användaren är inloggad.
   */
  public isLoggedIn(): boolean {
    return this.xpUserService.getUser() != null && this.oAuthService.hasValidAccessToken();
  }

  public hasAccess(allowedRole: string): boolean {
    let allowed = false;
    this.xpUserService.getUserRoles$().subscribe(roles => {
      if (roles.find(role => role === allowedRole)) {
        return allowed = true;
      }
    });
    return allowed;
  }

  isMetriaSaljare(user: User): boolean {
    return user?.roles?.includes(roles.METRIA_SALJARE);
  }

  isLoggedInVendure(user: User): Promise<boolean> {
    if (this.isMetriaSaljare(user)) {
      return this.isLoggedInVendureAdmin(user);
    } else {
      return this.isLoggedInVendureUser(user)
    }
  }

  isLoggedInVendureUser(user: User): Promise<boolean> {
    return this.activeCustomerGQL.fetch().pipe(
      map(res => {
        if (res.data?.activeCustomer?.__typename === "Customer") {
          this.setUser(user, ["peak"]);
          return true;
        } else {
          return false;
        }
      }),
      first()
    ).toPromise()
  }

  isLoggedInVendureAdmin(user: User): Promise<boolean> {
    return this.activeAdministratorGQL.fetch().pipe(
      map(res => {
        if(res.data?.activeAdministrator?.__typename === "Administrator") {
          this.setUser(user, ["peak"]);
          return true
        } else {
          return false;
        }
      }),
      first()
    ).toPromise()
  }

  loginVendure(user: User): Promise<boolean> {
    if (this.isMetriaSaljare(user)) {
      return this.loginVendureAdmin(user);
    } else {
      return this.loginVendureUser(user)
    }
  }

  loginVendureUser(user: User): Promise<boolean> {
    const token = this.authStorage.getItem("access_token");
    if (token) {
      return this.authenticateUserGQL.mutate({
        token: token,
      }).pipe(
        map(({ data, loading }) => {
          switch (data?.authenticate?.__typename as UserAuthenticateResult["__typename"]) {
            case "CurrentUser":
              this.apollo.client.resetStore();
              this.setUser(user, ["peak"]);
              return true;
            case "InvalidCredentialsError":
              console.error("Vendure: Invalid Credentials")
              return false
          }
        }),
        first()
      ).toPromise()
    } else {
      this.logout();
    }
  }

  loginVendureAdmin(user: User): Promise<boolean> {
    const token = this.authStorage.getItem("access_token");
    if (token) {
      return this.authenticateAdminGQL.mutate({
        token: token,
      }).pipe(
        map(({ data, loading }) => {
          switch (data?.authenticate?.__typename as AdminAuthenticateResult["__typename"]) {
            case "CurrentUser":
              this.apollo.use("adminApi").client.resetStore();
              this.setUser(user, ["peak"]);
              return true
            case "InvalidCredentialsError":
              console.error("Vendure: Invalid Credentials")
              return false
          }
        }),
        first()
      ).toPromise()
    } else {
      this.logout();
    }
  }

  // Där man kallar på den här funktionen hade man istället hämta ut vilken kanal användaren har tillgång till
  // antingen från CurrentUser eller separat anrop
  private setUser(user: User, channels: string[]) {
    user.vendureChannels = channels
    this.xpUserService.setUser(user);
  }

  logoutVendure(): void {
    if (this.isMetriaSaljare(this.xpUserService.getUser())) {
      this.logoutAdminGQL.mutate().subscribe();
      this.apollo.client.clearStore();
      this.apollo.use("adminApi").client.clearStore();
    } else {
      this.logoutUserGQL.mutate().subscribe();
      this.apollo.client.clearStore();
    }
  }
}
