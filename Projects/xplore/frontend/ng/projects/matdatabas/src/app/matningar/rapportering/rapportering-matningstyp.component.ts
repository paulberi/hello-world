import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {MatrundaService, RapporteringMatningstyp} from "../../services/matrunda.service";
import {EJ_GRANSKAD, GODKANT, FEL, MatningSaveResult} from "../../services/matobjekt.service";
import {
  FELKOD_ANNAT_FEL, FELKOD_FLODAR, FELKOD_FRUSET,
  FELKOD_HINDER,
  FELKOD_OK, FELKOD_TORR,
  Matning,
  MatobjektService
} from "../../services/matobjekt.service";
import {UntypedFormControl, UntypedFormGroup} from "@angular/forms";
import {getLocalDateISOString, getLocalDateTimeISOString} from "../../common/date-utils";
import { Moment } from "moment";
import moment from "moment";
import {isTouch} from "../../common/touch-utils";
import {Larm} from "../../services/larm.service";
import {FormGroupUnsaved} from "../../common/form-group-unsaved";
import {Berakningstyp} from "../../services/definitionmatningstyp.service";

@Component({
  selector: "mdb-rapportering-matningstyp",
  template: `
    <div class="description">
      <div class="matobjekt-image">
        <picture *ngIf="matningstyp.bifogadBildId">
          <img class="img" [src]="matningstyp.bifogadBildThumbnail | secureLoadImage | async" alt="Bild på mätobjekt">
        </picture>

        <div class="img missing" *ngIf="!matningstyp.bifogadBildId">
          <div>Bild saknas</div>
        </div>
      </div>
      <div>
        <h3><a href="/matobjekt/{{matningstyp.matobjektId}}" target="_blank">{{matningstyp.matobjekt}}</a> -
            <a href="/matobjekt/{{matningstyp.matobjektId}}/matningstyper/{{matningstyp.matningstypId}}/matningar" target="_blank">{{matningstyp.matningstyp}}</a>
        </h3>
        <div><span class="label">Fastighet:</span> {{matningstyp.fastighet}}</div>
        <div><span class="label">Läge:</span> {{matningstyp.lage}}</div>
      </div>
      <form class="form" [formGroup]="form">
      <ng-container formGroupName="tidpunkt">
        <div>
          <mat-form-field>
              <mat-label>Datum</mat-label>
              <input matInput placeholder="åååå-mm-dd" required [matDatepicker]="myDatepicker" formControlName="avlastDatum">
              <mat-datepicker-toggle matSuffix [for]="myDatepicker"></mat-datepicker-toggle>
              <mat-datepicker [touchUi]="isTouch" #myDatepicker></mat-datepicker>
          </mat-form-field>
        </div>

        <div>
            <mat-form-field>
                <mat-label>Klockslag</mat-label>
                <input matInput placeholder="tt:mm"
                    pattern="([01]?[0-9]|2[0-3]):[0-5][0-9]"
                    required formControlName="avlastTid">
            </mat-form-field>
            <button type="button" mat-raised-button color="secondary" (click)="setCurrentTime()"
                style="max-width: 2em; float: right">
                Nu
            </button>
            <mat-error *ngIf="form.get('tidpunkt').hasError('invalidDatum')">Tid och datum får inte vara i framtiden</mat-error>
        </div>
      </ng-container>
        <mat-form-field>
          <select matNativeControl placeholder="Felkod" required formControlName="felkod">
            <option value="${FELKOD_OK}">Ok</option>
            <option value="${FELKOD_HINDER}" selected>Hinder</option>
            <option *ngIf="checkMatningstyp()" value="${FELKOD_FLODAR}">Flödar</option>
            <option *ngIf="checkMatningstyp()" value="${FELKOD_TORR}">Torr</option>
            <option *ngIf="checkMatningstyp()" value="${FELKOD_FRUSET}">Fruset</option>
            <option value="${FELKOD_ANNAT_FEL}">Annat fel</option>
          </select>
        </mat-form-field>
        <mat-form-field>
            <!-- decimal-number kommer från DecimalNumberValueAccessor       -->
          <input autocomplete="off" matInput type="decimal-number" placeholder="{{matningstyp.matningstyp}}{{matningstyp.storhet ? ': '+matningstyp.storhet+' ':' '}} [{{matningstyp.enhet}}]"
           formControlName="avlastVarde">
          <mat-error>{{form.controls.avlastVarde.errors?.error}}</mat-error>
        </mat-form-field>
        <div style="align-self: center">
          <div><span class="label">Tidigare mätningar:</span> {{tidigareMatningar}}</div>
          <div *ngIf="matningstyp.maxPejlbartDjup"><span class="label">Max pejlbart djup:</span> {{matningstyp.maxPejlbartDjup | number}} m</div>
          <div *ngIf="matningstyp.fixpunkt"><span class="label">Fixpunkt:</span> {{matningstyp.fixpunkt}}</div>
        </div>
        <mat-form-field>
          <textarea matInput mat-autosize="true" placeholder="Kommentar" maxlength="250" formControlName="kommentar"></textarea>
          <mat-error>{{form.controls.kommentar.errors?.error}}</mat-error>
        </mat-form-field>
      </form>
      <div class="actions">
        <xp-spinner-button *ngIf="saveEnabled" (spinnerButtonClick)="onSave()" [isLoading]="pendingSave" color="primary"
                           [isDisabled]="!form.valid || (form.pristine && !rapporteringState.isNeedsSynchronization()) || pendingSave">
          <span>Rapportera</span>
        </xp-spinner-button>
        <xp-spinner-button *ngIf="updateEnabled" (spinnerButtonClick)="onUpdate()" [isLoading]="pendingSave" color="primary"
                           [isDisabled]="!form.valid || (form.pristine && !rapporteringState.isNeedsSynchronization()) || (isRapporterad() && isGodkand()) || (isRapporterad() && isFelmarkerad()) || pendingSave">
          <span>{{uppdateraButtonText()}}</span>
        </xp-spinner-button>
        <button *ngIf="newEnabled" type="button" mat-raised-button color="primary" (click)="onNew()">
          <span>Ny mätning</span>
        </button>
        <button *ngIf="abortEnabled" type="button" mat-raised-button color="primary" (click)="onAbort()">
          <span>Avbryt</span>
        </button>
        <span *ngIf="rapporteringState.isNeedsSynchronization()" class="notice">Mätvärde sparat lokalt, men ej synkroniserad med servern. Det kan bero på nätverksproblem - kontrollera och försök igen.</span>
        <span *ngIf="rapporteringState.isSubmitted()" class="notice">Mätvärde inrapporterat.</span>
        <span *ngIf="rapporteringState.isInvalid()" class="error">Ogiltigt mätvärde.</span>

        <span *ngIf="larmGenerated?.length > 0" style="color:red">
            <b>Larm genererat: </b>
            <span *ngFor="let larm of larmGenerated; let isLast=last">{{larm.larmnivaNamn}}{{isLast ? '' : ', '}} </span>
        </span>
      </div>
    </div>
  `,
  styles: [`
    :host {
      display: grid;
      grid-gap: 1rem;
    }

    .description {
      display: grid;
      grid-template-columns: auto 1fr;
      grid-template-rows: auto auto auto;
      grid-gap: 1rem;
    }

    .matobjekt-image .img {
      width: 150px;
      height: 112px;
      border-radius: 10px;
      object-fit: contain;
    }

    .matobjekt-image .missing {
      border: 1px dashed grey;
      opacity: 0.33;
      display: grid;
      align-items: center;
      justify-items: center;
    }

    .notice {
      font-style: italic;
    }

    .error {
      font-style: italic;
      color: red;
    }

    .label {
      font-weight: bold;
    }

    .form {
      grid-column: 1 / span 2;
      display: grid;
      grid-gap: 1rem;
    }

    .actions {
      grid-column: 1 / span 2;
      align-items: center;
    }

    @media only screen and (min-width: 1024px) {
      :host {
        align-items: center;
      }

      .matobjekt-image .img {
        width: 450px;
        height: 336px;
        object-fit: contain;
      }
      .matobjekt-image {
        grid-column: 1;
        grid-row: 1 / span 2;
      }

      .form {
        grid-column: 2;
        grid-template-columns: auto auto;
        grid-template-rows: auto auto auto;
      }

      .actions {
        grid-column: 2;
      }
    }

  `]
})


