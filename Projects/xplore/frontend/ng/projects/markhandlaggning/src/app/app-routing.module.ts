import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { ProjectPageComponent } from "./components/project-page/project-page.component";
import { AuthGuard } from "../../auth.guard";
import { NotAuthorizedComponent } from "./components/not-authorized/not-authorized.component";


const routes: Routes = [
  { path: "notauthorized", component: NotAuthorizedComponent},
  { 
    path: "", 
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
    children: [
      { path: "", redirectTo: "/home", pathMatch: "full"},
      { path: "home", component: ProjectPageComponent},
      { path: "**", component: ProjectPageComponent }
    ]
  },


];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
