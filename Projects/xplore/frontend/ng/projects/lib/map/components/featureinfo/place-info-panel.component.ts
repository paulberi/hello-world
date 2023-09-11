import {ChangeDetectionStrategy, ChangeDetectorRef, Component} from "@angular/core";

import {Observable} from "rxjs";

import {FeatureInfo} from "../../../map-core/feature-info.model";
import {map} from "rxjs/operators";
import {StyleService} from "../../services/style.service";
import {StateService} from "../../services/state.service";
import {ConfigService} from "../../../config/config.service";
import {MapService} from "../../../map-core/map.service";
import {FeatureInfoDisplayObject, TemplateProperty} from "./feature-info-panel.component";

/**
 * Visar upp attributinfo för objekt man klickat på i kartan.
 */
@Component({
  selector: "xp-place-info-panel",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ng-container *ngIf="fidos | async; let fidos">
      <ng-container *ngFor="let fido of fidos">
        <div class="mat-expansion-panel-header-inner">
          <div class="zoom-button" (click)="onZoom($event, fido)">
            <mat-icon color="primary" class="zoom-button zoom-button-icon toolbar-button">zoom_in</mat-icon>
          </div>
          <mat-panel-title>
            <div class="namn" [innerHTML]="fido.featureInfo.feature.get('name')"></div>
          </mat-panel-title>
        </div>
      </ng-container>
    </ng-container>
  `,
  styles: [`
    .zoom-button {
      cursor: pointer;
    }

    .zoom-button-icon {
      position: relative;
      top: -2px;
    }

    .namn {
      display: inline;
      padding-top: 3px;
    }

    .mat-expansion-panel-header-inner {
      width: 100%;
      display: flex;
      align-items: flex-start;
      margin-top: 2px;
    }

  `]
})
export class PlaceInfoPanelComponent {
  fidos: Observable<FeatureInfoDisplayObject[]>;
  layerEditing: boolean;

  constructor(private cdr: ChangeDetectorRef,
              private styleService: StyleService,
              private mapService: MapService,
              private configService: ConfigService,
              private stateService: StateService) {
    this.layerEditing = ConfigService.appConfig.layerEditing;
    this.fidos = this.stateService.uiStates.asObservable().pipe(
      map((uiStates) => this.toDisplayObjects(uiStates.valdaFeatures))
    );
  }

  private toDisplayObjects(featureInfos: FeatureInfo[]): FeatureInfoDisplayObject[] {
    return featureInfos
      .filter(f => f.layer.get("layerDef").id === "map_pin")
      .map(this.toDisplayObject.bind(this));
  }

  private toDisplayObject(featureInfo: FeatureInfo): FeatureInfoDisplayObject {
    let properties: TemplateProperty[];

    return {
      featureInfo: featureInfo,
      legendUrl: this.createLegend(featureInfo),
      properties: properties
    };
  }

  private createLegend(featureInfo: FeatureInfo): string {
    return this.styleService.createLegend(featureInfo.layer.get("layerDef").style, [20, 24]);
  }

  private onUpdated() {
    this.cdr.markForCheck();
  }

  onZoom(event: MouseEvent, fido: FeatureInfoDisplayObject) {
    event.stopPropagation();

    const extent = fido.featureInfo.feature.getGeometry().getExtent();
    this.mapService.zoomToFit(extent, this.configService.config.zoomToFitMinResolution);
  }
}
