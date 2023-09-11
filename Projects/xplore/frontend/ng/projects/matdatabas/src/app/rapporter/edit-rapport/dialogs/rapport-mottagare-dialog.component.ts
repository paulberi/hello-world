import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Component, OnInit, Inject } from "@angular/core";
import { UntypedFormGroup, UntypedFormControl, ValidationErrors } from "@angular/forms";
import { RapportMottagare } from "../../../services/rapport.service";

@Component({
  selector: "mdb-rapport-mottagare-dialog",
  template: `
      <form [formGroup]="form" class="form-field">
        <h1>Lägg till mottagare</h1>
        <hr>
        <div>
          <mat-form-field>
            Namn:
            <input type="text" required matInput autocomplete="off" formControlName="namn" />
            <mat-error *ngIf="form.controls.namn.hasError('required')">Fältet är obligatoriskt</mat-error>
          </mat-form-field>
        </div>
        <div>
          <mat-form-field>
            E-post:
            <input type="email" matInput required email autocomplete="off" formControlName="epost" />
            <mat-error *ngIf="form.controls.epost.hasError('required')">Fältet är obligatoriskt</mat-error>
            <mat-error *ngIf="form.controls.epost.hasError('email')">Inte en giltig e-postadress</mat-error>
          </mat-form-field>
        </div>
        <div class="form-error">
          <mat-error *ngIf="form.hasError('unique')">Mottagare existerar redan</mat-error>
        </div>
      </form>
      <div class="actions">
        <button mat-raised-button color="primary"(click)="save()" [disabled]="!form.valid">Spara</button>
        <button mat-raised-button color="secondary" (click)="cancel()">Avbryt</button>
      </div>
    `,
  styles: [`
      form {
        text-align: center
      }

      .form-error {
        min-height: 1.5rem;
      }

      .actions {
        display: flex;
        justify-content: center;
      }
    `]
})

export class RapportMottagareDialogComponent implements OnInit {
  form: UntypedFormGroup;
  existingMottagare: RapportMottagare[];

  constructor(private dialogRef: MatDialogRef<RapportMottagareDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: any) {
      this.existingMottagare = data.existingMottagare;
  }

  ngOnInit(): void {
    /* Modals should be centered on screen by default, but do not appear to do so here. I have to
    fiddle with these settings*/
    this.form = new UntypedFormGroup({
      namn: new UntypedFormControl(),
      epost: new UntypedFormControl(),
    }, [this.uniqueMottagareValidator.bind(this)]);
  }

  save(): void {
    this.dialogRef.close(this.form.value as RapportMottagare);
  }

  cancel(): void {
    this.dialogRef.close(null);
  }

  private uniqueMottagareValidator(group: UntypedFormGroup): ValidationErrors | null {
    const namn = group.controls.namn.value;
    const epost = group.controls.epost.value;

    if (this.existingMottagare.some(m => this.mottagareEquals(m, namn, epost))) {
      return { "unique": true };
    } else {
      return null;
    }
  }

  private mottagareEquals(mottagare: RapportMottagare, namn: string, epost: string) {
    return mottagare.namn === namn && mottagare.epost === epost;
  }
}
