import {Component, OnInit, ViewChild} from "@angular/core";
import {Anvandare, AnvandareService} from "../../services/anvandare.service";
import { MatCheckboxChange } from "@angular/material/checkbox";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import {AbstractControl, UntypedFormControl, UntypedFormGroup, ValidationErrors, Validators} from "@angular/forms";
import {catchError, map, switchMap} from "rxjs/operators";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {Observable, of, timer} from "rxjs";
import {Location} from "@angular/common";
import {HttpErrorResponse} from "@angular/common/http";
import {Anvandargrupp, AnvandargruppService} from "../../services/anvandargrupp.service";
import {Kartlager, KartlagerService} from "../../services/kartlager.service";
import {ValidationResult} from "../../common/validation-result";
import {ConfirmationDialogModel} from "../../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import {DialogService} from "../../../../../lib/dialogs/dialog.service";
import {UNSAVED_CHANGES} from "../../services/can-deactivate-guard.service";
import {FormGroupUnsaved} from "../../common/form-group-unsaved";

@Component({
  selector: "mdb-edit-anvandare",
  template: `
    <h2>Användare</h2>

    <form *ngIf="anvandareForm | async as form" class="anvandare" [formGroup]="form">
      <h3>Grunduppgifter</h3>
      <div class="anvandarinfo">
        <mat-form-field>
          <input matInput placeholder="Namn" type="text" formControlName="namn" maxlength="130" required>
          <mat-error *ngIf="form.get('namn').hasError('required')">Fältet är obligatoriskt</mat-error>
        </mat-form-field>

        <mat-form-field>
          <input matInput placeholder="Företag" type="text" formControlName="foretag" maxlength="60" required>
          <mat-error *ngIf="form.get('foretag').hasError('required')">Fältet är obligatoriskt</mat-error>
        </mat-form-field>

        <mat-checkbox formControlName="aktiv">Aktiv</mat-checkbox>

        <mat-form-field class="text-field">
          <input matInput [readonly]="existingUser" placeholder="Användarnamn" type="text" formControlName="inloggningsnamn" maxlength="130"
                 required>
          <mat-error *ngIf="form.get('inloggningsnamn').hasError('required')">Fältet är obligatoriskt</mat-error>
          <mat-error *ngIf="form.get('inloggningsnamn').hasError('namnExists')">Användarnamnet är upptaget</mat-error>
          <mat-error *ngIf="form.get('inloggningsnamn').hasError('minlength')">Användarnamnet är för kort</mat-error>
          <mat-error *ngIf="form.get('inloggningsnamn').hasError('email')">Användarnamnet måste vara en giltig e-postadress</mat-error>
        </mat-form-field>

        <mat-form-field>
          <select matNativeControl placeholder="Behörighet" formControlName="behorighet">
            <option [value]="-1">Observatör</option>
            <option [value]="0">Mätrapportör</option>
            <option [value]="1">Tillståndshandläggare</option>
            <option [value]="2">Administratör</option>
          </select>
        </mat-form-field>
      </div>

      <h3>Karta</h3>
      <div class="karta" [class.invisible]="errorKartlager">
        <mat-form-field class="karta-select">
          <mat-label>Gå direkt till</mat-label>
          <select matNativeControl formControlName="defaultKartlagerId">
            <option></option>
            <option *ngFor="let kl of kartlager" [ngValue]="kl.id"
                    [selected]="kl.id === form.get('defaultKartlagerId').value">
              {{kl.namn}}: {{kl.beskrivning}}
            </option>
          </select>
        </mat-form-field>
      </div>
      <p *ngIf="errorKartlager" class="rest-error">{{errorKartlager}}</p>

      <h3>Grupper</h3>
      <table mat-table [dataSource]="agDataSource" matSort matSortActive="namn" matSortDisableClear
             matSortDirection="asc" [class.invisible]="errorAnvandargrupper">
        <ng-container matColumnDef="namn">
          <th mat-header-cell class="namn-column" *matHeaderCellDef mat-sort-header disableClear>Namn</th>
          <td mat-cell class="namn-column" *matCellDef="let ag"><a routerLink="../../anvandargrupp/{{ag.id}}">{{ag.namn}}</a>
          </td>
        </ng-container>
        <ng-container matColumnDef="beskrivning">
          <th mat-header-cell class="beskrivning-column" *matHeaderCellDef mat-sort-header disableClear>
            Beskrivning
          </th>
          <td mat-cell class="beskrivning-column" *matCellDef="let ag">{{ag.beskrivning}}</td>
        </ng-container>
        <ng-container matColumnDef="removeGroup">
          <th mat-header-cell class="remove-group-column" *matHeaderCellDef></th>
          <td mat-cell class="remove-group-column" *matCellDef="let ag">
            <button mat-icon-button (click)="onRemoveAnvandargrupp(ag.id)">
              <mat-icon id="close-button-icon">remove_circle</mat-icon>
            </button>
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="agColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: agColumns;"></tr>
      </table>
      <div class="anvandargrupper-actions">
        <mat-form-field *ngIf="allaAnvandargrupper.length">
          <mat-label>Användargrupp</mat-label>
          <select matNativeControl [(ngModel)]="selectedAnvandargrupp" [ngModelOptions]="{standalone: true}">
            <option></option>
            <option *ngFor="let ag of getMissingAnvandargrupper()" [ngValue]="ag.id">{{ag.namn}}</option>
          </select>
        </mat-form-field>
        <button *ngIf="allaAnvandargrupper.length" class="add-anvandargrupp-button"
                (click)="onAddAnvandargrupp()" [disabled]="!selectedAnvandargrupp" mat-raised-button
                color="primary">
          Lägg till
        </button>
      </div>
      <p *ngIf="errorAnvandargrupper" class="rest-error">{{errorAnvandargrupper}}</p>

      <h3>Påminnelser och larm</h3>
      <div class="paminnelser-och-larm">
        <mat-checkbox formControlName="skickaEpost" (change)="onSkickaEpostChecked($event, form)">
          Skicka e-post
        </mat-checkbox>
        <mat-form-field class="text-field">
          <input matInput placeholder="E-postadress" type="email" formControlName="epost" maxlength="130">
          <mat-error *ngIf="form.get('epost').hasError('required')">
            Fältet är obligatoriskt om "Skicka e-post" är ikryssat
          </mat-error>
          <mat-error *ngIf="form.get('epost').hasError('email')">Ogiltig e-postadress</mat-error>
        </mat-form-field>
      </div>

      <div class="actions">
        <button type="button" mat-stroked-button color="primary" (click)="goBack()">Tillbaka</button>
        <button type="button" mat-raised-button color="primary" (click)="onSave(form)" [disabled]="!allowedToSave(form)">Spara
        </button>
        <button mat-raised-button *ngIf="anvandarId && canDelete" type="button" (click)="onTaBort()"
                color="warn">Ta bort
        </button>
        <button mat-raised-button *ngIf="anvandarId && !canDelete" type="button" (click)="onAnonymisera()"
                color="warn">Anonymisera
        </button>
      </div>
    </form>
    <p *ngIf="error" class="rest-error">{{error}}</p>
  `,
  styles: [`
    .anvandare {
      display: grid;
      grid-gap: 0.5rem;
    }

    .anvandarinfo {
      display: grid;
      grid-template-columns: 1fr;
      grid-gap: 1rem;
      align-items: center;
    }

    .karta {
      display: grid;
      grid-template-columns: 1fr;
      grid-gap: 1rem;
    }

    .karta-select {
      grid-column-start: 1;
      grid-column-end: 3;
    }

    .anvandargrupper-actions mat-form-field {
      margin-right: 1rem;
    }

    .paminnelser-och-larm {
      display: grid;
      grid-template-columns: 1fr 5fr 1fr;
      grid-gap: 1rem;
      align-items: center;
    }

    .actions {
      display: grid;
      grid-gap: 0.5rem;
    }

    @media only screen and (min-width: 576px) {
      .anvandarinfo {
        grid-template-columns: 3fr 3fr 1fr;
      }

      .karta {
        grid-template-columns: 3fr 3fr 1fr;
      }

      .actions {
        grid-template-columns: auto auto auto;
        justify-content: left;
        grid-gap: 1rem;
      }
    }

    .beskrivning-column {
      width: 60%;
    }

    .remove-group-column {
      text-align: right;
      width: 4em;
    }
  `]
})
export class EditAnvandareComponent implements OnInit {

