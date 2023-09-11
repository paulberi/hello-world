import { Component, Input, OnInit, Output, SimpleChanges } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { NisKalla, ProjektTyp } from "../../../../../../../generated/markkoll-api";
import { ImportTyp } from "../../../model/importTyp";
import { uuid } from "../../../model/uuid";

import { MkProjektimporterPresenter } from "./projektimporter.presenter";

@Component({
  selector: "mk-projektimporter-ui",
  templateUrl: "./projektimporter.component.html",
  styleUrls: ["./projektimporter.component.scss"],
  providers: [
    MkProjektimporterPresenter
  ]
})
export class MkProjektimporterComponent implements OnInit {

  /** Projekt-ID */
  @Input() projektId: uuid;

  /** Projekttyp */
  @Input() projektTyp: ProjektTyp;

  /** Importtyp */
  @Input() importTyp: ImportTyp = ImportTyp.VERSION;

  /** Event när användaren ändrar importtyp */
  @Output() importTypChange = this.presenter.importTypChange;

  readonly importVersion = ImportTyp.VERSION;
  readonly importHaglof = ImportTyp.HAGLOF;

  constructor(private presenter: MkProjektimporterPresenter) {}

  ngOnChanges(changes: SimpleChanges) {
    if (changes.importTyp) {
      this.initializeForm();
    }
  }

  ngOnInit() {
    this.initializeForm();
  }

  isImportHaglof(): boolean {
    return this.importTyp == ImportTyp.HAGLOF;
  }

  isImportVersion(): boolean {
    return this.importTyp == ImportTyp.VERSION;
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }

  private initializeForm() {
    this.presenter.initializeForm(this.importTyp);
  }
}
