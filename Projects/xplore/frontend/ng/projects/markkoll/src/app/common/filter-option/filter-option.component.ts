import { Component, EventEmitter, forwardRef, Input, OnInit, Output, ViewChild } from "@angular/core";
import { ControlValueAccessor, DefaultValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";

export interface OptionItem {
  value: string;
  label: string;
}

/** Dropdownmeny som inkluderar ett tomt fält */
@Component({
  selector: "mk-filter-option",
  templateUrl: "./filter-option.component.html",
  styleUrls: ["./filter-option.component.scss"],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => MkFilterOptionComponent),
      multi: true
    }
  ]
})
export class MkFilterOptionComponent implements OnInit, ControlValueAccessor {
  @ViewChild(DefaultValueAccessor, { static: true }) private valueAccessor: DefaultValueAccessor;

  /** Menyval */
  @Input() options: OptionItem[];

  /** Förvald text */
  @Input() defaultText: string;

  @Input() value: string;

  /** Emittar det valda värdet i dropdownmenyn */
  @Output() valueChange = new EventEmitter<string>();

  constructor() { }

  writeValue(obj: any) {
    this.valueAccessor.writeValue(obj);
  }

  registerOnChange(fn: any) {
    this.valueAccessor.registerOnChange(fn);
  }

  registerOnTouched(fn: any) {
    this.valueAccessor.registerOnTouched(fn);
  }

  setDisabledState(isDisabled: boolean) {
    this.valueAccessor.setDisabledState(isDisabled);
  }

  ngOnInit(): void {
  }
}
