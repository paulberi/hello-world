import { MkOmbudAddModule } from "./ombud-add/ombud-add.module";
import { MkAvtalProgressBarModule } from "./avtal/avtal-progress-bar/avtal-progress-bar.module";
import { MkAgareEditModule } from "./agare-edit/agare-edit.module";
import { MkOmbudEditModule } from "./ombud-edit/ombud-edit.module";
import { MkAvtalGenerateButtonModule } from "./avtal/avtal-generate-button/avtal-generate-button.module";
import { MkFastighetsinformationModule } from "./fastighet/fastighet.module";
import { XpAccordionTableModule } from "../../../../lib/ui/accordion-table/accordion-table.module";
import { MkAvtalFilterModule } from "./avtal/avtal-filter/avtal-filter.module";
import { NgModule } from "@angular/core";
import { MkAgareTableModule } from "./agare-table/agare-table.module";
import { MkAvtalskartaModule } from "./avtal/avtalskarta/avtalskarta.module";
import { MkIntrangModule } from "./intrang/intrang.module";
import { XpUiSearchFieldModule } from "../../../../lib/ui/search-field/search-field.module";
import { XpSpinnerButtonModule } from "../../../../lib/ui/spinner-button/spinner-button.module";
import { MkAvtalListModule } from "./avtal/avtal-list/avtal-list.module";
import { MkAgareImportDialogModule } from "./agare-import-dialog/agare-import-dialog.module";
import { MkFastighetListModule } from "./fastighet-list/fastighet-list.module";
import { MkSamfallighetModule } from "./samfallighet/samfallighet.module";
import { MkLoggbokTabModule } from "./loggbok-tab/loggbok-tab.module";
import { MkPipesModule } from "../common/pipes/pipes.module";
import { MkCreateProjektModule } from "./projekt/create-projekt/create-projekt.module";
import { MkProjektinformationModule } from "./projekt/projektinformation/projektinformation.module";
import { MkProjektlistaModule } from "./projekt/projektlista/projektlista.module";
import { MkProjektdokumentModule } from "./projekt/projektdokument/projektdokument.module";
import { MkProjektimporterModule } from "./projekt/projektimporter/projektimporter.module";
import { MkHaglofImportModule } from "./projekt/haglofimport/haglogimport.module";
import { MkVersionImportModule } from "./projekt/versionimport/versionimport.module";
import { MkEditProjektModule } from "./projekt/edit-projekt/edit-projekt.module";
import { ProjektdokumentTableComponent } from "./projekt/projektdokument-table/projektdokument-table.component";

@NgModule({
  imports: [
    XpAccordionTableModule,
    MkOmbudAddModule,
    MkAgareTableModule,
    MkAvtalFilterModule,
    MkAvtalGenerateButtonModule,
    MkAvtalskartaModule,
    MkAvtalProgressBarModule,
    MkFastighetsinformationModule,
    MkIntrangModule,
    MkAgareEditModule,
    MkOmbudEditModule,
    XpUiSearchFieldModule,
    XpSpinnerButtonModule,
    MkAvtalListModule,
    MkAgareImportDialogModule,
    MkFastighetListModule,
    MkSamfallighetModule,
    MkLoggbokTabModule,
    MkPipesModule,
    MkCreateProjektModule,
    MkEditProjektModule,
    MkProjektinformationModule,
    MkProjektlistaModule,
    MkProjektdokumentModule,
    MkHaglofImportModule,
    MkVersionImportModule,
    MkProjektimporterModule
  ]
})
export class MkMarkhandlaggningModule { }
