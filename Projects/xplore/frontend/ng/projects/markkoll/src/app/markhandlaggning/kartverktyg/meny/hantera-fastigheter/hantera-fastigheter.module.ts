import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MkHanteraFastigheterComponent } from './hantera-fastigheter.component';
import { MatButtonModule } from '@angular/material/button';
import { TranslocoModule } from '@ngneat/transloco';
import { MatDialogModule } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';

@NgModule({
  declarations: [
    MkHanteraFastigheterComponent
  ],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    MatButtonModule,
    TranslocoModule,
    MatDialogModule,
    MatIconModule,
    MatDividerModule
  ],
  exports: [
    MkHanteraFastigheterComponent
  ]
})
export class MkHanteraFastigheterModule { }
