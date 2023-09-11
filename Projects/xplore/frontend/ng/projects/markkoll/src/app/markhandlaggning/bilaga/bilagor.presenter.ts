import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup, Validators } from "@angular/forms";
import { BilagaTyp } from "../../../../../../generated/markkoll-api";

export interface MkAddBilagaEvent {
  fil: Blob;
  kategori: BilagaTyp;
}

export class MkBilagorPresenter {
  addBilaga = new EventEmitter<MkAddBilagaEvent>();
  form: UntypedFormGroup;

  initializeForm() {
    this.form = new UntypedFormGroup({
      fil: new UntypedFormControl([], [Validators.required]),
      kategori: new UntypedFormControl(null, [Validators.required])
    });
  }

  resetForm() {
    this.form.reset();

    // Nollställ validator, så den inte blir röd och ful när man nollställer
    this.form.controls.kategori.setErrors(null);
  }

  canSave(): boolean {
    return this.form.valid;
  }

  submit() {
    this.addBilaga.emit({
      fil: this.form.controls.fil.value[0],
      kategori: this.form.controls.kategori.value
    });
  }
}
