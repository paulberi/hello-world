import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { Sort } from "@angular/material/sort";
import { ElnatBilaga, BilagaTyp } from "../../../../../../generated/markkoll-api";
import { uuid } from "../../model/uuid";
import { MkBilagorPresenter } from "./bilagor.presenter";

@Component({
  selector: "mk-bilagor-ui",
  templateUrl: "bilagor.component.html",
  styleUrls: ["bilagor.component.scss"],
  providers: [MkBilagorPresenter]
})
export class MkBilagorComponent implements OnInit {
  constructor(private presenter: MkBilagorPresenter) {}

  @Input() bilagor: ElnatBilaga[] = [];

  @Output() addBilaga = this.presenter.addBilaga;

  @Output() removeBilaga = new EventEmitter<uuid>();

  @Output() downloadBilaga = new EventEmitter<uuid>();

  @Output() sortChange = new EventEmitter<Sort>();

  readonly columns = ["bilaga", "uppladdat", "kategori", "laddaHem"];

  ngOnInit() {
    this.presenter.initializeForm();
  }

  canSave(): boolean {
    return this.presenter.canSave();
  }

  save() {
    this.presenter.submit();
  }

  resetForm() {
    this.presenter.resetForm();
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }

  get bilagaTyper(): BilagaTyp[] {
    return Object.values(BilagaTyp);
  }
}
