import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup, Validators } from "@angular/forms";
import { FastighetsokAuth, MetriaMapsAuth } from "../../../../../generated/kundconfig-api";

export class AdmCredentialsFormPresenter {
  form: UntypedFormGroup;
  authChange = new EventEmitter<MetriaMapsAuth>();

  initializeForm(auth: MetriaMapsAuth) {
    this.form = new UntypedFormGroup({
      id: new UntypedFormControl(auth?.id, [Validators.required]),
      username: new UntypedFormControl(auth?.username, [Validators.required]),
      password: new UntypedFormControl(auth?.password, [Validators.required]),
      kundId: new UntypedFormControl(auth?.kundId)
    });
  }

  submit() {
    const auth: MetriaMapsAuth = {
      id: this.form.controls.id.value,
      username: this.form.controls.username.value,
      password: this.form.controls.password.value,
      kundId: this.form.controls.kundId.value
    };

    this.authChange.emit(auth);
  }

  canSave(): boolean {
    return this.form.valid && this.form.dirty;
  }
}
