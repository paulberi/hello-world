import { UntypedFormGroup, UntypedFormControl } from "@angular/forms";

export class MkDokumentRadioFormPresenter {
  form: UntypedFormGroup;

  initializeForm() {
    this.form = new UntypedFormGroup({
      template: new UntypedFormControl(false)
    });
  }
}
