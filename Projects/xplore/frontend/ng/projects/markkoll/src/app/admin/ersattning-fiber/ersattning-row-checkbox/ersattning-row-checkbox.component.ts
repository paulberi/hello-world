import { CommonModule } from "@angular/common";
import { Component, forwardRef, Input, ViewChild } from "@angular/core";
import { NG_VALUE_ACCESSOR } from "@angular/forms";
import { MatCheckboxModule } from "@angular/material/checkbox";

@Component({
  selector: "mk-ersattning-row-checkbox",
  templateUrl: "./ersattning-row-checkbox.component.html",
  styleUrls: ["./ersattning-row-checkbox.component.scss"],
  standalone: true,
  imports: [
    CommonModule,
    MatCheckboxModule,
  ],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    multi: true,
    useExisting: forwardRef(() => MkErsattningRowCheckboxComponent)
  }]
})
export class MkErsattningRowCheckboxComponent {
  @Input() title: string;

  @Input() isFocused = false;

  @Input() value = true;

  touched = false;

  disabled = false;

  onChange = (value: boolean) => {};

  onTouched = () => {};

  writeValue(value: boolean) {
    this.value = value;
  }

  registerOnChange(fn: (_: boolean) => void): void {
    this.onChange = (value: boolean) => {
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

  onCheckboxChange(value: boolean) {
    this.value = value;
    this.onChange(value);
  }

  onFocus() {
    this.isFocused = true;
  }

  onFocusOut() {
    this.isFocused = false;
  }
}
