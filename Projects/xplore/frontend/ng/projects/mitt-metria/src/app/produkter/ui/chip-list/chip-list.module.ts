import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatChipsModule } from "@angular/material/chips";
import { MatIconModule } from "@angular/material/icon";
import { ChipListComponent } from "./chip-list.component";

@NgModule({
  declarations: [
    ChipListComponent
  ],
  imports: [
    CommonModule,
    MatChipsModule,
    MatIconModule
  ],
  exports: [
    ChipListComponent
  ]
})
export class MMChipListModule { }