import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatDividerModule } from "@angular/material/divider";
import { MatExpansionModule } from "@angular/material/expansion";
import { TranslocoModule } from "@ngneat/transloco";
import { MkUserFormModule } from "../../user/user-form/user-form.module";
import { MkUsersComponent } from "./users.component";
import { MkUsersContainer } from "./users.container";

@NgModule({
  declarations: [
    MkUsersComponent,
    MkUsersContainer
  ],
  imports: [
    CommonModule,
    MatDividerModule,
    MatExpansionModule,
    MkUserFormModule,
    TranslocoModule
  ],
  exports: [
    MkUsersComponent,
    MkUsersContainer
  ]
})
export class MkUsersModule { }
