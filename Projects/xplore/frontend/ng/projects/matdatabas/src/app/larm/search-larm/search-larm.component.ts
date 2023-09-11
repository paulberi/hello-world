import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from "@angular/core";
import {MatningstypMatobjektFilter, MatobjektService} from "../../services/matobjekt.service";
import {UntypedFormControl, UntypedFormGroup} from "@angular/forms";
import {ListMatobjektgrupp, MatobjektgruppService} from "../../services/matobjektgrupp.service";
import {Anvandargrupp, AnvandargruppService} from "../../services/anvandargrupp.service";
import {merge, Observable, of} from "rxjs";
import {ListMatrunda, MatrundaService} from "../../services/matrunda.service";
import {catchError, debounceTime, distinctUntilChanged, filter, flatMap, map, startWith, switchMap} from "rxjs/operators";
import {MatobjektTyp} from "../../matobjekt/matobjekt-typ";
import {MatSort} from "@angular/material/sort";
import {MatCheckbox, MatCheckboxChange} from "@angular/material/checkbox";
import {MatPaginator} from "@angular/material/paginator";
import {Larm, LarmService} from "../../services/larm.service";
import {formatNumber} from "@angular/common";
import {Larmniva, LarmnivaService} from "../../services/larmniva.service";
import {MdbDialogService} from "../../services/mdb-dialog.service";
import { Moment } from "moment";
import moment from "moment";
import {SearchFormSettingsService} from "../../services/search-form-settings.service";

