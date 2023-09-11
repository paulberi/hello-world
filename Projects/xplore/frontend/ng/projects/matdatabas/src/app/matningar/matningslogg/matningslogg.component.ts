import {AfterViewInit, Component, Input, OnChanges, OnInit, SimpleChanges, ViewChild} from "@angular/core";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {Matningslogg, MatningsloggService} from "../../services/matningslogg.service";
import {merge, of} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {Matning} from "../../services/matobjekt.service";

@Component({
  selector: "mdb-matningslogg",
  template: `
    <table mat-table [dataSource]="data" matSort matSortActive="loggatDatum" matSortDisableClear matSortDirection="asc">
      <ng-container matColumnDef="loggatDatum">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Datum</th>
        <td mat-cell *matCellDef="let row">{{formatDatum(row.loggatDatum)}}</td>
      </ng-container>
      <ng-container matColumnDef="handelse">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Typ</th>
        <td mat-cell *matCellDef="let row">{{handelse[row.handelse]}}</td>
      </ng-container>
      <ng-container matColumnDef="beskrivning">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Beskrivning</th>
        <td mat-cell *matCellDef="let row">{{row.beskrivning}}</td>
      </ng-container>
      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
    <mat-paginator [length]="resultsLength" [pageSize]="10" [pageSizeOptions]="[10, 20, 50]"></mat-paginator>
  `,
  styles: [`
    table {
      margin-bottom: 1rem;
    }
    .mat-column-loggatDatum, .mat-column-handelse, .mat-column-beskrivning {
      width: 10%;
    }
  `]
})
export class MatningsloggComponent implements OnInit, AfterViewInit, OnChanges {

  @Input() matning: Matning;
  displayedColumns: string[] = ["loggatDatum", "handelse", "beskrivning"];
  resultsLength = 0;
  isLoadingResults = true;
  data: Matningslogg[];
  handelse: {[key: number]: string} = {
    0: "Rapporterat",
    1: "Importerat",
    2: "Beräkning",
    3: "Felmarkerat",
    4: "Korrigerat",
    5: "Godkänt",
    6: "Ändrat"
  };

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  constructor(private matningsloggService: MatningsloggService) { }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.matningsloggService.getPage(this.matning.id,
            this.paginator.pageIndex, this.paginator.pageSize, this.sort.active, this.sort.direction);
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

  ngOnChanges(changes: SimpleChanges): void {
    const change = changes["matning"];
    if (!change.firstChange) {
      this.paginator._changePageSize(this.paginator.pageSize);
    }

  }

  formatDatum(datum: string): string {
    return datum.replace("T", " ").substring(0, datum.lastIndexOf(":"));
  }
}
