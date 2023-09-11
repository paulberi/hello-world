import { DatePipe } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { MatPaginatorIntl } from '@angular/material/paginator';
import * as fileSaver from "file-saver";
import { XpNotificationService } from '../../../../../../lib/ui/notification/notification.service';
import { OrderLine } from '../../../../../../lib/ui/order-line/order-line.component';

export interface OrderHistory {
  order: Order;
  lines: Line[];
}

export interface Order {
  code: string;
  updatedAt: string;
  totalWithTax: number | string;
  currencyCode: string;
  state: string;
  externalRef: string;
  emailAddress: string;
}

export interface Line {
  ready: boolean;
  downloadUrl: string;
  productName: string;
  taxRate: number;
  orderLine: OrderLine
}

@Component({
  selector: 'mm-order-history',
  templateUrl: './order-history.component.html',
  styleUrls: ['./order-history.component.scss']
})
export class OrderHistoryComponent implements OnInit {
  @Input() orders: OrderHistory;
  @Input() isLoading: boolean = true;
  @Input() totalItems: number;
  @Input() labelMatPaginator: string;
  @Input() admin: boolean = false;
  @Input() customer: boolean = false;

  constructor(
    private matPaginatorIntl: MatPaginatorIntl,
    private notificationService: XpNotificationService
  ) { }

  ngOnInit(): void {
    this.translateMatPaginator();
  }

  translateMatPaginator() {
    const rangeLabel = (page: number, pageSize: number, length: number) => {
      if (length == 0 || pageSize == 0) { return `0 av ${length}`; }
      length = Math.max(length, 0);

      const startIndex = page * pageSize;
      const endIndex = startIndex < length ?
        Math.min(startIndex + pageSize, length) :
        startIndex + pageSize;

      return `${startIndex + 1} - ${endIndex} av ${length}`;
    }

    this.matPaginatorIntl.itemsPerPageLabel = this.labelMatPaginator;
    this.matPaginatorIntl.firstPageLabel = "Första sida";
    this.matPaginatorIntl.previousPageLabel = "Föregående sida";
    this.matPaginatorIntl.nextPageLabel = "Nästa sida";
    this.matPaginatorIntl.lastPageLabel = "Sista sida";
    this.matPaginatorIntl.getRangeLabel = rangeLabel;
  }

  setOrderStateCustomer(lines: Line[]): string {
    if (lines.length) {
      return lines.every(line => line.ready === true) ? "Klart" : "Produceras";
    } else {
      return "Okänd status"
    }
  }

  createReceipt(event: Event, orderHistory: OrderHistory) {
    event.stopPropagation();

    const currency = orderHistory.order.currencyCode;
    let vat = 0;
    const pipe = new DatePipe("sv-SE");

    let data = `Kvitto\n\n`;
    data += `Orderdatum\t\t` + pipe.transform(orderHistory.order.updatedAt, "yyyy-mm-dd") + `\n`;
    data += `Order ID\t\t` + orderHistory.order.code + `\n\n`;
    data += `-----------------------------------` + `\n\n`;

    orderHistory.lines.forEach(line => {
      const productVariant = line.productName;
      const price = line.orderLine.linePrice;
      const tax = line.taxRate;
      const productVat = +line.orderLine.vat;
      vat += productVat;
      data += productVariant + `\n\t` + price + ` ` + currency + ` (inkl. moms ` + tax + `%)\n\n`;
    });

    data += `-----------------------------------` + `\n\n`;
    data += `Summa\t\t\t` + orderHistory.order.totalWithTax + ` ` + currency + `\n`;
    data += `Varav moms\t\t` + vat.toFixed(2) + ` ` + currency + `\n`;
    data += `\n-----------------------------------` + `\n\n`;

    data += 'Metria AB\n';
    data += 'Tel. 010-121 81 00\n';
    data += 'Org. nummer: 556799-2242\n';

    const MIME = { type: 'text/text;charset=utf-8' };
    const file = new Blob([data], MIME);
    fileSaver.saveAs(file, "Kvitto_Mitt_Metria_" + orderHistory.order.code + ".txt");
  }

  downloadProduct(downloadUrl: string) {
    if (downloadUrl) {
      window.open(downloadUrl);
    } else {
      this.notificationService.error("Kunde inte hämta fil");
    }
  }
}
