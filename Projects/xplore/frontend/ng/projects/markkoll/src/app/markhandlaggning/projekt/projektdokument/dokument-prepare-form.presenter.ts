import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup, Validators } from "@angular/forms";

export class MkDokumentPrepareFormPresenter {
  form: UntypedFormGroup;

  dokumentPrepare = new EventEmitter<File>();

  initializeForm() {
    this.form = new UntypedFormGroup({
      file: new UntypedFormControl([], Validators.required)
    });
  }

  prepareDokument() {
    const file = this.form.controls.file.value[0];
    this.dokumentPrepare.emit(file);
  }
}
