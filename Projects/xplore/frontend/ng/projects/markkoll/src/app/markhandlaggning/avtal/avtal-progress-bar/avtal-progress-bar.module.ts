import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatProgressBarModule } from "@angular/material/progress-bar";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MkAvtalProgressBarComponent } from "./avtal-progress-bar.component";

@NgModule({
  declarations: [
    MkAvtalProgressBarComponent,
  ],
  imports: [
    BrowserAnimationsModule,
    MatProgressBarModule,
    CommonModule
  ],
  exports: [
    MkAvtalProgressBarComponent
  ]
})
export class MkAvtalProgressBarModule {}
