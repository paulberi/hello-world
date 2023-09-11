import {
  Directive,
  ElementRef,
  EventEmitter,
  Inject,
  Input,
  Output,
  Renderer2,
} from "@angular/core";
import type { AfterViewInit } from "@angular/core";

/**
 * This directive allows to hook HTML element resize
 */
@Directive({
  selector: "[carouselResize]",
})
export class CarouselResizeDirective implements AfterViewInit {
  /**
   * An optional CSS query to select an HTML element within the host element
   */
  @Input() public set query(value: string) {
    this._query = value;
    // Hook only if the component has been initialised,
    // i.e. ngAfterViewInit has already passed
    if(this.parent && this.iframe) {
      this.hook();
    }
  }
  public get query(): string {
    return this._query;
  }

  /**
   * This event will be emitted when a resize event occurs
   */
  @Output() public readonly resize = new EventEmitter<HTMLElement>();

  private _query: string;
  private parent: HTMLElement;
  private iframe: HTMLIFrameElement;

  public constructor(
    @Inject(ElementRef) private readonly el: ElementRef<HTMLElement>,
    @Inject(Renderer2) private readonly renderer2: Renderer2
  ) {}

  public ngAfterViewInit(): void {
    this.hook();
    this.iframe.title = "Slider";
  }

  private hook(): void {
    // If we have created an iframe, it has to be deleted first
    if(this.parent && this.iframe) {
      this.renderer2.removeChild(this.parent, this.iframe);
    }
    // Here we need to find the parent
    this.parent = this.query ?
      this.el.nativeElement.querySelector<HTMLElement>(this.query) :
      this.el.nativeElement;
    if(!this.parent) {
      throw new Error("ResizeDirective: parent not found");
    }
    // Create the iframe ...
    this.iframe = this.renderer2.createElement("iframe");
    this.renderer2.setStyle(this.iframe, "position", "absolute");
    this.renderer2.setStyle(this.iframe, "width", "100%");
    this.renderer2.setStyle(this.iframe, "height", "100%");
    this.renderer2.setStyle(this.iframe, "background-color", "transparent");
    this.renderer2.setStyle(this.iframe, "margin", "0");
    this.renderer2.setStyle(this.iframe, "padding", "0");
    this.renderer2.setStyle(this.iframe, "border-width", "0");
    this.renderer2.setStyle(this.iframe, "overflow", "hidden");
    this.renderer2.setStyle(this.iframe, "z-index", "-1");
    this.renderer2.listen(this.iframe, "load", () => {
      // Here we listen to the content window resize event.
      // Since the iframe has width and height 100%, its size will
      // follow the parent size. This allows to detect the parent resize.
      this.renderer2.listen(this.iframe.contentWindow, "resize", () => {
        this.resize.emit(this.parent);
      });
    });
    // ... and add it as a child to the parent
    this.renderer2.appendChild(this.parent, this.iframe);
  }
}
