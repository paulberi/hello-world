import { Component, Input, OnInit } from "@angular/core";
import Layer from "ol/layer/Layer";
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import { Observable, of } from "rxjs";
import { Extent } from "ol/extent";
import { SrMapService } from "../../services/map.service";

@Component({
  selector: "sr-projektkarta",
  templateUrl: "./projektkarta.container.html",
})
export class SrProjektkartaContainerComponent implements OnInit {
  @Input() projektId: string;
  
  backgroundLayers: Layer[];
  mapExtent$: Observable<Extent>;

  private buffertLayer: VectorLayer<any>;
  private highlightLayer: VectorLayer<any>;
  private intrangLayer: Layer;

  constructor(private mapService: SrMapService) {}

  ngOnInit() {
    this.buffertLayer = new VectorLayer({ source: new VectorSource() });
    this.highlightLayer = new VectorLayer({ source: new VectorSource() });
    this.backgroundLayers = this.mapService.getBackgroundLayers();
    this.intrangLayer = this.mapService.createLayer(this.mapService.intrangLayer(this.projektId, true));
    this.mapService.getGeoJSON(this.projektId).subscribe((geoJSON: GeoJSON.FeatureCollection<GeoJSON.Geometry>) => {
      const extents = [];
      geoJSON.bbox.map((extent) => extents.push(extent));
      this.mapExtent$ = of(extents);
    })
  }

  clearHighlights() {
    this.highlightLayer.getSource().clear();
  }

  get layers(): Layer[] {
    const layers: Layer[] = [
      this.buffertLayer, 
      this.highlightLayer,
      this.intrangLayer
    ];
    return layers;
  }
}
