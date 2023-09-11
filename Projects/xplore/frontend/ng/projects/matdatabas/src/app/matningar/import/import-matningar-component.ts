import {Component, OnInit, ViewChild, AfterViewInit} from "@angular/core";
import {UntypedFormGroup, UntypedFormBuilder, FormControl, Validators} from "@angular/forms";
import {MatobjektService, ImportMatning} from "../../services/matobjekt.service";
import {UserService} from "../../services/user.service";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import {merge, Observable} from "rxjs";
import {ImportService} from "../../services/import.service";
import {Kallsystem, KallsystemService} from "../../services/kallsystem.service";
import {UNSAVED_CHANGES} from "../../services/can-deactivate-guard.service";

@Component({
  selector: "mdb-import-matningar",
  template: `
    <h2>Import av mätdata</h2>
    <h3>1. Ladda upp och kontrollera</h3>
    <form [formGroup]="formLoad">
      <mat-form-field>
        <select matNativeControl placeholder="Källsystem" required formControlName="kallsystem">
          <option></option>
          <option *ngFor="let ks of kallsystem" [ngValue]="ks">{{ks.namn}}</option>
        </select>
      </mat-form-field>
      <mat-form-field>
        <select matNativeControl placeholder="Format" required formControlName="format">
          <option *ngFor="let f of format" [value]="f">{{f}}</option>
        </select>
      </mat-form-field>
      <mat-form-field>
        <input matInput [readonly]="true" type="text" placeholder="Välj fil" required formControlName="filename"/>
        <button matSuffix mat-icon-button (click)="onSelectFile()" type="button">
          <mat-icon>more_horiz</mat-icon>
        </button>
      </mat-form-field>
      <div class="load">
        <mat-hint *ngIf="selectedKallsystemHasTips()" style="width: 600px">
            {{formLoad.controls.kallsystem.value.tips}}
        </mat-hint>
        <div>
          <mdb-save-button label="Ladda upp" (clicked)="onLoad()" [saving]="loading"
                           [disabled]="!hasFile() || !formLoad.valid"></mdb-save-button>
          <p *ngIf="loadError" class="rest-error" style="white-space: pre;">{{loadError}}</p>
        </div>
      </div>
    </form>
    <input id="fileInput" hidden type="file" (change)="onFileSelected($event)" name="file" accept=".csv">
    <table mat-table [dataSource]="viewData" matSort matSortActive="matobjekt" matSortDisableClear matSortDirection="asc">
      <ng-container matColumnDef="matobjekt">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Mätobjekt</th>
        <td mat-cell *matCellDef="let row" [ngClass]="{'import-error': hasPropertyError(row, 'matobjekt')}">
          <a *ngIf="showMatbjektLink(row)" routerLink="/matobjekt/{{row.matobjektId}}">{{row.matobjekt}}</a>
          <span *ngIf="!showMatbjektLink(row)">{{row.matobjekt}}</span>
        </td>
      </ng-container>
      <ng-container matColumnDef="matningstyp">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Mätningstyp</th>
        <td mat-cell *matCellDef="let row" [ngClass]="{'import-error': hasPropertyError(row, 'matningstyp')}">{{row.matningstyp}}</td>
      </ng-container>
      <ng-container matColumnDef="storhet">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Storhet</th>
        <td mat-cell *matCellDef="let row" [ngClass]="{'import-error': hasPropertyError(row, 'matningstyp')}">{{row.storhet}}</td>
      </ng-container>
      <ng-container matColumnDef="avlastDatum">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Datum</th>
        <td mat-cell *matCellDef="let row" [ngClass]="{'import-error': hasPropertyError(row, 'avlastDatum')}">{{row.avlastDatum}}</td>
      </ng-container>
      <ng-container matColumnDef="avlastVarde">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Uppmätt värde</th>
        <td mat-cell class="wrap-text" *matCellDef="let row">
          <ng-container *ngIf="row.avlastVarde">
            <span [ngClass]="{'import-error': hasPropertyError(row, 'inomDetektionsomrade')}">{{row.inomDetektionsomrade}}</span>
            <span [ngClass]="{'import-error': hasPropertyError(row, 'avlastVarde')}">{{row.avlastVarde}}</span>
            <span [ngClass]="{'import-error': hasPropertyError(row, 'enhetAvlast')}"> {{row.enhetAvlast}}</span>
          </ng-container>
        </td>
      </ng-container>
      <ng-container matColumnDef="beraknatVarde">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Beräknat värde</th>
        <td mat-cell class="wrap-text" *matCellDef="let row">
          <ng-container *ngIf="row.beraknatVarde">
            <span [ngClass]="{'import-error': hasPropertyError(row, 'inomDetektionsomrade')}">{{row.inomDetektionsomrade}}</span>
            <span [ngClass]="{'import-error': hasPropertyError(row, 'beraknatVarde')}">{{row.beraknatVarde}}</span>
            <span [ngClass]="{'import-error': hasPropertyError(row, 'enhetBeraknat')}"> {{row.enhetBeraknat}}</span>
          </ng-container>
        </td>
      </ng-container>
      <ng-container matColumnDef="felkod">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Felkod</th>
        <td mat-cell class="wrap-text" *matCellDef="let row" [ngClass]="{'import-error': hasPropertyError(row, 'felkod')}">
          {{row.felkod}}</td>
      </ng-container>
      <ng-container matColumnDef="kommentarAndImportFel">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Kommentar/felmeddelande</th>
        <td mat-cell class="wrap-text" *matCellDef="let row">
        <div [ngClass]="{'import-error': hasPropertyError(row, 'kommentar')}">{{row.kommentar}}</div>
        <div class="import-error" *ngFor="let importFel of row.importFel">
          {{importFel.error}}
        </div>
        </td>
      </ng-container>
      <ng-container matColumnDef="remove">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let row">
          <button mat-icon-button (click)="onRemove(row)">
          <mat-icon>remove_circle</mat-icon>
          </button>
        </td>
      </ng-container>
      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
    <mat-paginator [length]="data.length" [pageSize]="50" [pageSizeOptions]="[10, 25, 50]"></mat-paginator>
    <h3>2. Spara mätdata</h3>
    <form [formGroup]="formSave">
      <mat-form-field>
        <input matInput type="text" placeholder="Rapportör" required formControlName="rapportor"/>
      </mat-form-field>
      <mat-form-field class="status-field">
        <input matInput [readonly]="true" type="text" placeholder="Status" formControlName="status"
          [ngClass]="{'import-error': hasAnyError}"/>
      </mat-form-field>
      <div>
        <mdb-save-button  label="Spara" (clicked)="onSave()" color="primary" [disabled]="!canSave()" [saving]="saving"></mdb-save-button>
      </div>
    </form>
  `,
    styles: [`
      mat-form-field {
        margin-right: 1rem;
      }

      .status-field {
        width: 350px;
      }

      .mat-column-remove {
        width: 20%;
        text-align: right;
      }

      .import-error {
        color: red;
      }

      .load {
        display: grid;
        grid-gap: 1rem;
      }
    `]
})