export class RapporteringMatningstypComponent implements OnInit {
  @Input() matningstyp: RapporteringMatningstyp;
  @Input() rapportor: string;
  @Input() statusStore: Map<number, string>;
  @Input() senasteMatningarStore: Map<number, Matning>;
  @Output() isDirty = new EventEmitter<boolean>();

  isTouch = isTouch();
  rapporteringState = new RapporteringState();
  form: FormGroupUnsaved;
  larmGenerated: Larm[];

  startDate = getLocalDateISOString();

  pendingSave = false;

  saveEnabled = false;
  updateEnabled = false;
  newEnabled = false;
  abortEnabled = false;

  constructor(private matobjektService: MatobjektService,
              private matrundaService: MatrundaService) {
  }

  ngOnInit(): void {
    this.rapporteringState.setStore(this.statusStore, this.matningstyp.matningstypId);

    const currentDate = getLocalDateTimeISOString();
    const storedMatning = localStorage.getItem(this.storageKey);

    if (storedMatning != null) {
      const matning = JSON.parse(storedMatning);
      this.form = this.createForm(matning);
      this.form.markAsDirty();
      this.rapporteringState.markAsNeedsSynchronization();
    } else if (this.isRapporterad()) {
      const matning = this.senasteMatningarStore.get(this.matningstyp.matningstypId);
      if (matning) {
        this.form = this.createForm(matning);
      } else {
        this.form = this.createForm({
          avlastDatum: currentDate,
          felkod: FELKOD_ANNAT_FEL,
          rapportor: this.rapportor,
          kommentar: "Error reading senaste mätning."
        });
      }
    } else {
      this.form = this.createForm({
        avlastDatum: currentDate,
        felkod: FELKOD_OK,
        rapportor: this.rapportor,
      });
    }

    this.form.markAsPristine();
    this.resetButtonState();
  }

