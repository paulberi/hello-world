import {Component, OnInit, TemplateRef, ViewChild} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {UntypedFormControl, UntypedFormGroup, Validators} from "@angular/forms";
import {Location} from "@angular/common";
import {ConfirmationDialogModel} from "../../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import {DialogService} from "../../../../../lib/dialogs/dialog.service";
import {Kartlager, Kartlagerfil, KartlagerService} from "../../services/kartlager.service";
import {prettifyISODateString} from "../../common/date-utils";
import {createLegendUrl} from "../../../../../lib/map-core/style-utils";
import {userDefinedLayerStyleMap} from "../../common/components/styles";
import {finalize} from "rxjs/operators";
import {Observable} from "rxjs";
import {UNSAVED_CHANGES} from "../../services/can-deactivate-guard.service";
import {FormGroupUnsaved} from "../../common/form-group-unsaved";
import {HttpDownloadService} from "../../../../../lib/http/http-download.service";
import { InformationDialogModel } from "../../../../../lib/dialogs/information-dialog/information-dialog.component";
import { HttpErrorResponse, HttpStatusCode } from "@angular/common/http";

@Component({
  selector: "mdb-edit-kartlager",
  template: `
    <div class="main-content">
      <h3>Redigera kartlager</h3>
      <form *ngIf="form" [formGroup]="form">
        <mat-form-field>
          <input matInput placeholder="Namn" formControlName="namn" required autocomplete="off">
          <mat-error>Du måste ange ett namn</mat-error>
        </mat-form-field>

        <mat-form-field>
          <input matInput placeholder="Grupp" formControlName="grupp" autocomplete="off">
        </mat-form-field>

        <mat-form-field>
                  <textarea matInput mat-autosize="true" placeholder="Beskrivning"
                            formControlName="beskrivning" maxlength="500"></textarea>
        </mat-form-field>

        <mat-checkbox formControlName="visa">Visa per default</mat-checkbox>

        <mat-table [dataSource]="form.controls.kartlagerfiler.value">
          <ng-container matColumnDef="filnamn">
            <mat-header-cell *matHeaderCellDef>Filnamn</mat-header-cell>
            <mat-cell *matCellDef="let row">
              <span *ngIf="row.id != null">{{row.filnamn}}</span>
              <ng-container *ngIf="row.id == null">{{row.filnamn}}</ng-container>
            </mat-cell>
          </ng-container>
          <ng-container matColumnDef="stil">
            <mat-header-cell *matHeaderCellDef>Stil</mat-header-cell>
            <mat-cell *matCellDef="let row">
                <select (change)="setStyle(row, $event)">
                  <option *ngFor="let stil of stilar" [selected]="row.stil == stil" >{{stil}}</option>
                </select>
                <img class="legend-preview" [src]="getLegendUrl(row.stil)">
            </mat-cell>
          </ng-container>
          <ng-container matColumnDef="skapadDatum">
            <mat-header-cell *matHeaderCellDef>Skapad</mat-header-cell>
            <mat-cell *matCellDef="let row">{{getDateString(row.skapadDatum)}}</mat-cell>
          </ng-container>
          <ng-container matColumnDef="download" class="mat-column-download">
            <mat-header-cell *matHeaderCellDef></mat-header-cell>
            <mat-cell *matCellDef="let row">
              <button type="button" mat-stroked-button color="primary" (click)="onDownloadFile(row)">
                <span>Ladda ner</span>
              </button>
            </mat-cell>
          </ng-container>
          <ng-container matColumnDef="remove">
            <mat-header-cell *matHeaderCellDef></mat-header-cell>
            <mat-cell *matCellDef="let row">
              <button mat-icon-button (click)="onRemoveFile(row)">
                <mat-icon>remove_circle</mat-icon>
              </button>
            </mat-cell>
          </ng-container>

          <mat-header-row *matHeaderRowDef="displayedColumns"></mat-header-row>
          <mat-row *matRowDef="let row; columns: displayedColumns;"></mat-row>
        </mat-table>
        <div>
          <button type="button" mat-raised-button color="primary" (click)="selectFile()">Lägg till fil</button>
        </div>

        <div class="actions">
          <button type="button" (click)="goBack()" mat-stroked-button color="primary">Tillbaka</button>
          <mdb-save-button (clicked)="onSave(form)" [disabled]="!form.dirty || !form.valid" [saving]="saving" label="Spara"></mdb-save-button>
          <button mat-raised-button color="warn" (click)="onDelete()" [disabled]="currentId == 'new'">Ta bort</button>
        </div>
      </form>
    </div>

    <input id="dokumentFileInput" hidden type="file" multiple (change)="onFileSelected($event)" name="file">
    
    <ng-template #usedLayerError>
      <p>Kartlagret kan inte tas bort eftersom det används</p>
    </ng-template>
    <ng-template #unknownError>
      <p>Okänt fel</p>
    </ng-template>
  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
      padding: 0.5rem;
    }

    form {
      display: grid;
      grid-gap: 0.5rem;
    }

    .mat-column-download {
      justify-content: flex-end;
    }

    .mat-column-remove {
      flex: 0.2;
      justify-content: flex-end;
    }

    .actions {
      display: grid;
      grid-gap: 0.5rem;
      margin-top: 1rem;
      margin-bottom: 1rem;
    }

    @media only screen and (min-width: 576px) {
      .actions {
        grid-template-columns: auto auto auto;
        justify-content: left;
        grid-gap: 1rem;
      }
    }

    .legend-preview {
      margin-left: 10px;
    }
  `]
})
export class EditKartlagerComponent implements OnInit {
  currentId: string;
  form: FormGroupUnsaved;
  displayedColumns = ["filnamn", "stil", "skapadDatum", "download", "remove"];
  stilar: string[] = [];

