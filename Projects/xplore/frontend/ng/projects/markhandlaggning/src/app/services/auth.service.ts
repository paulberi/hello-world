import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { AuthConfig } from "angular-oauth2-oidc";
import { LoginService, User } from "../../../../lib/oidc/login.service";


@Injectable({
  providedIn: "root"
})
export class AuthService {
  public user: User;
  constructor(private loginService: LoginService) { 
  }

  authConfig: AuthConfig = {
    issuer: environment.authIssuer,
    redirectUri: window.location.origin + "/",
    silentRefreshRedirectUri: window.location.origin + window.location.pathname + "silent-refresh.html",
    clientId: environment.keyCloakClientId,
    scope: environment.keyCloakScope,
    responseType: "code",    
    disableAtHashCheck: true,
    showDebugInformation: true,
    requireHttps: false,
  };

  logout() {
    this.loginService.logout();
  }
}
