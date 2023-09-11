import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { TranslocoModule } from "@ngneat/transloco";
import { XpDropzoneModule } from "../../../../../../lib/ui/dropzone/dropzone.module";
import { XpSpinnerButtonModule } from "../../../../../../lib/ui/spinner-button/spinner-button.module";
import { MkHaglofImportComponent } from "./haglofimport.component";
import { MkHaglofImportContainer } from "./haglogimport.container";
import { MatListModule } from '@angular/material/list';
import { XpMessageModule } from "../../../../../../lib/ui/feedback/message/message.module";
import { MkPipesModule } from "../../../common/pipes/pipes.module";

@NgModule({
  declarations: [
    MkHaglofImportComponent,
    MkHaglofImportContainer
  ],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    XpSpinnerButtonModule,
    XpDropzoneModule,
    FormsModule,
    ReactiveFormsModule,
    TranslocoModule,
    XpMessageModule,
    MkPipesModule,
    MatListModule
  ],
  exports: [
    MkHaglofImportComponent,
    MkHaglofImportContainer
  ]
})
export class MkHaglofImportModule {}
