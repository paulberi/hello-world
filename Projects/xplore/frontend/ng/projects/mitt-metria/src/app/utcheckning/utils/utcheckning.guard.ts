import { XpNotificationService } from './../../../../../lib/ui/notification/notification.service';
import { GetActiveOrderGQL, GetShippingMethodsGQL, SetShippingMethodGQL, TransitionToArrangingPaymentGQL } from '../data-access/utcheckning-guard.shop.generated';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class UtcheckningGuard implements CanActivate {
  constructor(
    private router: Router,
    private getActiveOrderGQL: GetActiveOrderGQL,
    private getShippingMethodsGQL: GetShippingMethodsGQL,
    private setShippingMethodGQL: SetShippingMethodGQL,
    private transitionToArrangingPaymentGQL: TransitionToArrangingPaymentGQL,
    private notificationService: XpNotificationService
  ) {

  }
  canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {

    const activeOrder$ = this.getActiveOrderGQL.fetch().pipe(
      map(({ data }) => data?.activeOrder),
    );

    return activeOrder$.pipe(map(activeOrder => {
      if (!activeOrder || !activeOrder.lines.length) {
        this.router.navigate(['/']);
        this.notificationService.error("Varukorgen är tom, lägg en eller flera produkter i varukorgen för att påbörja utcheckning.")
        return false;
      }

      if (!activeOrder.shippingLines.length) {
        // Sätter default shipping method pga alltid leverans med email
        this.getDefaultShippingMethod().subscribe(defaultShippingMethod => {
          if (defaultShippingMethod) {
            this.setShippingMethodGQL.mutate({ shippingMethodId: defaultShippingMethod.id }).subscribe(() => {
              this.setStateToArrangingPayment()
            })
          }
        })
      } else {
        this.setStateToArrangingPayment()
      }
    }))
  }

  private getDefaultShippingMethod() {
    return this.getShippingMethodsGQL.fetch().pipe(
      map(({ data }) => data?.eligibleShippingMethods?.find(method => method.code === 'metria-shipping'))
    );
  }

  private setStateToArrangingPayment() {
    return this.transitionToArrangingPaymentGQL.mutate().subscribe(() => {
      return true;
    })
  }

}
