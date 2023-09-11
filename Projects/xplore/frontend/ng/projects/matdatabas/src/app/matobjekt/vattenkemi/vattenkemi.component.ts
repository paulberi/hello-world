import {AfterViewInit, Component, OnInit, ViewChild} from "@angular/core";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {Analys, MatobjektService} from "../../services/matobjekt.service";
import {ActivatedRoute} from "@angular/router";
import {merge, of} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";

@Component({
  selector: "mdb-vattenkemi",
  template: `
    <table mat-table [dataSource]="data" matSort matSortActive="analysDatum" matSortDisableClear matSortDirection="desc">
      <ng-container matColumnDef="analysDatum">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Datum</th>
        <td mat-cell *matCellDef="let row"><a routerLink="{{row.id}}">{{formatDatum(row.analysDatum)}}</a></td>
      </ng-container>
      <ng-container matColumnDef="rapportor">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Rapportör</th>
        <td mat-cell *matCellDef="let row">{{row.rapportor}}</td>
      </ng-container>
      <ng-container matColumnDef="rapporter">
        <th mat-header-cell *matHeaderCellDef [disabled]="true" mat-sort-header disableClear>Analysrapport</th>
        <td mat-cell *matCellDef="let row">
          <div *ngFor="let rapport of row.rapporter">
            <a href="{{getRapportLink(rapport.id)}}">{{rapport.filnamn}}</a>
          </div>
        </td>
      </ng-container>
      <ng-container matColumnDef="kommentar">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Kommentar</th>
        <td mat-cell *matCellDef="let row">{{row.kommentar}}</td>
      </ng-container>
      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
    <mat-paginator [length]="resultsLength" [pageSize]="20" [pageSizeOptions]="[10, 20, 50]"></mat-paginator>
    <div class="actions">
      <button routerLink="new" mat-raised-button color="primary">Lägg till analys</button>
    </div>
  `,
  styles: [`
    table {
      margin-bottom: 1rem;
    }
  `]
})
export class VattenkemiComponent implements AfterViewInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  displayedColumns: string[] = ["analysDatum", "rapportor", "rapporter", "kommentar"];
  resultsLength = 0;
  isLoadingResults = true;
  matobjektId: number;
  data: Analys[];

  constructor(private route: ActivatedRoute, private matobjektService: MatobjektService) { }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          this.matobjektId = +this.route.parent.snapshot.paramMap.get("id");
          return this.matobjektService.getVattenkemiPage(this.matobjektId,
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

  getRapportLink(rapportId: number): string {
    return "/api/bifogadfil/" + rapportId + "/data";
  }

  formatDatum(datum: string): string {
    return datum.replace("T", " ").substring(0, datum.lastIndexOf(":"));
  }

}
