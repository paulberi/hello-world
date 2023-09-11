import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminHeaderComponent } from './pages/admin/admin-header/admin-header.component';
import { AdminMealsComponent } from './pages/admin/admin-meals/admin-meals.component';
import { AdminMenusComponent } from './pages/admin/admin-menus/admin-menus.component';
import { AdminSchoolsComponent } from './pages/admin/admin-schools/admin-schools.component';
import { CreateUserComponent } from './pages/admin/admin-user/create-user/create-user.component';
import { DeleteUserComponent } from './pages/admin/admin-user/delete-user/delete-user.component';
import { UpdateUserComponent } from './pages/admin/admin-user/update-user/update-user.component';
import { UserHeaderComponent } from './pages/admin/admin-user/user-header/user-header.component';

import { LoginFailedComponent } from './pages/login-failed/login-failed.component';
import { LogoutComponent } from './pages/logout/logout.component';
import { MenuComponent } from './pages/menu/menu.component';
import { AuthGuard } from '@auth0/auth0-angular';
import { LoginComponent } from './pages/login/login.component';

const routes: Routes = [
  { path:  '', redirectTo: '/', pathMatch: 'full' },
  { path: '', component: MenuComponent },
  { path: 'adminMeals', component: AdminMealsComponent },
  { path: 'adminSchools', component: AdminSchoolsComponent},
  { path: 'adminMenus', component: AdminMenusComponent},
  { path: 'admin', component: AdminHeaderComponent },
  { path: 'logout', component: LogoutComponent },
  { path: 'login', component: LoginComponent },
  { path: 'loginFailed', component : LoginFailedComponent },
  { path: 'user', component: UserHeaderComponent },
  { path: 'createUser', component: CreateUserComponent},
  { path: 'updateUser', component: UpdateUserComponent},
  { path: 'deleteUser', component: DeleteUserComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
