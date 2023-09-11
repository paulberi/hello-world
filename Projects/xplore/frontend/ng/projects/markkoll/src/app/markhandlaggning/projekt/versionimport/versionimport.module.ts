import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { TranslocoModule } from "@ngneat/transloco";
import { XpSpinnerButtonModule } from "../../../../../../lib/ui/spinner-button/spinner-button.module";
import { MkProjektversionListaModule } from "../projektversion-lista/projektversion-lista.module";
import { MkUploadFileModule } from "../upload-file/upload-file.module";
import { MkVersionImportComponent } from "./versionimport.component";
import { MkVersionImportContainerComponent } from "./versionimport.container";

@NgModule({
  declarations: [
    MkVersionImportComponent,
    MkVersionImportContainerComponent
  ],
  imports: [
    CommonModule,
    MkUploadFileModule,
    XpSpinnerButtonModule,
    MkProjektversionListaModule,
    TranslocoModule
  ],
  exports: [
    MkVersionImportComponent,
    MkVersionImportContainerComponent,
  ]
})
export class MkVersionImportModule { }
