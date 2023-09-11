import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { TranslocoModule } from "@ngneat/transloco";
import { MkErsattningRowModule } from "../ersattning-row/ersattning-row.module";
import { MkErsattningFiberFormComponent } from "./ersattning-fiber-form.component";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MkErsattningRowCheckboxComponent } from "../ersattning-row-checkbox/ersattning-row-checkbox.component";



@NgModule({
  declarations: [MkErsattningFiberFormComponent],
  imports: [
    CommonModule,
    MkErsattningRowModule,
    TranslocoModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MkErsattningRowCheckboxComponent
  ],
  exports: [MkErsattningFiberFormComponent]
})
export class MkErsattningFiberFormModule { }
