import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { LogoutComponent } from './pages/logout/logout.component';
import { CreateUserComponent } from './pages/user/create-user/create-user.component';
import { UpdateUserComponent } from './pages/user/update-user/update-user.component';
import { DeleteUserComponent } from './pages/user/delete-user/delete-user.component';
import { ListUsersComponent } from './pages/user/list-users/list-users.component';
import { FormsModule } from '@angular/forms';
import { AuthService } from './Services/auth.service';
import { RoleService } from './Services/role.service';
import { CommentToOrdersService } from './Services/comment-to-orders.service';
import { CompanyService } from './Services/company.service';
import { OrderService } from './Services/order.service';
import { UserService } from './Services/user.service';
import { AdmComponent } from './pages/user/adm/adm.component';
import { ListordersComponent } from './pages/order/listorders/listorders.component';
import * as $ from 'jquery';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LogoutComponent,
    CreateUserComponent,
    UpdateUserComponent,
    DeleteUserComponent,
    ListUsersComponent,
    AdmComponent,
    ListordersComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,

  ],
  providers: [
    AuthService,
    RoleService,
    OrderService,
    UserService,
    CompanyService,
    CommentToOrdersService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
