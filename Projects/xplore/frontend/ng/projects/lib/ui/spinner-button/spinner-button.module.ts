import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { XpSpinnerButtonComponent } from "./spinner-button.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

@NgModule({
  declarations: [
    XpSpinnerButtonComponent
  ],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatButtonModule
  ],
  exports: [
    XpSpinnerButtonComponent
  ]
})
export class XpSpinnerButtonModule {}
