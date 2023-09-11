import { NgModule } from "@angular/core";
import { MkSamfallighetComponent } from "./samfallighet.component";
import { MatTabsModule } from "@angular/material/tabs";
import { CommonModule } from "@angular/common";
import { TranslocoModule } from "@ngneat/transloco";
import { MkLoggbokTabModule } from "../loggbok-tab/loggbok-tab.module";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MkAvtalProgressBarModule } from "../avtal/avtal-progress-bar/avtal-progress-bar.module";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatSelectModule } from "@angular/material/select";
import { XpSpinnerButtonModule } from "../../../../../lib/ui/spinner-button/spinner-button.module";
import { MkIntrangModule } from "../intrang/intrang.module";
import { MkAvtalskartaModule } from "../avtal/avtalskarta/avtalskarta.module";
import { MatTableModule } from "@angular/material/table";
import { MkSamfallighetContainerComponent } from "./samfallighet.container";
import { MkPipesModule } from "../../common/pipes/pipes.module";
import { XpFeedbackModule } from "../../../../../lib/ui/feedback/feedback.module";
import { MkAgareTableModule } from "../agare-table/agare-table.module";
import { MkOmbudEditModule } from "../ombud-edit/ombud-edit.module";
import { MkOmbudAddModule } from "../ombud-add/ombud-add.module";
import { MatButtonModule } from "@angular/material/button";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { XpMessageModule } from "../../../../../lib/ui/feedback/message/message.module";
import { MkElnatVarderingsprotokollModule } from "../varderingsprotokoll-elnat/varderingsprotokoll-elnat.module";
import { MkVirkesinlosenModule } from "../virkesinlosen/virkesinlosen.module";
import { MkFiberVarderingsprotokollModule } from "../varderingsprotokoll-fiber/varderingsprotokoll-fiber.module";

@NgModule({
  declarations: [
    MkSamfallighetComponent,
    MkSamfallighetContainerComponent
  ],
  imports: [
    CommonModule,
    MatTabsModule,
    TranslocoModule,
    MkLoggbokTabModule,
    MatPaginatorModule,
    MatTableModule,
    MatCheckboxModule,
    MkAvtalProgressBarModule,
    MatFormFieldModule,
    MatButtonModule,
    MatSelectModule,
    XpSpinnerButtonModule,
    MkIntrangModule,
    MkAvtalskartaModule,
    MkAgareTableModule,
    MkOmbudEditModule,
    MkOmbudAddModule,
    MkPipesModule,
    XpFeedbackModule,
    XpMessageModule,
    MkElnatVarderingsprotokollModule,
    MkVirkesinlosenModule,
    MkFiberVarderingsprotokollModule
  ],
  exports: [
    MkSamfallighetComponent,
    MkSamfallighetContainerComponent
  ]
})
export class MkSamfallighetModule {}