  anvandareForm: Observable<FormGroupUnsaved>;
  formIsDirty: boolean;
  anvandarId: number;
  anvandarnamn: string;
  canDelete: boolean;
  existingUser = false;

  agColumns: string[] = ["namn", "beskrivning", "removeGroup"];
  agDataSource: MatTableDataSource<Anvandargrupp>;

  initialAnvandargrupper: number[] = [];
  anvandargrupper: Anvandargrupp[] = [];
  allaAnvandargrupper: Anvandargrupp[] = [];
  selectedAnvandargrupp: number;

  kartlager: Kartlager[] = [];

  RestError = RestError;
  error: RestError = null;
  errorAnvandargrupper: RestError = null;
  errorKartlager: RestError = null;

  @ViewChild(MatSort) sort: MatSort;

  constructor(private route: ActivatedRoute, private router: Router, private location: Location,
              private anvandareService: AnvandareService, private anvandargruppService: AnvandargruppService,
              private kartlagerService: KartlagerService, private dialogService: DialogService) {
  }

  ngOnInit() {
    this.anvandareForm = this.route.paramMap.pipe(
      switchMap((params: ParamMap) => {
          this.anvandarId = +params.get("id");
          if (this.anvandarId) {
            this.existingUser = true;
            return this.anvandareService.get(this.anvandarId);
          } else {
            this.existingUser = false;

            return of({
              id: null,
              namn: "",
              foretag: "",
              aktiv: true,
              inloggningsnamn: "",
              behorighet: 0,
              defaultKartlagerId: null,
              skickaEpost: false,
              epost: "",
              inloggadSenast: null,
              anvandargrupper: []
            });
          }
        }
      ),
      map((anvandare: Anvandare) => {
        this.anvandarnamn = anvandare.inloggningsnamn;
        this.canDelete = anvandare.inloggadSenast === null;

        this.getAnvandargrupper(anvandare.anvandargrupper);
        this.getKartlager();

        const newForm = new FormGroupUnsaved({
          id: new UntypedFormControl(anvandare.id),
          namn: new UntypedFormControl(anvandare.namn),
          foretag: new UntypedFormControl(anvandare.foretag),
          aktiv: new UntypedFormControl(anvandare.aktiv),
          inloggningsnamn: new UntypedFormControl(anvandare.inloggningsnamn,
            this.existingUser ? [] : [Validators.required, Validators.minLength(1), Validators.email],
            [this.checkAnvandarnamn.bind(this)]),
          behorighet: new UntypedFormControl(anvandare.behorighet),
          defaultKartlagerId: new UntypedFormControl(anvandare.defaultKartlagerId),
          skickaEpost: new UntypedFormControl(anvandare.skickaEpost),
          epost: new UntypedFormControl(anvandare.epost, anvandare.skickaEpost ?
            [Validators.required, Validators.email] : [Validators.email]),
          anvandargrupper: new UntypedFormControl(anvandare.anvandargrupper)
        });

        newForm.valueChanges.subscribe(v => this.formIsDirty = newForm.isChanged());
        this.initialAnvandargrupper = newForm.get("anvandargrupper").value;

        return newForm;
      }),
      catchError((error: HttpErrorResponse) => {
        this.error = RestError.GET_ANVANDARE;
        return of(null);
      })
    );
  }

