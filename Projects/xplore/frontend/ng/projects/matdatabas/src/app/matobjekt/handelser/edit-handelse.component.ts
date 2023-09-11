import {Location} from "@angular/common";
import {HttpErrorResponse} from "@angular/common/http";
import {Component, OnInit} from "@angular/core";
import {UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable, of} from "rxjs";
import {catchError} from "rxjs/operators";
import {ConfirmationDialogModel} from "../../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import {DialogService} from "../../../../../lib/dialogs/dialog.service";
import {MatobjektService, Handelse, SaveHandelse} from "../../services/matobjekt.service";
import {isTouch} from "../../common/touch-utils";
import moment, {Moment} from "moment";
import {UserService} from "../../services/user.service";
import {UNSAVED_CHANGES} from "../../services/can-deactivate-guard.service";
import {FormGroupUnsaved} from "../../common/form-group-unsaved";

export enum RestError {
  GET_HANDELSE = "Misslyckades hämta händelse. Ladda om sidan för att försöka på nytt.",
  POST_HANDELSE = "Misslyckades lagra händelse. Försök igen om en liten stund",
  PUT_HANDELSE = "Misslyckades uppdatera händelse. Försök igen om en liten stund",
  DELETE_HANDELSE = "Misslyckades radera händelse. Försök igen om en liten stund"
}

@Component({
  selector: "mdb-edit-handelse",
  template: `
    <div class="main-content">
      <h2>Händelse</h2>
      <form *ngIf="form" [formGroup]="form">
        <div class="form-controls-horizontal">
          <mat-form-field>
            <input matInput placeholder="Benämning" type="text" formControlName="benamning" maxlength="60" required>
            <mat-error *ngIf="form.get('benamning').hasError('required')">Fältet är obligatoriskt</mat-error>
          </mat-form-field>
          <mat-form-field>
            <mat-label>Datum</mat-label>
            <input matInput placeholder="åååå-mm-dd" required [matDatepicker]="myDatepicker" formControlName="datum">
            <mat-datepicker-toggle matSuffix [for]="myDatepicker"></mat-datepicker-toggle>
            <mat-datepicker [touchUi]="isTouch" #myDatepicker></mat-datepicker>
            <mat-error *ngIf="form.get('datum').hasError('required')">Fältet är obligatoriskt</mat-error>
          </mat-form-field>

          <mat-form-field>
            <mat-label>Klockslag</mat-label>
            <input matInput placeholder="tt:mm"
                   pattern="([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9]){0,1}"
                   required formControlName="tid">
            <mat-error *ngIf="form.get('tid').hasError('required')">Fältet är obligatoriskt</mat-error>
          </mat-form-field>
        </div>
        <div class="form-controls-vertical">
          <mat-form-field>
            <textarea matInput mat-autosize="true" placeholder="Beskrivning" formControlName="beskrivning"
              maxlength="500" matAutosizeMinRows=4></textarea>
          </mat-form-field>
          <h3>Bilder</h3>
          <mdb-dokument [ids]="form.get('bifogadebilderIds').value" [accept]="getAcceptTypes()" [acceptFunc]="acceptFile"
                        [readonly]="readonly"
            (uploading)="onDokumentUploading($event)" laggTillText="bild"
            (updated)="onDokumentUpdated($event, form)"></mdb-dokument>
          </div>
        <div class="actions">
          <button type="button" mat-stroked-button color="primary" (click)="goBack()">Tillbaka</button>
          <button type="button" mat-raised-button color="primary" (click)="onSave(form)"
            [disabled]="!allowedToSave(form)">Spara</button>
          <button mat-raised-button *ngIf="handelseId && !readonly" type="button" (click)="onTaBort()" color="warn">Ta bort</button>
        </div>
      </form>
      <p *ngIf="error" class="rest-error">{{error}}</p>
    </div>
    `,
  styles: [`

    .form-controls-horizontal {
      display: grid;
      grid-row-gap: 0.5rem;
      grid-column-gap: 1rem;
      grid-template-columns: auto auto;
    }

    .form-controls-vertical {
      display: grid;
      grid-gap: 1rem;
    }
  `]
})
export class EditHandelseComponent implements OnInit {

