import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatOptionModule } from "@angular/material/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { TranslocoModule } from "@ngneat/transloco";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MkPipesModule } from "../../../common/pipes/pipes.module";
import { MatInputModule } from "@angular/material/input";
import { MatSelectModule } from "@angular/material/select";
import { MkEditProjektComponent } from "./edit-projekt.component";
import { MatCheckboxModule } from "@angular/material/checkbox";

@NgModule({
  declarations: [
    MkEditProjektComponent
  ],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatCheckboxModule,
    MatOptionModule,
    MatDatepickerModule,
    MatInputModule,
    MatSelectModule,
    ReactiveFormsModule,
    FormsModule,
    TranslocoModule,
    MkPipesModule
  ],
  exports: [
    MkEditProjektComponent
  ]
})
export class MkEditProjektModule {
}
