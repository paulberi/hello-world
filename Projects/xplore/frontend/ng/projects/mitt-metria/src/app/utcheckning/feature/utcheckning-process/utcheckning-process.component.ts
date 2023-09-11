import { environment } from './../../../../environments/environment.prod';
import { ActiveCustomerGQL, AddPaymentGQL, GetActiveOrderTotalPriceGQL, GetGembetPaymentStatusGQL, InitiateGembetPaymentGQL, TransitionToAddingItemsGQL, UpdateCustomerInvoicingLabelGQL, GetActiveOrderStateGQL } from './../../data-access/utcheckning-process.shop.generated';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import { GetPaymentMethodsGQL } from '../../data-access/utcheckning-process.shop.generated';
import { Customer, PaymentMethod } from '../../../../../../../generated/graphql/shop/shop-api-types';
import { ActiveOrderDocument } from '../../../../../../lib/vendure/cart/cart.shop.generated';
import { HttpClient } from '@angular/common/http';
import { calculatePrice, translateCurrencyCode } from '../../../../../../lib/vendure/price-utils';

@Component({
  selector: 'mm-utcheckning-process',
  templateUrl: './utcheckning-process.component.html',
  styleUrls: ['./utcheckning-process.component.scss']
})
export class UtcheckningProcessComponent implements OnInit, OnDestroy {
  paymentMethods$: Observable<PaymentMethod[]>;
  activeCustomer: Customer;
  paymentErrorMessage: string | undefined;
  pending: boolean = false;
  totalPriceWithTax: string;
  vat: string;
  currency: string;
  initialInvoiceRef: string;
  licenceTermsUrl: string;

  activeCustomerSubscription$: Subscription;
  priceSubscription$: Subscription;
  queryParamSubscription$: Subscription;

  constructor(
    private getPaymentMethodsGQL: GetPaymentMethodsGQL,
    private addPaymentGQL: AddPaymentGQL,
    private activeCustomerGQL: ActiveCustomerGQL,
    private getActiveOrderTotalPriceGQL: GetActiveOrderTotalPriceGQL,
    private getGembetPaymentStatusGQL: GetGembetPaymentStatusGQL,
    private initiateGembetPaymentGQL: InitiateGembetPaymentGQL,
    private transitionToAddingItemsGQL: TransitionToAddingItemsGQL,
    private updateCustomerInvoicingLabelGQL: UpdateCustomerInvoicingLabelGQL,
    private getActiveOrderStateGQL: GetActiveOrderStateGQL,
    private router: Router,
    private route: ActivatedRoute,
    private httpClient: HttpClient,
  ) { }

  // Set shippingMethod och ändra state till arrangingPayment görs i guard

  ngOnInit(): void {
    this.getActiveCustomer();
    this.getPrice();
    this.paymentMethods$ = this.getEligablePaymentMethods();
    this.subscribeToQueryParams();
    this.getLicenceTermsUrl();
  }

  getActiveCustomer(): void {
    this.activeCustomerSubscription$ = this.activeCustomerGQL.watch().valueChanges.subscribe(({ data }) => {
      this.activeCustomer = data?.activeCustomer as Customer;
      this.initialInvoiceRef = data?.activeCustomer?.customFields?.invoicingLabel || "";
    })
  }

  getLicenceTermsUrl(): void {
    this.httpClient
      .get(environment.ehandelUrls, { responseType: "json" })
      .toPromise()
      .then((response: any) => {
        const url = response?.shop.split('/api')[0];
        this.licenceTermsUrl = url ? (url + "/assets/source/fc/licensvillkor.pdf") : "https://ehandel.metria.se/assets/source/fc/licensvillkor.pdf";
      }).catch(() => {
        console.error("could not fetch url for license terms");
        this.licenceTermsUrl = "https://ehandel.metria.se/assets/source/fc/licensvillkor.pdf";
      });
  }

  getPrice(): void {
    this.priceSubscription$ = this.getActiveOrderTotalPriceGQL.watch().valueChanges.subscribe(({ data }) => {
      this.totalPriceWithTax = (data?.activeOrder?.totalWithTax / 100).toString() || "NaN";
      this.vat = (calculatePrice(data?.activeOrder?.totalWithTax - data?.activeOrder?.total)).toString() || "NaN";
      this.currency = translateCurrencyCode(data?.activeOrder?.currencyCode) || "";
    })
  }

  getEligablePaymentMethods(): Observable<any> {
    return this.getPaymentMethodsGQL.fetch().pipe(
      map(({ data }) => data?.eligiblePaymentMethods.filter(method => method.isEligible))
    );
  }

