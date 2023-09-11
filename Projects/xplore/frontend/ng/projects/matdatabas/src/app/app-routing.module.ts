import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {StartComponent} from "./start/start.component";
import {AuthGuard} from "./auth.guard";
import {AppShellComponent} from "./app-shell.component";
import {NotAuthorizedComponent} from "./not-authorized/not-authorized.component";
import {EditMeddelandeComponent} from "./installningar/meddelanden/edit-meddelande.component";
import {EditMeddelandePageComponent} from "./installningar/meddelanden/edit-meddelande-page.component";
import {AnvandareComponent} from "./installningar/anvandare/anvandare.component";
import {EditAnvandareComponent} from "./installningar/anvandare/edit-anvandare.component";
import {AnvandargruppPageComponent} from "./installningar/anvandargrupp/anvandargrupp-page.component";
import {EditAnvandargruppComponent} from "./installningar/anvandargrupp/edit-anvandargrupp/edit-anvandargrupp.component";
import {SystemloggPageComponent} from "./installningar/systemlogg/systemlogg-page.component";
import {MatobjektComponent} from "./matobjekt/matobjekt.component";
import {MatobjektgruppComponent} from "./matobjektgrupp/matobjektgrupp.component";
import {MatningstyperComponent} from "./matobjekt/matningstyper/matningstyper.component";
import {MatobjektContainerComponent} from "./matobjekt/matobjekt-container.component";
import {SelectMatningstypComponent} from "./matobjekt/matningstyper/select-matningstyp.component";
import { HandelserComponent } from "./matobjekt/handelser/handelser.component";
import {GrunduppgifterComponent} from "./matobjekt/grunduppgifter/grunduppgifter.component";
import {EditMatningstypComponent} from "./matobjekt/matningstyper/edit-matningstyp.component";
import {EditMatobjektgruppComponent} from "./matobjektgrupp/edit-matobjektgrupp.component";
import { EditHandelseComponent } from "./matobjekt/handelser/edit-handelse.component";
import {DefinitionMatningstyperComponent} from "./definition-matningstyper/definition-matningstyper.component";
import {EditDefinitionMatningstypComponent} from "./definition-matningstyper/edit-definition-matningstyp.component";
import {MatrundorComponent} from "./matrunda/matrundor.component";
import {GransvardenComponent} from "./gransvarden/gransvarden.component";
import {EditMatrundaComponent} from "./matrunda/edit-matrunda/edit-matrunda.component";
import {LarmnivaerComponent} from "./installningar/larmnivaer/larmnivaer.component";
import {RapporteraVattenkemiComponent} from "./matningar/vattenkemi/rapportera-vattenkemi.component";
import {RapporteringMatdataComponent} from "./matningar/rapportering/rapportering-matdata.component";
import {RapporterComponent} from "./rapporter/rapporter.component";
import {GranskningComponent} from "./granskning/granskning.component";
import {MatningarComponent} from "./matningar/matningar.component";
import {EditMatningComponent} from "./matningar/edit-matning.component";
import {GranskningGrafComponent} from "./granskning/granskning-graf.component";
import {ExportComponent} from "./export/export.component";
import {VattenkemiComponent} from "./matobjekt/vattenkemi/vattenkemi.component";
import {EditVattenkemiComponent} from "./matobjekt/vattenkemi/edit-vattenkemi.component";
import {ImportMatningarComponent} from "./matningar/import/import-matningar-component";
import {LarmComponent} from "./larm/larm.component";
import {LarmhistorikComponent} from "./matobjekt/larmhistorik/larmhistorik.component";
import {PaminnelserComponent} from "./paminnelser/paminnelser.component";
import {KartlagerComponent} from "./installningar/kartlager/kartlager.component";
import {EditKartlagerComponent} from "./installningar/kartlager/edit-kartlager.component";
import {CanDeactivateGuard} from "./services/can-deactivate-guard.service";
import {SchedulerComponent} from "./scheduler/scheduler/scheduler.component";
import { RapportPageComponent } from "./rapporter/rapport-page.component";
import { EditRapportComponent } from "./rapporter/edit-rapport/edit-rapport.component";
import { SetSpecialAuthComponent} from "./set-special-auth/set-special-auth-component";
import {MatobjektMatrundorComponent} from "./matrunda/matobjekt-matrundor.component";
import {SelectMatrundorComponent} from "./matrunda/select-matrundor.component";

