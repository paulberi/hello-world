import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductInformationComponent } from './product-information.component';
import { MMHeaderModule } from '../header/header.module';
import { MMDescriptionModule } from '../description/description.module';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule } from '@ngneat/transloco';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [ProductInformationComponent],
  imports: [
    CommonModule,
    MMHeaderModule,
    MMDescriptionModule,
    MatButtonModule,
    MatIconModule,
    TranslocoModule
  ],
  exports: [ProductInformationComponent]
})
export class MMProductInformationModule { }
