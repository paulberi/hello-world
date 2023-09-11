import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { TranslocoModule } from "@ngneat/transloco";
import { XpSpinnerButtonModule } from "../../ui/spinner-button/spinner-button.module";
import { XpEditUserComponent } from "./edit-user.component";

@NgModule({
  declarations: [XpEditUserComponent],
  imports: [
    BrowserAnimationsModule,
    CommonModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    TranslocoModule,
    ReactiveFormsModule,
    XpSpinnerButtonModule
  ],
  exports: [XpEditUserComponent]
})
export class XpEditUserModule { }
