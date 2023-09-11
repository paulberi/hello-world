import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {formatNumber, Location} from "@angular/common";
import {forkJoin, Observable, of} from "rxjs";
import {Gransvarde, GransvardeService} from "../services/gransvarde.service";
import {UntypedFormArray, UntypedFormControl, UntypedFormGroup} from "@angular/forms";
import {Larmniva, LarmnivaService} from "../services/larmniva.service";
import {Anvandargrupp, AnvandargruppService} from "../services/anvandargrupp.service";
import {Matningstyp, MatobjektService} from "../services/matobjekt.service";
import {MatTableDataSource} from "@angular/material/table";
import {RestError} from "../matobjekt/handelser/edit-handelse.component";
import {UserService} from "../services/user.service";
import {UNSAVED_CHANGES} from "../services/can-deactivate-guard.service";
import {FormGroupUnsaved} from "../common/form-group-unsaved";
import {DefinitionmatningstypService} from "../services/definitionmatningstyp.service";


@Component({
  selector: "mdb-gransvarden",
  template: `
    <form *ngIf="gransvardenForm" [formGroup]="gransvardenForm">
      <h2>{{matningstypNamn}}</h2>
      <table mat-table [dataSource]="dataSource">
        <ng-container matColumnDef="typAvKontroll">
          <th mat-header-cell *matHeaderCellDef>Typ av kontroll</th>
          <td mat-cell *matCellDef="let row" [formGroup]="row">
            <mdb-edit [edit]="edit">
              <ng-template mdb-view-mode>
                {{typ[row.get('typAvKontroll').value]}}
              </ng-template>
              <ng-template mdb-edit-mode>
                <mat-form-field class="typavkontroll-field">
                  <select matNativeControl formControlName="typAvKontroll">
                    <option *ngFor="let item of typ | keyvalue" [value]="item.key">{{item.value}}</option>
                  </select>
                </mat-form-field>
              </ng-template>
            </mdb-edit>
          </td>
        </ng-container>
        <ng-container matColumnDef="gransvarde">
          <th mat-header-cell *matHeaderCellDef>Gränsvärde</th>
          <td mat-cell *matCellDef="let row" [formGroup]="row">
            <mdb-edit [edit]="edit">
              <ng-template mdb-view-mode>
                {{formatValue(row.get('gransvarde').value)}} {{enhet}}
              </ng-template>
              <ng-template mdb-edit-mode>
                <mat-form-field class="gransvarde-field">
                  <input matInput autocomplete="off" type="decimal-number" formControlName="gransvarde" required>
                  <span matSuffix>{{enhet}}</span>
                  <mat-error *ngIf="row.get('gransvarde').hasError('required')">Ange ett giltigt decimaltal.</mat-error>
                </mat-form-field>
              </ng-template>
            </mdb-edit>
          </td>
        </ng-container>
        <ng-container matColumnDef="larmniva">
          <th mat-header-cell *matHeaderCellDef>Larmnivå</th>
          <td mat-cell *matCellDef="let row" [formGroup]="row">
            <mdb-edit [edit]="edit">
              <ng-template mdb-view-mode>
                {{getLarmNivaNamn(row.get('larmnivaId').value)}}
              </ng-template>
              <ng-template mdb-edit-mode>
                <mat-form-field class="larmniva-field">
                  <select matNativeControl formControlName="larmnivaId">
                    <option *ngFor="let niva of larmnivaer" [ngValue]="niva.id">{{niva.namn}}</option>
                  </select>
                </mat-form-field>
              </ng-template>
            </mdb-edit>
          </td>
        </ng-container>
        <ng-container matColumnDef="skickaTill">
          <th mat-header-cell *matHeaderCellDef>Skicka larm till</th>
          <td mat-cell *matCellDef="let row" [formGroup]="row">
            <mdb-edit [edit]="edit">
              <ng-template mdb-view-mode>
                {{getSkickaTillNamn(row.get('larmTillAnvandargruppId').value)}}
              </ng-template>
              <ng-template mdb-edit-mode>
                <mat-form-field class="skickatill-field">
                  <select matNativeControl formControlName="larmTillAnvandargruppId">
                    <option *ngFor="let grupp of anvandargrupper" [ngValue]="grupp.id">{{grupp.namn}}</option>
                  </select>
                </mat-form-field>
              </ng-template>
            </mdb-edit>
          </td>
        </ng-container>

        <ng-container matColumnDef="aktiv">
          <th mat-header-cell *matHeaderCellDef>Aktiv</th>
          <td mat-cell *matCellDef="let row" [formGroup]="row">
            <mdb-edit [edit]="edit">
              <ng-template mdb-view-mode>
                <mat-checkbox [disabled]="true"
                              matTooltip="{{toggleActiveTooltip(true)}}"
                              formControlName="aktiv">
                </mat-checkbox>
              </ng-template>
              <ng-template mdb-edit-mode>
                <mat-checkbox [disabled]="!row.get('canDeactivate').value"
                              matTooltip="{{toggleActiveTooltip(row.get('canDeactivate').value)}}"
                              formControlName="aktiv">
                </mat-checkbox>
              </ng-template>
            </mdb-edit>
          </td>
        </ng-container>

        <ng-container matColumnDef="actions">
          <th mat-header-cell *matHeaderCellDef></th>
          <td mat-cell *matCellDef="let row">
            <div matTooltip="{{removeTooltip(row.get('delete').value)}}">
              <button [disabled]="!row.get('delete').value"
                      mat-raised-button color="warn"
                      (click)="deleteRow(row)">Ta bort
              </button>
            </div>
          </td>
        </ng-container>
        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
      </table>
      <div class="actions">
        <button *ngIf="edit" mat-raised-button color="primary" (click)="addRow()">Lägg till gränsvärde</button>
      </div>
      <div class="actions">
        <button type="button" mat-stroked-button color="primary" (click)="goBack()">Tillbaka</button>
        <button *ngIf="edit" type="button" (click)="toViewMode()" mat-raised-button color="primary">Avsluta</button>
        <button *ngIf="!edit && userService.userDetails.isTillstandshandlaggare()"
                type="button" (click)="toEditMode()" mat-raised-button color="primary">Ändra</button>
        <button *ngIf="userService.userDetails.isTillstandshandlaggare()" type="button"
                mat-raised-button color="primary" (click)="onSave(gransvardenForm)"
                [disabled]="!allowedToSave(gransvardenForm)">Spara
        </button>
      </div>
    </form>
  `,
  styles: [`
    table {
      margin-bottom: 1rem;
    }

    .mat-column-typAvKontroll {
      width: 15%;
    }

    .mat-column-gransvarde {
      width: 15%;
    }

    .mat-column-larmniva {
      width: 20%;
    }

    .mat-column-skickaTill {
      width: 20%;
    }

    .mat-column-aktiv {
      width: 20px;
    }

    .mat-column-actions {
      width: 10%;
    }

    .typavkontroll-field {
      width: 75px;
    }

    .gransvarde-field {
      width: 100px;
    }

    @media only screen and (max-width: 768px) {
      .larmniva-field, .skickatill-field {
        width: 150px;
      }
    }

  `]
})
export class GransvardenComponent implements OnInit {


