import { AfterViewChecked, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UntypedFormGroup } from '@angular/forms';
import { BehaviorSubject } from 'rxjs';
import { CancelOrderResult, Order } from '../../../../../../../generated/graphql/admin/admin-api-types';
import { UpdateOrderItemsResult } from '../../../../../../../generated/graphql/admin/admin-api-types';
import { XpNotificationService } from '../../../../../../lib/ui/notification/notification.service';
import { AdminAddItemToOrderGQL, AdminCancelOrderGQL, AdminGetCustomerByEmailGQL, AdminGetOrCreateActiveOrderForCustomerGQL, AdminOngoingOrderDocument, AdminOngoingOrderGQL, AdminPlaceOrderGQL, AdminSetOrderCustomFieldsGQL } from './create-order.admin.generated';
import { JsonSchemaService } from '../../../shared/utils/json-schema.service';
import { ParentFormPresenter } from './parent-form.presenter';
import { distinctUntilChanged } from "rxjs/operators";
import { DialogService } from '../../../../../../lib/dialogs/dialog.service';
import { ConfirmationDialogModel } from '../../../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component';
import { Property } from '../../../shared/utils/property-utils';
import { AdminOrdersDocument } from '../../data-access/all-orders.admin.generated';
import { ProductVariantsSchemasByProductIdGQL } from './create.order.shop.generated';

@Component({
  selector: 'mm-create-order',
  templateUrl: './create-order.component.html',
  styleUrls: ['./create-order.component.scss'],
  providers: [ParentFormPresenter]
})
export class CreateOrderComponent implements OnInit, AfterViewChecked {
  productVariantId: string;
  orderId: string;
  order: Order;
  schema: any;
  properties: BehaviorSubject<Property<string>[]> = new BehaviorSubject([]);
  required: BehaviorSubject<string[]> = new BehaviorSubject([]);
  parameterOrderId: string;
  parameterShippingAddress: string;

  get parentForm(): UntypedFormGroup {
    return this.parentFormPresenter.parentForm;
  }

  constructor(
    private parentFormPresenter: ParentFormPresenter,
    private notificationService: XpNotificationService,
    private jsonSchemaService: JsonSchemaService,
    private cdr: ChangeDetectorRef,
    private productVariantsSchemasByProductIdGQL: ProductVariantsSchemasByProductIdGQL,
    private adminGetCustomerByEmailGQL: AdminGetCustomerByEmailGQL,
    private adminSetOrderCustomFieldsGQL: AdminSetOrderCustomFieldsGQL,
    private adminGetOrCreateActiveOrderForCustomerGQL: AdminGetOrCreateActiveOrderForCustomerGQL,
    private adminAddItemToOrderGQL: AdminAddItemToOrderGQL,
    private adminOngoingOrderGQL: AdminOngoingOrderGQL,
    private adminPlaceOrderGQL: AdminPlaceOrderGQL,
    private adminCancelOrderGQL: AdminCancelOrderGQL,
    private dialogService: DialogService,
  ) { }

  ngOnInit(): void {
    this.initializeParentForm()
    this.onProductFormChanges();
    this.onCustomerFormChanges();

    this.cdr.detectChanges();
  }

  ngAfterViewChecked() {
    this.cdr.detectChanges();
  }

  initializeParentForm(): void {
    this.parentFormPresenter.initializeForm();
  }

  onProductFormChanges() {
    this.parentFormPresenter.productFormChange.subscribe(async (value: any) => {
      this.parentForm.get("dynamicForm").reset();
      if (value?.productId && value?.productVariantId) {
        this.productVariantId = value.productVariantId;
        this.setJSONSchema(value.productId, this.productVariantId)
      } else {
        this.productVariantId = "";
        this.schema = null;
      }
    })
  }

  setJSONSchema(productId: string, productVariantId: string): void {
    this.productVariantsSchemasByProductIdGQL.watch({ id: productId }
    ).valueChanges.subscribe((result: any) => {
      const variants = result?.data?.product?.variants
      if (variants) {
        const productVariant = variants.find(variant => variant.id === productVariantId)
        const schema = productVariant?.customFields?.attributeSchema;
        if (schema) {
          try {
            this.schema = JSON.parse(schema)
            this.addProperties(this.schema);
          } catch (error) {
            console.error("Error converting schema to json:", error?.message);
            this.notificationService.error("Kunde inte hämta utsnitt")
            this.productVariantId = "";
            this.schema = null;
          }
        } else {
          console.error("JSON schema could not be found");
          this.notificationService.error("Kunde inte hämta utsnitt")
          this.schema = null;
        }
      } else {
        console.error("JSON schema could not be found: No product variants could be found");
        this.notificationService.error("Kunde inte hämta utsnitt")
        this.schema = null;
      }
    })
  }

  addProperties(schema: any) {
    if (schema?.title) {
      let properties = [];
      for (const [key, value] of Object.entries(schema?.properties)) {
        const property: any = value;
        property.key = key;
        properties = [...properties || [], property];
      }
      this.properties.next(properties);
      this.required.next(schema?.required);
    }
  }

