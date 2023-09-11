import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { BackgroundButtonsPanelComponent } from "./background-buttons-panel.component";
import { BackgroundButtonComponent } from "./background-button.component";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatButtonModule } from "@angular/material/button";



@NgModule({
  declarations: [
    BackgroundButtonsPanelComponent,
    BackgroundButtonComponent,
  ],
  imports: [
    CommonModule,
    MatTooltipModule,
    MatButtonModule
  ],
  exports: [
    BackgroundButtonsPanelComponent,
  ]
})
export class BackgroundButtonsPanelModule { }