  displayedColumns: string[] = ["typAvKontroll", "gransvarde", "larmniva", "skickaTill", "aktiv"];
  data: Gransvarde[];
  typ: {[key: number]: string} = {0: "Max", 1: "Min"};
  gransvardenForm: FormGroupUnsaved;
  matningstypId: number;
  matobjektId: number;
  matningstypNamn: string;
  enhet: string;
  anvandargrupper: Anvandargrupp[] = [];
  larmnivaer: Larmniva[] = [];
  dataSource: MatTableDataSource<any>;
  edit = false;
  error: RestError = null;
  deleted: Gransvarde[] = [];

  constructor(private route: ActivatedRoute, private location: Location, private gransvardeService: GransvardeService,
              private anvandargruppService: AnvandargruppService, private larmnivaService: LarmnivaService,
              public userService: UserService, private definitionmatningstypService: DefinitionmatningstypService,
              private matobjektService: MatobjektService) { }

  ngOnInit() {
    this.matningstypId = +this.route.snapshot.paramMap.get("id");
    this.matobjektId = +this.route.parent.snapshot.paramMap.get("id");

    forkJoin([
      this.gransvardeService.getGransvardenForMatningstyp(this.matningstypId),
      this.larmnivaService.getLarmnivaer(),
      this.userService.userDetails.isTillstandshandlaggare() ? this.anvandargruppService.getAll() : of<Anvandargrupp[]>([]),
      this.matobjektService.getMatningstyp(this.matobjektId, this.matningstypId)
    ]).subscribe(res => {
      this.data = res[0];
      this.larmnivaer = res[1];
      this.anvandargrupper = res[2];
      const matningstyp: Matningstyp = res[3];

      this.matningstypNamn = matningstyp.typ;

      this.definitionmatningstypService.get(matningstyp.definitionMatningstypId).subscribe(definitionMatninstyp => {
        this.enhet = definitionMatninstyp.beraknadEnhet;

        if (this.enhet == null || this.enhet === "") {
          this.enhet = matningstyp.enhet;
        }
      });

      const larmTill: Anvandargrupp = {id: null, antalAnvandare: 0, beskrivning: "Inget utskick", namn: "Inget utskick"};
      this.anvandargrupper.unshift(larmTill);

      this.gransvardenForm = new FormGroupUnsaved({
        gransvarden: this.createFormArray()
      });

      this.data.map(item => {
        this.gransvardeService.canDelete(item.id).subscribe(canDelete => {
          this.gransvarden.controls.find(formgroup =>
            formgroup.get("id").value === item.id).patchValue({delete: canDelete});
        });
      });

      this.data.map(item => {
        this.gransvardeService.canDeactivate(item.id).subscribe(canDeactivate => {
          this.gransvarden.controls.find(formgroup =>
            formgroup.get("id").value === item.id).patchValue({canDeactivate: canDeactivate});
        });
      });

      this.dataSource = new MatTableDataSource(this.gransvarden.controls);
    });
  }

