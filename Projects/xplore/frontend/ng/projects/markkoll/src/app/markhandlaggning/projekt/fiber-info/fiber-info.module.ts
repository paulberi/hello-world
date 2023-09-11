import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MkFiberInfoComponent } from "./fiber-info.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { TranslocoModule } from "@ngneat/transloco";
import { ReactiveFormsModule } from "@angular/forms";
import { MatInputModule } from "@angular/material/input";
import { MatSelectModule } from "@angular/material/select";
import { MatCheckboxModule } from "@angular/material/checkbox";

@NgModule({
  declarations: [
    MkFiberInfoComponent
  ],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    MatInputModule,
    ReactiveFormsModule,
    MatCheckboxModule,
    MatSelectModule,
    TranslocoModule,
  ],
  exports: [
    MkFiberInfoComponent
  ]
})
export class MkFiberInfoModule {}