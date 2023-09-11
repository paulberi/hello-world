import { UntypedFormControl, Validators } from "@angular/forms";
import { MarkkollUser, UserInfo } from "../../../../../../generated/markkoll-api";
import { FormGroupChangeCheck } from "../../../../../lib/util/formGroupChangeCheck";

export class MkUserFormPresenter {
  form: FormGroupChangeCheck;
  edituser: MarkkollUser;

  initializeForm(edituser?: MarkkollUser) {
    this.edituser = edituser;

    this.form = new FormGroupChangeCheck({
      fornamn: new UntypedFormControl(edituser?.fornamn, [Validators.required]),
      efternamn: new UntypedFormControl(edituser?.efternamn, [Validators.required]),
      email: new UntypedFormControl(edituser?.email, [Validators.required, Validators.email]),
    });
  }

  onSubmit(): UserInfo {
    const user: UserInfo = {
      fornamn: this.form.controls.fornamn.value,
      efternamn: this.form.controls.efternamn.value,
      email: this.form.controls.email.value,
    };
    this.form.reset();

    return user;
  }

  onSave(): MarkkollUser {
    this.edituser.fornamn = this.form.controls.fornamn.value;
    this.edituser.efternamn = this.form.controls.efternamn.value;
    this.edituser.email = this.form.controls.email.value;
    return this.edituser;
  }

  canSave(): boolean {
    return this.form.valid && this.form.isChanged;
  }
}
