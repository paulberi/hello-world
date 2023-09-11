import {AfterViewInit, Component, OnInit, ViewChild} from "@angular/core";
import {UntypedFormControl, UntypedFormGroup} from "@angular/forms";
import {Larm} from "../services/larm.service";
import {MatningstypMatobjektFilter, MatobjektMapinfo, MatobjektService} from "../services/matobjekt.service";
import {ListMatobjektgrupp, MatobjektgruppService} from "../services/matobjektgrupp.service";
import {Anvandargrupp, AnvandargruppService} from "../services/anvandargrupp.service";
import {merge, Observable, of} from "rxjs";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {Paminnelse, PaminnelseService} from "../services/paminnelse.service";
import { MatobjektTyp } from "../matobjekt/matobjekt-typ";
import {catchError, debounceTime, distinctUntilChanged, filter, flatMap, map, startWith, switchMap} from "rxjs/operators";
import {MdbMapComponent} from "../common/components/mdb-map.component";
import {MdbDialogService} from "../services/mdb-dialog.service";
import {ListMatrunda, MatrundaService} from "../services/matrunda.service";
import {SearchFormSettingsService} from "../services/search-form-settings.service";

@Component({
  selector: "mdb-paminnelser",
  template: `
    <div class="main-content">
      <h2>Visa påminnelser</h2>
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
              <mat-form-field>
                <select matNativeControl placeholder="Mätansvarig" formControlName="anvandargrupp">
                  <option></option>
                  <option *ngFor="let ag of anvandargrupper" [ngValue]="ag.id">{{ag.namn}}</option>
                </select>
              </mat-form-field>
              <div class="checkboxes">
                <mat-checkbox formControlName="onlyForsenade">Endast sena mätningar</mat-checkbox>
              </div>
              <div>
                <a href="javascript:void(0)" (click)="resetFilters()"><mat-icon style="vertical-align: text-bottom">clear_back</mat-icon> Rensa filtrering</a>
              </div>
            </div>
          </form>
        </div>
        <mdb-map class="mapSpace" #map [fullscreen]="false" (matobjektInArea)="matobjektInAreaEvent($event)"
                 [selection]="false"
                 [selectMatobjektInArea]="true"
                 [rapporteraLink]="false"
        ></mdb-map>
      </div>


      <div class="search-result">
        <table mat-table [dataSource]="paminnelser" matSort matSortActive="matobjektNamn" matSortDisableClear
               matSortDirection="asc">
          <ng-container matColumnDef="matobjektNamn">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Namn</th>
            <td mat-cell *matCellDef="let p"><a routerLink="/matobjekt/{{p.matobjektId}}">{{p.matobjektNamn}}</a></td>
          </ng-container>
          <ng-container matColumnDef="matningstypNamn">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Mätningstyp</th>
            <td mat-cell *matCellDef="let p">{{p.matningstypNamn}}</td>
          </ng-container>
          <ng-container matColumnDef="matobjektFastighet">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Fastighet</th>
            <td mat-cell *matCellDef="let p">{{p.matobjektFastighet}}</td>
          </ng-container>
          <ng-container matColumnDef="matobjektLage">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Läge</th>
            <td mat-cell *matCellDef="let p">{{p.matobjektLage}}</td>
          </ng-container>
          <ng-container matColumnDef="avlastDatum">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Senaste mätning</th>
            <td mat-cell *matCellDef="let p">{{formatDatum(p.avlastDatum)}}</td>
          </ng-container>
          <ng-container matColumnDef="tidsenhet">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Intervall</th>
            <td mat-cell *matCellDef="let p">{{p.antalGanger}} ggr/{{tidsenhetIntervall[p.tidsenhet]}}</td>
          </ng-container>
          <ng-container matColumnDef="forsenad">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Försenad</th>
            <td mat-cell *matCellDef="let p">{{p.forsenadDagar}} dagar</td>
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
        height: 30em;
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

    .search-result {
      display: grid;
      grid-gap: 0;
    }

    .mat-column-matobjektNamn, .mat-column-matningstypNamn, .mat-column-matobjektFastighet {
      width: 10%;
    }

    .mat-column-matobjektLage {
      width: auto;
    }

    .mat-column-tidsenhet, .mat-column-forsenad, .mat-column-avlastDatum {
      width: 10%;
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
export class PaminnelserComponent implements OnInit, AfterViewInit {
  public MatobjektTyp = MatobjektTyp;

  searchForm: UntypedFormGroup;
  searching = true;

  columns: string[] = ["matobjektNamn", "matningstypNamn", "matobjektFastighet",
    "matobjektLage", "avlastDatum", "tidsenhet", "forsenad"];
  resultsLength = 0;
  paminnelser: Paminnelse[] = [];
  matningstypMatobjektFilter: MatningstypMatobjektFilter = {};

  activeMatobjektgrupp: number = null;
  selectedMatobjektgrupper: ListMatobjektgrupp[] = [null, null];

  anvandargrupper: Anvandargrupp[] = [];
  matrundor: ListMatrunda[];
  matobjektnamn: string[] = [];
  filteredMatobjektnamn: Observable<string[]>;
  fastigheter: string[] = [];
  filteredFastigheter: Observable<string[]>;
  tidsenhetIntervall: {[key: number]: string} = {0: "tim", 1: "dag", 2: "vecka", 3: "mån", 4: "år"};

  matobjektInArea: number[];

  @ViewChild(MdbMapComponent, {static: true}) map: MdbMapComponent;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  constructor(private matobjektService: MatobjektService, private paminnelseService: PaminnelseService,
              public popupService: MdbDialogService,
              public matrundaService: MatrundaService,
              public searchFormSettingsService: SearchFormSettingsService,
              private matobjektgruppService: MatobjektgruppService, private anvandargruppService: AnvandargruppService) { }

  ngOnInit() {
    this.searchForm = new UntypedFormGroup({
      namn: new UntypedFormControl(),
      typ: new UntypedFormControl(""),
      fastighet: new UntypedFormControl(),
      matrunda: new UntypedFormControl(),
      matobjektgrupp0: new UntypedFormControl(),
      matobjektgrupp1: new UntypedFormControl(),
      anvandargrupp: new UntypedFormControl(),
      onlyForsenade: new UntypedFormControl()
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

    this.initAnvandargrupper();
    this.initNamn();
    this.initFastigheter();
  }

  ngAfterViewInit(): void {
    const storageFormPolygon = this.searchFormSettingsService.getStoredSearchPolygon();

    if (storageFormPolygon != null) {
      this.map.setSelectionPolygon(storageFormPolygon);
    }

    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        filter(() => this.matningstypMatobjektFilter != null),
        switchMap(() => {
          this.searching = true;
          return this.paminnelseService.getPaminnelsePage(
            this.paginator.pageIndex,
            this.paginator.pageSize,
            this.sort.active,
            this.sort.direction,
            this.searchForm.get("onlyForsenade").value,
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
      this.paminnelser = data;
      this.searching = false;
    });

    this.searchForm.valueChanges.pipe(distinctUntilChanged(), debounceTime(500)).pipe(switchMap(val => {
      return this.getFeatures(val);
    })).subscribe(
      result => {
        this.map.clearMatobjekt();
        this.map.setMatobjekt(result, false);
      });

    this.getFeatures(this.searchForm.getRawValue()).subscribe(
      result => {
        this.map.clearMatobjekt();
        this.map.setMatobjekt(result, false);
      });

    this.searchForm.valueChanges.pipe(distinctUntilChanged(), debounceTime(500)).subscribe(val => {
      this.onSokPaminnelser();
    });
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

  onSokPaminnelser() {
    this.searchFormSettingsService.putStoredSearchForm(this.searchForm.value, this.selectedMatobjektgrupper);
    this.searchFormSettingsService.putStoredSearchPolygon(this.map.getSelectionPolygon());

    this.updateMatningstypMatobjektFilter();
    this.paginator.pageIndex = 0;
    this.paginator._changePageSize(this.paginator.pageSize);
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
      matansvarigAnvandargruppIds: anvandarGrupp ? [this.searchForm.get("anvandargrupp").value] : null,
      matobjektIds: this.matobjektInArea ? this.matobjektInArea : null
    };
  }

  formatDatum(datum: string): string {
    return datum ? datum.replace("T", " ").substring(0, datum.lastIndexOf(":")) : null;
  }

  getFeatures(formValues) {
    const typ = this.searchForm.get("typ").value;
    const anvandarGrupp = this.searchForm.get("anvandargrupp").value;
    const matobjektgrupper = this.selectedMatobjektgrupper.filter(mg => mg != null).map(mg => mg.id);

    const mapFilter: MatningstypMatobjektFilter = {
      matobjektNamn: this.searchForm.get("namn").value,
      matobjektTyp: typ ? Object.keys(MatobjektTyp).indexOf(typ) : null,
      fastighet: this.searchForm.get("fastighet").value,
      matrunda: formValues.matrunda,
      matobjektgrupper: matobjektgrupper.length ? matobjektgrupper : null,
      matansvarigAnvandargruppIds: anvandarGrupp ? [anvandarGrupp] : null,
    };

    return this.matobjektService.getMatobjektMapinfo(mapFilter);
  }

  matobjektInAreaEvent(selected: number[]) {
    this.matobjektInArea = selected;
    this.onSokPaminnelser();
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
      onlyForsenade: true,
    });

    this.selectedMatobjektgrupper = [null, null];
  }

  resetFilters() {
    this.setInitialSearchForm();
    this.map.clearAreaSelection();
  }
}
