import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MatTabsModule } from "@angular/material/tabs";
import { MatListModule } from "@angular/material/list";
import { MinaSidorComponent } from "./mina-sidor.component";
import { MatIconModule } from "@angular/material/icon";
import { TranslocoModule } from "@ngneat/transloco";





@NgModule({
  declarations: [MinaSidorComponent],
  imports: [
    CommonModule,
    MatTabsModule,
    MatListModule,
    MatIconModule,
    TranslocoModule,
  ],
  exports: [MinaSidorComponent],
})
export class MinaSidorModule {}