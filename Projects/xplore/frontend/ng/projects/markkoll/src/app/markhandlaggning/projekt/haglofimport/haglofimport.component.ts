import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { HaglofImportVarningar } from "../../../../../../../generated/markkoll-api";
import { MkHaglofImportPresenter } from "./haglofimport.presenter";

@Component({
  selector: "mk-haglofimport-ui",
  templateUrl: "./haglofimport.component.html",
  styleUrls: ["./haglofimport.component.scss"],
  providers: [MkHaglofImportPresenter]
})
export class MkHaglofImportComponent implements OnInit {

  /** Genererade varningar från importen */
  @Input() warnings: HaglofImportVarningar;

  /** Event när användaren vill importa en fil */
  @Output() import = this.presenter.submitForm;

  /** Event när användaren stänger varningsrutan */
  @Output() closeWarnings = new EventEmitter<void>();

  readonly acceptedFileEndings = [".json"];

  constructor(private presenter: MkHaglofImportPresenter) {}

  hasWarnings(): boolean {
    return this.warnings && Object.values(this.warnings).some(w => w.length > 0);
  }

  importDisabled() {
    return !this.presenter.canSave();
  }

  isNullOrEmpty(array: any[]): boolean {
    return !array || array.length === 0;
  }

  ngOnInit() {
    this.presenter.initializeForm();
  }

  onImport() {
    this.presenter.submit();
  }

  get form() {
    return this.presenter.form;
  }
}
