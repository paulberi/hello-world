import {Component, HostListener, Input, OnDestroy, OnInit, ViewChild} from "@angular/core";
import {UntypedFormControl, FormGroup} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {ListMatrunda, MatrundaService} from "../../services/matrunda.service";
import {UserService} from "../../services/user.service";
import {getLocalDateISOString, momentDateToISOString} from "../../common/date-utils";
import {MapService} from "../../../../../lib/map-core/map.service";
import {LayerService} from "../../../../../lib/map-core/layer.service";
import {Matning, MatningstypMatobjektFilter, MatobjektMapinfo, MatobjektService} from "../../services/matobjekt.service";
import {auditTime, catchError, distinctUntilChanged, switchMap, takeUntil} from "rxjs/operators";
import {MdbMapComponent} from "../../common/components/mdb-map.component";
import moment from "moment";
import {isTouch} from "../../common/touch-utils";
import {UNSAVED_CHANGES} from "../../services/can-deactivate-guard.service";
import {FormGroupUnsaved} from "../../common/form-group-unsaved";
import {RapporteringMatningstyperComponent} from "./rapportering-matningstyper.component";
import {Observable, interval, of, Subject} from "rxjs";
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import {saveAs} from "file-saver";
import {GeoJSON} from "ol/format";
import Polygon from "ol/geom/Polygon";

@Component({
  selector: "mdb-rapportering-matdata",
  template: `
    <h2>Rapportera mätdata</h2>
    <div class="headerSpace">
      <div class="formSpace">

        <form [formGroup]="form">
          <mat-form-field *ngIf="!matobjektId">
            <select matNativeControl placeholder="Mätrunda" required formControlName="matrunda" (change)="load()">
              <option></option>
              <option *ngFor="let m of matrundor" [value]="m.id">{{m.namn}} ({{m.antalMatobjekt}} st)</option>
            </select>
          </mat-form-field>
          <mat-form-field>
            <mat-label>Startdatum</mat-label>
            <input matInput placeholder="åååå-mm-dd" required [matDatepicker]="startDatumDatepicker"
                formControlName="startDate" (dateChange)="load()">
            <mat-datepicker-toggle matSuffix [for]="startDatumDatepicker"></mat-datepicker-toggle>
            <mat-datepicker [touchUi]="isTouch" #startDatumDatepicker></mat-datepicker>
          </mat-form-field>
          <mat-form-field>
            <input autocomplete="off" matInput type="text" placeholder="Mätorganisation" required
                formControlName="matorganisation" (change)="load()">
          </mat-form-field>

<!--          Notera samma knappar längre ner-->
          <div class="actions actionsBeforeMap">
            <button type="button" mat-raised-button color="primary" (click)="toggleList()" [disabled]=form.invalid>
              {{visibleList && !loadedMatobjektId ? "Dölj lista": "Visa i lista"}}
            </button>
            <a type="button" mat-stroked-button *ngIf="!matobjektId" color="primary"
               href="javascript:void(0)" (click)="downloadFile(faltprotokollUrl)"
               [disabled]="form.invalid">Ladda ner fältprotokoll</a>
          </div>

          <mat-checkbox [checked]="allObjectsInMap" (change)="allaMatobjekt()">Visa alla mätobjekt i kartan</mat-checkbox>
        </form>
      </div>

      <mdb-map class="mapSpace" #map
               [fullscreen]="false" [selection]="true"
               [selectMatobjektInArea]="true"
               [selectInMap]="false"
               (matobjektInArea)="matobjektInAreaEvent($event)"
               rapporteraLink="true"></mdb-map>

<!--      Notera samma knappar högre upp -->
      <div class="actions actionsAfterMap">
        <button type="button" mat-raised-button color="primary" (click)="load()" [disabled]=form.invalid>Visa i lista</button>
        <a type="button" mat-stroked-button *ngIf="!matobjektId" color="primary"
           href="javascript:void(0)" (click)="downloadFile(faltprotokollUrl)"
           [disabled]="form.invalid">Ladda ner fältprotokoll</a>
      </div>
    </div>

    <mdb-rapportering-matningstyper *ngIf="visibleList || loadedMatobjektId"
                                    [matobjektId]="loadedMatobjektId"
                                    [matrundaId]="loadedMatrundaId"
                                    [startDate]="loadedStartdatum"
                                    [rapportor]="loadedRapportor"
                                    [matobjektInArea]="matobjektInArea"
                                    (isDirty) = "setDirty($event)"
    ></mdb-rapportering-matningstyper>
  `,
  styles: [`
    form {
      display: grid;
      grid-gap: 1rem;
    }

    .headerSpace {
      /*margin-bottom: 2rem;*/
    }

    .actionsBeforeMap {
      display: none;
    }

    .actionsAfterMap {
      margin-top: 1rem;
    }

    @media only screen and (min-width: 930px) {
      .actionsBeforeMap {
        display: grid;
      }

      .actionsAfterMap {
        display: none;
      }

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
  `],
  providers: [MapService]
})

