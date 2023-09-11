import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatChipsModule } from "@angular/material/chips";
import { MatDividerModule } from "@angular/material/divider";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatPaginatorModule } from "@angular/material/paginator";
import { TranslocoModule } from "@ngneat/transloco";
import { MkPipesModule } from "../../common/pipes/pipes.module";
import { MkNoteringarModule } from "../noteringar/noteringar.module";
import { MkFastighetListComponent } from "./fastighet-list.component";

@NgModule({
  declarations: [MkFastighetListComponent],
  imports: [
    CommonModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDividerModule,
    MatExpansionModule,
    MatPaginatorModule,
    MkNoteringarModule,
    MkPipesModule,
    TranslocoModule,
  ],
  exports: [MkFastighetListComponent]
})
export class MkFastighetListModule { }
