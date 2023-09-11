import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MkOmbudAddComponent } from "./ombud-add.component";
import { TranslocoModule } from "@ngneat/transloco";
import { MatFormFieldModule } from "@angular/material/form-field";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

@NgModule({
  declarations: [
    MkOmbudAddComponent
  ],
  imports: [
    CommonModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    TranslocoModule,
    MatInputModule,
    MatButtonModule,
    BrowserAnimationsModule
  ],
  exports: [
    MkOmbudAddComponent
  ]
})
export class MkOmbudAddModule { }
