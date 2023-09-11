import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from "@angular/core";
import {EJ_GRANSKAD, FEL, GODKANT, Matning, MatobjektService, ReviewMatning} from "../../services/matobjekt.service";
import {UntypedFormControl, FormGroup} from "@angular/forms";
import { Location } from "@angular/common";
import {FormGroupUnsaved} from "../../common/form-group-unsaved";
import { UserService } from '../../services/user.service';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Component({
  selector: "mdb-granska-matning",
  template: `
    <li class="rest-error" *ngFor="let error of reviewErrors">
      {{formatError(error)}}
    </li>
    <form [formGroup]="form" >
      <div class="granska-actions">
        <button type="button" mat-raised-button color="primary"
                [disabled]="!canReject || updating"(click)="rejectMatningar()">Felmarkera</button>
        <button type="button" mat-raised-button color="primary"
                [disabled]="!canApprove || updating" (click)="approveMatningar()">Godkänn</button>
        <button *ngIf="taBortEnabled()" type="button" mat-raised-button color="primary" color="warn"
                [disabled]="!canDelete() || updating" (click)="deleteMatningar()">Ta bort</button>
      </div>
      <div class="granska-varde">
        <mat-form-field *ngIf="ifSelectionModeMulti()">
          <select matNativeControl placeholder="Operation" required formControlName="operation">
            <option *ngFor="let ms of operationTyp | keyvalue" [value]="ms.key">{{ms.value}}</option>
          </select>
        </mat-form-field>
        <mat-form-field>
          <input matInput autocomplete="off" type="decimal-number" placeholder="Nytt uppmätt värde"
                 formControlName="varde" required>
          <mat-error>Ange ett giltigt decimaltal.</mat-error>
        </mat-form-field>
      </div>
      <div class="korrigera-actions">
        <button type="button" mat-raised-button color="primary"
                [disabled]="form.invalid || !matningarSelected()"  (click)="updateVardeMatningar()">Korrigera</button>
      </div>
      <div class="kommentar">
        <mat-form-field>
          <textarea matInput mat-autosize="true" autocomplete="off" placeholder="Ny kommentar"
                    formControlName="kommentar" maxlength="250" matAutosizeMinRows=2>
          </textarea>
        </mat-form-field>
      </div>
      <div class="kommentar-action">
        <button type="button" mat-raised-button color="primary" [disabled]="!matningarSelected()"
                (click)="updateKommentarMatningar()">Ändra kommentar</button>
      </div>
    </form>
  `,
  styles: [`
    form {
      display: grid;
    }

    .granska-actions {
      display: grid;
      grid-column-gap: 2rem;
      grid-template-columns: 1fr 1fr 1fr 1fr;
      width: 80%;
      margin-top: 1rem;
    }

    .granska-varde {
      display: grid;
      grid-column-gap: 2rem;
      grid-template-columns: 1fr 1fr 1fr 1fr;
      width: 80%;
      margin-top: 2rem;
    }

    .korrigera-actions {
      display: grid;
      grid-column-gap: 2rem;
      grid-template-columns: 1fr 1fr 1fr 1fr;
      width: 80%;

    }

    .kommentar {
      display: grid;
      grid-column-gap: 2rem;
      grid-template-columns: 1fr 1fr;
      width: 80%;
      margin-top: 1rem;
    }

    .kommentar-action {
      display: grid;
      grid-column-gap: 2rem;
      grid-template-columns: 1fr 1fr 1fr 1fr;
      width: 80%;
    }

    button {
      min-width: 180px;
      height: 36px;
    }

    @media only screen and (min-width: 1120px) {
      .granska-actions {
        display: grid;
        grid-column-gap: 2rem;
        grid-template-columns: 1fr 1fr 1fr 1fr;
        width: 80%;
        margin-top: 1rem;
      }

      .granska-varde {
        display: grid;
        grid-column-gap: 2rem;
        grid-template-columns: 1fr 1fr 1fr 1fr;
        width: 80%;
        margin-top: 2rem;

      }

      .korrigera-actions {
        display: grid;
        grid-column-gap: 2rem;
        grid-template-columns: 1fr 1fr 1fr 1fr;
        width: 80%;

      }

      .kommentar {
        display: grid;
        grid-column-gap: 2rem;
        grid-template-columns: 1fr 1fr;
        width: 80%;
        margin-top: 1rem;
      }

      .kommentar-action {
        display: grid;
        grid-column-gap: 2rem;
        grid-template-columns: 1fr 1fr 1fr 1fr;
        width: 80%;
      }
    }

  `]
})
export class GranskaMatningComponent implements OnInit, OnChanges {

  @Input() matningar: number[];
  @Input() matningstypId: number;
  @Input() matobjektId: number;
  @Input() selectionMode: SelectionMode = SelectionMode.MULTI;
  @Output() updated = new EventEmitter<Matning>();
  @Output() isChanged = new EventEmitter<boolean>();

