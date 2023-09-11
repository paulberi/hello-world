import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ProductFilterComponent } from "./product-filter.component";
import { XpUiSearchFieldModule } from "../../../../../../lib/ui/search-field/search-field.module";
import { XpCategoryFilterModule } from "../../../../../../lib/ui/category-filter/category-filter.module";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { TranslocoModule } from "@ngneat/transloco";


@NgModule({
  declarations: [ProductFilterComponent],
  imports: [
    CommonModule,
    XpCategoryFilterModule,
    XpUiSearchFieldModule,
    FormsModule,
    ReactiveFormsModule,
    TranslocoModule
  ],
  exports: [ProductFilterComponent]
})
export class MMProductFilterModule { }
