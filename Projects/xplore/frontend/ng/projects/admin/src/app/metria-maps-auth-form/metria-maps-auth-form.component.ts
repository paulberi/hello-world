import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { MetriaMapsAuth } from "../../../../../generated/kundconfig-api";
import { AdmCredentialsFormPresenter } from "./metria-maps-auth-form.presenter";

/**
 * Ett formulär med inloggningsuppgifter
 */
@Component({
  providers: [AdmCredentialsFormPresenter],
  selector: "adm-metria-maps-auth-form",
  templateUrl: "./metria-maps-auth-form.component.html",
  styleUrls: ["./metria-maps-auth-form.component.scss"]
})
export class AdmMetriaMapsAuthFormComponent implements OnInit, OnChanges {

  /** Autentiseringsuppgifter för MetriaMaps */
  @Input() metriaMapsAuth: MetriaMapsAuth;

  /** Event som emittas när autentiseringsuppgifter uppdateras */
  @Output() metriaMapsAuthChange = this.presenter.authChange;

  /** Event som emittas när man avbryter processen */
  @Output() cancel = new EventEmitter<void>();

  /** Event som emittas när användarne vill radera systeminloggningen */
  @Output() reset = new EventEmitter<void>();

  constructor(private presenter: AdmCredentialsFormPresenter) {}

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }

  ngOnInit() {
    this.initializeForm();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.metriaMapsAuth) {
      this.initializeForm();
    }
  }

  initializeForm() {
    this.presenter.initializeForm(this.metriaMapsAuth);
  }

  submit() {
    this.presenter.submit();
  }

  canSave(): boolean {
    return this.presenter.canSave();
  }

  authIsEmpty(): boolean {
    return this.metriaMapsAuth.username == null &&
           this.metriaMapsAuth.password == null;
  }
}
