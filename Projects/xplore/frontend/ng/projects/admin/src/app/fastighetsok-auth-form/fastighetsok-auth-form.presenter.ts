import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup, Validators } from "@angular/forms";
import { FastighetsokAuth } from "../../../../../generated/kundconfig-api";

export class AdmFastighetsokAuthFormPresenter {
  form: UntypedFormGroup;
  authChange = new EventEmitter<FastighetsokAuth>();

  private _emptyAtInitialization = true;

  initializeForm(auth: FastighetsokAuth) {
    this.form = new UntypedFormGroup({
      id: new UntypedFormControl(auth?.id, [Validators.required]),
      username: new UntypedFormControl(auth?.username, [Validators.required]),
      password: new UntypedFormControl(auth?.password, [Validators.required]),
      kundmarke: new UntypedFormControl(auth?.kundmarke, [Validators.required]),
      kundId: new UntypedFormControl(auth?.kundId)
    });

    this._emptyAtInitialization = auth.username == null && auth.password == null && auth.kundmarke == null;
  }

  submit() {
    const auth: FastighetsokAuth = {
      id: this.form.controls.id.value,
      username: this.form.controls.username.value,
      password: this.form.controls.password.value,
      kundmarke: this.form.controls.kundmarke.value,
      kundId: this.form.controls.kundId.value
    };

    this.authChange.emit(auth);
  }

  canSave(): boolean {
    return this.form.valid && this.form.dirty;
  }

  emptyAtInitialization(): boolean {
    return this._emptyAtInitialization;
  }
}
