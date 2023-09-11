import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { TranslocoModule } from "@ngneat/transloco";
import { XpRecommendationComponent } from "./recommendation.component";

@NgModule({
  declarations: [
    XpRecommendationComponent,
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    TranslocoModule,
  ],
  exports: [
    XpRecommendationComponent,
  ]
})
export class XpRecommendationModule { }