  form: FormGroupUnsaved;
  operationTyp: {[key: number]: string} = {0: "=", 1: "+", 2: "-"};
  canApprove: boolean;
  canReject: boolean;
  updating = false;
  reviewErrors: ReviewError[] = [];
  isAdmin: boolean;

  constructor(private matobjektService: MatobjektService,
              private location: Location,
              private userService: UserService) { }

  ngOnInit() {
    this.initForm();
    this.userService.loadUserDetails().subscribe(user => this.isAdmin = user.isAdmin());
  }

  ngOnChanges(changes: SimpleChanges): void {
    const selected = changes["matningar"].currentValue;
    if (selected) {
      if (selected.length === 0) {
        this.resetForm();
      } else {
        this.initForm();
        this.canApprove = true;
        this.canReject = true;
        this.updating = true;
        selected.map(matningId => {
          this.matobjektService.getMatning(this.matobjektId, this.matningstypId, matningId).subscribe(res => {
            this.canApprove = this.canApprove && (res.status !== GODKANT);
            this.canReject = this.canReject && (res.status !== FEL);
            this.updating = false;
          });
        });
      }
    }
  }

  initForm() {
    this.form = new FormGroupUnsaved({
      operation: new UntypedFormControl(0),
      varde: new UntypedFormControl(),
      kommentar: new UntypedFormControl("")
    });
    this.form.valueChanges.subscribe(_ => {
      this.isChanged.emit(this.form.isChanged());
    });
  }

  canDelete(): boolean {
    return this.matningar && this.matningar.length > 0;
  }

  resetForm() {
    this.initForm();
    this.reviewErrors = [];
    this.canReject = false;
    this.canApprove = false;
  }

  matningarSelected() {
    return this.matningar && this.matningar.length > 0;
  }

  updateVardeMatningar() {
    const avlastVarde = this.form.get("varde").value;
    const operation = this.form.get("operation").value;

    this.reviewMatningar({status: null, avlastVarde: avlastVarde, kommentar: null, operation: operation});
  }

  approveMatningar() {
    this.reviewMatningar({status: GODKANT, avlastVarde: null, kommentar: null, operation: null});
  }

  rejectMatningar() {
    this.reviewMatningar({status: FEL, avlastVarde: null, kommentar: null, operation: null});
  }

  deleteMatningar() {
    if (confirm("Är du verkligen säker på att du vill ta bort de valda mätningarna?")) {
      this.matobjektService.deleteMatningar(this.matningar).subscribe(
        () => this.location.back()
      );
    }
  }

  updateKommentarMatningar() {
    const kommentar = this.form.get("kommentar").value;
    this.reviewMatningar({status: null, avlastVarde: null, kommentar: kommentar, operation: null});
  }

  reviewMatningar(review: ReviewMatning) {
    this.matningar.map( matningId => {
      // TODO: Would be far more convenient and probably better overall to handle more than one review in each call.
      // You could potentially end up with A LOT of requests here.
      this.matobjektService.reviewMatning(this.matobjektId, this.matningstypId, matningId, review).subscribe(
        reviewed => this.handleReviewDone(reviewed),
        () => this.handleReviewError(matningId, review)
      );
    });
  }

  handleReviewError(matningId: number, reviewMatning: ReviewMatning) {
    this.matobjektService.getMatning(this.matobjektId, this.matningstypId, matningId).subscribe(matning => {
      if (this.reviewErrors.map(currentError => currentError.matning.id).indexOf(matningId) < 0) {
        if (reviewMatning.avlastVarde) {
          this.reviewErrors.push({matning: matning, restError: RestError.VARDE_MATNING});
        } else if (reviewMatning.status) {
          this.reviewErrors.push({matning: matning, restError: RestError.STATUS_MATNING});
        } else {
          this.reviewErrors.push({matning: matning, restError: RestError.KOMMENTAR_MATNING});
        }
      }
    });

  }

  handleReviewDone(matning: Matning) {
    this.clearError(matning.id);
    this.form.markAsPristine();
    this.isChanged.emit(false);
    this.updated.emit(matning);
  }

  formatDatum(datum: string): string {
    return datum.replace("T", " ").substring(0, datum.lastIndexOf(":"));
  }

  formatError(error: ReviewError): string {
    return this.formatDatum(error.matning.avlastDatum) + " " + error.restError;
  }

  clearError(matningId: number) {
    const index = this.reviewErrors.map(error => {
       return error.matning.id;
    }).indexOf(matningId);

    if (index > -1) {
      this.reviewErrors.splice(index, 1);
    }
  }

  ifSelectionModeMulti() {
    return this.selectionMode === SelectionMode.MULTI;
  }

  taBortEnabled(): boolean {
    return this.isAdmin && this.ifSelectionModeMulti();
  }
}

export interface ReviewError {
  matning: Matning;
  restError: RestError;

}
export enum RestError {
  VARDE_MATNING = "Felaktigt mätvärde efter korrigering.",
  STATUS_MATNING = "Mätvärde för mätning har en felkod kan därför inte godkännas.",
  KOMMENTAR_MATNING = ""
}

export enum SelectionMode {
  SINGLE,
  MULTI
}
