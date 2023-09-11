import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import {merge, of} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {ListMatobjekt, MatobjektService} from "../services/matobjekt.service";
import {MatobjektTyp} from "../matobjekt/matobjekt-typ";
import {DialogService} from "../../../../lib/dialogs/dialog.service";

@Component({
  selector: "mdb-edit-matobjektgrupp-matobjekt",
  template: `
    <div class="main-content" [ngStyle]="{'display': !error ? 'grid': 'none'}">
      <div class="matobjekt-container">
        <table mat-table [dataSource]="matobjekt" matSort matSortActive="namn" matSortDisableClear matSortDirection="asc"
               [hidden]="!matobjekt.length">
          <ng-container matColumnDef="namn">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Namn</th>
            <td mat-cell *matCellDef="let m"><a routerLink="/matobjekt/{{m.id}}">{{m.namn}}</a></td>
          </ng-container>
          <ng-container matColumnDef="typ">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Typ</th>
            <td mat-cell *matCellDef="let m">{{getMatobjektTypNamn(m.typ)}}</td>
          </ng-container>
          <ng-container matColumnDef="fastighet">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Fastighet</th>
            <td mat-cell *matCellDef="let m">{{m.fastighet}}</td>
          </ng-container>
          <ng-container matColumnDef="lage">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Läge</th>
            <td mat-cell *matCellDef="let m">{{m.lage}}</td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="columns"></tr>
          <tr mat-row *matRowDef="let m; columns: columns;"></tr>
        </table>
        <mat-paginator [length]="resultsLength" [pageSize]="10"
                       [pageSizeOptions]="[10, 25, 50]" [class.invisible]="!matobjekt.length"></mat-paginator>
      </div>
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

    mat-form-field {
      width: 100%;
    }

    .matobjekt-container {
      display: grid;
      grid-gap: 0;
    }

    .search-container {
      display: grid;
      grid-gap: 1rem;
      position: relative;
      width: 100%;
      border: 1px lightgrey dashed;
      border-radius: 5px;
      padding: 1rem;
      padding-bottom: 0;
      background: whitesmoke;
    }

    .mat-column {
      width: 15%;
    }

    .mat-column-lage {
      width: 55%;
    }
  `]
})
export class EditMatobjektgruppMatobjektComponent implements OnInit, AfterViewInit {

  @Input() ids: number[];

  MatobjektTyp = MatobjektTyp;

  columns: string[] = ["namn", "typ", "fastighet", "lage"];
  resultsLength = 0;
  matobjekt: ListMatobjekt[] = [];

  RestError = RestError;
  error: RestError = null;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private matobjektService: MatobjektService, private dialogService: DialogService) { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          if (this.ids.length > 0) {
            return this.matobjektService.getPage(
              this.paginator.pageIndex, this.paginator.pageSize, this.sort.active, this.sort.direction,
              {matobjektIds: this.ids});
          } else {
            return of({totalElements: 0, content: []});
          }
        }),
        map(data => {
          this.resultsLength = data.totalElements;
          return data.content;
        }),
        catchError(() => {
          this.error = RestError.GET_MATOBJEKT;
          return of([]);
        })
      ).subscribe(data => this.matobjekt = data);
  }

  getMatobjektTypNamn(typ: number) {
    return Object.values(MatobjektTyp)[typ];
  }
}

export enum RestError {
  GET_MATOBJEKT = "Misslyckades hämta listan med mätobjekt. Ladda om sidan för att försöka på nytt.",
}
