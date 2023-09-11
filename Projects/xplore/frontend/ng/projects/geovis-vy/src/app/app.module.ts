import {APP_INITIALIZER, NgModule, ErrorHandler} from "@angular/core";
import {AppComponent} from "./app.component";
import {AuthConfig, OAuthModule} from "angular-oauth2-oidc";
import {environment} from "../environments/environment";
import {AppShellModule} from "../../../lib/app-shell/app-shell.module";
import {ConfigService} from "../../../lib/config/config.service";
import {HttpClient} from "@angular/common/http";
import {appConfig, urlConfig} from "./app.config";
import {ApmErrorHandler} from "@elastic/apm-rum-angular";
import {XpTracingNoRouterModule} from "../../../lib/tracing/tracing-no-router.module";

export function initApp(configService: ConfigService, httpClient: HttpClient) {
  return () => {
    return httpClient.get("/configAuthIssuer", {responseType: "text"}).toPromise()
      .then(configAuthIssuer => {
        const authConfig: AuthConfig = {
          // Url of the Identity Provider
          issuer: configAuthIssuer,

          // URL of the SPA to redirect the user to after login
          redirectUri: window.location.origin + window.location.pathname,

          // The SPA's id. The SPA is registerd with this id at the auth-server
          clientId: "geovis-vy",

          responseType: "code",

          // set the scope for the permissions the client should request
          scope: "openid profile email offline_access",

          // showDebugInformation: true,

          requireHttps: false
        };

        ConfigService.setAppConfig(appConfig, authConfig, urlConfig);
      });
  };
}


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    AppShellModule,
    XpTracingNoRouterModule.forRoot({serviceName: "geovis-vy-frontend"}),
    OAuthModule.forRoot({
      resourceServer: {
        allowedUrls: [
          environment.authUrl,
          environment.configurationUrl,
          environment.metriaMapsPrefix,
          environment.sokServiceUrl,
          environment.ownerListUrl,
          "/geoserver",
          "/maps",
          "/mapproxy"
        ],
        sendAccessToken: true
      }
    })
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initApp,
      deps: [ConfigService, HttpClient],
      multi: true
    },
    { provide: ErrorHandler, useClass: ApmErrorHandler}
  ],
  exports: [
    AppComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
