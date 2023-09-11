import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { QueryRef } from 'apollo-angular';
import { OrderHistoryService } from '../shared/data-access/order-history/order-history.service';
import { OrderHistory } from '../shared/ui/order-history/order-history.component';
import { ShopOrdersGQL } from './data-access/orderhistorik.shop.generated';

@Component({
  selector: 'mm-orderhistorik',
  templateUrl: './orderhistorik.component.html',
  styleUrls: ['./orderhistorik.component.scss']
})
export class OrderhistorikComponent implements OnInit {
  feedQuery: QueryRef<any>;
  orders: OrderHistory[];
  totalItems: number;
  isLoading: boolean = true;
  startIndex: number = 0;
  numberOfOrders: number = 10;
  hasError: boolean = false;
  errorMsg: string = "Kunde inte hämta orderhistorik";

  constructor(private shopOrdersGQL: ShopOrdersGQL, private orderHistoryService: OrderHistoryService) { }

  ngOnInit(): void {
    this.initFeedQuery();
    this.getOrders();
  }

  initFeedQuery() {
    this.feedQuery = this.shopOrdersGQL.watch({ numberOfOrders: this.numberOfOrders, startIndex: this.startIndex });
  }

  getOrders() {
    this.feedQuery.valueChanges.subscribe(({ data, loading }) => {
      this.isLoading = loading;
      this.hasError = false;
      if (data?.orderHistory.items) {
        this.orders = this.orderHistoryService.mapOrderHistory(data.orderHistory?.items);
        this.totalItems = data?.orderHistory.totalItems;
      }
    }, error => {
      console.error("Could not get orders: ", error?.message)
      this.errorMsg = error?.message ? this.errorMsg.concat(` (${error?.message})`) : this.errorMsg;
      this.isLoading = false;
      this.hasError = true;
    })
  }

  fetchMore() {
    this.feedQuery.fetchMore({
      variables: {
        numberOfOrders: this.numberOfOrders,
        startIndex: this.startIndex
      }
    }).then(({ data, loading }) => {
      this.isLoading = loading;
      if (data?.orderHistory) {
        this.orders = this.orderHistoryService.mapOrderHistory(data.orderHistory?.items);
        this.totalItems = data.orderHistory?.totalItems;
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
