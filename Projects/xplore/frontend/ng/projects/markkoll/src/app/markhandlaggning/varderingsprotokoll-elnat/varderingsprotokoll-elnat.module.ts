import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MkElnatVarderingsprotokollComponent } from "./varderingsprotokoll-elnat.component";
import { MatButtonToggleModule } from "@angular/material/button-toggle";
import { MatButtonModule } from "@angular/material/button";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatIconModule } from "@angular/material/icon";
import { XpMessageModule } from "../../../../../lib/ui/feedback/message/message.module";
import { TranslocoModule } from "@ngneat/transloco";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatOptionModule } from "@angular/material/core";
import { MatSelectModule } from "@angular/material/select";
import { MkPipesModule } from "../../common/pipes/pipes.module";
import { MkElnatVarderingsprotokollContainer } from "./varderingsprotokoll-elnat.container";
import { MkBilagorModule } from "../bilaga/bilagor.module";


@NgModule({
  declarations: [
    MkElnatVarderingsprotokollComponent,
    MkElnatVarderingsprotokollContainer
  ],
  imports: [
    CommonModule,
    MatButtonToggleModule,
    MatButtonModule,
    ReactiveFormsModule,
    FormsModule,
    MatIconModule,
    XpMessageModule,
    TranslocoModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatInputModule,
    MatOptionModule,
    MatSelectModule,
    MkPipesModule,
    MkBilagorModule
  ],
  exports: [
    MkElnatVarderingsprotokollComponent,
    MkElnatVarderingsprotokollContainer
  ]
})
export class MkElnatVarderingsprotokollModule { }
