import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { Avtalsstatus } from "../../../../../../generated/markkoll-api";
import { MkAgare } from "../../model/agare";
import { MkAgareFormPresenter } from "../agare-form-presenter/agare-form.presenter";

/**
 * Formulär för att redigera ett ombud.
 */
@Component({
  selector: "mk-ombud-edit",
  providers: [MkAgareFormPresenter],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: "./ombud-edit.component.html",
  styleUrls: ["./ombud-edit.component.scss"]
})
export class MkOmbudEditComponent implements OnChanges {

  /** Ombud */
  @Input() ombud: MkAgare;

  /** Visa fält för kontaktperson */
  @Input() showKontaktperson = false;

  /** Tooltip som visas i informationsruta för kontaktperson */
  @Input() kontaktpersonTooltip = "";

  /** Event när användaren klickar på Spara */
  @Output() ombudChange: EventEmitter<MkAgare> = this.presenter.submit;

  /** Event när användaren klickar på Ta bort */
  @Output() delete = new EventEmitter<void>();

  constructor(private presenter: MkAgareFormPresenter) { }

  ngOnChanges(changes: SimpleChanges): void {
    this.presenter.initializeForm(this.ombud);
  }

  save() {
    this.presenter.onSubmit();
  }

  onDelete() {
    this.delete.emit();
  }

  canSave(): boolean {
    return this.presenter.canSave();
  }

  ersattningUtbetald() {
    return this.form?.controls.status.value === Avtalsstatus.ERSATTNINGUTBETALD;
  }

  get avtalsstatus(): string[] {
    return Object.values(Avtalsstatus);
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }
}
