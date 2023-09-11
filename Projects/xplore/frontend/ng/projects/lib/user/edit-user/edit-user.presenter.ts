import { EventEmitter } from "@angular/core";
import { UntypedFormControl, Validators } from "@angular/forms";
import { MarkkollUser, UserInfo } from "../../../../generated/kundconfig-api";
import { FormGroupChangeCheck } from "../../util/formGroupChangeCheck";

export class XpEditUserPresenter {
  form: FormGroupChangeCheck;
  submit = new EventEmitter<UserInfo>();

  initializeForm(user?: MarkkollUser) {
    this.form = new FormGroupChangeCheck({
      fornamn: new UntypedFormControl(user?.fornamn, [Validators.required]),
      efternamn: new UntypedFormControl(user?.efternamn, [Validators.required]),
      email: new UntypedFormControl(user?.email, [Validators.required, Validators.email])
    });
  }

  onSubmit() {
    const user: UserInfo = {
      fornamn: this.form.controls.fornamn.value,
      efternamn: this.form.controls.efternamn.value,
      email: this.form.controls.email.value
    };
    this.submit.emit(user);
    this.form.reset();
  }

  canSave(): boolean {
    return this.form.valid && this.form.isChanged;
  }
}
