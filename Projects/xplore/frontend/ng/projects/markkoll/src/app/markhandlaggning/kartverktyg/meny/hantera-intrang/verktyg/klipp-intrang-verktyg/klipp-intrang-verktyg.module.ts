import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KlippIntrangVerktygComponent } from './klipp-intrang-verktyg.component';
import { MatSelectModule } from "@angular/material/select";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { TranslocoModule } from "@ngneat/transloco";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MkPipesModule } from "../../../../../../common/pipes/pipes.module";

@NgModule({
  declarations: [
    KlippIntrangVerktygComponent
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatSelectModule,
    MatIconModule,
    FormsModule,
    ReactiveFormsModule,
    MkPipesModule,
    TranslocoModule
  ],
  exports: [
    KlippIntrangVerktygComponent
  ]
})
export class KlippIntrangVerktygModule { }
