import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule } from '@ngneat/transloco';
import { XpOrderLineComponent } from './order-line.component';

@NgModule({
  declarations: [XpOrderLineComponent],
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    TranslocoModule
  ],
  exports: [XpOrderLineComponent]
})
export class XpOrderLineModule { }
