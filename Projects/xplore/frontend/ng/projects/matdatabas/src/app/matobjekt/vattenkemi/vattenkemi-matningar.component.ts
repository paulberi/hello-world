import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {UntypedFormArray, UntypedFormControl, UntypedFormGroup, Validators} from "@angular/forms";
import {Matning} from "../../services/matobjekt.service";
import {BehaviorSubject} from "rxjs";
import {Varde} from "./edit-vattenkemi.component";
import {formatNumber} from "@angular/common";

@Component({
  selector: "mdb-vattenkemi-matningar",
  template: `
    <div class="main-content" [formGroup]="formGroup">
      <table mat-table [dataSource]="dataSource" formArrayName="matningar">
        <ng-container matColumnDef="namn">
          <th mat-header-cell *matHeaderCellDef>Mätningstyp</th>
          <td mat-cell *matCellDef="let index = index">
            {{getControl(index, 'namn').value}}
          </td>
        </ng-container>
        <ng-container matColumnDef="inomDetektionsomrade">
          <th mat-header-cell *matHeaderCellDef></th>
          <td mat-cell *matCellDef="let element; let i = index" [formGroupName]="i">
            <mat-form-field>
              <select matNativeControl formControlName="inomDetektionsomrade" (change)="updateRow(i)">
                <option *ngFor="let item of detektion | keyvalue" [value]="item.key">{{item.value}}</option>
              </select>
            </mat-form-field>
          </td>
        </ng-container>
        <ng-container matColumnDef="avlastVarde">
          <th mat-header-cell *matHeaderCellDef>Mätning</th>
          <td mat-cell *matCellDef="let element; let i = index" [formGroupName]="i">
            <mat-form-field>
              <input matInput autocomplete="off" (input)="updateRow(i)" placeholder="Mätvärde"
                     formControlName="avlastVarde">
              <span matSuffix>{{getControl(i, 'enhet').value}}</span>
              <mat-error>Ange ett giltigt decimaltal.</mat-error>
            </mat-form-field>
          </td>
        </ng-container>
        <tr mat-header-row *matHeaderRowDef="columns"></tr>
        <tr mat-row *matRowDef="let row; columns: columns;"></tr>
      </table>
    </div>
  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
    }

    .mat-column-namn {
      width: 20%
    }

    .mat-column-inomDetektionsomrade {
      width: 20%;
      margin-right: 0.5rem;
    }

    .mat-column-avlastVarde {
      width: 30%;
    }
  `]
})
export class VattenkemiMatningarComponent implements OnInit {

  @Input() matningar: Matning[];
  @Output() updated = new EventEmitter<Varde>();

  columns: string[] = ["namn", "inomDetektionsomrade", "avlastVarde"];

  formGroup: UntypedFormGroup;
  controls: UntypedFormArray;
  detektion: {[key: number]: string} = {0: "> (över)", 1: "inom", 2: "< (under)"};
  matningar$: BehaviorSubject<Matning[]>;
  dataSource;
  vardeRegex = /^-?[0-9]{1,7}([\\.|\\,][0-9]{1,3})?$/;
  constructor() {
  }


  ngOnInit() {

    const toGroups = this.matningar.map(item => new UntypedFormGroup({
      id: new UntypedFormControl(item.id),
      namn: new UntypedFormControl(item.namn),
      inomDetektionsomrade: new UntypedFormControl(item.inomDetektionsomrade),
      avlastVarde: new UntypedFormControl(this.formatValue(item.avlastVarde), Validators.pattern(this.vardeRegex)),
      enhet: new UntypedFormControl(item.enhet),
      matningstypId: new UntypedFormControl(item.matningstypId)}));
    this.controls = new UntypedFormArray(toGroups);
    this.formGroup = new UntypedFormGroup( {
      matningar: this.controls
    });

    this.matningar$ = new BehaviorSubject(this.matningar);
    this.dataSource = this.matningar$;
  }

  getControl(index, fieldName) {
    const a  = this.controls.at(index).get(fieldName) as UntypedFormControl;
    return this.controls.at(index).get(fieldName) as UntypedFormControl;
  }

  updateRow(index) {
    const controlDetektion = this.getControl(index, "inomDetektionsomrade");
    const controlAvlastVarde = this.getControl(index, "avlastVarde");
    if (controlAvlastVarde.valid) {
      if (controlAvlastVarde.value) {
        const value = controlAvlastVarde.value.toString();
        const n = Number(value.replace(",", "."));
        this.updated.emit({
          id: index, value: n,
          inomDetektion: controlDetektion.value
        });
      } else {
        this.updated.emit({
          id: index, value: null,
          inomDetektion: controlDetektion.value
        });
      }
    }

  }

  formatValue(value): string {
    if (value) {
      return formatNumber(value, "sv-SE");
    }
    return value;
  }

}
