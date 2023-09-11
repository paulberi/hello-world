import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatRadioModule } from "@angular/material/radio";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { TranslocoModule } from "@ngneat/transloco";
import { MkHaglofImportModule } from "../haglofimport/haglogimport.module";
import { MkVersionImportModule } from "../versionimport/versionimport.module";
import { MkProjektimporterComponent } from "./projektimporter.component";
import { MkProjektimporterContainer } from "./projektimporter.container";

@NgModule({
  declarations: [
    MkProjektimporterComponent,
    MkProjektimporterContainer
  ],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    MatRadioModule,
    FormsModule,
    ReactiveFormsModule,
    TranslocoModule,
    MkHaglofImportModule,
    MkVersionImportModule
  ],
  exports: [
    MkProjektimporterComponent,
    MkProjektimporterContainer
  ]
})
export class MkProjektimporterModule {}
