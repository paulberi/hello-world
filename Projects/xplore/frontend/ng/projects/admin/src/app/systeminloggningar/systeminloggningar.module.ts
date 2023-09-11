import { NgModule } from "@angular/core";
import { XpAccordionTableModule } from "../../../../lib/ui/accordion-table/accordion-table.module"
import { AdmSysteminloggningarComponent } from "./systeminloggningar.component";
import { MatIconModule } from "@angular/material/icon";
import { MatCheckboxDefaultOptions, MatCheckboxModule, MAT_CHECKBOX_DEFAULT_OPTIONS } from "@angular/material/checkbox";
import { CommonModule } from "@angular/common";
import { MatTableModule } from "@angular/material/table";
import { TranslocoModule } from "@ngneat/transloco";
import { AdmSysteminloggningarContainer } from "./systeminloggningar.container";
import { MatButtonModule } from "@angular/material/button";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { AdmFastighetsokAuthFormModule } from "../fastighetsok-auth-form/fastighetsok-auth-form.module";
import { AdmMetriaMapsAuthFormModule } from "../metria-maps-auth-form/metria-maps-auth-form.module";
import { MatDialogModule } from "@angular/material/dialog";

@NgModule({
  declarations: [
    AdmSysteminloggningarComponent,
    AdmSysteminloggningarContainer
  ],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    XpAccordionTableModule,
    MatIconModule,
    MatButtonModule,
    MatTableModule,
    TranslocoModule,
    AdmMetriaMapsAuthFormModule,
    AdmFastighetsokAuthFormModule,
    MatDialogModule
  ],
  exports: [
    AdmSysteminloggningarComponent,
    AdmSysteminloggningarContainer
  ],
})
export class AdmSysteminloggningarModule {}
