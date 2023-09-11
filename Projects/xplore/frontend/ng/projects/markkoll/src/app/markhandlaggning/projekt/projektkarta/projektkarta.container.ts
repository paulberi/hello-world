import { AfterContentInit, AfterViewInit, Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from "@angular/core";
import { uuid } from "../../../model/uuid";
import { MkMapService } from "../../../services/map.service";
import Layer from "ol/layer/Layer";
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import { environment } from "../../../../environments/environment";
import { StyleFunction } from "ol/style/Style";
import { hanteraIntrangStyle, selectedFastighetStyle } from "../../../common/map/styles";
import { Feature } from "ol";
import { Geometry } from "ol/geom";
import { Observable, Subject } from "rxjs";
import { Extent } from "ol/extent";
import { MkProjektkartaComponent } from "./projektkarta.component";
import { MkMapComponent, FeatureInfoResult } from "../../../common/map/map.component";
import { MkProjektkarta, MkProjektkartaClickEvent, MkProjektkartaLayer, MkProjektkartaService, MkSelectedFastighet } from "../../../services/projektkarta.service";
import { ProjektService } from "../../../services/projekt.service";
import { ProjektTyp } from "../../../../../../../generated/markkoll-api";

@Component({
  selector: "mk-projektkarta",
  templateUrl: "./projektkarta.container.html",
  providers: []
})
export class MkProjektkartaContainerComponent implements OnInit, OnDestroy, MkProjektkarta {
  @Input() projektId: uuid;
  @Input() projektTyp: ProjektTyp;

  private mapInfoClickSubject = new Subject<MkProjektkartaClickEvent>();

  backgroundLayers: Layer[];
  mapExtent$: Observable<Extent>;

  private buffertLayer: VectorLayer<any>;
  private highlightLayer: VectorLayer<any>;
  private informationsLayers: Layer[];
  private riksintressenLayers: Layer[];

  private avtalsstatusLayer: Layer;
  private previousVersionLayer: Layer;
  private intrangLayer: Layer;

  private indataLayer: Layer;

  // Tillhör kartverktyget Hantera fastigheter
  private fastighetsytorLayer: Layer;
  private mittlinjeredovisadesamfLayer: Layer;
  private hanteraFastigheterLayer: Layer;
  private _selectedFastighet: MkSelectedFastighet = null;

  @ViewChild(MkProjektkartaComponent, { static: false }) private component: MkProjektkartaComponent;
  private get map(): MkMapComponent {
    return this.component.map;
  }

  constructor(private mapService: MkMapService,
    private projektkartaService: MkProjektkartaService,
    private projektService: ProjektService) {
    this.projektkartaService.register(this);
  }

  ngOnInit() {
    this.buffertLayer = new VectorLayer({ source: new VectorSource() });
    this.highlightLayer = new VectorLayer({ source: new VectorSource() });
    this.avtalsstatusLayer = this.mapService.createLayer(this.mapService.avtalsstatusLayer(this.projektId));
    this.previousVersionLayer = this.mapService.createLayer(this.mapService.intrangPreviousVersionLayer(this.projektId, false));
    this.intrangLayer = this.mapService.createLayer(this.mapService.intrangLayer(this.projektId, true));

    const informationsLayersDefs = this.mapService.getLayerDefs("information");
    this.informationsLayers = this.mapService.createLayers(informationsLayersDefs);
    const riksintressenLayerDefs = this.mapService.getLayerDefs("riksintressen");
    this.riksintressenLayers = this.mapService.createLayers(riksintressenLayerDefs);

    const backgroundLayerDefs = this.mapService.getLayerDefs("bakgrundskartor");
    this.backgroundLayers = this.mapService.createLayers(backgroundLayerDefs);

    const indataLayerDef = this.mapService.indataLayerDef(this.projektId);
    this.indataLayer = this.mapService.createLayer(indataLayerDef);

    this.mittlinjeredovisadesamfLayer = this.mapService.createLayer(this.mapService.mittlinjeredovisadesamfLayer());
    this.hanteraFastigheterLayer = new VectorLayer({ source: new VectorSource(), visible: false });

    // Tillhör kartverktyget Hantera fastigheter
    this.fastighetsytorLayer = this.informationsLayers.find(layer => layer.get("id") === "fastighetsytorLayer");

    this.highlightLayer.set("id", "highlightLayer");
    this.hanteraFastigheterLayer.set("id", "hanteraFastigheterLayer");
    this.fastighetsytorLayer.set("map_info_click", true);
    this.mittlinjeredovisadesamfLayer.set("map_info_click", true);
    this.avtalsstatusLayer.set("map_info_click", true);
    this.avtalsstatusLayer.getSource().refresh();

    this.mapService.projektintrangBuffert(this.projektId, false, this.buffertLayer);
    this.mapExtent$ = this.mapService.getProjektExtent(this.projektId);
  }

  ngOnDestroy() {
    this.projektkartaService.deregister();
    this.mapInfoClickSubject.complete();
  }

  clearHighlights() {
    this.highlightLayer.getSource().clear();
  }

  highlightFastighetInMap(fastighetId: uuid, zoom: boolean) {
    this.mapService
      .getFastighetFeatures(environment.markkollWfsUrl, this.projektId, fastighetId)
      .subscribe(features => {
        const zIndex = this.mapService.avtalsstatusLayer(this.projektId).zIndex + 1;
        this.highlightFeatures(features, selectedFastighetStyle as StyleFunction, zIndex);
        if (zoom && this.map) {
          this.map.zoomToFeatures(features);
        }
      });
  }

  onMapClick(results: FeatureInfoResult[]) {
    results.filter(r => r.layerId === "avtalsstatus")
      .map(r => r.features)
      .forEach(features => this.emitFeatures(features));
  }

  emitFeatures(features: Feature<Geometry>[]) {
    if (features.length > 0) {
      const fbet = features[0].get("fastighetsbeteckning");
      const fid = features[0].get("fastighet_id");
      this.highlightFastighetInMap(fid, false);
      this._selectedFastighet = {
        fastighetId: fid,
        fastighetsbeteckning: fbet
      };
      this.mapInfoClickSubject.next({
        fastighetsbeteckning: fbet,
        fastighetId: fid
      });
    } else {
      this.clearHighlights();
      this._selectedFastighet = null;
      this.mapInfoClickSubject.next({
        fastighetsbeteckning: null,
        fastighetId: null
      });
    }
  }

  selectedFastighet(): MkSelectedFastighet {
    return this._selectedFastighet;
  }

  setLayerVisibility(projektkartaLayer: MkProjektkartaLayer, visible: boolean) {
    const layer = this.getLayer(projektkartaLayer);

    layer.setVisible(visible);
  }

  refreshLayer(projektkartaLayer: MkProjektkartaLayer) {
    const layer = this.getLayer(projektkartaLayer);

    layer.getSource().refresh();
  }

  get layers(): Layer[] {
    const layers: Layer[] = [
      this.mittlinjeredovisadesamfLayer,
      this.avtalsstatusLayer,
      this.previousVersionLayer,
      this.buffertLayer,
      this.intrangLayer,
      this.highlightLayer,
      this.hanteraFastigheterLayer,
      this.indataLayer
    ];
    this.informationsLayers.forEach(layer => layers.push(layer));
    this.riksintressenLayers.forEach(layer => layers.push(layer));
    return layers;
  }

  get mapInfoClick(): Observable<MkProjektkartaClickEvent> {
    return this.mapInfoClickSubject.asObservable();
  }

  private highlightFeatures(features: Feature[], style: StyleFunction, zIndex: number) {
    this.highlightLayer.getSource().clear();
    this.highlightLayer.setStyle(style);
    this.highlightLayer.setZIndex(zIndex);
    this.highlightLayer.getSource().addFeatures(features);
  }

  private getLayer(projektkartaLayer: MkProjektkartaLayer): Layer {
    switch (projektkartaLayer) {
      case MkProjektkartaLayer.AVTALSSTATUS:
        return this.avtalsstatusLayer;
      case MkProjektkartaLayer.BUFFERT:
        return this.buffertLayer;
      case MkProjektkartaLayer.FASTIGHETSYTOR:
        return this.fastighetsytorLayer;
      case MkProjektkartaLayer.HANTERA_FASTIGHETER:
        return this.hanteraFastigheterLayer;
      case MkProjektkartaLayer.HIGHLIGHT:
        return this.highlightLayer;
      case MkProjektkartaLayer.INTRANG:
        return this.intrangLayer;
      case MkProjektkartaLayer.MITTLINJEREDOVISADE_SAMF:
        return this.mittlinjeredovisadesamfLayer;
      case MkProjektkartaLayer.PREVIOUS_VERSION:
        return this.previousVersionLayer;
    }
  }
}
