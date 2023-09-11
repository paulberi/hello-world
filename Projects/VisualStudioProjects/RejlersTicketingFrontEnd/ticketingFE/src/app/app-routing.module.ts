import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { ListordersComponent } from './pages/order/listorders/listorders.component';
import { AdmComponent } from './pages/user/adm/adm.component';
import { ListUsersComponent } from './pages/user/list-users/list-users.component';

const routes: Routes = [
  {path:"", redirectTo: 'rejlers/login',pathMatch: "full"},
  {path:"rejlers/login",component:LoginComponent},
  {path:"rejlers/user",component:ListUsersComponent},
  {path: "rejlers/orders", component:ListordersComponent},
  {path: "rejlers/admin", component:AdmComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
