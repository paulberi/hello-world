import { NgModule } from "@angular/core";
import { TranslocoModule } from "@ngneat/transloco";
import { CommonModule } from "@angular/common";
import { SamradListModule } from "./samrad-list/samrad-list.module";
import { SamradSidaModule } from "./samrad-sida/samrad-sida.module";
import { MinaSidorModule } from "./mina-sidor/mina-sidor.module";
import { SrProjektkartaModule } from "./projektkarta/projektkarta.module";

@NgModule({
  imports: [
    TranslocoModule,
    CommonModule,
    SamradListModule,
    SamradSidaModule,
    MinaSidorModule,
    SrProjektkartaModule,
  ]
})
export class PublicModule {}
