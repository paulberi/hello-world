import { Component, OnInit } from "@angular/core";
import {AbstractControl, UntypedFormArray, UntypedFormControl, UntypedFormGroup, ValidatorFn, Validators} from "@angular/forms";
import {Larmniva, LarmnivaService} from "../../services/larmniva.service";
import {MatTableDataSource} from "@angular/material/table";
import {RestError} from "../../matobjekt/handelser/edit-handelse.component";
import {Location} from "@angular/common";
import {Observable} from "rxjs";
import {UNSAVED_CHANGES} from "../../services/can-deactivate-guard.service";
import {FormGroupUnsaved} from "../../common/form-group-unsaved";

@Component({
  selector: "mdb-larmnivaer",
  template: `
    <form *ngIf="larmnivaerForm" [formGroup]="larmnivaerForm">
      <h2>Definiera larmnivåer</h2>
      <table mat-table [dataSource]="dataSource">
        <ng-container matColumnDef="namn">
          <th mat-header-cell *matHeaderCellDef>Namn</th>
          <td mat-cell *matCellDef="let row" [formGroup]="row">
            <mdb-edit [edit]="edit">
              <ng-template mdb-view-mode>
                {{row.get('namn').value}}
              </ng-template>
              <ng-template mdb-edit-mode>
                <mat-form-field>
                  <input matInput type="text" placeholder="Namn" formControlName="namn" required>
                  <mat-error *ngIf="row.get('namn').hasError('unique')">Namnet används redan</mat-error>
                  <mat-error *ngIf="row.get('namn').hasError('required')">Ange ett namn</mat-error>
                </mat-form-field>
              </ng-template>
            </mdb-edit>
          </td>
        </ng-container>
        <ng-container matColumnDef="beskrivning">
          <th mat-header-cell *matHeaderCellDef>Beskrivning</th>
          <td mat-cell *matCellDef="let row" [formGroup]="row">
            <mdb-edit [edit]="edit">
              <ng-template mdb-view-mode>
                {{row.get('beskrivning').value}}
              </ng-template>
              <ng-template mdb-edit-mode>
                <mat-form-field>
                  <input matInput type="text" placeholder="Beskrivning" formControlName="beskrivning" required>
                  <mat-error *ngIf="row.get('beskrivning').hasError('required')">Ange en beskrivning</mat-error>
                </mat-form-field>
              </ng-template>
            </mdb-edit>
          </td>
        </ng-container>
        <ng-container matColumnDef="actions">
          <th mat-header-cell *matHeaderCellDef></th>
          <td mat-cell *matCellDef="let row">
            <button [disabled]="!row.get('delete').value" mat-raised-button color="warn" (click)="deleteRow(row)">Ta bort</button>
          </td>
        </ng-container>
        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
      </table>
      <div class="actions">
        <button *ngIf="edit" mat-raised-button color="primary" (click)="addRow()">Lägg till larmnivå</button>
      </div>
      <div class="actions">
        <button *ngIf="edit" type="button" (click)="toViewMode()" mat-raised-button color="primary">Avsluta</button>
        <button *ngIf="!edit" type="button" (click)="toEditMode()" mat-raised-button color="primary">Ändra larmnivåer</button>
        <button *ngIf="edit" type="button" mat-raised-button color="primary" (click)="onSave(larmnivaerForm)"
                [disabled]="!allowedToSave(larmnivaerForm)">Spara
        </button>
      </div>
    </form>
  `,
  styles: [`
    table {
      margin-bottom: 1rem;
    }
  `]
})
export class LarmnivaerComponent implements OnInit {

  displayedColumns: string[] = ["namn", "beskrivning"];
  data: Larmniva[];
  larmnivaerForm: FormGroupUnsaved;
  dataSource: MatTableDataSource<any>;
  edit = false;
  error: RestError = null;
  deleted: Larmniva[] = [];

  constructor(private larmnivaService: LarmnivaService, private location: Location) {
  }

  ngOnInit() {
    this.larmnivaService.getLarmnivaer().subscribe(res => {
      this.data = res;
      this.larmnivaerForm = new FormGroupUnsaved({
        larmnivaer: this.createFormArray()
      });
      this.data.map(item => {
        this.larmnivaService.canDelete(item.id).subscribe( larmniva => {
          this.larmnivaer.controls.map(formGroup => {
            if (formGroup.get("id").value === item.id) {
              item.delete = larmniva;
              formGroup.get("delete").setValue(larmniva);
            }
          });
          this.larmnivaerForm.markAsPristine();
        });
      });
      this.dataSource = new MatTableDataSource(this.larmnivaer.controls);
    });
  }