  canDeactivate(): Observable<boolean> | boolean {
    if (this.edit && this.gransvardenForm.isChanged()) {
      return confirm(UNSAVED_CHANGES);
    } else {
      return true;
    }
  }

  createFormArray(): UntypedFormArray {
    return new UntypedFormArray(this.data.map(item => new UntypedFormGroup({
      id: new UntypedFormControl(item.id),
      larmTillAnvandargruppId: new UntypedFormControl(item.larmTillAnvandargruppId),
      gransvarde: new UntypedFormControl(item.gransvarde),
      typAvKontroll: new UntypedFormControl(item.typAvKontroll),
      larmnivaId: new UntypedFormControl(item.larmnivaId),
      matningstypId: new UntypedFormControl(item.matningstypId),
      aktiv: new UntypedFormControl(item.aktiv),
      delete: new UntypedFormControl(item.delete),
      canDeactivate: new UntypedFormControl(item.canDeactivate)
    })));
  }

  deleteRow(formGroup) {
    this.deleted.push(formGroup.value);
    const index: number = this.dataSource.data.findIndex(d => d === formGroup);
    this.dataSource.data.splice(index, 1);
    this.dataSource = new MatTableDataSource<Element>(this.dataSource.data);
    this.gransvardenForm.markAsDirty();
  }

  addRow() {
    const formGroup = new UntypedFormGroup({
      id: new UntypedFormControl(),
      larmTillAnvandargruppId: new UntypedFormControl(this.anvandargrupper[0].id),
      gransvarde: new UntypedFormControl(0),
      typAvKontroll: new UntypedFormControl(0),
      larmnivaId: new UntypedFormControl(this.larmnivaer[0].id),
      matningstypId: new UntypedFormControl(this.matningstypId),
      aktiv: new UntypedFormControl(true),
      delete: new UntypedFormControl(true),
      canDeactivate: new UntypedFormControl(true)
    });
    formGroup.markAsDirty();
    this.gransvarden.push(formGroup);
    this.dataSource = new MatTableDataSource(this.gransvarden.controls);
    this.gransvardenForm.markAsDirty();
  }

  get gransvarden(): UntypedFormArray {
    return <UntypedFormArray>this.gransvardenForm.get("gransvarden");
  }

  getLarmNivaNamn(id) {
    return this.larmnivaer.find(x => x.id === id).namn;
  }

  getSkickaTillNamn(id) {
    return this.anvandargrupper.find(x => x.id === id).namn;
  }

  toEditMode() {
    this.edit = true;
    this.displayedColumns.push("actions");

  }

  toViewMode() {
    this.edit = false;
    if (this.displayedColumns.includes("actions")) {
      this.displayedColumns.pop();
    }

    if (this.gransvardenForm.dirty) {
      this.resetForm();
    }
  }

  goBack() {
    this.location.back();
  }

  allowedToSave(form: UntypedFormGroup): boolean {
    return form.valid && form.dirty;
  }

  onSave(form: UntypedFormGroup) {
    this.error = null;
    const gransvarden: UntypedFormArray = form.get("gransvarden") as UntypedFormArray;
    this.data = [];
    gransvarden.controls.forEach(control => {
      const gransvarde: Gransvarde = control.value;
      if (control.dirty) {
          if (gransvarde.id) {
            this.gransvardeService.put(gransvarde.id, gransvarde).subscribe(
              () => this.data.push(gransvarde));
          } else {
            this.gransvardeService.post(gransvarde).subscribe(res => {
              gransvarde.id = res.id;
              control.get("id").setValue(gransvarde.id);
              this.data.push(gransvarde);
            });
          }
      } else {
        this.data.push(gransvarde);
      }
    });

    this.deleted.forEach(gransvarde => {
      if (gransvarde.id) {
        this.gransvardeService.delete(gransvarde.id).subscribe();
      }
    });

    this.deleted = [];
    form.markAsPristine();
    this.toViewMode();
  }

  formatValue(value): string {
    return formatNumber(value, "sv-SE");
  }

  resetForm() {
    this.gransvardenForm.setControl("gransvarden", this.createFormArray());
    this.dataSource = new MatTableDataSource(this.gransvarden.controls);
    this.gransvardenForm.markAsPristine();
  }

  toggleActiveTooltip(allowed: boolean) {
    if (!allowed) {
      return "För att avaktivera gränsvärde måste larm kvitteras";
    } else {
      return "Gränsvärdets status";
    }
  }

  removeTooltip(allowed: boolean) {
    if (!allowed) {
      return "Gränsvärden med aktiva larm kan ej tas bort.";
    } else {
      return "Ta bort gränsvärde";
    }
  }
}

