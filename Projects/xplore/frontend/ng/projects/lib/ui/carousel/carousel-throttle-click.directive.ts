import {
  Directive,
  EventEmitter,
  HostListener,
  Input,
  Output,
} from "@angular/core";
import { Subject, Subscription } from "rxjs";
import { throttleTime } from "rxjs/operators";

/**
 * Direktiv för att sätta en throttleTime på knappar eller liknande, för att förhindra dubbelklicka
 */
@Directive({
  selector: "[carouselThrottleClick]",
})
export class CarouselThrottleClickDirective {
  @Input() throttleTime = 700;
  @Output() throttleClick = new EventEmitter();
  private clicks = new Subject();
  private subscription: Subscription;

  constructor() {}

  ngOnInit() {
    this.subscription = this.clicks
      .pipe(throttleTime(this.throttleTime))
      .subscribe((e) => this.throttleClick.emit(e));
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  @HostListener("click", ["$event"])
  clickEvent(event) {
    event.preventDefault();
    event.stopPropagation();
    this.clicks.next(event);
  }
}
