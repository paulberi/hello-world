import { map } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AddItemToOrderGQL, AddItemToOrderMutationVariables } from './order.shop.generated';
import { UpdateOrderItemsResult } from '../../../../../../../generated/graphql/shop/shop-api-types';
import { ActiveOrderDocument } from '../../../../../../lib/vendure/cart/cart.shop.generated';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private addItemToOrderGQL: AddItemToOrderGQL) { }

  addItemToOrder(order: AddItemToOrderMutationVariables): Observable<UpdateOrderItemsResult> {
    return this.addItemToOrderGQL.mutate(order, { refetchQueries: [ActiveOrderDocument] }).pipe(
      map(result => result.data?.addItemToOrder as UpdateOrderItemsResult));
  }
}

