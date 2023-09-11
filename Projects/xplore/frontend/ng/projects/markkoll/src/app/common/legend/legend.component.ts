import { Component, Input } from '@angular/core';
import { Legend } from "./legend";

@Component({
  selector: 'mk-legend',
  templateUrl: './legend.component.html',
  styleUrls: ['./legend.component.scss']
})
export class MkLegendComponent {
  @Input() legend: Legend;
  @Input() displayGroups: boolean;

  constructor() {
  }

}
