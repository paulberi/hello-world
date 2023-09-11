import { NgModule } from "@angular/core";
import { XpAccordionTableComponent } from "./accordion-table.component";
import { MatTableModule } from "@angular/material/table";
import { CommonModule } from "@angular/common";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

@NgModule({
  declarations: [XpAccordionTableComponent],
  imports: [
    CommonModule,
    MatTableModule,
    BrowserAnimationsModule
  ],
  exports: [XpAccordionTableComponent]
})
export class XpAccordionTableModule { }
