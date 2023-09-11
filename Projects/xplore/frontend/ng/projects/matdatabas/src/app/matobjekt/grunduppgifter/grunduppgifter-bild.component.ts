import {Component, EventEmitter, Input, Output} from "@angular/core";
import {BifogadfilService} from "../../services/bifogadfil.service";
import {DialogService} from "../../../../../lib/dialogs/dialog.service";
import {ConfirmationDialogModel} from "../../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import {HttpClient, HttpParams} from "@angular/common/http";

@Component({
  selector: "mdb-grunduppgifter-bild",
  template: `
    <div class="main-content">
      <ng-container *ngIf="id; else placeholder">
        <img (click)="showImage(getBildUrl())" class="bild mat-elevation-z8" [src]="getBildUrl() | secureLoadImage | async"/>

        <div *ngIf="!readonly" class="actions remove">
          <button mat-raised-button (click)="onLaggTill()" color="primary" [disabled]="showDonut">
            <span *ngIf="!showDonut">Välj ny bild</span>
            <mat-spinner *ngIf="showDonut" [diameter]="20" color="accent"></mat-spinner>
          </button>
          <button (click)="onRemove()" mat-raised-button color="primary" [disabled]="showDonut">Ta bort bild</button>
        </div>
      </ng-container>
      <ng-template #placeholder>
        <div class="bild-placeholder mat-elevation-z8">
          <div class="bild-placeholder-inner">
            <mat-icon>block</mat-icon>
            <span>Bild saknas</span>
          </div>
        </div>
        <div class="actions">
          <button *ngIf="!readonly" mat-raised-button (click)="onLaggTill()" color="primary" [disabled]="showDonut">
            <span *ngIf="!showDonut">Lägg till bild</span>
            <mat-spinner *ngIf="showDonut" [diameter]="20" color="accent"></mat-spinner>
          </button>
        </div>
      </ng-template>
    </div>
    <p *ngIf="error" class="rest-error">{{error}}</p>
    <input id="bildFileInput" hidden type="file" (change)="onSelected($event)" name="file" accept=".jpeg,.jpg,.png">
  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
      grid-template-rows: auto auto 1fr;
    }

    .bild {
      max-width: 100%;
      max-height: 400px;
    }

    .bild-placeholder {
      width: 100%;
      height: 400px;
      max-height: 400px;
      border-radius: 10px;
      border: 1px dashed grey;
      display: grid;
      justify-items: center;
      align-items: center;
      margin-bottom: 0.5rem;
      opacity: 0.33;
    }

    .bild-placeholder-inner {
      display: grid;
      grid-gap: 0.5rem;
      justify-items: center;
      align-items: center;
    }

    .actions {
      display: grid;
      grid-gap: 1rem;
      grid-template-columns: auto 1fr;
    }

    .remove {
      grid-template-columns: auto auto 1fr 1fr;
    }

    button {
      min-width: 80px;
      min-height: 36px;
    }

    mat-spinner {
      margin: 0 auto;
    }
  `]
})
export class GrunduppgifterBildComponent {
  @Input() id: string;
  @Input() readonly = false;
  @Output() updated = new EventEmitter<string>();
  @Output() uploading = new EventEmitter<boolean>();

  RestError = RestError;
  error: RestError = null;

  showDonut = false;

  constructor(private bifogadFilService: BifogadfilService, private dialogService: DialogService,
              private httpClient: HttpClient) {
    this.uploading.subscribe(active => this.showDonut = active);
  }

  onLaggTill() {
    this.error = null;
    document.getElementById("bildFileInput").click();
  }

  onSelected(event) {
    this.uploading.emit(true);
    this.bifogadFilService.post(event.target.files[0]).subscribe(
      bf => {
        this.id = bf.id;
        this.updated.emit(this.id);
        this.uploading.emit(false);
      },
      () => {
        this.error = RestError.POST_BILD;
        this.uploading.emit(false);
      });
  }

  onRemove() {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Ta bort",
        "<p>Är du säker på att du vill ta bort bilden?<p>Operationen går inte att ångra efter att man tryckt på Spara.",
        "Avbryt",
        "Ta bort"),
      this.handleConfirmationDialogTaBort.bind(this));
  }

  handleConfirmationDialogTaBort(dialogResult: boolean) {
    if (dialogResult) {
      this.id = null;
      this.updated.emit(null);
    }
  }

  getBildUrl(): string {
    return this.bifogadFilService.getDataLink(this.id);
  }

  showImage(bildUrl: string) {
    const httpParams = new HttpParams();
    const newWindow = window.open();

    return this.httpClient
      .get(bildUrl, {
        params: httpParams,
        responseType: "blob",
        observe: "response"
      })
      .toPromise()
      .then(response => {
        const contentType = response.headers.get("Content-Type");

        const file = new Blob([response.body], {type: "image/jpeg"});
        const fileURL = window.URL.createObjectURL(file);
        newWindow.location.href = fileURL;
      });
  }
}

export enum RestError {
  GET_BILD = "Misslyckades hämta bilden för mätobjektet. Ladda om sidan för att försöka på nytt.",
  POST_BILD = "Misslyckades lagra bilden. Försök igen om en liten stund"
}