@Component({
  selector: "mdb-search-larm",
  template: `
    <div class="main-content">
      <form [formGroup]="searchForm">
        <div class="search-form-fields">
          <mat-form-field>
            <input matInput id="namn" type="text" placeholder="Namn på mätobjekt" formControlName="namn"
                   [matAutocomplete]="autoNamn"/>
            <mat-autocomplete #autoNamn="matAutocomplete">
              <mat-option *ngFor="let namn of filteredMatobjektnamn | async" [value]="namn">{{namn}}</mat-option>
            </mat-autocomplete>
          </mat-form-field>
          <mat-form-field>
            <select matNativeControl placeholder="Typ" formControlName="typ">
              <option [value]="''">Alla typer</option>
              <option *ngFor="let mt of MatobjektTyp | keyvalue" [value]="mt.key">{{mt.value}}</option>
            </select>
          </mat-form-field>
          <mat-form-field>
            <input matInput id="fastighet" type="text" placeholder="Fastighet" formControlName="fastighet"
                   [matAutocomplete]="autoFastighet"/>
            <mat-autocomplete #autoFastighet="matAutocomplete">
              <mat-option *ngFor="let fastighet of filteredFastigheter | async" [value]="fastighet">{{fastighet}}
              </mat-option>
            </mat-autocomplete>
          </mat-form-field>
          <mat-form-field>
            <input matInput [readonly]="true" type="text" placeholder="Grupp" formControlName="matobjektgrupp0"/>
            <button *ngIf="searchForm.get('matobjektgrupp0').value" matSuffix mat-icon-button type="button"
                    (click)="onClearMatobjektgrupp(0, searchForm)">
              <mat-icon>clear</mat-icon>
            </button>
            <button matSuffix mat-icon-button (click)="onSelectMatobjektgrupp(0)" type="button">
              <mat-icon>search</mat-icon>
            </button>
          </mat-form-field>
          <mat-form-field>
            <input matInput [readonly]="true" type="text" placeholder="Namn på grupp" formControlName="matobjektgrupp1"/>
            <button *ngIf="searchForm.get('matobjektgrupp1').value" matSuffix mat-icon-button type="button"
                    (click)="onClearMatobjektgrupp(1, searchForm)">
              <mat-icon>clear</mat-icon>
            </button>
            <button matSuffix mat-icon-button (click)="onSelectMatobjektgrupp(1)" type="button">
              <mat-icon>search</mat-icon>
            </button>
          </mat-form-field>
          <mat-form-field>
            <select matNativeControl placeholder="Mätrunda" formControlName="matrunda">
              <option></option>
              <option *ngFor="let m of matrundor" [value]="m.id">{{m.namn}} ({{m.antalMatobjekt}} st)</option>
            </select>
          </mat-form-field>
          <mat-form-field>
            <select matNativeControl placeholder="Mätansvarig" formControlName="anvandargrupp">
              <option></option>
              <option *ngFor="let ag of anvandargrupper" [ngValue]="ag.id">{{ag.namn}}</option>
            </select>
          </mat-form-field>
          <mat-form-field>
            <select matNativeControl placeholder="Larmnivå" formControlName="larmniva">
              <option></option>
              <option *ngFor="let l of larmnivaer" [ngValue]="l.id">{{l.namn}}</option>
            </select>
          </mat-form-field>
          <div class="checkboxes">
            <mat-checkbox formControlName="egnaLarm">Endast larm till mina grupper</mat-checkbox>
            <mat-checkbox (click)="onKvitteradeToggle($event)" formControlName="kvitteraLarm">Visa kvitterade larm</mat-checkbox>
          </div>
          <mat-form-field>
            <mat-label>Från och med</mat-label>
            <input matInput placeholder="åååå-mm-dd" [matDatepicker]="fromDatumDatepicker" formControlName="fromDatum">
            <mat-datepicker-toggle matSuffix [for]="fromDatumDatepicker"></mat-datepicker-toggle>
            <mat-datepicker #fromDatumDatepicker></mat-datepicker>
          </mat-form-field>
          <mat-form-field>
            <mat-label>Till och med</mat-label>
            <input matInput placeholder="åååå-mm-dd" [matDatepicker]="toDatumDatepicker" formControlName="toDatum">
            <mat-datepicker-toggle matSuffix [for]="toDatumDatepicker"></mat-datepicker-toggle>
            <mat-datepicker #toDatumDatepicker></mat-datepicker>
          </mat-form-field>
        </div>
        <div>
          <a href="javascript:void(0)" (click)="resetFilters()"><mat-icon style="vertical-align: text-bottom">clear_back</mat-icon> Rensa filtrering</a>
        </div>
      </form>

      <div class="search-result">
        <table mat-table [dataSource]="larm" matSort matSortActive="matobjektNamn" matSortDisableClear
               matSortDirection="asc">
          <ng-container matColumnDef="checkbox">
            <th mat-header-cell *matHeaderCellDef>
              <mat-checkbox #checkAll (change)="onSelectAllOnPage($event)"
                            [disabled]="showKvitteradeLarm()"></mat-checkbox>
            </th>
            <td mat-cell *matCellDef="let l">
              <mat-checkbox (change)="toggleLarmId(l.id)"
                            [checked]="selectedLarm.has(l.id)"
                            [disabled]="showKvitteradeLarm()"></mat-checkbox>
            </td>
          </ng-container>
          <ng-container matColumnDef="matobjektNamn">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Namn</th>
            <td mat-cell *matCellDef="let l"><a routerLink="/matobjekt/{{l.matobjektId}}">{{l.matobjektNamn}}</a></td>
          </ng-container>
          <ng-container matColumnDef="matningstypNamn">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Mätningstyp</th>
            <td mat-cell *matCellDef="let l">{{l.matningstypNamn}}</td>
          </ng-container>
          <ng-container matColumnDef="matobjektFastighet">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Fastighet</th>
            <td mat-cell *matCellDef="let l">{{l.matobjektFastighet}}</td>
          </ng-container>
          <ng-container matColumnDef="avlastDatum">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Datum</th>
            <td mat-cell *matCellDef="let l">{{formatDatum(l.avlastDatum)}}</td>
          </ng-container>
          <ng-container matColumnDef="larmnivaNamn">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Larmnivå</th>
            <td mat-cell *matCellDef="let l">{{l.larmnivaNamn}}</td>
          </ng-container>
          <ng-container matColumnDef="gransvarde">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Gränsvärde</th>
            <td mat-cell *matCellDef="let l">{{typAvKontroll[l.typAvKontroll]}}: {{formatValue(l.gransvarde) + l.enhet}}</td>
          </ng-container>
          <ng-container matColumnDef="varde">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Värde</th>
            <td mat-cell *matCellDef="let l">{{formatVarde(l) + l.enhet}}</td>
          </ng-container>
          <ng-container matColumnDef="graf">
            <th mat-header-cell *matHeaderCellDef [disabled]="true" mat-sort-header disableClear></th>
            <td mat-cell *matCellDef="let l"><a [routerLink]="['../granskning-graf', {matningstypIds: l.matningstypId}]">Graf</a></td>
          </ng-container>
          <tr mat-header-row *matHeaderRowDef="columns"></tr>
          <tr mat-row *matRowDef="let row; columns: columns;"></tr>
        </table>
        <mat-paginator class="footer-item" [length]="resultsLength" [pageSize]="10"
                       [pageSizeOptions]="[10, 25, 50]"></mat-paginator>
        <h4>Valda: {{getNrSelected()}} st</h4>
      </div>
      <div class="actions">
        <button (click)="onKvitteraLarm()"
                [disabled]="!isKvitteraLarmButtonEnabled()"
                mat-raised-button color="primary">
          Kvittera larm
        </button>
      </div>
    </div>
  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
    }

    form {
      display: grid;
    }

    mat-form-field {
      width: 100%;
    }

    .search-form-fields {
      display: grid;
    }

    .search-result {
      display: grid;
      grid-gap: 0;
    }

    .checkboxes {
      display: grid;
      grid-gap: 0;
    }

    .mat-column-checkbox, .mat-column-varde {
      width: 5%;
    }

    .mat-column-matobjektNamn, .mat-column-matningstypNamn, .mat-column-matobjektFastighet {
      width: 15%;
    }

    .mat-column-gransvarde {
      width: 10%;
    }

    @media only screen and (min-width: 980px) {
      .search-form-fields {
        grid-column-gap: 1rem;
        grid-template-columns: repeat(4, 1fr);
      }
    }

    @media only screen and (min-width: 576px) and (max-width: 979px) {
      .search-form-fields {
        grid-column-gap: 1rem;
        grid-template-columns: repeat(2, 1fr);
      }
    }
  `]
})
export class SearchLarmComponent implements AfterViewInit, OnInit {

