import { Component, forwardRef, OnInit } from '@angular/core';
import { UntypedFormGroup, NG_ASYNC_VALIDATORS, NG_VALUE_ACCESSOR } from '@angular/forms';
import { CreateCustomerResult } from '../../../../../../../../../generated/graphql/admin/admin-api-types';
import { XpNotificationService } from '../../../../../../../../lib/ui/notification/notification.service';
import { AdminCreateCustomerGQL, AdminFilterCustomerByEmailDocument } from '../../../../data-access/customer-form.admin.generated';
import { CustomerFormPresenter } from './customer-form.presenter';

@Component({
  selector: 'mm-customer-form',
  templateUrl: './customer-form.component.html',
  styleUrls: ['./customer-form.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CustomerFormPresenter),
      multi: true
    },
    {
      provide: NG_ASYNC_VALIDATORS,
      useExisting: forwardRef(() => CustomerFormPresenter),
      multi: true
    },
    CustomerFormPresenter
  ]
})
export class CustomerFormComponent implements OnInit {

  get customerForm(): UntypedFormGroup {
    return this.customerFormPresenter.customerForm;
  }

  constructor(
    private customerFormPresenter: CustomerFormPresenter,
    private adminCreateCustomer: AdminCreateCustomerGQL,
    private notificationService: XpNotificationService,
  ) {
  }

  ngOnInit(): void {
    this.customerFormPresenter.initializeForm().then().catch((err) => console.error(err))
  }

  addCustomer() {
    this.adminCreateCustomer.mutate({
      firstName: this.customerForm.get("firstName").value,
      lastName: this.customerForm.get("lastName").value,
      emailAddress: this.customerForm.get("shippingAddress").value,
    }, { refetchQueries: [{ query: AdminFilterCustomerByEmailDocument, variables: { emailAddress: this.customerForm.get("shippingAddress").value } }] }).subscribe(result => {
      switch (result.data?.createCustomer?.__typename as CreateCustomerResult["__typename"]) {
        case "Customer":
          this.notificationService.success("Kund skapad");
          this.customerForm.get("firstName").reset()
          this.customerForm.get("lastName").reset()
          this.customerForm.get("shippingAddress").setErrors(null)
          this.customerForm.updateValueAndValidity();
          break;
        case "EmailAddressConflictError":
          this.notificationService.error("Epostadressen är upptagen")
      }
    })
  }

}
