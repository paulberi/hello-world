import {AfterViewInit, Component, Input, ViewChild} from "@angular/core";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {merge, of} from "rxjs";
import {AnvandargruppService} from "../../../services/anvandargrupp.service";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {formatNumber} from "@angular/common";
import {Larm, LarmService} from "../../../services/larm.service";

@Component({
  selector: "mdb-anvandargrupp-larm",
  template: `
      <ng-container>
          <table class="table" mat-table [dataSource]="data" matSort matSortActive="matobjekt" matSortDisableClear
                 matSortDirection="asc">
              <ng-container matColumnDef="matobjekt">
                  <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Mätobjekt</th>
                  <td mat-cell *matCellDef="let row"><a routerLink="/matobjekt/{{row.matobjektId}}">{{row.matobjektNamn}}</a>
                  </td>
              </ng-container>
              <ng-container matColumnDef="matningstyp">
                  <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Mätningstyp</th>
                  <td mat-cell *matCellDef="let row">{{row.matningstypNamn}}</td>
              </ng-container>
              <ng-container matColumnDef="fastighet">
                  <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Fastighet</th>
                  <td mat-cell *matCellDef="let row">{{row.matobjektFastighet}}</td>
              </ng-container>
              <ng-container matColumnDef="larmniva">
                  <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Larmnivå</th>
                  <td mat-cell *matCellDef="let row">{{row.larmnivaNamn}}</td>
              </ng-container>
              <ng-container matColumnDef="gransvarde">
                  <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Gränsvarde</th>
                  <td mat-cell *matCellDef="let row">{{formatValue(row.gransvarde)}}</td>
              </ng-container>

              <tr mat-header-row *matHeaderRowDef="columns"></tr>
              <tr mat-row *matRowDef="let row; columns: columns;"></tr>
          </table>
          <mat-paginator [length]="resultsLength" [pageSize]="10"
                         [pageSizeOptions]="[10, 20, 50]"></mat-paginator>
      </ng-container>
  `,
  styles: [`
      :host {
          display: grid;
      }
  `]
})
export class AnvandargruppLarmComponent implements AfterViewInit {
  @Input() gruppId: number;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  isLoadingResults = true;
  resultsLength = 0;
  data: Larm[];
  columns = ["matobjekt", "matningstyp", "fastighet", "larmniva", "gransvarde"];

  constructor(private service: AnvandargruppService, private larmService: LarmService) {
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.larmService.getLarmPage(this.paginator.pageIndex, this.paginator.pageSize,
            this.sort.active, this.sort.direction, null, false,
            [this.gruppId], null, {});
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

  formatValue(value): string {
    return formatNumber(value, "sv-SE");
  }
}
