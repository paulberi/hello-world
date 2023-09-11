import {OnInit, AfterViewInit, Component, Inject, LOCALE_ID, NgZone, OnDestroy, TemplateRef, ViewChild} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {EJ_GRANSKAD, FEL, GODKANT, Matning, MatobjektService, ReviewMatning} from "../services/matobjekt.service";
import {formatNumber, Location} from "@angular/common";
import {MatningarGraf} from "./matningar-graf";
import {DataSerie, GranskningDataService} from "./granskning-data.service";
import {Subject} from "rxjs";
import {debounceTime, distinctUntilChanged, map} from "rxjs/operators";
import {UserService} from "../services/user.service";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {formatDate} from "@angular/common";
import {RestError} from "../matningar/granska-matning/granska-matning.component";
import {UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";

/**
 * Huvudsidan för granskning i graf.
 *
 * Binder samma events mellan olika inställingsformulär / dialoger och grafen i sig.
 */
@Component({
  selector: "mdb-granskning",
  template: `
    <h2>Granska mätvärden</h2>

    <form>
      <mat-form-field>
        <mat-label>Tidsintervall</mat-label>
        <select matNativeControl (change)="onIntervalChange($event)">
          <option value="6m">Senaste 6 månaderna</option>
          <option value="1y">Senaste året</option>
          <option value="2y">Senaste 2 åren</option>
          <option selected value="5y">Senaste 5 åren</option>
          <option value="10y">Senaste 10 åren</option>
          <option value="">Sedan start</option>
        </select>
      </mat-form-field>
      <mat-form-field>
        <input matInput type="date" (change)="dateChanges.next($event.target)"
               placeholder="Sätt referensdatum för rörelse">
      </mat-form-field>
      <mat-checkbox name="onlyGodkanda" [(ngModel)]="graf == null ? false : granskningDataService.onlyGodkanda"
                    *ngIf="userService.userDetails.isMatrapportor()">
        Visa endast godkända mätvärden
      </mat-checkbox>
      <mat-checkbox name="onlyGodkanda" [(ngModel)]="graf == null ? false : granskningDataService.onlyFelkodOk"
                    *ngIf="userService.userDetails.isMatrapportor()">
        Visa endast mätvärden utan felkod
      </mat-checkbox>
      <button *ngIf="showUpdateButton" mat-stroked-button color="primary" class="header-button" (click)="onUppdateraGraf()">Uppdatera</button>
    </form>

    <div class="chart">
      <mdb-granskning-graf-setup #grafPopup
                                 [(scrollbarXEnabled)]="graf == null ? true : graf.scrollbarXEnabled"
                                 [(scrollbarYEnabled)]="graf == null ? true : graf.scrollbarYEnabled"
                                 [(connectLines)]="graf == null ? true : graf.connectLines"
                                 [(reuseAxes)]="graf == null ? true : graf.reuseAxes">
      </mdb-granskning-graf-setup>
      <mdb-granskning-data-setup #dataPopup
                                 (matningstypEnabled)="graf.showMatningstyp($event)"
                                 (matningstypDisabled)="graf.hideMatningstyp($event)"
                                 (referensdataEnabled)="graf.showReferensdata($event)"
                                 (referensdataDisabled)="graf.hideReferensData($event)"
                                 (referensvardeAdded)="granskningDataService.addReferensdataForMatobjekt($event)"
                                 (gransvardeEnabled)="graf.showGransvarde($event)"
                                 (gransvardeDisabled)="graf.hideGransvarde($event)">
      </mdb-granskning-data-setup>
      <div id="chartdiv"></div>
      <div>
        <button (click)="grafPopup.visible = true" class="settings-button" mat-stroked-button color="primary">
          <mat-icon>settings</mat-icon>
          <br>Anpassa graf
        </button>
        <button (click)="dataPopup.visible = true" class="settings-button" mat-stroked-button color="primary">
          <mat-icon>multiline_chart</mat-icon>
          <br>Mätserier
        </button>
      </div>
    </div>
    <div class="actions">
      <button type="button" mat-stroked-button color="primary" (click)="goBack()">Tillbaka</button>
      <mdb-save-button *ngIf="userService.userDetails.isTillstandshandlaggare()"
                       [disabled]="disableGodkann" [label]="'Godkänn'" (clicked)="godkann()" [saving]="saving">
      </mdb-save-button>
    </div>

    <ng-template #godkannTemplate>
      <h2 mat-dialog-title>Godkänn mätvärden</h2>

      <div mat-dialog-content>
        <div *ngFor="let ejGranskadeSerie of ejGranskadeSeries">
          <h3>
            <mat-checkbox (change)="ejGranskadeSerie.selected = $event.checked"
                          [checked]="ejGranskadeSerie.selected">
              {{ejGranskadeSerie.serie.matobjektNamn}} - {{ejGranskadeSerie.serie.matningstypNamn}}
              ({{ejGranskadeSerie.ejGranskadeDataOk.length}} ok, {{ejGranskadeSerie.ejGranskadeDataFel.length}} fel)
            </mat-checkbox>
          </h3>
        </div>
      </div>
      <div mat-dialog-actions align="center">
        <button mat-button (click)="onGodkannDialogClose()">Avbryt</button>
        <button mat-raised-button color="warn" (click)="onGodkannDialogSave()">Godkänn</button>
      </div>
    </ng-template>

    <ng-template #granskaVardeTemplate>
      <h2 mat-dialog-title>{{ejGranskatVarde.serie.matobjektNamn}} - {{ejGranskatVarde.serie.matningstypNamn}} </h2>
      <div mat-dialog-content>
        <h3>
          <mat-label>Datum: </mat-label>{{ejGranskatVarde.datum}}<br>
          <mat-label>Status: </mat-label>{{ejGranskatVarde.status}}<br>
          <div *ngIf="giltigtVarde(ejGranskatVarde.felkod)">
            <mat-label>Värde: </mat-label>{{ejGranskatVarde.varde}}<br>
          </div>
          <div *ngIf="!giltigtVarde(ejGranskatVarde.felkod)">
            <mat-label>Felkod: </mat-label>{{ejGranskatVarde.felkod}}<br>
          </div>
        </h3>
      </div>
      <div mat-dialog-actions align="center">
        <button mat-stroked-button (click)="onGranskaVardeClose()">Avbryt</button>
        <button mat-stroked-button [disabled]="!canReject" (click)="onGrandskaVardeReject()">Felmarkera</button>
        <button mat-raised-button color="primary" [disabled]="!canApprove" (click)="onGranskaVardeApprove()">Godkänn</button>
      </div>
    </ng-template>
    <ng-template #setupAxisTemplate>
      <h2 mat-dialog-title>Konfigurera axel: {{axisSettings.label}} </h2>
      <div mat-dialog-content>
        <form id="axis-form" [formGroup]="axisForm" (submit)="onSetupAxisSave()">
          <mat-form-field class="axis-field" appearance="fill">
            <mat-label>Min</mat-label>
            <input matInput type="decimal-number" required formControlName="axisMin" maxlength="10">
            <mat-error>Ange ett giltigt decimaltal.</mat-error>
          </mat-form-field>
          <mat-form-field class="axis-field" appearance="fill">
            <mat-label>Max</mat-label>
            <input matInput type="decimal-number" required formControlName="axisMax" maxlength="10">
            <mat-error>Ange ett giltigt decimaltal.</mat-error>
          </mat-form-field>
          <div mat-dialog-actions align="center">
            <button mat-stroked-button (click)="onSetupAxisClose()">Avbryt</button>
            <button mat-raised-button color="primary" type="submit" [disabled]="!axisForm.valid" (click)="onSetupAxisSave()">OK</button>
          </div>
        </form>
      </div>
    </ng-template>
  `,
  styles: [`
    :host {
      display: grid;
      grid-gap: 0.5rem;
    }

    form > mat-form-field {
      margin-right: 0.5rem;
    }

    #axis-form {
      min-height: 140px;
    }

    .chart {
      position: relative;
      display: grid;
      grid-template-columns: auto 4rem;
      row-gap: 0;
      column-gap: 0.5rem;
    }

    .settings-button {
      width: 5rem;
      padding: 0.3rem;
      margin-top: 1.5rem;
      white-space: normal;
      line-height: normal;
    }

    .header-button {
      padding: 0.3rem;
      margin-left: 1.0rem;
    }

    mat-checkbox {
      padding: 0.3rem;
    }

    #chartdiv {
      width: 100%;
      height: calc(max(100vh - 300px, 600px))
    }
  `],
  providers: [GranskningDataService]
})
export class GranskningGrafComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild("godkannTemplate") godkannTemplate: TemplateRef<any>;
  @ViewChild("granskaVardeTemplate") granskaVardeTemplate: TemplateRef<any>;
  @ViewChild("setupAxisTemplate") setupAxisTemplate: TemplateRef<any>;

  axisForm: UntypedFormGroup;
  axisSettings: { axis: any, label: string, min: number, max: number };

  disableGodkann = true;
  saving = false;
  graf: MatningarGraf;

  dateChanges = new Subject();

  ejGranskadeSeries: { serie: DataSerie; ejGranskadeDataOk: any[]; ejGranskadeDataFel: any[]; selected?: boolean}[];
  ejGranskatVarde: { serie: DataSerie; matningsId: number; datum: string; status: string, varde: string, felkod: string };

  canApprove: boolean;
  canReject: boolean;

  statusNames: { [status: number]: string } = { 0: "Ej granskad", 1: "Godkänt", 2: "Fel" };

  showUpdateButton = false;

  private godkannDialogRef: MatDialogRef<any, any>;
  private granskaVardeDialogRef: MatDialogRef<any, any>;
  private setupAxisDialogRef: MatDialogRef<any, any>;
  private readonly legendMaxHeight = 150;

  constructor(@Inject(LOCALE_ID) private locale: string,
              private route: ActivatedRoute,
              private location: Location,
              private zone: NgZone,
              private matobjektService: MatobjektService,
              private formBuilder: UntypedFormBuilder,
              public userService: UserService,
              public granskningDataService: GranskningDataService,
              public dialog: MatDialog) {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get("matningstypIds");
      const matningstypIds = idParam.split(",").map(s => parseInt(s, 10));

      if (!userService.userDetails.isMatrapportor()) {
        granskningDataService.onlyGodkanda = true;
      }

      this.setInterval("5y");

      granskningDataService.fetchMatningstyper(matningstypIds).then(() => {
        granskningDataService.fetchDefaultReferensdata();
      });

      granskningDataService.matningstyperUpdated.subscribe((series: DataSerie[]) => {
        series.forEach(serie => {
          this.graf.addMatningstyp(serie);
        });
        this.disableGodkann = granskningDataService.ejGranskadeIds.length === 0;
      });

      /**
       * We need to redraw the graph here because of a problem with bullets disappearing.
       */
      granskningDataService.gransvardenUpdated.subscribe(gransvarden => {
        gransvarden.forEach(g => {
          if (g.aktiv) {
            this.graf.addGransvarde(g)
          }
        });
        this.graf.redraw();
      });

      granskningDataService.referensdataUpdated.subscribe((series: DataSerie[]) => {
        series.forEach(serie => this.graf.addReferensdata(serie));
      });

      this.dateChanges.pipe(
       debounceTime(500),
       map((target: HTMLInputElement) => target.valueAsDate),
       distinctUntilChanged()
      ).subscribe(date => {
        this.granskningDataService.sattningReferensDatum = date;
      });
    });
  }

  ngOnInit() {
    this.axisForm = this.formBuilder.group({
      axisMin: ["", [ Validators.required] ],
      axisMax: ["", [ Validators.required] ]
    });
 }

  ngAfterViewInit(): void {
    this.zone.runOutsideAngular(() => {
      this.graf = new MatningarGraf("chartdiv", this.legendMaxHeight);
      /* If a series visibility is changed from within the graph ui
       * we need to update the state "within" angular since the graph
       * is running "outside" of angular for performance reasons.
       *
       * The "series" property refers to the data used by the graph
       * data setup UI.
       */
      this.graf.seriesToggled.subscribe(ev => {
        this.zone.run(() => ev.series.visible = ev.visible);
      });
      this.graf.dataPointClicked.subscribe(ev => {
        this.zone.run(() => {
          this.granskaVarde(ev.series, ev.dataPoint);
        });
      });
      this.graf.valueAxisClicked.subscribe(ev => {
        this.zone.run(() => {
          this.configureAxis(ev.axis);
        });
      });
    });
  }

  ngOnDestroy() {
    this.zone.runOutsideAngular(() => {
      if (this.graf) {
        this.graf.dispose();
      }
    });
  }

  goBack() {
    this.location.back();
  }

  godkann() {
    const ejGranskadeSeries = this.granskningDataService.getEjGranskade();

    this.ejGranskadeSeries = [];

    for (const s of ejGranskadeSeries) {
      const serie: { serie: DataSerie; ejGranskadeDataOk: any[]; ejGranskadeDataFel: any[]; selected?: boolean} = s;

      if (serie.serie.visible) {
        serie.selected = true;
        this.ejGranskadeSeries.push(serie);
      }
    }

    this.godkannDialogRef = this.dialog.open(this.godkannTemplate, {
      maxWidth: "600px"
    });
  }

  onGodkannDialogClose() {
    this.godkannDialogRef.close();
  }

  onGodkannDialogSave() {
    this.godkannDialogRef.close();
    this.saving = true;

    const ejGranskadeIds = [];

    for (const serie of this.ejGranskadeSeries) {
      if (serie.selected) {
        for (const d of serie.ejGranskadeDataOk) {
          ejGranskadeIds.push(d.id);
        }

        for (const d of serie.ejGranskadeDataFel) {
          ejGranskadeIds.push(d.id);
        }
      }
    }

    if (ejGranskadeIds.length > 0) {
      this.matobjektService.godkann(ejGranskadeIds).subscribe(() => {
        this.granskningDataService.refetchMatningstyper();
        this.saving = false;
      }, () => this.saving = false);
    } else {
      this.saving = false;
    }
  }

  granskaVarde(series: DataSerie, dataPoint: any) {
    const formateratVarde = this.formateraVarde(dataPoint.value, series.decimaler, this.getEnhet(series));
    this.ejGranskatVarde = {
      serie: series,
      matningsId: dataPoint.id,
      datum: this.formateraDatum(dataPoint.date),
      status: this.statusNames[dataPoint.status],
      varde: formateratVarde,
      felkod: dataPoint.felkod
    };
    this.canApprove = dataPoint.status !== GODKANT;
    this.canReject = dataPoint.status !== FEL;
    this.granskaVardeDialogRef = this.dialog.open(this.granskaVardeTemplate, {
      maxWidth: "600px"
    });
  }

  onGranskaVardeClose() {
    this.granskaVardeDialogRef.close();
  }

  onGranskaVardeApprove() {
    this.granskaVardeDialogRef.close();
    const review: ReviewMatning = { status: GODKANT, avlastVarde: null, kommentar: null, operation: null };
    this.reviewMatning(this.ejGranskatVarde.serie, this.ejGranskatVarde.matningsId, review);
  }

  onGrandskaVardeReject() {
    this.granskaVardeDialogRef.close();
    const review: ReviewMatning = { status: FEL, avlastVarde: null, kommentar: null, operation: null };
    this.reviewMatning(this.ejGranskatVarde.serie, this.ejGranskatVarde.matningsId, review);
  }

  reviewMatning(serie: DataSerie, matningId: number, review: ReviewMatning) {
    this.matobjektService.reviewMatning(serie.matobjektId, serie.matningstypId, matningId, review).subscribe(
      reviewed => this.handleReviewDone(reviewed),
      () => this.handleReviewError(matningId, review)
    );
  }

  handleReviewError(matningId: number, reviewMatning: ReviewMatning) {
    this.granskningDataService.refetchMatningstyper();
  }

  handleReviewDone(matning: Matning) {
    this.granskningDataService.refetchMatningstyper();
  }

  configureAxis(axis: any) {
    this.axisSettings = {
      axis: axis,
      label: axis.title.text,
      min: axis.min,
      max: axis.max
    };

    this.axisForm.get("axisMin").setValue(axis.min);
    this.axisForm.get("axisMax").setValue(axis.max);

    this.setupAxisDialogRef = this.dialog.open(this.setupAxisTemplate, {
      maxWidth: "600px"
    });
  }

  onSetupAxisClose() {
    this.setupAxisDialogRef.close();
  }

  onSetupAxisSave() {
    const min = this.axisForm.get("axisMin").value;
    const max = this.axisForm.get("axisMax").value;
    this.setupAxisDialogRef.close();
    this.graf.setValueAxisRange(this.axisSettings.axis, min, max);
  }

  onIntervalChange(event) {
    this.setInterval(event.target.value);
  }

  onUppdateraGraf() {
    this.graf.redraw();
  }

  setInterval(interval: string) {
    let date = new Date();
    switch (interval) {
      case "6m":
        date.setMonth(date.getMonth() - 6);
        break;
      case "1y":
        date.setFullYear(date.getFullYear() - 1);
        break;
      case "2y":
        date.setFullYear(date.getFullYear() - 2);
        break;
      case "5y":
        date.setFullYear(date.getFullYear() - 5);
        break;
      case "10y":
        date.setFullYear(date.getFullYear() - 10);
        break;
      default:
        date = null;
    }
    this.granskningDataService.fromDatum = date;
  }

  getEnhet(serie: DataSerie) {
    if (serie.beraknadEnhet != null) {
      return serie.beraknadEnhet;
    } else {
      return serie.enhet;
    }
  }

  giltigtVarde(felkod: string) {
    return (felkod == null || felkod === "Ok");
  }

  formateraVarde(varde: number, decimaler: number, enhet: string) {
    const digitsInfo = "1.0-" + decimaler;
    return formatNumber(varde, "sv-SE", digitsInfo) + " " + enhet;
  }

  formateraDatum(datum: Date): string {
    return formatDate(datum, "yyyy-MM-dd HH:mm", this.locale);
  }
}

