import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderAttributesComponent } from './order-attributes.component';


@NgModule({
    declarations: [OrderAttributesComponent],
    imports: [
        CommonModule
    ],
    exports: [OrderAttributesComponent]
})
export class MMOrderAttributesModule { }