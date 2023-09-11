import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { SrMapModule } from "../../utils/map/map.module";
import { SrProjektkartaComponent } from "./projektkarta.component";
import { SrProjektkartaContainerComponent } from "./projektkarta.container";

@NgModule({
  declarations: [SrProjektkartaComponent, SrProjektkartaContainerComponent],
  imports: [CommonModule, SrMapModule],
  exports: [SrProjektkartaComponent, SrProjektkartaContainerComponent],
})
export class SrProjektkartaModule {}
