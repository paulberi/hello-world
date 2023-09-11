import {Component, OnInit, ViewChild} from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import {Observable, of, timer} from "rxjs";
import {catchError, map, switchMap} from "rxjs/operators";
import {Matobjekt, MatobjektService} from "../../services/matobjekt.service";
import {AbstractControl, UntypedFormControl, UntypedFormGroup, ValidationErrors, Validators} from "@angular/forms";
import {MatobjektgruppService} from "../../services/matobjektgrupp.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import {Location} from "@angular/common";
import {ValidationResult} from "../../common/validation-result";
import {ConfirmationDialogModel} from "../../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import {DialogService} from "../../../../../lib/dialogs/dialog.service";
import {MatobjektTyp} from "../matobjekt-typ";
import {UserService} from "../../services/user.service";
import {UNSAVED_CHANGES} from "../../services/can-deactivate-guard.service";
import {FormGroupUnsaved} from "../../common/form-group-unsaved";

@Component({
  selector: "mdb-grunduppgifter",
  template: `
    <form *ngIf="form | async as form" [formGroup]="form">
      <div class="generella-uppgifter">
        <h3>Generella uppgifter</h3>
        <div class="generella-uppgifter-content">
          <mat-form-field class="namn">
            <input matInput placeholder="Namn" type="text" formControlName="namn" maxlength="130" required>
            <mat-error *ngIf="form.get('namn').hasError('required')">Fältet är obligatoriskt</mat-error>
            <mat-error *ngIf="form.get('namn').hasError('namnExists')">Namnet är upptaget</mat-error>
            <mat-error *ngIf="form.get('namn').hasError('minlength')">Namnet är för kort</mat-error>
          </mat-form-field>
          <mat-form-field class="typ">
            <select matNativeControl placeholder="Typ" formControlName="typ">
              <option *ngFor="let mt of MatobjektTyp | keyvalue" [value]="mt.key">{{mt.value}}</option>
            </select>
          </mat-form-field>
          <mat-checkbox formControlName="aktiv">Aktiv</mat-checkbox>
          <mat-checkbox formControlName="kontrollprogram">Kontrollprogram</mat-checkbox>
        </div>
      </div>
      <div class="bild-pa-matobjektet">
        <h3>Bild på mätobjektet</h3>
        <mdb-grunduppgifter-bild [id]="form.get('bifogadBildId').value" (uploading)="onBildUploading($event)"
                                 [readonly]="readonly"
                                 (updated)="onBildUpdated($event, form)"></mdb-grunduppgifter-bild>
      </div>
      <div class="geografisk-plats">
        <h3>Geografisk plats</h3>
        <div class="geografisk-plats-content">
          <mat-form-field>
            <input matInput placeholder="Position N" type="number" formControlName="posN" maxlength="12" required>
            <mat-error *ngIf="form.get('posN').hasError('required')">Fältet är tomt eller ogiltigt</mat-error>
          <mat-error *ngIf="form.get('posN').hasError('pattern')">Talet måste vara mindre än 10^7 och ha högst 3 decimaler</mat-error>
          </mat-form-field>

          <mat-form-field>
            <input matInput placeholder="Position E" type="number" formControlName="posE" maxlength="12" required>
            <mat-error *ngIf="form.get('posE').hasError('required')">Fältet är tomt eller ogiltigt</mat-error>
          <mat-error *ngIf="form.get('posE').hasError('pattern')">Talet måste vara mindre än 10^7 och ha högst 3 decimaler</mat-error>
          </mat-form-field>

          <mat-form-field class="fastighet">
            <input matInput placeholder="Fastighet" type="text" formControlName="fastighet" maxlength="60">
          </mat-form-field>

          <mat-form-field class="lage">
          <textarea matInput mat-autosize="true" placeholder="Läge"
                    formControlName="lage" maxlength="500" matAutosizeMinRows=4></textarea>
          </mat-form-field>
        </div>
      </div>
      <div class="gruppering" *ngIf="userService.userDetails.isObservator()">
        <h3>Gruppering</h3>
        <mdb-grunduppgifter-grupper [grupper]="form.get('matobjektgrupper').value"
                                    [readonly]="readonly"
                                    (updated)="onMatobjektgrupperUpdated($event, form)"></mdb-grunduppgifter-grupper>
      </div>
      <div class="dokument" *ngIf="userService.userDetails.isObservator()">
        <h3>Dokument</h3>
        <mdb-grunduppgifter-dokument [ids]="form.get('dokument').value" (uploading)="onDokumentUploading($event)"
                                     [readonly]="readonly"
                                     (updated)="onDokumentUpdated($event, form)"></mdb-grunduppgifter-dokument>
      </div>

      <div class="actions">
        <button type="button" mat-stroked-button color="primary" (click)="goBack()">Tillbaka</button>
        <mdb-save-button [label]="'Spara'" (clicked)="onSave(form)" [saving]="saving" [disabled]="!allowedToSave(form)"></mdb-save-button>
        <div *ngIf="matobjektId" [matTooltip]="getTooltip(canDelete | async)">
          <button mat-raised-button type="button" (click)="onTaBort()"
                  [disabled]="!(canDelete | async) || saving" color="warn">Ta bort
          </button>
        </div>
      </div>
    </form>
    <p *ngIf="error" class="rest-error">{{error}}</p>
  `,
  styles: [`
    form {
      display: grid;
      grid-row-gap: 1rem;
      grid-column-gap: 2rem;
      grid-template-columns: 1fr 1fr;
    }

    .generella-uppgifter {
      display: grid;
      grid-gap: 1rem;
    }

    .generella-uppgifter-content {
      display: grid;
      grid-row-gap: 0.5rem;
      grid-column-gap: 1rem;
      grid-template-columns: auto auto;
      align-items: center;
    }

    .bild-pa-matobjektet {
      display: grid;
      grid-gap: 1rem;
      grid-column-start: 2;
      grid-column-end: 3;
      grid-row-start: 1;
      grid-row-end: 3;
    }

    .geografisk-plats {
      display: grid;
      grid-gap: 0.5rem;
      grid-row-start: 2;
      grid-row-end: 3;
    }

    .geografisk-plats-content {
      display: grid;
      grid-row-gap: 0.5rem;
      grid-column-gap: 1rem;
      grid-template-columns: auto auto;
    }

    .namn, .typ, .lage, .fastighet {
      grid-column-start: 1;
      grid-column-end: 3;
    }

    .gruppering {
      display: grid;
      grid-gap: 1rem;
      grid-row-start: 3;
      grid-row-end: 4;
      grid-column-start: 1;
      grid-column-end: 3;
    }

    .dokument {
      display: grid;
      grid-gap: 1rem;
      grid-row-start: 4;
      grid-row-end: 5;
      grid-column-start: 1;
      grid-column-end: 3;
    }

    .actions {
      display: grid;
      grid-gap: 1rem;
      grid-row-start: 5;
      grid-row-end: 6;
    }

    @media only screen and (min-width: 576px) {
      .actions {
        grid-template-columns: auto auto auto;
        justify-content: left;
        grid-gap: 1rem;
      }
    }

    .mat-column {
      width: 15%;
    }

    .mat-column-lage {
      width: 50%;
    }

    .mat-column-aktiv {
      width: 5%;
      text-align: right;
    }
  `]
})
export class GrunduppgifterComponent implements OnInit {

