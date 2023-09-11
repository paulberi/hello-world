import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  Input, OnChanges,
  SimpleChanges,
  ViewChild
} from "@angular/core";
import {LayerDef} from "../../../config/config.service";
import {StyleService} from "../../services/style.service";

/**
 * Representerar legendsymbolen för enskilda lager.
 */
@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-layer-panel-legend",
  template: `
    <img *ngIf="legendSrc" width="20" height="20" [src]="legendSrc"/>
  `,
  styles: [`
    img {
      position: relative;
      bottom: 2px;
      vertical-align: middle;
      margin-right: 5px;
    }
  `]
})
export class LayerPanelLegendComponent implements OnChanges {
  @Input() layer: LayerDef;
  @ViewChild("legendCanvas") canvas: ElementRef;

  legendSrc: string;

  constructor(private styleService: StyleService) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes["layer"]) {
      this.legendSrc = this.styleService.createLegend(this.layer.style, [20, 20]);
    }
  }
}
