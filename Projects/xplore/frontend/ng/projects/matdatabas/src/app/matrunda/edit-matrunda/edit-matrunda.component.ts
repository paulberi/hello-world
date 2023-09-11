import {Component, OnInit} from "@angular/core";
import {Observable, of, timer} from "rxjs";
import {catchError, map, switchMap} from "rxjs/operators";
import {AbstractControl, UntypedFormControl, UntypedFormGroup, ValidationErrors, Validators} from "@angular/forms";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import {Location} from "@angular/common";
import {ValidationResult} from "../../common/validation-result";
import {ConfirmationDialogModel} from "../../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import {DialogService} from "../../../../../lib/dialogs/dialog.service";
import {Matrunda, MatrundaMatningstyp, MatrundaService} from "../../services/matrunda.service";
import {ViewMode} from "../../common/view-mode";
import {UserService} from "../../services/user.service";
import {UNSAVED_CHANGES} from "../../services/can-deactivate-guard.service";
import {FormGroupUnsaved} from "../../common/form-group-unsaved";

@Component({
  selector: "mdb-edit-matrunda",
  template: `
    <form *ngIf="form | async as form" [formGroup]="form">
      <h3>{{ matrundaId ? ('Mätrunda: ' + matrundanamn) : 'Ny mätrunda'}}</h3>
      <div class="form-fields">
        <mat-form-field>
          <input matInput placeholder="Namn" type="text" formControlName="namn" maxlength="130" required>
          <mat-error *ngIf="form.get('namn').hasError('required')">Fältet är obligatoriskt</mat-error>
          <mat-error *ngIf="form.get('namn').hasError('namnExists')">Namnet är upptaget</mat-error>
          <mat-error *ngIf="form.get('namn').hasError('minlength')">Namnet är för kort</mat-error>
        </mat-form-field>
        <mat-checkbox formControlName="aktiv">Aktiv</mat-checkbox>
        <mat-form-field class="beskrivning">
            <textarea matInput mat-autosize="true" placeholder="Beskrivning" formControlName="beskrivning"
                      maxlength="500" matAutosizeMinRows=2></textarea>
        </mat-form-field>
      </div>

      <div class="matningstyper">
        <h3>Mätobjekt</h3>
        <div class="matningstyper-content" [ngClass]="{'modifying': viewMode !== ViewMode.VIEW}">
          <ng-container [ngSwitch]="viewMode">
            <mdb-edit-matrunda-view-matningstyper *ngSwitchCase="ViewMode.VIEW"
                                                  [matningstyper]="form.get('matningstyper').value">
            </mdb-edit-matrunda-view-matningstyper>
            <mdb-edit-matrunda-edit-matningstyper *ngSwitchCase="ViewMode.EDIT"
                                                  [matningstyper]="form.get('matningstyper').value"
                                                  (updated)="onOrderUpdated($event, form)">
            </mdb-edit-matrunda-edit-matningstyper>
            <mdb-edit-matrunda-search-matningstyper *ngSwitchCase="ViewMode.SEARCH"
                                                    [initialFilter]="getMatningstypMatobjektFilter(form)"
                                                    (selected)="onMatningstyperUpdated($event, form)">
            </mdb-edit-matrunda-search-matningstyper>
          </ng-container>
          <div class="matningstyper-actions">
            <button *ngIf="!readonly && viewMode === ViewMode.VIEW" type="button"
                    (click)="viewMode = ViewMode.SEARCH"
                    mat-raised-button color="primary">Välj mätobjekt
            </button>
            <button *ngIf="!readonly && viewMode === ViewMode.VIEW && form.get('matningstyper').value.length" type="button"
                    (click)="viewMode = ViewMode.EDIT"
                    mat-raised-button color="primary">Ändra ordning
            </button>
            <button type="button" *ngIf="!readonly && viewMode !== ViewMode.VIEW"
                    (click)="viewMode = ViewMode.VIEW" mat-raised-button color="primary">Klar
            </button>
          </div>
        </div>
      </div>

      <div class="actions">
        <button type="button" mat-stroked-button color="primary" (click)="goBack()">Tillbaka</button>
        <mdb-save-button [label]="'Spara'" (clicked)="onSave(form)" [saving]="saving" [disabled]="!allowedToSave(form)"></mdb-save-button>
        <button mat-raised-button *ngIf="!readonly && matrundaId" type="button" (click)="onTaBort()"
                color="warn" [disabled]="saving">Ta bort</button>
      </div>
    </form>
    <p *ngIf="error" class="rest-error">{{error}}</p>
  `,
  styles: [`
    form {
      display: grid;
      grid-gap: 1rem;
    }

    .form-fields {
      display: grid;
      grid-gap: 1rem;
    }

    .matningstyper-content {
      display: grid;
      grid-gap: 1rem;
    }

    .modifying {
      border: 1px lightgrey dashed;
      border-radius: 5px;
      padding: 1rem;
      padding-bottom: 0;
      background: whitesmoke;
    }

    .matningstyper-actions {
      display: grid;
      grid-gap: 1rem;
      grid-template-columns: auto auto 1fr;
      padding-bottom: 1rem;
    }

    .actions {
      display: grid;
      grid-gap: 1rem;
    }

    @media only screen and (min-width: 576px) {
      .actions {
        grid-template-columns: auto auto auto;
        justify-content: left;
        grid-gap: 1rem;
      }

      .form-fields {
        grid-column-gap: 1rem;
        grid-template-columns: 4fr 1fr;
      }

      .beskrivning {
        grid-column-start: 1;
        grid-column-end: 3;
      }
    }
  `]
})
export class EditMatrundaComponent implements OnInit {

