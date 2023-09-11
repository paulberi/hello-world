import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TranslocoModule } from "@ngneat/transloco";
import { XpCategoryFilterComponent } from "./category-filter.component";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatInputModule } from "@angular/material/input";
import { MatCheckboxModule } from "@angular/material/checkbox";

@NgModule({
  declarations: [XpCategoryFilterComponent],
  imports: [
    CommonModule,
    TranslocoModule,
    MatExpansionModule,
    MatInputModule,
    MatCheckboxModule,
  ],
  exports: [XpCategoryFilterComponent]
})
export class XpCategoryFilterModule { }
