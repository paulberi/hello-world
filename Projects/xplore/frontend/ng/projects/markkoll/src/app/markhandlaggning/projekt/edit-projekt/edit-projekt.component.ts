import {Component, forwardRef, Input, OnChanges, OnInit, Output, SimpleChanges} from "@angular/core";
import {
  Avtalsinstallningar,
  Beredare,
  ProjektTyp,
  Utskicksstrategi,
} from "../../../../../../../generated/markkoll-api";
import { AbstractControl, ControlValueAccessor, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR, ValidationErrors } from "@angular/forms";
import { MkEditProjektPresenter } from "./edit-projekt.presenter";
import { Projekt } from "../../../model/projekt";
import { UserRoleEntry } from "../user-roles/user-roles.component";
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from "@angular/material/core";
import { MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter } from "@angular/material-moment-adapter";

export interface UpdateProjektEvent {
  projekt: Projekt;
  users: UserRoleEntry[];
  beredare: Beredare;
  avtalsinstallningar: Avtalsinstallningar;
}
export const DATE_FORMATS = {
  parse: {
    dateInput: 'YYYY-MM-DD',
  },
  display: {
    dateInput: 'YYYY-MM-DD',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@Component({
  selector: "mk-edit-projekt",
  templateUrl: "./edit-projekt.component.html",
  providers: [
    { provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE,
      MAT_MOMENT_DATE_ADAPTER_OPTIONS] },
    { provide: MAT_MOMENT_DATE_ADAPTER_OPTIONS, useValue: { useUtc: true } },
    MkEditProjektPresenter,
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => MkEditProjektComponent),
      multi: true
    },
    {
      provide: NG_VALIDATORS,
      useExisting: MkEditProjektComponent,
      multi: true
    },
    {
      provide: MAT_DATE_FORMATS,
      useValue: DATE_FORMATS
    }],
  styleUrls: ["edit-projekt.component.scss"],
})
export class MkEditProjektComponent implements OnInit, OnChanges, ControlValueAccessor {

  @Input() projekt: Projekt;

  @Input() ledningsagareOptions: string[] = [];

  @Output() projektChange = this.presenter.projektChange;

  readonly utskicksstrategiOptions = Object.keys(Utskicksstrategi);

  private _onChange: any;
  private _onTouched: any;

  constructor(private presenter: MkEditProjektPresenter) {}

  validate(control: AbstractControl): ValidationErrors | null {
    return this.presenter.form?.valid ? null : {invalid: true};
  }

  writeValue(obj: any) {
    this.projekt = obj;
    this.presenter.initializeForm(this.projekt);
  }

  registerOnChange(fn: any) {
    this._onChange = fn;
  }

  registerOnTouched(fn: any) {
    this._onTouched = fn;
  }

  setDisabledState(isDisabled: boolean) {
  }

  ngOnInit() {
    this.presenter.initializeForm(this.projekt);
    this.projektChange.subscribe(projekt => this._onChange(projekt));
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.projektInfo) {
      this.presenter.initializeForm(this.projekt);
    }
  }

  get form(): FormGroup {
    return this.presenter.form;
  }

  isFiberProjekt(): boolean {
    return this.projekt?.projektInfo?.projektTyp === ProjektTyp.FIBER;
  }

  projektTypOptions(): ProjektTyp[] {
    const fiberTyper = [ProjektTyp.FIBER];
    const elnatTyper = [ProjektTyp.LOKALNAT, ProjektTyp.REGIONNAT];

    if (fiberTyper.includes(this.projekt?.projektInfo?.projektTyp)) {
      return fiberTyper;
    } else if (elnatTyper.includes(this.projekt?.projektInfo?.projektTyp)) {
      return elnatTyper;
    } else {
      return [];
    }
  }
}
