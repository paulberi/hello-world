import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { TranslocoModule } from "@ngneat/transloco";
import { DescriptionComponent } from "./description.component";

@NgModule({
  declarations: [
    DescriptionComponent
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    TranslocoModule
  ],
  exports: [
    DescriptionComponent
  ]
})
export class MMDescriptionModule { }
