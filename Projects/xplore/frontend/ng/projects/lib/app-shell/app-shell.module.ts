import {BrowserModule} from "@angular/platform-browser";
import {APP_INITIALIZER, NgModule} from "@angular/core";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatButtonModule} from "@angular/material/button";
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {MatDialogModule} from "@angular/material/dialog";
import {MatDividerModule} from "@angular/material/divider";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {MatMenuModule} from "@angular/material/menu";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatRadioModule} from "@angular/material/radio";
import {MatSelectModule} from "@angular/material/select";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MapModule} from "../map/map.module";
import {InfoPanelComponent} from "../map/components/info-panel/info-panel.component";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {NgxChartsModule} from "@swimlane/ngx-charts";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {registerLocaleData} from "@angular/common";
import localeSv from "@angular/common/locales/sv";
import {HelpDialogComponent} from "../map/components/dialogs/help-dialog/help-dialog.component";
import {ContactDialogComponent} from "../map/components/dialogs/contact-dialog/contact-dialog.component";
import {AppShellComponent} from "./app-shell.component";
import {CollapsableInfoPanelComponent} from "../map/components/info-panel/collapsable-info-panel.component";
import {ShareDialogComponent} from "../map/components/dialogs/share-dialog/share-dialog.component";
import {ConfirmationDialogComponent} from "../map/components/dialogs/confirmation-dialog/confirmation-dialog.component";
import {AddFeatureDialogComponent} from "../map/components/dialogs/add-feature-dialog/add-feature-dialog.component";
import {EnvironmentConfigService} from "../config/environment-config.service";
import {AuthenticationInterceptor} from "../http/authentication-interceptor";
import {InfoFastighetsgransDialogComponent} from "../map/components/dialogs/info-fastighetsgrans-dialog/info-fastighetsgrans-dialog.component";
import {AboutDialogComponent} from "../map/components/dialogs/about-dialog/about-dialog.component";
import {KartmaterialDialogComponent} from "../map/components/dialogs/kartmaterial-dialog/kartmaterial-dialog.component";

// Load the Sv locale data. Used for formatting.
registerLocaleData(localeSv);

@NgModule({
    declarations: [
        AppShellComponent,
        InfoPanelComponent,
        HelpDialogComponent,
        InfoFastighetsgransDialogComponent,
        AboutDialogComponent,
        KartmaterialDialogComponent,
        ContactDialogComponent,
        CollapsableInfoPanelComponent,
        ShareDialogComponent,
        ConfirmationDialogComponent,
        AddFeatureDialogComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        MapModule,
        MatButtonModule,
        MatDialogModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatMenuModule,
        MatRadioModule,
        MatProgressSpinnerModule,
        MatSelectModule,
        MatDividerModule,
        MatProgressBarModule,
        NgxChartsModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule,
        MatButtonToggleModule,
        MatTooltipModule
    ],
    exports: [
        AppShellComponent,
        BrowserModule,
        BrowserAnimationsModule,
        MapModule,
        MatButtonModule,
        MatDialogModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatMenuModule,
        MatRadioModule,
        MatProgressSpinnerModule,
        MatSelectModule,
        MatDividerModule,
        MatProgressBarModule,
        NgxChartsModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule,
        InfoPanelComponent,
        HelpDialogComponent,
        InfoFastighetsgransDialogComponent,
        ContactDialogComponent,
        CollapsableInfoPanelComponent,
        ShareDialogComponent,
        ConfirmationDialogComponent,
        AddFeatureDialogComponent,
        MatButtonToggleModule,
        MatTooltipModule
    ],
    providers: [
        { provide: HTTP_INTERCEPTORS, useClass: AuthenticationInterceptor, multi: true },
        EnvironmentConfigService
    ]
})
export class AppShellModule {
}
