import { EventEmitter } from "@angular/core";
import { FormControl, UntypedFormControl, UntypedFormGroup } from "@angular/forms";
import { Avtalsinstallningar, Beredare } from "../../../../../../../generated/markkoll-api";
import { FormGroupChangeCheck } from "../../../../../../lib/util/formGroupChangeCheck";
import { Projekt } from "../../../model/projekt";
import { UpdateProjektEvent } from "../edit-projekt/edit-projekt.component";
import { UserRoleEntry } from "../user-roles/user-roles.component";

export class MkProjektinformationPresenter {
  private _form: FormGroupChangeCheck;

  updateProjekt = new EventEmitter<UpdateProjektEvent>();

  initializeForm(projekt: Projekt, userRoleEntries: UserRoleEntry[], beredare: Beredare, avtalsinstallningar: Avtalsinstallningar) {
    this._form = new FormGroupChangeCheck({
      projekt: new FormControl(projekt),
      users: new FormControl(userRoleEntries),
      beredare: new FormControl(beredare),
      avtalsinstallningar: new FormControl(avtalsinstallningar)
    });
  }

  canSave(): boolean {
    return this._form.isChanged && this._form.valid;
  }

  markAsPristine() {
    this._form.markAsPristine();
  }

  onSubmit() {
    this.updateProjekt.emit({
      projekt: this.form.controls.projekt.value,
      users: this.form.controls.users.value,
      beredare: this.form.controls.beredare.value,
      avtalsinstallningar: this.form.controls.avtalsinstallningar.value
    });
  }

  get form(): UntypedFormGroup {
    return this._form;
  }
}
