import { EventEmitter, Input, Output } from '@angular/core';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'mm-utcheckning-wrapper',
  templateUrl: './utcheckning-wrapper.component.html',
  styleUrls: ['./utcheckning-wrapper.component.scss']
})
export class UtcheckningWrapperComponent {
  @Input() goBackLabel: string = "Fortsätt handla";
  @Output() back = new EventEmitter<void>();

  constructor() { }

}
