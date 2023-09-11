import { Component, EventEmitter, Input, Output } from '@angular/core';

export interface Chip {
  id: string;
  title?: string;
  text: string;
}

@Component({
  selector: 'mm-chip-list',
  templateUrl: './chip-list.component.html',
  styleUrls: ['./chip-list.component.scss']
})
export class ChipListComponent {
  @Input() chips: Chip[];
  @Output() removeChip = new EventEmitter<string>();

  constructor() { }

}
