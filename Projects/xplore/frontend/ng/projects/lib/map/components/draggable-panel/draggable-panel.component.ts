import {AfterViewInit, Component, ElementRef, EventEmitter, Input, Output, ViewChild} from "@angular/core";
import {MapService} from "../../../map-core/map.service";

@Component({
  selector: "xp-draggable-panel",
  template: `
    <div class="draggable-panel" [style.height.px]="maxHeight">
      <mat-divider></mat-divider>
      <div class="dragger" #dragHandle>
        <div class="drag-handle"></div>
      </div>
      <mat-divider></mat-divider>
      <div class="content" #dragContent>
        <ng-content></ng-content>
      </div>
    </div>
  `,
  styles: [`
    .draggable-panel {
      min-height: 26px;
      max-height: 100vh;
    }

    .dragger {
      padding-top: 10px;
      height: 16px;
      cursor: pointer;
    }

    .drag-handle {
      margin-left: auto;
      margin-right: auto;
      margin-bottom: 10px;
      width: 10%;
      border: #636363 solid 3px;
      border-radius: 5px;
      background-color: #636363;
    }

    .content {
      box-sizing: border-box;
      padding: 10px;
      max-height: calc(100% - 26px); /* Size of the panel not counting the drag handle */
      overflow: auto;
    }
  `]
})
export class DraggablePanelComponent implements AfterViewInit {
  @Input() maxHeight = 0;
  @Output() stateUpdate = new EventEmitter<boolean>();

  @ViewChild('dragHandle', { static: true }) panel: ElementRef;
  @ViewChild('dragContent', { static: true }) panelContent: ElementRef;

  touchMoveCaught = false;

  boundOnMouseMove = this.onMouseMove.bind(this);
  boundOnTouchMove = this.onTouchMove.bind(this);

  constructor(private mapService: MapService) {
    this.mapService.click$.subscribe(() => {
      this.maxHeight = Math.min(this.maxHeight, this.panelContent.nativeElement.scrollHeight);
    });
  }

  ngAfterViewInit() {
    this.panel.nativeElement.addEventListener('mousedown', (e) => {
      e.preventDefault();
      document.addEventListener('mousemove', this.boundOnMouseMove);
    });

    document.addEventListener('mouseup', (e) => {
      document.removeEventListener('mousemove', this.boundOnMouseMove);
    });

    this.panel.nativeElement.addEventListener('touchstart', (e) => {
      this.touchMoveCaught = false;
      e.preventDefault();
      document.addEventListener('touchmove', this.boundOnTouchMove);
    });

    document.addEventListener('touchend', (e) => {
      if (!this.touchMoveCaught) {
        this.maxHeight = 26;
      }

      document.removeEventListener('touchmove', this.boundOnTouchMove);
    });
  }

  onMouseMove(event) {
    this.maxHeight = Math.min(this.getViewportHeight() - Math.max(event.clientY, 0), this.panelContent.nativeElement.scrollHeight + 26);
  }

  onTouchMove(event) {
    this.touchMoveCaught = true;
    this.maxHeight = Math.min(this.getViewportHeight() - Math.max(event.targetTouches[0].clientY, 0), this.panelContent.nativeElement.scrollHeight + 26);
  }

  expandToHeight(expansionHeight) {
    this.maxHeight = Math.min(this.panelContent.nativeElement.scrollHeight + 26, expansionHeight);
  }

  getViewportHeight() {
    return Math.max(document.documentElement.clientHeight || 0, window.innerHeight || 0);
  }
}
