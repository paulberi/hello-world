import {UntypedFormControl, UntypedFormGroup, Validators} from "@angular/forms";
import {EventEmitter} from "@angular/core";
import {MkAvtalsAction} from "../../../model/avtalAction";
import {ActionTyp} from "../../../model/actions";

export class MkAvtalActionsPresenter {

  form: UntypedFormGroup;

  change = new EventEmitter<MkAvtalsAction>();

  initializeForm(action: MkAvtalsAction) {
    this.form = new UntypedFormGroup({
      actionTyp: new UntypedFormControl(action?.actionTyp, Validators.required),
      selection: new UntypedFormControl(action?.selection, Validators.required),
      dokumentmallId: new UntypedFormControl(action?.dokumentmallId),
      avtalsstatus: new UntypedFormControl(action?.avtalsstatus)
    });

    this.form.valueChanges.subscribe(form => {
      if (form.actionTyp && form.actionTyp === ActionTyp.STATUS) {
        this.form.controls["dokumentmallId"].clearValidators();
        this.form.controls["avtalsstatus"].setValidators(Validators.required);
      } else if (form.actionTyp && (form.actionTyp === ActionTyp.INFOBREV || form.actionTyp === ActionTyp.MARKUPPLATELSEAVTAL)) {
        this.form.controls["avtalsstatus"].clearValidators();
        this.form.controls["dokumentmallId"].setValidators(Validators.required);
      } else {
        this.form.controls["avtalsstatus"].clearValidators();
        this.form.controls["dokumentmallId"].clearValidators();
      }

      this.change.emit(this.form.getRawValue());
    });
  }

  setDisabled(isDisabled: boolean) {
    if (!this.form) {
      return;
    } else if (isDisabled) {
      this.form.disable();
    } else {
      this.form.enable();
    }
  }

  isValid() {
    return this.form.valid;
  }
}
