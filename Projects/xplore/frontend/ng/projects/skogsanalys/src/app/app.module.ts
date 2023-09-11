import {APP_INITIALIZER, ErrorHandler, Inject, NgModule, NgZone} from "@angular/core";
import {AppComponent} from "./app.component";
import {AnalysisPanelComponent} from "./components/analysis-panel/analysis-panel.component";
import {NmdPanelComponent} from "./components/analysis-panel/panels/nmd-panel.component";
import {HuggningsklasserPanelComponent} from "./components/analysis-panel/panels/huggningsklasser/huggningsklasser-panel.component";
import {SkogligaGrunddataPanelComponent} from "./components/analysis-panel/panels/skogliga-grunddata-panel.component";
import {AvverkningsstatistikPanelComponent} from "./components/analysis-panel/panels/avverkningsstatistik-panel.component";
import {AuthConfig, OAuthModule} from "angular-oauth2-oidc";
import {environment} from "../environments/environment";
import {CommonModule, registerLocaleData} from "@angular/common";
import localeSv from "@angular/common/locales/sv";
import {AppShellModule} from "../../../lib/app-shell/app-shell.module";
import {ConfigService} from "../../../lib/config/config.service";
import {HTTP_INTERCEPTORS, HttpClient} from "@angular/common/http";
import {appConfig, urlConfig} from "./app.config";
import {ApmErrorHandler} from "@elastic/apm-rum-angular";
import {XpTracingNoRouterModule} from "../../../lib/tracing/tracing-no-router.module";
import { AppRoutingModule } from "./app-routing.module";
import { MapPageComponent } from "./components/map/map-page.component";
import { ReportComponent } from "./components/report/report.component";
import {AuthenticationInterceptor} from "../../../lib/http/authentication-interceptor";
import {AppShellComponent} from "./components/app-shell/app-shell.component";
import {TranslocoRootModule} from "./transloco-root.module";
import {NmdReportComponent} from "./components/analysis-report/panels/nmd-report.component";
import {HuggningsklasserReportComponent} from "./components/analysis-report/panels/huggningsklasser-report.component";
import {AvverkningsstatistikReportComponent} from "./components/analysis-report/panels/avverkningsstatistik-report.component";
import {SkogligaGrunddataReportComponent} from "./components/analysis-report/panels/skogliga-grunddata-report.component";
import {AnalysisReportComponent} from "./components/analysis-report/analysis-report.component";
import {SetSpecialAuthComponent} from "./set-special-auth/set-special-auth-component";
import {Router} from "@angular/router";
import {SkyddadeOmradenPanelComponent} from "./components/analysis-panel/panels/skyddade-omraden/skyddade-omraden-panel.component";
import {ContextedDialogModule} from "../../../lib/dialogs/contexted-dialog/contexted-dialog.module";
import {MatTableModule} from "@angular/material/table";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {
  SkyddadeOmradenReportComponent
} from "./components/analysis-report/panels/skyddade-omraden/skyddade-omraden-report.component";
import {DirectivesModule} from "../../../lib/directives/directives.module";
import {OverlayModule} from "@angular/cdk/overlay";

// Load the Sv locale data. Used for formatting.
registerLocaleData(localeSv);

export function initApp(configService: ConfigService, httpClient: HttpClient, router: Router) {
  return () => {
    return httpClient.get("/configAuthIssuer", {responseType: "text"}).toPromise()
      .then(configAuthIssuer => {
        const authConfig: AuthConfig = {
          // Url of the Identity Provider
          issuer: configAuthIssuer,

          // URL of the SPA to redirect the user to after login
          redirectUri: window.location.origin + "/oauthLogin",

          // The SPA's id. The SPA is registerd with this id at the auth-server
          clientId: "xplore-map",

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
    AppComponent,
    AnalysisPanelComponent,
    NmdPanelComponent,
    HuggningsklasserPanelComponent,
    SkogligaGrunddataPanelComponent,
    AvverkningsstatistikPanelComponent,
    SkyddadeOmradenPanelComponent,
    MapPageComponent,
    ReportComponent,
    AnalysisReportComponent,
    NmdReportComponent,
    HuggningsklasserReportComponent,
    SkogligaGrunddataReportComponent,
    AvverkningsstatistikReportComponent,
    SkyddadeOmradenReportComponent,
    AppShellComponent,
    SetSpecialAuthComponent
  ],
    imports: [
        AppShellModule,
        XpTracingNoRouterModule.forRoot({serviceName: "skogsanalys-frontend"}),
        OAuthModule.forRoot({
            resourceServer: {
                allowedUrls: [
                    environment.configurationUrl,
                    environment.skogligaGrunddataUrl,
                    environment.nmdUrl,
                    environment.huggningsklasserUrl,
                    environment.avverkningsstatistikUrl,
                    environment.metriaMapsPrefix,
                    environment.sokServiceUrl,
                    environment.skyddadeOmradenUrl,
                    "/skogsanalys"
                ],
                sendAccessToken: true
            }
        }),
        TranslocoRootModule.forRoot({
            availableLanguages: environment.availableLanguages,
            defaultLanguage: environment.defaultLanguage
        }),
        AppRoutingModule,
        CommonModule,
        ContextedDialogModule,
        MatTableModule,
        BrowserAnimationsModule,
        DirectivesModule,
        OverlayModule
    ],
  exports: [
    AppComponent
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AuthenticationInterceptor, multi: true},
    {
      provide: APP_INITIALIZER,
      useFactory: initApp,
      deps: [ConfigService, HttpClient, Router],
      multi: true
    },
    { provide: ErrorHandler, useClass: ApmErrorHandler}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
