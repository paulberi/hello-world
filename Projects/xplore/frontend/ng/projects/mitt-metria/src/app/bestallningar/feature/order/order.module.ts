import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';
import { TranslocoModule } from '@ngneat/transloco';
import { MMOrderRoutingModule } from './order-routing.module';
import { OrderComponent } from './order.component';

@NgModule({
    declarations: [OrderComponent],
    imports: [
        CommonModule,
        TranslocoModule,
        MatTabsModule,
        MMOrderRoutingModule
    ],
    exports: [OrderComponent]
})
export class MMOrderModule { }