  canDeactivate(): Observable<boolean> | boolean {
    if (this.formIsDirty || this.hasAnvandargruppAdded(this.initialAnvandargrupper) || this.hasAnvandargruppRemoved(this.initialAnvandargrupper)) {
      return confirm(UNSAVED_CHANGES);
    } else {
      return true;
    }
  }

  checkAnvandarnamn({value}: AbstractControl): Observable<ValidationErrors | null> {
    return timer(1000).pipe(
      switchMap(() => {
          return (value === this.anvandarnamn) ? of(null) :
            this.anvandareService.namnExists(value).pipe(
              map((response: ValidationResult) => response.result ? {namnExists: true} : null)
            );
        }
      )
    );
  }

  getAnvandargrupper(anvandargrupper: number[]) {
    this.anvandargruppService.getAll().subscribe(
      allaAnvandargrupper => {
        this.allaAnvandargrupper = allaAnvandargrupper;
        this.anvandargrupper = allaAnvandargrupper.filter(ag => anvandargrupper.some(id => id === ag.id));
        this.updateAnvandargrupperDataSource();
      },
      error => this.errorAnvandargrupper = RestError.GET_ANVANDARGRUPPER);
  }

  getKartlager() {
    this.kartlagerService.getAll().subscribe(
      kartlager => this.kartlager = kartlager,
      error => this.errorKartlager = RestError.GET_KARTLAGER);
  }

  updateAnvandargrupperDataSource() {
    this.agDataSource = new MatTableDataSource(this.anvandargrupper);
    this.agDataSource.sort = this.sort;
  }

  goBack() {
    this.location.back();
  }

