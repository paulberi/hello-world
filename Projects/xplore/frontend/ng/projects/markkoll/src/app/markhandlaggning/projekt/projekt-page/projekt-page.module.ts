import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatIconModule } from "@angular/material/icon";
import { MatMenu, MatMenuModule } from "@angular/material/menu";
import { MatTabsModule } from "@angular/material/tabs";
import { RouterModule } from "@angular/router";
import { TranslocoModule } from "@ngneat/transloco";
import { MkAvtalListModule } from "../../avtal/avtal-list/avtal-list.module";
import { MkProjektinformationModule } from "../projektinformation/projektinformation.module";
import { MkProjektkartaModule } from "../projektkarta/projektkarta.module";
import { MkProjektloggTabModule } from "../projektlogg-tab/projektlogg-tab.module";
import { MkProjektPageComponent } from "./projekt-page.component";
import { MkProjektPageContainer } from "./projekt-page.container";

@NgModule({
  declarations: [
    MkProjektPageComponent,
    MkProjektPageContainer
  ],
  imports: [
    CommonModule,
    MatMenuModule,
    MatIconModule,
    MatTabsModule,
    MkAvtalListModule,
    MkProjektinformationModule,
    MkProjektkartaModule,
    MkProjektloggTabModule,
    TranslocoModule,
    MatCheckboxModule,
    MatButtonModule,
    RouterModule
  ],
  exports: [
    MkProjektPageComponent,
    MkProjektPageContainer
  ]
})
export class MkProjektPageModule {}
