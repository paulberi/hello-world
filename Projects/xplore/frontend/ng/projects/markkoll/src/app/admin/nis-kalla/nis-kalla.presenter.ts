import { EventEmitter } from "@angular/core";
import { UntypedFormGroup, UntypedFormControl } from "@angular/forms";
import { NisKalla } from "../../../../../../generated/markkoll-api";
import { FormGroupChangeCheck } from "../../../../../lib/util/formGroupChangeCheck";

export class MkNisKallaPresenter {
  change = new EventEmitter<NisKalla>();

  private _form: FormGroupChangeCheck;
  get form(): UntypedFormGroup {
    return this._form;
  }

  initializeForm(nisKalla: NisKalla) {
    this._form = new FormGroupChangeCheck({
      dpCom: new UntypedFormControl(nisKalla?.dpCom ? nisKalla.dpCom : false),
      dpPower: new UntypedFormControl(nisKalla?.dpPower ? nisKalla.dpPower : false),
      trimble: new UntypedFormControl(nisKalla?.trimble ? nisKalla.trimble : false)
    });
    this._form.valueChanges.subscribe(res => {
      this.change.emit(this._form.value as NisKalla);
    });
  }
}
