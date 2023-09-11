import {AfterViewInit, Component, ViewChild} from "@angular/core";
import {merge, of} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import {Anvandargrupp, AnvandargruppService} from "../../services/anvandargrupp.service";

@Component({
  selector: "mdb-anvandargupper",
  template: `
    <div class="main-content">
      <h2>Användargrupper</h2>
      <div class="actions">
        <button routerLink="new" mat-raised-button color="primary">Lägg till användargrupp</button>
      </div>
      <table mat-table [dataSource]="data" matSort matSortActive="namn" matSortDisableClear matSortDirection="asc">
        <ng-container matColumnDef="namn">
          <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Namn</th>
          <td mat-cell *matCellDef="let row"><a routerLink="{{row.id}}">{{row.namn}}</a></td>
        </ng-container>
        <ng-container matColumnDef="beskrivning">
          <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Beskrivning</th>
          <td mat-cell *matCellDef="let row">{{row.beskrivning}}</td>
        </ng-container>
        <ng-container matColumnDef="antalAnvandare">
          <th mat-header-cell *matHeaderCellDef mat-sort-header [arrowPosition]="'before'"
              disableClear>Användare
          </th>
          <td mat-cell *matCellDef="let row">{{row.antalAnvandare}} st</td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
      </table>
      <mat-paginator [length]="resultsLength" [pageSize]="10"
                     [pageSizeOptions]="[10, 20, 50]"></mat-paginator>
    </div>
  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
    }

    .mat-column-beskrivning {
      width: 50%;
    }

    .mat-column-antalAnvandare {
      text-align: right;
    }

  `]
})
export class AnvandargruppPageComponent implements AfterViewInit {
  displayedColumns: string[] = ["namn", "beskrivning", "antalAnvandare"];
  resultsLength = 0;
  isLoadingResults = true;
  data: Anvandargrupp[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private anvandargruppService: AnvandargruppService) {
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.anvandargruppService.getPage(
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
