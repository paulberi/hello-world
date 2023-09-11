import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup, ValidatorFn, Validators } from "@angular/forms";

export interface RectangleFormControl {
  title: string;
  id: string,
  min: number,
  max: number
}

export interface RectangleFormValue {
  eMin: number;
  eMax: number;
  nMin: number;
  nMax: number;
}

export class RectanglePresenter {
  private _rectangleForm: UntypedFormGroup;

  formChanges = new EventEmitter<RectangleFormValue>();

  get rectangleForm(): UntypedFormGroup {
    return this._rectangleForm;
  }

  initializeForm(formControls: RectangleFormControl[]) {
    this._rectangleForm = new UntypedFormGroup({}, { validators: [this.validateMinMax("nMin", "nMax"), this.validateMinMax("eMin", "eMax")] });
    formControls.forEach(formControl => {
      this._rectangleForm.addControl(formControl.id, new UntypedFormControl(null, [
        Validators.required, Validators.min(formControl.min), Validators.max(formControl.max)
      ]
      ))
    });

    this._rectangleForm.valueChanges.subscribe(value => {
      this.formChanges.emit(value);
    });
  }

  validateMinMax(min: string, max: string): ValidatorFn {
    return (form: UntypedFormGroup): null => {
      const minControl = form.get(min);
      const maxControl = form.get(max);
      const existingErrors = form.get(min)?.errors;
      delete existingErrors?.minGreaterThanMax;
      if (maxControl?.valid) {
        if (minControl.value >= maxControl.value) {
          minControl.setErrors({ ...existingErrors, "minGreaterThanMax": true });
        } else if (Object.keys(existingErrors || []).length === 0) {
          minControl.setErrors(null);
        } else {
          minControl.setErrors(existingErrors);
        }
      }
      return null
    }
  }
}
