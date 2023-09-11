import { EventEmitter } from "@angular/core";
import { UntypedFormGroup, UntypedFormControl, Validators } from "@angular/forms";

export class MkHaglofImportPresenter {
  form: UntypedFormGroup;

  submitForm = new EventEmitter<File>();

  initializeForm() {
    this.form = new UntypedFormGroup({
      files: new UntypedFormControl([], Validators.required)
    });
  }

  submit() {
    this.submitForm.emit(this.form.controls.files.value[0]);
  }

  canSave() {
    return this.form.valid;
  }
}
