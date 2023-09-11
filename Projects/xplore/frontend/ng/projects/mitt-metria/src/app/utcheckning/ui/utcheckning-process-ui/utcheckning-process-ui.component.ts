import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

interface PaymentMethod {
  code: string,
  name: string
}

interface Customer {
  firstName: string,
  lastName: string,
  emailAddress: string;
}

@Component({
  selector: 'mm-utcheckning-process-ui',
  templateUrl: './utcheckning-process-ui.component.html',
  styleUrls: ['./utcheckning-process-ui.component.scss']
})
export class UtcheckningProcessUiComponent {
  @Input() customer: Customer;
  @Input() termsLink: string;
  @Input() price: string;
  @Input() vat: string;
  @Input() currency: string;
  @Input() paymentMethods: PaymentMethod[];
  @Input() invoiceRef: string;
  @Input() paymentError: string;
  @Input() pending: boolean;
  @Output() pay = new EventEmitter<{ selectedPayment: string, invoiceRef: string }>();

  termsChecked: boolean = false;
  selectedPayment: string = "";

  constructor() { }

}
