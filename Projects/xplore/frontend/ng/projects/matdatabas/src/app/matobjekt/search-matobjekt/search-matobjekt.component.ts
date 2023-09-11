import {AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output, ViewChild} from "@angular/core";
import { MatCheckboxChange } from "@angular/material/checkbox";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import {merge, Observable, of} from "rxjs";
import {catchError, debounceTime, distinctUntilChanged, filter, flatMap, map, startWith, switchMap} from "rxjs/operators";
import {
  ListMatobjekt,
  MatningstypMatobjektFilter,
  MatobjektMapinfo,
  MatobjektService
} from "../../services/matobjekt.service";
import {UntypedFormControl, UntypedFormGroup} from "@angular/forms";
import {ListMatobjektgrupp, MatobjektgruppService} from "../../services/matobjektgrupp.service";
import {Anvandargrupp, AnvandargruppService} from "../../services/anvandargrupp.service";
import {MatobjektTyp} from "../matobjekt-typ";
import {MdbMapComponent} from "../../common/components/mdb-map.component";
import {UserService} from "../../services/user.service";
import {MdbDialogService} from "../../services/mdb-dialog.service";
import {ListMatrunda, MatrundaService} from "../../services/matrunda.service";
import {SearchFormSettingsService} from "../../services/search-form-settings.service";

@Component({
  selector: "mdb-search-matobjekt",
  template: `
    <div class="main-content">
      <div class="headerSpace">
        <div class="formSpace">
          <div *ngIf="laggTillMatobjektButton && userService.userDetails.isTillstandshandlaggare()" class="actions">
            <button routerLink="new" mat-raised-button color="primary">Lägg till mätobjekt</button>
          </div>

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
                <select matNativeControl placeholder="Typ av mätobjekt" formControlName="typ">
                  <option [value]="''">Alla typer</option>
                  <option *ngFor="let mt of MatobjektTyp | keyvalue" [value]="mt.key">{{mt.value}}</option>
                </select>
              </mat-form-field>
              <mat-form-field>
                <select matNativeControl placeholder="Mätningsstatus" formControlName="status">
                  <option [value]="''">Alla status</option>
                  <option *ngFor="let ms of MatningStatus | keyvalue" [value]="ms.key">{{ms.value}}</option>
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
                <input matInput [readonly]="true" type="text" placeholder="...och grupp" formControlName="matobjektgrupp1"/>
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
              <mat-form-field *ngIf="userService.userDetails.isMatrapportor()">
                <select matNativeControl placeholder="Mätansvarig" formControlName="anvandargrupp">
                  <option></option>
                  <option *ngFor="let ag of anvandargrupper" [ngValue]="ag.id">{{ag.namn}}</option>
                </select>
              </mat-form-field>
              <div class="checkboxes">
                <mat-checkbox formControlName="aktiva">Aktiva mätobjekt</mat-checkbox>
              </div>
            </div>
            <div>
              <a href="javascript:void(0)" (click)="resetFilters()"><mat-icon style="vertical-align: text-bottom">clear_back</mat-icon> Rensa filtrering</a>
            </div>
          </form>
        </div>
        <mdb-map class="mapSpace" #map [fullscreen]="false" (matobjektInArea)="matobjektInAreaEvent($event)"
                 [selection]="isMultiSelect()"
                 selectMatobjektInArea="true"
                 [rapporteraLink]="rapporteraLinkInMap"
                 ></mdb-map>
      </div>

      <div class="search-result">
        <table mat-table [dataSource]="matobjekt" matSort matSortActive="namn" matSortDisableClear
               matSortDirection="asc">
          <ng-container matColumnDef="checkbox" *ngIf="this.isMultiSelect()">
            <th mat-header-cell *matHeaderCellDef>
              <mat-checkbox (change)="onSelectAllOnPage($event)"></mat-checkbox>
            </th>
            <td mat-cell *matCellDef="let row">
              <mat-checkbox (change)="toggleMatobjekt(row.id)"
                            [checked]="selectedMatobjekt.has(row.id)"></mat-checkbox>
            </td>
          </ng-container>
          <ng-container matColumnDef="namn">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Namn</th>
            <td mat-cell *matCellDef="let row"><a routerLink="/matobjekt/{{row.id}}">{{row.namn}}</a></td>
          </ng-container>
          <ng-container matColumnDef="typ">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Typ</th>
            <td mat-cell *matCellDef="let row">{{getMatobjektTypNamn(row.typ)}}</td>
          </ng-container>
          <ng-container matColumnDef="fastighet">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Fastighet</th>
            <td mat-cell *matCellDef="let row">{{row.fastighet}}</td>
          </ng-container>
          <ng-container matColumnDef="lage">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Läge</th>
            <td mat-cell *matCellDef="let row">{{row.lage}}</td>
          </ng-container>
          <ng-container matColumnDef="aktiv">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear [arrowPosition]="'before'">Aktiv</th>
            <td mat-cell *matCellDef="let row">{{row.aktiv ? "Ja" : "Nej"}}</td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="columns"></tr>
          <tr mat-row *matRowDef="let row; columns: columns;"></tr>
        </table>
        <mat-paginator class="footer-item" [length]="resultsLength" [pageSize]="10"
                       [pageSizeOptions]="[10, 25, 50]"></mat-paginator>
      </div>
    </div>
  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
    }

    .headerSpace {
      /*margin-bottom: 2rem;*/
    }

    @media only screen and (min-width: 930px) {
      .headerSpace {
        display: grid;
        grid-column-gap: 1rem;
        grid-template-columns: auto 1fr;
      }
    }

    .mapSpace {
      display: block;
      height: 10em;
      border: 1px solid grey;
    }

    @media only screen and (min-width: 930px) {
      .mapSpace {
        display: block;
        height: 34em;
      }
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

    .search-button {
      width: fit-content;
    }

    .search-result {
      display: grid;
      grid-gap: 0;
    }

    .checkboxes {
      display: grid;
      grid-gap: 0;
    }

    .mat-column-checkbox {
      width: 5%;
    }

    .mat-column-namn, .mat-column-typ, .mat-column-fastighet {
      width: 15%;
    }

    .mat-column-lage {
      width: auto;
    }

    .mat-column-aktiv {
      width: 10%;
      text-align: right;
    }

    /*@media only screen and (min-width: 980px) {*/
    /*  .search-form-fields {*/
    /*    grid-column-gap: 1rem;*/
    /*    grid-template-columns: repeat(2, 1fr);*/
    /*  }*/
    /*}*/

    /*@media only screen and (min-width: 576px) and (max-width: 979px) {*/
    /*  .search-form-fields {*/
    /*    grid-column-gap: 1rem;*/
    /*    grid-template-columns: repeat(2, 1fr);*/
    /*  }*/
    /*}*/
  `]
})
export class SearchMatobjektComponent implements OnInit, AfterViewInit {

