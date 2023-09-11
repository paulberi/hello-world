import { EventEmitter } from "@angular/core";
import { UntypedFormGroup, UntypedFormControl } from "@angular/forms";
import { ImportTyp } from "../../../model/importTyp";

export class MkProjektimporterPresenter {
  form: UntypedFormGroup;
  importTypChange = new EventEmitter<string>();

  initializeForm(importType: string) {
    this.form = new UntypedFormGroup({
      importType: new UntypedFormControl(importType || ImportTyp.VERSION)
    });
    this.form.valueChanges.subscribe(form => this.importTypChange.emit(form.importType));
  }
}
