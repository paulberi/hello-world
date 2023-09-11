import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MkFiberVarderingsprotokollComponent } from "./varderingsprotokoll-fiber.component";
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
import { MkFiberVarderingsprotokollContainerComponent } from "./varderingsprotokoll-fiber.container";



@NgModule({
  declarations: [
    MkFiberVarderingsprotokollComponent,
    MkFiberVarderingsprotokollContainerComponent
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
    MkPipesModule
  ],
  exports: [
    MkFiberVarderingsprotokollComponent,
    MkFiberVarderingsprotokollContainerComponent
  ]
})
export class MkFiberVarderingsprotokollModule { }
