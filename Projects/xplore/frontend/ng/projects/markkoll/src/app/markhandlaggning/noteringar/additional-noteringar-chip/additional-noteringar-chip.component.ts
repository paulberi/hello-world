import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'mk-additional-noteringar-chip',
  templateUrl: './additional-noteringar-chip.component.html',
  styleUrls: ['./additional-noteringar-chip.component.scss']
})
export class MkAdditionalNoteringarChipComponent {

  @Input() tooltip;

  constructor() { }
}
