import { Component, forwardRef, Input, OnInit } from "@angular/core";
import { ControlValueAccessor, UntypedFormGroup, UntypedFormControl, NG_VALUE_ACCESSOR, NG_VALIDATORS,
  ValidationErrors, AbstractControl } from "@angular/forms";
import { Moment } from "moment";
import moment from "moment";
import { isTouch } from "../touch-utils";
import { string } from '@amcharts/amcharts4/core';

@Component({
  selector: "mdb-datetime-picker",
  template: `
    <form [formGroup]="form">
      <mat-form-field floatLabel="always">
        <mat-label>{{label}}</mat-label>
        <input matInput
               placeholder="åååå-mm-dd"
               autocomplete="off"
               [matDatepicker]="myDatepicker"
               formControlName="date"
               (focus)="onDateFocus()"
               (focusout)="onDateFocusLost()">
        <mat-datepicker-toggle matSuffix [for]="myDatepicker"></mat-datepicker-toggle>
        <mat-hint *ngIf="hint">{{hint}}</mat-hint>
        <mat-datepicker [touchUi]="isTouch" #myDatepicker></mat-datepicker>
      </mat-form-field>

      <mat-form-field floatLabel="never">
        <input matInput placeholder="tt:mm"
          pattern="([01]?[0-9]|2[0-3]):[0-5][0-9]"
          autocomplete="off"
          formControlName="time"
          (focus)="onTimeFocus()"
          (focusout)="onTimeFocusLost()">
      </mat-form-field>
      <mat-error class="error">{{errorTextShown()}}</mat-error>
    </form>
    `,
  styles: [`
        form {
            display: grid;
            grid-template-columns: 1fr 1fr;
            grid-column-gap: 1rem;
        }

        .error {
          font-size: 10px;
        }
    `],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DatetimePickerComponent),
      multi: true
    },
    {
      provide: NG_VALIDATORS,
      useExisting: DatetimePickerComponent,
      multi: true
    },
  ]
})

export class DatetimePickerComponent implements ControlValueAccessor, OnInit {
  @Input() label: string;
  @Input() hint: string;
  @Input() errorText: string;
  @Input() isRequired = false;
  isTouch: boolean = isTouch();
  isDateFocused = false;
  isTimefocused = true;

  form: UntypedFormGroup = new UntypedFormGroup({
    date: new UntypedFormControl(),
    time: new UntypedFormControl(),
  });

  onChange: (val: Moment) => void = () => { };
  onTouch: (val: Moment) => void = () => { };

  ngOnInit() {
    this.date.valueChanges.subscribe(v => {
      if (this.time.value == null) {
        this.time.markAsUntouched();
      }
      this.invokeOnChange();
    });
    this.time.valueChanges.subscribe(v => {
      if (this.date.value == null) {
        this.date.markAsUntouched();
      }
      this.invokeOnChange();
    });
  }

  setDisabledState?(isDisabled: boolean): void {
    /* setting emitEvent to false ensures that the component will remain pristine, and not set
    off an ExpressionChangedAfterItHasBeenCheckedError */
    if (isDisabled) {
      this.date.disable({ emitEvent: false });
      this.time.disable({ emitEvent: false });
    } else {
      this.date.enable({ emitEvent: false });
      this.time.enable({ emitEvent: false });
    }
  }

  writeValue(val: Moment) {
    if (val) {
      this.date.setValue(val.format("YYYY-MM-DD"));
      this.time.setValue(val.format("HH:mm"));
    }
  }

  registerOnChange(fn: any) {
    this.onChange = fn;
  }

  registerOnTouched(fn: any) {
    this.onTouch = fn;
  }

  validate(value: UntypedFormControl): ValidationErrors | null {
    if (this.date.touched || this.time.touched) {
      this.onTouch(null);
    }

    if (this.date.invalid && !this.isDateFocused) {
      return { invalidDate: true };
    } else if (this.time.invalid && !this.isTimefocused) {
      return { invalidTime: true };
    } else if (this.hasRequiredError()) {
      return { required: true };
    } else if (this.isPartiallyFilled()) {
      return { partiallyFilled: true };
    } else {
      return null;
    }
  }

  stringIsEmpty(s: string): boolean {
    return s === "" || s == null;
  }

  isPartiallyFilled(): boolean {
    return (this.date.value == null && !this.stringIsEmpty(this.time.value)) ||
           (this.date.value != null && this.stringIsEmpty(this.time.value));
  }

  /* Implementing the ControlValueAccessor interface was an enticing endeavour, but unfortunately
  some things just don't work the way you expect them to. This includes using the 'required' html
  property. I had to introduce an isRequired input property and this check. */
  hasRequiredError(): boolean {
    if (this.isRequired) {
      return (this.date.value == null || this.stringIsEmpty(this.time.value));
    } else {
      return false;
    }
  }

  onDateFocus() {
    this.isDateFocused = true;
  }

  onDateFocusLost() {
    this.isDateFocused = false;
    this.invokeOnChange();
  }

  onTimeFocus() {
    this.isTimefocused = true;
  }

  onTimeFocusLost() {
    this.isTimefocused = false;
    this.invokeOnChange();
  }

  errorTextShown(): string {
    if (this.errorText) {
      return this.errorText;
    } else {
      return null;
    }
  }

  private formsToMoment(): Moment | null {
    if (this.date.valid && this.time.valid && this.time.value) {
      const date = moment(this.date.value);
      const time = moment(this.time.value, "HH:mm");
      date.hour(time.hours());
      date.minutes(time.minutes());
      return date;
    }

    return null;
  }

  private invokeOnChange() {
    this.onChange(this.formsToMoment());
  }

  private get date(): AbstractControl {
    return this.form.controls.date;
  }

  private get time(): AbstractControl {
    return this.form.controls.time;
  }
}
