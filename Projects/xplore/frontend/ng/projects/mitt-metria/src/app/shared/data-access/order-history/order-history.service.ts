import { Injectable } from "@angular/core";
import { OrderHistory as AdminOrderHistory } from "../../../../../../../generated/graphql/admin/admin-api-types";
import { OrderHistory as ShopOrderHistory } from "../../../../../../../generated/graphql/shop/shop-api-types";
import { DisplayAttribute, parseDisplayAttributes } from "../../../../../../lib/ui/order-line/order-line.component";
import { OrderHistory } from "../../ui/order-history/order-history.component";
import { calculatePrice, translateCurrencyCode } from "../../../../../../lib/vendure/price-utils";

@Injectable({
    providedIn: "root"
})
export class OrderHistoryService {

    constructor() { }

    public mapOrderHistory(orderHistoryItems: ShopOrderHistory[] | AdminOrderHistory[]): OrderHistory[] {
        return orderHistoryItems.map(item => {
            return {
                order: {
                    code: item?.order?.code,
                    updatedAt: item?.order?.updatedAt,
                    totalWithTax: calculatePrice(item?.order?.totalWithTax),
                    currencyCode: translateCurrencyCode(item?.order?.currencyCode),
                    state: item?.order?.currencyCode,
                    externalRef: item?.order?.customFields?.externalRef,
                    emailAddress: item?.order?.customFields?.emailAddress,
                },
                lines: item.lines.map(line => {
                    return {
                        ready: line?.ready,
                        downloadUrl: line?.downloadUrl,
                        productName: line?.orderLine?.productVariant?.product?.name,
                        taxRate: line?.orderLine?.taxRate,
                        orderLine: {
                            orderLineId: line?.orderLine?.id,
                            attributes: parseDisplayAttributes(line?.orderLine?.customFields?.clientAttributes),
                            currency: translateCurrencyCode(item?.order?.currencyCode),
                            linePrice: calculatePrice(line?.orderLine?.unitPriceWithTax),
                            vat: calculatePrice((line?.orderLine.unitPriceWithTax - line?.orderLine?.unitPrice)),
                        }
                    }
                })
            }
        })
    }
}