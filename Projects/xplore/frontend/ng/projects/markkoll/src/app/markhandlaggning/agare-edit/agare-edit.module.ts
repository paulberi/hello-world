import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MkAgareEditComponent } from "./agare-edit.component";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { TranslocoModule } from "@ngneat/transloco";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatOptionModule } from "@angular/material/core";
import { MatSelectModule } from "@angular/material/select";
import { MatInputModule } from "@angular/material/input";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatButtonModule } from "@angular/material/button";
import { MkPipesModule } from "../../common/pipes/pipes.module";
import { MatIconModule } from "@angular/material/icon";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatMomentDateModule } from "@angular/material-moment-adapter";
import { MkCountrySelectModule } from "../../common/country-select/country-select.module";

@NgModule({
  declarations: [
    MkAgareEditComponent,
  ],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatCheckboxModule,
    FormsModule,
    ReactiveFormsModule,
    MatOptionModule,
    MatDatepickerModule,
    MatMomentDateModule,
    TranslocoModule,
    MatSelectModule,
    MatIconModule,
    MatInputModule,
    MatTooltipModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MkPipesModule,
    MkCountrySelectModule
  ],
  exports: [
    MkAgareEditComponent,
  ]
})
export class MkAgareEditModule { }
