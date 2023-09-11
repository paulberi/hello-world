import {UntypedFormControl, UntypedFormGroup} from "@angular/forms";
import {EventEmitter} from "@angular/core";

export class XpPrefixedSelectionPresenter {
  form: UntypedFormGroup;

  selectionChange = new EventEmitter<string>();

  initializeForm(selection: string, disabled: boolean) {
    this.form = new UntypedFormGroup({
      selectionControl: new UntypedFormControl({value: selection, disabled: disabled})
    });

    this.form.valueChanges.subscribe(form => {
      this.selectionChange.emit(form.selectionControl);
    });
  }

  reset() {
    this.form.controls["selectionControl"].setValue("");
  }
}
