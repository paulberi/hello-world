import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup, Validators } from "@angular/forms";

export class MkLedningsagarePresenter {
  form: UntypedFormGroup;

  ledningsagareAdd = new EventEmitter<string>();

  initializeForm() {
    this.form = new UntypedFormGroup({
      namn: new UntypedFormControl("", Validators.required)
    });
  }

  submit() {
    this.ledningsagareAdd.emit(this.form.controls.namn.value);
  }
}
