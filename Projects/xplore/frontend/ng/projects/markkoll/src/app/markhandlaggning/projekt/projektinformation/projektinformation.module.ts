import {NgModule} from "@angular/core";
import {MkProjektinformationComponent} from "./projektinformation.component";
import {CommonModule} from "@angular/common";
import {MkProjektinformationContainerComponent} from "./projektinformation.container";
import {MatButtonModule} from "@angular/material/button";
import {TranslocoModule} from "@ngneat/transloco";
import {XpSpinnerButtonModule} from "../../../../../../lib/ui/spinner-button/spinner-button.module";
import { MkProjektimporterModule } from "../projektimporter/projektimporter.module";
import { MkProjektBehorighetModule } from "../projekt-behorighet/projekt-behorighet.module";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { XpMessageModule } from "../../../../../../lib/ui/feedback/message/message.module";
import { XpNotFoundModule } from "../../../../../../lib/ui/feedback/not-found/not-found.module";
import { MkEditProjektModule } from "../edit-projekt/edit-projekt.module";
import { MkUserRolesModule } from "../user-roles/user-roles.module";
import { MkExpansionPanelModule } from "../../../common/expansion-panel/expansion-panel.module";
import { MkBeredareModule } from "../beredare/beredare.module";
import { AvtalsinstallningarComponent } from "../avtalsinstallningar/avtalsinstallningar.component";

@NgModule({
  declarations: [
    MkProjektinformationComponent,
    MkProjektinformationContainerComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MkBeredareModule,
    MkEditProjektModule,
    MkExpansionPanelModule,
    MkProjektBehorighetModule,
    MkUserRolesModule,
    MkProjektimporterModule,
    ReactiveFormsModule,
    TranslocoModule,
    XpMessageModule,
    XpNotFoundModule,
    XpSpinnerButtonModule,
    AvtalsinstallningarComponent
  ],
  exports: [
    MkProjektinformationComponent,
    MkProjektinformationContainerComponent
  ]
})
export class MkProjektinformationModule {}
