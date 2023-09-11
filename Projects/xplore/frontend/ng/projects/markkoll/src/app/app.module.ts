import { ApiModule as MarkkollApiModule, Configuration, ConfigurationParameters } from "../../../../generated/markkoll-api";
import { ApiModule as SamradApiModule } from "../../../../generated/samrad-api/admin-api/api.module";
import { APP_INITIALIZER, LOCALE_ID, NgModule } from "@angular/core";
import { AppComponent } from "./app.component";
import { MkAppRoutingModule } from "./app-routing.module";
import { AppShellComponent } from "./app-shell/app-shell.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { BrowserModule } from "@angular/platform-browser";
import { HttpClient, HttpClientModule } from "@angular/common/http";
import { MkMapModule } from "./common/map/map.module";
import { NgxChartsModule } from "@swimlane/ngx-charts";
import { AuthConfig, OAuthModule } from "angular-oauth2-oidc";
import { MkProfileModule } from "./app-shell/profile/profile.module";
import { TranslocoRootModule } from "./transloco-root.module";
import { XpErrorModule } from "../../../lib/error/error.module";
import { XpLayoutModule } from "../../../lib/ui/layout/layout.module";
import { XpPaginatedTableModule } from "../../../lib/ui/paginated-table/paginated-table.module";
import { MkSetSpecialAuthComponent } from "./app-shell/set-special-auth/set-special-auth.component";
import { MkCommonModule } from "./common/common.module";
import { MatTabsModule } from "@angular/material/tabs";
import { environment } from "../environments/environment";

import { registerLocaleData } from "@angular/common";
import localeSv from "@angular/common/locales/sv";
import { ConfigService } from "../../../lib/config/config.service";
import { appConfig, urlConfig } from "./app.config";
import { XpTracingModule } from "../../../lib/tracing/tracing.module";
import { TranslocoMessageFormatModule } from "@ngneat/transloco-messageformat";
import { MkMarkhandlaggningModule } from "./markhandlaggning/markhandlanggning.module";
import vpConfig from "./vp_config.json";
import { VARDERING_CONFIG } from "./services/vardering-elnat.service";
import { MkAdminModule } from "./admin/admin.module";
import { BuildDetailsHttpService } from "./services/build-details-http.service";
import { MkUpToDateModule } from "./common/up-to-date/up-to-date.module";
import { MkProjektPageModule } from "./markhandlaggning/projekt/projekt-page/projekt-page.module";
import { SamradAdminModule } from "./samrad/samrad-admin/samrad-admin.module";

export function fetchBuildDetails(buildDetailsService: BuildDetailsHttpService) {
  return () => buildDetailsService.fetchBuildDetails();
}
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

          // showDebugInformation: true,

          requireHttps: false,
        };

        ConfigService.setAppConfig(appConfig, authConfig, urlConfig);
      });
  };
}

export function MarkkollApiModuleConfigFactory() {
  const params: ConfigurationParameters = {
    basePath: environment.backendUrl
  };

  return new Configuration(params);
}

export function SamradApiModuleConfigFactory() {
  const params: ConfigurationParameters = {
    basePath: environment.samradBackendUrl
  };

  return new Configuration(params);
}

@NgModule({
  declarations: [AppComponent, AppShellComponent, MkSetSpecialAuthComponent],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatTabsModule,
    NgxChartsModule,
    XpErrorModule,
    XpLayoutModule,
    XpPaginatedTableModule,
    MkAppRoutingModule,
    MkCommonModule,
    MkMapModule,
    MkProfileModule,
    MkMarkhandlaggningModule,
    MkAdminModule,
    MkProjektPageModule,
    SamradAdminModule,
    MkUpToDateModule,
    TranslocoRootModule.forRoot({
      availableLanguages: environment.availableLanguages,
      defaultLanguage: environment.defaultLanguage
    }),
    TranslocoMessageFormatModule.forRoot(),
    XpTracingModule.forRoot({
      serviceName: "markkoll-frontend",
      ignoreTransactions: [/\/\?state.*/],
      pageLoadTransactionName: "page-load"
    }),
    OAuthModule.forRoot({
      resourceServer: {
        allowedUrls: [
          environment.backendUrl,
          environment.geoserverUrl,
          environment.metriaMapsUrl,
          environment.markkollWfsUrl,
          environment.samradBackendUrl,
        ],
        sendAccessToken: true,
      },
    }),
    SamradApiModule.forRoot(SamradApiModuleConfigFactory),
    MarkkollApiModule.forRoot(MarkkollApiModuleConfigFactory),
  ],
  exports: [AppComponent],
  providers: [
    { provide: LOCALE_ID, useValue: "sv-SE" },
    {
      provide: APP_INITIALIZER,
      useFactory: initApp,
      deps: [ConfigService, HttpClient],
      multi: true,
    },
    { provide: VARDERING_CONFIG, useValue: vpConfig },
    { provide: APP_INITIALIZER, useFactory: fetchBuildDetails, deps: [BuildDetailsHttpService], multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {
  constructor() {
    registerLocaleData(localeSv, "sv");
  }
}
