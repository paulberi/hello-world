import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup, Validators } from "@angular/forms";
import { Abonnemang } from "../../../../../generated/kundconfig-api";

export class AdmAbonnemangPresenter {
  form: UntypedFormGroup;
  abonnemangAdd = new EventEmitter<Abonnemang>();

  initializeForm() {
    this.form = new UntypedFormGroup({
      id: new UntypedFormControl(null),
      produkt: new UntypedFormControl(null, [Validators.required]),
      typ: new UntypedFormControl(null, [Validators.required]),
    });
  }

  submit() {
    const abonnemang: Abonnemang = {
      id: this.form.controls.id.value,
      produkt: this.form.controls.produkt.value,
      typ: this.form.controls.typ.value
    };

    this.abonnemangAdd.emit(abonnemang);
  }

  canSave() {
    return this.form.valid;
  }
}
