import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppShellComponent } from './app-shell/app-shell.component';
import { AuthGuard } from './shared/guards/auth.guard';
import { XpNotAuthorizedComponent } from '../../../lib/ui/feedback/not-authorized/not-authorized.component';
import { AuthRolesGuard } from './shared/guards/auth-roles.guard';
import { roles } from "./shared/utils/roles";

const routes: Routes = [
  {
    path: "notauthorized",
    component: XpNotAuthorizedComponent
  },
  {
    path: "",
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
    component: AppShellComponent,
    children: [
      { path: "", redirectTo: "/produkter", pathMatch: "full" },
      { path: "oauthLogin", redirectTo: "/produkter" },
      {
        path: "bestallningar",
        loadChildren: () => import("./bestallningar/feature/bestallningar-shell/bestallningar-shell.module").then((m) => m.MMBestallningarShellModule),
        data: { roles: roles.METRIA_SALJARE },
        canActivate: [AuthRolesGuard]
      },
      { path: "mina-abonnemang", loadChildren: () => import("./mina-abonnemang/mina-abonnemang.module").then((m) => m.MMMinaAbonnemangModule) },
      { path: "orderhistorik", loadChildren: () => import("./orderhistorik/orderhistorik.module").then((m) => m.MMOrderhistorikModule) },
      { path: "produkter", loadChildren: () => import("./produkter/feature/produkter-shell/produkter-shell.module").then((m) => m.MMProdukterShellModule) },
      { path: "utcheckning", loadChildren: () => import("./utcheckning/feature/utcheckning-shell/utcheckning-shell.module").then((m) => m.MMUtcheckningShellModule) },
      { path: "**", redirectTo: "/produkter" }
    ]
  }];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
