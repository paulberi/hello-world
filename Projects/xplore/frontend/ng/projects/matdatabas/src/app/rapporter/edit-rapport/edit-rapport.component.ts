import { Component, OnInit } from "@angular/core";
import { Location } from "@angular/common";
import { RapportGrafSettings, RapportMottagare, RapportSettings, RapportService,
  grafSettingsEmpty, Tidsintervall, DatumPeriod } from "../../services/rapport.service";
import { MdbDialogService } from "../../services/mdb-dialog.service";
import { filter, map, concatMap, switchMap, tap } from "rxjs/operators";
import "../../common/extension-methods";
import { UntypedFormGroup, UntypedFormControl, ValidationErrors, AbstractControl } from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import { of, Observable, timer } from "rxjs";
import { RapportTableColumn } from "./rapport-removebutton-table.component";
import moment from "moment";
import { Moment } from "moment";
import { FormGroupUnsaved } from "../../common/form-group-unsaved";
import { UNSAVED_CHANGES } from "../../services/can-deactivate-guard.service";
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import { ConfigService } from "../../../../../lib/config/config.service";

export enum RestError {
  GET_RAPPORT = "Misslyckades hämta information om rapporten. Ladda om sidan för att försöka på nytt.",
  POST_RAPPORT = "Misslyckades lagra rapporten. Försök igen om en liten stund",
  PUT_RAPPORT = "Misslyckades uppdatera rapporten. Försök igen om en liten stund",
  DELETE_RAPPORT = "Misslyckades radera rapporten. Försök igen om en liten stund"
}

