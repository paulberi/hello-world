import {Component, OnInit} from "@angular/core";
import {Observable, of, timer} from "rxjs";
import {catchError, map, switchMap} from "rxjs/operators";
import {AbstractControl, UntypedFormControl, UntypedFormGroup, ValidationErrors, Validators} from "@angular/forms";
import {Matobjektgrupp, MatobjektgruppService} from "../services/matobjektgrupp.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import {Location} from "@angular/common";
import {ValidationResult} from "../common/validation-result";
import {ConfirmationDialogModel} from "../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import {DialogService} from "../../../../lib/dialogs/dialog.service";
import {Kategori} from "./kategori";
import {Kartsymbol} from "./kartsymbol";
import {ViewMode} from "../common/view-mode";
import {SelectionMode} from "../matobjekt/search-matobjekt/search-matobjekt.component";
import {UserService} from "../services/user.service";
import {MatningstypMatobjektFilter} from "../services/matobjekt.service";
import {UNSAVED_CHANGES} from "../services/can-deactivate-guard.service";
import {FormGroupUnsaved} from "../common/form-group-unsaved";

@Component({
  selector: "mdb-edit-matobjektgrupp",
  template: `
    <form *ngIf="form | async as form" [formGroup]="form">
        <h3 *ngIf="matobjektgruppId">Grupp: {{matobjektgruppnamn}}</h3>
      <h3 *ngIf="!matobjektgruppId">Ny grupp</h3>
      <div class="form-fields">
        <mat-form-field>
          <input matInput placeholder="Namn" type="text" formControlName="namn" maxlength="130" required>
          <mat-error *ngIf="form.get('namn').hasError('required')">Fältet är obligatoriskt</mat-error>
          <mat-error *ngIf="form.get('namn').hasError('namnExists')">Namnet är upptaget</mat-error>
          <mat-error *ngIf="form.get('namn').hasError('minlength')">Namnet är för kort</mat-error>
        </mat-form-field>
        <mat-form-field>
          <select matNativeControl placeholder="Kategori" formControlName="kategori">
            <option *ngFor="let k of Kategori | keyvalue" [value]="k.key">{{k.value}}</option>
          </select>
        </mat-form-field>
        <mat-form-field>
          <select matNativeControl placeholder="Särskild kartsymbol" formControlName="kartsymbol">
            <option *ngFor="let k of Kartsymbol | keyvalue" [value]="k.key">{{k.value}}</option>
          </select>
        </mat-form-field>
        <mat-form-field class="beskrivning">
            <textarea matInput mat-autosize="true" placeholder="Beskrivning" formControlName="beskrivning"
                      maxlength="500" matAutosizeMinRows=2></textarea>
        </mat-form-field>
      </div>

      <div class="matobjekt">
        <h3>Mätobjekt</h3>
        <div class="matobjekt-content" [ngClass]="{'modifying': viewMode !== ViewMode.VIEW}">
          <ng-container [ngSwitch]="viewMode">
            <mdb-edit-matobjektgrupp-matobjekt *ngSwitchCase="ViewMode.VIEW"
                                               [ids]="form.get('matobjekt').value">
            </mdb-edit-matobjektgrupp-matobjekt>
            <mdb-search-matobjekt *ngSwitchCase="ViewMode.EDIT"
                                  [selectionMode]="SelectionMode.MULTI"
                                  [initialFilter]="getMatobjektFilter(form)"
                                  (selected)="onMatobjektUpdated($event, form)">
            </mdb-search-matobjekt>
          </ng-container>
          <div class="matobjekt-actions">
            <button *ngIf="!readonly && viewMode === ViewMode.VIEW" type="button" (click)="viewMode = ViewMode.EDIT"
                    mat-raised-button color="primary">Välj mätobjekt
            </button>
            <button type="button" *ngIf="viewMode === ViewMode.EDIT" (click)="viewMode = ViewMode.VIEW"
                    mat-raised-button color="primary">Klar
            </button>
          </div>
        </div>
      </div>

      <div class="actions">
        <button type="button" mat-stroked-button color="primary" (click)="goBack()">Tillbaka</button>
        <mdb-save-button [label]="'Spara'" (clicked)="onSave(form)" [saving]="saving" [disabled]="!allowedToSave(form)"></mdb-save-button>
        <button mat-raised-button *ngIf="!readonly && matobjektgruppId" type="button" (click)="onTaBort()"
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
      grid-row-gap: 0.5rem;
    }

    .matobjekt-content {
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

    .matobjekt-actions {
      display: grid;
      grid-gap: 1rem;
      grid-template-columns: auto 1fr;
      padding-bottom: 1rem;
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

      .form-fields {
        grid-column-gap: 1rem;
        grid-template-columns: 2fr 1fr 1fr;
      }

      .beskrivning {
        grid-column-start: 1;
        grid-column-end: 4;
      }
    }
  `]
})
export class EditMatobjektgruppComponent implements OnInit {

  Kategori = Kategori;
  Kartsymbol = Kartsymbol;
  Object = Object;
  ViewMode = ViewMode;
  SelectionMode = SelectionMode;

  form: Observable<FormGroupUnsaved>;
  matobjektgruppId: number;
  matobjektgruppnamn: string;
  saving: boolean;
  formIsDirty: boolean;
  readonly = true;

