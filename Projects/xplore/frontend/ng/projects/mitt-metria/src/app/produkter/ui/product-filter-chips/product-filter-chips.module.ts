import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ProductFilterChipsComponent } from "./product-filter-chips.component";
import { MMChipListModule } from "../chip-list/chip-list.module";


@NgModule({
  declarations: [ProductFilterChipsComponent],
  imports: [
    CommonModule,
    MMChipListModule
  ],
  exports: [ProductFilterChipsComponent]
})
export class MMProductFilterChipsModule { }