export class RapporteringMatdataComponent implements OnInit, OnDestroy {
  form: FormGroupUnsaved;
  matrundor: ListMatrunda[];
  matobjektId: number;
  public allObjectsInMap = false;
  hasDirtyMatningar = false;

  private ngUnsubscribe = new Subject<void>();
  private updateEjRapporterade = new Subject<void>();

  matobjektInArea?: number[];

  loadedMatobjektId: number;
  loadedMatrundaId: number;
  loadedStartdatum: string;
  loadedRapportor: string;

  visibleList: boolean = false;

  @ViewChild(MdbMapComponent, {static: true}) map: MdbMapComponent;
  @ViewChild(RapporteringMatningstyperComponent) matningstyper: RapporteringMatningstyperComponent;

  isTouch = isTouch();

  currentDate = getLocalDateISOString();
  private rapporterade: Set<number>;
  private matobjektMapinfo: MatobjektMapinfo[];

  constructor(private route: ActivatedRoute,
              private router: Router,
              userService: UserService,
              private httpClient: HttpClient,
              private layerService: LayerService,
              private matobjektService: MatobjektService,
              private matrundaService: MatrundaService) {

    this.form = new FormGroupUnsaved({
      matrunda: new UntypedFormControl(),
      startDate: new UntypedFormControl(this.currentDate),
      matorganisation: new UntypedFormControl(userService.userDetails.company)
    });

    matrundaService.getAll({onlyAktiva: true}).subscribe(result => {
      this.matrundor = result;
    });
  }

  toggleList() {
    this.visibleList = !this.visibleList;
  }

  setDirty(isDirty: boolean) {
    this.hasDirtyMatningar = isDirty;
  }

  canDeactivate(): Observable<boolean> | boolean {
    if (!this.hasDirtyMatningar || confirm(UNSAVED_CHANGES)) {
      if (this.matningstyper) {
        this.matningstyper.resetDirtyState();
      }
      return true;
    } else {
      return false;
    }
  }

  get faltprotokollUrl() {
    const matrundaId = this.form.controls.matrunda.value;
    if (this.isStartDateSet()) {
      const startDate = momentDateToISOString(this.form.controls.startDate.value);
      return "/api/matrunda/" + matrundaId + "/faltprotokoll.xlsx" + "?startDate=" + startDate;
    } else {
      return "/api/matrunda/" + matrundaId + "/faltprotokoll.xlsx";
    }
  }

  load() {
    if (this.matobjektId !== null) {
      if (this.isStartDateSet()) {
        const startDate = momentDateToISOString(this.form.controls.startDate.value);
        const rapportor = this.form.controls.matorganisation.value;
        this.router.navigate([{matobjekt: this.matobjektId, startDate: startDate, rapportor: rapportor}], {relativeTo: this.route});
      } else {
        this.router.navigate([{matobjekt: this.matobjektId}], {relativeTo: this.route});
      }
    } else {
      const matrundaId = this.form.controls.matrunda.value;

      const navCommand: any = {matrunda: matrundaId};

      if (this.isStartDateSet()) {
        const startDate = momentDateToISOString(this.form.controls.startDate.value);
        const rapportor = this.form.controls.matorganisation.value;

        navCommand.startDate = startDate;
        navCommand.rapportor = rapportor;
      }

      const selectedPolygon = this.map.getSelectionPolygon();

      if (selectedPolygon != null) {
        const parser = new GeoJSON();
        const geojson = parser.writeGeometryObject(selectedPolygon);

        navCommand.selectedArea = JSON.stringify(geojson);
      }

      this.router.navigate([navCommand], {relativeTo: this.route});
    }
  }

  private isStartDateSet() {
    const startDate = this.form.controls.startDate.value;
    return startDate != null && startDate !== "";
  }

