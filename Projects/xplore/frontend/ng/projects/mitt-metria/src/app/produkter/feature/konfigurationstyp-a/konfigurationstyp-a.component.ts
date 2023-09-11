import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControlStatus } from '@angular/forms';
import { XpNotificationService } from '../../../../../../lib/ui/notification/notification.service';
import { Property, hasProperty, getEnumFromProperties } from '../../../shared/utils/property-utils';
import { JsonSchemaService } from '../../../shared/utils/json-schema.service';
import { GetAreaPriceForVariantGeojsonGQL, ProductVariantsByProductIdGQL } from '../../data-access/konfigurationstyp-a.shop.generated';
import { OrderService } from '../../data-access/order/order.service';
import { AddItemToOrderMutationVariables } from '../../data-access/order/order.shop.generated';
import { KonfigurationstypAFormControl } from '../../ui/konfigurationstyp-a-ui/konfigurationstyp-a-ui.component';
import { DisplayAttribute } from '../../../../../../lib/ui/order-line/order-line.component';
import { calculatePrice, translateCurrencyCode } from '../../../../../../lib/vendure/price-utils';
import { GeoJson } from '../../../../../../lib/map-core/geojson.util';
import { AttributeFormResult } from '../../utils/konfigurationstyp-a.presenter';
import { Step } from '../../ui/stepper/stepper.component';

export enum ProductVariant {
  POLYGON = "polygon",
  KOMMUN = "kommun",
  RIKSTACKANDE = "rikstackande"
}

export interface KonfigurationsTypAProductVariant {
  id: string;
  key: string;
  name: string;
  schema: Property<string>;
  currency: string;
  price?: number;
}

@Component({
  selector: 'mm-konfigurationstyp-a',
  templateUrl: './konfigurationstyp-a.component.html',
  styleUrls: ['./konfigurationstyp-a.component.scss']
})
export class KonfigurationstypAComponent implements OnInit {
  @Input() productId: string;
  @Input() productName: string;
  @Output() cancelOrder = new EventEmitter();

  isLoading: boolean = true;
  displayAttributes: DisplayAttribute[] = [];
  orderAttributes;
  productVariants: KonfigurationsTypAProductVariant[] = [];
  hasPolygonProperty: boolean = false;
  filteredProductVariants: KonfigurationsTypAProductVariant[] = [];
  selectedProductVariant: KonfigurationsTypAProductVariant;
  propertyOptions: KonfigurationstypAFormControl;
  referenssystem: string[] = [];
  leveransformat: string[] = [];
  konfigurationTypAValid: boolean = false;
  price: string;
  currency: string;

  activeStep: number = 1;
  stepperSteps: Step[] = [
    {
      stepNumber: 1,
      title: "Välj omrade"
    },
    {
      stepNumber: 2,
      title: "Välj referenssystem"
    },
    {
      stepNumber: 3,
      title: "Välj leveransformat"
    }
  ];

  constructor(private productVariantsByProductIdGQL: ProductVariantsByProductIdGQL,
    private getAreaPriceForVariantGeojsonGQL: GetAreaPriceForVariantGeojsonGQL,
    private orderService: OrderService,
    private notificationService: XpNotificationService,
    private jsonSchemaService: JsonSchemaService) { }

  ngOnInit(): void {
    this.displayAttributes = [
      { displayName: "Produkt", value: this.productName || "" },
      { displayName: "Område", value: null },
      { displayName: "Referenssystem", value: null },
      { displayName: "Leveransformat", value: null }
    ];

    this.getProductVariants();
  }

  getActiveStep(activeStep: number) {
    this.activeStep = activeStep;
  }

  getProductVariants() {
    this.productVariantsByProductIdGQL.watch({ id: this.productId }).valueChanges.subscribe(({ data }) => {
      const productVariants = data.product?.productVariants.map(variant => {
        const replacedName = variant.name.replace(variant.product.name, "");
        return {
          id: variant.id, key: null, name: replacedName ? replacedName : variant.name, schema: variant?.customFields?.attributeSchema as any, currency: variant.currencyCode, price: variant.priceWithTax
        };
      });
      this.getProducerSchema(productVariants);
    })
  }