  @Input() initialFilter: MatningstypMatobjektFilter;
  @Output() selected = new EventEmitter<number[]>();

  public MatobjektTyp = MatobjektTyp;

  searchForm: UntypedFormGroup;
  searching = true;

  columns: string[] = ["checkbox", "matobjektNamn", "matningstypNamn", "matobjektFastighet",
    "avlastDatum", "larmnivaNamn", "gransvarde", "varde", "graf"];
  resultsLength = 0;
  larm: Larm[] = [];
  matningstypMatobjektFilter: MatningstypMatobjektFilter = {};

  selectedMatobjektgrupper: ListMatobjektgrupp[] = [null, null];

  anvandargrupper: Anvandargrupp[] = [];
  matrundor: ListMatrunda[];
  larmnivaer: Larmniva[];
  matobjektnamn: string[] = [];
  filteredMatobjektnamn: Observable<string[]>;
  fastigheter: string[] = [];
  filteredFastigheter: Observable<string[]>;
  larmStatus = 0;

  selectedLarm: Set<number> = new Set<number>();
  typAvKontroll: {[key: number]: string} = {0: "Max", 1: "Min"};

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild("checkAll") private checkAll: MatCheckbox;
  constructor(private matrundaService: MatrundaService, private matobjektService: MatobjektService,
              private matobjektgruppService: MatobjektgruppService, private anvandargruppService: AnvandargruppService,
              private larmService: LarmService,
              public popupService: MdbDialogService,
              private searchFormSettingsService: SearchFormSettingsService,
              private larmnivaService: LarmnivaService) {
  }

