import {NgModule, ErrorHandler} from "@angular/core";
import {AppComponent} from "./app.component";
import {OAuthModule} from "angular-oauth2-oidc";
import {AppShellModule} from "../../../lib/app-shell/app-shell.module";
import { MatButtonModule } from "@angular/material/button";
import { MatInputModule } from "@angular/material/input";
import { MatMenuModule } from "@angular/material/menu";
import {AboutDialogComponent} from "./about-dialog/about-dialog.component";
import {KartmaterialDialogComponent} from "./kartmaterial-dialog/kartmaterial-dialog.component";
import {ApmErrorHandler} from "@elastic/apm-rum-angular";
import {XpTracingNoRouterModule} from "../../../lib/tracing/tracing-no-router.module";

@NgModule({
    declarations: [
        AppComponent,
        AboutDialogComponent,
        KartmaterialDialogComponent
    ],
    imports: [
        AppShellModule,
        MatButtonModule,
        MatInputModule,
        MatMenuModule,
        XpTracingNoRouterModule.forRoot({ serviceName: "bbk-frontend" }),
        OAuthModule.forRoot()
    ],
    exports: [
        AppComponent
    ],
    providers: [
        { provide: ErrorHandler, useClass: ApmErrorHandler }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
