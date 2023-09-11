import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { XpCarouselComponent } from "./carousel.component";
import { CarouselThrottleClickDirective } from "./carousel-throttle-click.directive";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { CarouselResizeDirective } from './carousel-resize.directive';
import { TranslocoModule } from "@ngneat/transloco";



@NgModule({
  declarations: [XpCarouselComponent, CarouselThrottleClickDirective, CarouselResizeDirective],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatIconModule,
    TranslocoModule
  ],
  exports: [XpCarouselComponent, CarouselThrottleClickDirective, CarouselResizeDirective]
})
export class XpCarouselModule { }
