import { NgModule } from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {MapPageComponent} from "./components/map/map-page.component";
import {ReportComponent} from "./components/report/report.component";
import {AuthGuard} from "./auth.guard";
import {AppShellComponent} from "./components/app-shell/app-shell.component";
import {SetSpecialAuthComponent} from "./set-special-auth/set-special-auth-component";

const routes: Routes = [
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
      { path: "", component: MapPageComponent},
      {path: "oauthLogin", component: MapPageComponent},
      {
        path: "r",
        component: ReportComponent
      },
    ]
  }
];


@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
