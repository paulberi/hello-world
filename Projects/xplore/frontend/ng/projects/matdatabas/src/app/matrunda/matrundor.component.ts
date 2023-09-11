import {AfterViewInit, Component, ViewChild} from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import {ActivatedRoute} from "@angular/router";
import {merge, of} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {MatrundaService, ListMatrunda} from "../services/matrunda.service";
import {UserService} from "../services/user.service";

@Component({
  selector: "mdb-matrundor",
  template: `
    <h2>Skapa mätrundor</h2>
    <div class="actions">
      <button *ngIf="userService.userDetails.isTillstandshandlaggare()" routerLink="new" mat-raised-button
              color="primary">Lägg till mätrunda</button>
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
      <ng-container matColumnDef="aktiv">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Aktiv</th>
        <td mat-cell *matCellDef="let row">{{row.aktiv ? "Ja" : "Nej"}}</td>
      </ng-container>
      <ng-container matColumnDef="antalMatobjekt">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Antal mätobjekt</th>
        <td mat-cell *matCellDef="let row">{{row.antalMatobjekt}}</td>
      </ng-container>
      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
    <mat-paginator [length]="resultsLength" [pageSize]="50" [pageSizeOptions]="[10, 20, 50]"></mat-paginator>
  `,
  styles: [`
    table {
      margin-bottom: 1rem;
    }

    .mat-column-aktiv {
      width: 10%;
    }

    .mat-column-antalMatobjekt {
      width: 10%;
      text-align: right;
    }

  `]
})
export class MatrundorComponent implements AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  displayedColumns: string[] = ["namn", "beskrivning", "aktiv", "antalMatobjekt"];
  resultsLength = 0;
  isLoadingResults = true;
  data: ListMatrunda[];

  constructor(private route: ActivatedRoute, private matrundaService: MatrundaService,
              public userService: UserService) { }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.matrundaService.getPage(this.paginator.pageIndex, this.paginator.pageSize, this.sort.active, this.sort.direction);
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