  addProductToOrder() {
    const dynamicFormValues = this.parentForm.get("dynamicForm").value;

    //Även om en input har type="number", blir värdet ändå en sträng
    //Lösning: Konverterar strängar som har siffror till number
    for (const [key, value] of Object.entries(dynamicFormValues)) {
      if (!isNaN(value as any) && typeof value === "string") {
        dynamicFormValues[key] = +value;
      }
    }

    if (this.schema) {
      const validateJSON = this.jsonSchemaService.validateJSONSchema(this.schema, dynamicFormValues)
      if (validateJSON) {
        this.addItemToOrderAdmin(this.productVariantId, JSON.stringify(dynamicFormValues));
      }
    } else {
      this.notificationService.error("Kan ej verifiera input");
    }
  }

  onCustomerFormChanges() {
    this.parentFormPresenter.customerFormStatusChange
      .pipe(distinctUntilChanged((a, b) => JSON.stringify(a) === JSON.stringify(b)))
      .subscribe(status => {
        if (status === "VALID") {
          this.cancelOrderForm();
          this.orderId = null;
          this.order = null;
          const customerFormValues = this.parentForm.get("customerForm").value;
          this.adminGetCustomerByEmailGQL.watch({ emailAddress: customerFormValues.shippingAddress }).valueChanges.subscribe(result => {
            if (!result.loading && result.data?.getCustomerByEmail?.id) {
              this.createOrderForCustomer(result.data?.getCustomerByEmail?.id);
            }
          })
        }
      })
  }

  createOrderForCustomer(customerId: string) {
    this.adminGetOrCreateActiveOrderForCustomerGQL.mutate({ customerId }).subscribe(result => {
      if (result.data?.getOrCreateActiveOrderForCustomer.id) {
        this.orderId = result.data?.getOrCreateActiveOrderForCustomer?.id;
        this.addSalesforceIdAndEmail();
        this.ongoingOrder(this.orderId);
      }
    }, (error) => {
      console.log(error.message);
      this.notificationService.error("Order kunde inte skapas");
    })
  }

  ongoingOrder(orderId: string) {
    this.adminOngoingOrderGQL.watch({ orderId }).valueChanges.subscribe((result: any) => {
      this.order = result.data?.order;
    })
  }

  addSalesforceIdAndEmail() {
    const customerFormValues = this.parentForm.get("customerForm").value;
    this.adminSetOrderCustomFieldsGQL.mutate({
      orderId: this.orderId,
      emailAddress: customerFormValues.shippingAddress,
      externalRef: customerFormValues.orderId
    }).subscribe();
  }

  addItemToOrderAdmin(productVariantId: string, formValues: string) {
    this.adminAddItemToOrderGQL.mutate({
      orderId: this.orderId,
      productVariantId: productVariantId,
      attributes: {
        attributes: formValues
      }
    }, { refetchQueries: [{ query: AdminOngoingOrderDocument, variables: { orderId: this.orderId } }] }).subscribe((result: any) => {
      switch (result.data?.addItemToOrder?.__typename as UpdateOrderItemsResult["__typename"]) {
        case "Order":
          this.notificationService.success("Produkt tillagd");
          break;
        case "OrderModificationError":
        case "OrderLimitError":
          this.notificationService.error("Produkten finns redan i pågående beställning");
        case "NegativeQuantityError":
        case "InsufficientStockError":
          console.log(result.data?.addItemToOrder?.__typename, ": ", result.data?.addItemToOrder?.message);
      }
    })
  }

  cancelOrderForm() {
    this.parentFormPresenter.resetForm();
    this.schema ? this.schema = null : null;
    this.productVariantId ? this.productVariantId = "" : null;
  }

  placeOrder() {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Skicka beställning",
        "Innan en beställning går till produktion och leverans så måste kunden accepterat pris och leveransvillkor. Klicka på Beställ om du vill gå vidare, eller Avbryt för att gå tillbaka till beställningsformuläret.",
        "Avbryt",
        "Beställ"),
      dialogResult => {
        if (dialogResult) {
          this.addSalesforceIdAndEmail();
          this.adminPlaceOrderGQL.mutate({
            orderId: this.orderId
          }, { refetchQueries: [AdminOrdersDocument] }).subscribe(result => {
            if (result.data?.placeOrder?.id) {
              this.cancelOrderForm();
              this.parentForm.get("customerForm").reset();
              this.notificationService.success("Beställning skickad");
            }
          }, (error) => {
            console.log(error.message);
            this.notificationService.error("Beställning kunde ej skickas");
          })
        }
      });

  }

  cancelOrder() {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Avbryt beställning",
        "Är du säker på att du vill radera pågående beställning?",
        "Avbryt",
        "Radera"),
      dialogResult => {
        if (dialogResult) {
          this.adminCancelOrderGQL.mutate({ orderId: this.orderId }).subscribe(result => {
            switch (result.data?.cancelOrder.__typename as CancelOrderResult["__typename"]) {
              case "Order":
                this.cancelOrderForm();
                this.parentForm.get("customerForm").reset();
                this.notificationService.success("Order avbruten");
                break;
              case "CancelActiveOrderError":
              case "EmptyOrderLineSelectionError":
              case "MultipleOrderError":
              case "OrderStateTransitionError":
              case "QuantityTooGreatError":
                this.notificationService.success("Order kunde ej avbrytas");
            }
          })
        }
      });

  }

}