  saving = false;

  @ViewChild("usedLayerError") usedLayerError: TemplateRef<any>;
  @ViewChild("unknownError") unknownError: TemplateRef<any>;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private location: Location,
              private dialogService: DialogService,
              private service: KartlagerService,
              private httpDownloadService: HttpDownloadService) {
  }

  ngOnInit() {
    this.service.getStyles().subscribe(stilar => this.stilar = stilar);

    this.route.paramMap.subscribe(params => {
      this.currentId = params.get("id");
      if (this.currentId === "new") {
        this.form = this.createForm(this.emptyForm());
      } else {
        this.service.get(+this.currentId).subscribe(kartlager => {
          this.form = this.createForm(kartlager);
        });
      }
    });
  }

  canDeactivate(): Observable<boolean> | boolean {
    if (this.form.isChanged()) {
      return confirm(UNSAVED_CHANGES);
    } else {
      return true;
    }
  }

  private emptyForm(): Partial<Kartlager> {
    return {
      namn: "",
      grupp: "",
      beskrivning: "",
      visa: true,
      kartlagerfiler: []
    };
  }

  private createForm(data: Partial<Kartlager>) {
    return new FormGroupUnsaved({
      namn: new UntypedFormControl(data.namn, Validators.required),
      grupp: new UntypedFormControl(data.grupp),
      beskrivning: new UntypedFormControl(data.beskrivning),
      visa: new UntypedFormControl(data.visa),
      kartlagerfiler: new UntypedFormControl(data.kartlagerfiler)
    });
  }

  goBack() {
    this.location.back();
  }

  onSave(form: UntypedFormGroup) {
    this.saving = true;
    const value = form.value;
    if (this.currentId === "new") {
      this.service.post(value).pipe(finalize(() => this.saving = false)).subscribe(result => {
        this.form.markAsPristine();
        this.router.navigate(["../", result.id], {relativeTo: this.route, replaceUrl: true});
      });
    } else {
      this.service.put(+this.currentId, value).pipe(finalize(() => this.saving = false)).subscribe(result => {
        this.form.markAsPristine();
        this.form = this.createForm(result);
      });
    }
  }

  onDelete() {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Ta bort",
        "<p>Är du säker på att du vill ta bort kartlagret?<p>Operationen går inte att ångra.",
        "Avbryt",
        "Ta bort"),
      (confirmed) => {
        if (confirmed) {
          this.service.delete(+this.currentId).subscribe(() => {
            this.form.markAsPristine();
            this.goBack();
          }, (e: HttpErrorResponse) => {
            if (e.status === HttpStatusCode.MethodNotAllowed) {
              this.dialogService.showInformationDialog(
                new InformationDialogModel(
                  "Gick inte att ta bort kartlager",
                  this.usedLayerError,
                )
              );
            } else {
              this.dialogService.showInformationDialog(
                new InformationDialogModel(
                  "Gick inte att ta bort kartlager",
                  this.unknownError,
                )
              );
            }
          });
        }
      });
  }

  selectFile() {
    document.getElementById("dokumentFileInput").click();
  }

  onFileSelected(event) {
    const files = event.target.files;
    for (const file of files) {
      file.text().then(s => {
        const oldValue: Kartlagerfil[] = this.form.controls.kartlagerfiler.value;
        const newValue = oldValue.concat([{id: null, filnamn: file.name, fil: s, stil: this.stilar[0]}]);
        this.form.controls.kartlagerfiler.setValue(newValue);
        this.form.markAsDirty();
      });
    }
  }

  onDownloadFile(file: Kartlagerfil) {
    this.getKartlagerFilData(file.id.toString());
  }

  onRemoveFile(file: Kartlagerfil) {
    this.form.controls.kartlagerfiler.setValue(this.form.controls.kartlagerfiler.value.filter(f => f !== file));
    this.form.markAsDirty();
  }

  setStyle(row: Kartlagerfil, event) {
    row.stil = event.target.value;
    this.form.markAsDirty();
  }

  getDateString(date) {
    return prettifyISODateString(date);
  }

  getLegendUrl(stil: string) {
    return createLegendUrl(userDefinedLayerStyleMap.get(stil), [20, 20]);
  }

  getKartlagerFilData(id: string) {
    this.httpDownloadService.httpDownload("/api/kartlager/filer/" + id);
  }
}
