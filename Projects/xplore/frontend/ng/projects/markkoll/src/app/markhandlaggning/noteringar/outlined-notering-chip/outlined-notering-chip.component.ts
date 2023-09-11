import { Component, Input } from '@angular/core';

@Component({
  selector: 'mk-outlined-notering-chip',
  templateUrl: './outlined-notering-chip.component.html',
  styleUrls: ['./outlined-notering-chip.component.scss']
})
export class MkOutlinedNoteringChipComponent {

  @Input() icon: string;
  @Input() color: "primary" | "accent" | "warn" = "primary";

  constructor() { }

  getColorValue() {
    return {
      'chip-color-primary': this.color === "primary",
      'chip-color-accent': this.color === "accent",
      'chip-color-warn': this.color === "warn",
    }
  }
}
