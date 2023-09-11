import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup, Validators } from "@angular/forms";
import { Kund, KundInfo } from "../../../../../generated/kundconfig-api";
import { FormGroupChangeCheck } from "../../../../lib/util/formGroupChangeCheck";

export interface AdmCreateKundEvent {
  kund: KundInfo;
  createMore: boolean;
}
export class AdmCreateKundPresenter {
  private _form: FormGroupChangeCheck;
  currentKunder: Kund[] = [];
  createMore = false;
  submit = new EventEmitter<AdmCreateKundEvent>();

  get form(): UntypedFormGroup {
    return this._form;
  }

  initializeForm(kunder) {
    this.currentKunder = kunder;
    this._form = new FormGroupChangeCheck({
      namn: new UntypedFormControl("", [Validators.required, this.nameDuplicateValidator.bind(this)]),
      organisationsnummer: new UntypedFormControl("", [Validators.required, this.orgnummerDuplicateValidator.bind(this)]),
      epost: new UntypedFormControl("", [Validators.email, Validators.required], ),
      telefon: new UntypedFormControl("", Validators.required),
      kontaktperson: new UntypedFormControl(""),
      createMore: new UntypedFormControl(this.createMore)
    });
  }

  nameDuplicateValidator(input: UntypedFormControl) {
    if (this.currentKunder) {
      const alreadyExists = this.currentKunder.find(kund => kund.namn === input.value);
      if (alreadyExists) {
        return {"alreadyExists": true};
      } else {
        return null;
      }
    }
  }

  orgnummerDuplicateValidator(input: UntypedFormControl) {
    if (this.currentKunder) {
      const alreadyExists = this.currentKunder.find(kund => kund.id === input.value);
      if (alreadyExists) {
        return {"alreadyExists": true};
      } else {
        return null;
      }
    }
  }

  onSave() {
    const controls = this.form.controls;

    const kund: KundInfo = {
      namn: controls.namn.value,
      organisationsnummer: controls.organisationsnummer.value,
      epost: controls.epost.value,
      telefon: controls.telefon.value,
      kontaktperson: controls.kontaktperson.value
    };

    this.createMore = controls.createMore.value;
    this.submit.emit({kund: kund, createMore: controls.createMore.value});
  }

  canSave() {
    return this._form.valid && this._form.isChanged;
  }
}
