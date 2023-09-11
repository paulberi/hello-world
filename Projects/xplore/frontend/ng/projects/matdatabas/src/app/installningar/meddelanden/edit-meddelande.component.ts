import {Component, OnInit} from "@angular/core";
import {Location} from "@angular/common";
import {ActivatedRoute, Router} from "@angular/router";
import {Meddelande, MeddelandeService} from "../../services/meddelande.service";
import {UntypedFormControl, UntypedFormGroup} from "@angular/forms";
import {ConfirmationDialogModel} from "../../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import {DialogService} from "../../../../../lib/dialogs/dialog.service";
import {Observable} from "rxjs";
import {UNSAVED_CHANGES} from "../../services/can-deactivate-guard.service";
import {FormGroupUnsaved} from "../../common/form-group-unsaved";

@Component({
  selector: "mdb-edit-meddelande",
  template: `
    <div class="main-content">
      <h3>Redigera meddelande</h3>
      <form *ngIf="form" class="meddelande" [formGroup]="form">
        <mat-form-field>
          <input matInput placeholder="Datum" type="date" formControlName="datum" required>
        </mat-form-field>

        <mat-form-field>
          <input matInput placeholder="Rubrik" type="text" formControlName="rubrik" required maxlength="130">
        </mat-form-field>

        <mat-form-field>
          <input matInput placeholder="Länk" type="text" formControlName="url" maxlength="255">
        </mat-form-field>

        <mat-form-field>
                  <textarea matInput mat-autosize="true" placeholder="Meddelande"
                            formControlName="meddelande" required maxlength="500"></textarea>
        </mat-form-field>

        <div class="actions">
          <button mat-stroked-button type="button" color="primary" (click)="goBack()">Tillbaka</button>
          <button mat-raised-button type="button" color="primary" (click)="onSave(form)"
                  [disabled]="!form.dirty || !form.valid">Spara
          </button>
          <button mat-raised-button type="button" color="warn" (click)="onDelete()" [disabled]="!canDelete">Ta bort
          </button>
        </div>
      </form>
    </div>
  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 0.5rem;
      padding: 0.5rem;
    }

    .meddelande {
      display: grid;
    }

    .actions {
      display: grid;
      grid-gap: 0.5rem;
    }

    @media only screen and (min-width: 576px) {
      .actions {
        grid-template-columns: auto auto auto;
        justify-content: left;
        grid-gap: 1rem;
      }
    }
  `]
})
export class EditMeddelandeComponent implements OnInit {
  currentId: string;
  form: FormGroupUnsaved;
  canDelete = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private location: Location,
              private dialogService: DialogService,
              private meddelandeService: MeddelandeService) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.currentId = params.get("id");
      if (this.currentId === "new") {
        this.form = this.createForm(this.emptyMeddelande());
      } else {
        this.meddelandeService.getMeddelande(+this.currentId).subscribe(meddelande => {
          this.canDelete = true;
          this.form = this.createForm(meddelande);
        });
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

  private emptyMeddelande(): Partial<Meddelande> {
    return {
      datum: new Date().toISOString().substring(0, 10),
      rubrik: "",
      url: "",
      meddelande: ""
    };
  }

  private createForm(meddelande: Partial<Meddelande>) {
    return new FormGroupUnsaved({
      datum: new UntypedFormControl(meddelande.datum),
      rubrik: new UntypedFormControl(meddelande.rubrik),
      url: new UntypedFormControl(meddelande.url),
      meddelande: new UntypedFormControl(meddelande.meddelande),
    });
  }

  goBack() {
    this.location.back();
  }

  onSave(form: UntypedFormGroup) {
    const value = form.value;

    if (value.url) {
      if (!value.url.startsWith("http")) {
        value.url = "http://" + value.url;
      }
    }

    if (this.currentId === "new") {
      this.meddelandeService.postMeddelande(value).subscribe(meddelande => {
        this.form.markAsPristine();
        this.router.navigate(["../", meddelande.id], {relativeTo: this.route, replaceUrl: true});
      });
    } else {
      this.meddelandeService.putMeddelande(+this.currentId, value).subscribe(() => {
        this.form.markAsPristine();
      });
    }
  }

  onDelete() {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Ta bort",
        "<p>Är du säker på att du vill ta bort meddelandet?<p>Operationen går inte att ångra.",
        "Avbryt",
        "Ta bort"),
      (confirmed) => {
        if (confirmed) {
          this.meddelandeService.deleteMeddelande(+this.currentId).subscribe(() => {
            this.form.markAsPristine();
            this.goBack();
          });
        }
      });
  }
}
