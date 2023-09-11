import { Component, Input, OnInit } from '@angular/core';
import { Order } from '../../../../../../../generated/graphql/admin/admin-api-types';
import { UpdateOrderItemsResult } from '../../../../../../../generated/graphql/admin/admin-api-types';
import { XpNotificationService } from '../../../../../../lib/ui/notification/notification.service';
import { XpUserService } from '../../../../../../lib/user/user.service';
import { AdminAdjustOrderLineGQL } from '../../data-access/cart-items.admin.generated';
import { AdminOngoingOrderDocument } from '../create-order/create-order.admin.generated';

export enum OrderStates {
  CREATED = 'Created',
  ADDING_ITEMS = 'AddingItems',
  ARRANGING_PAYMENT = 'ArrangingPayment',
  PAYMENT_AUTHORIZED = 'PaymentAuthorized',
  PAYMENT_SETTLED = 'PaymentSettled',
  PARTIALLY_SHIPPED = 'PartiallyShipped',
  SHIPPED = 'Shipped',
  PARTIALLY_DELIVERED = 'PartiallyDelivered',
  DELIVERED = 'Delivered',
  MODIFYING = 'Modifying',
  ARRANGING_ADDITIONAL_PAYMENT = 'ArrangingAdditionalPayment',
  CANCELLED = 'Cancelled'
}
@Component({
  selector: 'mm-cart-items',
  templateUrl: './cart-items.component.html',
  styleUrls: ['./cart-items.component.scss']
})
export class CartItemsComponent implements OnInit {
  @Input() order: Order;
  userFullName: string;

  constructor(
    private notificationService: XpNotificationService,
    private xpUserService: XpUserService,
    private adminAdjustOrderLineGQL: AdminAdjustOrderLineGQL
  ) { }

  ngOnInit(): void {
    this.getUser();
  }

  getUser() {
    this.userFullName = this.xpUserService.getUser().claims.name;
  }

  removeProduct(id: string) {
    this.adminAdjustOrderLineGQL.mutate({
      orderLineId: id
    }, { refetchQueries: [{ query: AdminOngoingOrderDocument, variables: { orderId: this.order.id } }] }).subscribe((res: any) => {
      switch (res.data?.removeOrderLine?.__typename as UpdateOrderItemsResult["__typename"]) {
        case "Order":
          break;
        case "OrderModificationError":
          this.notificationService.error("Du har ett pågående köp, vänligen avbryt det för att kunna ändra i din order");
          console.log(res.data?.removeOrderLine?.__typename, ":", res.data?.removeOrderLine?.message);
      }
    })
  }
}
