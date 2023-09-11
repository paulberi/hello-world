import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatChipsModule } from "@angular/material/chips";
import { MatIconModule } from "@angular/material/icon";
import { MkNoteringarComponent } from "./noteringar.component";
import { MkOutlinedNoteringChipComponent } from './outlined-notering-chip/outlined-notering-chip.component';
import { MkFilledNoteringChipComponent } from './filled-notering-chip/filled-notering-chip.component';
import { TranslocoModule } from "@ngneat/transloco";
import { MkPipesModule } from "../../common/pipes/pipes.module";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MkAdditionalNoteringarChipComponent } from './additional-noteringar-chip/additional-noteringar-chip.component';

@NgModule({
  declarations: [
    MkNoteringarComponent,
    MkOutlinedNoteringChipComponent,
    MkFilledNoteringChipComponent,
    MkAdditionalNoteringarChipComponent
  ],
  imports: [
    CommonModule,
    MatChipsModule,
    MatIconModule,
    MatTooltipModule,
    TranslocoModule,
    MkPipesModule
  ],
  exports: [
    MkNoteringarComponent
  ]
})
export class MkNoteringarModule { }
