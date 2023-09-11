import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MkAvtalFilterComponent } from "./avtal-filter.component";
import { TranslocoModule } from "@ngneat/transloco";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatOptionModule } from "@angular/material/core";
import { MatSelectModule } from "@angular/material/select";
import { XpUiSearchFieldModule } from "../../../../../../lib/ui/search-field/search-field.module";

@NgModule({
  declarations: [
    MkAvtalFilterComponent
  ],
  imports: [
    CommonModule,
    TranslocoModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatOptionModule,
    MatSelectModule,
    XpUiSearchFieldModule
  ],
  exports: [
    MkAvtalFilterComponent
  ]
})
export class MkAvtalFilterModule { }