export class ImportMatningarComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  displayedColumns: string[] = ["matobjekt", "matningstyp", "storhet", "avlastDatum", "avlastVarde", "beraknatVarde",
    "felkod", "kommentarAndImportFel", "remove"];
  formatMap = new Map([["Standard (CSV)", "STANDARD"], ["Instrument (CSV)", "INSTRUMENT"]]);
  format = ["Standard (CSV)", "Instrument (CSV)"];
  formLoad: UntypedFormGroup;
  formSave: UntypedFormGroup;
  file: File = null;
  data: ImportMatning[] = [];
  viewData: ImportMatning[] = [];
  isAdminOrTH: boolean;
  saving = false;
  loading = false;
  loadError: string = null;
  kallsystem: Kallsystem[] = [];
  unsavedChanges: boolean;

  constructor(
    private formBuilder: UntypedFormBuilder,
    private matobjektService: MatobjektService,
    private importService: ImportService,
    private kallsystemService: KallsystemService,
    private userService: UserService) {
      this.isAdminOrTH = this.userService.userDetails.isAdmin() || this.userService.userDetails.isTillstandshandlaggare();
  }

  ngOnInit() {
    this.formLoad = this.formBuilder.group({
      kallsystem: ["", Validators.required],
      format: [this.format[0]],
      filename: [""]
    });
    this.formSave = this.formBuilder.group({
      rapportor: [""],
      status: [""]
    });

    this.kallsystemService.getAll().subscribe(
      kallsystem => {
        this.kallsystem = kallsystem.filter( ks => ks.manuellImport === true);
      });

  }

  canDeactivate(): Observable<boolean> | boolean {
    if (this.unsavedChanges) {
      return confirm(UNSAVED_CHANGES);
    } else {
      return true;
    }
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    merge(this.sort.sortChange, this.paginator.page).subscribe(() => this.updateViewData());
  }

  onSelectFile() {
    document.getElementById("fileInput").click();
  }

  onFileSelected(event: any) {
    this.file = event.target.files[0];
    this.formLoad.controls.filename.setValue(this.file.name);
  }

  onLoad() {
    this.loading = true;
    this.loadError = null;
    const format = this.formatMap.get(this.formLoad.controls.format.value);
    this.importService.parseImport(format,
      this.formLoad.controls.kallsystem.value.namn, this.file).subscribe(
      data => {
        this.loading = false;
        this.setData(data);
        },
      (error) => {
        this.loading = false;
        this.setData([]);
        this.unsavedChanges = false;
        this.loadError = RestError.LOAD_ERROR;
        if (error.error) {
          this.loadError += "\n" + this.parseErrorMessage(error.error);
        }
      }
    );
  }

  onRemove(matning: ImportMatning) {
    this.data.splice(this.data.indexOf(matning), 1);
    this.setData(this.data);
    if (this.data.length === 0) {
      this.unsavedChanges = false;
    }
  }

  getKommentarAndFel(matning: ImportMatning): string {
    return matning.kommentar + this.hasError(matning) ? matning.importFel.map(e => e.error).join(" ") : "";
  }

  hasAnyError(): boolean {
    return this.data.filter(m => this.hasError(m)).length > 0;
  }

  hasData(): boolean {
    return this.data.length > 0;
  }

  hasRapportor(): boolean {
    return this.formSave.controls.rapportor.value.length > 0;
  }

  hasError(matning: ImportMatning): boolean {
    return matning.importFel.length > 0;
  }

  hasPropertyError(matning: ImportMatning, property: string): boolean {
    return matning.importFel.find(e => e.property === property) != null;
  }

  hasFile(): boolean {
    return this.file != null;
  }

  canSave(): boolean {
    return this.hasData() && !this.hasAnyError() && this.hasRapportor();
  }

  showMatbjektLink(matning: ImportMatning): boolean {
    return matning.matobjektId != null && this.isAdminOrTH;
  }

  private setData(data: ImportMatning[]) {
    this.unsavedChanges = true;
    this.data = data;
    this.paginator.length = this.data.length;
    const errorNr = this.data.filter(m => m.importFel.length > 0).length;
    const erroMsg = errorNr > 0 ? "Filen innehåller fel. " + errorNr + " st " +
      (errorNr > 1 ? "mätningar" : "mätning") + " kan inte sparas." : "";
    this.formSave.controls.status.setValue(erroMsg);
    this.updateViewData();
  }

  private updateViewData() {
    this.sortData();
    const indexStart = this.paginator.pageSize * this.paginator.pageIndex;
    const indexStop = Math.min(this.paginator.pageSize * (this.paginator.pageIndex + 1), this.data.length);
    this.viewData = this.data.slice(indexStart, indexStop);
  }

  private sortData() {
    const sortprop = this.sort.active;
    const asc = this.sort.direction === "asc";
    switch (sortprop) {
      case "avlastVarde":
        this.data.sort((first, second) => {
          const firstNum = parseFloat(first[sortprop].replace(",", "."));
          const secondNum = parseFloat(second[sortprop].replace(",", "."));
          const numSort = asc ? firstNum - secondNum : secondNum - firstNum;
          if (!isNaN(numSort)) {
            return numSort;
          }
          return asc ? first[sortprop].localeCompare(second[sortprop]) : second[sortprop].localeCompare(first[sortprop])
        });
        break;
      case "kommentarAndImportFel":
        this.data.sort((first, second) => {
          const firstStr = this.getKommentarAndFel(first);
          const secondStr = this.getKommentarAndFel(second);
          return asc ? firstStr.localeCompare(secondStr) : secondStr.localeCompare(firstStr);
        });
        break;
      default:
        this.data.sort((first, second) =>
          asc ? first[sortprop].localeCompare(second[sortprop]) : second[sortprop].localeCompare(first[sortprop])
        );
    }
  }

  private parseErrorMessage(error: ImportMatning[]) {
    return error[0].importFel[0].error;
  }

  onSave() {
    this.saving = true;

    this.importService.executeImport(this.formSave.controls.rapportor.value,
      this.formLoad.controls.kallsystem.value.namn, this.data).subscribe(
      data => {
        this.saving = false;
        this.setData([]);
        this.unsavedChanges = false;
      },
      (error) => {
        this.saving = false;
        this.formSave.controls.status.setValue(RestError.LOAD_ERROR);
      }
    );
  }

  selectedKallsystemHasTips(): boolean {
    return this.formLoad.controls.kallsystem.value && this.formLoad.controls.kallsystem.value.tips;
  }
}

export enum RestError {
  LOAD_ERROR = "Filen kunde ej läsas in."
}