  private createForm(defaults: Partial<Matning>) {
    const defaultDateTime = defaults.avlastDatum.split("T");
    const defaultDate = moment(defaultDateTime[0]);
    const defaultTime = defaultDateTime[1].slice(0, 5); // skip seconds

    const newForm = new FormGroupUnsaved({
      tidpunkt: new UntypedFormGroup ({
        avlastDatum: new UntypedFormControl(defaultDate),
        avlastTid: new UntypedFormControl(defaultTime),
      }, {validators: tidValidator}),
      avlastVarde: new UntypedFormControl(defaults.avlastVarde),
      kommentar: new UntypedFormControl(defaults.kommentar),
      felkod: new UntypedFormControl(defaults.felkod),
      rapportor: new UntypedFormControl(defaults.rapportor)
    }, {validators: matvardeValidator});

    newForm.valueChanges.subscribe(v => this.isDirty.emit(newForm.isChanged()));

    return newForm;
  }

  public uppdateraButtonText() {
    if (this.isGodkand()) {
      return "Godkänd";
    } else if (this.isFelmarkerad()) {
      return "Felmarkerad";
    } else {
      return "Ändra";
    }
  }

  private resetButtonState() {
    if (!this.isRapporterad() || this.rapporteringState.isNeedsSynchronization()) {
      this.saveEnabled = true;
      this.updateEnabled = false;
      this.newEnabled = false;
    } else {
      this.saveEnabled = false;
      this.updateEnabled = true;
      this.newEnabled = true;
    }
  }

  isRapporterad() {
    if (this.matningstyp.senasteAvlastDatum == null) {
      return false;
    }

    const startDate = new Date(this.startDate);
    const avlastDate = new Date(this.matningstyp.senasteAvlastDatum);

    return avlastDate.getTime() > startDate.getTime();
  }

  public isGodkand() {
    return this.matningstyp.status1 === GODKANT;
  }

  public isFelmarkerad() {
    return this.matningstyp.status1 === FEL;
  }

  get tidigareMatningar() {
    return [this.matningstyp.varde1, this.matningstyp.varde2, this.matningstyp.varde3].filter(v => v != null).join("; ");
  }

  get storageKey() {
    return "MATNING_" + this.matningstyp.matningstypId;
  }

  onSave() {
    const matning = this.form.value;
    const avlastDatum = avlastDatumForm(this.form.get("tidpunkt"));

    const avlastDatumString = this.formateraDatum(avlastDatum);
    const matningToServer: Matning = {...matning, avlastDatum: avlastDatumString};

    this.rapporteringState.resetState();
    this.larmGenerated = null;

    localStorage.setItem(this.storageKey, JSON.stringify(matningToServer));
    this.pendingSave = true;
    this.matobjektService.postMatning(this.matningstyp.matobjektId, this.matningstyp.matningstypId, matningToServer)
      .subscribe((m) => this.matningSuccess(m),
            (error) => this.matningError(error));

    this.abortEnabled = false;
  }

  onUpdate() {
    const matning = this.form.value;
    const avlastDatum = avlastDatumForm(this.form.get("tidpunkt"));

    const avlastDatumString = this.formateraDatum(avlastDatum);
    const matningToServer: Matning = {...matning, avlastDatum: avlastDatumString};

    this.rapporteringState.resetState();
    this.larmGenerated = null;

    const senasteMatningId = this.senasteMatningarStore.get(this.matningstyp.matningstypId).id;
    matningToServer.id = senasteMatningId;
    localStorage.setItem(this.storageKey, JSON.stringify(matningToServer));
    this.pendingSave = true;
    this.matobjektService.putMatning(this.matningstyp.matobjektId, this.matningstyp.matningstypId, senasteMatningId, matningToServer)
      .subscribe((m) => this.matningSuccess(m),
        (error) => this.matningError(error));
  }

  onNew() {
    this.setCurrentTime();
    this.form.get("avlastVarde").patchValue(null);
    this.form.get("kommentar").patchValue(null);
    this.form.markAsPristine();

    this.rapporteringState.resetState();

    this.saveEnabled = true;
    this.updateEnabled = false;
    this.newEnabled = false;
    this.abortEnabled = true;
  }

