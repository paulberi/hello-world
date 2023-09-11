import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatDividerModule } from "@angular/material/divider";
import { MatIconModule } from "@angular/material/icon";
import { MatTabsModule } from "@angular/material/tabs";
import { TranslocoModule } from "@ngneat/transloco";
import { MkHanteraIntrangComponent } from "./hantera-intrang.component";
import { IntrangVerktygDirective } from "./verktyg/intrang-verktyg.directive";
import { IntrangVerktygModule } from "./verktyg/intrang-verktyg.module";

@NgModule({
  declarations: [
    MkHanteraIntrangComponent,
    IntrangVerktygDirective
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatDividerModule,
    MatIconModule,
    MatTabsModule,
    TranslocoModule,
    IntrangVerktygModule
  ],
  exports: [
    MkHanteraIntrangComponent
  ]
})
export class MkHanteraIntrangModule { }
