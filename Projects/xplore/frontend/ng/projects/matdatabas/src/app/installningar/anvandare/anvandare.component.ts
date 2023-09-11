import {AfterViewInit, Component, ViewChild} from "@angular/core";
import {Anvandare, AnvandareService} from "../../services/anvandare.service";
import {merge, of} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import { MatCheckboxChange } from "@angular/material/checkbox";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";

@Component({
  selector: "mdb-anvandare",
  template: `
    <h2>Användare</h2>
    <div class="actions">
      <button routerLink="../anvandare/new" mat-raised-button color="primary">Lägg till användare</button>
    </div>
    <table mat-table [dataSource]="data" class=""
           matSort matSortActive="inloggningsnamn" matSortDisableClear matSortDirection="asc">
      <ng-container matColumnDef="inloggningsnamn">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Användarnamn</th>
        <td mat-cell *matCellDef="let row"><a routerLink="../anvandare/{{row.id}}">{{row.inloggningsnamn}}</a></td>
      </ng-container>
      <ng-container matColumnDef="namn">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Namn</th>
        <td mat-cell *matCellDef="let row">{{row.namn}}</td>
      </ng-container>
      <ng-container matColumnDef="foretag">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Företag</th>
        <td mat-cell *matCellDef="let row">{{row.foretag}}</td>
      </ng-container>
      <ng-container matColumnDef="behorighet">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Behörighet</th>
        <td mat-cell *matCellDef="let row">{{this.displayValueBehorighet(row.behorighet)}}</td>
      </ng-container>
      <ng-container matColumnDef="aktiv">
        <th mat-header-cell class="narrow" *matHeaderCellDef mat-sort-header disableClear [arrowPosition]="'before'">Aktiv</th>
        <td mat-cell class="narrow" *matCellDef="let row">{{row.aktiv ? "Ja" : "Nej"}}</td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
    <div class="table-footer">
      <mat-checkbox class="footer-item" (change)="onVisaInaktivaChange($event)" [checked]="visaInaktiva">Visa
        inaktiva
      </mat-checkbox>
      <mat-paginator class="footer-item" [length]="resultsLength" [pageSize]="10"
                     [pageSizeOptions]="[10, 25, 50]"></mat-paginator>
    </div>
  `,
  styles: [`
    .narrow {
      width: 60px;
    }

    .table-footer {
      display: table;
      width: 100%;
    }

    .footer-item {
      display: table-cell;
      vertical-align: middle;
    }
  `]
})
export class AnvandareComponent implements AfterViewInit {
  displayedColumns: string[] = ["inloggningsnamn", "namn", "foretag", "behorighet", "aktiv"];
  visaInaktiva = true;
  resultsLength = 0;
  data: Anvandare[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private anvandareService: AnvandareService) {
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          return this.anvandareService.getPage(
            this.paginator.pageIndex, this.paginator.pageSize, this.sort.active, this.sort.direction, this.visaInaktiva);
        }),
        map(data => {
          this.resultsLength = data.totalElements;
          return data.content;
        }),
        catchError(() => {
          return of([]);
        })
      ).subscribe(data => this.data = data);
  }

  displayValueBehorighet(behorighet: number): string {
    switch (behorighet) {
      case -1:
        return "Observatör";
      case 0:
        return "Mätrapportör";
      case 1:
        return "Tillståndshandläggare";
      case 2:
        return "Administratör";
    }

    return "";
  }

  onVisaInaktivaChange(event: MatCheckboxChange) {
    this.visaInaktiva = event.checked;
    this.paginator.pageIndex = 0;
    this.paginator._changePageSize(this.paginator.pageSize);
  }
}
