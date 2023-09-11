import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { XpUiSearchFieldComponent } from "./search-field.component";
import { MatButtonModule } from "@angular/material/button";
import { MatInputModule } from "@angular/material/input";
import { FormsModule } from "@angular/forms";

@NgModule({
  declarations: [
    XpUiSearchFieldComponent
  ],
  imports: [
    FormsModule,
    CommonModule,
    MatFormFieldModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
  ],
  exports: [
    XpUiSearchFieldComponent
  ]
})
export class XpUiSearchFieldModule {}