  onAbort() {
    this.saveEnabled = false;
    this.updateEnabled = true;
    this.newEnabled = true;
    this.abortEnabled = false;
  }

  private matningSuccess(matningSaveResult: MatningSaveResult) {
    // Indikera att mätningstypen är rapporterad för andra komponenter
    const matning = matningSaveResult.matning;
    this.matningstyp.senasteAvlastDatum = matning.avlastDatum;
    this.senasteMatningarStore.set(this.matningstyp.matningstypId, matning);
    this.matningstyp.status1 = EJ_GRANSKAD;

    this.larmGenerated = matningSaveResult.larm;

    this.pendingSave = false;
    localStorage.removeItem(this.storageKey);
    this.form.markAsPristine();
    this.rapporteringState.markAsSubmitted();

    this.isDirty.emit(false);

    this.resetButtonState();
    this.reloadData();
  }

  private matningError(error) {
    if (error.status === 400) {
      this.rapporteringState.markAsInvalid();
    } else {
      // There doesn't appear to be an error with the submitted data, so we assume
      // something else went wrong the server communication. Perhaps the user or the server is offline.
      this.rapporteringState.markAsNeedsSynchronization();
    }
    this.pendingSave = false;
  }

  checkMatningstyp() {
    return (this.matningstyp.berakningstyp === Berakningstyp.NIVA_NEDMATNING
      || this.matningstyp.berakningstyp === Berakningstyp.NIVA_PORTRYCK);
  }

  private reloadData() {
    const matobjektId = this.matningstyp.matobjektId;
    const matningstypId = this.matningstyp.matningstypId;
    this.matrundaService.getMatrundaFromMatobjektMatningstyper(matobjektId, this.startDate).subscribe(matningstyper => {
      matningstyper.filter(mt => mt.matningstypId === matningstypId).map(mt => {
        this.matningstyp = mt;
      });
    });
  }

  setCurrentTime() {
    const now = moment();
    this.form.markAsDirty();

    this.form.get("tidpunkt").patchValue({
      avlastDatum: moment(now.format("YYYY-MM-DD")),
      avlastTid: now.local().format("HH:mm")
    });
  }

  private formateraDatum(datum: Moment): string {
    return datum.local().format("YYYY-MM-DDTHH:mm");
  }
}

function avlastDatumForm(form) {
  const matning = form.value;
  const avlastDatum: Moment = moment(matning.avlastDatum);
  const avlastTid = moment(matning.avlastTid, "hh:mm");

  avlastDatum.hour(avlastTid.hour());
  avlastDatum.minutes(avlastTid.minutes());

  return avlastDatum;
}

const tidValidator = (form: UntypedFormGroup) => {
  const avlastDatum = moment(avlastDatumForm(form));
  const now = moment();
  return now.isBefore(avlastDatum) ? { "invalidDatum": true } : null;
};

const matvardeValidator = (form: UntypedFormGroup) => {
  const avlastVarde = form.get("avlastVarde");
  const kommentar = form.get("kommentar");
  const felkod = form.get("felkod").value;

  if (isError(felkod)) {
    if (avlastVarde.value != null) {
      avlastVarde.setErrors({error: "Inget mätvärde ska anges."});
    } else {
      avlastVarde.setErrors(null);
    }

    if (isEmpty(kommentar.value)) {
      kommentar.setErrors({error: "Kommentar måste anges."});
    } else {
      kommentar.setErrors(null);
    }
  } else {
    if (avlastVarde.value == null) {
      avlastVarde.setErrors({error: "Mätvärde måste anges."});
    } else {
      avlastVarde.setErrors(null);
    }

    kommentar.setErrors(null);
  }
  return null;
};

const isEmpty = (s: string) => {
  return s == null || s.trim() === "";
};

const isError = (s: string) => {
  return !isEmpty(s) && s !== FELKOD_OK;
};

class RapporteringState {
  private statusStore: Map<number, string>;
  private id: number;

  private _getState() {
    if (this.statusStore.has(this.id)) {
      return this.statusStore.get(this.id);
    } else {
      return "default";
    }
  }

  private _setState(state: string) {
    this.statusStore.set(this.id, state);
  }

  markAsInvalid() {
    this._setState("invalid");
  }

  isInvalid() {
    return this._getState() === "invalid";
  }

  markAsSubmitted() {
    this._setState("submitted");
  }

  isSubmitted() {
    return this._getState() === "submitted";
  }

  markAsNeedsSynchronization() {
    this._setState("needsSynchronization");
  }

  isNeedsSynchronization() {
    return this._getState() === "needsSynchronization";
  }

  resetState() {
    this._setState("default");
  }

  setStore(statusStore: Map<number, string>, id: number) {
    this.statusStore = statusStore;
    this.id = id;
  }
}

