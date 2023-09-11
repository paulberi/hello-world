import { NgModule } from "@angular/core";
import { TranslocoModule } from "@ngneat/transloco";
import { CommonModule } from "@angular/common";
import { AdminComponent } from "./admin.component";
import { MatTabsModule } from "@angular/material/tabs";
import { MatListModule } from "@angular/material/list";
import { AktuellaSamradListaModule } from "./aktuella-samrad-lista/aktuella-samrad-lista.module";
import { ArkiveradeSamradListaModule } from "./arkiverade-samrad-lista/arkiverade-samrad-lista.module";
import { SkapaSamradModule } from "./skapa-samrad/skapa-samrad.module";
import { UppdateraSamradComponent } from './uppdatera-samrad/uppdatera-samrad.component';

@NgModule({
  declarations: [AdminComponent, UppdateraSamradComponent],
  imports: [
    TranslocoModule,
    CommonModule,
    AktuellaSamradListaModule,
    ArkiveradeSamradListaModule,
    MatTabsModule,
    MatListModule,
    SkapaSamradModule
  ],
  exports: [AdminComponent],
})
export class AdminModule {}
