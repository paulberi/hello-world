import { EventEmitter } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { FormGroupChangeCheck } from "../../../../../../../../../lib/util/formGroupChangeCheck";
import { MkIntrangForm } from "./intrang-form";
import { IntrangOption } from "./intrang-form.component";

export class MkIntrangFormPresenter implements MkIntrangForm {
  private _form: FormGroupChangeCheck = this.createForm([]);
  private _intrangOptions: IntrangOption[] = [];

  intrangOptionAdd = new EventEmitter<IntrangOption>();

  hasUnsavedChanges(): boolean {
    return this._form.isChanged;
  }

  isValid(allowDashes: boolean): boolean {
    if (!allowDashes) {
      return this.form.valid && !this.hasDashSelected(this.form);
    }
    else {
      return this.form.valid;
    }
  }

  setDisabled(isDisabled: boolean) {
    if (isDisabled) {
      this.form.disable();
    } else {
      this.form.enable();
    }
  }

  setFormValue(intrangOptions: IntrangOption[]) {
    this._intrangOptions = intrangOptions;
    this.form.setValue({
      intrangstyp: this.ValueIfAllItemsEqual(intrangOptions?.map(io => io.intrangstyp)) || "-",
      subtyp: this.ValueIfAllItemsEqual(intrangOptions?.map(io => io.subtyp)) || "-",
      avtalstyp: this.ValueIfAllItemsEqual(intrangOptions?.map(io => io.avtalstyp)) || "-",
    });
    this.form.markAsPristine();
  }

  submit() {
    const formValue = this.form.value;
    Object.keys(formValue).forEach(key => {
      if (formValue[key] === "-") {
        formValue[key] = null;
      }
    });

    this.intrangOptionAdd.emit(formValue);
  }

  get form(): FormGroup {
    return this._form;
  }

  get intrangOptions(): IntrangOption[] {
    return this._intrangOptions;
  }

  private createForm(intrangOptions: IntrangOption[]): FormGroupChangeCheck {
    return new FormGroupChangeCheck({
      intrangstyp: new FormControl(this.ValueIfAllItemsEqual(intrangOptions?.map(io => io.intrangstyp)) || "-", Validators.required),
      subtyp: new FormControl(this.ValueIfAllItemsEqual(intrangOptions?.map(io => io.subtyp)) || "-", Validators.required),
      avtalstyp: new FormControl(this.ValueIfAllItemsEqual(intrangOptions?.map(io => io.avtalstyp)) || "-", Validators.required),
    });
  }

  private hasDashSelected(form: FormGroup): boolean {
    return Object.keys(form.controls).some(key => form.controls[key].value === "-");
  }

  private ValueIfAllItemsEqual<T>(array: T[]): T {
    if (array?.length > 0) {
      const val = array[0];
      return array.every(item => item == val) ? val : null;
    } else {
      return null;
    }
  }
}
