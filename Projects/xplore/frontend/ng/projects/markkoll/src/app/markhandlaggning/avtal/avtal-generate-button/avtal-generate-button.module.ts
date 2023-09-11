import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { TranslocoModule } from "@ngneat/transloco";
import { MkAvtalGenerateButtonComponent } from "./avtal-generate-button.component";
import { MatButtonModule } from "@angular/material/button";

@NgModule({
  declarations: [
    MkAvtalGenerateButtonComponent
  ],
  imports: [
    TranslocoModule,
    CommonModule,
    MatButtonModule
  ],
  exports: [
    MkAvtalGenerateButtonComponent
  ]
})
export class MkAvtalGenerateButtonModule {}