  ngOnInit(): void {
    this.updateEjRapporterade.pipe(auditTime(100)).pipe(switchMap(val => {
      if (document.hidden) {
        return of(null);
      }

      const matrundaId = this.form.get("matrunda").value;
      if (matrundaId) {
        return this.matrundaService.getSenasteMatningar(matrundaId).pipe(
          catchError(err => of(null))
        );
      } else {
        this.rapporterade = null;
        return of(null);
      }
    })).pipe(takeUntil(this.ngUnsubscribe)).subscribe(matningMap => {
      if (matningMap == null) {
        return;
      }

      this.rapporterade = new Set();

      const startDate = momentDateToISOString(this.form.controls.startDate.value);

      for (const key of Object.keys(matningMap)) {
        const matning: Matning = matningMap[key];

        if (matning !== null) {
          const ejRapporterad = matning.avlastDatum < startDate;

          if (!ejRapporterad) {
            this.rapporterade.add(matning.matningstypId);
          }
        }
      }

      this.setDataInMap(this.matobjektMapinfo, false);
    });


    /*
     * When the "load"-method is called, we open a child route with parameters taken from
     * the form on this page. Conversely, if we reload the page with the child route already
     * open, we load the parameters into the form. This is what happens below.
     */
    this.route.paramMap.subscribe(params => {
      const matrundaId = +params.get("matrunda");
      this.form.controls.matrunda.setValue(matrundaId);
      this.loadedMatrundaId = matrundaId;

      const matobjektIdString = params.get("matobjekt");

      if (matobjektIdString == null) {
        this.matobjektId = null;
      } else {
        this.matobjektId = +matobjektIdString;
      }

      this.loadedMatobjektId = this.matobjektId;

      let startDate = params.get("startDate");

      if (!startDate) {
        startDate = this.currentDate;
      }

      this.form.controls.startDate.setValue(moment(startDate));
      this.loadedStartdatum = startDate;

      const matorganistaion = params.get("rapportor");

      if (matorganistaion) {
        this.form.controls.matorganisation.setValue(matorganistaion);
      }
      this.loadedRapportor = this.form.controls.matorganisation.value;

      const selectedArea = params.get("selectedArea");

      if (selectedArea) {
        const parser = new GeoJSON();
        const polygon = parser.readGeometry(JSON.parse(selectedArea));
        this.map.setSelectionPolygon(<Polygon>polygon);
      }

      this.updateMap(true);
    });

    this.form.get("matrunda").valueChanges.pipe(distinctUntilChanged(), takeUntil(this.ngUnsubscribe)).subscribe(val => {
      this.matobjektId = null;
      this.allObjectsInMap = false;
      this.rapporterade = null;
      this.updateMap(true);
    });

    this.form.get("startDate").valueChanges.pipe(distinctUntilChanged(), takeUntil(this.ngUnsubscribe)).subscribe(val => {
      this.updateEjRapporterade.next();
    });

    interval(60 * 1000).pipe(takeUntil(this.ngUnsubscribe)).subscribe(() => {
      this.updateEjRapporterade.next();
    });

    this.matobjektService.matningstypIdUpdated.pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((matningstypId) => {
        this.updateEjRapporterade.next();
      }
    );
  }

  ngOnDestroy(): void {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }

  updateMap(fitExtent: boolean) {
    const matrundaId = this.form.get("matrunda").value;
    const matobjektId = this.matobjektId;

    if (matrundaId || matobjektId || this.allObjectsInMap) {
      let filter: MatningstypMatobjektFilter;

      if (this.allObjectsInMap) {
        filter = {
          excludeAutomatiska: true
        };
      } else if (matobjektId) {
        filter = {
          matobjektIds: matobjektId ? [matobjektId] : null,
          onlyAktiva: true,
          excludeAutomatiska: true
        };
      } else {
        filter = {
          matrunda: matrundaId,
          onlyAktiva: true,
          excludeAutomatiska: true
        };
      }

      this.matobjektService.getMatobjektMapinfo(filter).subscribe(
        result => {
          this.matobjektMapinfo = result;
          this.setDataInMap(result, fitExtent);
        });

      if (matrundaId) {
        this.updateEjRapporterade.next();
      }
    } else {
      if (this.map) {
        this.map.clearMatobjekt();
      }
    }
  }

  private setDataInMap(result: MatobjektMapinfo[], fitExtent: boolean) {
    this.map.setMatobjekt(result, false, fitExtent, (mapinfo: MatobjektMapinfo) => {
      if (this.rapporterade == null) {
        return true;
      }

      let ejRapporterad = true;

      for (const matningstypId of mapinfo.matningstypIds) {
        if (this.rapporterade.has(matningstypId)) {
          ejRapporterad = false;
        }
      }

      return ejRapporterad;
    });
  }

  allaMatobjekt() {
    this.allObjectsInMap = !this.allObjectsInMap;

    this.updateMap(false);
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

  @HostListener("document:visibilitychange", ["$event"])
  visibilitychange() {
    if (document.hidden) {
      // Do nothing
    } else {
      this.updateEjRapporterade.next();
    }
  }

  matobjektInAreaEvent(selected: number[]) {
    this.matobjektInArea = selected;
  }
}

