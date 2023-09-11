import { APP_INITIALIZER, LOCALE_ID, NgModule } from "@angular/core";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { AppShellComponent } from "./app-shell/app-shell.component";
import { XpLayoutModule } from "../../../lib/ui/layout/layout.module";
import { OAuthModule } from "angular-oauth2-oidc";
import { TranslocoRootModule } from "./transloco-root.module";
import { ConfigService } from "../../../lib/config/config.service";
import { HttpClient, HttpClientModule } from "@angular/common/http";
import { initApp } from "./app.config";
import { GraphQLModule } from "./graphql.module";
import { XpTracingModule } from "../../../lib/tracing/tracing.module";
import { environment } from "../environments/environment";
import { registerLocaleData } from '@angular/common';
import { XpLayoutV2Module } from "../../../lib/ui/layout-v2/layout-v2.module";
import { AuthGuard } from "./shared/guards/auth.guard";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import localeSv from '@angular/common/locales/sv';
import { MatSnackBarModule } from "@angular/material/snack-bar";
registerLocaleData(localeSv);

@NgModule({
  declarations: [
    AppComponent,
    AppShellComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    XpLayoutModule,
    XpLayoutV2Module,
    GraphQLModule,
    MatSnackBarModule, //Http client service behöver snackbar (används av sokservice)
    XpTracingModule.forRoot({ serviceName: "mitt-metria-frontend" }),
    OAuthModule.forRoot({
      resourceServer: {
        allowedUrls: [
          environment.sokServiceUrl
        ],
        sendAccessToken: true
      }
    }),
    TranslocoRootModule.forRoot({
      availableLanguages: environment.availableLanguages,
      defaultLanguage: environment.defaultLanguage
    })
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initApp,
      deps: [ConfigService, HttpClient],
      multi: true
    },
    { provide: LOCALE_ID, useValue: "sv-SE" },
    AuthGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
