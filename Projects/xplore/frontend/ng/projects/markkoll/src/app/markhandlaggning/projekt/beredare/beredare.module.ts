import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MkBeredareComponent } from "./beredare.component";
import { MatInputModule } from "@angular/material/input";
import { TranslocoModule } from "@ngneat/transloco";

@NgModule({
  declarations: [
    MkBeredareComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    TranslocoModule
  ],
  exports: [
    MkBeredareComponent
  ]
})
export class MkBeredareModule {}
