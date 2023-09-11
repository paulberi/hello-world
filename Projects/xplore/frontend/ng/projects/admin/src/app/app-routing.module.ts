import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AdmStartComponent} from "./start/start.component";
import {AdmManageUsersComponent} from "./manage-users/manage-users.component";
import {AuthGuard} from "./auth.guard";
import {NotAuthorizedComponent} from "./not-authorized/not-authorized.component";
import {AppShellComponent} from "./shell/app-shell.component";
import { roles } from "./roles";
import { AdmKundvyContainerComponent } from "./kundvy/kundvy.container";
import { AdmCreateKundContainerComponent } from "./create-kund/create-kund.container";

const routes: Routes = [
  {
    path: "notauthorized",
    component: NotAuthorizedComponent,
    data: {
      roles: []
    }
  },
  {
    path: "",
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
    component: AppShellComponent,
    data: {
      roles: [roles.ADMIN_API]
    },
    children: [
      {path: "", redirectTo: "/start", pathMatch: "full", data: {roles: [roles.GLOBAL_ADMIN, roles.ADMIN_API]}},
      {path: "oauthLogin", redirectTo: "/start"},
      {path: "start", component: AdmStartComponent, data: {roles: [roles.ADMIN_API]}},
      {path: ":realm/anvandare", component: AdmManageUsersComponent, data: {roles: [roles.ADMIN_API]}},
      {path: "kund", component: AdmKundvyContainerComponent, data: {roles: [roles.ADMIN_API, roles.GLOBAL_ADMIN]}},
      {path: "kund/skapa", component: AdmCreateKundContainerComponent, data: {roles: [roles.GLOBAL_ADMIN]}},
    ]
  }];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule],
})

export class AppRoutingModule {
}
