import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { TranslocoModule } from "@ngneat/transloco";
import { AdmFastighetsokAuthFormComponent } from "./fastighetsok-auth-form.component";

@NgModule({
  declarations: [
    AdmFastighetsokAuthFormComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    TranslocoModule
  ],
  exports: [
    AdmFastighetsokAuthFormComponent
  ]
})
export class AdmFastighetsokAuthFormModule {}
