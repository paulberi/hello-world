import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatDialogModule } from "@angular/material/dialog";
import { MatDividerModule } from "@angular/material/divider";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatSelectModule } from "@angular/material/select";
import { MatTabsModule } from "@angular/material/tabs";
import { TranslocoModule } from "@ngneat/transloco";
import { XpSpinnerButtonModule } from "../../../../../../../../../lib/ui/spinner-button/spinner-button.module";
import { MkPipesModule } from "../../../../../../common/pipes/pipes.module";
import { MkIntrangFormComponent } from "./intrang-form.component";

@NgModule({
  declarations: [
    MkIntrangFormComponent
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    MkPipesModule,
    ReactiveFormsModule,
    TranslocoModule,

    MatDialogModule,
    XpSpinnerButtonModule
  ],
  exports: [MkIntrangFormComponent]
})
export class MkIntrangFormModule { }
