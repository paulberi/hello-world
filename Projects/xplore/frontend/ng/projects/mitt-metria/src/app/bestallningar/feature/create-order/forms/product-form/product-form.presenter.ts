import { EventEmitter } from "@angular/core";
import { ControlValueAccessor, UntypedFormControl, UntypedFormGroup } from "@angular/forms";

export class ProductFormPresenter implements ControlValueAccessor {
  private _productForm: UntypedFormGroup;
  onChange: any = () => { };
  onTouched: any = () => { };

  productIdChange = new EventEmitter<string>();

  get productForm(): UntypedFormGroup {
    return this._productForm;
  }

  get value(): any {
    return this._productForm.value;
  }
  set value(value: any) {
    this._productForm.setValue(value);
    this.onChange(value);
    this.onTouched();
  }

  initializeForm() {
    this._productForm = new UntypedFormGroup({
      productId: new UntypedFormControl(""),
      productVariantId: new UntypedFormControl("")
    });

    this._productForm.valueChanges.subscribe(value => {
      this.onChange(value);
      this.onTouched();
    });

    this._productForm.get("productId").valueChanges.subscribe(change => {
      this.productIdChange.emit(change);
    });
  }

  writeValue(value: any): void {
    if (value) {
      this.value = value;
    }

    if (value === null) {
      this._productForm.reset();
    }
  }
  registerOnChange(fn: any): void {
    this.onChange = fn;
  }
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }
}
