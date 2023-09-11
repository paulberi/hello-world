import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { AuthConfig } from "angular-oauth2-oidc";
import { ConfigService } from "../../../../lib/config/config.service";
import { environment } from "../../environments/environment";
import { appConfig, urlConfig } from "../app.config";

@Injectable({
  providedIn: "root",
})
export class AuthConfigService {
  constructor(private httpClient: HttpClient) {}

  async adminAuthConfig(): Promise<any> {
    return this.httpClient
      .get(environment.authIssuer, { responseType: "text" })
      .toPromise()
      .then((configAuthIssuer) => {
        const authConfig: AuthConfig = {
          // Url of the Identity Provider
          issuer: configAuthIssuer,

          // URL of the SPA to redirect the user to after login
          redirectUri: window.location.origin + "/admin/redirect",

          // The SPA's id. The SPA is registerd with this id at the auth-server
          clientId: environment.keyCloakClientId,

          responseType: "code",

          // set the scope for the permissions the client should request
          scope: environment.keyCloakScope,

          // showDebugInformation: true,

          requireHttps: false,
        };
        ConfigService.setAppConfig(appConfig, authConfig, urlConfig);
      });
  }

  async publicAuthConfig(): Promise<any> {
    return this.httpClient
      .get(environment.authIssuerBankId, { responseType: "text" })
      .toPromise()
      .then((configAuthBankId) => {
        const authConfig: AuthConfig = {
          // Url of the Identity Provider
          issuer: configAuthBankId,

          // URL of the SPA to redirect the user to after login
          redirectUri: window.location.origin + "/redirect",

          // The SPA's id. The SPA is registerd with this id at the auth-server
          clientId: environment.keyCloakClientId,

          responseType: "code",

          // set the scope for the permissions the client should request
          scope: environment.keyCloakScope,

          // showDebugInformation: true,

          requireHttps: false,
        };
        ConfigService.setAppConfig(appConfig, authConfig, urlConfig);
      });
  }
}
