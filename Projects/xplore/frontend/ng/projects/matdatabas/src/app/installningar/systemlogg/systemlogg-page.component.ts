import {Component, OnInit, ViewChild, AfterViewInit, Output, EventEmitter} from "@angular/core";
import {Systemlogg, SystemloggService, SystemloggSearchParams} from "../../services/systemlogg.service";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import {merge, of} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {UntypedFormGroup, UntypedFormBuilder, UntypedFormControl, ValidationErrors} from "@angular/forms";
import moment from "moment";
import {isTouch} from "../../common/touch-utils";
import {momentDateEndOfDayToISOString, momentDateStartOfDayToISOString} from "../../common/date-utils";

@Component({
  selector: "mdb-systemlogg-page",
  template: `
    <h2>Systemlogg</h2>
    <form [formGroup]="searchForm">
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
      <mat-form-field>
        <mat-select placeholder="Händelse" formControlName="handelse">
          <mat-option [value]=-1>{{handelseMapping.get(-1)}}</mat-option>
          <mat-option [value]=0>{{handelseMapping.get(0)}}</mat-option>
          <mat-option [value]=1>{{handelseMapping.get(1)}}</mat-option>
          <mat-option [value]=2>{{handelseMapping.get(2)}}</mat-option>
          <mat-option [value]=3>{{handelseMapping.get(3)}}</mat-option>
          <mat-option [value]=4>{{handelseMapping.get(4)}}</mat-option>
          <mat-option [value]=5>{{handelseMapping.get(5)}}</mat-option>
          <mat-option [value]=6>{{handelseMapping.get(6)}}</mat-option>
        </mat-select>
      </mat-form-field>
      <button mat-raised-button color="primary" (click)="search()" [disabled]="searchForm.invalid">Sök</button>
    </form>

    <table mat-table [dataSource]="data" matSort matSortActive="datum" matSortDisableClear matSortDirection="desc">
      <ng-container matColumnDef="datum">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Datum</th>
        <td mat-cell *matCellDef="let row">{{formatDatum(row.datum)}}</td>
      </ng-container>
      <ng-container matColumnDef="anvandarnamn">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Användare</th>
        <td mat-cell *matCellDef="let row">{{row.anvandarnamn}}</td>
      </ng-container>
      <ng-container matColumnDef="handelse">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Händelse</th>
        <td mat-cell *matCellDef="let row">{{this.handelseMapping.get(row.handelse)}}</td>
      </ng-container>
      <ng-container matColumnDef="beskrivning">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Beskrivning</th>
        <td mat-cell *matCellDef="let row">
          <div style="display: inline-block; word-wrap: break-word">
            {{row.beskrivning}}
          </div>
        </td>
      </ng-container>
      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
    <mat-paginator [length]="resultsLength" [pageSize]="50" [pageSizeOptions]="[10, 25, 50]"></mat-paginator>
  `,
  styles: [`
    mat-form-field {
      margin-right: 1rem;
    }

    .mat-column-datum {
      width: 250px;
    }

    .mat-column-anvandarnamn {
      width: 250px;
    }

    .mat-column-handelse {
      width: 200px;
    }

    @media only screen and (min-width: 576px) {
      .mat-column-datum {
        width: auto;
      }

      .mat-column-anvandarnamn {
        width: auto;
      }

      .mat-column-handelse {
        width: auto;
      }
    }
  `]
})
export class SystemloggPageComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @Output() searchChange = new EventEmitter<SystemloggSearchParams>();

  displayedColumns: string[] = ["datum", "anvandarnamn", "handelse", "beskrivning"];
  handelseMapping = new Map([[-1, "Alla"], [0, "Mätningstyp"],
    [1, "Inloggning"], [2, "Användare"], [3, "System"], [4, "Import"], [5, "Rapport"], [6, "Mätning"]]);

  resultsLength = 0;
  isLoadingResults = true;
  data: Systemlogg[] = [];
  searchForm: UntypedFormGroup;
  searchParams: SystemloggSearchParams;
  isTouch = isTouch();

  constructor(
    private systemloggService: SystemloggService,
    private formBuilder: UntypedFormBuilder) {
  }

  ngOnInit() {
    const dateToday = moment();
    const dateFrom = moment().subtract(2, "day");
    this.searchParams = new SystemloggSearchParams();
    this.searchParams.tomDatum = momentDateEndOfDayToISOString(dateToday);
    this.searchParams.fromDatum = momentDateStartOfDayToISOString(dateFrom);
    this.searchForm = this.formBuilder.group({
      fromDatum: [dateFrom, this.validateFromTomDatum],
      tomDatum: [dateToday, this.validateFromTomDatum],
      handelse: [this.searchParams.handelse]
    });
  }

  ngAfterViewInit() {
    merge(this.sort.sortChange, this.searchChange).subscribe(() => this.paginator.pageIndex = 0);
    merge(this.sort.sortChange, this.paginator.page, this.searchChange)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.systemloggService.getPage(this.paginator.pageIndex, this.paginator.pageSize, this.sort.active, this.sort.direction,
            this.searchParams);
        }),
        map(data => {
          this.isLoadingResults = false;
          this.resultsLength = data.totalElements;
          return data.content;
        }),
        catchError(() => {
          this.isLoadingResults = false;
          return of([]);
        })
      ).subscribe(data => this.data = data);
  }

  search() {
    this.searchParams.fromDatum = momentDateStartOfDayToISOString(this.searchForm.get("fromDatum").value);
    this.searchParams.tomDatum = momentDateEndOfDayToISOString(this.searchForm.get("tomDatum").value);
    this.searchParams.handelse = this.searchForm.get("handelse").value;
    this.searchChange.emit(this.searchParams);
  }

  validateFromTomDatum(control: UntypedFormControl): ValidationErrors {
    const fromControl = control.root.get("fromDatum");
    const from = fromControl != null ? fromControl.value : null;
    const tomControl = control.root.get("tomDatum");
    const tom = tomControl != null ? tomControl.value : null;
    if (from != null && tom != null) {
      const fromDate = new Date(from);
      const tomDate = new Date(tom);
      if (fromDate > tomDate) {
        return {"tombeforefrom": {value: control.value}};
      }
    }
    return null;
  }

  formatDatum(datum: string): string {
    return datum.replace("T", " ").substring(0, datum.lastIndexOf(":"));
  }
}