@Component({
  selector: "mdb-edit-rapport",
  template: `
  <h2>Rapporter</h2>

  <hr>
  <form [formGroup]="form" *ngIf="form">
    <div>
      <div class="left-column">
        <div class="general-information">
          <div class="name-and-active">
            <mat-form-field>
              <input type="text"
                    matInput
                    autocomplete="off"
                    required
                    mat-autosize="true"
                    maxlength="60"
                    placeholder="Namn"
                    formControlName="name" />
              <mat-error *ngIf="form.controls.name.invalid">{{rapportnamnErrorMessage()}}</mat-error>
            </mat-form-field>

            <div>
              <mat-checkbox class="status" formControlName="active">
                Aktiv
              </mat-checkbox>
            </div>
          </div>

          <mat-form-field class="beskrivning">
            <input type="text"
                      matInput
                      mat-autosize="true"
                      autocomplete="off"
                      placeholder="Beskrivning"
                      formControlName="description" />
          </mat-form-field>

          <div class="content">
            <h3>Innehåll</h3>
            <mat-form-field>
              <input type="text"
                    matInput
                    autocomplete="off"
                    required
                    maxlength="60"
                    placeholder="Rubrik"
                    formControlName="introductionTitle" />
              <mat-error>Fältet är obligatoriskt</mat-error>
            </mat-form-field>
            <mat-form-field>
              <textarea matInput
                        mat-autosize="true"
                        autocomplete="off"
                        maxlength="1000"
                        matAutosizeMinRows="11"
                        placeholder="Information"
                        formControlName="introductionInformation">
              </textarea>
            </mat-form-field>
          </div>

          <h3>Mottagare</h3>
          <div class="mottagare-field">
            <div class="rapportering-intervall">
              <mat-form-field>
                <mat-select matNativeControl
                            formControlName="tidsintervall"
                            placeholder="Tidsintervall:">
                  <mat-option *ngFor="let val of tidsintervall" [value]="val.value">{{val.displayValue}}</mat-option>
                </mat-select>
                <mat-hint>
                  Hur ofta rapporten ska skickas ut<br>
                  Rapporterna skickas 01:00 på natten
                </mat-hint>
              </mat-form-field>
            </div>

            <div>
              <mat-form-field floatLabel="always">
                <mat-label>Startdatum</mat-label>
                <input matInput
                       required
                       placeholder="åååå-mm-dd"
                       autocomplete="off"
                       [matDatepicker]="myDatepicker"
                       formControlName="startDatum">
                <mat-datepicker-toggle matSuffix [for]="myDatepicker"></mat-datepicker-toggle>
                <mat-datepicker [touchUi]="false" #myDatepicker></mat-datepicker>
                <mat-hint>När rapporterna tidigast ska skickas</mat-hint>
                <mat-error>Fältet är obligatoriskt</mat-error>
              </mat-form-field>
           </div>
           <div></div>
          </div>

        </div>
        <div *ngIf="rapportSettings.senastSkickad">Senast skickad: {{senastSkickad()}}</div>

        <div class="mottagare">
          <mdb-rapport-removebutton-table [columns]="mottagareColumns"
                                          [(rows)]="mottagare"
                                          deleteConfirmationMessage="Vill du ta bort den här mottagaren?">
          </mdb-rapport-removebutton-table>
          <div class="align-right">
            <button type="button"
                    mat-raised-button
                    color="primary"
                    (click)="addMottagare()">Lägg till mottagare
            </button>
          </div>
        </div>
        <mat-form-field class="mail-message">
          <textarea matInput
                    mat-autosize="true"
                    autocomplete="off"
                    maxlength="500"
                    matAutosizeMinRows=3
                    placeholder="Ändra mejlmeddelande"
                    formControlName="mailMessage">
          </textarea>
          <mat-hint>Meddelande i mejl. Ersätter standardmeddelandet.</mat-hint>
        </mat-form-field>
      </div>
      <div></div>
    </div>

    <div>
      <div class="right-column">
        <div>
          <h3>Lägesbild</h3>
          <div class="lägesbild">
            <mdb-grunduppgifter-bild [id]="bildlagesId" (updated)="bildlagesId = $event">
            </mdb-grunduppgifter-bild>
          </div>
        </div>
        <h3>Grafer</h3>
        <div class="graph-settings">
          <mat-form-field>
              <mat-select matNativeControl
                          placeholder="Dataperiod fr.o.m.:"
                          formControlName="dataperiodFrom">
              <mat-option *ngFor="let val of dataperiodFrom" [value]="val">{{dataPeriodFromText(val)}}</mat-option>
            </mat-select>
            <mat-hint>Hur långt tillbaka i tiden mätvärden ska visas i grafer</mat-hint>
          </mat-form-field>

          <div class="rorelsereferens-datum">
            <mdb-datetime-picker label="Beräkna rörelse relativt"
                                 formControlName="rorelsereferensdatum"
                                 [errorText]="datetimeErrorMessage(form.controls.rorelsereferensdatum)"
                                 hint="Datum för beräkning av relativa mätvärden">
            </mdb-datetime-picker>
            <div></div>
          </div>
        </div>

        <div class="graphs">
          <mat-expansion-panel [disabled]="!expandPanel"
                               (click)="expandPanel = true"
                               *ngFor="let graf of grafer; let i=index">
            <mat-expansion-panel-header>
                <mat-panel-title>
                  {{graf.rubrik}}
                </mat-panel-title>
                <mat-panel-description class="align-right">
                  <mat-icon (click)="removeGraf(i)" mat-icon id="close-button-icon">remove_circle</mat-icon>
                </mat-panel-description>
            </mat-expansion-panel-header>

            <div class="graf-panel">
              <mdb-edit-rapport-graf [rapportGraf]="graf"></mdb-edit-rapport-graf>
            </div>
          </mat-expansion-panel>
         <mat-expansion-panel hideToggle (click)="addGraf()" style="background-color: #2b5972;">
            <mat-expansion-panel-header #addGrafHeader (click)="addGrafHeader._toggle()">
              <mat-panel-title style="color: white;">
                <div>
                  Lägg till ny graf
                </div>
              </mat-panel-title>
              <mat-panel-description class="align-right" style="padding-right: 7px;">
                <mat-icon mat-icon id="add" color="accent">add</mat-icon>
              </mat-panel-description>
            </mat-expansion-panel-header>
          </mat-expansion-panel>
        </div>

        <div></div>
      </div>
    </div>
  </form>

  <hr>

  <div class="actions">
    <button mat-stroked-button color="primary" (click)="goBack()">Tillbaka</button>
    <button mat-raised-button (click)="preview()" [disabled]="!canPreview()" color="primary">Förhandsgranska</button>
    <mdb-save-button [label]="'Spara'" (clicked)="save(form)" [saving]="saving" [disabled]="!canSave()"></mdb-save-button>
    <button mat-raised-button color="primary"
            (click)="delete()"
            [disabled]="!isEditing()"
            color="warn">
        Ta bort
    </button>
  </div>
  <p *ngIf="error" class="rest-error">{{error}}</p>

  `,
  styles: [`
      form {
        display: grid;
        grid-template-columns: 1fr 1fr;
        grid-template-rows: auto auto;
        grid-gap: 10%;
      }

      @media only screen and (max-width: 1024px) {
        form {
          display: block;
        }
      }

      .left-column {
        display: grid;
        grid-gap: 1rem;
        max-height: initial;
      }

      .rapportering-intervall {
        display: grid;
        grid-template-columns: auto auto auto;
        grid-gap: 1rem;
        padding-bottom: 1rem;
      }

      .status {
        vertical-align: -22px;
      }

      .general-information {
        display: grid;
        grid-template-columns: 1fr 1fr;
        grid-gap: 1rem;
        margin-right: 5rem;
      }

      .name-and-active {
        display: grid;
        grid-template-columns: 1fr auto;
        grid-column-start: span 2;
        grid-gap: 3rem;
      }

      .mottagare-field {
        display: grid;
        grid-template-columns: auto auto 1fr;
        grid-column-start: span 2;

      }
      .mottagare {
        display: grid;
        grid-gap: 1rem;
      }

      .content {
        display: grid;
        grid-gap: 1rem;
        grid-column-start: span 2;
      }

      .lägesbild {
        display: grid;
        padding-top: 1rem;
        grid-gap: 1rem;
      }

      .right-column {
        display: grid;
        grid-gap: 1rem;
      }

      .new-graph-button {
        margin-bottom: 1rem;
      }

      .graf-panel {
        display: grid;
        grid-gap: 1rem;
      }

      .mail-message {
        display: grid;
        padding-top: 1rem;
        padding-bottom: 1rem;
      }

      .beskrivning {
        display: grid;
        grid-column-start: span 2;
      }

      .actions {
        display: grid;
        grid-gap: 1rem;
        grid-template-columns: auto auto auto auto;
      }

      .center-vertically {
        display: flex;
        align-items: center;
      }

      .preview {
        display: grid;
        grid-gap: 1rem;
      }

      .align-right {
        display: flex;
        justify-content: flex-end;
      }

      .rorelsereferens-datum {
        display: grid;
        grid-template-columns: 1fr 1fr;
        padding-bottom: 3rem;
      }

      .graph-settings {
        display: grid;
        grid-template-columns: 2fr 3fr;
        grid-gap: 5rem;
      }

      .graphs {
        display: grid;
        grid-gap: 1rem;
      }
  `]
})

