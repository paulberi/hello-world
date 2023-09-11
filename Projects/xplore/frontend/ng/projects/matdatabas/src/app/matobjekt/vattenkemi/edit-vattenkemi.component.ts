import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {Analys, Matning, Matningstyp, Matobjekt, MatobjektService} from "../../services/matobjekt.service";
import {AbstractControl, UntypedFormControl, UntypedFormGroup, ValidationErrors, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";
import {DialogService} from "../../../../../lib/dialogs/dialog.service";
import {Observable, of, Subject, timer} from "rxjs";
import {filter, finalize, map, switchMap, tap} from "rxjs/operators";
import {ValidationResult} from "../../common/validation-result";
import {DefinitionmatningstypService} from "../../services/definitionmatningstyp.service";
import {UserService} from "../../services/user.service";
import {isTouch} from "../../common/touch-utils";
import moment, {Moment} from "moment";
import {momentFromDateAndTime} from "../../common/date-utils";
import {FormGroupUnsaved} from "../../common/form-group-unsaved";
import {UNSAVED_CHANGES} from "../../services/can-deactivate-guard.service";

@Component({
  selector: "mdb-edit-vattenkemi",
  template: `
    <div *ngIf="matobjekt | async as matobjekt" class="main-content">
    <h2 *ngIf="showNamn">Mätobjekt: {{matobjekt.namn}}</h2>
    <div *ngIf="matobjekt.lage" class="lage">Läge: {{matobjekt.lage}}</div>
    <form *ngIf="form" [formGroup]="form">
      <div class="left-column">
        <div class="analys">
          <h3>Analys</h3>
          <ng-container formGroupName="analysTidpunkt">
            <mat-form-field>
                <mat-label>Datum</mat-label>
                <input matInput placeholder="åååå-mm-dd" required [matDatepicker]="myDatepicker" formControlName="analysDatum">
                <mat-datepicker-toggle matSuffix [for]="myDatepicker"></mat-datepicker-toggle>
                <mat-datepicker [touchUi]="isTouch" #myDatepicker></mat-datepicker>

                <mat-error *ngIf="form.get('analysTidpunkt.analysDatum').hasError('required')">Fältet är obligatoriskt</mat-error>
            </mat-form-field>
            <mat-form-field>
                <mat-label>Klockslag</mat-label>
                <input matInput placeholder="tt:mm"
                   pattern="([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9]){0,1}"
                   required formControlName="analysTid">

                <mat-error *ngIf="form.get('analysTidpunkt.analysTid').hasError('required')">Fältet är obligatoriskt</mat-error>
            </mat-form-field>
            <mat-error *ngIf="form.get('analysTidpunkt').hasError('analysDatumExists')">
              Mätobjektet har redan en analys med angiven tidstämpel</mat-error>
          </ng-container>

          <mat-form-field class="rapportor">
            <input matInput autocomplete="off" placeholder="Rapportör" type="text" formControlName="rapportor" maxlength="60" required>
            <mat-error *ngIf="form.get('rapportor').hasError('required')">Fältet är obligatoriskt</mat-error>
          </mat-form-field>

          <mat-form-field>
            <textarea matInput autocomplete="off" mat-autosize="true" placeholder="Kommentar"
                      formControlName="kommentar" maxlength="500" matAutosizeMinRows=6>
            </textarea>
          </mat-form-field>
        </div>

        <div class="rapporter">
          <h3>Rapporter</h3>
          <mdb-vattenkemi-rapporter [ids]="form.get('rapporter').value" (uploading)="onRapportUploading($event)"
                                       (updated)="onRapporterUpdated($event, form)"></mdb-vattenkemi-rapporter>
        </div>
      </div>

      <div class="right-column">
        <div class="matningar">
          <h3>Mätningar</h3>
          <mdb-vattenkemi-matningar *ngIf="matningar" [matningar]="matningar"
                                       (updated)="onMatningarUpdated($event)"></mdb-vattenkemi-matningar>
        </div>
      </div>
      <div class="actions">
        <button type="button" mat-stroked-button color="primary" (click)="goBack()">Tillbaka</button>
        <mdb-save-button [label]="'Rapportera'" (clicked)="saveAnalys(form)" [saving]="saving" [disabled]="!allowedToSave(form)">
        </mdb-save-button>
        <span *ngIf="saveOk" class="notice">Vattenkemi inrapporterat.</span>
      </div>
    </form>
    </div>
    <p *ngIf="error" class="rest-error">{{error}}</p>
  `,
  styles: [`
    .main-content, form, .analys, .rapporter, .matningar, .actions {
      display: grid;
      grid-gap: 1rem;
    }

    .lage {
      font-style: italic;
    }

    @media only screen and (min-width: 576px) {
      form {
        grid-template-columns: 1fr 1fr;
        grid-column-gap: 2rem;
      }
    }
  `]
})
export class EditVattenkemiComponent implements OnInit {

  @Input() matobjektId: number;
  @Input() analysId: number;
  @Output() isDirty: EventEmitter<boolean> = new EventEmitter<boolean>();

  form: FormGroupUnsaved;

  rapportUploading = false;
  saving: boolean;
  saveOk = false;
  matobjekt: Observable<Matobjekt>;
  matningar: Matning[];
  RestError = RestError;
  error: RestError = null;
  analysDatum: string;
  analys: Analys;
  showNamn = false;
  isTouch = isTouch();

  constructor(private route: ActivatedRoute, private router: Router, private location: Location,
              private matobjektService: MatobjektService, private dialogService: DialogService,
              private definitionMatningstypService: DefinitionmatningstypService,
              private userService: UserService) {
  }

  ngOnInit() {
    if (this.matobjektId) {
      this.initMatningar();
      this.initForm();
      this.showNamn = true;
    } else {
      this.matobjektId = +this.route.snapshot.parent.paramMap.get("id");
      this.analysId = +this.route.snapshot.paramMap.get("id");
      if (this.analysId) {
        this.matobjektService.getVattenkemi(this.matobjektId, this.analysId).subscribe(
          (analys: Analys) => {
            this.analys = analys;
            this.initMatningar(this.analys.matningar);
            this.initForm(this.analys);
          },
          () => this.error = RestError.GET_VATTENKEMI
        );
      } else {
        this.initMatningar();
        this.initForm();
      }

    }
    if (this.matobjektId) {
      this.matobjekt = this.matobjektService.get(this.matobjektId);
    }
  }

  canDeactivate(): Observable<boolean> | boolean {
    if (this.form.isChanged()) {
      return confirm(UNSAVED_CHANGES);
    } else {
      return true;
    }
  }

  initForm(analys?: Analys) {
    let defaultDate;
    let defaultTime;

    if (analys) {
      const defaultDateTime = analys.analysDatum.split("T");
      defaultDate = moment(defaultDateTime[0]);
      defaultTime = defaultDateTime[1];
    } else {
      defaultDate = moment();
      defaultTime = defaultDate.format("HH:mm");
    }

    this.analysDatum = momentFromDateAndTime(defaultDate, defaultTime)
      .local().format("YYYY-MM-DDTHH:mm");


    this.form = new FormGroupUnsaved({
      analysTidpunkt: new UntypedFormGroup({
        analysDatum: new UntypedFormControl(defaultDate,
          [Validators.required]),
        analysTid: new UntypedFormControl(defaultTime,
          [Validators.required],
          ),
      }, [], [this.checkAnalysdatum.bind(this)]),
      rapportor: new UntypedFormControl(analys ? analys.rapportor : this.userService.userDetails.company),
      kommentar: new UntypedFormControl(analys ? analys.kommentar : ""),
      rapporter: new UntypedFormControl(analys ? analys.rapporter.map(f => f.id) : []),
      matningar: new UntypedFormControl(analys ? analys.matningar : [])
    });
    this.form.valueChanges.subscribe(v => this.isDirty.emit(this.form.isChanged()));
  }

  allowedToSave(form: FormGroupUnsaved): boolean {
    return form.valid && form.isChanged() && !this.rapportUploading;
  }

  onRapportUploading(active: boolean) {
    this.rapportUploading = active;
  }

  onRapporterUpdated(ids: string[], form: UntypedFormGroup) {
    const fc = form.get("rapporter");
    fc.setValue(ids);
    fc.markAsDirty();
  }

  onMatningarUpdated(varde: Varde) {
    this.matningar[varde.id].avlastVarde = varde.value;
    this.matningar[varde.id].inomDetektionsomrade = varde.inomDetektion;
    this.matningar[varde.id].saved = false;
    this.form.markAsDirty();

  }

  saveAnalys(form: UntypedFormGroup) {
    this.saveOk = false;

    this.form.get("matningar").setValue(this.validMatningar);

    const analysValue = this.form.value;
    const analysDatumString = momentFromDateAndTime(analysValue.analysTidpunkt.analysDatum, analysValue.analysTidpunkt.analysTid)
      .local().format("YYYY-MM-DDTHH:mm");

    const analysToServer: Analys = {...analysValue, analysDatum: analysDatumString};

    if (this.analysId) {
      this.matobjektService.putVattenkemi(this.matobjektId, this.analysId, analysToServer).subscribe(
        analys => this.handleSaveSuccess(form, analys),
        () => this.handleSaveFailure(RestError.PUT_VATTENKEMI));
    } else {
      this.matobjektService.postVattenkemi(this.matobjektId, analysToServer).subscribe(
        analys => this.handleSaveSuccess(form, analys),
        () => this.handleSaveFailure(RestError.POST_VATTENKEMI));
    }
  }

  handleSaveSuccess(form: UntypedFormGroup, analys?: Analys) {
    this.saving = false;
    this.error = null;
    this.saveOk = true;
    if (analys) {
      this.analysId = analys.id;
      this.updateMatningar(analys.matningar);
      this.initForm(analys);
    }
    form.markAsUntouched();
    form.markAsPristine();
    form.updateValueAndValidity();
  }

  updateMatningar(matningar: Matning[]) {
    this.matningar.map(matning => {
      matningar.map(updatedMatning => {
        if (matning.matningstypId === updatedMatning.matningstypId) {
          matning.id = updatedMatning.id;
          matning.inomDetektionsomrade = updatedMatning.inomDetektionsomrade;
          matning.avlastVarde = updatedMatning.avlastVarde;
          matning.avlastDatum = updatedMatning.avlastDatum;
          matning.rapportor = updatedMatning.rapportor;
          matning.status = updatedMatning.status;
          matning.saved = true;
        }
      });
    });
  }

  handleSaveFailure(error: RestError) {
    this.saving = false;
    this.saveOk = false;
    this.error = error;
  }

  checkAnalysdatum({value}: AbstractControl): Observable<ValidationErrors | null> {
    return timer(1000).pipe(
      switchMap(() => {
        const analysDatumString = momentFromDateAndTime(value.analysDatum, value.analysTid)
          .local().format("YYYY-MM-DDTHH:mm");

        return (analysDatumString === this.analysDatum) ? of(null) :
            this.matobjektService.analysExists(this.matobjektId, analysDatumString).pipe(
              map((response: ValidationResult) => response.result ? {analysDatumExists: true} : null)
            );
        }
      )
    );
  }

  initMatningar(matningar?: Matning[]) {
    const mat: Matning[] = [];

    this.matobjektService.getMatningstyper(this.matobjektId).subscribe((matningstyper: Matningstyp[]) => {
      matningstyper.map(matningstyp => {
        this.definitionMatningstypService.get(matningstyp.definitionMatningstypId).subscribe(definition => {
          mat.push({id: null, matningstypId: matningstyp.id, avlastDatum: null, avlastVarde: null,
            beraknatVarde: null, felkod: null, inomDetektionsomrade: 1, kommentar: null, rapportor: null, status: 0,
            namn: definition.namn, enhet: matningstyp.enhet, saved: true});
          if (matningstyper.length === mat.length) {
            this.matningar = mat;
            if (matningar) {
              this.updateMatningar(matningar);
            }
          }
        });
      });
    });
  }

  get validMatningar(): Matning[] {
    return this.matningar.filter(matning => (matning.avlastVarde && !matning.saved));
  }

  goBack() {
    this.location.back();
  }
}

export interface Varde {
  id: number;
  value: number;
  inomDetektion: number;
}

export enum RestError {
  GET_VATTENKEMI = "Misslyckades hämta analysen för vattenkemi. Ladda om sidan för att försöka på nytt.",
  POST_VATTENKEMI = "Misslyckades lagra analysen för vattenkemi. Försök igen om en liten stund",
  PUT_VATTENKEMI = "Misslyckades uppdatera analysen för vattenkemi. Försök igen om en liten stund",
  DELETE_VATTENKEMI = "Misslyckades radera analysen för vattenkemi. Försök igen om en liten stund"
}
