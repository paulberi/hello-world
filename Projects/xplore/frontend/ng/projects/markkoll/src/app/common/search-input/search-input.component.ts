import { Component, EventEmitter, forwardRef, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from "@angular/core";
import { ControlValueAccessor, DefaultValueAccessor, UntypedFormControl, NG_VALUE_ACCESSOR } from "@angular/forms";
import { debounceTime } from "rxjs/operators";

/**
 * Komponent som ska representera ett sökfält, som emittar värdet i sökfältet efter en viss
 * fördröjning.
 */
@Component({
  selector: "mk-search-input",
  templateUrl: "./search-input.component.html",
  styleUrls: ["./search-input.component.scss"],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => MkSearchInputComponent),
      multi: true
    }
  ]
})
export class MkSearchInputComponent implements OnInit, ControlValueAccessor {
  @ViewChild(DefaultValueAccessor, { static: true }) valueAccessor: DefaultValueAccessor;

  /** Etikett */
  @Input() placeholder: string;

  /**
   * Fördröjning, i millisekunder, från det att man senast skrivit något i sökfältet tills det att
   * värdet i sökfältet emittas.
   */
  @Input() debounceTime = 500;

  /** Emittar värdet i sökfältet. */
  @Output() searchChange = new EventEmitter<string>();

  @Input() search = new UntypedFormControl();

  constructor() { }

  ngOnInit(): void {
    this.search
        .valueChanges
        .pipe(debounceTime(this.debounceTime))
        .subscribe(value => this.searchChange.emit(value));
  }

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
}