  @Input() selectionMode: SelectionMode = SelectionMode.NONE;
  @Input() initialFilter: MatningstypMatobjektFilter;
  @Input() rapporteraLinkInMap = false;
  @Input() laggTillMatobjektButton = false;
  @Output() selected = new EventEmitter<number[]>();

  @ViewChild(MdbMapComponent, {static: true}) map: MdbMapComponent;

  public MatobjektTyp = MatobjektTyp;
  public MatningStatus = MatningStatus;

  searchForm: UntypedFormGroup;
  searching = true;

  columns: string[] = ["namn", "typ", "fastighet", "lage", "aktiv"];
  resultsLength = 0;
  matobjekt: ListMatobjekt[] = [];
  matobjektFilter: MatningstypMatobjektFilter = {};

  selectedMatobjektgrupper: ListMatobjektgrupp[] = [null, null];

  anvandargrupper: Anvandargrupp[] = [];
  matrundor: ListMatrunda[];

  matobjektnamn: string[] = [];
  filteredMatobjektnamn: Observable<string[]>;
  fastigheter: string[] = [];
  filteredFastigheter: Observable<string[]>;

  selectedMatobjekt: Set<number> = new Set<number>();
  matobjektInArea: number[];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private matobjektService: MatobjektService, private matobjektgruppService: MatobjektgruppService,
              public userService: UserService,
              public matrundaService: MatrundaService,
              public popupService: MdbDialogService,
              public searchFormSettingsService: SearchFormSettingsService,
              private anvandargruppService: AnvandargruppService) {
  }

  ngOnInit() {
    this.matrundaService.getAll({onlyAktiva: true}).subscribe(result => {
      this.matrundor = result;
    });

    this.searchForm = new UntypedFormGroup({
      namn: new UntypedFormControl(),
      typ: new UntypedFormControl(""),
      status: new UntypedFormControl(""),
      fastighet: new UntypedFormControl(),
      matrunda: new UntypedFormControl(),
      matobjektgrupp0: new UntypedFormControl(),
      matobjektgrupp1: new UntypedFormControl(),
      anvandargrupp: new UntypedFormControl(),
      aktiva: new UntypedFormControl()
    });

    this.setInitialSearchForm();

    const storageValue = this.searchFormSettingsService.getStoredSearchForm();

    if (storageValue) {
      this.searchForm.patchValue(storageValue.form);
      this.selectedMatobjektgrupper = storageValue.selectedMatobjektgrupper;
      this.updateMatobjektFilter();
    }

    this.initColumns();
    this.initAnvandargrupper();
    this.initNamn();
    this.initFastigheter();
  }

  ngAfterViewInit() {
    const storageFormPolygon = this.searchFormSettingsService.getStoredSearchPolygon();

    if (storageFormPolygon != null) {
      this.map.setSelectionPolygon(storageFormPolygon);
    }

    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    if (this.selectionMode && this.initialFilter) {
      this.matobjektFilter = this.initialFilter;
      if (this.matobjektFilter.matobjektIds) {
        this.matobjektFilter.matobjektIds.forEach(id => this.selectedMatobjekt.add(id));
      }
    }

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        filter(() => this.matobjektFilter != null),
        switchMap(() => {
          this.searching = true;
          return this.matobjektService.getPage(
            this.paginator.pageIndex,
            this.paginator.pageSize,
            this.sort.active,
            this.sort.direction,
            this.matobjektFilter);
        }),
        map(data => {
          this.resultsLength = data.totalElements;
          return data.content;
        }),
        catchError(() => {
          return of([]);
        })
      ).subscribe(data => {
      this.matobjekt = data;
      this.searching = false;
    });

    this.searchForm.valueChanges.pipe(distinctUntilChanged(), debounceTime(500)).pipe(switchMap(val => {
      return this.getFeatures(val);
    })).subscribe(
      result => {
        this.map.clearMatobjekt();
        this.map.setMatobjekt(result, false, false, this.getMatobjektIsSelectedCallback(), this.getMatobjektIsSelectedChangeCallback());
      });

    this.getFeatures(this.searchForm.getRawValue()).subscribe(
      result => {
        this.map.clearMatobjekt();
        this.map.setMatobjekt(result, false, false, this.getMatobjektIsSelectedCallback(), this.getMatobjektIsSelectedChangeCallback());
      });

    this.searchForm.valueChanges.pipe(distinctUntilChanged(), debounceTime(500)).subscribe(val => {
      this.onSokMatobjekt();
    });
  }

  private getMatobjektIsSelectedCallback() {
    return (mapinfo: MatobjektMapinfo) => {
      return this.selectedMatobjekt.has(mapinfo.id);
    };
  }

  private getMatobjektIsSelectedChangeCallback() {
    return (mapinfo: MatobjektMapinfo, state) => {
      if (state) {
        this.selectedMatobjekt.add(mapinfo.id);
      } else {
        this.selectedMatobjekt.delete(mapinfo.id);
      }

      this.emitSelectedMatobjekt();
    };
  }

  getMatobjektTypNamn(typ: number) {
    return Object.values(MatobjektTyp)[typ];
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

  initColumns() {
    if (this.selectionMode) {
      this.columns = ["checkbox", ...this.columns];
    }
  }

  initAnvandargrupper() {
    if (this.userService.userDetails.isMatrapportor()) {
      this.anvandargruppService.getAll().subscribe(
        anvandargrupper => {
          this.anvandargrupper = anvandargrupper;
        });
    }
  }

  initNamn() {
    this.filteredMatobjektnamn = this.searchForm.get("namn").valueChanges
      .pipe(debounceTime(500),
        flatMap((value: string) => value.length > 0 ?
          this.matobjektService.getMatobjektNamn(value).pipe(
            catchError(() => of([])))
          : of([])
        )
      );
  }

  initFastigheter() {
    this.filteredFastigheter = this.searchForm.get("fastighet").valueChanges
      .pipe(debounceTime(500),
        flatMap((value: string) => value.length > 0 ?
          this.matobjektService.getMatobjektFastigheter(value).pipe(
            catchError(() => of([])))
          : of([])
        )
      );
  }

  onSokMatobjekt() {
    this.searchFormSettingsService.putStoredSearchForm(this.searchForm.value, this.selectedMatobjektgrupper);
    this.searchFormSettingsService.putStoredSearchPolygon(this.map.getSelectionPolygon());

    this.updateMatobjektFilter();
    this.paginator.pageIndex = 0;
    this.paginator._changePageSize(this.paginator.pageSize);
  }

  updateMatobjektFilter() {
    const typ = this.searchForm.get("typ").value;
    const status = this.searchForm.get("status").value;
    const anvandargrupp = this.searchForm.get("anvandargrupp").value;
    const matobjektgrupper = this.selectedMatobjektgrupper.filter(mg => mg != null).map(mg => mg.id);
    this.matobjektFilter = {
      matobjektNamn: this.searchForm.get("namn").value,
      matobjektTyp: typ ? Object.keys(MatobjektTyp).indexOf(typ) : null,
      matningStatus: status ? Object.keys(MatningStatus).indexOf(status) : null,
      fastighet: this.searchForm.get("fastighet").value,
      matrunda: this.searchForm.get("matrunda").value,
      matobjektgrupper: matobjektgrupper.length ? matobjektgrupper : null,
      matansvarigAnvandargruppIds: anvandargrupp ? [anvandargrupp] : null,
      matobjektIds: this.matobjektInArea ? this.matobjektInArea : null,
      onlyAktiva: this.searchForm.get("aktiva").value
    };
  }

  toggleMatobjekt(id: number) {
    if (this.selectedMatobjekt.has(id)) {
      this.selectedMatobjekt.delete(id);
    } else {
      this.selectedMatobjekt.add(id);
    }

    this.emitSelectedMatobjekt();
    this.map.selectedMatobjektUpdated();
  }

  onSelectAllOnPage(event: MatCheckboxChange) {
    this.matobjekt.forEach(lm => {
      if (event.checked) {
        this.selectedMatobjekt.add(lm.id);
      } else {
        this.selectedMatobjekt.delete(lm.id);
      }
    });

    this.emitSelectedMatobjekt();
    this.map.selectedMatobjektUpdated();
  }

  emitSelectedMatobjekt() {
    this.selected.emit(Array.from(this.selectedMatobjekt.values()));
  }

  getFeatures(formValues) {
    const typ = this.searchForm.get("typ").value;
    const status = this.searchForm.get("status").value;
    const matobjektgrupper = this.selectedMatobjektgrupper.filter(mg => mg != null).map(mg => mg.id);

    const mapFilter: MatningstypMatobjektFilter = {
      matobjektNamn: formValues.namn,
      matobjektTyp: typ ? Object.keys(MatobjektTyp).indexOf(typ) : null,
      matningStatus: status ? Object.keys(MatningStatus).indexOf(status) : null,
      fastighet: formValues.fastighet,
      matrunda: formValues.matrunda,
      matobjektgrupper: matobjektgrupper.length ? matobjektgrupper : null,
      matansvarigAnvandargruppIds: formValues.anvandargrupp ? [formValues.anvandargrupp] : null,
      onlyAktivaMatobjekt: this.searchForm.get("aktiva").value
    };

    return this.matobjektService.getMatobjektMapinfo(mapFilter);
  }

  matobjektInAreaEvent(selected: number[]) {
    this.matobjektInArea = selected;
    this.onSokMatobjekt();
  }

  isMultiSelect() {
    return this.selectionMode === SelectionMode.MULTI;
  }

  setInitialSearchForm() {
    this.searchForm.patchValue({
      namn: "",
      typ: "",
      status: "",
      fastighet: "",
      matrunda: "",
      matobjektgrupp0: "",
      matobjektgrupp1: "",
      anvandargrupp: "",
      aktiva: false,
    });

    this.selectedMatobjektgrupper = [null, null];
  }

  resetFilters() {
    this.setInitialSearchForm();
    this.map.clearAreaSelection();
  }
}

export enum MatningStatus {
  EJ_GRANSKAT = "Ej granskat",
  GODKANT = "Godkänt",
  FEL = "Fel"
}

export enum SelectionMode {
  NONE,
  MULTI
}
