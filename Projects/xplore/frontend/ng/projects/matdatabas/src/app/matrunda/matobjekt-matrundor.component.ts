import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from "@angular/core";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import {ListMatrunda, Matrunda, MatrundaService} from "../services/matrunda.service";
import {MdbDialogService} from "../services/mdb-dialog.service";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {Matningstyp, MatobjektService, MatrundaMap} from "../services/matobjekt.service";
import {FormGroup} from "@angular/forms";
import {ConfirmationDialogModel} from "../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import {DialogService} from "../../../../lib/dialogs/dialog.service";
import {UserService} from "../services/user.service";

interface MatningstypMatrunda {
  matningstypId: number;
  matningstyp: string;
  matrundaId: number;
  matrunda: string;
}

@Component({
  selector: "mdb-matobjekt-matrundor",
  template: `
    <div class="main-content" [ngStyle]="{'display': !error ? 'grid': 'none'}">
      <table mat-table [dataSource]="dataSource" matSort matSortActive="matningstyp" matSortDisableClear
             matSortDirection="asc" [hidden]="!matningstypMatrundor.length">
        <ng-container matColumnDef="matningstyp">
          <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Mätningstyp</th>
          <td mat-cell *matCellDef="let item">{{item.matningstyp}}</td>
        </ng-container>
        <ng-container matColumnDef="matrunda">
          <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Mätrunda</th>
          <td mat-cell *matCellDef="let item"><a routerLink="/matrunda/{{item.matrundaId}}">{{item.matrunda}}</a></td>
        </ng-container>
        <ng-container matColumnDef="remove">
          <th mat-header-cell *matHeaderCellDef></th>
          <td mat-cell *matCellDef="let item">
            <button *ngIf="!readonly && userService.userDetails.isTillstandshandlaggare()" mat-icon-button
                    (click)="onRemoveMatrunda(item.matrundaId, item.matningstypId)">
              <mat-icon id="close-button-icon">remove_circle</mat-icon>
            </button>
          </td>
        </ng-container>
        <tr mat-header-row *matHeaderRowDef="columns"></tr>
        <tr mat-row *matRowDef="let row; columns: columns;"></tr>
      </table>
      <div *ngIf="userService.userDetails.isTillstandshandlaggare()" class="actions">
        <button *ngIf="!readonly" routerLink="select"
                mat-raised-button color="primary">Lägg till i mätrunda
        </button>
      </div>
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
  `]
})
export class MatobjektMatrundorComponent implements OnInit {
  @Input() matrundor: MatrundaMap[] = [];
  @Input() readonly = false;
  @Output() updated = new EventEmitter<number[]>();

  columns: string[] = ["matningstyp", "matrunda", "remove"];
  dataSource: MatTableDataSource<MatningstypMatrunda>;

  allaMatrundor: ListMatrunda[] = [];
  matningstyper: Matningstyp[];
  matobjektId: number;

  matningstypMatrundor: MatningstypMatrunda[] = [];

  RestError = RestError;
  error: RestError = null;

  @ViewChild(MatSort) sort: MatSort;

  constructor(private route: ActivatedRoute,
              private matobjektService: MatobjektService,
              private matrundaService: MatrundaService,
              private dialogService: DialogService,
              public userService: UserService) { }

  isInvisible() {
    return this.error === RestError.GET_MATRUNDOR;
  }

  ngOnInit() {
    const matobjektId = +this.route.parent.snapshot.paramMap.get("id");
    this.matobjektId = matobjektId;

    this.matobjektService.get(matobjektId).subscribe(matobjekt => {
        this.matrundor = matobjekt.matrundor;

        this.matobjektService.getMatningstyper(matobjektId).subscribe(
          matningstyper => {
            this.matningstyper = matningstyper;
            this.refreshTable();
          },
          () => this.error = RestError.GET_MATOBJEKT
        );

        this.matrundaService.getAll().subscribe(
          allaMatrundor => {
            this.allaMatrundor = allaMatrundor;
            this.refreshTable();
          },
          () => this.error = RestError.GET_MATRUNDOR
        );

      },
      () => this.error = RestError.GET_MATOBJEKT
    );

  }

  refreshTable() {
    this.matningstypMatrundor = [];

    this.matrundor.forEach(item => {
      if (this.allaMatrundor === undefined || this.matningstyper === undefined) {
        return;
      }

      const matrunda = this.allaMatrundor.find(e => e.id === item.matrundaId);
      const matningstyp = this.matningstyper.find(e => e.id === item.matningstypId);

      if (matrunda === undefined || matningstyp === undefined) {
        return;
      }

      const row: MatningstypMatrunda = {
        matrundaId: matrunda.id,
        matrunda: matrunda.namn,
        matningstypId: matningstyp.id,
        matningstyp: matningstyp.typ
      };

      this.matningstypMatrundor.push(row);
    });

    this.updateDataSource();
  }

  updateDataSource() {
    this.dataSource = new MatTableDataSource(this.matningstypMatrundor);

    this.dataSource.sort = this.sort;
    this.dataSource.sortingDataAccessor = (data, sortHeaderId) => {
      return data[sortHeaderId].toLocaleLowerCase();
    };
  }

  onRemoveMatrunda(matrundaId: number, matningstypId: number) {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Ta bort",
        "<p>Är du säker på att du vill ta bort mätningstypen från mätrundan?</p>",
        "Avbryt",
        "Ta bort"),
      this.handleConfirmationDialogTaBort.bind(this, matrundaId, matningstypId)
    );
  }

  handleConfirmationDialogTaBort(matrundaId: number, matningstypId: number, dialogResult: boolean) {
    if (dialogResult) {
      this.matrundaService.get(matrundaId).subscribe(
        matrunda => {
          const index = matrunda.matningstyper.findIndex(r => r.matningstypId === matningstypId);

          if (index !== -1) {
            matrunda.matningstyper.splice(index, 1);

            this.matrundaService.put(matrundaId, matrunda).subscribe(
              () => this.handleSaveSuccess(matrunda),
              () => this.handleSaveFailure(RestError.PUT_MATRUNDA)
            );
          }
        },
        () => this.error = RestError.GET_MATRUNDA
      );

    }
  }

  handleSaveSuccess(matrunda?: Matrunda) {
    this.matobjektService.get(this.matobjektId).subscribe(matobjekt => {
        this.matrundor = matobjekt.matrundor;
        this.refreshTable();
      },
      () => this.error = RestError.GET_MATOBJEKT
    );
  }

  handleSaveFailure(error: RestError) {
    console.log("Kunde inte spara mätrunda, error: " + RestError);
    this.error = error;
  }
}

export enum RestError {
  GET_MATOBJEKT = "Misslyckades hämta grunduppgifter för mätobjektet. Ladda om sidan för att försöka på nytt.",
  GET_MATRUNDOR = "Misslyckades hämta information om mätrundorna. Ladda om sidan för att försöka på nytt.",
  GET_MATRUNDA = "Misslyckades hämta information om mätrundan. Ladda om sidan för att försöka på nytt.",
  PUT_MATRUNDA = "Misslyckades uppdatera mätrundan. Försök igen om en liten stund"
}