  public MatobjektTyp = MatobjektTyp;
  public Object = Object;

  form: Observable<FormGroupUnsaved>;
  formIsDirty = false;
  matobjektId: number;
  matobjektnamn: string;

  canDelete: Observable<boolean>;
  bildUploading = false;
  dokumentUploading = false;
  saving: boolean;

  RestError = RestError;
  error: RestError = null;

  positionRegex = /^[0-9]{1,7}([\\.|\\,][0-9]{1,3})?$/;

  readonly = true;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  getTooltip(ok) {
    return  ok ? null : "Mätobjektet innehåller mätningar";
  }

  constructor(private route: ActivatedRoute, private router: Router, private location: Location,
              private matobjektService: MatobjektService, private matobjektgruppService: MatobjektgruppService,
              private dialogService: DialogService, public userService: UserService) { }

  canDeactivate(): Observable<boolean> | boolean {
    if (this.formIsDirty) {
      return confirm(UNSAVED_CHANGES);
    } else {
      return true;
    }
  }

  ngOnInit() {
    if (this.userService.userDetails.isTillstandshandlaggare()) {
      this.readonly = false;
    }

    this.form = this.route.parent.paramMap.pipe(
      switchMap((params: ParamMap) => {
          this.matobjektId = +params.get("id");
          if (this.matobjektId) {
            return this.matobjektService.get(this.matobjektId);
          } else {
            return of({
              typ: 0,
              namn: "",
              aktiv: true,
              kontrollprogram: true,
              posN: null,
              posE: null,
              fastighet: "",
              lage: "",
              bifogadBildId: null,
              matobjektgrupper: [],
              matrundor: [],
              dokument: []
            });
          }
        }
      ),
      map((matobjekt: Matobjekt) => {
        this.matobjektnamn = matobjekt.namn;
        if (this.matobjektId) {
          this.canDelete = this.matobjektService.canDelete(this.matobjektId);
        }

        const newForm = new FormGroupUnsaved({
          typ: new UntypedFormControl(Object.keys(MatobjektTyp)[matobjekt.typ]),
          namn: new UntypedFormControl(matobjekt.namn,
            [Validators.required, Validators.minLength(1)],
            [this.checkMatobjektnamn.bind(this)]),
          aktiv: new UntypedFormControl(matobjekt.aktiv),
          kontrollprogram: new UntypedFormControl(matobjekt.kontrollprogram),
          posN: new UntypedFormControl(matobjekt.posN,
            [Validators.required, Validators.pattern(this.positionRegex)]),
          posE: new UntypedFormControl(matobjekt.posE,
            [Validators.required, Validators.pattern(this.positionRegex)]),
          fastighet: new UntypedFormControl(matobjekt.fastighet),
          lage: new UntypedFormControl(matobjekt.lage),
          bifogadBildId: new UntypedFormControl(matobjekt.bifogadBildId),
          matobjektgrupper: new UntypedFormControl(matobjekt.matobjektgrupper),
          matrundor: new UntypedFormControl(matobjekt.matrundor),
          dokument: new UntypedFormControl(matobjekt.dokument)
        });

        if (this.readonly) {
          newForm.disable();
        }

        newForm.valueChanges.subscribe( v => this.formIsDirty = newForm.isChanged());

        return newForm;
      }),
      catchError((error: HttpErrorResponse) => {
        this.error = RestError.GET_MATOBJEKT;
        return of(null);
      })
    );
  }

