import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from "@angular/core";
import { UntypedFormGroup, FormGroupDirective } from "@angular/forms";
import { Agartyp } from "../../../../../../generated/markkoll-api";
import { MkAgare } from "../../model/agare";
import { MkAgareFormPresenter } from "../agare-form-presenter/agare-form.presenter";

/**
 * Formulär för att lägga till ett ombud.
 */
@Component({
  selector: "mk-ombud-add",
  providers: [MkAgareFormPresenter],
  templateUrl: "./ombud-add.component.html",
  styleUrls: ["./ombud-add.component.scss"]
})
export class MkOmbudAddComponent implements OnInit, OnChanges {
  /** Ombud */
  @Input() ombud: MkAgare = null;

  /** Event när användaren klickar på Spara */
  @Output() ombudChange = this.presenter.submit;

  /** Event när användaren klickar på Avbryt */
  @Output() cancel = new EventEmitter<void>();

  @ViewChild(FormGroupDirective) private formGroupDirective: FormGroupDirective;

  constructor(private presenter: MkAgareFormPresenter) { }

  ngOnInit() {
    this.presenter.initializeForm({ ...this.ombud, agartyp: Agartyp.OMBUD });
  }

  ngOnChanges(_changes: SimpleChanges): void {
    this.ngOnInit();
  }

  save() {
    this.presenter.onSubmit();
  }

  onCancel() {
    this.cancel.emit();
  }

  canSave(): boolean {
    return this.presenter.canSave();
  }

  resetForm() {
    /* mat-errors blir inte nollställda med enbart tar en reset() på FormGroup-instansen. Vi slipper
    det om vi initialiserar formuläret igen. */
    this.ngOnInit();
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }
}
