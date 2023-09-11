import { EventEmitter } from "@angular/core";
import { UntypedFormGroup, UntypedFormControl, Validators } from "@angular/forms";
import { DokumentTyp } from "../../../../../../../generated/markkoll-api";
import { SaveProjektdokument } from "./projektdokument.component";

export class MkDokumentCreateFormPresenter {
  form: UntypedFormGroup;

  dokumentCreate = new EventEmitter<SaveProjektdokument>();

  initializeForm(dokumentTyp: DokumentTyp) {
    this.form = new UntypedFormGroup({
      name: new UntypedFormControl("", Validators.required),
      dokumenttyp: new UntypedFormControl(dokumentTyp, Validators.required),
      default: new UntypedFormControl(false),
      file: new UntypedFormControl([], Validators.required)
    });
  }

  createDokument() {
    const projektdokumentInfo: SaveProjektdokument = {
      file: this.form.controls.file.value[0],
      projektdokumentInfo: {
        namn: this.form.controls.name.value,
        dokumenttyp: this.form.controls.dokumenttyp.value,
        selected: this.form.controls.default.value
      }
    };
    this.dokumentCreate.emit(projektdokumentInfo);
  }
}