  getProducerSchema(productVariants: KonfigurationsTypAProductVariant[]) {
    productVariants.forEach((variant, index) => {
      let schema: any = variant?.schema;
      if (schema) {
        schema = JSON.parse(schema || {});
        if (hasProperty(schema, ProductVariant.POLYGON)) {
          this.hasPolygonProperty = true;
          this.productVariants.push({ id: variant.id, key: ProductVariant.POLYGON, name: variant.name, schema: schema, currency: variant.currency });
        } else if (hasProperty(schema, ProductVariant.KOMMUN)) {
          this.productVariants.push({ id: variant.id, key: ProductVariant.KOMMUN, name: variant.name, schema: schema, currency: variant.currency });
        } else if (hasProperty(schema, ProductVariant.RIKSTACKANDE)) {
          this.productVariants.push({ id: variant.id, key: ProductVariant.RIKSTACKANDE, name: variant.name, schema: schema, currency: variant.currency, price: variant.price });
        }
        if (this.hasPolygonProperty) {
          this.filteredProductVariants = this.productVariants.filter(variant => variant.key !== ProductVariant.POLYGON);
        } else {
          this.filteredProductVariants = this.productVariants;
        }
        this.isLoading = false;
      } else {
        console.warn("Producer schema for " + variant.name + " could not be found");
        if (index === productVariants.length - 1 && !this.productVariants.length) {
          this.notificationService.error("Produkten går tyvärr inte att beställa just nu, prova igen om en stund eller kontakta support.");
          console.error(this.productName + " has no available product variants or associated schemas");
          this.cancelOrder.emit();
        }
      }
    })
  }

  checkIfFormIsValid(status: FormControlStatus) {
    if (status === "VALID" && this.price) {
      this.konfigurationTypAValid = true;
    } else {
      this.konfigurationTypAValid = false;
    }
  }

  polygonChanges(polygon: GeoJson[]) {
    if (polygon) {
      if (this.propertyOptions) {
        this.propertyOptions = null;
      }
      this.resetPriceValues();
      this.selectedProductVariant = this.productVariants.find(variant => variant.key === ProductVariant.POLYGON);
      this.getPropertiesFromSchema(this.selectedProductVariant.schema);
      this.updateAttributes(1, this.selectedProductVariant.key, polygon[0]);
      this.calculatePriceForProductVariant(polygon[0]);
    }
  }

  productVariantChanges(productVariant: KonfigurationsTypAProductVariant) {
    if (productVariant) {
      this.resetPriceValues();
      this.selectedProductVariant = this.productVariants.find(variant => variant.id === productVariant.id);
      this.getPropertiesFromSchema(productVariant.schema, productVariant.key);
    }
  }

  attributesChanges(attributes: AttributeFormResult) {
    if (attributes.omrade !== null) {
      this.orderAttributes = attributes;
      this.updateAttributes(2, "Referenssystem", attributes?.refsys);
      this.updateAttributes(3, "Leveransformat", attributes?.format);
      if (this.selectedProductVariant?.key && this.selectedProductVariant?.key !== ProductVariant.POLYGON) {
        this.updateAttributes(1, this.selectedProductVariant.key, attributes.omrade);
        this.calculatePriceForProductVariant(attributes.omrade);
      }
    } else {
      this.resetPriceValues();
      this.selectedProductVariant = null;
      this.updateAttributes(1, "Område", null);
    }

    if (attributes.omrade?.length === 0) {
      this.resetPriceValues();
    }
  }

