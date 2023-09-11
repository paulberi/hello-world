import { Component, Input } from '@angular/core';

@Component({
  selector: 'mk-filled-notering-chip',
  templateUrl: './filled-notering-chip.component.html',
  styleUrls: ['./filled-notering-chip.component.scss']
})
export class MkFilledNoteringChipComponent {
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
