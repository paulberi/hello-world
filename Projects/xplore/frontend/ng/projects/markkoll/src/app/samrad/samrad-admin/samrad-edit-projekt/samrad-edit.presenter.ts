import { EventEmitter } from "@angular/core";
import { FormControl, Validators } from "@angular/forms";
import { debounceTime } from "rxjs/operators";
import { ProjektRsp as Projekt } from "../../../../../../../generated/samrad-api";
import { FormGroupChangeCheck } from "../../../../../../lib/util/formGroupChangeCheck";

export class SamradProjektinformationPresenter {
  form: FormGroupChangeCheck;

  updateProjekt = new EventEmitter<Projekt>();
  canSave = new EventEmitter<boolean>();

  initializeForm(samrad: Projekt) {
    this.form = new FormGroupChangeCheck(
      {
        rubrik: new FormControl(samrad?.rubrik, Validators.required),
        slug: new FormControl(samrad?.slug, [
          Validators.required,
          Validators.pattern("^[a-zA-Z0-9]+(?:-[a-zA-Z0-9]+)*$"),
        ]),
        brodtext: new FormControl(samrad?.brodtext ? samrad?.brodtext : '', Validators.required),
      },
      {
        updateOn: "change",
      }
    );
    this.form.valueChanges
      .pipe(debounceTime(500))
      .subscribe(() => this.onEvents());
  }

  onEvents() {
    this.updateProjekt.emit(this.form.value);
    this.canSave.emit(this.form.isChanged && this.form.valid);
  }

  onSubmit() {
    this.form.markAsPristine();
    this.onEvents();
  }
}
