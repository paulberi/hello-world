import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup } from "@angular/forms";

export class ParentFormPresenter {
  private _parentForm: UntypedFormGroup;

  productFormChange = new EventEmitter<any>();
  customerFormStatusChange = new EventEmitter<any>();

  get parentForm(): UntypedFormGroup {
    return this._parentForm;
  }

  initializeForm() {
    this._parentForm = new UntypedFormGroup({
      customerForm: new UntypedFormControl(""),
      productForm: new UntypedFormControl(""),
      dynamicForm: new UntypedFormControl("")
    });

    this._parentForm.get("productForm").valueChanges.subscribe(change => {
      this.productFormChange.emit(change);
    });

    this._parentForm.get("customerForm").statusChanges.subscribe(status => {
      this.customerFormStatusChange.emit(status);
    });
  }

  resetForm() {
    this._parentForm.get("productForm").reset();
    this._parentForm.get("dynamicForm").reset();
  }
}
