import {Component} from "@angular/core";
import {LayerService} from "../../../map-core/layer.service";
import {LayerDef} from "../../../config/config.service";

/**
 * En panel med knappar för varje definierat bakgrundslager. Varje knapp har en thumbnail som motsvarar bakgrundskartans
 * utseende. Thumbnailen hämtas med en statiskt definierat BBOX som valts ut för att ge ett representativt utseende.
 */
@Component({
  selector: "xp-background-buttons-panel",
  template: `
    <ng-container *ngFor="let layer of layers">
      <xp-background-button (layerChange)="onClick($event)" [layer]="layer"></xp-background-button>
    </ng-container>
  `,
  styles: [`
    :host {
      display: flex;
      flex-direction: row;
    }
  `]
})
export class BackgroundButtonsPanelComponent {
  layers: LayerDef[] = [];

  constructor(private layerService: LayerService) {
    this.getBackgroundLayers();
    this.layerService.layers.getLayers().on("add", () => {
      this.getBackgroundLayers()
    });
  }

  private getBackgroundLayers() {
    this.layerService.layers.getLayers().forEach(layer => {
      const layerDef = layer.get("layerDef");
      if (layerDef.group === "background") {
        if (this.layers.indexOf(layerDef) === -1) {
          this.layers.push(layerDef);
        }
      }
    });
  }

  onClick(layer: LayerDef) {
    this.layers.forEach(layer => {
      layer.visible = false;
      this.layerService.updateLayer(layer);
    });

    layer.visible = true;
    this.layerService.updateLayer(layer);
  }
}