const routes: Routes = [
  {
    path: "notauthorized",
    component: NotAuthorizedComponent
  },
  {
      path: "rapport/:id",
      component: RapportPageComponent
  },
  {
    path: "setSpecialAuth",
    component: SetSpecialAuthComponent
  },
  {
    path: "",
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
    component: AppShellComponent,
    children: [
      {path: "", redirectTo: "/start/0", pathMatch: "full"},
      {path: "oauthLogin", component: StartComponent},
      {path: "editrapport", component: EditRapportComponent, canDeactivate: [CanDeactivateGuard]},
      {path: "editrapport/:id", component: EditRapportComponent, canDeactivate: [CanDeactivateGuard]},
      {path: "start", redirectTo: "start/0"},
      {path: "start/:page", component: StartComponent},
      {path: "matobjekt", component: MatobjektComponent},
      {path: "matobjekt/:id", component: MatobjektContainerComponent, children: [
          {path: "", redirectTo: "grunduppgifter", pathMatch: "full" },
          {path: "grunduppgifter", component: GrunduppgifterComponent, canDeactivate: [CanDeactivateGuard]},
          {path: "matningstyper", component: MatningstyperComponent},
          {path: "matningstyper/select", component: SelectMatningstypComponent},
          {path: "matningstyper/:id", component: EditMatningstypComponent, canDeactivate: [CanDeactivateGuard]},
          {path: "matningstyper/:id/gransvarden", component: GransvardenComponent, canDeactivate: [CanDeactivateGuard]},
          {path: "matningstyper/:id/matningar", component: MatningarComponent},
          {path: "matningstyper/:id/matningar/:matningid", component: EditMatningComponent, canDeactivate: [CanDeactivateGuard]},
          {path: "matrundor", component: MatobjektMatrundorComponent},
          {path: "matrundor/select", component: SelectMatrundorComponent},
          {path: "analyser", component: VattenkemiComponent},
          {path: "analyser/:id", component: EditVattenkemiComponent, canDeactivate: [CanDeactivateGuard]},
          {path: "handelser", component: HandelserComponent},
          {path: "handelser/:id", component: EditHandelseComponent, canDeactivate: [CanDeactivateGuard] },
          {path: "larmhistorik", component: LarmhistorikComponent},
        ]
      },
      {path: "matobjektgrupp", component: MatobjektgruppComponent},
      {path: "matobjektgrupp/:id", component: EditMatobjektgruppComponent, canDeactivate: [CanDeactivateGuard]},
      {path: "matrunda", component: MatrundorComponent},
      {path: "matrunda/:id", component: EditMatrundaComponent, canDeactivate: [CanDeactivateGuard] },
      {path: "export", component: ExportComponent},
      {path: "rapporter", component: RapporterComponent},
      {path: "rapportera-vattenkemi", component: RapporteraVattenkemiComponent, canDeactivate: [CanDeactivateGuard]},
      {path: "rapportera-matdata", component: RapporteringMatdataComponent, canDeactivate: [CanDeactivateGuard]},
      {path: "anvandare", component: AnvandareComponent},
      {path: "anvandare/:id", component: EditAnvandareComponent, canDeactivate: [CanDeactivateGuard] },
      {path: "anvandargrupp", component: AnvandargruppPageComponent},
      {path: "anvandargrupp/:id", component: EditAnvandargruppComponent, canDeactivate: [CanDeactivateGuard]},
      {path: "definitionmatningstyp", component: DefinitionMatningstyperComponent},
      {path: "definitionmatningstyp/:id", component: EditDefinitionMatningstypComponent, canDeactivate: [CanDeactivateGuard]},
      {path: "meddelanden", redirectTo: "meddelanden/0"},
      {path: "meddelanden/:page", component: EditMeddelandePageComponent},
      {path: "meddelande/:id", component: EditMeddelandeComponent, canDeactivate: [CanDeactivateGuard]},
      {path: "systemlogg", component: SystemloggPageComponent},
      {path: "larmnivaer", component: LarmnivaerComponent, canDeactivate: [CanDeactivateGuard]},
      {path: "kartlager", component: KartlagerComponent},
      {path: "kartlager/:id", component: EditKartlagerComponent, canDeactivate: [CanDeactivateGuard]},
      {path: "granskning", component: GranskningComponent},
      {path: "larm", component: LarmComponent},
      {path: "paminnelser", component: PaminnelserComponent},
      {path: "granskning-graf", component: GranskningGrafComponent},
      {path: "import", component: ImportMatningarComponent, canDeactivate: [CanDeactivateGuard] },
      {path: "scheduler", component: SchedulerComponent}
    ]
  }];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule],
  providers: [CanDeactivateGuard]
})

export class AppRoutingModule {
}
