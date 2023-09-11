import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { TranslocoModule } from "@ngneat/transloco";
import { AdmKunduppgifterComponent } from "./kunduppgifter.component";

@NgModule({
  declarations: [AdmKunduppgifterComponent],
  imports: [
    BrowserAnimationsModule,
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    TranslocoModule
  ],
  exports: [AdmKunduppgifterComponent]
})
export class AdmKunduppgifterModule { }
