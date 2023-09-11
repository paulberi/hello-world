import { APP_INITIALIZER, NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { AppShellComponent } from "./app-shell/app-shell.component";
import { XpLayoutModule } from "../../../lib/ui/layout/layout.module";
import { TranslocoRootModule } from "./transloco-root.module";
import { environment } from "../environments/environment";
import { XpTracingModule } from "../../../lib/tracing/tracing.module";
import { OAuthModule } from "angular-oauth2-oidc";
import { HttpClientModule } from "@angular/common/http";
import { ApiModule, Configuration } from "../../../../generated/samrad-api";
import { ApiModule as AdminApiModule } from "../../../../generated/samrad-api/admin-api/api.module";
import { Configuration as AdminConfiguration } from "../../../../generated/samrad-api/admin-api/configuration";
import { AdminModule } from "./admin/admin.module";
import { PublicModule } from "./public/public.module";
import { InitieraKundService } from "./services/initiera-kund.service";
import { SrMapModule } from "./utils/map/map.module";
import { XpNotFoundModule } from "../../../lib/ui/feedback/not-found/not-found.module";

@NgModule({
  declarations: [AppComponent, AppShellComponent],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    AdminModule,
    PublicModule,
    SrMapModule,
    XpLayoutModule,
    XpNotFoundModule,
    TranslocoRootModule.forRoot({
      availableLanguages: environment.availableLanguages,
      defaultLanguage: environment.defaultLanguage,
    }),
    XpTracingModule.forRoot({ serviceName: "samrad-frontend" }),
    OAuthModule.forRoot({
      resourceServer: {
        allowedUrls: [environment.backendUrlAdmin],
        sendAccessToken: true,
      },
    }),
    ApiModule.forRoot(
      () =>
        new Configuration({
          basePath: environment.backendUrl,
        })
    ),
    AdminApiModule.forRoot(
      () =>
        new AdminConfiguration({
          basePath: environment.backendUrlAdmin,
        })
    ),
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: (kund: InitieraKundService) => {
        return () => kund.initKund();
      },
      deps: [InitieraKundService],
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
