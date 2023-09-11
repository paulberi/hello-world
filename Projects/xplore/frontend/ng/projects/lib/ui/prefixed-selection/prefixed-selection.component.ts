import { Component, Input, OnInit } from "@angular/core";
import { XpPrefixedSelectionPresenter } from "./prefixed-selection.presenter";
import { XpSelectOption } from "../model/selectOption";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { disable } from "ol/rotationconstraint";

@Component({
  selector: "xp-prefixed-selection",
  templateUrl: "./prefixed-selection.component.html",
  styleUrls: ["./prefixed-selection.component.scss"],
  providers: [
    XpPrefixedSelectionPresenter,
    {
      provide: NG_VALUE_ACCESSOR,
      multi: true,
      useExisting: XpPrefixedSelectionComponent
    }
  ]
})
export class XpPrefixedSelectionComponent implements ControlValueAccessor, OnInit {

  /** Valt alternativ */
  selected: string;

  /** Text som visas innan selection*/
  @Input() prefix: string;

  /** Vilka val som ska finnas*/
  @Input() options: XpSelectOption[];

  /** Om man ska kunna välja flera*/
  @Input() multiple = false;

  /** Hur man vill aligna texten*/
  @Input() labelTextAlign: "left" | "center" | "right" = "left";

  /** Text som syns innan något är valt*/
  @Input() label: string;

  disabled = false;

  touched = false;

  onChange = (selected) => { };
  onTouched = () => { };

  constructor(private presenter: XpPrefixedSelectionPresenter) { }

  ngOnInit(): void {
    this.presenter.initializeForm(this.selected, this.disabled);
    this.presenter.selectionChange.subscribe(selection => {
      this.onChange(selection);
    });
  }

  get form() {
    return this.presenter.form;
  }

  writeValue(selected: string) {
    this.selected = selected;
    this.presenter.initializeForm(this.selected, this.disabled);
  }

  registerOnChange(onChange: any) {
    this.onChange = onChange;
  }

  registerOnTouched(onTouched: any) {
    this.onTouched = onTouched;
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

  reset() {
    this.presenter.reset();
  }
}
