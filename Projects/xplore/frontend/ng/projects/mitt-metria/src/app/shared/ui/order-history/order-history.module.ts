import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslocoModule } from '@ngneat/transloco';
import { OrderHistoryComponent } from './order-history.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { XpOrderLineModule } from '../../../../../../lib/ui/order-line/order-line.module';


@NgModule({
    declarations: [OrderHistoryComponent],
    imports: [
        CommonModule,
        TranslocoModule,
        MatExpansionModule,
        MatChipsModule,
        MatIconModule,
        MatProgressSpinnerModule,
        XpOrderLineModule,
        MatButtonModule
    ],
    exports: [OrderHistoryComponent]
})
export class MMOrderHistoryModule { }