  ViewMode = ViewMode;

  form: Observable<FormGroupUnsaved>;
  formIsDirty: boolean;
  matrundaId: number;
  matrundanamn: string;
  viewMode: ViewMode = ViewMode.VIEW;
  saving: boolean;
  readonly = true;

  RestError = RestError;
  error: RestError = null;

  constructor(private route: ActivatedRoute, private router: Router, private location: Location,
              private userService: UserService,
              private matrundaService: MatrundaService, private dialogService: DialogService) {
  }

  ngOnInit() {
    if (this.userService.userDetails.isTillstandshandlaggare()) {
      this.readonly = false;
    }

    this.form = this.route.paramMap.pipe(
      switchMap((params: ParamMap) => {
          this.matrundaId = +params.get("id");
          if (this.matrundaId) {
            return this.matrundaService.get(this.matrundaId);
          } else {
            return of({
              id: null,
              namn: null,
              aktiv: true,
              beskrivning: "",
              matningstyper: []
            });
          }
        }
      ),
      map((matrunda: Matrunda) => {
        this.matrundanamn = matrunda.namn;

        const newForm = new FormGroupUnsaved({
          namn: new UntypedFormControl(matrunda.namn,
            [Validators.required, Validators.minLength(1)],
            [this.checkMatrundanamn.bind(this)]),
          aktiv: new UntypedFormControl(matrunda.aktiv),
          beskrivning: new UntypedFormControl(matrunda.beskrivning),
          matningstyper: new UntypedFormControl(matrunda.matningstyper)
        });

        if (this.readonly) {
          newForm.disable();
        }

        newForm.valueChanges.subscribe(v => this.formIsDirty = newForm.isChanged());

        return newForm;
      }),
      catchError((error: HttpErrorResponse) => {
        this.error = RestError.GET_MATRUNDA;
        return of(null);
      })
    );
  }

  canDeactivate(): Observable<boolean> | boolean {
    if (this.formIsDirty) {
      return confirm(UNSAVED_CHANGES);
    } else {
      return true;
    }
  }

  goBack() {
    this.location.back();
  }

  checkMatrundanamn({value}: AbstractControl): Observable<ValidationErrors | null> {
    return timer(1000).pipe(
      switchMap(() => {
          return (value === this.matrundanamn) ? of(null) :
            this.matrundaService.namnExists(value).pipe(
              map((response: ValidationResult) => response.result ? {namnExists: true} : null)
            );
        }
      )
    );
  }

  allowedToSave(form: UntypedFormGroup): boolean {
    return form.valid && form.dirty && this.viewMode === ViewMode.VIEW;
  }

  onMatningstyperUpdated(ids: number[], form: UntypedFormGroup) {
    const fc = form.get("matningstyper");
    const existing: MatrundaMatningstyp[] = fc.value;
    let modified: MatrundaMatningstyp[] = [];

    modified = existing.filter(mm => ids.find(id => id === mm.matningstypId));
    ids.forEach(id => {
      if (!existing.find(mm => mm.matningstypId === id)) {
        modified.push({matningstypId: id, ordning: null});
      }
    });

    for (let i = 0; i < modified.length; i++) {
      modified[i].ordning = i + 1;
    }

    fc.setValue(modified);
    fc.markAsDirty();
  }

  onOrderUpdated(matningstyper: MatrundaMatningstyp[], form: UntypedFormGroup) {
    const fc = form.get("matningstyper");
    fc.setValue(matningstyper);
    fc.markAsDirty();
  }

  onSave(form: UntypedFormGroup) {
    this.error = null;
    this.saving = true;

    if (this.matrundaId) {
      this.matrundaService.put(this.matrundaId, form.value).subscribe(
        () => this.handleSaveSuccess(form),
        () => this.handleSaveFailure(RestError.PUT_MATRUNDA));
    } else {
      this.matrundaService.post(form.value).subscribe(
        matrunda => this.handleSaveSuccess(form, matrunda),
        () => this.handleSaveFailure(RestError.POST_MATRUNDA));
    }
  }

  handleSaveSuccess(form: UntypedFormGroup, matrunda?: Matrunda) {
    this.saving = false;
    form.markAsUntouched();
    form.markAsPristine();
    form.updateValueAndValidity();

    if (matrunda) {
      this.router.navigate([`../${matrunda.id}`], {relativeTo: this.route});
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
        "Är du säker på att du vill ta bort mätrundan?<p>Operationen går inte att ångra.",
        "Avbryt",
        "Ta bort"),
      this.handleConfirmationDialogTaBort.bind(this));
  }

  handleConfirmationDialogTaBort(dialogResult: boolean) {
    if (dialogResult) {
      this.matrundaService.delete(this.matrundaId).subscribe(
        () => { this.formIsDirty = false; this.goBack(); },
        () => this.error = RestError.DELETE_MATRUNDA);
    }
  }

  getMatningstypMatobjektFilter(form: UntypedFormGroup) {
    const matningstyper: MatrundaMatningstyp[] = form.get("matningstyper").value;
    return matningstyper.length ? {includeIds: matningstyper.map(mt => mt.matningstypId)} : null;
  }
}

export enum RestError {
  GET_MATRUNDA = "Misslyckades hämta information om mätrundan. Ladda om sidan för att försöka på nytt.",
  POST_MATRUNDA = "Misslyckades lagra mätrundan. Försök igen om en liten stund",
  PUT_MATRUNDA = "Misslyckades uppdatera mätrundan. Försök igen om en liten stund",
  DELETE_MATRUNDA = "Misslyckades radera mätrundan. Försök igen om en liten stund"
}

