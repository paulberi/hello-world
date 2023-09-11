import { Component, Input, OnChanges, Output, SimpleChanges } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { Avtalsstatus } from "../../../../../../generated/markkoll-api";
import { MkAgare } from "../../model/agare";
import { MkAgareFormPresenter } from "../agare-form-presenter/agare-form.presenter";

/**
 * Formulär för att redigare en markägare.
 */
@Component({
  providers: [MkAgareFormPresenter],
  selector: "mk-agare-edit",
  templateUrl: "./agare-edit.component.html",
  styleUrls: ["./agare-edit.component.scss"]
})
export class MkAgareEditComponent implements OnChanges {
  /** Ägare */
  @Input() agare: MkAgare;

  /** Visa fält för kontaktperson */
  @Input() showKontaktperson = false;

  /** Tooltip som visas i informationsruta för kontaktperson */
  @Input() kontaktpersonTooltip = "";

  /** Event när användaren klickar på Spara */
  @Output() agareChange = this.presenter.submit;

  constructor(private presenter: MkAgareFormPresenter) { }

  ngOnChanges(_changes: SimpleChanges): void {
    this.presenter.initializeForm(this.agare);
  }

  save() {
    this.presenter.onSubmit();
  }

  canSave(): boolean {
    return this.presenter.canSave();
  }

  get avtalsstatus(): string[] {
    return Object.values(Avtalsstatus);
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }

  ersattningUtbetald() {
    return this.form?.controls.status.value === Avtalsstatus.ERSATTNINGUTBETALD;
  }
}