  viewMode: ViewMode = ViewMode.VIEW;

  RestError = RestError;
  error: RestError = null;

  constructor(private route: ActivatedRoute, private router: Router, private location: Location,
              private userService: UserService,
              private matobjektgruppService: MatobjektgruppService, private dialogService: DialogService) {
  }

  ngOnInit() {
    if (this.userService.userDetails.isTillstandshandlaggare()) {
      this.readonly = false;
    }

    this.form = this.route.paramMap.pipe(
      switchMap((params: ParamMap) => {
          this.matobjektgruppId = +params.get("id");
          if (this.matobjektgruppId) {
            return this.matobjektgruppService.get(this.matobjektgruppId);
          } else {
            return of({
              id: null,
              namn: null,
              kategori: 0,
              kartsymbol: 0,
              beskrivning: "",
              matobjekt: []
            });
          }
        }
      ),
      map((matobjektgrupp: Matobjektgrupp) => {
        this.matobjektgruppnamn = matobjektgrupp.namn;

        const newForm = new FormGroupUnsaved({
          namn: new UntypedFormControl(matobjektgrupp.namn,
            [Validators.required, Validators.minLength(1)],
            [this.checkMatobjektgruppnamn.bind(this)]),
          kategori: new UntypedFormControl(Object.keys(Kategori)[matobjektgrupp.kategori]),
          kartsymbol: new UntypedFormControl(Object.keys(Kartsymbol)[matobjektgrupp.kartsymbol]),
          beskrivning: new UntypedFormControl(matobjektgrupp.beskrivning),
          matobjekt: new UntypedFormControl(matobjektgrupp.matobjekt)
        });

        if (this.readonly) {
          newForm.disable();
        }

        newForm.valueChanges.subscribe(v => this.formIsDirty = newForm.isChanged());

        return newForm;
      }),
      catchError((error: HttpErrorResponse) => {
        this.error = RestError.GET_MATOBJEKTGRUPP;
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

  checkMatobjektgruppnamn({value}: AbstractControl): Observable<ValidationErrors | null> {
    return timer(1000).pipe(
      switchMap(() => {
          return (value === this.matobjektgruppnamn) ? of(null) :
            this.matobjektgruppService.namnExists(value).pipe(
              map((response: ValidationResult) => response.result ? {namnExists: true} : null)
            );
        }
      )
    );
  }

  allowedToSave(form: UntypedFormGroup): boolean {
    return form.valid && form.dirty && this.viewMode === ViewMode.VIEW;
  }

  onMatobjektUpdated(matobjekt: number[], form: UntypedFormGroup) {
    const fc = form.get("matobjekt");
    if (fc.value !== matobjekt) {
      fc.setValue(matobjekt);
      fc.markAsDirty();
    }
  }

  onSave(form: UntypedFormGroup) {
    const value = form.value;
    value["kategori"] = Object.keys(Kategori).indexOf(form.get("kategori").value);
    value["kartsymbol"] = Object.keys(Kartsymbol).indexOf(form.get("kartsymbol").value);

    this.error = null;
    this.saving = true;

    if (this.matobjektgruppId) {
      this.matobjektgruppService.put(this.matobjektgruppId, form.value).subscribe(
        () => this.handleSaveSuccess(form),
        () => this.handleSaveFailure(RestError.PUT_MATOBJEKTGRUPP));
    } else {
      this.matobjektgruppService.post(form.value).subscribe(
        matobjektgrupp => this.handleSaveSuccess(form, matobjektgrupp),
        () => this.handleSaveFailure(RestError.POST_MATOBJEKTGRUPP));
    }
  }

  handleSaveSuccess(form: UntypedFormGroup, matobjektgrupp?: Matobjektgrupp) {
    this.saving = false;
    form.markAsUntouched();
    form.markAsPristine();
    form.updateValueAndValidity();
    this.formIsDirty = false;

    if (matobjektgrupp) {
      this.router.navigate([`../${matobjektgrupp.id}`], {relativeTo: this.route, replaceUrl: true});
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
        "<p>Är du säker på att du vill ta bort mätobjektgruppen?<p>Operationen går inte att ångra.",
        "Avbryt",
        "Ta bort"),
      this.handleConfirmationDialogTaBort.bind(this));
  }

  handleConfirmationDialogTaBort(dialogResult: boolean) {
    if (dialogResult) {
      this.matobjektgruppService.delete(this.matobjektgruppId).subscribe(
        () => { this.formIsDirty = false; this.goBack(); },
        () => this.error = RestError.DELETE_MATOBJEKTGRUPP);
    }
  }

  getMatobjektFilter(form: UntypedFormGroup): MatningstypMatobjektFilter {
    return form.get("matobjekt").value.length ? {matobjektIds: form.get("matobjekt").value} : null;
  }
}

export enum RestError {
  GET_MATOBJEKTGRUPP = "Misslyckades hämta information om mätobjektgruppen. Ladda om sidan för att försöka på nytt.",
  POST_MATOBJEKTGRUPP = "Misslyckades lagra mätobjektgruppen. Försök igen om en liten stund",
  PUT_MATOBJEKTGRUPP = "Misslyckades uppdatera mätobjektgruppen. Försök igen om en liten stund",
  DELETE_MATOBJEKTGRUPP = "Misslyckades radera mätobjektgruppen. Försök igen om en liten stund"
}

