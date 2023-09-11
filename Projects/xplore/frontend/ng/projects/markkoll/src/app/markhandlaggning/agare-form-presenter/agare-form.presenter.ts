import { EventEmitter } from "@angular/core";
import { AbstractControl, FormControl, UntypedFormControl, UntypedFormGroup, ValidationErrors, ValidatorFn, Validators } from "@angular/forms";
import moment, { Moment } from "moment";
import { Agartyp, Avtalsstatus } from "../../../../../../generated/markkoll-api";
import { FormGroupChangeCheck } from "../../../../../lib/util/formGroupChangeCheck";
import { COUNTRIES_SE } from "../../common/country-select/countries.se";
import { MkAgare } from "../../model/agare";

export class MkAgareFormPresenter {
  submit = new EventEmitter<MkAgare>();

  private _form: FormGroupChangeCheck;
  get form(): UntypedFormGroup {
    return this._form;
  }

  initializeForm(agare: MkAgare) {
    const PHONE_PATTERN = /^[0-9\+\-\ ]*$/;
    const ANDEL_PATTERN = /^\d+\/\d+$/;

    this._form = new FormGroupChangeCheck({
      id: new UntypedFormControl(agare?.id),
      namn: new UntypedFormControl(agare?.namn, [nameValidator()]),
      adress: new UntypedFormControl(agare?.adress || ""),
      postnummer: new UntypedFormControl(agare?.postnummer || ""),
      ort: new UntypedFormControl(agare?.ort || ""),
      land: new FormControl(this.findLand(agare?.land) || ""),
      telefon: new UntypedFormControl(agare?.telefon, [Validators.pattern(PHONE_PATTERN)]),
      ePost: new UntypedFormControl(agare?.ePost, { validators: [Validators.email], updateOn: "blur" }),
      status: new UntypedFormControl(agare?.status || Avtalsstatus.EJBEHANDLAT),
      inkluderaIAvtal: new UntypedFormControl(agare?.inkluderaIAvtal == null ? true : agare.inkluderaIAvtal),
      andel: new UntypedFormControl(agare?.andel || "", [Validators.pattern(ANDEL_PATTERN), andelValidator()]),
      labels: new UntypedFormControl(agare?.labels),
      kontaktperson: new UntypedFormControl(agare?.kontaktperson || false),
      bankkonto: new UntypedFormControl(agare?.bankkonto || ""),
      fodelsedatumEllerOrgnummer: new UntypedFormControl(agare?.fodelsedatumEllerOrgnummer),
      agartyp: new UntypedFormControl(agare?.agartyp || Agartyp.OMBUD),
      utbetalningsdatum: new UntypedFormControl(agare?.utbetalningsdatum || this.utbetalningsdatum(moment()))
    },
      {
        validators: [this.utbetalningsdatumValidator()]
      });
  }

  private findLand(land: string) {
    const landLower = land?.toLowerCase();
    return COUNTRIES_SE.find(c => c.name.toLowerCase() === landLower);
  }

  canSave(): boolean {
    return this._form?.valid && this._form?.isChanged;
  }

  onSubmit() {
    const controls = this.form.controls;
    const status = controls.status.value;

    const utbetalningsdatum = status === Avtalsstatus.ERSATTNINGUTBETALD ?
      controls.utbetalningsdatum.value : null;

    const agare: MkAgare = {
      id: controls.id.value,
      namn: controls.namn.value,
      adress: controls.adress.value,
      postnummer: controls.postnummer.value,
      ort: controls.ort.value,
      land: controls.land.value?.name,
      telefon: controls.telefon.value,
      ePost: controls.ePost.value,
      status: status,
      inkluderaIAvtal: controls.inkluderaIAvtal.value,
      andel: controls.andel.value.replace(/\s/g, ""),
      labels: controls.labels.value,
      kontaktperson: controls.kontaktperson.value,
      bankkonto: controls.bankkonto.value,
      fodelsedatumEllerOrgnummer: controls.fodelsedatumEllerOrgnummer.value,
      agartyp: controls.agartyp.value,
      utbetalningsdatum: utbetalningsdatum
    };

    this.submit.emit(agare);
  }

  private utbetalningsdatum(datetime: Moment): string {
    // Vi kapar tidszonen så vi inte riskerar att få fel datum vid konvertering från GMT+1 till GMT...
    return datetime.format("YYYY-MM-DD");
  }

  private utbetalningsdatumValidator(): ValidatorFn {
    return (form: AbstractControl) => {
      const utbetalningsdatum = form.get("utbetalningsdatum").value;
      const status = form.get("status").value;

      if (status === Avtalsstatus.ERSATTNINGUTBETALD) {
        if (utbetalningsdatum == null) {
          return { utbetalningsdatumMissing: true };
        } else {
          return null;
        }
      } else {
        return null;
      }
    };
  }
}

function nameValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const name = control.value;
    return (!name || name.trim().length === 0) ? { invalidName: { value: control.value } } : null;
  };
}

function andelValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const andel = control.value;
    var split = andel.split('/');
    return (split[0] > split[1]) ? { invalidAndel: { value: control.value } } : null;
  };
}
