import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup } from "@angular/forms";
import { ElnatInfo } from "../../../../../../../generated/markkoll-api";

export class MkElnatInfoPresenter {
  change = new EventEmitter<ElnatInfo>();
  valid = new EventEmitter<boolean>(false);

  private _form: UntypedFormGroup;

  get form(): UntypedFormGroup {
    return this._form;
  }

  initializeForm(isReadonly: boolean, elnatInfo: ElnatInfo, ledningsagare: string[]) {
    this._form = new UntypedFormGroup({
      bestallare: new UntypedFormControl({value: elnatInfo?.bestallare || "", disabled: isReadonly}),
      ledningsagare: new UntypedFormControl({value: elnatInfo?.ledningsagare || this.ledningsagareValue(ledningsagare), disabled: isReadonly}),
      ledningsstracka: new UntypedFormControl({value: elnatInfo?.ledningsstracka || "", disabled: isReadonly})
    });
    this.change.emit(this.getFormData());
    this.registerOnChange();
  }

  canSave(): boolean {
    return this._form.valid;
  }

  registerOnChange() {
    this._form.valueChanges.subscribe(form => {
      this.change.emit(this.getFormData());
      this.valid.emit(this.canSave());
    });
  }

  getFormData(): ElnatInfo {
    const controls = this.form.controls;
    const elnatInfo: ElnatInfo = {
      bestallare: controls.bestallare.value,
      ledningsagare: controls.ledningsagare.value,
      ledningsstracka: controls.ledningsstracka.value,
    };
    return elnatInfo;
  }

  private ledningsagareValue(ledningsagare: string[]) {
    if (!ledningsagare || ledningsagare.length !== 1) {
      return null;
    }
    else {
      return ledningsagare[0];
    }
  }
}
