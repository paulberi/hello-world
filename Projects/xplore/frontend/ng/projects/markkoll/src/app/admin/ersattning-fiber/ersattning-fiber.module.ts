import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MkErsattningFiberComponent } from "./ersattning-fiber.component";
import { MkErsattningFiberContainerComponent } from "./ersattning-fiber.container";
import { XpMessageModule } from "../../../../../lib/ui/feedback/message/message.module";
import { TranslocoModule } from "@ngneat/transloco";
import { MkErsattningRowModule } from "./ersattning-row/ersattning-row.module";
import { MatButtonModule } from "@angular/material/button";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatExpansionModule } from "@angular/material/expansion";
import { MkErsattningFiberFormModule } from "./ersattning-fiber-form/ersattning-fiber-form.module";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatDialogModule } from "@angular/material/dialog";



@NgModule({
  declarations: [
    MkErsattningFiberComponent,
    MkErsattningFiberContainerComponent,
  ],
  imports: [
    CommonModule,
    XpMessageModule,
    MkErsattningRowModule,
    TranslocoModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    FormsModule,
    MatInputModule,
    MatDialogModule,
    MatExpansionModule,
    MkErsattningFiberFormModule
  ],
  exports: [
    MkErsattningFiberComponent,
    MkErsattningFiberContainerComponent,
  ]
})
export class MkErsattningFiberModule { }
