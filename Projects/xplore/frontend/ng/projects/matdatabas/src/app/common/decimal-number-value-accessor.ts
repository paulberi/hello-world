import {Directive, ElementRef, Renderer2, forwardRef} from "@angular/core";
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from "@angular/forms";

export const DECIMAL_NUMBER_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => DecimalNumberValueAccessor),
  multi: true
};

/**
 * The sole purpose of the directive is an apparent bug that won't let you type negative number with a comma as a
 * decimal separator, when under a Swedish locale (at least on macOS).
 *
 * Use like this:
 * <input type="decimal-number" autocomplete="off" matInput placeholder="..." required formControlName="...">
 *
 * The only difference is "decimal-number" instead of "number".
 *
 * Adapted from Angulars own NumberValueAccessor.
 */
@Directive({
  selector:
    "input[type=decimal-number][formControlName]",
  host: {
    "(change)": "onChange($event.target.value)",
    "(input)": "onChange($event.target.value)",
    "(blur)": "onTouched()"
  },
  providers: [DECIMAL_NUMBER_VALUE_ACCESSOR]
})
export class DecimalNumberValueAccessor implements ControlValueAccessor {
  /**
   * @description
   * The registered callback function called when a change or input event occurs on the input
   * element.
   */
  onChange = (_: any) => {
  };

  /**
   * @description
   * The registered callback function called when a blur event occurs on the input element.
   */
  onTouched = () => {
  };

  constructor(private _renderer: Renderer2, private _elementRef: ElementRef) {
  }

  /**
   * Sets the "value" property on the input element.
   *
   * @param value The checked value
   */
  writeValue(value: number): void {
    // The value needs to be normalized for IE9, otherwise it is set to 'null' when null
    const normalizedValue = value == null ? "" : value.toString().replace(".", ",");
    this._renderer.setProperty(this._elementRef.nativeElement, "value", normalizedValue);
  }

  /**
   * @description
   * Registers a function called when the control value changes.
   *
   * @param fn The callback function
   */
  registerOnChange(fn: (_: number | null) => void): void {
    this.onChange = (value) => {
      // console.log("parsing value: " + value);
      if (typeof value === "string" && value !== "") {
        const n = Number(value.replace(",", "."));
        fn(isNaN(n) ? null : n);
      } else {
        fn(null);
      }
    };
  }

  /**
   * @description
   * Registers a function called when the control is touched.
   *
   * @param fn The callback function
   */
  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  /**
   * Sets the "disabled" property on the input element.
   *
   * @param isDisabled The disabled value
   */
  setDisabledState(isDisabled: boolean): void {
    this._renderer.setProperty(this._elementRef.nativeElement, "disabled", isDisabled);
  }
}
