import {Component, EventEmitter, Input, Output} from "@angular/core";

@Component({
  selector: "xp-collapsable-panel",
  template: `
    <xp-collapse-button #collapseButton [icon]="icon" [svgIcon]="svgIcon" [collapsed]="collapsed" (stateUpdate)="onStateUpdate($event)">
    </xp-collapse-button>
    <div *ngIf="!collapseButton.collapsed" class="collapse-panel">
      <ng-content></ng-content>
    </div>
  `,
  styles: [`
    :host {
      display: block;
      padding: 5px 5px 5px 5px;
    }

    .collapse-panel {
      padding: 6px 10px;
      min-height: 50px;
      margin: -5px -5px -5px -5px;
    }
  `]
})
export class CollapsablePanelComponent {
  @Input() icon: string;
  @Input() svgIcon: string;
  @Input() collapsed: boolean;

  @Output() stateUpdate = new EventEmitter<boolean>();

  constructor() {
  }

  onStateUpdate(collapsed: boolean): void {
    this.collapsed = collapsed;
    this.stateUpdate.emit(this.collapsed);
  }
}
