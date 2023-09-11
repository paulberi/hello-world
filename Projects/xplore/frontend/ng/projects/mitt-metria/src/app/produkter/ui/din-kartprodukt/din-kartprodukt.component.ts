import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DisplayAttribute } from '../../../../../../lib/ui/order-line/order-line.component';

@Component({
  selector: 'mm-din-kartprodukt',
  templateUrl: './din-kartprodukt.component.html',
  styleUrls: ['./din-kartprodukt.component.scss']
})
export class DinKartproduktComponent {
  @Input() header: string;
  @Input() displayAttributes: DisplayAttribute[];
  @Input() price: string;
  @Input() currency: string;
  @Input() disableButton: boolean;
  @Output() addToCart = new EventEmitter<void>();
}
