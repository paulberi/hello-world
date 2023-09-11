import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from "@angular/core";
import { MatCheckboxChange } from "@angular/material/checkbox";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import {merge, Observable, of} from "rxjs";
import {catchError, debounceTime, distinctUntilChanged, filter, flatMap, map, startWith, switchMap} from "rxjs/operators";
import {MatningstypMatobjekt, MatningstypMatobjektFilter, MatobjektMapinfo, MatobjektService} from "../../services/matobjekt.service";
import {UntypedFormControl, UntypedFormGroup} from "@angular/forms";
import {ListMatobjektgrupp, MatobjektgruppService} from "../../services/matobjektgrupp.service";
import {Anvandargrupp, AnvandargruppService} from "../../services/anvandargrupp.service";
import {MatobjektTyp} from "../../matobjekt/matobjekt-typ";
import {ListMatrunda, MatrundaService} from "../../services/matrunda.service";
import {MdbMapComponent} from "./mdb-map.component";
import {MdbDialogService} from "../../services/mdb-dialog.service";
import {UserService} from "../../services/user.service";
import {SearchFormSettingsService} from "../../services/search-form-settings.service";

@Component({
  selector: "mdb-search-matvarde",
  template: `
    <div class="main-content">
      <div class="headerSpace">
        <div class="formSpace">
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
              <div *ngIf="filterEjGranskade" class="checkboxes">
                <mat-checkbox formControlName="ejGranskade">Ej granskade</mat-checkbox>
              </div>
              <div>
                <a href="javascript:void(0)" (click)="resetFilters()"><mat-icon style="vertical-align: text-bottom">clear_back</mat-icon> Rensa filtrering</a>
              </div>
            </div>
          </form>
        </div>
        <mdb-map class="mapSpace" #map [fullscreen]="false" (matobjektInArea)="matobjektInAreaEvent($event)"
                 [selection]="true"
                 [selectMatobjektInArea]="true"
                 [rapporteraLink]="false"
        ></mdb-map>
      </div>

      <div class="search-result">
        <h4>Valda: {{getNrSelected()}} st
          &nbsp;&nbsp;&nbsp;
          <a href="javascript:void(0)" (click)="onRemoveSelection()"><mat-icon style="vertical-align: text-bottom">clear_back</mat-icon> Rensa urval</a>
          &nbsp;&nbsp;&nbsp;
          <mat-checkbox [checked]="showOnlySelected" (change)="toogleShowOnlySelected($event)">Visa valda</mat-checkbox>
        </h4>

        <table mat-table [dataSource]="matningstypMatobjekt" matSort matSortActive="matobjektNamn" matSortDisableClear
               matSortDirection="asc">
          <ng-container matColumnDef="checkbox">
            <th mat-header-cell *matHeaderCellDef>
              <mat-checkbox (change)="onSelectAllOnPage($event)"></mat-checkbox>
            </th>
            <td mat-cell *matCellDef="let mm">
              <mat-checkbox (change)="toggleMatningstyp(mm.matningstypId)"
                            [checked]="selectedMatningstyp.has(mm.matningstypId)"></mat-checkbox>
            </td>
          </ng-container>
          <ng-container matColumnDef="matobjektNamn">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Namn</th>
            <td mat-cell *matCellDef="let mm">
              <ng-container *ngIf="matobjektLink"><a routerLink="/matobjekt/{{mm.matobjektId}}">{{mm.matobjektNamn}}</a></ng-container>
              <ng-container *ngIf="!matobjektLink">{{mm.matobjektNamn}}</ng-container>
            </td>
          </ng-container>
          <ng-container matColumnDef="matningstypNamn">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Mätningstyp</th>
            <td mat-cell *matCellDef="let mm">{{mm.matningstypNamn}}</td>
          </ng-container>
          <ng-container matColumnDef="matobjektFastighet">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Fastighet</th>
            <td mat-cell *matCellDef="let mm">{{mm.matobjektFastighet}}</td>
          </ng-container>
          <ng-container matColumnDef="matobjektLage">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Läge</th>
            <td mat-cell *matCellDef="let mm">{{mm.matobjektLage}}</td>
          </ng-container>
          <ng-container matColumnDef="ejGranskade">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Ej granskade</th>
            <td mat-cell *matCellDef="let mm">{{mm.ejGranskade}}</td>
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

    .mat-column-matobjektNamn, .mat-column-matningstypNamn, .mat-column-matobjektFastighet {
      width: 15%;
    }

    .mat-column-ejGranskade {
      width: 10%;
      text-align: center;
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
export class SearchMatvardeComponent implements OnInit, AfterViewInit {

  @Input() initialFilter: MatningstypMatobjektFilter;
  @Input() initialSelected: number[];
  @Input() matobjektLink: boolean;
  @Input() clearSelectedOnSokChange = true;
  @Input() filterEjGranskade = true;
  @Output() selected = new EventEmitter<number[]>();

  @ViewChild(MdbMapComponent, {static: true}) map: MdbMapComponent;

  public MatobjektTyp = MatobjektTyp;

  searchForm: UntypedFormGroup;
  searching = true;

  columns: string[];
  resultsLength = 0;
  matningstypMatobjekt: MatningstypMatobjekt[] = [];
  matningstypMatobjektFilter: MatningstypMatobjektFilter = {};

  selectedMatobjektgrupper: ListMatobjektgrupp[] = [null, null];

  anvandargrupper: Anvandargrupp[] = [];
  matrundor: ListMatrunda[];
  matobjektnamn: string[] = [];
  filteredMatobjektnamn: Observable<string[]>;
  fastigheter: string[] = [];
  filteredFastigheter: Observable<string[]>;

  selectedMatningstyp: Set<number> = new Set<number>();
  showOnlySelected = false;

  matobjektInArea: number[];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private matrundaService: MatrundaService, private matobjektService: MatobjektService,
              public popupService: MdbDialogService,
              public userService: UserService,
              public searchFormSettingsService: SearchFormSettingsService,
              private matobjektgruppService: MatobjektgruppService, private anvandargruppService: AnvandargruppService) {
  }

  ngOnInit() {
    if (this.filterEjGranskade) {
      this.columns = ["checkbox", "matobjektNamn", "matningstypNamn", "matobjektFastighet", "matobjektLage", "ejGranskade"];
    } else {
      this.columns = ["checkbox", "matobjektNamn", "matningstypNamn", "matobjektFastighet", "matobjektLage"];
    }

    this.searchForm = new UntypedFormGroup({
      namn: new UntypedFormControl(),
      typ: new UntypedFormControl(""),
      fastighet: new UntypedFormControl(),
      matrunda: new UntypedFormControl(),
      matobjektgrupp0: new UntypedFormControl(),
      matobjektgrupp1: new UntypedFormControl(),
      anvandargrupp: new UntypedFormControl(),
      aktiva: new UntypedFormControl(),
      ejGranskade: new UntypedFormControl()
    });

    this.setInitialSearchForm();

    const storageValue = this.searchFormSettingsService.getStoredSearchForm();

    if (storageValue) {
      this.searchForm.patchValue(storageValue.form);
      this.selectedMatobjektgrupper = storageValue.selectedMatobjektgrupper;
    }

    this.matrundaService.getAll({onlyAktiva: true}).subscribe(result => {
      this.matrundor = result;
    });

    this.updateMatningstypMatobjektFilter();
    this.initAnvandargrupper();
    this.initNamn();
    this.initFastigheter();

    if (this.initialSelected) {
      this.initialSelected.forEach(id => this.selectedMatningstyp.add(id));
      this.emitSelectedMatningstyp();
    } else {
      const pagingAndSelection = new Set(this.searchFormSettingsService.getStoredMatningstypSelection());

      if (pagingAndSelection != null) {
        for (const id of pagingAndSelection) {
          this.selectedMatningstyp.add(id);
        }

        this.emitSelectedMatningstyp();
      }
    }
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
      aktiva: false,
      ejGranskade: this.filterEjGranskade
    });

    this.selectedMatobjektgrupper = [null, null];
  }

  ngAfterViewInit() {
    const storageFormPolygon = this.searchFormSettingsService.getStoredSearchPolygon();

    if (storageFormPolygon != null) {
      this.map.setSelectionPolygon(storageFormPolygon);
    }

    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    if (this.initialFilter) {
      this.matningstypMatobjektFilter = this.initialFilter;
      if (this.matningstypMatobjektFilter.includeIds) {
        this.matningstypMatobjektFilter.includeIds.forEach(id => this.selectedMatningstyp.add(id));
      }
    }

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        filter(() => this.matningstypMatobjektFilter != null),
        switchMap(() => {
          this.searching = true;
          return this.matobjektService.getMatningstypMatobjektPage(
            this.paginator.pageIndex,
            this.paginator.pageSize,
            this.sort.active,
            this.sort.direction,
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
      this.matningstypMatobjekt = data;
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
      let c = 0;
      let t = 0;
      let f = 0;

      for (const mt of mapinfo.matningstypIds) {
        c = c + 1;

        if (this.selectedMatningstyp.has(mt)) {
          t = t + 1;
        } else {
          f = f + 1;
        }
      }

      if (c === 0) {
        return null;
      } else if (c === t) {
        return true;
      } else if (c === f) {
        return false;
      } else {
        return null;
      }
    };
  }

  private getMatobjektIsSelectedChangeCallback() {
    return (mapinfo: MatobjektMapinfo, state) => {
      for (const mt of mapinfo.matningstypIds) {
        if (state) {
          this.selectedMatningstyp.add(mt);
        } else {
          this.selectedMatningstyp.delete(mt);
        }
      }

      if (this.showOnlySelected) {
        this.updateMatningstypMatobjektFilter();
        this.paginator.pageIndex = 0;
        this.paginator._changePageSize(this.paginator.pageSize);
      }
      this.emitSelectedMatningstyp();
    };
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
    if (this.userService.userDetails.isMatrapportor()) {
      this.anvandargruppService.getAll().subscribe(
        anvandargrupper => {
          this.anvandargrupper = anvandargrupper;
        });
    }
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

  onSokMatobjekt() {
    this.searchFormSettingsService.putStoredSearchForm(this.searchForm.value, this.selectedMatobjektgrupper);
    this.searchFormSettingsService.putStoredSearchPolygon(this.map.getSelectionPolygon());

    this.updateMatningstypMatobjektFilter();
    this.paginator.pageIndex = 0;
    this.paginator._changePageSize(this.paginator.pageSize);

    if (this.clearSelectedOnSokChange) {
      this.selectedMatningstyp.clear();
    }

    this.emitSelectedMatningstyp();
  }

  onRemoveSelection() {
    this.showOnlySelected = false;
    this.selectedMatningstyp.clear();
    this.updateMatningstypMatobjektFilter();
    this.paginator.pageIndex = 0;
    this.paginator._changePageSize(this.paginator.pageSize);
    this.emitSelectedMatningstyp();
    this.map.selectedMatobjektUpdated();
  }

  updateMatningstypMatobjektFilter() {
    const typ = this.searchForm.get("typ").value;
    const status = this.searchForm.get("ejGranskade").value;
    const matobjektgrupper = this.selectedMatobjektgrupper.filter(mg => mg != null).map(mg => mg.id);
    const anvandarGrupp = this.searchForm.get("anvandargrupp").value;

    if (this.showOnlySelected) {
      this.matningstypMatobjektFilter = {
        includeIds: Array.from(this.selectedMatningstyp),
      };
    } else {
      this.matningstypMatobjektFilter = {
        matobjektNamn: this.searchForm.get("namn").value,
        matobjektTyp: typ ? Object.keys(MatobjektTyp).indexOf(typ) : null,
        fastighet: this.searchForm.get("fastighet").value,
        matrunda: this.searchForm.get("matrunda").value,
        matobjektgrupper: matobjektgrupper.length ? matobjektgrupper : null,
        matansvarigAnvandargruppIds: anvandarGrupp ? [anvandarGrupp] : null,
        matningStatus: status ? 0 : null,
        matobjektIds: this.matobjektInArea ? this.matobjektInArea : null,
        onlyAktiva: this.searchForm.get("aktiva").value
      };
    }
  }

  toggleMatningstyp(id: number) {
    if (this.selectedMatningstyp.has(id)) {
      this.selectedMatningstyp.delete(id);
    } else {
      this.selectedMatningstyp.add(id);
    }

    this.emitSelectedMatningstyp();
    this.map.selectedMatobjektUpdated();
  }

  onSelectAllOnPage(event: MatCheckboxChange) {
    this.matningstypMatobjekt.forEach(mtmo => {
      if (event.checked) {
        this.selectedMatningstyp.add(mtmo.matningstypId);
      } else {
        this.selectedMatningstyp.delete(mtmo.matningstypId);
      }
    });

    this.emitSelectedMatningstyp();
    this.map.selectedMatobjektUpdated();
  }

  getNrSelected() {
    return this.selectedMatningstyp.size;
  }

  emitSelectedMatningstyp() {
    this.searchFormSettingsService.putStoredMatningstypSelection(Array.from(this.selectedMatningstyp));
    this.selected.emit(Array.from(this.selectedMatningstyp.values()));
  }

  getFeatures(formValues) {
    const typ = this.searchForm.get("typ").value;
    const status = this.searchForm.get("ejGranskade").value;
    const anvandarGrupp = this.searchForm.get("anvandargrupp").value;
    const matobjektgrupper = this.selectedMatobjektgrupper.filter(mg => mg != null).map(mg => mg.id);

    const mapFilter: MatningstypMatobjektFilter = {
      matobjektNamn: this.searchForm.get("namn").value,
      matobjektTyp: typ ? Object.keys(MatobjektTyp).indexOf(typ) : null,
      fastighet: this.searchForm.get("fastighet").value,
      matrunda: formValues.matrunda,
      matobjektgrupper: matobjektgrupper.length ? matobjektgrupper : null,
      matansvarigAnvandargruppIds: anvandarGrupp ? [anvandarGrupp] : null,
      matningStatus: status ? 0 : null,
      onlyAktiva: this.searchForm.get("aktiva").value
    };

    return this.matobjektService.getMatobjektMapinfo(mapFilter);
  }

  matobjektInAreaEvent(selected: number[]) {
    this.matobjektInArea = selected;
    this.onSokMatobjekt();
  }

  toogleShowOnlySelected($event: MatCheckboxChange) {
    this.showOnlySelected = $event.checked;
    this.updateMatningstypMatobjektFilter();
    this.paginator.pageIndex = 0;
    this.paginator._changePageSize(this.paginator.pageSize);
  }

  resetFilters() {
    this.setInitialSearchForm();
    this.map.clearAreaSelection();
  }
}
