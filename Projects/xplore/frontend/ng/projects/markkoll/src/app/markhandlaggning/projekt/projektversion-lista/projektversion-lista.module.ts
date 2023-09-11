import {NgModule} from "@angular/core";
import {MkProjektversionListaComponent} from "./projektversion-lista.component";
import {MkProjektversionModule} from "../projektversion/projektversion.module";
import {CommonModule} from "@angular/common";
import {TranslocoModule} from "@ngneat/transloco";
import { MkPipesModule } from "../../../common/pipes/pipes.module";

@NgModule({
  declarations: [
    MkProjektversionListaComponent
  ],
  imports: [
    CommonModule,
    MkProjektversionModule,
    TranslocoModule,
    MkPipesModule
  ],
  exports: [
    MkProjektversionListaComponent
  ]
})
export class MkProjektversionListaModule {}
