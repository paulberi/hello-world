import { ApiModule as XploreAdminApiModule, Configuration, ConfigurationParameters } from "../../../../generated/admin-api";
import { ApiModule as XploreKundConfigApiModule } from "../../../../generated/kundconfig-api";
import { APP_INITIALIZER, LOCALE_ID, NgModule } from "@angular/core";
import { AppComponent } from "./app.component";
import { AppRoutingModule } from "./app-routing.module";
import { AppShellComponent } from "./shell/app-shell.component";
import { AuthConfig, OAuthModule } from "angular-oauth2-oidc";
import { AuthenticationInterceptor } from "../../../lib/http/authentication-interceptor";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { BrowserModule } from "@angular/platform-browser";
import { ConfigService } from "../../../lib/config/config.service";
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatDateFormats } from "@angular/material/core";
import { environment } from "../environments/environment";
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from "@angular/common/http";
import { MomentDateAdapter } from "@angular/material-moment-adapter";
import { NotAuthorizedComponent } from "./not-authorized/not-authorized.component";
import { registerLocaleData } from "@angular/common";
import { XpLayoutModule } from "../../../lib/ui/layout/layout.module";
import localeSv from "@angular/common/locales/sv";
import mockServer from "./app.mock";
import { AdmTranslocoRootModule } from "./transloco/transloco-root.module";
import { XpErrorModule } from "../../../lib/error/error.module";
import { AdmKundvyModule } from "./kundvy/kundvy.module";
import { AdmManageUsersModule } from "./manage-users/manage-users.module";
import { AdmCreateKundModule } from "./create-kund/create-kund.module";
import { appConfig, urlConfig } from "./app.config";
import {XpTracingModule} from "../../../lib/tracing/tracing.module";

export const MY_FORMATS: MatDateFormats = {
  parse: {
    dateInput: "YYYY-MM-DD",
  },
  display: {
    dateInput: "YYYY-MM-DD",
    monthYearLabel: "MMM YYYY",
    dateA11yLabel: "LL",
    monthYearA11yLabel: "MMMM YYYY",
  },
};

export function initApp(configService: ConfigService, httpClient: HttpClient) {
  return () => {

    // Mock API when in development mode and environment variable apiMock is set to true
    if (process.env.NODE_ENV === "development" && environment.apiMock) {
      mockServer("development");
    }

    return httpClient.get("/configAuthIssuer", {responseType: "text"}).toPromise()
      .then(configAuthIssuer => {
        const authConfig: AuthConfig = {
          // Url of the Identity Provider
          issuer: configAuthIssuer,

          // URL of the SPA to redirect the user to after login
          redirectUri: window.location.origin + "/oauthLogin",

          // The SPA"s id. The SPA is registerd with this id at the auth-server
          clientId: "xplore-admin-frontend",

          responseType: "code",

          // set the scope for the permissions the client should request
          scope: "openid profile email offline_access",

          requireHttps: "remoteOnly"
        };

        ConfigService.setAppConfig(appConfig, authConfig, urlConfig);
      });
  };
}

export function XploreAdminApiModuleConfigFactory() {
  const params: ConfigurationParameters = {
    basePath: "/api"
  };

  return new Configuration(params);
}
export function KundconfigApiModuleConfigFactory() {
  const params: ConfigurationParameters = {
    basePath: "/kund-config/api"
  };

  return new Configuration(params);
}

@NgModule({
    declarations: [
        AppComponent,
        AppShellComponent,
        NotAuthorizedComponent,
    ],
    imports: [
        HttpClientModule,
        AppRoutingModule,
        BrowserModule,
        AdmManageUsersModule,
        BrowserAnimationsModule,
        XpLayoutModule,
        XpErrorModule,
        AdmKundvyModule,
        AdmCreateKundModule,
        AdmTranslocoRootModule.forRoot({
            availableLanguages: environment.availableLanguages,
            defaultLanguage: environment.defaultLanguage
        }),
        XploreAdminApiModule.forRoot(XploreAdminApiModuleConfigFactory),
        XploreKundConfigApiModule.forRoot(KundconfigApiModuleConfigFactory),
        XpTracingModule.forRoot({
          serviceName: "xplore-admin-frontend",
          ignoreTransactions: [/\/oauthLogin\?state.*/],
          pageLoadTransactionName: "page-load"
        }),
        OAuthModule.forRoot({
            resourceServer: {
                allowedUrls: [
                    "/api",
                    "/kund-config/api"
                ],
                sendAccessToken: true
            }
        })
    ],
    exports: [
        AppComponent
    ],
    providers: [
        { provide: HTTP_INTERCEPTORS, useClass: AuthenticationInterceptor, multi: true },
        { provide: LOCALE_ID, useValue: "sv-SE" },
        {
            provide: APP_INITIALIZER,
            useFactory: initApp,
            deps: [ConfigService, HttpClient],
            multi: true
        },
        { provide: MAT_DATE_LOCALE, useValue: "sv-SE" },
        { provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE] },
        { provide: MAT_DATE_FORMATS, useValue: MY_FORMATS },
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
  constructor() {
    registerLocaleData(localeSv, "sv");
  }
}
