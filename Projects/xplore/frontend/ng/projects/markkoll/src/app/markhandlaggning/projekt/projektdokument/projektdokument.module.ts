import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MkProjektdokumentComponent } from "./projektdokument.component";
import {MatStepperModule} from "@angular/material/stepper";
import { MatInputModule } from "@angular/material/input";
import { MatFormFieldModule } from "@angular/material/form-field";
import { ReactiveFormsModule } from "@angular/forms";
import { TranslocoModule } from "@ngneat/transloco";
import { MatOptionModule } from "@angular/material/core";
import { MatSelectModule } from "@angular/material/select";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatButtonModule } from "@angular/material/button";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { XpDropzoneModule } from "../../../../../../lib/ui/dropzone/dropzone.module";
import { MatIconModule } from "@angular/material/icon";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MkProjektdokumentContainerComponent } from "./projektdokument.container";
import { MatDividerModule } from "@angular/material/divider";
import { MatRadioModule } from "@angular/material/radio";
import { MatTableModule } from "@angular/material/table";
import { ProjektdokumentTableComponent } from "../projektdokument-table/projektdokument-table.component";

@NgModule({
  declarations: [
    MkProjektdokumentComponent,
    MkProjektdokumentContainerComponent
  ],
  imports: [
    BrowserAnimationsModule,
    CommonModule,
    MatButtonModule,
    MatCheckboxModule,
    MatDividerModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatOptionModule,
    MatTooltipModule,
    MatTableModule,
    MatRadioModule,
    MatSelectModule,
    MatStepperModule,
    ReactiveFormsModule,
    TranslocoModule,
    XpDropzoneModule,
    ProjektdokumentTableComponent
  ],
  exports: [
    MkProjektdokumentComponent,
    MkProjektdokumentContainerComponent
  ]
})
export class MkProjektdokumentModule { }
