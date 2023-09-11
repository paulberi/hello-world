import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { XpVerticalTabsComponent } from "./vertical-tabs.component";
import { TranslocoModule } from "@ngneat/transloco";
import { MatButtonToggleModule } from "@angular/material/button-toggle";
import { MatIconModule } from "@angular/material/icon";
import { XpVerticalTabComponent } from "./vertical-tab.component";

@NgModule({
  declarations: [
    XpVerticalTabsComponent,
    XpVerticalTabComponent
  ],
  imports: [
    CommonModule,
    TranslocoModule,
    MatButtonToggleModule,
    MatIconModule
  ],
  exports: [
    XpVerticalTabsComponent,
    XpVerticalTabComponent
  ]
})
export class XpVerticalTabsModule { }