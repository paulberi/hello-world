import { NgModule } from "@angular/core";
import { MatTabsModule } from "@angular/material/tabs";
import { MkFastighetsinformationModule } from "../../fastighet/fastighet.module";
import { MkAvtalListComponent } from "./avtal-list.component";
import { CommonModule } from "@angular/common";
import { MkAvtalFilterModule } from "../avtal-filter/avtal-filter.module";
import { MatIconModule } from "@angular/material/icon";
import { RouterModule } from "@angular/router";
import { TranslocoModule } from "@ngneat/transloco";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MkAvtalGenerateButtonModule } from "../avtal-generate-button/avtal-generate-button.module";
import { MatButtonModule } from "@angular/material/button";
import { MkMapModule } from "../../../common/map/map.module";
import { MatSelectModule } from "@angular/material/select";
import { MatFormFieldModule } from "@angular/material/form-field";
import { ReactiveFormsModule } from "@angular/forms";
import { MkAvtalListContainerComponent } from "./avtal-list.container";
import { MkFastighetListModule } from "../../fastighet-list/fastighet-list.module";
import { MkSamfallighetModule } from "../../samfallighet/samfallighet.module";
import { MkPipesModule } from "../../../common/pipes/pipes.module";
import { MatCardModule } from "@angular/material/card";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { XpMessageModule } from "../../../../../../lib/ui/feedback/message/message.module";
import { MkAvtalActionsModule } from "../avtal-actions/avtal-actions.module";
import { XpExpandablePanelModule } from "../../../../../../lib/ui/expandable-panel/expandable-panel.module";
import { XpVerticalTabsModule } from "../../../../../../lib/ui/vertical-tabs/vertical-tabs.module";
import { MkAvtalListKartverktygModule } from "../../kartverktyg/avtal-list-kartverktyg/avtal-list-kartverktyg.module";
import { MkEditVarderingsprotokollMetadataModule } from "../../varderingsprotokoll-elnat/edit-varderingsprotokoll-metadata/edit-varderingsprotokoll-metadata.module";

@NgModule({
  declarations: [
    MkAvtalListComponent,
    MkAvtalListContainerComponent
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    MatTabsModule,
    MkAvtalFilterModule,
    MkAvtalGenerateButtonModule,
    MkFastighetListModule,
    MkFastighetListModule,
    MkFastighetsinformationModule,
    MkMapModule,
    MkPipesModule,
    MkSamfallighetModule,
    ReactiveFormsModule,
    RouterModule,
    TranslocoModule,
    XpMessageModule,
    MkAvtalActionsModule,
    XpExpandablePanelModule,
    XpVerticalTabsModule,
    MkAvtalListKartverktygModule,
    MkEditVarderingsprotokollMetadataModule
  ],
  exports: [
    MkAvtalListComponent,
    MkAvtalListContainerComponent
  ]
})
export class MkAvtalListModule { }
