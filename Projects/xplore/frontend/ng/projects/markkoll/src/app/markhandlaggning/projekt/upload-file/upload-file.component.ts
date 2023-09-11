import { Component, Input, OnChanges, OnInit, Output, SimpleChanges } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { TranslocoService } from "@ngneat/transloco";
import { NisKalla, ProjektTyp } from "../../../../../../../generated/markkoll-api";
import { MkInstallningarService } from "../../../services/installningar.service";
import { MkUserService } from "../../../services/user.service";
import { MkUploadFilePresenter } from "./upload-file.presenter";

/**
 * Ladda upp en fil till ett Markkoll-projekt med geografisk information om intrång.
 * Sätt även NIS som används som indatakälla.
 */
@Component({
  selector: "mk-upload-file",
  providers: [MkUploadFilePresenter],
  templateUrl: "./upload-file.component.html",
  styleUrls: ["./upload-file.component.scss"]
})

export class MkUploadFileComponent implements OnInit, OnChanges {

  /**
   * Filtrera drowndown med NIS beroende på projekttyp.
   */
  @Input() projektTyp: ProjektTyp;

  /**
   * Filtrera drowndown med NIS beroende på inställningar.
   */
  @Input() nisKalla: NisKalla;

  /** Om komponenten ska validera att man har valt en godtagbar fil */
  @Input() disableValidation: boolean = true;

  /**
   * Event när filer läggs till eller tas bort.
   **/
  @Output() filesChange = this.presenter.change;

  /**
   * Event med en flagga om formuläret är rätt ifyllt.
   **/
  @Output() validChange = this.presenter.valid;

  constructor(
    private translate: TranslocoService,
    private presenter: MkUploadFilePresenter) { }

  zipFileEnding = [".zip"];

  ngOnInit() {
    this.presenter.initializeForm(this.disableValidation, this.nisKalla, this.projektTyp);
  }

  ngOnChanges(changes: SimpleChanges) {
    this.presenter.filterIndataTyper(changes.projektTyp?.currentValue ? changes.projektTyp?.currentValue : this.projektTyp, this.nisKalla);
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }

  get indataTyper(): string[] {
    return this.presenter.indataTyper;
  }
}
