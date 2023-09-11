import {AfterViewInit, Component, ViewChild} from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import {ActivatedRoute} from "@angular/router";
import {merge, of} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {Handelse, MatobjektService} from "../../services/matobjekt.service";
import {UserService} from "../../services/user.service";

@Component({
  selector: "mdb-handelser",
  template: `
    <table mat-table [dataSource]="data" matSort matSortActive="datum" matSortDisableClear matSortDirection="desc">
      <ng-container matColumnDef="benamning">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Händelse</th>
        <td mat-cell *matCellDef="let row"><a routerLink="{{row.id}}">{{row.benamning}}</a></td>
      </ng-container>
      <ng-container matColumnDef="datum">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Datum</th>
        <td mat-cell *matCellDef="let row">{{formatDatum(row.datum)}}</td>
      </ng-container>
      <ng-container matColumnDef="foretag">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Loggad av</th>
        <td mat-cell *matCellDef="let row">{{row.foretag}}</td>
      </ng-container>
      <ng-container matColumnDef="beskrivning">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Beskrivning och bilder</th>
        <td mat-cell *matCellDef="let row">
          {{row.beskrivning}}
          <div *ngIf="row.bifogadebilder.length > 0">
            Bilder:
          </div>
          <div *ngFor="let bild of row.bifogadebilder">
            <a href="{{getBildLink(bild.id)}}">{{bild.filnamn}}</a>
          </div>
        </td>
      </ng-container>
      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
    <mat-paginator [length]="resultsLength" [pageSize]="20" [pageSizeOptions]="[10, 20, 50]"></mat-paginator>
    <div class="actions">
      <button *ngIf="userService.userDetails.isTillstandshandlaggare()" routerLink="new" mat-raised-button color="primary">Lägg till händelse</button>
    </div>
  `,
  styles: [`
    table {
      margin-bottom: 1rem;
    }
  `]
})
export class HandelserComponent implements AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  displayedColumns: string[] = ["benamning", "datum", "foretag", "beskrivning"];
  resultsLength = 0;
  isLoadingResults = true;
  matobjektId: number;
  data: Handelse[];

  constructor(private route: ActivatedRoute, private matobjektService: MatobjektService,
              public userService: UserService) { }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          this.matobjektId = +this.route.parent.snapshot.paramMap.get("id");
          return this.matobjektService.getHandelser(this.matobjektId,
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

  getBildLink(bildId: number): string {
    return "/api/bifogadfil/" + bildId + "/data";
  }

  formatDatum(datum: string): string {
    return datum.replace("T", " ").substring(0, datum.lastIndexOf(":"));
  }

}
