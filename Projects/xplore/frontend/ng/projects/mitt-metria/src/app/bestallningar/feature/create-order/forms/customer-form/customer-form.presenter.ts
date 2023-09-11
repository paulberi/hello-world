import { Injectable } from "@angular/core";
import { AbstractControl, AsyncValidatorFn, ControlValueAccessor, UntypedFormControl, UntypedFormGroup, ValidationErrors, ValidatorFn, Validators } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Observable, of } from "rxjs";
import { filter, map, startWith, take } from "rxjs/operators";
import { AdminFilterCustomerByEmailGQL } from "../../../../data-access/customer-form.admin.generated";

@Injectable()
export class CustomerFormPresenter implements ControlValueAccessor, Validators {
  private _customerForm: UntypedFormGroup;
  onChange: any = () => { };
  onTouched: any = () => { };
  parameterOrderId: string;
  parameterShippingAddress: string;

  constructor(
    private adminFilterCustomerByEmailGQL: AdminFilterCustomerByEmailGQL,
    private route: ActivatedRoute
  ) {
    this.parameterOrderId = this.route.snapshot.queryParamMap.get("orderId");
    this.parameterShippingAddress = this.route.snapshot.queryParamMap.get("email");
  }

  get customerForm(): UntypedFormGroup {
    return this._customerForm;
  }

  get value(): any {
    return this._customerForm.value;
  }

  set value(value: any) {
    this._customerForm.setValue(value);
    this.onChange(value);
    this.onTouched();
  }

  //Väntar på att formuläret har skapats upp och sedan körs writeValue för att parentForm ska kunna se 
  //värdena i formControls om det sätts några när formuläret skapas
  async initializeForm() {
    await this.createForm();
    this.writeValue(this._customerForm.value);
    this.parameterOrderId ? this._customerForm.get("orderId").markAsTouched() : null;
    this.parameterShippingAddress ? this._customerForm.get("shippingAddress").markAsTouched() : null;
  

    this._customerForm.valueChanges.subscribe(value => {
      this.onChange(value);
      this.onTouched();
    });
  }

  createForm(): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      try {
        this._customerForm = new UntypedFormGroup({
          orderId: new UntypedFormControl(this.parameterOrderId || "", { validators: [Validators.required, Validators.minLength(8), Validators.maxLength(8), Validators.pattern(/^[0-9]{8}$/)], updateOn: "blur" }),
          shippingAddress: new UntypedFormControl(this.parameterShippingAddress || "", {
            validators: [Validators.required, Validators.email],
            asyncValidators: [this.validateCustomer()],
            updateOn: "blur"
          }),
          //if customer doesn't exist
          firstName: new UntypedFormControl(""),
          lastName: new UntypedFormControl("")
        });
        resolve();
      } catch {
        reject("Error: Could not create customer form");
      }
    })
  }

  writeValue(value: any): void {
    if (value) {
      this.value = value;
    }

    if (value === null) {
      this._customerForm.reset();
    }
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  validate(): Observable<ValidationErrors> {
    if (!this._customerForm || this._customerForm.valid) {
      return of(null);
    }

    return this._customerForm.statusChanges.pipe(
      startWith(this._customerForm.status),
      filter((status) => status !== 'PENDING'),
      take(1),
      map((status) => {
        return this._customerForm.valid ? null : { customerForm: { valid: false } };
      })
    );
  }

  validateCustomer(): AsyncValidatorFn {
    return (control: AbstractControl) => {
      return this.adminFilterCustomerByEmailGQL.fetch({
        emailAddress: control.value,
      }).pipe(
        map(result => {
          return !result.data.customers.items.length ? { customerExistsInvalid: true } : null;
        })
      );
    }
  }

}


