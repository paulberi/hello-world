import {
  AfterViewInit,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild
} from "@angular/core";
import {FELKOD_OK, Matning, MatningFilter, MatobjektService} from "../services/matobjekt.service";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {UntypedFormControl, UntypedFormGroup, ValidationErrors} from "@angular/forms";
import {MatCheckboxChange} from "@angular/material/checkbox";
import {merge, of} from "rxjs";
import {catchError, filter, map, startWith, switchMap, tap} from "rxjs/operators";
import {DefinitionmatningstypService} from "../services/definitionmatningstyp.service";
import {formatNumber} from "@angular/common";
import { isTouch } from "../common/touch-utils";
import moment from "moment";
import {momentDateEndOfDayToISOString, momentDateStartOfDayToISOString} from "../common/date-utils";
import {UserService} from "../services/user.service";


@Component({
  selector: "mdb-search-matning",
  template: `
    <form [formGroup]="searchForm">
      <div class="search-form-fields">
      <mat-form-field>
        <mat-label>Från och med</mat-label>
        <input matInput placeholder="åååå-mm-dd" required [matDatepicker]="fromDatumFrompicker" formControlName="fromDatum">
        <mat-datepicker-toggle matSuffix [for]="fromDatumFrompicker"></mat-datepicker-toggle>
        <mat-datepicker [touchUi]="isTouch" #fromDatumFrompicker></mat-datepicker>

        <mat-error *ngIf="searchForm.get('fromDatum').hasError('tombeforefrom')">Fr.o.m får inte vara efter t.o.m
          datum
        </mat-error>
      </mat-form-field>
      <mat-form-field>
        <mat-label>Till och med</mat-label>
        <input matInput placeholder="åååå-mm-dd" required [matDatepicker]="tomDatumFrompicker" formControlName="tomDatum">
        <mat-datepicker-toggle matSuffix [for]="tomDatumFrompicker"></mat-datepicker-toggle>
        <mat-datepicker [touchUi]="isTouch" #tomDatumFrompicker></mat-datepicker>

        <mat-error *ngIf="searchForm.get('tomDatum').hasError('tombeforefrom')">Fr.o.m får inte vara efter t.o.m
          datum
        </mat-error>
      </mat-form-field>
      <mat-form-field *ngIf="userService.userDetails.isMatrapportor()">
        <select matNativeControl placeholder="Mätningsstatus" formControlName="status">
          <option [value]="-1">Alla status</option>
          <option *ngFor="let ms of MatningStatus | keyvalue" [value]="ms.key">{{ms.value}}</option>
        </select>
      </mat-form-field>
      </div>
      <div class="matningar-actions">
        <button mat-raised-button color="primary" (click)="onSokMatning(searchForm)" >Filtrera mätvärden</button>
        <button mat-raised-button color="primary" (click)="resetSearch()">Rensa</button>
      </div>
    </form>
    <div class="search-result">
      <h3>Mätdata</h3>
      <table mat-table [dataSource]="matning" matSort matSortActive="avlastDatum" matSortDisableClear matSortDirection="desc">
        <ng-container matColumnDef="checkbox">
          <th mat-header-cell *matHeaderCellDef>
            <mat-checkbox (change)="onSelectAllOnPage($event)"></mat-checkbox>
          </th>
          <td mat-cell *matCellDef="let row">
            <mat-checkbox (change)="toggleMatning(row.id)"
                          [checked]="selectedMatning.has(row.id)">
            </mat-checkbox>
          </td>
        </ng-container>
        <ng-container matColumnDef="avlastDatum">
          <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Datum</th>
          <td mat-cell *matCellDef="let row"><a routerLink="{{row.id}}">{{formatDatum(row.avlastDatum)}}</a></td>
        </ng-container>
        <ng-container matColumnDef="rapportor">
          <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Rapportör</th>
          <td mat-cell *matCellDef="let row">{{row.rapportor}}</td>
        </ng-container>
        <ng-container matColumnDef="avlastVarde">
          <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Uppmätt {{storhet}}</th>
          <td mat-cell *matCellDef="let row">{{getAvlastVarde(row)}}</td>
        </ng-container>
        <ng-container matColumnDef="beraknatVarde">
          <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Beräknat {{beraknadStorhet}}</th>
          <td mat-cell *matCellDef="let row">{{getBeraknatVarde(row)}}</td>
        </ng-container>
        <ng-container matColumnDef="status">
          <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Status</th>
          <td mat-cell class="wrap-text" *matCellDef="let row">{{MatningStatus[row.status]}}</td>
        </ng-container>
        <ng-container matColumnDef="kommentar">
          <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Kommentar</th>
          <td mat-cell class="wrap-text" *matCellDef="let row">{{row.kommentar}}</td>
        </ng-container>
        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
      </table>
      <mat-paginator [length]="resultsLength" [pageSize]="10" [pageSizeOptions]="[10, 25, 50]"></mat-paginator>
      <h4>Valda: {{selectedMatning.size}} st</h4>
    </div>
  `,
  styles: [
    `
    .main-content {
      display: grid;
      grid-gap: 1rem;
    }

    form {
      display: grid;
    }

    .search-form-fields {
      display: grid;
    }

    .search-result {
      display: grid;
      grid-gap: 0;
      margin-top: 1rem;
    }

    .checkboxes {
      display: grid;
      grid-gap: 0;
    }

    .mat-column-checkbox {
      width: 5%;
    }

    .mat-column-avlastVarde, .mat-column-beraknatVarde, .mat-column-rapportor, .mat-column-status {
      width: 10%;
    }


    .mat-column-avlastDatum, .mat-column-kommentar {
      width: 15%;
    }

    .matningar-actions {
      display: grid;
      grid-gap: 1rem;
      grid-template-columns: auto auto 1fr;
      padding-bottom: 1rem;
    }
    @media only screen and (min-width: 980px) {
      .search-form-fields {
        grid-column-gap: 1rem;
        grid-template-columns: repeat(4, 1fr);
      }
    }

    @media only screen and (min-width: 576px) and (max-width: 979px) {
      .search-form-fields {
        grid-column-gap: 1rem;
        grid-template-columns: repeat(2, 1fr);
      }
    }
  `
  ]
})
export class SearchMatningComponent implements OnInit, AfterViewInit, OnChanges {

