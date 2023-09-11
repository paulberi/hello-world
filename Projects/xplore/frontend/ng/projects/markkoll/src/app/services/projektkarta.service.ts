import { Injectable } from "@angular/core";
import { Observable, Subject, Subscription } from "rxjs";
import { uuid } from "../model/uuid";

export enum MkProjektkartaLayer {
  BUFFERT = "BUFFERT",
  HIGHLIGHT = "HIGHLIGHT",
  AVTALSSTATUS = "AVTALSSTATUS",
  PREVIOUS_VERSION = "PREVIOUS_VERSION",
  INTRANG = "INTRANG",
  FASTIGHETSYTOR = "FASTIGHETSYTOR",
  MITTLINJEREDOVISADE_SAMF = "MITTLINJEREDOVISADE_SAMF",
  HANTERA_FASTIGHETER = "HANTERA_FASTIGHETER"
}

export interface MkProjektkarta {
  mapInfoClick: Observable<MkProjektkartaClickEvent>;

  clearHighlights();
  highlightFastighetInMap(fastighetId: uuid, zoom: boolean);
  setLayerVisibility(projektkartaLayer: MkProjektkartaLayer, visible: boolean);
  refreshLayer(projektkartaLayer: MkProjektkartaLayer);
  selectedFastighet(): MkSelectedFastighet;
}

export class MkProjektkartaClickEvent {
  fastighetsbeteckning: string;
  fastighetId: uuid;
}

export class MkSelectedFastighet {
  fastighetsbeteckning: string;
  fastighetId: uuid;
}
@Injectable({
  providedIn: "root"
})
export class MkProjektkartaService {
  projektkarta: MkProjektkarta;

  private mapClickSubject = new Subject<MkProjektkartaClickEvent>();
  private subscription: Subscription;

  register(projektkarta: MkProjektkarta) {
    this.deregister();

    this.projektkarta = projektkarta;
    this.subscription = this.projektkarta.mapInfoClick.subscribe(ev => this.mapClickSubject.next(ev));
  }

  deregister() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    this.projektkarta = null;
  }

  clearHighlights() {
    if (this.projektkarta) {
      this.projektkarta.clearHighlights();
    }
  }

  highlightFastighetInMap(fastighetId: uuid, zoom: boolean) {
    if (this.projektkarta) {
      this.projektkarta.highlightFastighetInMap(fastighetId, zoom);
    }
  }

  setLayerVisibility(projektkartaLayer: MkProjektkartaLayer, visible: boolean) {
    if (this.projektkarta) {
      this.projektkarta.setLayerVisibility(projektkartaLayer, visible);
    }
  }

  refreshLayer(projektkartaLayer: MkProjektkartaLayer) {
    if (this.projektkarta) {
      this.projektkarta.refreshLayer(projektkartaLayer);
    }
  }

  mapClick(): Observable<MkProjektkartaClickEvent> {
    return this.mapClickSubject.asObservable();
  }

  selectedFastighet(): MkSelectedFastighet {
    if (this.projektkarta) {
      return this.projektkarta.selectedFastighet();
    } else {
      return {
        fastighetId: null,
        fastighetsbeteckning: null
      };
    }
  }
 }
