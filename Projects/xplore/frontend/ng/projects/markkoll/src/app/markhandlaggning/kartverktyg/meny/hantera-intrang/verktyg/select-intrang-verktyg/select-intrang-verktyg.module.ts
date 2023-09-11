import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SelectIntrangVerktygComponent } from './select-intrang-verktyg.component';
import { MkIntrangFormModule } from "../intrang-form/intrang-form.module";
import { TranslocoModule } from "@ngneat/transloco";



@NgModule({
  declarations: [
    SelectIntrangVerktygComponent,
  ],
  imports: [
    CommonModule,
    MkIntrangFormModule,
    TranslocoModule
  ],
  exports: [
    SelectIntrangVerktygComponent
  ]
})
export class SelectIntrangVerktygModule { }
