import {Injectable} from "@angular/core";
import {AuthConfig, OAuthErrorEvent, OAuthService} from "angular-oauth2-oidc";
import {from} from "rxjs";

export interface User {
  loggedIn: boolean;
  claims: any;
  roles: string[];
  vendureChannels?: string[];
}

/**
 * En service som hanterar login mot keycloak samt styr när id och access-token ska refreshas.
 *
 */
@Injectable({
  providedIn: "root"
})
export class LoginService {
  specialAuth: string;

  constructor(private oauthService: OAuthService) {
    this.specialAuth = sessionStorage.getItem("SpecialAuthHeader");
  }

  public loginCodeFlow(configuration: AuthConfig): Promise<User> {
    this.oauthService.configure(configuration);

    return this.oauthService.loadDiscoveryDocumentAndLogin().then(loggedIn => {
      if (loggedIn) {
        this.oauthService.setupAutomaticSilentRefresh();
        return {loggedIn: true, claims: this.oauthService.getIdentityClaims(), roles: this.getRoles()};
      } else {
        return {loggedIn: false, claims: null, roles: null};
      }
    })
      .catch(error => {
          if (error instanceof OAuthErrorEvent) {
            if (error.type === "invalid_nonce_in_state") {
              // Här hamnar man ibland för nya användare som gör lösenordsåterställning
              // när dom ska logga in första gången, och följer länken i lösenordsåterställningsmailet
              this.oauthService.initLoginFlow();
            }
          }

          console.error("Authentication error", error);

          return {loggedIn: false, claims: null, roles: null};
        }
      );
  }

  public refreshToken() {
    return from(this.oauthService.refreshToken());
  }

  public shouldRefresh() {
    return !this.oauthService.hasValidAccessToken() && this.oauthService.getRefreshToken() != null;
  }

  public logout() {
    this.oauthService.logOut();
  }

  public getRoles(): string[] {
    if (this.specialAuth) {
      const payload = this.specialAuth.split(".")[1];
      return JSON.parse(atob(payload)).realm_access.roles;
    }
    if (this.oauthService.hasValidAccessToken()) {
      const payload = this.oauthService.getAccessToken().split(".")[1];
      return JSON.parse(atob(payload)).realm_access.roles;
    }

    return null;
  }
}
