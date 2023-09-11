import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MkIntrangComponent } from "./intrang.component";
import { MatButtonModule } from "@angular/material/button";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { TranslocoModule } from "@ngneat/transloco";
import { XpSpinnerButtonModule } from "../../../../../lib/ui/spinner-button/spinner-button.module";

@NgModule({
  declarations: [
    MkIntrangComponent
  ],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    TranslocoModule,
    XpSpinnerButtonModule
  ],
  exports: [
    MkIntrangComponent
  ]
})
export class MkIntrangModule {}
