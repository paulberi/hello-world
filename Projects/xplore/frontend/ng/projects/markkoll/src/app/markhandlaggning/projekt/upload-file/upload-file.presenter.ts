import { EventEmitter } from "@angular/core";
import { UntypedFormControl, UntypedFormGroup, Validators } from "@angular/forms";
import { tap } from "rxjs/operators";
import { IndataTyp, NisKalla, ProjektTyp } from "../../../../../../../generated/markkoll-api";

export interface MkUploadFile {
  files: File[];
  indataTyp: IndataTyp;
  buffert: number;
}

export class MkUploadFilePresenter {

  change = new EventEmitter<MkUploadFile>();
  valid = new EventEmitter<boolean>();

  private _form: UntypedFormGroup;
  private _indataTyper: IndataTyp[];

  get form(): UntypedFormGroup {
    return this._form;
  }

  get indataTyper(): IndataTyp[] {
    return this._indataTyper;
  }

  initializeForm(disableValidation: boolean, nisKalla: NisKalla, projektTyp: ProjektTyp) {
    this._form = new UntypedFormGroup({
      files: new UntypedFormControl([], disableValidation ? null : Validators.required),
      indataTyp: new UntypedFormControl(this._indataTyper.length === 1 ? this._indataTyper[0] : null, disableValidation ? null : Validators.required),
      buffert: new UntypedFormControl(0, disableValidation ? null : Validators.required)
    });

    this.filterIndataTyper(projektTyp, nisKalla);
    this.registerOnChange();
  }

  filterIndataTyper(projektTyp: ProjektTyp, nisKalla: NisKalla) {
    let values = [];
    if (this.isFiberProjekt(projektTyp)) {
      if (nisKalla?.dpCom) {
        values.push(IndataTyp.DPCOM);
      }
      if (values.length === 0) {
        values = [IndataTyp.DPCOM];
      }
    } else if (this.isElnatProjekt(projektTyp)) {
      if (nisKalla?.dpPower) {
        values.push(IndataTyp.DPPOWER);
      }
      if (nisKalla?.trimble) {
        values.push(IndataTyp.TRIMBLE);
      }
      if (values.length === 0) {
        values = [IndataTyp.DPPOWER, IndataTyp.TRIMBLE];
      }
    } else {
      values = [IndataTyp.DPCOM, IndataTyp.DPPOWER];
    }
    this.updateIndataTyp(values);
    this._indataTyper = values;
    this.isIndataTypDisabled();
  }

  canSave(): boolean {
    return this._form.valid;
  }

  registerOnChange() {
    this._form.valueChanges.subscribe((value) => {
      this.change.emit(this.getFormData());
      this.valid.emit(this.canSave());
    });

    // Vilkorad validering, om en fil har laddats upp måste indataTyp sättas
    this._form.get("files").valueChanges.pipe(
      tap((files: File[]) => {
        if (files.length > 0) {
          this._form.get("indataTyp").setValidators(Validators.required);
        } else {
          this._form.get("indataTyp").clearValidators();
          this._form.get("indataTyp").reset();
        }
        this._form.get("indataTyp").updateValueAndValidity({ onlySelf: true });
      })
    ).subscribe();
  }

  getFormData(): MkUploadFile {
    const files: File[] = this.form.controls.files.value;
    const indataTyp: IndataTyp = this.form.controls.indataTyp.value;
    const buffert: number = this.form.controls.buffert.value;
    return {
      files: files,
      indataTyp: indataTyp,
      buffert: buffert
    };
  }

  private isIndataTypDisabled() {
    this._indataTyper.length === 1 ? this._form?.controls['indataTyp'].disable() : this._form?.controls['indataTyp'].enable();
  }

  private updateIndataTyp(indataTyper: IndataTyp[]) {
    if (indataTyper.length === 1) {
      this._form?.controls.indataTyp.setValue(indataTyper[0]);
    } else {
      this._form?.controls.indataTyp.setValue(null);
    }

  }

  private isFiberProjekt(projektTyp: ProjektTyp): boolean {
    return projektTyp === ProjektTyp.FIBER;
  }

  private isElnatProjekt(projekttyp: ProjektTyp): boolean {
    return (projekttyp === ProjektTyp.LOKALNAT) ||
      (projekttyp === ProjektTyp.REGIONNAT);
  }
}