  static acceptTypes = ["image/jpeg", "image/png"];
  form: FormGroupUnsaved;
  matobjektId: number;
  handelseId: number;
  dokumentUploading = false;
  error: RestError = null;
  isTouch = isTouch();
  readonly = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private matobjektService: MatobjektService,
    private dialogService: DialogService,
    private userService: UserService,
    private formBuilder: UntypedFormBuilder) {
  }

  ngOnInit() {
    if (this.userService.userDetails.isTillstandshandlaggare()) {
      this.readonly = false;
    }

    this.matobjektId = +this.route.snapshot.parent.paramMap.get("id");
    this.handelseId = +this.route.snapshot.paramMap.get("id");
    if (this.handelseId) {
      this.matobjektService.getHandelse(this.matobjektId, this.handelseId).subscribe(handelse => {
        this.initForm(handelse);
      },
        catchError((error: HttpErrorResponse) => {
          this.error = RestError.GET_HANDELSE;
          return of(null);
        })
      );
    } else {
      let now = new Date().toLocaleString();
      now = now.substring(0, now.lastIndexOf(":")).replace(" ", "T");
      this.initForm({
        id: null,
        benamning: "",
        beskrivning: "",
        foretag: "",
        datum: now,
        bifogadebilder: []
      });
    }
  }

  canDeactivate(): Observable<boolean> | boolean {
    if (this.form.isChanged()) {
      return confirm(UNSAVED_CHANGES);
    } else {
      return true;
    }
  }

  initForm(handelse: Handelse) {
    const defaultDateTime = handelse.datum.split("T");
    const defaultDate = moment(defaultDateTime[0]);
    const defaultTime = defaultDateTime[1];

    this.form = new FormGroupUnsaved({
      benamning: new UntypedFormControl(handelse.benamning, Validators.required),
      beskrivning: new UntypedFormControl(handelse.beskrivning),
      datum: new UntypedFormControl(defaultDate),
      tid: new UntypedFormControl(defaultTime),
      bifogadebilderIds: new UntypedFormControl(handelse.bifogadebilder.map(f => f.id))
    });

    if (this.readonly) {
      this.form.disable();
    }
  }

  getAcceptTypes(): string {
    return EditHandelseComponent.acceptTypes.join(",");
  }

  goBack() {
    this.location.back();
  }

  allowedToSave(form: UntypedFormGroup): boolean {
    return form.valid && form.dirty;
  }

  onDokumentUploading(active: boolean) {
    this.dokumentUploading = active;
  }

  onDokumentUpdated(ids: number[], form: UntypedFormGroup) {
    const fc = form.get("bifogadebilderIds");
    fc.setValue(ids);
    fc.markAsDirty();
  }

  onSave(form: UntypedFormGroup) {
    this.error = null;

    const handelse = this.form.value;

    const datum: Moment = handelse.datum;
    const tid = moment(handelse.tid, "hh:mm");

    datum.hour(tid.hour());
    datum.minutes(tid.minutes());

    const datumString = datum.local().format("YYYY-MM-DDTHH:mm");

    const handelseToServer: SaveHandelse = {...handelse, datum: datumString};

    if (this.handelseId) {
      this.matobjektService.updateHandelse(this.matobjektId, this.handelseId, handelseToServer).subscribe(
        () => {
          this.form.markAsPristine();
          this.goBack();
          },
        () => this.error = RestError.POST_HANDELSE);
    } else {
      this.matobjektService.addHandelse(this.matobjektId, handelseToServer).subscribe(
        () => {
          this.form.markAsPristine();
          this.goBack();
        },
        () => this.error = RestError.PUT_HANDELSE);
    }
  }

  onTaBort() {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Ta bort",
        "<p>Är du säker på att du vill ta bort händelsen?<p>Operationen går inte att ångra.",
        "Avbryt",
        "Ta bort"),
      this.handleConfirmationDialogTaBort.bind(this));
  }

  handleConfirmationDialogTaBort(dialogResult: boolean) {
    if (dialogResult) {
      if (this.handelseId) {
        this.matobjektService.deleteHandelse(this.matobjektId, this.handelseId).subscribe(
          () => {
            this.form.markAsPristine();
            this.goBack();
          },
          () => this.error = RestError.DELETE_HANDELSE);
      } else {
        this.goBack();
      }
    }
  }

  acceptFile(file: File): boolean {
    if (!EditHandelseComponent.acceptTypes.some(t => t === file.type)) {
      this.dialogService.showConfirmationDialog(
        new ConfirmationDialogModel(
          "Ogiltig filtyp",
          "Bifogad bild måste vara av typen JPG eller PNG.",
          "Avbryt",
          "Ok"),
        () => {}
      );
      return false;
    }
    return true;
  }
}
