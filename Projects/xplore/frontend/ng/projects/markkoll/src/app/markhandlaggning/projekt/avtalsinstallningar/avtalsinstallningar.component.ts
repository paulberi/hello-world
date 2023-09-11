import { Component, forwardRef, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { NG_VALUE_ACCESSOR, NG_VALIDATORS, ControlValueAccessor, AbstractControl, ValidationErrors, FormsModule, ReactiveFormsModule, FormGroup, Validator } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatRadioModule, MAT_RADIO_DEFAULT_OPTIONS } from "@angular/material/radio";
import { MatSelectModule } from "@angular/material/select";
import { TranslocoModule, TranslocoService } from "@ngneat/transloco";
import { AvtalMappstrategi, Avtalsinstallningar, BerakningAbel07, BerakningRev } from "../../../../../../../generated/markkoll-api";
import { AvtalsinstallningarPresenter } from "./avtalsinstallningar.presenter";

@Component({
  standalone: true,
  selector: 'mk-avtalsinstallningar',
  templateUrl: './avtalsinstallningar.component.html',
  styleUrls: ['./avtalsinstallningar.component.scss'],
  imports: [
    MatFormFieldModule,
    MatRadioModule,
    MatSelectModule,
    FormsModule,
    ReactiveFormsModule,
    TranslocoModule
  ],
  providers: [
    AvtalsinstallningarPresenter,
    {
      provide: MAT_RADIO_DEFAULT_OPTIONS,
      useValue: { color: "accent" },
    },
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => AvtalsinstallningarComponent),
      multi: true
    },
    {
      provide: NG_VALIDATORS,
      useExisting: AvtalsinstallningarComponent,
      multi: true
    },
]
})
export class AvtalsinstallningarComponent implements ControlValueAccessor, OnChanges, OnInit, Validator {

  @Input() avtalsinstallningar: Avtalsinstallningar;

  @Output() avtalsinstallningarChange = this.presenter.avtalsinstallningarChange;

  readonly BerakningRev = BerakningRev;
  readonly BerakningAbel07 = BerakningAbel07;
  readonly AvtalMappstrategi = AvtalMappstrategi;

  private _onChange: any = () => {};
  private _onTouched: any = () => {};

  constructor(private presenter: AvtalsinstallningarPresenter,
              private translate: TranslocoService) { }

  ngOnInit() {
    this.presenter.initializeForm(this.avtalsinstallningar);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.avtalsinstallningar && !changes.avtalsinstallningar.firstChange) {
      this.presenter.updateForm(changes.avtalsinstallningar.currentValue);
    }
  }

  validate(control: AbstractControl): ValidationErrors | null {
    return this.presenter.form?.valid ? null : {invalid: true};
  }

  writeValue(obj: any): void {
    this.avtalsinstallningar = obj;
  }

  registerOnChange(fn: any): void {
    this._onChange = fn;
    this.avtalsinstallningarChange.subscribe(installningar => this._onChange(installningar));
  }

  registerOnTouched(fn: any): void {
    this._onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    throw new Error("Method not implemented.");
  }

  get form(): FormGroup {
    return this.presenter.form;
  }
}