  canDeactivate(): Observable<boolean> | boolean {
    if (this.larmnivaerForm.isChanged() || this.larmnivaerForm.dirty) {
      return confirm(UNSAVED_CHANGES);
    } else {
      return true;
    }
  }

  createFormArray(): UntypedFormArray {
    return new UntypedFormArray(this.data.map(item =>
      new UntypedFormGroup({
      id: new UntypedFormControl(item.id),
      namn: new UntypedFormControl(item.namn, Validators.required),
      beskrivning: new UntypedFormControl(item.beskrivning, Validators.required),
      delete: new UntypedFormControl(item.delete)
    })), uniqueValidator("namn"));
  }

  deleteRow(formGroup) {
    this.deleted.push(formGroup.value);
    const index: number = this.dataSource.data.findIndex(d => d === formGroup);
    this.dataSource.data.splice(index, 1);
    this.dataSource = new MatTableDataSource<Element>(this.dataSource.data);
    this.larmnivaerForm.markAsDirty();
  }

  addRow() {
    const formGroup = new UntypedFormGroup({
      namn: new UntypedFormControl("", Validators.required),
      beskrivning: new UntypedFormControl("", Validators.required),
      delete: new UntypedFormControl(true)
    });

    this.larmnivaer.push(formGroup);
    this.dataSource = new MatTableDataSource(this.larmnivaer.controls);
    this.larmnivaerForm.markAsDirty();
  }

  get larmnivaer(): UntypedFormArray {
    return <UntypedFormArray>this.larmnivaerForm.get("larmnivaer");
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

    if (this.larmnivaerForm.dirty) {
      this.larmnivaerForm.setControl("larmnivaer", this.createFormArray());
      this.dataSource = new MatTableDataSource(this.larmnivaer.controls);
      this.larmnivaerForm.markAsPristine();
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
    const larmnivaer: UntypedFormArray = form.get("larmnivaer") as UntypedFormArray;
    this.data = [];
    larmnivaer.controls.forEach(control => {
      const larmniva: Larmniva = control.value;
      if (control.dirty) {
        if (larmniva.id) {
          this.larmnivaService.put(larmniva.id, larmniva).subscribe(
            () => {
              this.larmnivaerForm.markAsPristine();
              this.data.push(larmniva);
            });
        } else {
          this.larmnivaService.post(larmniva).subscribe(res => {
            larmniva.id = res.id;
            control.get("id").setValue(larmniva.id);
            this.data.push(larmniva);
            this.larmnivaerForm.markAsPristine();
          });
        }
      } else {
        this.data.push(larmniva);
      }
    });

    this.deleted.forEach(larmniva => {
      if (larmniva.id) {
        this.larmnivaService.delete(larmniva.id).subscribe();
      }
    });

    this.deleted = [];
    form.markAsPristine();
    this.toViewMode();
  }
}

const uniqueValidator = (field: string): ValidatorFn => {
  return (formArray: UntypedFormArray): { [key: string]: boolean } => {
    const controls: AbstractControl[] = formArray.controls.filter((formGroup: UntypedFormGroup) => {
      return !(formGroup.get(field).value === null || formGroup.get(field).value === "");
    });
    const uniqueObj: any = { unique: true };
    let find = false;
    if (controls.length > 1) {
      for (let i = 0; i < controls.length; i++) {
        const formGroup: UntypedFormGroup = controls[i] as UntypedFormGroup;
        const mainControl: AbstractControl = formGroup.get(field);
        controls.forEach((group: UntypedFormGroup, index: number) => {
          if (i === index) {
            return;
          }

          const currControl: UntypedFormControl = group.get(field) as UntypedFormControl;
          const currValue: string = currControl.value;
          let newErrors: any;
          if (mainControl.value === currValue) {
            if (currControl.valid) {
              newErrors = uniqueObj;
            } else {
              newErrors = Object.assign(currControl.errors, uniqueObj);
            }
            find = true;
          } else {
            newErrors = currControl.errors;
            if (newErrors != null) {
              delete newErrors["unique"];
              if (Object.keys(newErrors).length === 0) {
                newErrors = null;
              }
            }
          }
          currControl.setErrors(newErrors);
        });
      }
      if (find) {
        return uniqueObj;
      }
    }

    return null;
  };
};
