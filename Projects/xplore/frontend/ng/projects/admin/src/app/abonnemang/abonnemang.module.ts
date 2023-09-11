import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { AdmAbonnemangComponent } from "./abonnemang.component";
import { MatFormFieldModule } from "@angular/material/form-field";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatSelectModule } from "@angular/material/select";
import { MatButtonModule } from "@angular/material/button";
import { XpAccordionTableModule } from "../../../../lib/ui/accordion-table/accordion-table.module";
import { MatIconModule } from "@angular/material/icon";
import { MatTableModule } from "@angular/material/table";
import { AdmAbonnemangContainer } from "./abonnemang.container";
import { TranslocoModule } from "@ngneat/transloco";

@NgModule({
  declarations: [
    AdmAbonnemangComponent,
    AdmAbonnemangContainer
  ],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    MatSelectModule,
    MatButtonModule,
    XpAccordionTableModule,
    MatIconModule,
    MatTableModule,
    FormsModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    TranslocoModule,
  ],
  exports: [
    AdmAbonnemangComponent,
    AdmAbonnemangContainer
  ]
})
export class AdmAbonnemangModule {}
