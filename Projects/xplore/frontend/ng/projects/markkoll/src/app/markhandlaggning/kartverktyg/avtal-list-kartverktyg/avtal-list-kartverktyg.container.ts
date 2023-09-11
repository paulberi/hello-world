import { Component, Input, OnDestroy, OnInit } from "@angular/core";
import Layer from "ol/layer/Layer";
import { MkMapComponent } from "../../../common/map/map.component";
import { uuid } from "../../../model/uuid";
import { ProjektService } from "../../../services/projekt.service";
import { MkMenyvalState } from "./avtal-list-kartverktyg.presenter";
import { MkHanteraFastigheterDelegate } from "../meny/hantera-fastigheter/hantera-fastigheter.component";
import { MkHanteraFastigheterDelegateImpl } from "../delegates/hantera-fastigheter-delegate-impl";
import { FastighetService } from "../../../services/fastighet.service";
import { AvtalService } from "../../../services/avtal.service";
import { MkHanteraIntrangDelegate, MkHanteraIntrangDelegateImpl } from "../delegates/hantera-intrang-delegate.impl";
import { ProjektTyp } from "../../../../../../../generated/markkoll-api/model/projektTyp";
import { ActivatedRoute } from "@angular/router";
import { MkKartmenyvalDelegate } from "../meny/default-kartverktyg-delegate";
import { hanteraIntrangStyle } from "../../../common/map/styles";
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import { VerktygsladaButton } from "../meny/hantera-intrang/verktygslada/verktygslada.component";
import { TranslocoService } from "@ngneat/transloco";
import { IntrangVerktygEnum } from "../meny/hantera-intrang/verktyg/intrang-verktyg-enum";

@Component({
  selector: "mk-avtal-list-kartverktyg",
  templateUrl: "./avtal-list-kartverktyg.container.html",
})
export class MkAvtalListKartverktygContainerComponent implements OnInit, OnDestroy {

  @Input() map: MkMapComponent;
  @Input() projektId: uuid;

  private readonly projektTyp: ProjektTyp = this.activatedRoute.snapshot.params.projektTyp;

  hanteraFastigheterDelegate: MkHanteraFastigheterDelegate;
  hanteraIntrangDelegate: MkHanteraIntrangDelegate;
  verktygsladaButtons: VerktygsladaButton[];

  private hanteraIntrangPunktLayer;
  private hanteraIntrangLinjeLayer;

  currentMenyval: MkKartmenyvalDelegate;

  constructor(private projektService: ProjektService,
    private activatedRoute: ActivatedRoute,
    private fastighetService: FastighetService,
    private avtalService: AvtalService,
    private translate: TranslocoService) {
  }

  ngOnInit() {
    this.hanteraIntrangPunktLayer = new VectorLayer({ source: new VectorSource(), visible: false });
    this.hanteraIntrangPunktLayer.setStyle(hanteraIntrangStyle);
    this.hanteraIntrangPunktLayer.set("id", "hanteraIntrangPunktLayer");
    this.hanteraIntrangPunktLayer.set("map_selectable", true);

    this.hanteraIntrangLinjeLayer = new VectorLayer({ source: new VectorSource(), visible: false });
    this.hanteraIntrangLinjeLayer.setStyle(hanteraIntrangStyle);
    this.hanteraIntrangLinjeLayer.set("id", "hanteraIntrangLinjeLayer");
    this.hanteraIntrangLinjeLayer.set("map_selectable", true);

    this.map.map.addLayer(this.hanteraIntrangLinjeLayer);
    this.map.map.addLayer(this.hanteraIntrangPunktLayer);


    this.hanteraFastigheterDelegate = new MkHanteraFastigheterDelegateImpl(this.map,
      this.projektService, this.projektId, this.fastighetService, this.avtalService);
    const hanteraIntrangDelegateImpl = new MkHanteraIntrangDelegateImpl(this.map, this.projektService,
      this.projektId, this.projektTyp, this.translate);

    this.hanteraIntrangDelegate = hanteraIntrangDelegateImpl;
    this.verktygsladaButtons = hanteraIntrangDelegateImpl.getButtons();
  }

  ngOnDestroy(): void {
    this.map.map.removeLayer(this.hanteraIntrangLinjeLayer);
    this.map.map.removeLayer(this.hanteraIntrangPunktLayer);
  }

  onToolStateChange(toolState: MkMenyvalState) {
    if (this.currentMenyval) {
      this.currentMenyval.inactivateMenyval();
    }

    this.setupNoToolDefault();

    this.currentMenyval = this.getDelegate(toolState);
    if (this.currentMenyval) {
      this.currentMenyval.activateMenyval();
    }
  }

  private setupNoToolDefault() {
    this.highlightLayer.setVisible(true);
    this.avtalsstatusLayer.setVisible(true);
    this.intrangLayer.setVisible(true);

    this.mittlinjeredovisadesamfLayer.setVisible(false);
    this.hanteraFastigheterLayer.setVisible(false);
    this.hanteraIntrangPunktLayer.setVisible(false);
    this.hanteraIntrangLinjeLayer.setVisible(false);
    this.fastighetsytorLayer.setVisible(false);
  }

  private getDelegate(toolState: MkMenyvalState): MkKartmenyvalDelegate {
    switch (toolState) {
      case MkMenyvalState.HANTERA_FASTIGHETER:
        return this.hanteraFastigheterDelegate;
      case MkMenyvalState.INTRANG:
        return this.hanteraIntrangDelegate;
      default:
        return null;
    }
  }

  private get highlightLayer(): Layer {
    return this.map.getLayer("highlightLayer");
  }

  private get hanteraFastigheterLayer(): Layer {
    return this.map.getLayer("hanteraFastigheterLayer");
  }

  private get avtalsstatusLayer(): Layer {
    return this.map.getLayer("avtalsstatus");
  }

  private get fastighetsytorLayer(): Layer {
    return this.map.getLayer("fastighetsytorLayer");
  }

  private get mittlinjeredovisadesamfLayer(): Layer {
    return this.map.getLayer("mittlinjeredovisadesamfLayer");
  }

  private get intrangLayer(): Layer {
    return this.map.getLayer("intrang_projekt");
  }
}
