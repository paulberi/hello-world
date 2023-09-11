import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatPaginatorModule } from '@angular/material/paginator';
import { TranslocoModule } from '@ngneat/transloco';
import { XpMessageModule } from '../../../../../../lib/ui/feedback/message/message.module';
import { MMOrderHistoryModule } from '../../../shared/ui/order-history/order-history.module';
import { MMAllOrdersRoutingModule } from './all-orders-routing.module';
import { AllOrdersComponent } from './all-orders.component';

@NgModule({
  declarations: [AllOrdersComponent],
  imports: [
    CommonModule,
    XpMessageModule,
    MMOrderHistoryModule,
    MatPaginatorModule,
    TranslocoModule,
    MMAllOrdersRoutingModule
  ],
  exports: [AllOrdersComponent]
})
export class MMAllOrdersModule { }