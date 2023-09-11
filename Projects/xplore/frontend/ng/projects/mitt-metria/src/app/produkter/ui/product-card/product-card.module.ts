import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TranslocoModule } from "@ngneat/transloco";
import { MatCardModule } from "@angular/material/card";
import { ProductCardComponent } from "./product-card.component";
import { MMDescriptionModule } from "../description/description.module";


@NgModule({
  declarations: [ProductCardComponent],
  imports: [
    CommonModule,
    TranslocoModule,
    MatCardModule,
    MMDescriptionModule
  ],
  exports: [ProductCardComponent]
})
export class MMProductCardModule { }
