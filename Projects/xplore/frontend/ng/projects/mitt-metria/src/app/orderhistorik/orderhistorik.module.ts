import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslocoModule } from '@ngneat/transloco';
import { OrderhistorikComponent } from './orderhistorik.component';
import { MMOrderhistorikRoutingModule } from './orderhistorik-routing.module';
import { MMOrderHistoryModule } from '../shared/ui/order-history/order-history.module';
import { MatPaginatorModule } from '@angular/material/paginator';
import { XpMessageModule } from '../../../../lib/ui/feedback/message/message.module';


@NgModule({
  declarations: [OrderhistorikComponent],
  imports: [
    CommonModule,
    MMOrderHistoryModule,
    MatPaginatorModule,
    XpMessageModule,
    MMOrderhistorikRoutingModule,
    TranslocoModule
  ],
  exports: [OrderhistorikComponent]
})
export class MMOrderhistorikModule { }