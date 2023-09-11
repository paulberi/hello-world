import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { TranslocoModule } from "@ngneat/transloco";
import { MMBannerModule } from "../../ui/banner/banner.module";
import { ProductsComponent } from './products.component';
import { MMProductFilterModule } from '../../ui/product-filter/product-filter.module';
import { MMProductFilterChipsModule } from '../../ui/product-filter-chips/product-filter-chips.module';
import { MMProductListModule } from '../../ui/product-list/product-list.module';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatListModule } from '@angular/material/list';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MMProductsRoutingModule } from './products-routing.module';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [
    ProductsComponent
  ],
  imports: [
    CommonModule,
    MMProductFilterModule,
    MMBannerModule,
    MMProductFilterChipsModule,
    MMProductListModule,
    MatSidenavModule,
    MatListModule,
    MatToolbarModule,
    MatProgressSpinnerModule,
    MatIconModule,
    TranslocoModule,
    MMProductsRoutingModule,
    MatButtonModule
  ],
  exports: [
    ProductsComponent
  ]
})
export class MMProductsModule { }
