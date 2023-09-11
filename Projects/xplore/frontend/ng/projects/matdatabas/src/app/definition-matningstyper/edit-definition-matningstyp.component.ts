import {Component, OnInit} from "@angular/core";
import {AbstractControl, UntypedFormControl, UntypedFormGroup, Validators} from "@angular/forms";
import {
  TYP_GRUNDVATTENNIVA,
  TYP_INFILTRATION,
  TYP_RORELSE,
  TYP_TUNNELVATTEN,
  TYP_VADERSTATION,
  TYP_VATTENKEMI,
  TYP_YTVATTENMATNING
} from "../services/matobjekt.service";
import {DefinitionMatningstyp, DefinitionmatningstypService} from "../services/definitionmatningstyp.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";
import {ConfirmationDialogModel} from "../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import {DialogService} from "../../../../lib/dialogs/dialog.service";
import {integerValidator} from "../common/validators";
import {Observable, of, timer} from "rxjs";
import {map, switchMap} from "rxjs/operators";
import {UNSAVED_CHANGES} from "../services/can-deactivate-guard.service";
import {FormGroupUnsaved} from "../common/form-group-unsaved";

@Component({
  selector: "mdb-edit-definitionmatningstyp",
  template: `
    <h2>Mätningstyp</h2>
    <form *ngIf="form" [formGroup]="form">
      <mat-form-field>
        <select matNativeControl placeholder="Mätobjektstyp" formControlName="matobjektTyp" required (change)="clearError()">
          <option [value]="${TYP_GRUNDVATTENNIVA}">Grundvattennivå</option>
          <option [value]="${TYP_INFILTRATION}">Infiltration</option>
          <option [value]="${TYP_RORELSE}">Rörelse</option>
          <option [value]="${TYP_TUNNELVATTEN}">Tunnelvatten</option>
          <option [value]="${TYP_VATTENKEMI}">Vattenkemi</option>
          <option [value]="${TYP_VADERSTATION}">Väderstation</option>
          <option [value]="${TYP_YTVATTENMATNING}">Ytvattenmätning</option>
        </select>
      </mat-form-field>
      <mat-form-field>
        <input matInput autocomplete="off" maxlength="60" placeholder="Namn" formControlName="namn" required>
        <mat-error>Ange ett namn (unikt inom mätobjektstypen)</mat-error>
      </mat-form-field>
      <mat-form-field>
        <input matInput autocomplete="off" maxlength="12" placeholder="Enhet" formControlName="enhet" required>
        <mat-error>Ange en enhet (max 12 tecken)</mat-error>
      </mat-form-field>
      <mat-form-field>
        <input type="number" autocomplete="off" matInput placeholder="Decimaler" required
               formControlName="decimaler">
        <mat-error>Ange antal decimaler (max 1 siffra)</mat-error>
      </mat-form-field>
      <mat-checkbox formControlName="automatiskInrapportering">Automatiskt inrapportering</mat-checkbox>
      <mat-checkbox formControlName="automatiskGranskning">Automatiskt granskning</mat-checkbox>
      <mat-form-field>
        <textarea matInput mat-autosize="true" placeholder="Beskrivning" formControlName="beskrivning" maxlength="500">
        </textarea>
      </mat-form-field>

      <div class="actions">
        <button type="button" (click)="goBack()" mat-stroked-button color="primary">Tillbaka</button>
        <button type="button" mat-raised-button color="primary" (click)="onSave()" [disabled]="!form.dirty || !form.valid">
          Spara
        </button>
        <button type="button" (click)="onDelete()" mat-raised-button color="warn" [disabled]="!canDelete">Ta bort</button>
      </div>
    </form>
  `,
  styles: [`
    form {
      display: grid;
      grid-gap: 1rem;
    }
  `]
})
export class EditDefinitionMatningstypComponent implements OnInit {
  currentId: string;
  form: FormGroupUnsaved;
  canDelete = false;
  initialNamn: string;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private location: Location,
              private dialogService: DialogService,
              private definitionMatningstypService: DefinitionmatningstypService) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.currentId = params.get("id");
      if (this.currentId === "new") {
        const matobjektTyp = +this.route.snapshot.paramMap.get("matobjektTyp");
        this.form = this.createForm(this.getDefaultValues(matobjektTyp));
      } else {
        this.definitionMatningstypService.get(+this.currentId).subscribe(definition => {
          this.initialNamn = definition.namn;
          this.form = this.createForm(definition);
        });

        this.definitionMatningstypService.canDelete(+this.currentId).subscribe(canDelete => this.canDelete = canDelete);
      }
    });
  }

  canDeactivate(): Observable<boolean> | boolean {
    if (this.form.isChanged()) {
      return confirm(UNSAVED_CHANGES);
    } else {
      return true;
    }
  }

  private createForm(definition: Partial<DefinitionMatningstyp>) {
    return new FormGroupUnsaved({
      matobjektTyp: new UntypedFormControl(definition.matobjektTyp),
      namn: new UntypedFormControl(definition.namn),
      enhet: new UntypedFormControl(definition.enhet, Validators.required),
      decimaler: new UntypedFormControl(definition.decimaler, [integerValidator, Validators.max(9)]),
      automatiskInrapportering: new UntypedFormControl(definition.automatiskInrapportering),
      automatiskGranskning: new UntypedFormControl(definition.automatiskGranskning),
      beskrivning: new UntypedFormControl(definition.beskrivning)
    }, {asyncValidators: namnExistsValidator(definition.namn, definition.matobjektTyp, this.definitionMatningstypService)});
  }

  private getDefaultValues(matobjektTyp: number): Partial<DefinitionMatningstyp> {
    return {
      matobjektTyp: matobjektTyp,
      decimaler: 4,
      automatiskInrapportering: false,
      automatiskGranskning: false
    };
  }

  goBack() {
    this.location.back();
  }

  onSave() {
    const value: Partial<DefinitionMatningstyp> = this.form.value;
    if (this.currentId === "new") {
      this.definitionMatningstypService.post(value).subscribe(definition => {
        this.form.markAsPristine();
        this.router.navigate(["../", definition.id], {relativeTo: this.route, replaceUrl: true});
      });
    } else {
      this.definitionMatningstypService.put(+this.currentId, value).subscribe(() => {
        this.form.markAsPristine();
      });
    }
  }

  onDelete() {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Ta bort",
        "<p>Är du säker på att du vill ta bort mätningstypen?<p>Operationen går inte att ångra.",
        "Avbryt",
        "Ta bort"),
      (confirmed) => {
        if (confirmed) {
          this.definitionMatningstypService.delete(+this.currentId).subscribe(() => {
            this.form.markAsPristine();
            this.goBack();
          });
        }
      });
  }

  // This allows us to recompute the cross-field async validation
  // Angular doesn't run re-async validation across the whole form if there are specific fields
  // with errors.
  clearError() {
    this.form.controls.namn.setErrors(null);
    this.form.markAsPending();
    this.form.updateValueAndValidity();
  }
}

const namnExistsValidator = (initialNamn: string, initialMatobjektTyp: number, service: DefinitionmatningstypService) => {
  return (form: UntypedFormGroup) => {
    const namn = form.controls.namn.value;
    const matobjektTyp = form.controls.matobjektTyp.value;

    if (namn == null || matobjektTyp == null || namn === initialNamn && matobjektTyp === initialMatobjektTyp) {
      return of(null);
    }

    return timer(1000).pipe(
      switchMap(() => service.namnExists(namn, matobjektTyp)),
      map(res => {
        if (res.result) {
          form.controls.namn.setErrors({namnExists: true});
        } else {
          form.controls.namn.setErrors(null);
        }
        return null;
      }));
  };
};
