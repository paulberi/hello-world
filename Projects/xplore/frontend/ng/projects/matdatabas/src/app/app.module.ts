import {BrowserModule} from "@angular/platform-browser";
import {APP_INITIALIZER, ErrorHandler, LOCALE_ID, NgModule} from "@angular/core";
import {DragDropModule} from "@angular/cdk/drag-drop";
import {AppRoutingModule} from "./app-routing.module";
import {AppComponent} from "./app.component";
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from "@angular/common/http";
import {MatobjektComponent} from "./matobjekt/matobjekt.component";
import {GranskningComponent} from "./granskning/granskning.component";
import {RapporterComponent} from "./rapporter/rapporter.component";
import {StartComponent} from "./start/start.component";
import {AppShellComponent} from "./app-shell.component";
import {NotAuthorizedComponent} from "./not-authorized/not-authorized.component";
import {MeddelandePageComponent} from "./start/meddelanden/meddelande-page.component";
import {MatIconModule} from "@angular/material/icon";
import {EditMeddelandeComponent} from "./installningar/meddelanden/edit-meddelande.component";
import {MatInputModule} from "@angular/material/input";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatButtonModule} from "@angular/material/button";
import {EditMeddelandePageComponent} from "./installningar/meddelanden/edit-meddelande-page.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {PageNavigationComponent} from "./common/page-navigation/page-navigation.component";
import {MatSortModule} from "@angular/material/sort";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule, MatPaginatorIntl} from "@angular/material/paginator";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatListModule} from "@angular/material/list";
import {MatToolbarModule} from "@angular/material/toolbar";
import {AnvandareComponent} from "./installningar/anvandare/anvandare.component";
import {EditAnvandareComponent} from "./installningar/anvandare/edit-anvandare.component";
import {AnvandargruppPageComponent} from "./installningar/anvandargrupp/anvandargrupp-page.component";
import {EditAnvandargruppComponent} from "./installningar/anvandargrupp/edit-anvandargrupp/edit-anvandargrupp.component";
import {AnvandargruppMatansvarComponent} from "./installningar/anvandargrupp/edit-anvandargrupp/anvandargrupp-matansvar.component";
import {AnvandargruppLarmComponent} from "./installningar/anvandargrupp/edit-anvandargrupp/anvandargrupp-larm.component";
import { MatAutocompleteModule } from "@angular/material/autocomplete";
import { MatDialogModule } from "@angular/material/dialog";
import { MatDividerModule } from "@angular/material/divider";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatSelectModule } from "@angular/material/select";
import { MatTooltipModule } from "@angular/material/tooltip";
import {AnvandargruppAnvandareComponent} from "./installningar/anvandargrupp/edit-anvandargrupp/anvandargrupp-anvandare.component";
import {SystemloggPageComponent} from "./installningar/systemlogg/systemlogg-page.component";
import {ConfirmationDialogComponent} from "../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import {InformationDialogComponent} from "../../../lib/dialogs/information-dialog/information-dialog.component";
import {MatPaginatorIntlSv} from "./mat-paginator-intl-sv";
import {MatRippleModule} from "@angular/material/core";
import {MatobjektgruppComponent} from "./matobjektgrupp/matobjektgrupp.component";
import {MatTabsModule} from "@angular/material/tabs";
import {MatningstyperComponent} from "./matobjekt/matningstyper/matningstyper.component";
import {MatobjektContainerComponent} from "./matobjekt/matobjekt-container.component";
import {SearchMatobjektComponent} from "./matobjekt/search-matobjekt/search-matobjekt.component";
import {SelectMatningstypComponent} from "./matobjekt/matningstyper/select-matningstyp.component";
import {HandelserComponent} from "./matobjekt/handelser/handelser.component";
import {GrunduppgifterComponent} from "./matobjekt/grunduppgifter/grunduppgifter.component";
import {GrunduppgifterGrupperComponent} from "./matobjekt/grunduppgifter/grunduppgifter-grupper.component";
import {GrunduppgifterDokumentComponent} from "./matobjekt/grunduppgifter/grunduppgifter-dokument.component";
import {GrunduppgifterBildComponent} from "./matobjekt/grunduppgifter/grunduppgifter-bild.component";
import {EditMatningstypComponent} from "./matobjekt/matningstyper/edit-matningstyp.component";
import {EditMatobjektgruppComponent} from "./matobjektgrupp/edit-matobjektgrupp.component";
import {EditMatobjektgruppMatobjektComponent} from "./matobjektgrupp/edit-matobjektgrupp-matobjekt.component";
import {SaveButtonComponent} from "./common/components/save-button.component";
import {DecimalNumberValueAccessor} from "./common/decimal-number-value-accessor";
import {EditHandelseComponent} from "./matobjekt/handelser/edit-handelse.component";
import {DokumentComponent} from "./common/bifogadfil/dokument.component";
import {DefinitionMatningstyperComponent} from "./definition-matningstyper/definition-matningstyper.component";
import {EditDefinitionMatningstypComponent} from "./definition-matningstyper/edit-definition-matningstyp.component";
import {MatrundorComponent} from "./matrunda/matrundor.component";
import {OverlayContainer} from "@angular/cdk/overlay";
import {GransvardenComponent} from "./gransvarden/gransvarden.component";
import {EditModeDirective} from "./common/edit-mode.directive";
import {ViewModeDirective} from "./common/view-mode.directive";
import {EditComponent} from "./common/components/edit.component";
import {EditMatrundaComponent} from "./matrunda/edit-matrunda/edit-matrunda.component";
import {EditMatrundaEditMatningstyperComponent} from "./matrunda/edit-matrunda/edit-matrunda-edit-matningstyper.component";
import {EditMatrundaViewMatningstyperComponent} from "./matrunda/edit-matrunda/edit-matrunda-view-matningstyper.component";
import {EditMatrundaSearchMatningstyperComponent} from "./matrunda/edit-matrunda/edit-matrunda-search-matningstyper.component";
import {LarmnivaerComponent} from "./installningar/larmnivaer/larmnivaer.component";
import {RapporteraVattenkemiComponent} from "./matningar/vattenkemi/rapportera-vattenkemi.component";
import {SearchMatobjektNamnComponent} from "./common/components/search-matobjekt-namn.component";
import {EditVattenkemiComponent} from "./matobjekt/vattenkemi/edit-vattenkemi.component";
import {VattenkemiRapporterComponent} from "./matobjekt/vattenkemi/vattenkemi-rapporter.component";
import {VattenkemiMatningarComponent} from "./matobjekt/vattenkemi/vattenkemi-matningar.component";
import {RapporteringMatdataComponent} from "./matningar/rapportering/rapportering-matdata.component";
import {RapporteringMatningstyperComponent} from "./matningar/rapportering/rapportering-matningstyper.component";
import {RapporteringMatningstypComponent} from "./matningar/rapportering/rapportering-matningstyp.component";
import {AuthenticationInterceptor} from "../../../lib/http/authentication-interceptor";
import {SearchMatvardeComponent} from "./common/components/search-matvarde.component";
import {SearchMatningComponent} from "./matningar/search-matning.component";
import {MatningarComponent} from "./matningar/matningar.component";
import {EditMatningComponent} from "./matningar/edit-matning.component";
import {GranskaMatningComponent} from "./matningar/granska-matning/granska-matning.component";
import {GranskningGrafComponent} from "./granskning/granskning-graf.component";
import {registerLocaleData} from "@angular/common";
import localeSv from "@angular/common/locales/sv";
import {MatningsloggComponent} from "./matningar/matningslogg/matningslogg.component";
import {VattenkemiComponent} from "./matobjekt/vattenkemi/vattenkemi.component";
import {ExportComponent} from "./export/export.component";
import {SearchExportComponent} from "./export/search-export/search-export.component";
import {ImportMatningarComponent} from "./matningar/import/import-matningar-component";
import {LarmComponent} from "./larm/larm.component";
import {SearchLarmComponent} from "./larm/search-larm/search-larm.component";
import {LarmhistorikComponent} from "./matobjekt/larmhistorik/larmhistorik.component";
import {ConfigService, MapConfig} from "../../../lib/config/config.service";
import {appConfig, urlConfig} from "./app.config";
import {MdbMapComponent} from "./common/components/mdb-map.component";
import {GranskningDataSetupComponent} from "./granskning/granskning-data-setup.component";
import {MatExpansionModule} from "@angular/material/expansion";
import {GranskningGrafSetupComponent} from "./granskning/granskning-graf-setup.component";
import {MapCoreModule} from "../../../lib/map-core/map-core.module";
import {MatobjektMapinfoComponent} from "./matobjekt/mapinfo/matobjekt-mapinfo.component";
import {PaminnelserComponent} from "./paminnelser/paminnelser.component";
import {MdbLayerPanelComponent} from "./common/components/mdb-layer-panel.component";
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {KartlagerComponent} from "./installningar/kartlager/kartlager.component";
import {EditKartlagerComponent} from "./installningar/kartlager/edit-kartlager.component";
import {MdbLayerComponent} from "./common/components/mdb-layer.component";
import { MAT_DATE_LOCALE, MAT_DATE_FORMATS, DateAdapter, MatDateFormats } from "@angular/material/core";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MomentDateAdapter, MatMomentDateModule} from "@angular/material-moment-adapter";
import { MatobjektMatrundorComponent } from "./matrunda/matobjekt-matrundor.component";
import { SelectMatrundorComponent } from "./matrunda/select-matrundor.component";
import { MatobjektgruppDialogComponent } from "./matobjektgrupp/matobjektgrupp-dialog.component";
import { SchedulerComponent } from "./scheduler/scheduler/scheduler.component";
import { RapportGrafComponent as RapportGrafComponent } from "./rapporter/rapport-graf.component";
import { RapportComponent } from "./rapporter/rapport.component";
import { RapportPageComponent } from "./rapporter/rapport-page.component";
import { EditRapportComponent } from "./rapporter/edit-rapport/edit-rapport.component";
import { EditRapportGrafComponent } from "./rapporter/edit-rapport/edit-rapport-graf.component";
import { RapportMottagareDialogComponent } from "./rapporter/edit-rapport/dialogs/rapport-mottagare-dialog.component";
import { RapportTableComponent } from "./rapporter/edit-rapport/rapport-removebutton-table.component";
import { DatetimePickerComponent } from "./common/components/datetime-picker.component";
import { MatserieDialogComponent } from "./rapporter/edit-rapport/dialogs/rapport-matserie-dialog.component";
import { RapportTableCheckboxComponent } from "./rapporter/edit-rapport/rapport-checkbox-table.component";
import { SetSpecialAuthComponent } from "./set-special-auth/set-special-auth-component";
import {SecureLoadImagePipe} from "./common/secureLoadImage.pipe";
import {AuthConfig, OAuthModule} from "angular-oauth2-oidc";
import {ApmErrorHandler} from "@elastic/apm-rum-angular";
import {XpTracingModule} from "../../../lib/tracing/tracing.module";
import { XpSpinnerButtonModule } from "../../../lib/ui/spinner-button/spinner-button.module";

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
    return httpClient.get("/configAuthIssuer", {responseType: "text"}).toPromise()
      .then(configAuthIssuer => {
        console.error("Load done!", configAuthIssuer);

        const authConfig: AuthConfig = {
          // Url of the Identity Provider
          issuer: configAuthIssuer,

          // URL of the SPA to redirect the user to after login
          redirectUri: window.location.origin + "/oauthLogin",

          // The SPA's id. The SPA is registerd with this id at the auth-server
          clientId: "miljokoll",

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
        AppShellComponent,
        MdbMapComponent,
        MatobjektComponent,
        GranskningComponent,
        RapporterComponent,
        StartComponent,
        NotAuthorizedComponent,
        MeddelandePageComponent,
        EditMeddelandeComponent,
        EditMeddelandePageComponent,
        PageNavigationComponent,
        AnvandareComponent,
        EditAnvandareComponent,
        AnvandargruppPageComponent,
        EditAnvandargruppComponent,
        AnvandargruppMatansvarComponent,
        AnvandargruppLarmComponent,
        AnvandargruppAnvandareComponent,
        SystemloggPageComponent,
        KartlagerComponent,
        EditKartlagerComponent,
        DefinitionMatningstyperComponent,
        EditDefinitionMatningstypComponent,
        ConfirmationDialogComponent,
        InformationDialogComponent,
        MatobjektComponent,
        MatobjektgruppComponent,
        EditMatobjektgruppComponent,
        EditMatobjektgruppMatobjektComponent,
        SearchMatobjektComponent,
        GrunduppgifterComponent,
        GrunduppgifterGrupperComponent,
        GrunduppgifterDokumentComponent,
        GrunduppgifterBildComponent,
        MatningstyperComponent,
        EditMatningstypComponent,
        MatobjektContainerComponent,
        SelectMatningstypComponent,
        HandelserComponent,
        SaveButtonComponent,
        DecimalNumberValueAccessor,
        EditHandelseComponent,
        DokumentComponent,
        HandelserComponent,
        GransvardenComponent,
        DokumentComponent,
        MatrundorComponent,
        EditModeDirective,
        ViewModeDirective,
        EditComponent,
        MatrundorComponent,
        EditMatrundaComponent,
        EditMatrundaViewMatningstyperComponent,
        EditMatrundaEditMatningstyperComponent,
        EditMatrundaSearchMatningstyperComponent,
        LarmnivaerComponent,
        RapporteraVattenkemiComponent,
        SearchMatobjektNamnComponent,
        EditVattenkemiComponent,
        VattenkemiRapporterComponent,
        VattenkemiMatningarComponent,
        RapporteringMatdataComponent,
        RapporteringMatningstyperComponent,
        RapporteringMatningstypComponent,
        SearchMatvardeComponent,
        MatningarComponent,
        SearchMatningComponent,
        EditMatningComponent,
        GranskaMatningComponent,
        MatningsloggComponent,
        GranskningGrafComponent,
        ExportComponent,
        SearchExportComponent,
        GranskningGrafComponent,
        VattenkemiComponent,
        ImportMatningarComponent,
        LarmComponent,
        SearchLarmComponent,
        LarmhistorikComponent,
        GranskningDataSetupComponent,
        GranskningGrafSetupComponent,
        MatobjektMapinfoComponent,
        PaminnelserComponent,
        MdbLayerPanelComponent,
        MdbLayerComponent,
        MatobjektgruppDialogComponent,
        MatobjektMatrundorComponent,
        SelectMatrundorComponent,
        SchedulerComponent,
        RapportComponent,
        RapportGrafComponent,
        RapportPageComponent,
        EditRapportComponent,
        EditRapportGrafComponent,
        RapportMottagareDialogComponent,
        RapportTableComponent,
        DatetimePickerComponent,
        MatserieDialogComponent,
        RapportTableCheckboxComponent,
        SetSpecialAuthComponent,
        SecureLoadImagePipe
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        FormsModule,
        HttpClientModule,
        MapCoreModule,
        AppRoutingModule,
        MatCheckboxModule,
        MatIconModule,
        MatInputModule,
        MatButtonModule,
        MatProgressSpinnerModule,
        MatPaginatorModule,
        MatSortModule,
        MatTableModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatDividerModule,
        MatDialogModule,
        MatRippleModule,
        MatSidenavModule,
        MatListModule,
        MatToolbarModule,
        MatTabsModule,
        MatAutocompleteModule,
        MatTooltipModule,
        DragDropModule,
        MatExpansionModule,
        MatButtonToggleModule,
        MatDatepickerModule,
        MatMomentDateModule,
        XpSpinnerButtonModule,
        XpTracingModule.forRoot({
            serviceName: "matdatabas-frontend",
            ignoreTransactions: [/\/oauthLogin\?state.*/],
            pageLoadTransactionName: "page-load"
        }),
        OAuthModule.forRoot({
            resourceServer: {
                allowedUrls: [
                    "/api",
                    "/metria-maps",
                    "/config"
                ],
                sendAccessToken: true
            }
        })
    ],
    providers: [
        { provide: ErrorHandler, useClass: ApmErrorHandler },
        { provide: MatPaginatorIntl, useClass: MatPaginatorIntlSv },
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
        { provide: MAT_DATE_FORMATS, useValue: MY_FORMATS }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
  constructor(overlayContainer: OverlayContainer) {
    overlayContainer.getContainerElement().classList.add("mat-typography");
    registerLocaleData(localeSv, "sv");
  }
}
