// https://www.htmlgoodies.com/javascript/element-resizing-angular/

import {
  trigger,
  state,
  style,
  transition,
  animate,
} from "@angular/animations";
import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild,
} from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { distinctUntilChanged } from "rxjs/operators";

@Component({
  selector: "xp-expandable-panel",
  templateUrl: "./expandable-panel.component.html",
  styleUrls: ["./expandable-panel.component.scss"],
  animations: [
    trigger("slideUpDown", [
      state("none", style({})),
      state("closed", style({ height: "0px" })),
      state("open", style({ height: "{{ expandHeight }}px" }), {
        params: { expandHeight: "100" },
      }),
      transition("* => closed", animate("400ms ease-in-out")),
      transition("* => open", animate("400ms ease-in-out"))
    ]),
  ],
})
export class XpExpandablePanelComponent implements OnInit, AfterViewInit {
  @ViewChild("dragHandle") dragHandle: ElementRef;
  @ViewChild("panelElement") panelElement: ElementRef;

  @Output() isPanelOpenChange = new EventEmitter<boolean>();

  /**
   * Position på taben
   **/
   @Input() tabPosition: "right" | "left" = "left";

  /**
   * Titel på taben
   **/
  @Input() tabTitle: string;

  /**
   * Icon på taben
   **/
  @Input() tabIcon = "travel_explore";

   /**
   * Hur högt panel ska expandera vid klick på tab
   **/
  @Input() initialHeight: number = 100;

   /**
   * Marginal för hur långt upp panelen kan dras.
   **/
  @Input() viewportTopMargin: number = 0;


  isPanelOpen$ = new BehaviorSubject<boolean>(false);

  panelAnimation: string;
  savedMaxHeight: number = 0;
  boundOnMouseMove = this.onMouseMove.bind(this);
  boundOnTouchMove = this.onTouchMove.bind(this);
  panelHeight: number = 0;

  panelHasMoved = false;
  deltaY: number = 0;
  panelDownY: number;

  constructor() {}

  ngOnInit() {
    this.panelAnimation = "none";
    this.isPanelOpen$.pipe(
      distinctUntilChanged()  
    )
    .subscribe(value => this.isPanelOpenChange.emit(value));
  }

  ngAfterViewInit() {

    this.dragHandle.nativeElement.addEventListener("mousedown", (e: any) => {
      e.stopPropagation();
      this.onPanelDown(e.clientY);
      document.addEventListener("mousemove", this.boundOnMouseMove);
    });

    document.addEventListener("mouseup", (e: any) => {
      e.stopPropagation();
      document.removeEventListener("mousemove", this.boundOnMouseMove);
    });

    this.dragHandle.nativeElement.addEventListener("touchstart", (e: any) => {
      e.stopPropagation();
      this.onPanelDown(e.targetTouches[0].clientY);
      document.addEventListener("touchmove", this.boundOnTouchMove);
    });

    document.addEventListener("touchend", (e: any) => {
      e.stopPropagation();
      document.removeEventListener("touchmove", this.boundOnTouchMove);
    });

    window.addEventListener("resize", (e: any) => {
      this.setPanelHeight(this.panelHeight);
    });
  }

  animEnd(event: any) {
    if(this.panelAnimation == "open") {
      this.panelHeight = this.initialHeight;
    }
    else if(this.panelAnimation == "closed") {
      this.panelHeight = 0;
      this.isPanelOpen$.next(false);
    }
    this.panelAnimation = "none";
  }

  onMouseMove(event: any) {
    this.onPanelMove(event.clientY);
  }

  onTouchMove(event: any) {
    this.onPanelMove(event.targetTouches[0].clientY);
  }

  private onPanelDown(yPos: number) {
    this.panelHasMoved = false;
    this.panelDownY = yPos;
    let curY = this.getViewportHeight() - this.panelHeight;
    this.deltaY = curY - yPos;
  }

  private onPanelMove(yPos: number) {
    if(!this.panelHasMoved && Math.abs(this.panelDownY - yPos) <= 5) {
      return;
    }
    this.panelHasMoved = true;
    this.setPanelHeight(this.calcWantedPanelHeightFromYPos(yPos + this.deltaY));
    if(this.panelHeight <= 10) {
      this.panelHeight = 0;
    }
    this.isPanelOpen$.next(this.panelHeight > 0);
  }

  private calcWantedPanelHeightFromYPos(yPos: number): number {
    return this.getViewportHeight() - yPos;
  }

  private setPanelHeight(height: number) {
    const tabHeight = 60;
    let maxHeight = this.getViewportHeight() - (this.viewportTopMargin + tabHeight);
    height = Math.max(height, 0);
    height = Math.min(maxHeight, height);
    this.panelHeight = height;
  }

  getViewportHeight() {
    return this.panelElement.nativeElement.offsetParent.offsetParent.clientHeight;
  }

  togglePanel() {
    if(this.panelHasMoved) {
      return;
    }
    if(!this.isPanelOpen$.getValue()) {
      this.panelAnimation = "open";
      this.isPanelOpen$.next(true);
    } 
    else {
      this.panelAnimation = "closed";
    }
  }

}
