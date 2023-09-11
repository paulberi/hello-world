import { MMKonfigurationstypBModule } from '../konfigurationstyp-b/konfigurationstyp-b.module';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatDividerModule } from "@angular/material/divider";
import { TranslocoModule } from "@ngneat/transloco";
import { ProductPageComponent } from "./product-page.component";
import { MMProductInformationModule } from "../../ui/product-information/product-information.module";
import { MMBannerModule } from "../../ui/banner/banner.module";
import { MMBreadcrumbsModule } from "../../ui/breadcrumbs/breadcrumbs.module";
import { MMProductPageRoutingModule } from './product-page-routing.module';
import { MMKonfigurationstypAModule } from '../konfigurationstyp-a/konfigurationstyp-a.module';

@NgModule({
  declarations: [
    ProductPageComponent,
  ],
  imports: [
    CommonModule,
    MatDividerModule,
    MMBannerModule,
    MMBreadcrumbsModule,
    MMProductInformationModule,
    TranslocoModule,
    MatIconModule,
    MatButtonModule,
    MMKonfigurationstypAModule,
    MMKonfigurationstypBModule,
    MMProductPageRoutingModule,
  ],
  exports: [
    ProductPageComponent
  ]
})
export class MMProductPageModule { }
