import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuard } from "./auth.guard";
import { AppShellComponent } from "./app-shell/app-shell.component";
import { XpNotAuthorizedComponent } from "../../../lib/ui/feedback/not-authorized/not-authorized.component";
import { ProfileComponent } from "./app-shell/profile/profile.component";
import { XpNotFoundComponent } from "../../../lib/ui/feedback/not-found/not-found.component";
import { MkSetSpecialAuthComponent } from "./app-shell/set-special-auth/set-special-auth.component";
import { CanDeactivateGuard } from "./unsaved-changes.guard";
import { AuthGuardAnalys } from "./auth.guard.analys";
import { MkAvtalListContainerComponent } from "./markhandlaggning/avtal/avtal-list/avtal-list.container";
import { MkCreateProjektContainerComponent } from "./markhandlaggning/projekt/create-projekt/create-projekt.container";
import { MkProjektinformationContainerComponent } from "./markhandlaggning/projekt/projektinformation/projektinformation.container";
import {MkProjektlistaComponent} from "./markhandlaggning/projekt/projektlista/projektlista.component";
import { AuthGuardAdmin } from "./auth.guard.admin";
import { MkAdminContainerComponent } from "./admin/admin.container";
import { MkProjektPageComponent } from "./markhandlaggning/projekt/projekt-page/projekt-page.component";
import { MkProjektPageContainer } from "./markhandlaggning/projekt/projekt-page/projekt-page.container";
import { MkProjektloggtabContainer } from "./markhandlaggning/projekt/projektlogg-tab/projektlogg-tab.container";
import { SamradAdminContainerComponent } from "./samrad/samrad-admin/samrad-admin.container";

const routes: Routes = [
  {
    path: "notauthorized",
    component: XpNotAuthorizedComponent
  },
  {
    path: "setSpecialAuth",
    component: MkSetSpecialAuthComponent
  },
  {
    path: "",
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
    component: AppShellComponent,
    children: [
      { path: "", redirectTo: "/projekt", pathMatch: "full"},
      {path: "oauthLogin", component: MkProjektlistaComponent},
      {
        path: "skapa-projekt",
        component: MkCreateProjektContainerComponent,
        canDeactivate: [CanDeactivateGuard]
      },
      { path: "projekt", component: MkProjektlistaComponent },
      {
        path: "projekt/:projektTyp/:projektId",
        component: MkProjektPageContainer,
        children: [
          { path: "avtal", component: MkAvtalListContainerComponent },
          { path: "projektinformation", component: MkProjektinformationContainerComponent, canDeactivate: [CanDeactivateGuard]},
          { path: "projektlogg", component: MkProjektloggtabContainer }
        ]
      },
      { 
        path: "samrad-admin/:projektId",
        component: SamradAdminContainerComponent,
        canDeactivate: [CanDeactivateGuard]
      },
      { path: "profile", component: ProfileComponent },
      { path: "admin", canActivate: [AuthGuardAdmin], component: MkAdminContainerComponent },
      { path: "404", component: XpNotFoundComponent },
      { path: "**", redirectTo: "404" },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: "legacy" })],
  exports: [RouterModule],
  providers: [CanDeactivateGuard]
})
export class MkAppRoutingModule { }
