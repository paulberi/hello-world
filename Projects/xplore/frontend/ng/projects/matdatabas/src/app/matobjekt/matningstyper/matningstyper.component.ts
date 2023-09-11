import {Component, OnInit, ViewChild} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Matningstyp, MatobjektService} from "../../services/matobjekt.service";
import {Observable} from "rxjs";
import {formatNumber} from "@angular/common";
import {UserService} from "../../services/user.service";
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: "mdb-matningstyper",
  template: `
    <table mat-table [dataSource]="dataSource" matSort matSortActive="typ" matSortDisableClear
           matSortDirection="asc">
      <ng-container matColumnDef="typ">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Mätningstyp</th>
        <td mat-cell *matCellDef="let row"><a routerLink="{{row.id}}">{{row.typ}}{{row.storhet ? ': '+row.storhet:''}}</a></td>
      </ng-container>
      <ng-container matColumnDef="senasteVarde">
        <th mat-header-cell *matHeaderCellDef>Senaste värde</th>
        <td mat-cell *matCellDef="let row">{{getSenasteVarde(row)}}</td>
      </ng-container>
      <ng-container matColumnDef="senasteVardeDatum">
        <th mat-header-cell *matHeaderCellDef>Datum</th>
        <td mat-cell *matCellDef="let row">{{row.senasteVardeDatum ? (row.senasteVardeDatum | date:'yyyy-MM-dd') : "N/A"}}</td>
      </ng-container>
      <ng-container matColumnDef="aktiv">
        <th mat-header-cell *matHeaderCellDef>Aktiv</th>
        <td mat-cell *matCellDef="let row">{{row.aktiv ? "Ja" : "Nej"}}</td>
      </ng-container>
      <ng-container matColumnDef="ejGranskade">
        <th mat-header-cell *matHeaderCellDef>Ej granskade</th>
        <td mat-cell *matCellDef="let row">{{row.ejGranskade}}</td>
      </ng-container>
      <ng-container matColumnDef="lankar">
          <th mat-header-cell *matHeaderCellDef></th>
          <td mat-cell *matCellDef="let row">
            <a [routerLink]="['/granskning-graf', {matningstypIds: row.id}]">Graf</a> |
            <a routerLink="{{row.id}}/matningar">Tabell</a> |
            <a routerLink="{{row.id}}/gransvarden">Gränsvärden</a>
          </td>
      </ng-container>
      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
    <div class="actions">
      <button *ngIf="userService.userDetails.isTillstandshandlaggare()" routerLink="select"
              mat-raised-button color="primary">Lägg till mätningstyp</button>
    </div>
  `,
  styles: [`
    table {
      margin-bottom: 1rem;
    }

    .mat-column-typ {
      width: 30%;
    }

    .mat-column-aktiv {
      width: 10%;
    }
    .mat-column-ejGranskade {
      width: 10%;
    }
    .mat-column-lankar {
      width: 20%;
    }
  `]
})
export class MatningstyperComponent implements OnInit {
  displayedColumns: string[];
  resultsLength = 0;
  data: Matningstyp[] = [];
  matningstyper: Matningstyp[];
  dataSource: MatTableDataSource<Matningstyp>;

  @ViewChild(MatSort) sort: MatSort;

  constructor(private route: ActivatedRoute, private matobjektService: MatobjektService,
              public userService: UserService) { }

  ngOnInit() {
    if (this.userService.userDetails.isMatrapportor()) {
      this.displayedColumns = ["typ", "senasteVarde", "senasteVardeDatum", "aktiv", "ejGranskade", "lankar"];
    } else {
      this.displayedColumns = ["typ", "senasteVarde", "senasteVardeDatum", "aktiv", "lankar"];
    }

    const matobjektId = +this.route.parent.snapshot.paramMap.get("id");
    this.matobjektService.getMatningstyper(matobjektId).subscribe(
      matningstyper => {
        this.matningstyper = matningstyper;
        this.updateDataSource();
      });
  }

  updateDataSource() {
    this.dataSource = new MatTableDataSource(this.matningstyper);
    this.dataSource.sort = this.sort;
    this.dataSource.sortingDataAccessor = (data, sortHeaderId) => {
      return data[sortHeaderId].toLocaleLowerCase();
    };
  }

  getSenasteVarde(matningstyp: Matningstyp): string {
    const senasteVarde = matningstyp.senasteVarde ? matningstyp.senasteVarde  : "N/A";
    const value = Number(senasteVarde);
    return isNaN(value) ? senasteVarde : this.formatValue(value) + " " + matningstyp.enhet;
  }

  formatValue(value): string {
    return formatNumber(value, "sv-SE");
  }
}
