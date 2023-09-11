import { EventEmitter } from "@angular/core";
import { FormGroup, UntypedFormControl, Validators } from "@angular/forms";
import { Kund } from "../../../../../generated/kundconfig-api";
import { FormGroupChangeCheck } from "../../../../lib/util/formGroupChangeCheck";

export class KunduppgifterPresenter {
  form: FormGroupChangeCheck;
  kundChange = new EventEmitter<Kund>();

  initializeForm(kund: Kund) {
    this.form = new FormGroupChangeCheck({
      id: new UntypedFormControl({value: kund?.id, disabled: true}),
      namn: new UntypedFormControl(kund?.namn, [Validators.required]),
      epost: new UntypedFormControl(kund?.epost, [Validators.required]),
      kontaktperson: new UntypedFormControl(kund?.kontaktperson),
      telefon: new UntypedFormControl(kund?.telefon, [Validators.required]),
      skapadAv: new UntypedFormControl(kund?.skapadAv),
      skapadDatum: new UntypedFormControl(kund?.skapadDatum),
      andradAv: new UntypedFormControl(kund?.andradAv),
      andradDatum: new UntypedFormControl(kund?.andradDatum),
    });
  }

  submit() {
    const kund: Kund = {
      id: this.form.controls.id.value,
      namn: this.form.controls.namn.value,
      epost: this.form.controls.epost.value,
      kontaktperson: this.form.controls.kontaktperson.value,
      telefon: this.form.controls.telefon.value,
      skapadAv: this.form.controls.skapadAv.value,
      skapadDatum: this.form.controls.skapadDatum.value,
      andradAv: this.form.controls.andradAv.value,
      andradDatum: this.form.controls.andradDatum.value
    };

    this.kundChange.emit(kund);
  }

  canSave(): boolean {
    return this.form.valid && this.form.isChanged;
  }
}