  onSave(form: UntypedFormGroup) {
    const value = form.value;
    value["anvandargrupper"] = this.anvandargrupper.map(ag => ag.id);

    this.error = null;

    if (this.anvandarId) {
      this.anvandareService.put(this.anvandarId, form.value).subscribe(
        () => this.onSaveSuccess(value["anvandargrupper"]),
        error => this.error = RestError.PUT_ANVANDARE);
    } else {
      this.anvandareService.post(form.value).subscribe(
        () => this.onSaveSuccess(value["anvandargrupper"]),
        error => this.error = RestError.POST_ANVANDARE);
    }
  }

  onSaveSuccess(anvandargrupper: number[]) {
    this.initialAnvandargrupper = anvandargrupper;
    this.formIsDirty = false;
    this.goBack();
  }

  onTaBort() {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Ta bort",
        "<p>Är du säker på att du vill ta bort användaren?<p>Operationen går inte att ångra.",
        "Avbryt",
        "Ta bort"),
      this.handleConfirmationDialogTaBort.bind(this));
  }

  onAnonymisera() {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Anonymisera",
        "<p>Är du säker på att du vill anonymisera användaren?<p>Operationen går inte att ångra.",
        "Avbryt",
        "Anonymisera"),
      this.handleConfirmationDialogAnonymisera.bind(this));
  }

  onAddAnvandargrupp() {
    this.anvandargrupper.push(this.allaAnvandargrupper.find(ag => ag.id === this.selectedAnvandargrupp));
    this.selectedAnvandargrupp = null;
    this.updateAnvandargrupperDataSource();
  }

  onRemoveAnvandargrupp(id: number) {
    this.anvandargrupper = this.anvandargrupper.filter(ag => ag.id !== id);
    this.selectedAnvandargrupp = null;
    this.updateAnvandargrupperDataSource();
  }

  allowedToSave(form: UntypedFormGroup): boolean {
    const initialAnvandargrupper = form.get("anvandargrupper").value;
    return !this.error && form.valid && (form.dirty || this.hasAnvandargruppAdded(initialAnvandargrupper) || this.hasAnvandargruppRemoved(initialAnvandargrupper));
  }

  hasAnvandargruppAdded(initialAnvandargrupper: number[]): boolean {
    return !!this.anvandargrupper.filter(ag => !initialAnvandargrupper.some(id => id === ag.id)).length;
  }

  hasAnvandargruppRemoved(initialAnvandargrupper: number[]): boolean {
    return !!initialAnvandargrupper.filter(id => !this.anvandargrupper.some(ag => ag.id === id)).length;
  }

  getMissingAnvandargrupper() {
    return this.allaAnvandargrupper.filter(ag => !this.anvandargrupper.some(ag2 => ag.id === ag2.id));
  }

  onSkickaEpostChecked(event: MatCheckboxChange, form: UntypedFormGroup) {
    const epostFormControl: AbstractControl = form.get("epost");
    epostFormControl.clearValidators();
    epostFormControl.setValidators(event.checked ? [Validators.required, Validators.email] : [Validators.email]);
    epostFormControl.updateValueAndValidity();
    epostFormControl.markAsTouched();
  }

  handleConfirmationDialogTaBort(dialogResult: boolean) {
    if (dialogResult) {
      this.anvandareService.delete(this.anvandarId).subscribe(
        () => { this.formIsDirty = false; this.goBack(); },
        error => this.error = RestError.DELETE_ANVANDARE);
    }
  }
  handleConfirmationDialogAnonymisera(dialogResult: boolean) {
    if (dialogResult) {
      this.anvandareService.anonymize(this.anvandarId).subscribe(
        () => { this.formIsDirty = false; this.goBack(); },
        error => this.error = RestError.ANONYMIZE_ANVANDARE);
    }
  }
}

export enum RestError {
  GET_ANVANDARE = "Misslyckades hämta användarinformation. Ladda om sidan för att försöka på nytt.",
  POST_ANVANDARE = "Misslyckades skapa användaren. Försök igen om en liten stund.",
  PUT_ANVANDARE = "Misslyckades uppdatera användaren. Försök igen om en liten stund.",
  DELETE_ANVANDARE= "Misslyckades radera användaren. Försök igen om en liten stund.",
  ANONYMIZE_ANVANDARE = "Misslyckades anonymisera användaren. Försök igen om en liten stund.",
  GET_ANVANDARGRUPPER = "Misslyckades hämta användargrupper. Ladda om sidan för att försöka på nytt.",
  GET_KARTLAGER = "Misslyckades hämta kartlager. Ladda om sidan för att försöka på nytt.",
}
