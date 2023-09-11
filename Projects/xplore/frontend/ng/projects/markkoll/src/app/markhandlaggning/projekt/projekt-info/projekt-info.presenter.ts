import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup, Validators } from "@angular/forms";
import moment from "moment";
import { ProjektInfo, Utskicksstrategi } from "../../../../../../../generated/markkoll-api";
export class MkProjektInfoPresenter {

  change = new EventEmitter<ProjektInfo>();
  valid = new EventEmitter<boolean>(true);

  private _form: UntypedFormGroup;

  get form(): UntypedFormGroup {
    return this._form;
  }

  initializeForm(isReadonly: boolean, projektTyper: string[], projektInfo?: ProjektInfo, ) {

    this._form = new UntypedFormGroup({
      namn: new UntypedFormControl({value: projektInfo?.namn || "", disabled: isReadonly}, Validators.required),
      projektTyp: new UntypedFormControl({ value: projektTyper.length === 1 ? projektTyper[0] : projektInfo?.projektTyp || "", disabled: isReadonly}, Validators.required, ),
      ort: new UntypedFormControl({value: projektInfo?.ort || "", disabled: isReadonly}),
      startDatum: new UntypedFormControl({value: projektInfo?.startDatum || "", disabled: isReadonly}),
      beskrivning: new UntypedFormControl({value: projektInfo?.beskrivning || "", disabled: isReadonly}),
      utskicksstrategi: new UntypedFormControl({value: projektInfo?.utskicksstrategi || Utskicksstrategi.ADRESS, disabled: isReadonly}, Validators.required),
      uppdragsnummer: new UntypedFormControl({value: projektInfo?.uppdragsnummer, disabled: isReadonly}),
      kundId: new UntypedFormControl({value: projektInfo?.kundId, disabled: isReadonly})
    });
    this.registerOnChange();

    if (projektTyper.length === 1) {
     this._form.get("projektTyp").disable();
    };
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

  getFormData(): ProjektInfo {
    const controls = this.form.controls;
    const projektInfo: ProjektInfo = {
      namn: controls.namn.value,
      projektTyp: controls.projektTyp.value,
      ort: controls.ort.value,
      startDatum: moment(controls.startDatum.value).utcOffset(0, true).isValid()
      ? moment(controls.startDatum.value).utcOffset(0, true).format() : null,
      beskrivning: controls.beskrivning.value,
      utskicksstrategi: controls.utskicksstrategi.value,
      uppdragsnummer: controls.uppdragsnummer.value,
      kundId: controls.kundId.value
    };
    return projektInfo;
  }
}
