import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule } from '@ngneat/transloco';
import { MMOrderAttributesModule } from '../../../shared/ui/order-attributes/order-attributes.module';
import { CartItemsComponent } from './cart-items.component';

@NgModule({
  declarations: [CartItemsComponent],
  imports: [
    CommonModule,
    MMOrderAttributesModule,
    MatButtonModule,
    MatDividerModule,
    MatIconModule,
    TranslocoModule
  ],
  exports: [CartItemsComponent]
})
export class MMCartItemsModule { }