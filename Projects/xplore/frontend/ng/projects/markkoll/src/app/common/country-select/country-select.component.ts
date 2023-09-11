import { Component, forwardRef, Input } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { COUNTRIES_SE, Country } from "./countries.se";

@Component({
  selector: 'mk-country-select',
  templateUrl: './country-select.component.html',
  styleUrls: ['./country-select.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    multi: true,
    useExisting: forwardRef(() => MkCountrySelectComponent)
  }]
})
export class MkCountrySelectComponent implements ControlValueAccessor {

  @Input() value: Country;

  readonly countries = COUNTRIES_SE;

  readonly compareWith = (c1: Country, c2: Country) =>
    c1 && c2 ? c1.name?.toLowerCase() === c2.name?.toLowerCase() : c1 === c2;

  private onChange = (value: number) => {};
  private onTouched = () => {};

  constructor() { }

  onSelectionChange(value) {
    this.value = value;
    this.onChange(value);
  }

  writeValue(obj: any): void {
    this.value = obj;
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    throw new Error("Method not implemented.");
  }
}
