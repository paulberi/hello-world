import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup, Validators } from "@angular/forms";

export interface FiberVarderingConfigCreateData {
  namn: string;
  personEllerOrgNummer: string;
}

export class MkErsattningFiberPresenter {
  form: UntypedFormGroup;

  fiberVarderingConfigCreate = new EventEmitter<FiberVarderingConfigCreateData>();

  initializeForm() {
    this.form = new UntypedFormGroup({
      namn: new UntypedFormControl("", Validators.required),
      personEllerOrgNummer: new UntypedFormControl("", Validators.required),
      });
  }

  submit() {
    const data: FiberVarderingConfigCreateData = {
      namn: this.form.controls.namn.value,
      personEllerOrgNummer: this.form.controls.personEllerOrgNummer.value,
    };
    this.fiberVarderingConfigCreate.emit(data);
  }
}