  getPropertiesFromSchema(schema: Property<string>, key?: string) {
    if (schema?.title) {
      let properties = [];
      for (const [key, value] of Object.entries(schema?.properties)) {
        const property: any = value;
        property.key = key;
        properties = [...properties || [], property];
      }
      if (key) {
        const found = properties.find(property => property.key === key);
        switch (found.key) {
          case ProductVariant.KOMMUN:
            this.propertyOptions = { label: found.description, multiple: true, options: found.items["enum"] };
            break;
          case ProductVariant.RIKSTACKANDE:
            this.propertyOptions = { label: found.description, multiple: false, options: [found.const] };
            break;
          default:
            this.propertyOptions = null;
        }
      }
      this.referenssystem = getEnumFromProperties(properties, "refsys");
      this.leveransformat = getEnumFromProperties(properties, "format");
    }
  }

  calculatePriceForProductVariant(value: GeoJson | string[]) {
    this.currency = translateCurrencyCode(this.selectedProductVariant.currency);
    if (value) {
      switch (this.selectedProductVariant.key) {
        case ProductVariant.POLYGON:
          this.getAreaPrice(this.selectedProductVariant.id, JSON.stringify(value || {}));
          break;
        case ProductVariant.KOMMUN:
          //TODO Lägg in prisberäknare när det är klart
          this.price = "NaN";
          break;
        case ProductVariant.RIKSTACKANDE:
          this.price = calculatePrice(this.selectedProductVariant.price) as string;
          break;
      }
    }
  }

  getAreaPrice(productVariantId: string, geojson: string) {
    this.getAreaPriceForVariantGeojsonGQL.watch({ id: productVariantId, geojsonStr: geojson }).valueChanges.subscribe(({ data }) => {
      const price = data.getAreaPriceForVariantGeoJson?.priceWithTax;
      this.price = calculatePrice(price) as string;
    }, error => {
      this.resetPriceValues();
      this.notificationService.error("Kunde inte beräkna pris, prova igen om en stund")
      console.error(error.message);
    })
  }

  updateAttributes(index: number, displayName: string, value: GeoJson | string[] | string) {
    if (value) {
      this.displayAttributes[index].value = value as string;
      switch (displayName) {
        case ProductVariant.POLYGON:
          this.displayAttributes[index].displayName = "Fil";
          const geoJson = value as GeoJson;
          this.displayAttributes[index].value = geoJson.properties?.fileName;
          break;
        case ProductVariant.KOMMUN:
          this.displayAttributes[index].displayName = "Kommun";
          const kommuner = value as string[];
          this.displayAttributes[index].value = kommuner.join(", ");
          break;
        case ProductVariant.RIKSTACKANDE:
          this.displayAttributes[index].displayName = "Rikstäckande";
          break;
        default:
          this.displayAttributes[index].displayName = displayName;
      }
    }
  }

  addToCart() {
    let omradeAttribute;
    if (this.selectedProductVariant.key === ProductVariant.POLYGON) {
      omradeAttribute = this.orderAttributes.omrade[0];
    } else {
      omradeAttribute = this.orderAttributes.omrade;
    }
    const orderAttributes = {
      [this.selectedProductVariant.key]: omradeAttribute,
      refsys: this.orderAttributes.refsys,
      format: this.orderAttributes.format
    };

    const validateJSON = this.jsonSchemaService.validateJSONSchema(this.selectedProductVariant.schema as any, orderAttributes);
    if (validateJSON) {
      const order: AddItemToOrderMutationVariables = {
        productVariantId: this.selectedProductVariant.id,
        customFields: {
          attributes: JSON.stringify(orderAttributes || {}),
          clientAttributes: JSON.stringify(this.displayAttributes || [])
        }
      };
      this.addItemToOrder(order);
    } else {
      this.notificationService.error("Produkten går tyvärr inte att lägga i varukorgen, prova igen om en stund");
    }
  }

  addItemToOrder(order: AddItemToOrderMutationVariables) {
    this.orderService.addItemToOrder(order).subscribe((res) => {
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

  resetPriceValues() {
    this.price = null;
    this.currency = null;
  }
}
