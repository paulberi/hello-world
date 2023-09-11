import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup } from "@angular/forms";
import { FiberInfo, Ledningsagare } from "../../../../../../../generated/markkoll-api";

export class MkFiberInfoPresenter {
  change = new EventEmitter<FiberInfo>();
  valid = new EventEmitter<boolean>(true);

  private _form: UntypedFormGroup;

  get form(): UntypedFormGroup {
    return this._form;
  }

  initializeForm(isReadonly: boolean, fiberInfo: FiberInfo, ledningsagare: string[]) {
    this._form = new UntypedFormGroup({
      bestallare: new UntypedFormControl({value: fiberInfo?.bestallare || "", disabled: isReadonly}),
      bidragsprojekt: new UntypedFormControl({value: fiberInfo?.bidragsprojekt || false, disabled: isReadonly}),
      varderingsprotokoll: new UntypedFormControl({value: fiberInfo?.varderingsprotokoll === undefined ? true : fiberInfo.varderingsprotokoll, disabled: isReadonly}),
      ledningsagare: new UntypedFormControl({value: fiberInfo?.ledningsagare || this.ledningsagareValue(ledningsagare),
        disabled: isReadonly || ledningsagare?.length === 1}),
      ledningsstracka: new UntypedFormControl({value: fiberInfo?.ledningsstracka || "", disabled: isReadonly}),
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

  getFormData(): FiberInfo {
    const controls = this.form.controls;
    const fiberInfo: FiberInfo = {
      bestallare: controls.bestallare.value,
      bidragsprojekt: controls.bidragsprojekt.value,
      varderingsprotokoll: controls.varderingsprotokoll.value,
      ledningsagare: controls.ledningsagare.value,
      ledningsstracka: controls.ledningsstracka.value,
    };
    return fiberInfo;
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
