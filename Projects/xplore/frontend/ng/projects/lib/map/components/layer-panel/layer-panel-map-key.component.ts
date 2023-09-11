import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input, OnChanges,
  OnInit,
  Output, SimpleChanges
} from "@angular/core";
import {ConfigService, LayerDef} from "../../../config/config.service";
import {LayerService} from "../../../map-core/layer.service";
import {Subscription} from "rxjs";
import TileWMS from "ol/source/TileWMS";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-layer-panel-map-key",
  template: `
    <div *ngFor="let layer of layersWithMapKey">
      <div *ngIf="!layer.mapKey?.title">{{layer.title}}</div>
      <div *ngIf="layer.mapKey?.title">{{layer.mapKey.title}}</div>


      <div class="map-key">
        <ng-container *ngIf="!layer.mapKey?.staticKey">
          <div *ngFor="let legendUrl of legendUrl.get(layer.id)">
            <div *ngIf="debug" class="debug">{{legendUrl}}</div>
            <img [src]="legendUrl | secureLoadImage | async"/>
          </div>
        </ng-container>

        <ng-container *ngIf="layer.mapKey?.staticKey">
          <table>
            <tr *ngFor="let item of layer.mapKey.staticKey">
              <td class="map-key-text map-key-code">{{item.code}}</td>
              <td>
                <div class="map-key-item" [style.background]="item.color"></div>
              </td>
              <td class="map-key-text">{{item.description}}</td>
            </tr>
          </table>

        </ng-container>
      </div>
    </div>
    `,
    styles: [`
      .map-key {
        margin-left: 10px;
        overflow-y: auto;
      }

      .map-key-item {
        width: 15px;
        height: 15px;
      }

      .map-key-code {
        width: 25px;
      }

      .map-key-text {
        font-size: 12px;
      }

      .debug {
        font-size: 9px;
        word-wrap: anywhere;
      }
    `]
})
export class LayerPanelMapKeyComponent implements OnChanges, OnInit {
  @Input() layers: LayerDef[];

  private layerSubscription: Subscription;
  public layersWithMapKey: LayerDef[];
  public legendUrl: Map<string, string[]> = new Map<string, string[]>();
  public debug = false;

  constructor(private cdr: ChangeDetectorRef, private layerService: LayerService,
              public configService: ConfigService) {

    if (new URLSearchParams(window.location.search).get("debug")) {
      this.debug = true;
    }
  }

  ngOnInit(): void {
    this.layersWithMapKey = this.layers.filter(layer => layer.visible).reverse();

    this.layerSubscription = this.layerService.getLayerChange().subscribe(
      () => {
        const newLayersWithMapKey: LayerDef[] = [];

        for (const layer of this.layers) {
          let show = false;

          if (layer.visible) {
            if (layer.mapKey?.visible === true) {
              show = true;
            }

            if (layer.mapKey?.visible == null) {
              // Om lagret har en stil så ritas den ut direkt i lagerlistan, default är på
              // för lager som inte har en stil
              if (!layer.style) {
                show = true;
              }
            }
          }

          if (show === true) {
            if (!layer.mapKey?.staticKey) {
              this.legendUrl.set(layer.id, this.getLegendUrl(layer));
            }

            newLayersWithMapKey.push(layer);
          }
        }
        this.layersWithMapKey = newLayersWithMapKey.reverse();
        this.cdr.markForCheck();
      });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes["layers"]) {
      this.layersWithMapKey = this.layers.filter(layer => layer.visible).reverse();
    }
  }

  public getLegendUrl(layerDef: LayerDef) {
    const layer = this.layerService.findLayer(layerDef.id);

    if (!layer) {
      return null;
    }

    const layerSource = layer.getSource();

    if (layerSource instanceof TileWMS) {
      let legendOptions = layerDef.mapKey?.legendOptions;

      if (!legendOptions) {
        if (layerDef.source.type === "tilewms") {
          if (this.configService.config.app.theme) {
            const theme = this.configService.config.app.theme;

            if (layerDef.source.serverType === "geoserver") {
              if (theme === "metria-dark-theme") {
                legendOptions = "fontName:SansSerif;fontAntiAliasing:false;fontColor:0xffffff;bgColor:0x303030";
              } else {
                legendOptions = "fontName:SansSerif;fontAntiAliasing:true;bgColor:0xfafafa";
              }
            }
          }
        }
      }

      let legendUrl: string;

      if (layerDef.mapKey?.resolutionDependent === undefined || layerDef.mapKey?.resolutionDependent === true) {
        legendUrl = layerSource.getLegendUrl(
          layerDef.__currentResolution,
          {
            legend_options: legendOptions
          });
      } else {
        legendUrl = layerSource.getLegendUrl(
          undefined,
          {
            legend_options: legendOptions
          });
      }

      const url = new URL(legendUrl, window.location.origin);

      let layers;
      let styles;
      if ("params" in layerDef.source) {
         layers = layerDef.source.params.LAYERS.split(",");

        const stylesParam = layerDef.source.params.STYLES;

        if (stylesParam) {
          styles = stylesParam.split(",");
        } else {
          styles = [];
        }
      }

      const legendUrlArray = [];
      const origin = window.location.origin;

      for (let i = 0; i < layers.length; i++) {
        const newUrl = new URL(legendUrl, origin);
        newUrl.searchParams.set("LAYER", layers[i]);

        const style = styles[i];

        if (style !== undefined && style !== "") {
          newUrl.searchParams.set("STYLE", style);
        } else {
          newUrl.searchParams.delete("STYLE");
        }

        let newUrlString = newUrl.toString();

        if (newUrl.origin === origin ) { // Ta bort origin så att vi får en relativ url igen, funkar bättre med oauth
          newUrlString = newUrlString.slice(origin.length);
        }

        legendUrlArray.push(newUrlString);
      }

      return legendUrlArray;
    }

    return null;
  }
}


