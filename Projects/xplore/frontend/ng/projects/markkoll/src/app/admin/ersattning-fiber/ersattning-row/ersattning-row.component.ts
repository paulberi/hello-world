import { Component, Input, forwardRef } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";

@Component({
  selector: "mk-ersattning-row",
  templateUrl: "./ersattning-row.component.html",
  styleUrls: ["./ersattning-row.component.scss"],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    multi: true,
    useExisting: forwardRef(() => MkErsattningRowComponent)
  }]
})
export class MkErsattningRowComponent implements ControlValueAccessor {
  isFocused = false;

  @Input() title;

  @Input() value = 0;

  @Input() isPercentValue = false;


  touched = false;

  disabled = false;

  onChange = (value: number) => {};

  onTouched = () => {};

  writeValue(value: number) {
    this.value = value as number;
  }

  registerOnChange(fn: (_: number) => void): void {
    this.onChange = (value: number) => {
      fn(value);
    };
}

  registerOnTouched(fn: any) {
    this.onTouched = fn;
  }

  markAsTouched() {
    if (!this.touched) {
      this.onTouched();
      this.touched = true;
    }
  }

  setDisabledState(disabled: boolean) {
    this.disabled = disabled;
  }

  onValueChange(value: any) {
    this.onChange(value.target.value as number);
  }

  focus() {
    this.isFocused = true;
  }
  focusout() {
    this.isFocused = false;
  }

}
