import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MkOmbudEditComponent } from "./ombud-edit.component";
import { MatFormFieldModule } from "@angular/material/form-field";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatOptionModule } from "@angular/material/core";
import { TranslocoModule } from "@ngneat/transloco";
import { MatSelectModule } from "@angular/material/select";
import { MatInputModule } from "@angular/material/input";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatButtonModule } from "@angular/material/button";
import { MkPipesModule } from "../../common/pipes/pipes.module";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatMomentDateModule } from "@angular/material-moment-adapter";
import { MatIconModule } from "@angular/material/icon";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatCheckboxModule } from "@angular/material/checkbox";

@NgModule({
  declarations: [
    MkOmbudEditComponent
  ],
  imports: [
    CommonModule,
    MatCheckboxModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    MatIconModule,
    MatOptionModule,
    MatTooltipModule,
    TranslocoModule,
    MatSelectModule,
    MatDatepickerModule,
    MatMomentDateModule,
    MatInputModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MkPipesModule
  ],
  exports: [
    MkOmbudEditComponent
  ]
})
export class MkOmbudEditModule { }
