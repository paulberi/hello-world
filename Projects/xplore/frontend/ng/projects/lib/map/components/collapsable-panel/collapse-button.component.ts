import {Component, EventEmitter, Input, Output} from "@angular/core";

@Component({
  selector: "xp-collapse-button",
  template: `
    <button *ngIf="collapsed" id="collapse-button" mat-mini-fab (click)="toggle()">
        <mat-icon *ngIf="!svgIcon">{{this.icon}}</mat-icon>
        <mat-icon *ngIf="svgIcon" svgIcon="{{this.svgIcon}}"></mat-icon>
      </button>

      <button *ngIf="!collapsed" id="close-button" mat-icon-button (click)="toggle()">
        <mat-icon id="close-button-icon">remove_circle</mat-icon>
      </button>
  `,
  styles: [`
    #collapse-button {
      position: relative;
      pointer-events: all;
      z-index: 100;
      overflow-y: hidden;
      box-shadow: 0 3px 5px -1px rgba(0,0,0,0.2),0 3px 7px -2px rgba(0,0,0,.14),0 1px 17px -6px rgba(0,0,0,.12);
      padding: 0 5px 0 5px;
    }
    #close-button {
      position: absolute;
      pointer-events: all;
      float: right;
      top: 5px;
      right: 5px;
      z-index: 100;
      height:16px !important;
      width:16px !important;
      font-size:16px !important;
    }
    #close-button-icon {
      position: absolute;
      top: -4px;
      left: -4px;
      min-height: 20px !important;
      min-width: 20px !important;
      font-size: 20px !important;
    }
  `]
})
export class CollapseButtonComponent {
  @Input() icon: string;
  @Input() svgIcon: string;
  @Input() collapsed: boolean;

  @Output() stateUpdate = new EventEmitter<boolean>();

  constructor() {
  }

  toggle(): void {
    this.collapsed = !this.collapsed;
    this.stateUpdate.emit(this.collapsed);
  }
}
