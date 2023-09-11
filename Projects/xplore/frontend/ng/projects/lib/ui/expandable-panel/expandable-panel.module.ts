import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { XpExpandablePanelComponent } from "./expandable-panel.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatIconModule } from "@angular/material/icon";
import { TranslocoModule } from "@ngneat/transloco";

@NgModule({
  declarations: [
    XpExpandablePanelComponent
  ],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    MatIconModule,
    TranslocoModule
  ],
  exports: [
    XpExpandablePanelComponent
  ]
})
export class XpExpandablePanelModule { }
