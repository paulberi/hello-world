import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { TranslocoModule } from '@ngneat/transloco';
import { MatDialogModule } from '@angular/material/dialog';
import { XpCartComponent } from './cart.component';
import { XpOrderLineModule } from '../../../ui/order-line/order-line.module';

@NgModule({
  declarations: [XpCartComponent],
  imports: [
    CommonModule,
    MatButtonModule,
    MatDialogModule,
    MatDividerModule,
    MatIconModule,
    XpOrderLineModule,
    TranslocoModule
  ],
  exports: [XpCartComponent]
})
export class XpCartModule { }