import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { FastighetsokAuth } from "../../../../../generated/kundconfig-api";
import { AdmFastighetsokAuthFormPresenter } from "./fastighetsok-auth-form.presenter";

@Component({
  providers: [AdmFastighetsokAuthFormPresenter],
  selector: "adm-fastighetsok-auth-form",
  templateUrl: "./fastighetsok-auth-form.component.html",
  styleUrls: ["./fastighetsok-auth-form.component.scss"]
})
export class AdmFastighetsokAuthFormComponent implements OnInit, OnChanges {
  constructor(private presenter: AdmFastighetsokAuthFormPresenter) {}

  /** Autentiseringsuppgifter för Fastighetsök */
  @Input() fastighetsokAuth: FastighetsokAuth;

  /** Event som emittas när autentiseringsuppgifter uppdateras */
  @Output() fastighetsokAuthChange = this.presenter.authChange;

  /** Event som emittas när man avbryter processen */
  @Output() cancel = new EventEmitter<void>();

  /** Event som emittas när användarne vill radera systeminloggningen */
  @Output() reset = new EventEmitter<void>();

  ngOnInit() {
    this.initializeForm();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.metriaMapsAuth) {
      this.initializeForm();
    }
  }

  initializeForm() {
    this.presenter.initializeForm(this.fastighetsokAuth);
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }

  canSave(): boolean {
    return this.presenter.canSave();
  }

  submit() {
    this.presenter.submit();
  }

  authIsEmpty(): boolean {
    return this.fastighetsokAuth.username == null &&
           this.fastighetsokAuth.password == null &&
           this.fastighetsokAuth.kundmarke == null;
  }

  emptyAtInitialization(): boolean {
    return this.presenter.emptyAtInitialization();
  }
}
