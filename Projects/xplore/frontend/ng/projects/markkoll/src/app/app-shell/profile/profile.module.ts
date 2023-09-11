import { CommonModule } from "@angular/common";
import { MatIconModule } from "@angular/material/icon";
import { NgModule } from "@angular/core";
import { ProfileComponent } from "./profile.component";
import { TranslocoModule } from "@ngneat/transloco";
import { XpUserModule } from "../../../../../lib/user/user.module";



@NgModule({
  declarations: [
    ProfileComponent
  ],
  imports: [
    CommonModule,
    MatIconModule,
    TranslocoModule,
    XpUserModule,
  ],
  exports: [
    ProfileComponent
  ]
})
export class MkProfileModule { }
