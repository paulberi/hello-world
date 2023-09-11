import {AfterViewInit, Component, OnInit, ViewChild} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../services/user.service";
import {merge, of} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {ListRapport, RapportService} from "../services/rapport.service";

@Component({
  selector: "mdb-rapporter",
  template: `
    <h2>Hantera rapporter</h2>
    <div class="actions">
      <button *ngIf="userService.userDetails.isTillstandshandlaggare()" routerLink="../editrapport" mat-raised-button
              color="primary">Lägg till rapport</button>
    </div>
    <table mat-table [dataSource]="data" matSort matSortActive="namn" matSortDisableClear matSortDirection="asc">
      <ng-container matColumnDef="namn">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Namn</th>
        <td mat-cell *matCellDef="let row"><a routerLink="../editrapport/{{row.id}}">{{row.namn}}</a></td>
      </ng-container>
      <ng-container matColumnDef="beskrivning">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Beskrivning</th>
        <td mat-cell *matCellDef="let row">{{row.beskrivning}}</td>
      </ng-container>
      <ng-container matColumnDef="senastSkickad">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Senast skickad</th>
        <td mat-cell *matCellDef="let row">{{formatDatum(row.senastSkickad)}}</td>
      </ng-container>
      <ng-container matColumnDef="aktiv">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Aktiv</th>
        <td mat-cell *matCellDef="let row">{{row.aktiv ? "Ja" : "Nej"}}</td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
    <mat-paginator [length]="resultsLength" [pageSize]="50" [pageSizeOptions]="[10, 20, 50]"></mat-paginator>
  `,
  styles: []
})
export class RapporterComponent implements AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  displayedColumns: string[] = ["namn", "beskrivning", "senastSkickad", "aktiv"];
  resultsLength = 0;
  isLoadingResults = true;
  data: ListRapport[];

  constructor(private route: ActivatedRoute, private rapportService: RapportService,
              public userService: UserService) { }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.rapportService.getPage(this.paginator.pageIndex, this.paginator.pageSize, this.sort.active, this.sort.direction);
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

  formatDatum(datum: string): string {
    return datum ? datum.replace("T", " ").substring(0, datum.lastIndexOf(":")) : null;
  }
}
