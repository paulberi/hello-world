import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SammanfogaIntrangVerktygComponent } from './sammanfoga-intrang-verktyg.component';
import { MkIntrangFormModule } from "../intrang-form/intrang-form.module";
import { TranslocoModule } from "@ngneat/transloco";



@NgModule({
  declarations: [
    SammanfogaIntrangVerktygComponent
  ],
  imports: [
    CommonModule,
    MkIntrangFormModule,
    TranslocoModule
  ],
  exports: [
    SammanfogaIntrangVerktygComponent
  ]
})
export class SammanfogaIntrangVerktygModule { }
