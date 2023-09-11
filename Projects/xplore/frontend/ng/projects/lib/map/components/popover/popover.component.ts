import {Component, Input, OnInit, ViewChild} from "@angular/core";
import {ConnectionPositionPair} from "@angular/cdk/overlay";
import {CdkOverlayOrigin} from "@angular/cdk/overlay";

/**
 * Popover komponent med titel, beskrivning och tillskrivning/erkännande
 */
@Component({
  selector: "xp-popover",
  template: `
    <ng-template
      cdkConnectedOverlay
      [cdkConnectedOverlayOrigin]="trigger"
      [cdkConnectedOverlayOpen]="open"
      [cdkConnectedOverlayPositions]="positions"
      [cdkConnectedOverlayPush]="false"
    >
      <div class="layer-panel-description mat-elevation-z1">
        <h3>{{title}}</h3>
        {{description}}
        <div class="attribution">{{attribution}}</div>
      </div>
    </ng-template>
  `,
  styles: [`
    .layer-panel-description {
      padding: 5px;
      width: 300px;
      border-radius: 2px;
    }
    .layer-panel-description h3 {
      margin-bottom: 2px;
    }
    .layer-panel-description .attribution {
      margin-top: 2px;
      font-style: italic;
    }
  `]
})
export class PopoverComponent implements OnInit {
  @Input() title: String;
  @Input() description: String;
  @Input() attribution: String;

  @Input() horizontalAlign: "before"|"after";

  @Input() trigger: CdkOverlayOrigin;
  public open = false;

  @ViewChild("popover", { static: true }) popover;

  private positionsBefore = [
    new ConnectionPositionPair({ originX: "start", originY: "center" }, { overlayX: "end", overlayY: "center" }),
    new ConnectionPositionPair({ originX: "start", originY: "center" }, { overlayX: "end", overlayY: "top" }),
    new ConnectionPositionPair({ originX: "start", originY: "center" }, { overlayX: "end", overlayY: "bottom" }),
  ];

  private positionsAfter = [
    new ConnectionPositionPair({ originX: "end", originY: "center" }, { overlayX: "start", overlayY: "center" }),
    new ConnectionPositionPair({ originX: "end", originY: "center" }, { overlayX: "start", overlayY: "top" }),
    new ConnectionPositionPair({ originX: "end", originY: "center" }, { overlayX: "start", overlayY: "bottom" }),
  ];

  positions = this.positionsBefore;

  constructor() {
  }

  ngOnInit(): void {
    switch (this.horizontalAlign) {
      case "before":
        this.positions = this.positionsBefore;
        break;

      case "after":
        this.positions = this.positionsAfter;
        break;
    }
  }
}