export class EditRapportComponent implements OnInit {
  readonly mottagareColumns: RapportTableColumn[] = [
    { name: "Namn", key: "namn" },
    { name: "E-post", key: "epost" },
  ];
  form: FormGroupUnsaved;
  rapportSettings: RapportSettings;
  rapportId: number;
  savedRapportName: string | null;
  savedStartDatumMoment: Moment | null;
  expandPanel = true;

  RestError = RestError;
  error: RestError = null;
  saving: boolean;

  tidsintervall = Tidsintervall;

  supportEmail: string;
  supportName: string;
  defautMailMessage: string;

  constructor(private popupService: MdbDialogService,
              private rapportService: RapportService,
              private route: ActivatedRoute,
              private router: Router,
              private httpClient: HttpClient,
              private location: Location,
              private configService: ConfigService) {

    this.supportEmail = configService.config.app.supportEmail;
    this.supportName = configService.config.app.supportName;
    this.defautMailMessage = configService.config.app.reportEmailTemplate;
  }

  ngOnInit() {
    this.route.paramMap.pipe(
      map(params => +params.get("id")),
      tap(id => this.rapportId = id),
      concatMap(id => id !== 0 ? this.rapportService.getRapportSettings(id) :
                                 of({} as RapportSettings))
    )
      .subscribe(rapportSettings => {
        this.rapportSettings = rapportSettings;
        this.savedRapportName = rapportSettings.namn;
        this.savedStartDatumMoment = moment(rapportSettings.startDatum);
        this.form = this.createForm(rapportSettings);
      });
  }

  canDeactivate(): Observable<boolean> | boolean {
    if (this.form.isChanged()) {
      return confirm(UNSAVED_CHANGES);
    } else {
      return true;
    }
  }

  get dataperiodFrom(): string[] {
    return Object.values(DatumPeriod);
  }

  dataPeriodFromText(dataPeriod: DatumPeriod): string {
    switch (dataPeriod) {
      case DatumPeriod.All:
        return "Alla";
      case DatumPeriod.Months6:
        return "6 månader";
      case DatumPeriod.Years1:
        return "1 år";
      case DatumPeriod.Years2:
        return "2 år";
      case DatumPeriod.Years5:
        return "5 år";
      case DatumPeriod.Years10:
        return "10 år";
      default:
        return "invalid dataPeriod: " + dataPeriod;
    }
  }

  get bildlagesId(): number {
    return this.form.controls.lagesbildId.value;
  }

  set bildlagesId(id: number) {
    const fc = this.form.get("lagesbildId");
    fc.setValue(id);
    fc.markAsDirty();
  }

