import {AfterViewInit, Component, Input, ViewChild} from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatTableDataSource } from "@angular/material/table";
import {
  MatrundaMatningstyp,
  MatrundaService
} from "../../services/matrunda.service";
import {createAktivHeaderTooltip, createAktivTooltip} from "./util/tooltip.util";
import {MatningstypMatobjekt, MatningstypMatobjektFilter, MatobjektService} from "../../services/matobjekt.service";

@Component({
  selector: "mdb-edit-matrunda-view-matningstyper",
  template: `
    <div class="main-content" [ngStyle]="{'display': !error ? 'grid': 'none'}">
      <table mat-table [dataSource]="dataSource" [hidden]="!data.length">
        <ng-container matColumnDef="matobjektNamn">
          <th mat-header-cell *matHeaderCellDef>Namn</th>
          <td mat-cell *matCellDef="let mm"><a routerLink="/matobjekt/{{mm.matobjektId}}">{{mm.matobjektNamn}}</a></td>
        </ng-container>
        <ng-container matColumnDef="matningstypNamn">
          <th mat-header-cell *matHeaderCellDef>Mätningstyp</th>
          <td mat-cell *matCellDef="let mm">{{mm.matningstypNamn}} {{mm.matningstypStorhet ? '('+mm.matningstypStorhet+')':''}}</td>
        </ng-container>
        <ng-container matColumnDef="matobjektFastighet">
          <th mat-header-cell *matHeaderCellDef>Fastighet</th>
          <td mat-cell *matCellDef="let mm">{{mm.matobjektFastighet}}</td>
        </ng-container>
        <ng-container matColumnDef="matobjektLage">
          <th mat-header-cell *matHeaderCellDef>Läge</th>
          <td mat-cell *matCellDef="let mm">{{mm.matobjektLage}}</td>
        </ng-container>
        <ng-container matColumnDef="aktiv">
          <th mat-header-cell *matHeaderCellDef matTooltip="{{getAktivHeaderTooltip()}}"
              matTooltipShowDelay="1000">Aktiv</th>
          <td mat-cell *matCellDef="let mm" matTooltip="{{getAktivTooltip(mm)}}" matTooltipShowDelay="1000">
            {{mm.aktiv ? "Ja" : "Nej"}}
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="columns"></tr>
        <tr mat-row *matRowDef="let m; columns: columns;"></tr>
      </table>
      <mat-paginator [pageSize]="10"
                     [pageSizeOptions]="[10, 25, 50]" [class.invisible]="!data.length"></mat-paginator>
    </div>
    <p *ngIf="error" class="rest-error">{{error}}</p>
  `,
  styles: [`
    .invisible {
      display: none;
    }

    .main-content {
      display: grid;
      grid-gap: 1rem;
    }

    .mat-column-matobjektNamn, .mat-column-matningstypNamn, .mat-column-matobjektFastighet {
      width: 15%;
    }

    .mat-column-aktiv {
      width: 10%;
      text-align: right;
    }
  `]
})
export class EditMatrundaViewMatningstyperComponent implements AfterViewInit {

  @Input() matningstyper: MatrundaMatningstyp[];

  columns: string[] = ["matobjektNamn", "matningstypNamn", "matobjektFastighet", "matobjektLage", "aktiv"];
  dataSource: MatTableDataSource<MatningstypMatobjekt>;

  resultsLength = 0;
  data: MatningstypMatobjekt[] = [];

  RestError = RestError;
  error: RestError = null;

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  constructor(private matobjektService: MatobjektService) { }

  ngAfterViewInit() {
    this.matningstyper = this.matningstyper.sort((first, second) => first.ordning - second.ordning);

    this.matobjektService.getMatningstypMatobjektUnpaged(this.getFilter())
      .subscribe(
        data => {
          this.data = this.sortByOrdning(data.content);
          this.dataSource = new MatTableDataSource(this.data);
          this.dataSource.paginator = this.paginator;
        },
        () => {
          this.error = RestError.GET_MATNINGSTYPER;
        }
      );
  }

  sortByOrdning(data: MatningstypMatobjekt[]): MatningstypMatobjekt[] {
    const sorted: MatningstypMatobjekt[] = [];
    this.matningstyper.forEach(mm => {
      sorted.push(data.find(d => d.matningstypId === mm.matningstypId));
    });
    return sorted;
  }

  getFilter(): MatningstypMatobjektFilter {
    return {includeIds: this.matningstyper.length ? this.matningstyper.map(mt => mt.matningstypId) : null};
  }

  getAktivHeaderTooltip(): string {
    return createAktivHeaderTooltip();
  }

  getAktivTooltip(mm: MatningstypMatobjekt): string {
    return createAktivTooltip(mm);
  }
}

export enum RestError {
  GET_MATNINGSTYPER = "Misslyckades hämta listan med mätningstyper. Ladda om sidan för att försöka på nytt.",
}
