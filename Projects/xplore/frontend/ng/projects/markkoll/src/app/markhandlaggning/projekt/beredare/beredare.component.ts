import { Component, forwardRef, Input, OnChanges, OnInit, Output, SimpleChanges } from "@angular/core";
import { AbstractControl, ControlValueAccessor, NG_VALIDATORS, NG_VALUE_ACCESSOR, ValidationErrors } from "@angular/forms";
import { Beredare } from "../../../../../../../generated/markkoll-api";
import { MkBeredarePresenter } from "./beredare.presenter";

@Component({
  selector: "mk-beredare",
  templateUrl: "./beredare.component.html",
  styleUrls: ["./beredare.component.scss"],
  providers: [
    MkBeredarePresenter,
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => MkBeredareComponent),
      multi: true
    },
    {
      provide: NG_VALIDATORS,
      useExisting: MkBeredareComponent,
      multi: true
    }
  ]
})
export class MkBeredareComponent implements OnInit, OnChanges, ControlValueAccessor {
  @Input() beredare: Beredare;

  @Output() beredareChange = this.presenter.beredareChange;

  _onChange = (_) => {};
  _onTouch = (_) => {};

  constructor(private presenter: MkBeredarePresenter) {}

  get form() {
    return this.presenter.form;
  }

  ngOnInit() {
    this.presenter.initializeForm(this.beredare);
    this.beredareChange.subscribe(b => this._onChange(b));
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.beredare && !changes.beredare.isFirstChange()) {
      this.presenter.setForm(this.beredare);
    }
  }

  registerOnChange(fn: any): void {
    this._onChange = fn;
    this.beredareChange.subscribe(b => this._onChange(b));
  }

  registerOnTouched(fn: any): void {
    this._onTouch = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
  }

  validate(control: AbstractControl): ValidationErrors | null {
    return this.presenter.form?.valid ? null : {invalid: true};
  }

  writeValue(beredare: any): void {
    this.beredare = beredare;
  }
}