  get grafer(): RapportGrafSettings[] {
    return this.form.controls.rapportGrafer.value;
  }

  set grafer(value: RapportGrafSettings[]) {
    this.form.controls.rapportGrafer.setValue(value);
  }

  isEditing(): boolean {
    return this.rapportId !== 0;
  }

  addGraf(): void {
    const graf = grafSettingsEmpty();
    graf.rubrik = "Graf " + (this.grafer.length + 1);

    this.grafer = this.grafer.add(graf);
  }

  removeGraf(grafId: number) {
    this.expandPanel = false;
    if (confirm("Vill du ta bort grafen " + this.grafer[grafId].rubrik + "?")) {
      this.grafer = this.grafer.filter(g => g !== this.grafer[grafId]);
      this.expandPanel = true;
    }
  }

  hasGrafer(): boolean {
    return this.grafer.length > 0;
  }

  get mottagare(): RapportMottagare[] {
    return this.form.controls.mottagare.value;
  }

  set mottagare(value: RapportMottagare[]) {
    this.form.controls.mottagare.setValue(value);
  }

  addMottagare() {
    this.popupService.addRapportMottagare(this.mottagare)
                     .pipe(filter(m => m != null))
                     .subscribe(m => this.mottagare = this.mottagare.add(m));
  }

  removeMottagare(id: number) {
    this.mottagare = this.mottagare.removeAtIndex(id);
  }

  hasMottagare(): boolean {
    return this.mottagare.length > 0;
  }

  goBack() {
    this.location.back();
  }

  delete() {
    if (confirm("Är du verkligen säker på att du vill ta bort rapporten?")) {
      this.rapportService.deleteRapport(this.rapportId).subscribe(
        () => this.goBack(),
        (error) => this.saveFailure(RestError.DELETE_RAPPORT)
      );
    }
  }

  canPreview() {
    return this.form && !this.form.isChanged() && this.rapportSettings.id != null;
  }

  preview() {
    const httpParams = new HttpParams();

    const newWindow = window.open();

    return this.httpClient
      .get(`/api/rapport/${this.rapportSettings.id}/pdf`, {
        params: httpParams,
        responseType: "blob",
        observe: "response"
      })
      .toPromise()
      .then(response => {
        const file = new Blob([response.body], {type: "application/pdf"});
        const fileURL = window.URL.createObjectURL(file);
        newWindow.location.href = fileURL;
      });
  }

  canSave(): boolean {
    return this.form && this.form.isChanged() && this.form.valid &&
      this.graferValidator(this.form.value.rapportGrafer);
  }

  save(form: UntypedFormGroup) {
    this.error = null;
    this.saving = true;

    if (this.isEditing()) {
      const rapport = this.editRapportSettings(this.form);
      this.rapportService.putRapportSettings(this.rapportId, rapport).subscribe(
        rapportSettings => this.saveSuccess(form, rapportSettings),
        (error) => this.saveFailure(RestError.PUT_RAPPORT)
      );
    } else {
      const rapport = this.createRapportSettings(this.form);
      this.rapportService.postRapportSettings(rapport).subscribe(
        rapportSettings => this.saveSuccess(form, rapportSettings),
        (error) => this.saveFailure(RestError.POST_RAPPORT)
      );
    }
  }

  rapportnamnErrorMessage(): string {
    const fc = this.form.controls.name;
    if (fc.hasError("required")) {
      return "Fältet är obligatoriskt";
    } else if (fc.hasError("uniqueName")) {
      return "Rapportnamnet existerar redan";
    } else {
      return "Okänt fel";
    }
  }

  datetimeErrorMessage(fc: AbstractControl): string | null {
    if (fc.hasError("invalidTime")) {
      return "Ogiltig tid";
    } else if (fc.hasError("invalidDate")) {
      return "Ogiltigt datum";
    } else if (fc.hasError("required") && fc.touched) {
      return "Fältet är obligatoriskt";
    } else {
      return null;
    }
  }

  senastSkickad(): string {
    return this.rapportSettings.senastSkickad.slice(0, 10);
  }

