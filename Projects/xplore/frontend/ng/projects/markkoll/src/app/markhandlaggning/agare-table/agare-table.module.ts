import { NgModule } from "@angular/core";
import { MkAgareTableComponent } from "./agare-table.component";
import { MatIconModule } from "@angular/material/icon";
import { MatTableModule } from "@angular/material/table";
import { XpAccordionTableModule } from "../../../../../lib/ui/accordion-table/accordion-table.module";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { CommonModule } from "@angular/common";
import { MatButtonModule } from "@angular/material/button";
import { TranslocoModule } from "@ngneat/transloco";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MkPipesModule } from "../../common/pipes/pipes.module";

@NgModule({
  declarations: [MkAgareTableComponent],
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatTableModule,
    MatCheckboxModule,
    XpAccordionTableModule,
    TranslocoModule,
    MatTooltipModule,
    MkPipesModule,
  ],
  exports: [MkAgareTableComponent]
})
export class MkAgareTableModule {}
