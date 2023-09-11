import {AfterViewInit, Component, Input, ViewChild} from "@angular/core";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {merge, of} from "rxjs";
import {AnvandargruppService, Matansvar} from "../../../services/anvandargrupp.service";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";

@Component({
  selector: "mdb-anvandargrupp-matansvar",
  template: `
      <table class="table" mat-table [dataSource]="data" matSort matSortActive="matobjekt" matSortDisableClear
             matSortDirection="asc">
          <ng-container matColumnDef="matobjekt">
              <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Mätobjekt</th>
              <td mat-cell *matCellDef="let row"><a routerLink="/matobjekt/{{row.matobjektId}}">{{row.matobjekt}}</a></td>
          </ng-container>
          <ng-container matColumnDef="matningstyp">
              <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Mätningstyp</th>
              <td mat-cell *matCellDef="let row">{{row.matningstyp}}</td>
          </ng-container>
          <ng-container matColumnDef="fastighet">
              <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Fastighet</th>
              <td mat-cell *matCellDef="let row">{{row.fastighet}}</td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="columns"></tr>
          <tr mat-row *matRowDef="let row; columns: columns;"></tr>
      </table>
      <mat-paginator [length]="resultsLength" [pageSize]="10"
                     [pageSizeOptions]="[10, 20, 50]"></mat-paginator>
  `,
  styles: [`
      :host {
          display: grid;
      }
  `]
})
export class AnvandargruppMatansvarComponent implements AfterViewInit {
  @Input() gruppId: number;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  isLoadingResults = true;
  resultsLength = 0;
  data: Matansvar[];
  columns = ["matobjekt", "matningstyp", "fastighet"];

  constructor(private service: AnvandargruppService) {
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.service.getMatansvar(this.gruppId,
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
}
