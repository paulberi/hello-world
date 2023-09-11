import { BreakpointObserver, BreakpointState } from "@angular/cdk/layout";
import {
  ChangeDetectorRef,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild,
} from "@angular/core";

export interface CarouselItem {
  id: string;
  imageSrc: string;
  imageTitle?: string;
  selected?: boolean;
}

/**
 * En responsiv bildkarusell
 */
@Component({
  selector: "xp-carousel",
  templateUrl: "./carousel.component.html",
  styleUrls: ["./carousel.component.scss"]
})
export class XpCarouselComponent implements OnInit {
  @ViewChild("slider", { static: true }) slider: ElementRef<any>;

  /**
   * Färg på pilarna och cirklarna
   */
  @Input() color: "primary" | "accent" = "primary";

  /**
   * Innehåll i karusellen
   */
  @Input() carouselItems: CarouselItem[];

  /**
   * Om titel ska visas
   */
  @Input() isTitleVisible: boolean = true;

  /**
   * Om titelns positions ska vara under eller på bilden
   */
  @Input() titlePosition: "bottom" | "on" = "bottom";

  /**
   * Border-radius på ramen i px
   */
  @Input() borderRadius: number = 0;

  /**
   * Event för klick på bild
   */
  @Output() imageClick = new EventEmitter();

  sliderWidth;
  imageWidth;
  selectedDot;
  imagesPerSlide;
  numberOfDots;

  constructor(private breakpointObserver: BreakpointObserver, private cdr: ChangeDetectorRef) {
    this.selectedDot = 0;
  }

  ngOnInit() {
    this.sliderWidth = this.slider.nativeElement.offsetWidth;

    this.breakpointObserver
    .observe(["(min-width: 760px) and (max-width: 1439px)"])
    .subscribe((state: BreakpointState) => {
      if (state.matches) {
        this.imagesPerSlide = 4;
        this.numberOfDots = Math.ceil(this.carouselItems.length / this.imagesPerSlide);
      }
    });
    this.breakpointObserver
    .observe(["(min-width: 1440px)"])
    .subscribe((state: BreakpointState) => {
      if (state.matches) {
        this.imagesPerSlide = 6;
        this.numberOfDots = Math.ceil(this.carouselItems.length / this.imagesPerSlide);
      }
    });
    this.breakpointObserver
    .observe(["(max-width: 760px)"])
    .subscribe((state: BreakpointState) => {
      if (state.matches) {
        this.imagesPerSlide = 2;
        this.numberOfDots = Math.ceil(this.carouselItems.length / this.imagesPerSlide);
      }
    });
  }

  sliderResize(event: HTMLElement): void {
    this.sliderWidth = event.offsetWidth;
    this.cdr.detectChanges();
    this.scrollStart();
  }

  imageResize(event: HTMLElement): void {
    this.imageWidth = event.offsetWidth;
    this.cdr.detectChanges();
    this.scrollStart();
  }

  scrollStart() {
  this.slider.nativeElement.scrollTo({
      left: this.slider.nativeElement.scrollLeft - (this.sliderWidth - 15) * 10,
      behavior: "smooth",
    });
    this.selectedDot = 0;
  }

  scrollLeft(scrollValue) {
    this.slider.nativeElement.scrollTo({
      left: this.slider.nativeElement.scrollLeft - scrollValue,
      behavior: "smooth",
    });
  }

  scrollRight(scrollValue) {
    this.slider.nativeElement.scrollTo({
      left: this.slider.nativeElement.scrollLeft + scrollValue,
      behavior: "smooth",
    });
  }

  onClickLeft() {
    this.scrollLeft(this.sliderWidth);
    this.selectedDot--;
  }

  onClickRight() {
    this.scrollRight(this.sliderWidth);
    this.selectedDot++;
  }

  onClickDots(i) {
    if (i < this.selectedDot) {
      this.scrollLeft((this.sliderWidth) * (this.selectedDot - i));
    } else if (i > this.selectedDot) {
      this.scrollRight((this.sliderWidth) * (i - this.selectedDot));
    }
    this.selectedDot = i;
  }
}
