import { NgModule } from "@angular/core";
import { MatTabsModule } from "@angular/material/tabs";
import { MkAvtalProgressBarModule } from "../avtal/avtal-progress-bar/avtal-progress-bar.module";
import { MkFastighetComponent } from "./fastighet.component";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatIconModule } from "@angular/material/icon";
import { TranslocoModule } from "@ngneat/transloco";
import { CommonModule } from "@angular/common";
import { MkAgareEditModule } from "../agare-edit/agare-edit.module";
import { MkOmbudEditModule } from "../ombud-edit/ombud-edit.module";
import { MkAgareTableModule } from "../agare-table/agare-table.module";
import { MkOmbudAddModule } from "../ombud-add/ombud-add.module";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatSelectModule } from "@angular/material/select";
import { FormsModule } from "@angular/forms";
import { MkIntrangModule } from "../intrang/intrang.module";
import { MkFastighetContainerComponent } from "./fastighet.container";
import { MkAvtalskartaModule } from "../avtal/avtalskarta/avtalskarta.module";
import { MatButtonModule } from "@angular/material/button";
import { MkAvtalGenerateButtonModule } from "../avtal/avtal-generate-button/avtal-generate-button.module";
import { MkLoggbokModule } from "../loggbok/loggbok.module";
import { MatInputModule } from "@angular/material/input";
import { XpSpinnerButtonModule } from "../../../../../lib/ui/spinner-button/spinner-button.module";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MkPipesModule } from "../../common/pipes/pipes.module";
import { MkLoggbokTabModule } from "../loggbok-tab/loggbok-tab.module";
import { XpMessageModule } from "../../../../../lib/ui/feedback/message/message.module";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatButtonToggleModule } from "@angular/material/button-toggle";
import { MkElnatVarderingsprotokollModule } from "../varderingsprotokoll-elnat/varderingsprotokoll-elnat.module";
import {MkVirkesinlosenModule} from "../virkesinlosen/virkesinlosen.module";
import { MkFiberVarderingsprotokollModule } from "../varderingsprotokoll-fiber/varderingsprotokoll-fiber.module";

@NgModule({
  declarations: [
    MkFastighetComponent,
    MkFastighetContainerComponent
  ],
    imports: [
        CommonModule,
        MatFormFieldModule,
        MatCheckboxModule,
        FormsModule,
        XpMessageModule,
        MatButtonModule,
        MatSelectModule,
        MatIconModule,
        MatTabsModule,
        MkOmbudAddModule,
        MkAvtalProgressBarModule,
        MkAvtalGenerateButtonModule,
        MkAvtalskartaModule,
        MkAgareTableModule,
        MkAgareEditModule,
        MkOmbudEditModule,
        MkIntrangModule,
        TranslocoModule,
        MkLoggbokModule,
        MatInputModule,
        MatSidenavModule,
        MatButtonToggleModule,
        XpSpinnerButtonModule,
        MatProgressSpinnerModule,
        MkPipesModule,
        MkLoggbokTabModule,
        MkElnatVarderingsprotokollModule,
        MkFiberVarderingsprotokollModule,
        MkVirkesinlosenModule
    ],
  exports: [
    MkFastighetComponent,
    MkFastighetContainerComponent
  ]
})
export class MkFastighetsinformationModule {}
