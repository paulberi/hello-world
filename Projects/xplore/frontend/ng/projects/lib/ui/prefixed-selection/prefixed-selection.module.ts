import {NgModule} from "@angular/core";
import {XpPrefixedSelectionComponent} from "./prefixed-selection.component";
import {CommonModule} from "@angular/common";
import {TranslocoModule} from "@ngneat/transloco";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatOptionModule} from "@angular/material/core";
import {MatSelectModule} from "@angular/material/select";
import { MatCheckboxModule } from "@angular/material/checkbox";

@NgModule({
  declarations: [
    XpPrefixedSelectionComponent
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
    MatCheckboxModule
  ],
  exports: [
    XpPrefixedSelectionComponent
  ]
})
export class XpPrefixedSelectionModule { }