  @Input() matningstypId: number;
  @Input() matobjektId: number;
  @Input() updatedMatning: Matning;
  @Output() selected = new EventEmitter<number[]>();

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  displayedColumns: string[] = ["checkbox", "avlastDatum", "rapportor", "avlastVarde", "beraknatVarde", "status", "kommentar"];

  MatningStatus: {[key: number]: string} = {0: "Ej granskat", 1: "Godkänt", 2: "Fel"};
  detektion: {[key: number]: string} = {0: ">", 1: "", 2: "<"};
  searching = true;
  searchForm: UntypedFormGroup;
  matningFilter: MatningFilter = {};
  tomDatum: moment.Moment;
  fromDatum: moment.Moment;
  matning: Matning[] = [];
  selectedMatning: Set<number> = new Set<number>();
  resultsLength = 0;
  decimaler: number;
  enhet: string;
  storhet: string;
  beraknadEnhet: string;
  beraknadStorhet: string;
  isTouch = isTouch();

  constructor(private matobjektService: MatobjektService, public userService: UserService,
              private definitionmatningstypService: DefinitionmatningstypService) { }

  ngOnInit() {
    this.initDatum();
    this.matningFilter = {
      fromDatum: momentDateStartOfDayToISOString(this.fromDatum),
      tomDatum: momentDateEndOfDayToISOString(this.tomDatum),
      status: this.userService.userDetails.isMatrapportor() ? null : 1
    };
    this.matobjektService.getMatningstyp(this.matobjektId, this.matningstypId)
      .pipe( tap(matningstyp => {
        this.enhet = matningstyp.enhet;
        this.decimaler = matningstyp.decimaler;
        }),
        switchMap(matningstyp => this.definitionmatningstypService.
      get(matningstyp.definitionMatningstypId))).subscribe(definition => {
        this.beraknadEnhet = definition.beraknadEnhet;
        this.storhet = definition.storhet;
        this.beraknadStorhet = definition.beraknadStorhet;
    });

    this.searchForm = new UntypedFormGroup({
      fromDatum: new UntypedFormControl(this.fromDatum, this.validateFromTomDatum),
      tomDatum: new UntypedFormControl(this.tomDatum, this.validateFromTomDatum),
      status: new UntypedFormControl(this.userService.userDetails.isMatrapportor() ? -1 : 1),
    });
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        filter(() => this.matningFilter != null),
        switchMap(() => {
          this.searching = true;
          return this.matobjektService.getMatningPage(
            this.matobjektId,
            this.matningstypId,
            this.paginator.pageIndex,
            this.paginator.pageSize,
            this.sort.active,
            this.sort.direction,
            this.matningFilter);
        }),
        map(data => {
          this.resultsLength = data.totalElements;
          return data.content;
        }),
        catchError(() => {
          return of([]);
        })
      ).subscribe(data => {
      this.matning = data;
      this.searching = false;
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.updateRow(changes["updatedMatning"].currentValue);
  }

  validateFromTomDatum(control: UntypedFormControl): ValidationErrors {
    const fromControl = control.root.get("fromDatum");
    const from: moment.Moment = fromControl != null ? fromControl.value : null;
    const tomControl = control.root.get("tomDatum");
    const tom: moment.Moment = tomControl != null ? tomControl.value : null;
    if (from != null && tom != null) {
      if (from.isAfter(tom)) {
        return {"tombeforefrom": {value: control.value}};
      }
    }
    return null;
  }

  initDatum() {
    const dateToday = moment();
    const dateFrom = moment().subtract(2, "year");

    this.tomDatum = dateToday;
    this.fromDatum = dateFrom;
  }

  resetSearch() {
    this.initDatum();
    this.searchForm.get("status").setValue(-1);
    this.searchForm.get("fromDatum").setValue(this.fromDatum);
    this.searchForm.get("tomDatum").setValue(this.tomDatum);
    this.onSokMatning(this.searchForm);
  }

  onSokMatning(form: UntypedFormGroup) {
    this.selectedMatning.clear();
    this.emitSelectedMatning();
    this.updateMatningFilter();
    this.paginator.pageIndex = 0;
    this.paginator._changePageSize(this.paginator.pageSize);
  }

  updateMatningFilter() {
    let status = this.searchForm.get("status").value;
    if (status < 0) {
      status = null;
    }
    this.matningFilter = {
      fromDatum: momentDateStartOfDayToISOString(this.searchForm.get("fromDatum").value),
      tomDatum: momentDateEndOfDayToISOString(this.searchForm.get("tomDatum").value),
      status: status,
    };
  }

  toggleMatning(id: number) {
    if (this.selectedMatning.has(id)) {
      this.selectedMatning.delete(id);
    } else {
      this.selectedMatning.add(id);
    }

    this.emitSelectedMatning();
  }

  onSelectAllOnPage(event: MatCheckboxChange) {
    this.matning.forEach(lm => {
      if (event.checked) {
        this.selectedMatning.add(lm.id);
      } else {
        this.selectedMatning.delete(lm.id);
      }
    });

    this.emitSelectedMatning();
  }

  updateRow(updatedMatning: Matning) {
    this.matning.map(matning => {
      if (matning.id === updatedMatning.id) {
        matning.kommentar = updatedMatning.kommentar;
        matning.avlastVarde = updatedMatning.avlastVarde;
        matning.status = updatedMatning.status;
        matning.beraknatVarde = updatedMatning.beraknatVarde;
      }
    });

    if (updatedMatning && this.selectedMatning.has(updatedMatning.id)) {
      this.toggleMatning(updatedMatning.id);
    }
  }

  emitSelectedMatning() {
    this.selected.emit(Array.from(this.selectedMatning.values()));
  }

  formatDatum(datum: string): string {
    return datum.replace("T", " ").substring(0, datum.lastIndexOf(":"));
  }

  setDigits(): string {
    return "1.0-" + (this.decimaler ? this.decimaler : 4);
  }

  formatValue(value): string {
    return formatNumber(value, "sv-SE", this.setDigits());
  }

  getBeraknatVarde(matning: Matning): string {
    // 0 is falsy, so we need to check the type as well.
    // null isn't type of number, so it shouldn't be shown.
    if (matning.beraknatVarde || (typeof matning.beraknatVarde === "number")) {
      return this.formatValue(matning.beraknatVarde) + " " + this.beraknadEnhet;
    } else {
      return null;
    }
  }

  getAvlastVarde(matning: Matning): string {
    if (matning.felkod !== FELKOD_OK) {
      return matning.felkod;
    } else if (matning.avlastVarde || (typeof matning.avlastVarde === "number")) {
      return this.detektion[matning.inomDetektionsomrade]  + " " + this.formatValue(matning.avlastVarde) + " " + this.enhet;
    } else {
      return null;
    }
  }
}
