import {ChangeDetectionStrategy, ChangeDetectorRef, Component} from "@angular/core";

import {Observable} from "rxjs";

import {FeatureInfo} from "../../../map-core/feature-info.model";
import {map} from "rxjs/operators";
import {StyleService} from "../../services/style.service";
import {StateService} from "../../services/state.service";
import {ConfigService} from "../../../config/config.service";
import {WfsSource} from "../../../map-core/wfs.source";

export interface FeatureInfoDisplayObject {
  featureInfo: FeatureInfo;
  legendUrl?: string;
  properties?: TemplateProperty[];
}

export interface TemplateProperty {
  id?: string;
  name?: string;
  label?: string;
  template?: string;
  type?: string;
  minLength?: number;
  maxLength?: number;
  linkRef?: string;
  hideLabel?: boolean;
  inputType?: string;
}

/**
 * Visar upp attributinfo för objekt man klickat på i kartan.
 */
@Component({
  selector: "xp-feature-info-panel",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ng-container *ngIf="fidos | async; let fidos">
      <ng-container *ngFor="let fido of fidos; let i = index;">
        <xp-feature-info-read-panel *ngIf="!isEditing(fido); else editMode" [fido]="fido"
                                    [prevFido]="i > 0 ? fidos[i-1]: null"
                                    [layerEditing]="isEditableLayer(fido)"></xp-feature-info-read-panel>
        <ng-template #editMode>
          <xp-feature-info-edit-panel [fido]="fido" (updated)="onUpdated()"></xp-feature-info-edit-panel>
        </ng-template>
      </ng-container>
    </ng-container>
  `,
  styles: [`
  `]
})
export class FeatureInfoPanelComponent {
  fidos: Observable<FeatureInfoDisplayObject[]>;
  layerEditing: boolean;

  constructor(private cdr: ChangeDetectorRef, private styleService: StyleService, private stateService: StateService) {
    this.layerEditing = ConfigService.appConfig.layerEditing;
    this.fidos = this.stateService.uiStates.asObservable().pipe(
      map((uiStates) => this.toDisplayObjects(uiStates.valdaFeatures))
    );
  }

  private toDisplayObjects(featureInfos: FeatureInfo[]): FeatureInfoDisplayObject[] {
    return featureInfos
      .filter(f => f.layer.get("layerDef").id !== "map_pin")
      .map(this.toDisplayObject.bind(this));
  }

  private toDisplayObject(featureInfo: FeatureInfo): FeatureInfoDisplayObject {
    let properties: TemplateProperty[];

    const template = featureInfo.layer.get("layerDef").infoTemplate;
    if (template) {
      properties = template.properties;
    }

    return {
      featureInfo: featureInfo,
      legendUrl: this.createLegend(featureInfo),
      properties: properties
    };
  }

  private createLegend(featureInfo: FeatureInfo): string {
    return this.styleService.createLegend(featureInfo.layer.get("layerDef").style, [20, 24]);
  }

  onUpdated() {
    this.cdr.markForCheck();
  }

  isEditing(fido: FeatureInfoDisplayObject): boolean {
    return this.layerEditing && fido.featureInfo.feature
      && (<any> fido.featureInfo.feature).isEditing
      && (<any> fido.featureInfo.feature).isEditing();
  }

  isEditableLayer(fido: FeatureInfoDisplayObject): boolean {
    const layer = fido.featureInfo.layer;
    return this.layerEditing && layer.getSource() instanceof WfsSource &&
      (layer.get("layerDef").hasOwnProperty("editable") ? layer.get("layerDef").editable : true);
  }
}

