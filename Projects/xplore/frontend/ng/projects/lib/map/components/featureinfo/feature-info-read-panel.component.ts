import {ChangeDetectionStrategy, Component, Input, OnInit} from "@angular/core";

import {FeatureInfo} from "../../../map-core/feature-info.model";
import {StateService} from "../../services/state.service";
import {EnvironmentConfigService} from "../../../config/environment-config.service";
import {escapeHtml} from "../../../util/escapeHtml.util";
import {MapService} from "../../../map-core/map.service";
import {ConfigService} from "../../../config/config.service";

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
  decimals?: number;
  minLength?: number;
  maxLength?: number;
  linkRef?: string;
  hideLabel?: boolean;
  inputType?: string;
  visible?: boolean;
  adaptAttributeValue?: AdaptAttributeValue[];
  replacements?: object;
}

// Is used to replace some characters of the string "attributeName" with other characters.
export interface AdaptAttributeValue {
  attributeName: string;
  previousChar: string[];
  newChar: string[];
}

/**
 * Visar upp attributinfo för objekt man klickat på i kartan.
 */
@Component({
  selector: "xp-feature-info-read-panel",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="header">
      <img *ngIf="fido.legendUrl" [src]="fido.legendUrl">
      <ng-container>
        <h3 [innerHTML]="getCalculatedTitleHtml()"></h3>
      </ng-container>
      <ng-container *ngIf="layerEditing">
        <button mat-icon-button (click)="edit()" matTooltip="Redigera" matTooltipShowDelay="1000">
          <mat-icon>edit</mat-icon>
        </button>
      </ng-container>
    </div>
    <button mat-icon-button [disabled]="zoomDisabled(fido)" class="zoom-button toolbar-button" (click)="onZoom($event, fido)">
      <mat-icon>zoom_in</mat-icon>
    </button>

    <table class="prop-table">
      <ng-container *ngIf="fido.properties as properties">
        <ng-container *ngFor="let prop of properties">
          <ng-container *ngIf="prop.name ==='*'">
            <ng-container *ngFor="let propKey of getFeaturePropertiesKeysForDisplay(fido)">
              <tr *ngIf="getFeaturePropertieForDisplay(propKey) !== null">
                <td class="prop-label">{{propKey}}:</td>
                <td class="prop-value">{{getFeaturePropertieForDisplay(propKey)}}</td>
              </tr>
            </ng-container>
          </ng-container>
          <tr *ngIf="displayProp(fido, prop)">
            <td class="prop-label" *ngIf="prop.label && !prop.hideLabel">{{prop.label}}:</td>
            <td class="prop-value" [attr.colspan]="prop.label && !prop.hideLabel ? 1 : 2" [innerHTML]="getDisplayValue(fido, prop)"></td>
          </tr>
        </ng-container>
      </ng-container>
    </table>
  `,
  styles: [`
    .header {
      display: flex;
      align-items: center;
    }

    .header h3 {
      margin: 0;
      width: 100%;
      word-break: break-all;
    }

    .header img {
      margin: 0 5px 0 0;
    }

    .prop-table {
      border-spacing: 0;
      margin: 5px 0 25px 0;
    }

    .prop-table td {
      vertical-align: top;
      margin: 5px 0 5px 0;
    }

    .prop-label {
      padding-right: 5px;
    }

    .prop-value {
      width: 100%;
      white-space: pre-wrap;
      word-wrap: break-word;
      word-break:break-word;
    }

    .zoom-button {
      height: 20px !important;
      width: 20px !important;
      min-height: 20px !important;
      min-width: 20px !important;
      font-size: 20px !important;
      margin-right: 2px;
      float: left;
    }

    button:disabled.zoom-button  {
      opacity: 0.2;
    }
  `]
})
export class FeatureInfoReadPanelComponent {
  @Input() fido: FeatureInfoDisplayObject;
  @Input() prevFido: FeatureInfoDisplayObject;
  @Input() layerEditing: boolean;

  private yesNo = {"true": "Ja", "false": "Nej"};

  constructor(public stateService: StateService,
              private environmentConfigService: EnvironmentConfigService,
              private configService: ConfigService,
              private mapService: MapService) {
  }

  getCalculatedTitleHtml() {
    const titleHtml = this.getTitleHtml(this.fido);
    const prevTitle = this.prevFido == null ? null : this.getTitleHtml(this.prevFido);

    if (prevTitle && prevTitle === titleHtml) {
      return null;
    } else {
      return titleHtml;
    }
  }

  public getDisplayValue(fido: FeatureInfoDisplayObject, property: TemplateProperty) {
    let displayValue: string;
    if (property.template) {
      displayValue = this.handleTemplate(fido, property);
    } else {
      displayValue = escapeHtml(fido.featureInfo.feature.get(property.name));
    }

    if (typeof displayValue === "string") {
      displayValue = displayValue.trim();
      // användare vet ej vad null betyder, bättre lämna tomt
      if (displayValue === "Null") {
        displayValue = "";
      }
    }

    switch (property.type) {
      case "title":
        // Add layer title if title property is missing
        displayValue = displayValue ? displayValue : escapeHtml(fido.featureInfo.layer.get("layerDef").title);
        break;
      case "date":
        // Remove hours/minute/seconds
        displayValue = displayValue.slice(0, 10);
        break;
      case "style":
        const style = JSON.parse(fido.featureInfo.feature.get(property.name));
        if (style.type === "Text") {
          displayValue = escapeHtml(style.style.text.text);
        } else {
          displayValue = "";
        }
        break;
      case "boolean":
        displayValue = this.yesNo[displayValue];
        break;
      case "replace":
        if (property.replacements && property.replacements.hasOwnProperty(displayValue)) {
          displayValue = property.replacements[displayValue];
        }
    }

    if (property.linkRef) {
      const link = this.getPropertyLink(fido, property);
      if (link) {
        // Create link only if link value has been set
        displayValue = `<a target='_blank' href='${link}'>${displayValue}</a>`;
      }
    }

    return displayValue;
  }

  private handleTemplate(fido: FeatureInfoDisplayObject, property: TemplateProperty) {
    return property.template.replace(/{{([^{}]+)}}/g, (match, name) => {
      let value;
      if (name.split(".")[0] === "env") {
        value = this.environmentConfigService.getConfig()[name.split(".")[1]];
      } else {
        value = escapeHtml(fido.featureInfo.feature.get(name));
      }
      // This code replaces some characters with other characters.
      // previousChars of adaptAttributeValue will be replaced with newChars in order.
      if (property.adaptAttributeValue) {
        for (let k = 0; k < property.adaptAttributeValue.length; k++) {
          if (property.adaptAttributeValue[k].attributeName
            && property.adaptAttributeValue[k].previousChar
            && property.adaptAttributeValue[k].newChar) {
            if (property.adaptAttributeValue[k].attributeName === name) {
              if (property.adaptAttributeValue[k].previousChar.length === property.adaptAttributeValue[k].newChar.length) {
                for (let i = 0; i < property.adaptAttributeValue[k].previousChar.length; i++) {
                  value = value.split(property.adaptAttributeValue[k].previousChar[i]).join(property.adaptAttributeValue[k].newChar[i]);
                }
              } else {
                console.log("The number of elements of adaptAttributeValue.previousChar and adaptAttributeValue.newChar in appConfig must be equal!");
              }
            }
          } else {
            console.log("adaptAttributeValue in appConfig is not complete!");
          }
        }
      }

      if (property.decimals) {
        value = value.toFixed(property.decimals);
      }

      return value ? value : "";
    });
  }

  private getPropertyLink(fido: FeatureInfoDisplayObject, property: TemplateProperty) {
    let link: string;

    if (property.linkRef) {
      const linkProperty = fido.properties.find(p => p.type === "url" && p.id === property.linkRef);
      if (linkProperty && this.hasValue(fido, linkProperty)) {
        if (linkProperty.template) {
          link = this.handleTemplate(fido, linkProperty);
        } else {
          link = escapeHtml(fido.featureInfo.feature.get(linkProperty.name));
        }
      }
    }

    return link ? this.formatLink(link) : link;
  }

  private formatLink(link: string) {
    if (!link) {
      return null;
    }

    if (link.startsWith("http://") || link.startsWith("https://")) {
      return link;
    } else if (!link.startsWith("/")) {
      return "http://" + link;
    } else {
      return link;
    }
  }

  public getTitleHtml(fido: FeatureInfoDisplayObject) {
    const titleProperty = this.getTitleProperty(fido);

    if (titleProperty) {
      return this.getDisplayValue(fido, titleProperty);
    } else {
      return "";
    }
  }

  public getTitleProperty(fido: FeatureInfoDisplayObject): TemplateProperty {
    if (!fido.properties) {
      return {type: "title"};
    }

    const titleProperty = fido.properties.find(p => p.type === "title");
    if (titleProperty) {
      return titleProperty;
    } else {
      return {type: "title"};
    }
  }

  edit() {
    (<any> this.fido.featureInfo.feature).startEdit();
  }

  private hasValue(fido: FeatureInfoDisplayObject, property: TemplateProperty): boolean {
    const value = fido.featureInfo.feature.get(property.name);
    return typeof value === "string" ? value.trim() : value;
  }

  getFeaturePropertiesKeysForDisplay(fido: FeatureInfoDisplayObject): string[] {
    if (this.fido.featureInfo == null || this.fido.featureInfo.feature == null) {
      return [];
    }

    const keys: string[] = [];

    for (const key of Object.keys(this.fido.featureInfo.feature.getProperties())) {
      switch (key) {
        case "geometry":
        case "bbox":
          break;

        default:
          keys.push(key);
      }
    }

    return keys;
  }

  getFeaturePropertieForDisplay(key) {
    if (this.fido.properties && this.fido.properties.find(p => p.name === key)) {
      return null;
    }

    const value = this.fido.featureInfo.feature.getProperties()[key];

    if (value == null) {
      return "";
    }

    return value.toString().trim();
  }

  displayProp(fido: FeatureInfoDisplayObject, prop: TemplateProperty) {
    if (prop.visible === false) {
      return false;
    }

    if (prop.name === "*") {
      return false;
    }

    if (!this.hasValue(fido, prop)) {
      return false;
    }

    switch (prop.type) {
      case "title":
      case "url":
        return false;
    }

    return true;
  }

  onZoom(event: MouseEvent, fido: FeatureInfoDisplayObject) {
    event.stopPropagation();

    const extent = fido.featureInfo.feature.getGeometry().getExtent();

    this.mapService.zoomToFit(extent, this.configService.config.zoomToFitMinResolution);
  }

  zoomDisabled(fido: FeatureInfoDisplayObject) {
    if (fido.featureInfo && fido.featureInfo.feature && fido.featureInfo.feature.getGeometry()) {
      return false;
    } else {
      return true;
    }
  }
}

