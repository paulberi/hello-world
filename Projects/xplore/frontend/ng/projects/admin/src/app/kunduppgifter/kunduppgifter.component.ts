import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { Kund } from "../../../../../generated/kundconfig-api";
import { KunduppgifterPresenter } from "./kunduppgifter.presenter";

@Component({
  providers: [KunduppgifterPresenter],
  selector: "adm-kunduppgifter",
  templateUrl: "./kunduppgifter.component.html",
  styleUrls: ["./kunduppgifter.component.scss"]
})
export class AdmKunduppgifterComponent implements OnChanges {
  @Input() kund: Kund;
  @Output() kundChange = this.presenter.kundChange;
  @Output() kundDelete = new EventEmitter<void>();

  constructor(private presenter: KunduppgifterPresenter) {}

  submit() {
    this.presenter.submit();
  }

  ngOnInit() {
    this.presenter.initializeForm(this.kund);
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.kund) {
      this.presenter.initializeForm(this.kund);
    }
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }

  canSave(): boolean {
    return this.presenter.canSave();
  }
}