  goBack() {
    this.location.back();
  }

  checkMatobjektnamn({value}: AbstractControl): Observable<ValidationErrors | null> {
    return timer(1000).pipe(
      switchMap(() => {
          return (value === this.matobjektnamn) ? of(null) :
            this.matobjektService.namnExists(value).pipe(
              map((response: ValidationResult) => response.result ? {namnExists: true} : null)
            );
        }
      )
    );
  }

  getMatobjektTypNamn(typ: number) {
    return Object.values(MatobjektTyp)[typ];
  }

  allowedToSave(form: UntypedFormGroup): boolean {
    return form.valid && form.dirty && !this.bildUploading && !this.dokumentUploading;
  }

  onBildUploading(active: boolean) {
    this.bildUploading = active;
  }

  onBildUpdated(id: number, form: UntypedFormGroup) {
    const fc = form.get("bifogadBildId");
    fc.setValue(id);
    fc.markAsDirty();
  }

  onDokumentUploading(active: boolean) {
    this.dokumentUploading = active;
  }

  onDokumentUpdated(ids: number[], form: UntypedFormGroup) {
    const fc = form.get("dokument");
    fc.setValue(ids);
    fc.markAsDirty();
  }

  onMatobjektgrupperUpdated(matobjektgrupper: number[], form: UntypedFormGroup) {
    const fc = form.get("matobjektgrupper");
    fc.setValue(matobjektgrupper);
    fc.markAsDirty();
  }

  onSave(form: UntypedFormGroup) {
    const value = form.value;
    value["typ"] = Object.keys(MatobjektTyp).indexOf(form.get("typ").value);

    this.error = null;
    this.saving = true;

    if (this.matobjektId) {
      this.matobjektService.put(this.matobjektId, form.value).subscribe(
        () => this.handleSaveSuccess(form),
        () => this.handleSaveFailure(RestError.PUT_MATOBJEKT));
    } else {
      this.matobjektService.post(form.value).subscribe(
        matobjekt => this.handleSaveSuccess(form, matobjekt),
        () => this.handleSaveFailure(RestError.POST_MATOBJEKT));
    }
  }

  handleSaveSuccess(form: UntypedFormGroup, matobjekt?: Matobjekt) {
    this.saving = false;
    form.markAsUntouched();
    form.markAsPristine();
    form.updateValueAndValidity();
    this.formIsDirty = false;

    if (matobjekt) {
      this.router.navigate([`../../${matobjekt.id}`], {relativeTo: this.route});
    }
  }

  handleSaveFailure(error: RestError) {
    this.saving = false;
    this.error = error;
  }

  onTaBort() {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Ta bort",
        "<p>Är du säker på att du vill ta bort mätobjektet?<p>Operationen går inte att ångra.",
        "Avbryt",
        "Ta bort"),
      this.handleConfirmationDialogTaBort.bind(this));
  }

  handleConfirmationDialogTaBort(dialogResult: boolean) {
    if (dialogResult) {
      this.matobjektService.delete(this.matobjektId).subscribe(
        () => { this.formIsDirty = false; this.goBack(); },
        () => this.error = RestError.DELETE_MATOBJEKT);
    }
  }

}

export enum RestError {
  GET_MATOBJEKT = "Misslyckades hämta grunduppgifter för mätobjektet. Ladda om sidan för att försöka på nytt.",
  POST_MATOBJEKT = "Misslyckades lagra mätobjektet. Försök igen om en liten stund",
  PUT_MATOBJEKT = "Misslyckades uppdatera mätobjektet. Försök igen om en liten stund",
  DELETE_MATOBJEKT = "Misslyckades radera mätobjektet. Försök igen om en liten stund"
}