  ngOnInit() {
    this.searchForm = new UntypedFormGroup({
      namn: new UntypedFormControl(),
      typ: new UntypedFormControl(""),
      fastighet: new UntypedFormControl(),
      matrunda: new UntypedFormControl(),
      matobjektgrupp0: new UntypedFormControl(),
      matobjektgrupp1: new UntypedFormControl(),
      anvandargrupp: new UntypedFormControl(),
      egnaLarm: new UntypedFormControl(true),
      larmniva: new UntypedFormControl(),
      fromDatum: new UntypedFormControl(),
      toDatum: new UntypedFormControl(),
      kvitteraLarm: new UntypedFormControl(false)
    });

    this.setInitialSearchForm();

    const storageValue = this.searchFormSettingsService.getStoredSearchForm();

    if (storageValue) {
      this.searchForm.patchValue(storageValue.form);
      this.selectedMatobjektgrupper = storageValue.selectedMatobjektgrupper;
      this.updateMatningstypMatobjektFilter();
    }

    this.matrundaService.getAll({onlyAktiva: true}).subscribe(result => {
      this.matrundor = result;
    });

    this.initLarmnivaer();
    this.initAnvandargrupper();
    this.initNamn();
    this.initFastigheter();
    this.updateMatningstypMatobjektFilter();
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    if (this.initialFilter) {
      this.matningstypMatobjektFilter = this.initialFilter;
      if (this.matningstypMatobjektFilter.includeIds) {
        this.matningstypMatobjektFilter.includeIds.forEach(id => this.selectedLarm.add(id));
      }
    }

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        filter(() => this.matningstypMatobjektFilter != null),
        switchMap(() => {
          this.searching = true;
          return this.larmService.getLarmPage(
            this.paginator.pageIndex,
            this.paginator.pageSize,
            this.sort.active,
            this.sort.direction,
            this.showKvitteradeLarm() ? 1 : 0,
            this.searchForm.get("egnaLarm").value,
            null,
            this.searchForm.get("larmniva").value,
            this.matningstypMatobjektFilter);
        }),
        map(data => {
          this.resultsLength = data.totalElements;
          return data.content;
        }),
        catchError(() => {
          return of([]);
        })
      ).subscribe(data => {
      this.larm = data;
      this.searching = false;
    });

