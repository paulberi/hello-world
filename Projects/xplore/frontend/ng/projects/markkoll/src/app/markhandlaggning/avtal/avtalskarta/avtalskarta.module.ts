import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MkAvtalskartaComponent } from "./avtalskarta.component";
import { MkMapModule } from "../../../common/map/map.module";
import { TranslocoModule } from "@ngneat/transloco";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { HttpClientModule } from "@angular/common/http";

@NgModule({
  declarations: [
    MkAvtalskartaComponent
  ],
  imports: [
    CommonModule,
    MkMapModule,
    MatButtonModule,
    MatIconModule,
    TranslocoModule,
    HttpClientModule
  ],
  exports: [
    MkAvtalskartaComponent
  ]
})
export class MkAvtalskartaModule {}
