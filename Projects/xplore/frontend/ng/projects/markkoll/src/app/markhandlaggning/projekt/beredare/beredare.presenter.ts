import { EventEmitter } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Beredare } from "../../../../../../../generated/markkoll-api";

export class MkBeredarePresenter {
  form: FormGroup;
  beredareChange = new EventEmitter<Beredare>();

  initializeForm(beredare: Beredare) {
    this.form = new FormGroup({
      namn: new FormControl(beredare?.namn),
      telefonnummer: new FormControl(beredare?.telefonnummer),
      adress: new FormControl(beredare?.adress),
      postnummer: new FormControl(beredare?.postnummer),
      ort: new FormControl(beredare?.ort)
    });

    this.form.valueChanges.subscribe(value => this.beredareChange.emit(value));
  }

  setForm(beredare: Beredare) {
    this.form.setValue(beredare);
  }
}
