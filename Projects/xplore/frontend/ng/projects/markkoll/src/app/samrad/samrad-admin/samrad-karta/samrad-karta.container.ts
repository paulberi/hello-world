import { Component, Input, OnInit } from '@angular/core';
import { Extent } from 'ol/extent';
import Layer from 'ol/layer/Layer';
import { Observable } from 'rxjs';
import { uuid } from '../../../model/uuid';
import { MkMapService } from '../../../services/map.service';
import { ProjektService } from '../../../services/projekt.service';

@Component({
  selector: 'mk-samrad-karta',
  templateUrl: './samrad-karta.container.html'
})
export class SamradKartaContainerComponent implements OnInit {

  @Input() projektId : uuid;

  backgroundLayers: Layer[];
  mapExtent$: Observable<Extent>;
  private intrangLayer: Layer;

  constructor(private mapService: MkMapService,
    private projektService: ProjektService) {
  }

  ngOnInit(): void {
    this.backgroundLayers = this.mapService.getSamradAdminBackgroundLayers();
    this.mapExtent$ = this.mapService.getProjektExtent(this.projektId);
    this.intrangLayer = this.mapService.createLayer(this.mapService.intrangLayer(this.projektId, true));
  }
  get layers(): Layer[] {
    const layers: Layer[] = [
      this.intrangLayer
    ];
    return layers;
  }

}
