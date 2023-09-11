import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MkHanteraKartlagerComponent } from './hantera-kartlager.component';
import { TranslocoModule } from '@ngneat/transloco';
import { MatListModule } from '@angular/material/list';
import { MatTabsModule } from "@angular/material/tabs";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatRadioModule } from "@angular/material/radio";
import { MkCommonModule } from "../../../../common/common.module";
import { MkLegendComponent } from "../../../../common/legend/legend.component";

@NgModule({
  declarations: [
    MkHanteraKartlagerComponent
  ],
  imports: [
    CommonModule,
    MatCheckboxModule,
    MatRadioModule,
    MatListModule,
    MatTabsModule,
    MatExpansionModule,
    MkCommonModule,
    TranslocoModule
  ],
  exports: [
    MkHanteraKartlagerComponent
  ]
})

export class MkHanteraKartlagerModule {
}
