import { Component, EventEmitter, Input, OnChanges, Output } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { Kund } from "../../../../../generated/kundconfig-api";
import { AdmCreateKundPresenter } from "./create-kund.presenter";

@Component({
  providers: [AdmCreateKundPresenter],
  selector: "adm-create-kund",
  templateUrl: "./create-kund.component.html",
  styleUrls: ["./create-kund.component.scss"]
})
export class AdmCreateKundComponent implements OnChanges {
  @Input() kunder: Kund[] = [];

  @Input() nyaKunder: Kund[] = [];

  @Output() submit = this.presenter.submit;

  @Output() back = new EventEmitter();
  constructor(private presenter: AdmCreateKundPresenter) { }

  ngOnChanges(): void {
    this.presenter.initializeForm(this.kunder);
  }

  save() {
    this.presenter.onSave();
  }

  canSave() {
    return this.presenter.canSave();
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }
}