  private createForm(rapportSettings: Partial<RapportSettings>) {
    return new FormGroupUnsaved({
      name: new UntypedFormControl(rapportSettings.namn, [], [this.uniqueNameValidator.bind(this)]),
      active: new UntypedFormControl(rapportSettings.aktiv || false),
      description: new UntypedFormControl(rapportSettings.beskrivning),
      dataperiodFrom: new UntypedFormControl(rapportSettings.dataperiodFrom || DatumPeriod.Years2),
      tidpunkt: new UntypedFormControl(),
      tidsintervall: new UntypedFormControl(rapportSettings.tidsintervall || Tidsintervall[0].value),
      introductionTitle: new UntypedFormControl(rapportSettings.inledningRubrik),
      introductionInformation: new UntypedFormControl(rapportSettings.inledningInformation),
      lagesbildId: new UntypedFormControl(rapportSettings.lagesbild),
      mottagare: new UntypedFormControl(rapportSettings.rapportMottagare || []),
      rapportGrafer: new UntypedFormControl(rapportSettings.rapportGraf || []),
      mailMessage: new UntypedFormControl(rapportSettings.mejlmeddelande || this.defautMailMessage),
      rorelsereferensdatum: new UntypedFormControl(
        this.datetimeToMoment(rapportSettings.rorelsereferensdatum)
      ),

      startDatum: new UntypedFormControl(this.datetimeToMoment(rapportSettings.startDatum)),
    });
  }

  private createRapportSettings(form: UntypedFormGroup): Partial<RapportSettings> {
    const now = this.momentToDatetime(moment());

    return {
      namn: form.controls.name.value,
      aktiv: form.controls.active.value,
      beskrivning: form.controls.description.value,
      dataperiodFrom: form.controls.dataperiodFrom.value,
      rorelsereferensdatum: this.momentToDatetime(form.controls.rorelsereferensdatum.value),
      tidsintervall: form.controls.tidsintervall.value,
      startDatum: this.momentToDatetime(form.controls.startDatum.value),
      inledningRubrik: form.controls.introductionTitle.value,
      inledningInformation: form.controls.introductionInformation.value,
      lagesbild: form.controls.lagesbildId.value,
      rapportGraf: form.controls.rapportGrafer.value,
      rapportMottagare: form.controls.mottagare.value,
      mejlmeddelande: form.controls.mailMessage.value,
    };
  }

  private editRapportSettings(form: UntypedFormGroup): Partial<RapportSettings> {
    const now = this.momentToDatetime(moment());

    return {
      namn: form.controls.name.value,
      aktiv: form.controls.active.value,
      beskrivning: form.controls.description.value,
      dataperiodFrom: form.controls.dataperiodFrom.value,
      rorelsereferensdatum: this.momentToDatetime(form.controls.rorelsereferensdatum.value),
      tidsintervall: form.controls.tidsintervall.value,
      startDatum: this.momentToDatetime(form.controls.startDatum.value),
      inledningRubrik: form.controls.introductionTitle.value,
      inledningInformation: form.controls.introductionInformation.value,
      lagesbild: form.controls.lagesbildId.value,
      rapportGraf: form.controls.rapportGrafer.value,
      rapportMottagare: form.controls.mottagare.value,
      mejlmeddelande: form.controls.mailMessage.value,
    };
  }

  private saveSuccess(form: UntypedFormGroup, rapportSettings: RapportSettings) {
    this.saving = false;
    this.savedStartDatumMoment = this.form.controls.startDatum.value;
    form.markAsUntouched();
    form.markAsPristine();
    form.updateValueAndValidity();

    if (rapportSettings) {
      this.router.navigate([`/editrapport/${rapportSettings.id}`], {relativeTo: this.route});
    }

  }

  private saveFailure(error: RestError) {
    this.saving = false;
    this.error = error;
  }

  private graferValidator(grafer: RapportGrafSettings[]): boolean {
    if (grafer.length === 0) {
      return false;
    } else if (grafer.some(g => g.rubrik === "" || g.rubrik == null)) {
      return false;
    } else if (grafer.some(g => !this.hasMatserier(g))) {
      return false;
    }

    return true;
  }

  private hasMatserier(graf: RapportGrafSettings): boolean {
    return graf.gransvarden.length > 0 ||
           graf.matningstyper.length > 0;
  }

  private uniqueNameValidator(control: AbstractControl): Observable<ValidationErrors | null> {
    if (control.value == null || control.value === this.savedRapportName) {
      return of(null);
    }

    return timer(1000).pipe(
      switchMap(() => this.rapportService.rapportNameExists(control.value)),
      map(res => res.result ? { uniqueName: true } : null)
    );
  }

  private datetimeToMoment(datetime: string): Moment | null {
    return datetime == null ? null : moment(datetime);
  }

  private momentToDatetime(m: Moment): string | null {
    if (m == null) {
      return null;
    }

    const iso = m.toISOString(true);
    const date = iso.slice(0, 10);
    const hoursMinutes = iso.slice(11, 16);
    return date + "T" + hoursMinutes + ":00";
  }
}
