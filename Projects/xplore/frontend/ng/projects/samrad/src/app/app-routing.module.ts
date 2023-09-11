import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AdminComponent } from "./admin/admin.component";
import { SkapaSamradComponent } from "./admin/skapa-samrad/skapa-samrad.component";
import { UppdateraSamradComponent } from "./admin/uppdatera-samrad/uppdatera-samrad.component";
import { AppShellComponent } from "./app-shell/app-shell.component";
import { AdminAuthGuard } from "./adminAuth.guard";
import { UserAuthGuard } from "./userAuth.guard";
import { SamradListComponent } from "./public/samrad-list/samrad-list.component";
import { SamradSidaComponent } from "./public/samrad-sida/samrad-sida.component";
import { MinaSidorComponent } from "./public/mina-sidor/mina-sidor.component";
import { XpNotFoundComponent } from "../../../lib/ui/feedback/not-found/not-found.component";

const routes: Routes = [
  {
    path: "",
    component: AppShellComponent,
    children: [
      { path: "admin", component: AdminComponent, canActivate: [AdminAuthGuard] },
      { path: "admin/skapa-samrad", component: SkapaSamradComponent,canActivate: [AdminAuthGuard] },
      { path: "admin/redirect", redirectTo: "/admin" },
      { path: "admin/:samradId",component: UppdateraSamradComponent, canActivate: [AdminAuthGuard]  },
      { path: "", component: SamradListComponent, pathMatch: "full"},
      { path: "login", component: SamradSidaComponent, canActivate: [UserAuthGuard] },
      { path: "mina-sidor", component: MinaSidorComponent, canActivate: [UserAuthGuard] },
      { path: "redirect", redirectTo: "/mina-sidor" },
      { path: "404", component: XpNotFoundComponent},
      { path: ":samradId", component: SamradSidaComponent },
      { path: "**", redirectTo: "404" },
    ],
  },
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
