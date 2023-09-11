import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'xp-draggable-panel',
  templateUrl: './draggable-panel.component.html',
  styleUrls: ['./draggable-panel.component.scss']
})
export class XpDraggablePanelComponent {
  @Output() panelClose = new EventEmitter();

  @Input() title: string;
  @Input() boundary: string;
  @Input() width: string = "266px";
  constructor() { }

}
