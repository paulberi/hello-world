import {Component, OnDestroy} from "@angular/core";

import Feature from "ol/Feature";
import GeoJSON from "ol/format/GeoJSON";

import {StateService} from "../../services/state.service";
import {Subject} from "rxjs";
import {catchError, switchMap} from "rxjs/operators";
import {SelectionService} from "../../services/selection.service";
import {SpatialPredicate} from "../../services/sok.service";
import {ConfigService} from "../../../config/config.service";

@Component({
  selector: "xp-geometri-sok-panel",
  template: `
    <ng-container *ngIf="selectedFeatures.length > 0" class="container">
      <mat-expansion-panel class="mat-expansion-panel-no-padding expander mat-elevation-z">
        <mat-expansion-panel-header collapsedHeight="30px" expandedHeight="30px" class="expander-header">
          Buffertfunktion
        </mat-expansion-panel-header>
        <div class="panel-body">
          <mat-form-field class="item-with-margin narrow-input">
            <input type="number" matInput placeholder="Buffer (meter)" [(ngModel)]="bufferDistance" [min]="0" [max]="100"
                   (input)="evaluateErrors()">
          </mat-form-field>
          <mat-checkbox [(ngModel)]="within">Helt inom</mat-checkbox>
          <div class="button-container">
            <button class="item-with-margin" mat-stroked-button (click)="selectWithGeometries()"
                    [disabled]="error">Utför
            </button>
          </div>
        </div>
        <div *ngIf="error" class="error-message">
          {{error}}
        </div>
      </mat-expansion-panel>
    </ng-container>
  `,
  styles: [`
    .expander {
      background: inherit;
    }
    .item-with-margin {
      margin: 0 10px 0 0;
    }
    .narrow-input {
      width: 100px;
    }
    .button-container {
      display: inline-block;
      float: right;
    }
    .error-message {
      color: red;
      text-align: center;
    }
  `]
})
export class GeometriSokPanelComponent implements OnDestroy {
  private geoJsonReader = new GeoJSON();

  maxBuffer: number;
  bufferDistance = 0;
  within = false;

  selectedFeatures: Feature[] = [];
  selectionSubject = new Subject<void>();

  error: string;

  constructor(private stateService: StateService,
              private configService: ConfigService,
              private selectionService: SelectionService) {
    this.maxBuffer = this.selectionService.maxBuffer;

    this.stateService.uiStates.subscribe(uiStates => {
        const delomraden = uiStates.valdaDelomraden.map(d => this.geoJsonReader.readFeature(
          d,
          {dataProjection: "EPSG:3006", featureProjection: this.configService.config.projectionCode}));
        const features = uiStates.valdaFeatures.map(f => f.feature);
        this.selectedFeatures = delomraden.concat(features);
        this.evaluateErrors();
      });

    this.selectionSubject.pipe(
      switchMap(() => {
        return this.selectionService.selectFeaturesByPolygon(this.selectedFeatures, this.getPredicate(), this.bufferDistance).pipe(
          catchError(() => {
            // We don't care about errors here.
            return null;
          })
        );
      })
    ).subscribe();
  }

  private getPredicate() {
    if (this.within) {
      return SpatialPredicate.WITHIN;
    } else {
      return SpatialPredicate.INTERSECTS;
    }
  }

  selectWithGeometries() {
    this.selectionSubject.next();
  }

  ngOnDestroy() {
    this.selectionSubject.unsubscribe();
  }

  evaluateErrors() {
    if (this.selectedFeatures.length > 20) {
      this.error = "För många valda delområden (max 20 st)";
    } else if (this.bufferDistance === null || this.bufferDistance < 0 || this.bufferDistance > this.maxBuffer) {
      this.error = "Ogiltigt värde valt för buffer (0 - " + this.maxBuffer + ")";
    } else {
      this.error = null;
    }
  }
}