  subscribeToQueryParams(): void {
    this.queryParamSubscription$ = this.route.queryParams.subscribe(params => {
      if (params.responseCode === 'OK') {
        this.addPaymentToOrder('creditcard');
      }
    });
  }

  async completeOrder(event: { selectedPayment: string, invoiceRef: string }): Promise<void> {
    const paymentMethodCode = event?.selectedPayment;

    if (paymentMethodCode === 'creditcard') {
      this.getGembetPaymentStatus(paymentMethodCode).subscribe(async status => {
        if (status && status !== 'INITIATED') {
          await this.addPaymentToOrder(paymentMethodCode).catch(error => console.error(error?.message || error))
        } else {
          await this.initiateGembetPayment(paymentMethodCode).catch(error => console.error(error?.message || error))
        }
      })
    } else if (paymentMethodCode === "invoice") {
      event?.invoiceRef && await this.updateCustomerInvoicingLabel(event.invoiceRef).catch(error => console.error(error?.message || error))
      await this.addPaymentToOrder(paymentMethodCode).catch(error => console.error(error?.message || error))
    } else {
      await this.addPaymentToOrder(paymentMethodCode).catch(error => console.error(error?.message || error))
    }
  }

  async back(): Promise<void> {
    //Skickar till produkter för att förhindra att man skickas till avbruten kortbetalning med location.back
    this.router.navigate(["produkter"])
    //this.location.back();
  }

  private getGembetPaymentStatus(paymentMethodCode: string): Observable<string | undefined> {
    return this.getGembetPaymentStatusGQL.fetch(
      { method: paymentMethodCode },
      { fetchPolicy: 'network-only' }
    ).pipe(map(({ data }) => data?.getGembetPaymentStatus))
  }

  private async initiateGembetPayment(paymentMethodCode: string): Promise<void> {
    // Removes eventual parameters. e.g. nets parameters after cancelled payment
    const currentUrl = this.removeUrlParameters(window.location.href);

    const { data } =
      await this.initiateGembetPaymentGQL.mutate({
        method: paymentMethodCode,
        redirectUrls: {
          returnUrlAccept: currentUrl,
          returnUrlDecline: currentUrl,
          returnUrlCancel: currentUrl
        }
      }).toPromise()

    if (data?.initiateGembetPayment) {
      const params = JSON.parse(data.initiateGembetPayment);
      if (params.action && params.action.doAction === 'REDIRECT') {
        const queryString = params.action.parameter.reduce(
          (result: any, param: any) => {
            return `${result.length ? result + '&' : ''}${param.name}=${param.value}`;
          },
          ''
        );
        window.location.href = params.action.address + '?' + queryString;
      }
    }
  }

  private removeUrlParameters(url: string): string {
    return url.split("?")[0] || url;
  }

  private async updateCustomerInvoicingLabel(invoicingLabel: string): Promise<void> {
    const { data } = await this.updateCustomerInvoicingLabelGQL.mutate({
      input: {
        invoicingLabel: invoicingLabel,
      }
    }).toPromise();
  }


  private async addPaymentToOrder(paymentMethodCode: string): Promise<void> {
    this.pending = true;

    const { data } = await this.addPaymentGQL.mutate({
      input: {
        method: paymentMethodCode,
        metadata: {}
      }
    }, { refetchQueries: [ActiveOrderDocument] }).toPromise();

    console.log(data)
    switch (data?.addPaymentToOrder?.__typename) {
      case 'Order':
        const order = data?.addPaymentToOrder;
        console.log(order)
        if (order && (order.state === 'PaymentSettled' || order.state === 'PaymentAuthorized')) {
          this.router.navigate(['bekraftelse', order.code], { relativeTo: this.route })
        }
        break;
      case 'PaymentDeclinedError':
      case 'PaymentFailedError':
        this.paymentErrorMessage = data?.addPaymentToOrder.paymentErrorMessage;
        break;
      case 'OrderPaymentStateError':
      case 'OrderStateTransitionError':
        this.paymentErrorMessage = data?.addPaymentToOrder.message;
        break;
    }

    this.pending = false;
  }

  async setOrderStateToAddingItems(): Promise<void> {
    this.getActiveOrderStateGQL.watch().valueChanges.subscribe(({ data }) => {
      if (data?.activeOrder && !(data?.activeOrder?.state === 'PaymentSettled' || data?.activeOrder?.state === 'PaymentAuthorized')) {
        this.transitionToAddingItemsGQL.mutate().subscribe(() => { })
      }
    })
  }

  ngOnDestroy(): void {
    this.activeCustomerSubscription$?.unsubscribe();
    this.priceSubscription$?.unsubscribe();
    this.queryParamSubscription$?.unsubscribe();
    this.setOrderStateToAddingItems();
  }
}

