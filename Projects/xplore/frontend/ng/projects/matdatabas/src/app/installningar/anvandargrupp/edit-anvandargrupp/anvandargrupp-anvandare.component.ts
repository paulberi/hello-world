import {AfterViewInit, Component, Input, ViewChild} from "@angular/core";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {merge, of} from "rxjs";
import {AnvandargruppService} from "../../../services/anvandargrupp.service";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {Anvandare} from "../../../services/anvandare.service";

@Component({
  selector: "mdb-anvandargrupp-anvandare",
  template: `
      <ng-container>
          <table class="table" mat-table [dataSource]="data" matSort matSortActive="inloggningsnamn" matSortDisableClear
                 matSortDirection="asc">
              <ng-container matColumnDef="inloggningsnamn">
                  <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Användarnamn</th>
                  <td mat-cell *matCellDef="let row"><a routerLink="/anvandare/{{row.id}}">{{row.inloggningsnamn}}</a>
                  </td>
              </ng-container>
              <ng-container matColumnDef="namn">
                  <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Namn</th>
                  <td mat-cell *matCellDef="let row">{{row.namn}}</td>
              </ng-container>
              <ng-container matColumnDef="foretag">
                  <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Företag</th>
                  <td mat-cell *matCellDef="let row">{{row.foretag}}</td>
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
export class AnvandargruppAnvandareComponent implements AfterViewInit {
  @Input() gruppId: number;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  isLoadingResults = true;
  resultsLength = 0;
  data: Anvandare[];
  columns = ["inloggningsnamn", "namn", "foretag"];

  constructor(private service: AnvandargruppService) {
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.service.getAnvandare(this.gruppId,
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
