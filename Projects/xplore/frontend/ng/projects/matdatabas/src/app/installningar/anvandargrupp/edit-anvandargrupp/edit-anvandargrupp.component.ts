import {Component, OnInit, Optional} from "@angular/core";
import {delay, map, switchMap} from "rxjs/operators";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {AbstractControl, UntypedFormControl, UntypedFormGroup, Validators} from "@angular/forms";
import {Observable, of, timer} from "rxjs";
import {Anvandargrupp, AnvandargruppService} from "../../../services/anvandargrupp.service";
import {Location} from "@angular/common";
import {ConfirmationDialogModel} from "../../../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import {DialogService} from "../../../../../../lib/dialogs/dialog.service";
import {UNSAVED_CHANGES} from "../../../services/can-deactivate-guard.service";
import {FormGroupUnsaved} from "../../../common/form-group-unsaved";

@Component({
  selector: "mdb-edit-anvandargrupp",
  template: `
    <div class="main-content">
      <h3>Redigera grupp</h3>
      <form *ngIf="form" class="grupp" [formGroup]="form">
        <mat-form-field>
          <input matInput placeholder="Namn" formControlName="namn" required>
          <mat-error *ngIf="form.controls.namn.errors?.namnExists">Namnet är redan upptaget</mat-error>
        </mat-form-field>

        <mat-form-field>
                  <textarea matInput mat-autosize="true" placeholder="Beskrivning"
                            formControlName="beskrivning" maxlength="500"></textarea>
        </mat-form-field>

        <div class="actions">
          <button type="button" (click)="goBack()" mat-stroked-button color="primary">Tillbaka</button>
          <button mat-raised-button color="primary" (click)="onSave(form)" [disabled]="!form.dirty || !form.valid">
            Spara
          </button>
          <button mat-raised-button color="warn" (click)="onDelete()" [disabled]="!canDelete">Ta bort</button>
        </div>


        <ng-container *ngIf="currentId !== 'new'">
          <h3>Användare</h3>
          <mdb-anvandargrupp-anvandare [gruppId]="currentId"></mdb-anvandargrupp-anvandare>
          <h3>Mätansvar</h3>
          <mdb-anvandargrupp-matansvar [gruppId]="currentId"></mdb-anvandargrupp-matansvar>
          <h3>Larm</h3>
          <mdb-anvandargrupp-larm [gruppId]="currentId"></mdb-anvandargrupp-larm>
        </ng-container>

      </form>
    </div>
  `,
  styles: [`
      .main-content {
          display: grid;
          grid-gap: 0.5rem;
          padding: 0.5rem;
      }

      .grupp {
          display: grid;
          grid-gap: 0.5rem;
      }

      .actions {
          display: grid;
          grid-gap: 0.5rem;
          margin-bottom: 1rem;
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
export class EditAnvandargruppComponent implements OnInit {
  currentId: string;
  form: FormGroupUnsaved;
  canDelete = false;
  initialNamn: string;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private location: Location,
              private dialogService: DialogService,
              private service: AnvandargruppService) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.currentId = params.get("id");
      if (this.currentId === "new") {
        this.form = this.createForm(this.emptyAnvandargrupp());
      } else {
        this.service.get(+this.currentId).subscribe(grupp => {
          this.initialNamn = grupp.namn;
          this.form = this.createForm(grupp);
        });
        this.service.canDelete(+this.currentId).subscribe(canDelete => this.canDelete = canDelete);
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

  private emptyAnvandargrupp(): Partial<Anvandargrupp> {
    return {
      namn: "",
      beskrivning: ""
    };
  }

  private createForm(anvandargrupp: Partial<Anvandargrupp>) {
    return new FormGroupUnsaved({
      namn: new UntypedFormControl(anvandargrupp.namn, Validators.required, this.namnExists.bind(this)),
      beskrivning: new UntypedFormControl(anvandargrupp.beskrivning)
    });
  }

  goBack() {
    this.location.back();
  }

  onSave(form: UntypedFormGroup) {
    const value = form.value;
    if (this.currentId === "new") {
      this.service.post(value).subscribe(anvandargrupp => {
        this.form.markAsPristine();
        this.router.navigate(["../", anvandargrupp.id], {relativeTo: this.route, replaceUrl: true});
      });
    } else {
      this.service.put(+this.currentId, value).subscribe(() => {
        this.form.markAsPristine();
      });
    }
  }

  onDelete() {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Ta bort",
        "<p>Är du säker på att du vill ta bort användargruppen?<p>Operationen går inte att ångra.",
        "Avbryt",
        "Ta bort"),
      (confirmed) => {
        if (confirmed) {
          this.service.delete(+this.currentId).subscribe(() => {
            this.form.markAsPristine();
            this.goBack();
          });
        }
      });
  }

  namnExists(control: AbstractControl) {
    if (control.value === this.initialNamn) {
      return of(null);
    }
    return timer(1000).pipe(
      switchMap(() => this.service.namnExists(control.value)),
      map(res => {
        return res.result ? { namnExists: true } : null;
    }));
  }
}
