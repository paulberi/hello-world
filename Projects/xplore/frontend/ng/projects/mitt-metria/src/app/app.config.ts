import { HttpClient } from "@angular/common/http";
import { AuthConfig } from "angular-oauth2-oidc";
import { AppConfig, ConfigService, UrlConfig } from "../../../lib/config/config.service";
import { environment } from "../environments/environment";

export const appConfig: AppConfig = {
  appName: "Mitt Metria",
  configuration: "mitt-metria",
};

export const urlConfig: UrlConfig = {
  configurationUrl: null,
  sokServiceUrl: environment.sokServiceUrl
};

export function initApp(configService: ConfigService, httpClient: HttpClient) {
  return () => {
    return httpClient
      .get(environment.authIssuer, { responseType: "text" })
      .toPromise()
      .then((configAuthIssuer) => {
        const authConfig: AuthConfig = {

          // Url of the Identity Provider
          issuer: configAuthIssuer,

          // URL of the SPA to redirect the user to after login
          redirectUri: window.location.origin + "/oauthLogin",

          // The SPA's id. The SPA is registerd with this id at the auth-server
          clientId: environment.keyCloakClientId,

          responseType: "code",

          // set the scope for the permissions the client should request
          scope: environment.keyCloakScope,

          //showDebugInformation: true,

          requireHttps: "remoteOnly",
        };
        ConfigService.setAppConfig(appConfig, authConfig, urlConfig);
      });
  };
};

export const languageFilePaths: string[] = [
  "/assets/locales/xp/",
  "/assets/locales/",
];
