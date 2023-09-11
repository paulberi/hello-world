import { Component, Input, OnInit, ViewChild } from "@angular/core";
import Layer from "ol/layer/Layer";
import { MkMapComponent } from "../../../common/map/map.component";
import { MkAvtalMap } from "../../../model/avtalskarta";
import { Avtalskarta, MkAvtalskartaPresenter } from "./avtalskarta.presenter";

/**
 * Avtalskarta med intrångsöversikt och intrångskarta.
 */
@Component({
  selector: "mk-avtalskarta",
  providers: [MkAvtalskartaPresenter],
  templateUrl: "./avtalskarta.component.html",
  styleUrls: ["./avtalskarta.component.scss"],
})
export class MkAvtalskartaComponent implements OnInit {

  /**
   * Information som behövs för att visa avtalskarta.
   */
  @Input() avtalskarta: MkAvtalMap;

  @ViewChild(MkMapComponent) map: MkMapComponent;
  paddingInMap = [25, 25, 25, 25];

  constructor(
    private presenter: MkAvtalskartaPresenter) {
  }

  ngOnInit(): void {
    this.presenter.initializeMap(this.avtalskarta);
  }

  /**
   * Navigera till föregående karta.
   */
  previous() {
    this.presenter.previous();
    this.setLayerVisibility();
    this.map.fitToExtent(this.aktivKarta.extent);
  }

  /**
   * Navigera till nästa karta.
   */
  next() {
    this.presenter.next();
    this.setLayerVisibility();
    this.map.fitToExtent(this.aktivKarta.extent);
  }

  setLayerVisibility() {
    const intrangProjektLayerId = "intrang_projekt";
    const intrangMittpunktLayerId = "intrang_mittpunkt";
    if (this.isFirst) {
      this.map.setLayerVisibility(intrangProjektLayerId, false);
      this.map.setLayerVisibility(intrangMittpunktLayerId, true);
    } else {
      this.map.setLayerVisibility(intrangProjektLayerId, true);
      this.map.setLayerVisibility(intrangMittpunktLayerId, false);
    }
  }

  get kartor(): Avtalskarta[] {
    return this.presenter.kartor;
  }

  get aktivKarta(): Avtalskarta {
    return this.presenter.aktivKarta;
  }

  get kartnummer(): number {
    return this.presenter.kartnummer;
  }

  get isFirst(): boolean {
    return this.presenter.isFirst;
  }

  get isLast(): boolean {
    return this.presenter.isLast;
  }

  get backgroundLayers(): Layer[] {
    return this.presenter.backgroundLayers;
  }

  get layers(): Layer[] {
    return this.presenter.layers;
  }
}
