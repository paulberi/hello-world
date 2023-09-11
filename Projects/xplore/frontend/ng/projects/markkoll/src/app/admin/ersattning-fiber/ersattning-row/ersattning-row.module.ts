import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MkErsattningRowComponent } from "./ersattning-row.component";
import { MatFormFieldModule } from "@angular/material/form-field";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatInputModule } from "@angular/material/input";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";



@NgModule({
  declarations: [
    MkErsattningRowComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
  ],
  exports: [
    MkErsattningRowComponent
  ]
})
export class MkErsattningRowModule { }
