import { Component, forwardRef, Input, OnInit, ViewChild } from "@angular/core";
import { ControlValueAccessor, DefaultValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { MatInput } from "@angular/material/input";
import { XpUiSearchFieldPresenter } from "./search-field.presenter";

/**
 * Ett tänkt sökfält med en viss 'debounce time' på 500ms innan värdet i sökfältet emittas
 */
@Component({
  selector: "xp-ui-search-field",
  templateUrl: "./search-field.component.html",
  styleUrls: ["./search-field.component.scss"],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => XpUiSearchFieldComponent),
      multi: true
    },
    XpUiSearchFieldPresenter
  ]
})
export class XpUiSearchFieldComponent implements OnInit, ControlValueAccessor {
  @ViewChild(DefaultValueAccessor, { static: true })
  private valueAccessor: DefaultValueAccessor;

  @ViewChild(MatInput, { static: true })
  private input: MatInput;

  /** Etikett */
  @Input() placeholder: string;

  /** Tid innan värdet från sökfältet emittas */
  @Input() debounceTime = 500;

  /** aria-label för "Rensa sökfält"-knappen */
  @Input() ariaLabel = "Rensa";

  private onChange = (query) => { };

  constructor(private presenter: XpUiSearchFieldPresenter) { }

  ngOnInit(): void {
    this.presenter.setDebounceTime(this.debounceTime)
    this.presenter.searchQuery$.subscribe(query => this.onChange(query));
    this.valueAccessor.registerOnChange(query => {this.presenter.search(query); return {}; });
  }

  writeValue(query: any): void {
    this.valueAccessor.writeValue(query);
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.valueAccessor.registerOnTouched(fn);
  }

  setDisabledState?(isDisabled: boolean): void {
    this.valueAccessor.setDisabledState(isDisabled);
  }

  reset() {
    this.writeValue("");
    this.presenter.search("");
  }

  isEmpty(): boolean {
    return !this.input || !this.input.value || this.input.value === "";
  }
}
