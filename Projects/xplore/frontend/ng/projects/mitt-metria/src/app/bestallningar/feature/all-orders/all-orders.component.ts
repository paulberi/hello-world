import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { QueryRef } from 'apollo-angular';
import { DeliveryOrderLine, Order } from '../../../../../../../generated/graphql/admin/admin-api-types';
import { OrderHistoryService } from '../../../shared/data-access/order-history/order-history.service';
import { OrderHistory } from '../../../shared/ui/order-history/order-history.component';
import { AdminOrdersGQL, GetDeliveryOrderGQL, GetDeliveryOrderLineGQL } from '../../data-access/all-orders.admin.generated';

@Component({
  selector: 'mm-all-orders',
  templateUrl: './all-orders.component.html',
  styleUrls: ['./all-orders.component.scss']
})
export class AllOrdersComponent implements OnInit {
  feedQuery: QueryRef<any>
  orders: OrderHistory[];
  totalItems: number;
  numberOfOrders: number = 10;
  startIndex: number = 0;
  isLoading: boolean = true;
  hasError: boolean = false;
  errorMsg: string = "Kunde inte hämta beställningshistorik";

  constructor(
    private adminOrdersGQL: AdminOrdersGQL,
    private orderHistoryService: OrderHistoryService,
    private getDeliveryOrderGQL: GetDeliveryOrderGQL,
    private getDeliveryOrderLneGQL: GetDeliveryOrderLineGQL
  ) { }

  ngOnInit(): void {
    this.initFeedQuery();
    this.getAllOrders();
  }

  initFeedQuery() {
    this.feedQuery = this.adminOrdersGQL.watch(
      {
        numberOfOrders: this.numberOfOrders,
        startIndex: this.startIndex
      },
      {
        notifyOnNetworkStatusChange: true,
        fetchPolicy: "network-only"
      });
  }

  getAllOrders() {
    this.feedQuery.valueChanges.subscribe(({ data, loading }) => {
      this.isLoading = loading;
      if (data?.orderHistory) {
        this.orders = data.orderHistory;
        this.totalItems = data.orderHistory?.totalItems;
        //await this.setStatusOnOrder(this.orders);
        this.orders = this.orderHistoryService.mapOrderHistory(data.orderHistory?.items);
      }
    }, error => {
      console.error("Could not get orders: ", error?.message)
      this.errorMsg = error?.message ? this.errorMsg.concat(` (${error?.message})`) : this.errorMsg;
      this.isLoading = false;
      this.hasError = true;
    });
  }

  //Kod för att hämta och lägga till statusarna från delivery
  //Koden funkar inte efter byte av query, fixa koden när det finns behov av att se statusar från delivery
/*   async setStatusOnOrder(orders) {
    for (let orderHistory of orders) {
      await new Promise<void>(resolve => {
        this.getDeliveryOrderGQL.watch({ id: orderHistory.order.id }).valueChanges.subscribe(result => {
          let orderStatus: string;
          orderStatus = this.translateStatus(result.data?.getDeliveryOrder[0]?.status);
          let count = 0;
          if (orderHistory.order.state !== "Cancelled") {
            for (let line of orderHistory.lines) {
              this.getDeliveryOrderLneGQL.watch({ id: line.orderLine.id }).valueChanges.subscribe(result => {
                let orderLineStatus: string;
                orderLineStatus = this.translateStatus(result.data?.getDeliveryOrderLine?.status);
                const tempLines = orderHistory.lines.map((line) => ({ ...line, status: orderLineStatus }));
                count++;
                if (count < 2) {
                  this.orders = [...this.orders || [], { ...orderHistory, order: { ...orderHistory.order, ...{ status: orderStatus } }, lines: [...tempLines] }];
                }
              })
            }
          } else {
            this.orders = [...this.orders || [], { ...orderHistory, order: { ...orderHistory.order, ...{ status: orderStatus } } }];
          }
          resolve();
        })
      })
    }
  } */

  translateStatus(status: string): string {
    switch (status) {
      case "READY":
        return "Klart";
      case "PRODUCING":
        return "Produceras";
      case "FAILED":
        return "Misslyckades";
      default:
        return "Okänd status";
    }
  }

  fetchMore() {
    this.feedQuery.fetchMore({
      variables: {
        numberOfOrders: this.numberOfOrders,
        startIndex: this.startIndex
      }
    }).then(({ data, loading }) => {
      if (data?.orderHistory) {
        this.orders = data.orderHistory?.items as OrderHistory[];
        this.totalItems = data.orderHistory?.totalItems;
        this.isLoading = false;
        //await this.setStatusOnOrder(this.orders);
      }
    }, error => {
      console.error("Could not fetch more orders: ", error?.message)
      this.errorMsg = error?.message ? this.errorMsg.concat(` (${error?.message})`) : this.errorMsg;
      this.isLoading = false;
      this.hasError = true;
    });
  }

  pageChange(event: PageEvent) {
    this.startIndex = event.pageIndex * event.pageSize;
    this.numberOfOrders = event.pageSize;
    this.fetchMore();
  }
}
