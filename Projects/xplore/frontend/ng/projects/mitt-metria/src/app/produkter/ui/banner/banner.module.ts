import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { TranslocoModule } from "@ngneat/transloco";
import { BannerComponent } from "./banner.component";

@NgModule({
  declarations: [
    BannerComponent
  ],
  imports: [
    CommonModule,
    TranslocoModule
  ],
  exports: [
    BannerComponent
  ]
})
export class MMBannerModule { }
