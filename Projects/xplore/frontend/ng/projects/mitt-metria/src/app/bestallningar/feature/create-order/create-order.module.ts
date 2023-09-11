import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { TranslocoModule } from '@ngneat/transloco';
import { XpDropzoneModule } from '../../../../../../lib/ui/dropzone/dropzone.module';
import { MMPolygonDropzoneModule } from '../../../shared/ui/polygon-dropzone/polygon-dropzone.module';
import { MMCartItemsModule } from '../cart-items/cart-items.module';
import { MMCreateOrderRoutingModule } from './create-order-routing.module';
import { CreateOrderComponent } from './create-order.component';
import { CustomerFormComponent } from './forms/customer-form/customer-form.component';
import { DropzoneComponent } from './forms/dynamic-form/complex-datatypes/dropzone/dropzone.component';
import { KartbladComponent } from './forms/dynamic-form/complex-datatypes/object/kartblad/kartblad.component';
import { ObjectComponent } from './forms/dynamic-form/complex-datatypes/object/object.component';
import { PolygonComponent } from './forms/dynamic-form/complex-datatypes/object/polygon/polygon.component';
import { RectangleComponent } from './forms/dynamic-form/complex-datatypes/object/rectangle/rectangle.component';
import { DynamicFormComponent } from './forms/dynamic-form/dynamic-form.component';
import { ProductFormComponent } from './forms/product-form/product-form.component';

@NgModule({
  declarations: [
    CreateOrderComponent,
    CustomerFormComponent,
    ProductFormComponent,
    DynamicFormComponent,
    DropzoneComponent,
    ObjectComponent,
    PolygonComponent,
    RectangleComponent,
    KartbladComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatDividerModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatRadioModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MMCartItemsModule,
    MMPolygonDropzoneModule,
    TranslocoModule,
    XpDropzoneModule,
    MMCreateOrderRoutingModule
  ],
  exports: [CreateOrderComponent]
})
export class MMCreateOrderModule { }