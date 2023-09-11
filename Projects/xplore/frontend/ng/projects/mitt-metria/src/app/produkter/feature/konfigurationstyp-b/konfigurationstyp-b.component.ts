import { hasProperty } from '../../../shared/utils/property-utils';
import { ProductVariant } from '../../../../../../../generated/graphql/shop/shop-api-types';
import { Subscription } from 'rxjs';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Place } from '../../../../../../lib/map/services/sok.service';
import { XpNotificationService } from '../../../../../../lib/ui/notification/notification.service';
import { OrderService } from '../../data-access/order/order.service';
import { ProductVariantsByProductIdGQL, } from '../../data-access/konfigurationstyp-b.shop.generated';
import { AddItemToOrderMutationVariables } from '../../data-access/order/order.shop.generated';
import { DisplayAttribute } from '../../../../../../lib/ui/order-line/order-line.component';
import { translateCurrencyCode } from '../../../../../../lib/vendure/price-utils';

enum DeliveryFormat {
  PDF = "PDF"
}

interface FastighetDisplay {
  produkt: DisplayAttribute,
  fastighet: DisplayAttribute,
  leveransformat: DisplayAttribute
}

@Component({
  selector: 'mm-konfigurationstyp-b',
  templateUrl: './konfigurationstyp-b.component.html',
  styleUrls: ['./konfigurationstyp-b.component.scss'],
})
export class KonfigurationstypBComponent implements OnInit {
  hasError: boolean = false;
  productVariant: ProductVariant;
  fastighetDisplay: FastighetDisplay;
  nyckel: string;

  addToCartSubscription: Subscription;
  productVariantsByProductIdSubscription: Subscription

  translateCurrencyCode = translateCurrencyCode;

  @Input() productId: string;
  @Input() productName: string;
  @Output() cancelOrder = new EventEmitter();

  constructor(
    private orderService: OrderService,
    private notificationService: XpNotificationService,
    private productVariantsByProductIdGQL: ProductVariantsByProductIdGQL,
  ) { }

  ngOnInit(): void {
    this.fastighetDisplay = {
      produkt: { displayName: "Produkt", value: this.productName || "" },
      fastighet: { displayName: "Fastighet", value: "" },
      leveransformat: { displayName: "Leveransformat", value: DeliveryFormat.PDF }
    }
    this.getProductVariant();
  }

  get displayAttributes() {
    return Object.values(this.fastighetDisplay)
  }

  get price() {
    return this.productVariant?.priceWithTax ? (this.productVariant.priceWithTax / 100).toFixed(2) || "NaN" : "NaN";
  }

  getProductVariant(): void {
    this.productVariantsByProductIdSubscription =
      this.productVariantsByProductIdGQL.fetch({ productId: this.productId }).subscribe(({ data }) => {
        if (data?.product?.variants[0]) {
          this.productVariant = data.product?.variants[0] as ProductVariant
          this.isFastighetBasedOnSchema(this.productVariant?.customFields?.attributeSchema || "");
        } else {
          this.hasError = true;
        }
      }, error => {
        console.error(error?.message || error)
        this.hasError = true;
      })
  }

  isFastighetBasedOnSchema(schema: string) {
    if (schema) {
      const parsedSchema = JSON.parse(schema);
      if (hasProperty(parsedSchema, "nyckel")) {
        return true;
      } else {
        this.hasError = true;
        console.warn("JSON schema does not contain key nyckel")
      }
    } else {
      console.warn("JSON schema not found")
      this.hasError = true;
    }
  }

  fastighetSelected(place: Place): void {
    this.fastighetDisplay.fastighet.value = place.name || "";
    this.nyckel = place.feature.properties.objekt_id || "";
  }

  addToCart(): void {
    const attributes: string = JSON.stringify({ nyckel: this.nyckel } || {})
    const clientAttributes = JSON.stringify(this.displayAttributes || []);
    const fastighetOrder: AddItemToOrderMutationVariables = {
      productVariantId: this.productVariant?.id,
      customFields: {
        attributes,
        clientAttributes
      }
    }

    if (!this.hasError) {
      this.addItemToOrder(fastighetOrder)
    }
  }

  addItemToOrder(order: AddItemToOrderMutationVariables) {
    this.addToCartSubscription = this.orderService.addItemToOrder(order).subscribe((res) => {
      switch (res?.__typename) {
        case "Order":
          break;
        case "OrderModificationError":
          this.notificationService.error("Produkten går tyvärr inte att lägga i varukorgen");
          console.error(res?.__typename, ":", res["message"] || "");
          break;
        case "OrderLimitError":
          this.notificationService.error("Produkten finns redan i pågående beställning");
        case "NegativeQuantityError":
        case "InsufficientStockError":
        default:
          console.error(res?.__typename, ":", res["message"] || "");
      }
    })
  }

  clear() {
    this.fastighetDisplay.fastighet.value = "";
    this.nyckel = "";
  }

  ngOnDestroy(): void {
    this.addToCartSubscription?.unsubscribe();
    this.productVariantsByProductIdSubscription?.unsubscribe();
  }
}
