import {APP_INITIALIZER, NgModule, ErrorHandler} from "@angular/core";
import {AppComponent} from "./app.component";
import {AuthConfig, OAuthModule} from "angular-oauth2-oidc";
import {AppShellModule} from "../../../lib/app-shell/app-shell.module";
import { MatButtonModule } from "@angular/material/button";
import { MatInputModule } from "@angular/material/input";
import { MatMenuModule } from "@angular/material/menu";
import {environment} from "../environments/environment";
import {EdpInfoPanelComponent} from "./edp-integration/edp-info-panel.component";
import {ConfigService} from "../../../lib/config/config.service";
import {HttpClient} from "@angular/common/http";
import {appConfig, urlConfig} from "./app.config";
import {EdpMarkCoordinatesComponent} from "./edp-integration/edp-mark-coordinates.component";
import {MatListModule} from "@angular/material/list";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {ApmErrorHandler} from "@elastic/apm-rum-angular";
import {XpTracingNoRouterModule} from "../../../lib/tracing/tracing-no-router.module";
import {CastorInfoPanelComponent} from "../../../lib/map/components/castor-integration/castor-info-panel.component";
import {ApiModule as CastorApiModule} from '../../../../generated/castor-api/';
import { Configuration, ConfigurationParameters } from "../../../../generated/castor-api";

export function initApp(configService: ConfigService, httpClient: HttpClient) {
  return () => {
    return httpClient.get("/configAuthIssuer", {responseType: "text"}).toPromise()
      .then(configAuthIssuer => {
        console.error("Load done!", configAuthIssuer);

        const authConfig: AuthConfig = {
          // Url of the Identity Provider
          issuer: configAuthIssuer,

          // URL of the SPA to redirect the user to after login
          redirectUri: window.location.origin + window.location.pathname,

          // The SPA's id. The SPA is registerd with this id at the auth-server
          clientId: "geovis-pro",

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
export function CastorApiModuleConfigFactory() {
  const params: ConfigurationParameters = {
    basePath: "/castorapi"
  };

  return new Configuration(params);
}
@NgModule({
    declarations: [
        AppComponent,
        EdpInfoPanelComponent,
        EdpMarkCoordinatesComponent,
        CastorInfoPanelComponent
    ],
    imports: [
        AppShellModule,
        MatButtonModule,
        MatInputModule,
        MatMenuModule,
        XpTracingNoRouterModule.forRoot({ serviceName: "geovis-pro-frontend" }),
        OAuthModule.forRoot({
            resourceServer: {
                allowedUrls: [
                    environment.authUrl,
                    environment.configurationUrl,
                    environment.sokServiceUrl,
                    environment.metriaMapsPrefix,
                    environment.ownerListUrl,
                    "/geoserver",
                    "/maps",
                    "/mapproxy"
                ],
                sendAccessToken: true
            }
        }),
        MatListModule,
        MatSlideToggleModule,
        CastorApiModule.forRoot(CastorApiModuleConfigFactory),
    ],
    exports: [
        AppComponent
    ],
    providers: [
        {
            provide: APP_INITIALIZER,
            useFactory: initApp,
            deps: [ConfigService, HttpClient],
            multi: true
        },
        { provide: ErrorHandler, useClass: ApmErrorHandler }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
