import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from "@angular/core";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import {BifogadFil, BifogadfilService} from "../../services/bifogadfil.service";
import {DialogService} from "../../../../../lib/dialogs/dialog.service";
import {ConfirmationDialogModel} from "../../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import { saveAs } from "file-saver";


@Component({
  selector: "mdb-grunduppgifter-dokument",
  template: `
    <div class="main-content" [ngStyle]="{'display': error !== RestError.GET_DOKUMENT ? 'grid': 'none'}">
      <table mat-table [dataSource]="dataSource" matSort matSortActive="skapad_datum" matSortDisableClear
             matSortDirection="asc" [hidden]="!dokument.length">
        <ng-container matColumnDef="filnamn">
          <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Namn</th>
          <td mat-cell *matCellDef="let d">
            <a href="javascript:void(0)" (click)="downloadFile(d.link)">{{d.filnamn}}</a>
          </td>
        </ng-container>
        <ng-container matColumnDef="skapad_datum">
          <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Datum</th>
          <td mat-cell *matCellDef="let d">{{formatTimestamp(d.skapad_datum)}}</td>
        </ng-container>
        <ng-container matColumnDef="remove">
          <th mat-header-cell *matHeaderCellDef></th>
          <td mat-cell *matCellDef="let d">
            <button mat-icon-button *ngIf="!readonly" (click)="onTaBort(d.id)">
              <mat-icon>remove_circle</mat-icon>
            </button>
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="columns"></tr>
        <tr mat-row *matRowDef="let row; columns: columns;"></tr>
      </table>
      <div class="actions">
        <button *ngIf="!readonly" type="button" (click)="onLaggTill()" mat-raised-button color="primary" [disabled]="showDonut">
          <span *ngIf="!showDonut">Lägg till dokument</span>
          <mat-spinner *ngIf="showDonut" [diameter]="20" color="accent"></mat-spinner>
        </button>
      </div>
    </div>
    <p *ngIf="error" class="rest-error">{{error}}</p>
    <input id="dokumentFileInput" hidden type="file" (change)="onSelected($event)" name="file">
  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
    }

    .mat-column-namn {
      width: 40%;
    }

    .mat-column-datum {
      width: 40%;
    }

    .mat-column-remove {
      width: 20%;
      text-align: right;
    }

    .actions {
      display: grid;
      grid-gap: 1rem;
      grid-template-columns: auto 1fr;
    }

    .actions button {
      min-width: 80px;
      min-height: 36px;
    }

    mat-spinner {
      margin: 0 auto;
    }
  `]
})
export class GrunduppgifterDokumentComponent implements OnInit {

  @Input() ids: number[];
  @Input() readonly = false;
  @Output() updated = new EventEmitter<string[]>();
  @Output() uploading = new EventEmitter<boolean>();

  columns: string[] = ["filnamn", "skapad_datum", "remove"];
  dataSource: MatTableDataSource<BifogadFil>;
  dokument: BifogadFil[] = [];

  RestError = RestError;
  error: RestError = null;

  showDonut = false;

  @ViewChild(MatSort) sort: MatSort;

  constructor(private bifogadFilService: BifogadfilService, private dialogService: DialogService,
              private httpClient: HttpClient) {
    this.uploading.subscribe(active => this.showDonut = active);
  }

  ngOnInit() {
    if (this.ids.length) {
      this.updateDokument();
    }
  }

  updateDokument() {
    this.uploading.emit(true);
    this.bifogadFilService.getList(this.ids).subscribe(
      dokument => {
        this.dokument = dokument;
        this.updateDataSource();
        this.uploading.emit(false);
      },
      error => {
        this.error = RestError.GET_DOKUMENT;
        this.uploading.emit(false);
      });
  }

  updateDataSource() {
    this.dataSource = new MatTableDataSource(this.dokument);
    this.dataSource.sort = this.sort;
    this.dataSource.sortingDataAccessor = (data, sortHeaderId) => {
      return data[sortHeaderId].toLocaleLowerCase();
    };
  }

  onLaggTill() {
    this.error = null;
    document.getElementById("dokumentFileInput").click();
  }

  onSelected(event) {
    this.uploading.emit(true);
    this.bifogadFilService.post(event.target.files[0]).subscribe(
      bf => {
        this.dokument.push(bf);
        this.updateDataSource();
        this.uploading.emit(false);
        this.updated.emit(this.dokument.map(d => d.id));
      },
      () => {
        this.error = RestError.POST_DOKUMENT;
        this.uploading.emit(false);
      });
  }

  onTaBort(id: string) {
    const filename = this.dokument.find(d => d.id === id).filnamn;
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Ta bort",
        `<p>Är du säker på att du vill ta bort dokumentet '${filename}'?<p>Operationen går inte att ångra efter att man tryckt på Spara.`,
        "Avbryt",
        "Ta bort"),
      dialogResult => this.handleConfirmationDialogTaBort(dialogResult, id));
  }

  handleConfirmationDialogTaBort(dialogResult: boolean, id: string) {
    if (dialogResult) {
      this.dokument =  this.dokument.filter(d => d.id !== id);
      this.updateDataSource();
      this.updated.emit(this.dokument.map(d => d.id));
    }
  }

  formatTimestamp(ts: string): string {
    return ts.replace("T", " ")
             .replace(/(\d\d:\d\d:\d\d).\d*/, "$1"); // strip out milliseconds
  }

  downloadFile(link: string ) {
    const httpParams = new HttpParams();

    return this.httpClient
      .get(link, {
        params: httpParams,
        responseType: "blob",
        observe: "response"
      })
      .toPromise()
      .then(response => {
        saveAs(response.body, this.getFileNameFromHttpResponse(response));
      });
  }

  private getFileNameFromHttpResponse(httpResponse: HttpResponse<Blob> ) {
    const contentDispositionHeader = httpResponse.headers.get("Content-Disposition");
    const result = contentDispositionHeader.split(";")[1].trim().split("=")[1];
    return result.replace(/"/g, "");
  }

}

export enum RestError {
  GET_DOKUMENT = "Misslyckades hämta dokumenten för mätobjektet. Ladda om sidan för att försöka på nytt.",
  POST_DOKUMENT = "Misslyckades lagra dokumentet. Försök igen om en liten stund"
}
