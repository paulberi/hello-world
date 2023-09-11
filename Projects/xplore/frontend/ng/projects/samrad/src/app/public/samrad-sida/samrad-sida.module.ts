import { NgModule } from "@angular/core";
import { TranslocoModule } from "@ngneat/transloco";
import { CommonModule } from "@angular/common";
import { SamradSidaComponent } from "./samrad-sida.component";
import { SrProjektkartaModule } from "../projektkarta/projektkarta.module";

@NgModule({
  declarations: [SamradSidaComponent],
  imports: [
    TranslocoModule,
    CommonModule,
    SrProjektkartaModule
  ],
  exports: [SamradSidaComponent],
})
export class SamradSidaModule {}