    this.searchForm.valueChanges.pipe(distinctUntilChanged(), debounceTime(500)).subscribe(val => {
      this.onSokLarm();
    });

  }

  private startDatumValue(momentIn: Moment): string | null {
    return momentIn ? moment(momentIn).hours(0).minutes(0).seconds(0).toISOString(false) :
                      null;
  }

  private endDatumValue(momentIn: Moment): string | null {
    return momentIn ? moment(momentIn).hours(23).minutes(59).seconds(59).toISOString(false) :
                      null;
  }

  onSelectMatobjektgrupp(id: number) {
    this.popupService.selectMatobjektgrupp().subscribe(grupp => {
      if (grupp) {
        this.selectedMatobjektgrupper[id] = grupp;
        this.searchForm.get("matobjektgrupp" + id).setValue(grupp.namn);
      }
    });
  }

  onClearMatobjektgrupp(id: number, form: UntypedFormGroup) {
    this.selectedMatobjektgrupper[id] = null;
    form.get("matobjektgrupp" + id).setValue(null);
  }

  initAnvandargrupper() {
    this.anvandargruppService.getAll().subscribe(
      anvandargrupper => {
        this.anvandargrupper = anvandargrupper;
      });
  }

  initLarmnivaer() {
    this.larmnivaService.getLarmnivaer().subscribe(
      larmnivaer => {
        this.larmnivaer = larmnivaer;
      });
  }

  initNamn() {
    this.filteredMatobjektnamn = this.searchForm.get("namn").valueChanges.pipe(
      debounceTime(500),
      flatMap((value: string) => value.length > 0 ?
        this.matobjektService.getMatobjektNamn(value).pipe(
          catchError(() => of([])))
        : of([])
      )
    );
  }

  initFastigheter() {
    this.filteredFastigheter = this.searchForm.get("fastighet").valueChanges.pipe(
      debounceTime(500),
      flatMap((value: string) => value.length > 0 ?
        this.matobjektService.getMatobjektFastigheter(value).pipe(
          catchError(() => of([])))
        : of([])
      )
    );
  }

  onSokLarm() {
    this.searchFormSettingsService.putStoredSearchForm(this.searchForm.value, this.selectedMatobjektgrupper);

    this.updateMatningstypMatobjektFilter();
    this.paginator.pageIndex = 0;
    this.paginator._changePageSize(this.paginator.pageSize);
    this.selectedLarm.clear();
    this.emitselectedLarm();
  }

  updateMatningstypMatobjektFilter() {
    const typ = this.searchForm.get("typ").value;
    const matobjektgrupper = this.selectedMatobjektgrupper.filter(mg => mg != null).map(mg => mg.id);
    const anvandarGrupp = this.searchForm.get("anvandargrupp").value;
    this.matningstypMatobjektFilter = {
      matobjektNamn: this.searchForm.get("namn").value,
      matobjektTyp: typ ? Object.keys(MatobjektTyp).indexOf(typ) : null,
      fastighet: this.searchForm.get("fastighet").value,
      matrunda: this.searchForm.get("matrunda").value,
      matobjektgrupper: matobjektgrupper.length ? matobjektgrupper : null,
      matansvarigAnvandargruppIds: anvandarGrupp ? [anvandarGrupp] : null,
      matningStatus: status ? 0 : null,
      matningFromDatum: this.startDatumValue(this.searchForm.get("fromDatum").value),
      matningToDatum: this.endDatumValue(this.searchForm.get("toDatum").value),
    };
  }

  toggleLarmId(id: number) {
    if (this.selectedLarm.has(id)) {
      this.selectedLarm.delete(id);
    } else {
      this.selectedLarm.add(id);
    }

    this.emitselectedLarm();
  }

  onSelectAllOnPage(event: MatCheckboxChange) {
    this.larm.forEach(larm => {
      if (event.checked) {
        this.selectedLarm.add(larm.id);
      } else {
        this.selectedLarm.delete(larm.id);
      }
    });

    this.emitselectedLarm();
  }

  getNrSelected() {
    return this.selectedLarm.size;
  }

  emitselectedLarm() {
    this.selected.emit(Array.from(this.selectedLarm.values()));
  }

  formatDatum(datum: string): string {
    return datum.replace("T", " ").substring(0, datum.lastIndexOf(":"));
  }

  formatValue(value): string {
    return formatNumber(value, "sv-SE");
  }

  onKvitteraLarm() {
    this.larmService.kvittera(Array.from(this.selectedLarm.values())).subscribe(() => {
      this.selectedLarm.clear();
      this.paginator._changePageSize(this.paginator.pageSize);
      this.checkAll.checked = false;
    });
  }

  onKvitteradeToggle(event: MatCheckboxChange) {
    if (!event.checked) {
      this.checkAll.checked = false;
    }
  }

  formatVarde(larm: Larm): string {
    return formatNumber(larm.varde, "sv-SE", this.setDigits(larm.matningstypDecimaler));
  }

  setDigits(decimaler): string {
    return "1.0-" + (decimaler ? decimaler : 4);
  }

  isKvitteraLarmButtonEnabled(): boolean {
    return this.getNrSelected() > 0 && !this.showKvitteradeLarm();
  }

  showKvitteradeLarm(): boolean {
    return this.searchForm.get("kvitteraLarm").value;
  }

  setInitialSearchForm() {
    this.searchForm.patchValue({
      namn: "",
      typ: "",
      fastighet: "",
      matrunda: "",
      matobjektgrupp0: "",
      matobjektgrupp1: "",
      anvandargrupp: "",
      egnaLarm: true,
      larmniva: "",
      fromDatum: "",
      toDatuem: "",
      kvitteraLarm: false
    });

    this.selectedMatobjektgrupper = [null, null];
  }

  resetFilters() {
    this.setInitialSearchForm();
  }
}
