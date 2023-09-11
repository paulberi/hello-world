import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup, Validators } from "@angular/forms";
import { FormGroupChangeCheck } from "../../../../../lib/util/formGroupChangeCheck";
import { MkIntrang } from "../../model/intrang";
export class MkIntrangPresenter {

  onSubmit = new EventEmitter<number>();

  private _form: FormGroupChangeCheck;
  get form(): UntypedFormGroup {
    return this._form;
  }

  initializeForm(intrang: MkIntrang, ersattning: number) {
    intrang = { ...intrang };
    this._form = new FormGroupChangeCheck({
      luftstrak: new UntypedFormControl(Math.ceil(intrang.luftstrak)),
      markstrak: new UntypedFormControl(Math.ceil(intrang.markstrak)),
      ersattning: new UntypedFormControl(ersattning, [Validators.required])
    });
  }

  canSave(): boolean {
    return this._form.valid && this._form.isChanged;
  }

  submit() {
    const ersattning: number = this.form.controls.ersattning.value;

    this.form.markAsPristine();
    this.onSubmit.emit(ersattning);
  }
}
