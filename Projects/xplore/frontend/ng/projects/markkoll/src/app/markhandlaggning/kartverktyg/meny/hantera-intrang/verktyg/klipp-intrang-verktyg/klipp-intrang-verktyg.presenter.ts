import { AbstractControl, FormControl, FormGroup, Validators } from "@angular/forms";
import { IntrangOption } from "../intrang-form/intrang-form.component";

export class KlippIntrangVerktygPresenter {
  form: FormGroup = this.createForm([]);

  getFormValue(): IntrangOption[] {
    const intrangArray = [this.form.controls.intrang1.value, this.form.controls.intrang2.value];
    return intrangArray;
  }

  setFormValue(intrangOptions: IntrangOption[]) {
    this.setOptionFormValue(this.form.controls.intrang1, intrangOptions[0]);
    this.setOptionFormValue(this.form.controls.intrang2, intrangOptions[1]);
  }

  private createForm(intrangOptions: IntrangOption[]): FormGroup {
    return new FormGroup({
      intrang1: this.createOptionForm(intrangOptions[0]),
      intrang2: this.createOptionForm(intrangOptions[1])
    });
  }

  private createOptionForm(intrangOption: IntrangOption): FormGroup {
    return new FormGroup({
      id: new FormControl(intrangOption?.id || "-"),
      geom: new FormControl(intrangOption?.geom || "-"),
      intrangstyp: new FormControl(intrangOption?.intrangstyp || "-", Validators.required),
      subtyp: new FormControl(intrangOption?.subtyp || "-", Validators.required),
      avtalstyp: new FormControl(intrangOption?.avtalstyp || "-", Validators.required),
    });
  }

  private setOptionFormValue(control: AbstractControl, intrangOption: IntrangOption) {
    control.setValue({
      id: intrangOption?.id || "-",
      geom: intrangOption?.geom || "-",
      intrangstyp: intrangOption?.intrangstyp || "-",
      subtyp: intrangOption?.subtyp || "-",
      avtalstyp: intrangOption?.avtalstyp || "-"
    });
  }
}
