import { CommonModule } from "@angular/common";
import { MatButtonModule } from "@angular/material/button";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatOptionModule } from "@angular/material/core";
import { MatSelectModule } from "@angular/material/select";
import { MkPipesModule } from "../../common/pipes/pipes.module";
import { NgModule } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { TranslocoModule } from "@ngneat/transloco";
import { MkUserFormComponent } from "./user-form.component";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { XpPrefixedSelectionModule } from "../../../../../lib/ui/prefixed-selection/prefixed-selection.module";
import { MatChipsModule } from "@angular/material/chips";
import { MatIconModule } from "@angular/material/icon";
import { MatDialogModule } from "@angular/material/dialog";

@NgModule({
  declarations: [MkUserFormComponent],
  imports: [
    BrowserAnimationsModule,
    CommonModule,
    MatButtonModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatOptionModule,
    MatSelectModule,
    MkPipesModule,
    ReactiveFormsModule,
    TranslocoModule,
    XpPrefixedSelectionModule,
  ],
  exports: [MkUserFormComponent]
})
export class MkUserFormModule { }
