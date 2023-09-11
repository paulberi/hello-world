import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { UpdateOrderItemsResult } from "../../../../generated/graphql/shop/shop-api-types";
import { XpNotificationService } from "../../ui/notification/notification.service";
import { parseDisplayAttributes } from "../../ui/order-line/order-line.component";
import { calculatePrice, translateCurrencyCode } from "../price-utils";
import { ActiveOrderDocument, ActiveOrderGQL, AdjustOrderLineGQL } from "./cart.shop.generated";
import { Cart } from "./cart/cart.component";

/**
* För att refetcha dessa queries behövs activeOrderDocument (hämtas från den genererade filen)
* skickas in i mutationen som lägger till varor i ordern.
* Exempel på hur man refetchar finns i funktionen deleteItemFromActiveOrder i denna service
*/

@Injectable({
    providedIn: "root"
})
export class CartService {

    constructor(
        private activeOrderGQL: ActiveOrderGQL,
        private adjustOrderLine: AdjustOrderLineGQL,
        private notificationService: XpNotificationService) { }

    /**
    * Hämtar kundens aktiva order.
    */
    public getActiveOrder(): Observable<Cart> {
        return this.activeOrderGQL.watch().valueChanges.pipe(
            map(({ data }) => {
                const activeOrder = data.activeOrder;
                const cartItems = data.activeOrder?.lines.map(line => {
                    return {
                        orderLineId: line.id,
                        attributes: parseDisplayAttributes(line?.customFields?.clientAttributes),
                        linePrice: calculatePrice(line?.unitPriceWithTax),
                        vat: calculatePrice((line?.unitPriceWithTax - line?.unitPrice)),
                        currency: translateCurrencyCode(activeOrder?.currencyCode)
                    }
                })
                return {
                    cartItems: cartItems || [],
                    totalPrice: calculatePrice(activeOrder?.totalWithTax),
                    vat: calculatePrice((activeOrder?.totalWithTax - activeOrder?.total)),
                    currency: translateCurrencyCode(activeOrder?.currencyCode)
                }
            })
        )
    }

    /**
    * Tar bort en produkt i varukorgen. OrderLineId skickas in som parameter.
    */
    public deleteItemFromActiveOrder(id: string) {
        this.adjustOrderLine.mutate({ orderLineId: id }, { refetchQueries: [ActiveOrderDocument] }).subscribe(result => {
            switch (result.data?.adjustOrderLine?.__typename as UpdateOrderItemsResult["__typename"]) {
                case "Order":
                    break;
                case "OrderModificationError":
                    this.notificationService.error("Du har ett pågående köp, vänligen avbryt det för att kunna ändra i din order");
            }
        }), error => {
            console.error(error);
            this.notificationService.error("Gick inte att ta bort produkt, prova igen om en stund");
        };
    }
}