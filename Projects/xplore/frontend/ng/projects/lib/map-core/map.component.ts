import {Component, ElementRef, Input, OnDestroy, OnInit, ViewChild, ViewContainerRef} from "@angular/core";

import {MapService} from "./map.service";
import {LayerService} from "./layer.service";
import {ConfigService, LayerDef} from "../config/config.service";
import {ViewService} from "./view.service";
import {BehaviorSubject} from "rxjs";

@Component({
  selector: "xp-map",
  template: `
    <div #mapElement id="my-map" [ngStyle]="{'background-color': backgroundColor}">
      <div #mapOverlay></div>
      <ng-content></ng-content>
    </div>
  `,
  styles: [`
    #my-map {
      height: 100%;
      position: relative;
    }
  `]
})
export class MapComponent implements OnInit, OnDestroy {
  @Input() fitPadding: BehaviorSubject<[number, number, number, number]>;

  backgroundColor = "#626262";
  @ViewChild("mapOverlay", { read: ViewContainerRef}) mapOverlay: ViewContainerRef;
  @ViewChild("mapElement", { read: ElementRef,  static: true}) mapElement: ElementRef;

  constructor(private mapService: MapService, private viewService: ViewService,
              private layerService: LayerService, private configService: ConfigService) {
  }

  ngOnInit() {
    this.mapService.setTarget(this.mapElement);

    if (this.configService.config.backgroundColor) {
      this.backgroundColor = this.configService.config.backgroundColor;
    }

    if (this.fitPadding) {
      this.fitPadding.subscribe(fitPadding => {
        this.mapService.setFitPadding(fitPadding);
      });
    }
  }

  ngOnDestroy(): void {
    this.mapService.setTarget(null);
  }

  onLayerChange(layerDef: LayerDef) {
    this.layerService.updateLayer(layerDef);
  }

  setCenter(coordinate) {
    this.viewService.view.setCenter(coordinate);
  }

  setZoom(zoom) {
    this.viewService.view.setZoom(zoom);
  }

  updateSize() {
    this.mapService.map.updateSize();
  }
}
