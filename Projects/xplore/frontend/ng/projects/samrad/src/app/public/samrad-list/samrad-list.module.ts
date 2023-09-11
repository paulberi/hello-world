import { NgModule } from "@angular/core";
import { TranslocoModule } from "@ngneat/transloco";
import { CommonModule } from "@angular/common";
import { SamradListComponent } from "./samrad-list.component";
import { MatListModule } from "@angular/material/list";
import { ScrollingModule } from "@angular/cdk/scrolling";
import { SrProjektkartaModule } from "../projektkarta/projektkarta.module";

@NgModule({
  declarations: [SamradListComponent],
  imports: [
    TranslocoModule,
    CommonModule,
    MatListModule,
    ScrollingModule,
    SrProjektkartaModule
  ],
  exports: [SamradListComponent],
})
export class SamradListModule {}
