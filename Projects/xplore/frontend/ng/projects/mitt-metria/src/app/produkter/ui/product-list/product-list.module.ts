import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TranslocoModule } from "@ngneat/transloco";
import { ProductListComponent } from "./product-list.component";
import { MMProductCardModule } from "../product-card/product-card.module";
import { MatButtonModule } from "@angular/material/button";


@NgModule({
  declarations: [ProductListComponent],
  imports: [
    CommonModule,
    TranslocoModule,
    MMProductCardModule,
    MatButtonModule
  ],
  exports: [ProductListComponent]
})
export class MMProductListModule { }
