import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MkAvtalListKartverktygComponent } from "./avtal-list-kartverktyg.component";
import { MkHanteraFastigheterModule } from "../meny/hantera-fastigheter/hantera-fastigheter.module";
import { XpExpandablePanelModule } from "../../../../../../lib/ui/expandable-panel/expandable-panel.module";
import { XpVerticalTabsModule } from "../../../../../../lib/ui/vertical-tabs/vertical-tabs.module";
import { TranslocoModule } from "@ngneat/transloco";
import { MkHanteraKartlagerModule } from "../meny/hantera-kartlager/hantera-kartlager.module";
import { MkHanteraIntrangModule } from "../meny/hantera-intrang/hantera-intrang.module";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { MkVerktygsladaModule } from "../meny/hantera-intrang/verktygslada/verktygslada.module";
import { MkAvtalListKartverktygContainerComponent } from "./avtal-list-kartverktyg.container";

@NgModule({
  declarations: [
    MkAvtalListKartverktygComponent,
    MkAvtalListKartverktygContainerComponent
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MkHanteraFastigheterModule,
    MkHanteraIntrangModule,
    MkHanteraKartlagerModule,
    MkVerktygsladaModule,
    TranslocoModule,
    XpExpandablePanelModule,
    XpVerticalTabsModule,
  ],
  exports: [
    MkAvtalListKartverktygComponent,
    MkAvtalListKartverktygContainerComponent
  ]
})
export class MkAvtalListKartverktygModule { }
