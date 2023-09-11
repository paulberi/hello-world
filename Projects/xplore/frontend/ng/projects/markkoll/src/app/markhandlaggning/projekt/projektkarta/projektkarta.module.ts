import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MkMapModule } from "../../../common/map/map.module";
import { MkAvtalListKartverktygModule } from "../../kartverktyg/avtal-list-kartverktyg/avtal-list-kartverktyg.module";
import { MkProjektkartaComponent } from "./projektkarta.component";
import { MkProjektkartaContainerComponent } from "./projektkarta.container";

@NgModule({
  declarations: [
    MkProjektkartaComponent,
    MkProjektkartaContainerComponent
  ],
  imports: [
    CommonModule,
    MkMapModule,
    MkAvtalListKartverktygModule
  ],
  exports: [
    MkProjektkartaComponent,
    MkProjektkartaContainerComponent
  ]
})
export class MkProjektkartaModule { }
