import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'mm-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  @Input() header: string;
  @Input() buttonText: string;
  @Input() buttonColor: "primary" | "accent" | "warn" = "accent";
  @Input() isButtonVisible: boolean = true;
  @Output() buttonClick = new EventEmitter<void>();
}
