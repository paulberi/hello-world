import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatIconModule } from "@angular/material/icon";
import { MkExpansionPanelComponent } from "./expansion-panel.component";
import { TranslocoModule } from "@ngneat/transloco";

@NgModule({
  declarations: [MkExpansionPanelComponent],
  imports: [
    CommonModule,
    MatExpansionModule,
    MatIconModule,
    TranslocoModule,
  ],
  exports: [MkExpansionPanelComponent]
})
export class MkExpansionPanelModule {}
