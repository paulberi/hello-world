import { NgModule } from "@angular/core";
import { XpDropzoneModule } from "../../../../../lib/ui/dropzone/dropzone.module";
import { MatTableModule } from "@angular/material/table";
import { CommonModule } from "@angular/common";
import { MatIconModule } from "@angular/material/icon";
import { MkBilagorComponent } from "./bilagor.component";
import { MatSelectModule } from "@angular/material/select";
import { MatFormFieldModule } from "@angular/material/form-field";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatButtonModule } from "@angular/material/button";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MkBilagorContainer } from "./bilagor.container";
import { MatSortModule } from "@angular/material/sort";
import { TranslocoModule } from "@ngneat/transloco";
import { MkPipesModule } from "../../common/pipes/pipes.module";

@NgModule({
  declarations: [
    MkBilagorComponent,
    MkBilagorContainer
  ],
  imports: [
    BrowserAnimationsModule,
    CommonModule,
    MatTableModule,
    XpDropzoneModule,
    MatFormFieldModule,
    MatIconModule,
    MatSelectModule,
    MatButtonModule,
    FormsModule,
    ReactiveFormsModule,
    MatSortModule,
    TranslocoModule,
    MkPipesModule
  ],
  exports: [
    MkBilagorComponent,
    MkBilagorContainer
  ]
})
export class MkBilagorModule {}
