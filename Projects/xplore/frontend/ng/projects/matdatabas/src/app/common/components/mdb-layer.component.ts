import {Component, EventEmitter, Input, Output} from "@angular/core";
import {MatCheckboxChange} from "@angular/material/checkbox";
import {getExtentFromLayer} from "../../../../../lib/map-core/layer.util";
import VectorLayer from "ol/layer/Vector";
import LayerGroup from "ol/layer/Group";
import {Extent} from "ol/extent";

export interface ZoomRequestEvent {
  extent: Extent;
}

@Component({
  selector: "mdb-layer",
  template: `
    <div class="layer-row">
      <span *ngIf="canZoom(layer)" (click)="onZoomRequest(layer)" class="material-icons zoom-button">zoom_in</span>
      <h4 *ngIf="isGroupLayer">{{layer.get('label')}}</h4>
      <mat-checkbox *ngIf="!isGroupLayer" [checked]="layer.getVisible()" (change)="onLayerCheckChange(layer, $event)">
        {{layer.get('label')}}
      </mat-checkbox>
    </div>
  `,
  styles: [`
    h4 {
      margin-bottom: 0 !important;
    }
    .layer-row {
      display: flex;
      align-items: center;
    }

    .zoom-button {
      font-size: 18px;
      margin-right: 5px;
      cursor: pointer;
    }
  `]
})
export class MdbLayerComponent {
  @Input() layer: any;
  @Input() isGroupLayer = false;
  @Output() zoomRequest = new EventEmitter<ZoomRequestEvent>();

  onLayerCheckChange(layer, event: MatCheckboxChange) {
    layer.setVisible(event.checked);
  }

  canZoom(layer) {
    return layer instanceof VectorLayer || layer instanceof LayerGroup;
  }

  onZoomRequest(layer) {
    this.zoomRequest.emit({extent: getExtentFromLayer(layer)});
  }
}

