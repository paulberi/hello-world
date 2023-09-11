import { Component, EventEmitter, Input, Output } from '@angular/core';

export function parseDisplayAttributes(json: string): DisplayAttribute[] {
  try {
      return JSON.parse(json);
  } catch {
      return [];
  }
}

export interface OrderLine {
  orderLineId: string;
  attributes: DisplayAttribute[];
  currency: string;
  linePrice: number | string;
  vat: number | string;
}

export interface DisplayAttribute {
  displayName: string;
  value: string;
}

@Component({
  selector: 'xp-order-line',
  templateUrl: './order-line.component.html',
  styleUrls: ['./order-line.component.scss']
})
export class XpOrderLineComponent {
  @Input() orderLine: OrderLine;
  @Input() isDeleteButtonVisible: boolean;
  @Output() deleteOrderLine = new EventEmitter<string>();
}