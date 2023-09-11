import { UntypedFormControl, UntypedFormGroup } from "@angular/forms";
import { EventEmitter } from "@angular/core";
import { TillvaratagandeTyp } from "../../../../../../generated/markkoll-api";
import { data } from "jquery";

export class MkVirkesinlosenPresenter {
  form: UntypedFormGroup;

  skogligVarderingChange = new EventEmitter<boolean>();
  tillvaratagandeTypChange = new EventEmitter<TillvaratagandeTyp>();

  initializeForm(skogsfastighet: boolean,
                 tillvaratagandeTyp: TillvaratagandeTyp,
                 rotnetto: number,
                 egetTillvaratagande: number) {

    this.form = new UntypedFormGroup({
      skogligVardering: new UntypedFormControl(skogsfastighet),
      tillvaratagandeTyp: new UntypedFormControl(tillvaratagandeTyp),
      rotnetto: new UntypedFormControl(rotnetto),
      egetTillvaratagande: new UntypedFormControl(egetTillvaratagande),
    });

    this.setDisabledControls(tillvaratagandeTyp);

    this.form.controls.skogligVardering.valueChanges
        .subscribe(sv => this.skogligVarderingChange.emit(sv));

    this.form.controls.tillvaratagandeTyp.valueChanges
        .subscribe(tvt => this.tillvaratagandeTypChange.emit(tvt));

    this.form.controls.tillvaratagandeTyp.valueChanges
        .subscribe(tvt => this.setDisabledControls(tvt));
  }

  private setDisabledControls(tillvaratagandeTyp: TillvaratagandeTyp) {
    this.form.controls.rotnetto.disable();
    this.form.controls.egetTillvaratagande.disable();

    switch (tillvaratagandeTyp) {
      case TillvaratagandeTyp.EGETTILLVARATAGANDE:
        this.form.controls.egetTillvaratagande.enable();
        break;
      case TillvaratagandeTyp.ROTNETTO:
        this.form.controls.rotnetto.enable();
        break;
    }
  }

  tillvaratagandeTyp(): string {
    return this.form?.controls?.tillvaratagandeTyp?.value || TillvaratagandeTyp.EJBESLUTAT;
  }
}
