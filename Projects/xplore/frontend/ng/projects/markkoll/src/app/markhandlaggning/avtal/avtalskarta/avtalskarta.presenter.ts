import { Injectable } from "@angular/core";
import { Extent } from "ol/extent";
import { fromExtent } from "ol/geom/Polygon";
import Layer from "ol/layer/Layer";
import WKT from "ol/format/WKT";
import { LayerDef } from "../../../../../../lib/config/config.service";
import { environment } from "../../../../environments/environment";
import { MkAvtalMap } from "../../../model/avtalskarta";
import { MkMapService } from "../../../services/map.service";

export interface Avtalskarta {
  title: string;
  extent: Extent;
}
@Injectable()
export class MkAvtalskartaPresenter {

  private _backgroundLayers: Layer[] = [];
  private _layers: Layer[];
  private _kartor: Avtalskarta[] = [];
  private _kartIndex = 0;

  constructor(private mapService: MkMapService) { }

  get backgroundLayers(): Layer[] {
    return this._backgroundLayers;
  }

  get layers(): Layer[] {
    return this._layers;
  }

  get kartor(): Avtalskarta[] {
    return this._kartor;
  }

  get aktivKarta(): Avtalskarta {
    return this._kartor[this._kartIndex];
  }

  get kartnummer(): number {
    return this._kartIndex + 1;
  }

  get isFirst(): boolean {
    return this._kartIndex === 0;
  }

  get isLast(): boolean {
    return this._kartIndex === this.kartor.length - 1;
  }

  /**
   * Navigera till föregående karta.
   */
  previous() {
    this._kartIndex = --this._kartIndex;
  }

  /**
   * Navigera till nästa karta.
   */
  next() {
    this._kartIndex = ++this._kartIndex;
  }

  initializeMap(avtalskarta: MkAvtalMap) {
    this._backgroundLayers = this.mapService.createLayers([
      this.mapService.defaultBackgroundLayer(avtalskarta.extent as Extent, avtalskarta.fastighetsId),
      this.mapService.fastighetYtaLayer(avtalskarta.fastighetsId, avtalskarta.projektId)
    ]);

    this._layers = this.mapService.createLayers([
      this.mapService.intrangLayer(avtalskarta.projektId, false),
      this.mapService.intrangMittpunktLayer(avtalskarta.fastighetsId, avtalskarta.projektId)
    ]);

    // Först en översiktskarta över fastigheten
    this._kartor.push({ title: avtalskarta.fastighetsbeteckning, extent: avtalskarta.extent as Extent });

    // Sedan alla områdesintrång
    avtalskarta.omraden.forEach(omrade => {
      this._kartor.push({ title: avtalskarta.fastighetsbeteckning + ">" + omrade.omradeNr, extent: omrade.extent as Extent });
    });
  }
}
