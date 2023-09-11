import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { NgModule } from "@angular/core";
import { TranslocoModule } from "@ngneat/transloco";
import { XpUserProfileComponent } from "./user-profile/user-profile.component";



@NgModule({
  declarations: [XpUserProfileComponent],
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    ReactiveFormsModule,
    TranslocoModule
  ],
  exports: [
    XpUserProfileComponent
  ]
})
export class XpUserModule { }
