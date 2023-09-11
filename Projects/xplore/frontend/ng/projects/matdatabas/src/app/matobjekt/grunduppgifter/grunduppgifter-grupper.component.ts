import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from "@angular/core";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import {ListMatobjektgrupp, MatobjektgruppService} from "../../services/matobjektgrupp.service";
import {MdbDialogService} from "../../services/mdb-dialog.service";

@Component({
  selector: "mdb-grunduppgifter-grupper",
  template: `
    <div class="main-content" [ngStyle]="{'display': !error ? 'grid': 'none'}">
      <table mat-table [dataSource]="dataSource" matSort matSortActive="namn" matSortDisableClear
             matSortDirection="asc" [hidden]="!matobjektgrupper.length">
        <ng-container matColumnDef="kategori">
          <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Kategori</th>
          <td mat-cell *matCellDef="let mg">{{mapKategori(mg.kategori)}}</td>
        </ng-container>
        <ng-container matColumnDef="namn">
          <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Grupp</th>
          <td mat-cell *matCellDef="let mg">
            <a routerLink="/matobjektgrupp/{{mg.id}}">{{mg.namn}}</a>
          </td>
        </ng-container>
        <ng-container matColumnDef="remove">
          <th mat-header-cell *matHeaderCellDef></th>
          <td mat-cell *matCellDef="let mg">
            <button *ngIf="!readonly" mat-icon-button (click)="onRemoveMatobjektgrupp(mg.id)">
              <mat-icon id="close-button-icon">remove_circle</mat-icon>
            </button>
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="columns"></tr>
        <tr mat-row *matRowDef="let row; columns: columns;"></tr>
      </table>
      <button *ngIf="!readonly" type="button" (click)="onLaggTillMatobjektgrupp()"
              mat-raised-button color="primary">Lägg till i grupp</button>
    </div>
    <p *ngIf="error" class="rest-error">{{error}}</p>
  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
    }

    button {
      width: fit-content;
    }

    .mat-column-kategori {
      width: 40%;
    }

    .mat-column-grupp {
      width: 40%;
    }

    .mat-column-remove {
      width: 20%;
      text-align: right;
    }
  `]
})
export class GrunduppgifterGrupperComponent implements OnInit {
  @Input() grupper: number[];
  @Input() readonly = false;
  @Output() updated = new EventEmitter<number[]>();

  columns: string[] = ["kategori", "namn", "remove"];
  dataSource: MatTableDataSource<ListMatobjektgrupp>;

  matobjektgrupper: ListMatobjektgrupp[] = [];
  allaMatobjektgrupper: ListMatobjektgrupp[] = [];
  data: ListMatobjektgrupp[] = [];

  RestError = RestError;
  error: RestError = null;

  @ViewChild(MatSort) sort: MatSort;

  constructor(private matobjektgruppService: MatobjektgruppService,
              private popupService: MdbDialogService) { }

  isInvisible() {
    return this.error === RestError.GET_GRUPPER;
  }

  ngOnInit() {
    this.matobjektgruppService.getAll().subscribe(
      allaMatobjektgrupper => {
        this.allaMatobjektgrupper = allaMatobjektgrupper;
        this.matobjektgrupper = allaMatobjektgrupper.filter(mg => this.grupper.some(id => id === mg.id));
        this.updateDataSource();
      },
      () => this.error = RestError.GET_GRUPPER
    );
  }

  updateDataSource() {
    this.dataSource = new MatTableDataSource(this.matobjektgrupper);
    this.dataSource.sort = this.sort;
    this.dataSource.sortingDataAccessor = (data, sortHeaderId) => {
      if (sortHeaderId === "kategori") {
        return this.mapKategori(data[sortHeaderId]).toLocaleLowerCase();
      }
      return data[sortHeaderId].toLocaleLowerCase();
    };
  }

  onLaggTillMatobjektgrupp() {
    this.popupService.selectMatobjektgrupp().subscribe(matobjektgrupp => {
      if (matobjektgrupp) {
        let exists = false;

        for (const matobjektgruppInArray of this.matobjektgrupper) {
          if (matobjektgruppInArray.id === matobjektgrupp.id) {
            exists = true;
          }
        }

        if (!exists) {
          this.matobjektgrupper.push(matobjektgrupp);
          this.updateDataSource();
          this.updated.emit(this.matobjektgrupper.map(mg => mg.id));
        }
      }
    });
  }

  onRemoveMatobjektgrupp(id: number) {
    this.matobjektgrupper = this.matobjektgrupper.filter(mg => mg.id !== id);
    this.updateDataSource();
    this.updated.emit(this.matobjektgrupper.map(mg => mg.id));
  }

  mapKategori(id: number) {
    return Object.values(Kategori)[id];
  }
}

export enum RestError {
  GET_GRUPPER = "Misslyckades hämta information om mätobjektetgrupperna. Ladda om sidan för att försöka på nytt.",
}

export enum Kategori {
  OVRIGT = "Övrigt",
  GRUNDVATTENMAGASIN = "Grundvattenmagasin",
  AVRINNINGSOMRADE = "Avrinningsområde",
  KVARTER = "Kvarter",
  DUBBGRUPP = "Dubbgrupp",
  OMRADE = "Område"
}
