import { Feature } from "ol";
import { Geometry } from "ol/geom";
import Layer from "ol/layer/Layer";
import VectorLayer from "ol/layer/Vector";
import { Observable, Subject } from "rxjs";
import { MkMapComponent } from "../../../common/map/map.component";
import { uuid } from "../../../model/uuid";
import { ProjektService } from "../../../services/projekt.service";
import { MkKartmenyvalDelegate } from "../meny/default-kartverktyg-delegate";
import { VerktygsladaButton } from "../meny/hantera-intrang/verktygslada/verktygslada.component";
import GeoJSON from "ol/format/GeoJSON";
import { GeometriTyp, getGeometrityp } from "../../../common/geometry-util";
import { IntrangMap } from "../meny/hantera-intrang/intrang-map";
import { Type } from "@angular/core";
import { TranslocoService } from "@ngneat/transloco";
import { IntrangVerktygOption } from "../meny/hantera-intrang/verktyg/intrang-verktyg-option";
import { IntrangVerktygComponent } from "../meny/hantera-intrang/verktyg/intrang-verktyg.component";
import { KlippIntrangVerktygComponent } from "../meny/hantera-intrang/verktyg/klipp-intrang-verktyg/klipp-intrang-verktyg.component";
import { KlippIntrangVerktyg } from "../meny/hantera-intrang/verktyg/klipp-intrang-verktyg/klipp-intrang.verktyg";
import { SammanfogaIntrangVerktygComponent } from "../meny/hantera-intrang/verktyg/sammanfoga-intrang-verktyg/sammanfoga-intrang-verktyg.component";
import { SammanfogaIntrangVerktyg } from "../meny/hantera-intrang/verktyg/sammanfoga-intrang-verktyg/sammanfoga-intrang.verktyg";
import { SelectIntrangVerktygComponent } from "../meny/hantera-intrang/verktyg/select-intrang-verktyg/select-intrang-verktyg.component";
import { SelectIntrangVerktyg } from "../meny/hantera-intrang/verktyg/select-intrang-verktyg/select-intrang.verktyg";
import { IntrangVerktygEnum } from "../meny/hantera-intrang/verktyg/intrang-verktyg-enum";
import { IntrangsStatus, ProjektIntrang, ProjektTyp, Version } from "../../../../../../../generated/markkoll-api";
import { MkIntrangVerktyg } from "../meny/hantera-intrang/verktyg/intrang-verktyg";

export interface MkHanteraIntrangDelegate extends MkKartmenyvalDelegate {
  getVerktygComponent(tool: IntrangVerktygEnum): IntrangVerktygOption<unknown>;
  setMapFeatures(intrangMap: IntrangMap);

  saveChanges(projektIntrang: ProjektIntrang[]): Observable<Version>;
  getProjektIntrang(): Observable<ProjektIntrang[]>;
  getProjektTyp(): ProjektTyp;
}

export class MkHanteraIntrangDelegateImpl implements MkHanteraIntrangDelegate {
  private onActivateSubject = new Subject<void>();
  private onDeactivateSubject = new Subject<void>();

  constructor(private mapComponent: MkMapComponent,
    private projektService: ProjektService,
    private projektId: uuid,
    private projektTyp: ProjektTyp,
    private translate: TranslocoService) {
  }

  private verktyg: VerktygData<any>[] = [
    {
      id: IntrangVerktygEnum.SELECT,
      component: SelectIntrangVerktygComponent,
      verktyg: new SelectIntrangVerktyg(this.mapComponent, this.hanteraIntrangLinjeLayer, this.hanteraIntrangPunktLayer),
      data: { projektTyp: this.projektTyp },
      icon: "touch_app",
      iconText: this.translate.translate("mk.kartverktyg.hanteraIntrang.verktygslada.select")
    },
    {
      id: IntrangVerktygEnum.KLIPP,
      component: KlippIntrangVerktygComponent,
      verktyg: new KlippIntrangVerktyg(this.mapComponent.map, this.hanteraIntrangLinjeLayer.getSource()),
      data: { projektTyp: this.projektTyp },
      icon: "content_cut",
      iconText: this.translate.translate("mk.kartverktyg.hanteraIntrang.verktygslada.cut")
    },
    {
      id: IntrangVerktygEnum.SAMMANFOGA,
      component: SammanfogaIntrangVerktygComponent,
      verktyg: new SammanfogaIntrangVerktyg(this.mapComponent, this.hanteraIntrangLinjeLayer),
      data: { projektTyp: this.projektTyp },
      icon: "cable",
      iconText: this.translate.translate("mk.kartverktyg.hanteraIntrang.verktygslada.join")
    },
  ]

  activateMenyval(): void {
    this.avtalsstatusLayer.setVisible(false);
    this.intrangLayer.setVisible(false);

    this.mittlinjeredovisadesamfLayer.setVisible(true);
    this.fastighetsytorLayer.setVisible(true);
    this.hanteraIntrangLinjeLayer.setVisible(true);
    this.hanteraIntrangPunktLayer.setVisible(true);

    // Ful lösning för att få tabben att bli vald
    window.dispatchEvent(new Event('resize'));

    this.onActivateSubject.next();
  }

  inactivateMenyval(): void {
    this.onDeactivateSubject.next();
  }

  get onActivate$(): Observable<void> {
    return this.onActivateSubject.asObservable();
  }

  get onDeactivate$(): Observable<void> {
    return this.onDeactivateSubject.asObservable();
  }

  getProjektIntrang(): Observable<ProjektIntrang[]> {
    return this.projektService.getProjektintrang(this.projektId);
  }

  saveChanges(projektIntrang: ProjektIntrang[]): Observable<Version> {
    return this.projektService.updateVersionIntrang(this.projektId, projektIntrang);
  }

  getProjektTyp() {
    return this.projektTyp;
  }

  getVerktygComponent(tool: IntrangVerktygEnum): IntrangVerktygOption<unknown> {
    const verktyg = this.verktyg.find(v => v.id == tool);
    if (!verktyg) {
      throw new Error("Okänt verktyg: " + tool);
    }

    return {
      component: verktyg.component,
      verktyg: verktyg.verktyg,
      data: verktyg.data
    };
  }

  setMapFeatures(intrangMap: IntrangMap) {
    const features = this.convertIntrangToFeatures(intrangMap);

    let punktFeatures = [];
    let linjeFeatures = [];

    features.forEach(f => {
      const geometriTyp = getGeometrityp(f.getGeometry());
      switch (geometriTyp) {
        case GeometriTyp.LINJE:
          linjeFeatures.push(f);
          break;
        case GeometriTyp.PUNKT:
          punktFeatures.push(f);
          break;
        default:
          throw Error("Okänd geometrityp: " + geometriTyp);
      }
    })

    this.hanteraIntrangPunktLayer.getSource().clear();
    this.hanteraIntrangPunktLayer.getSource().addFeatures(punktFeatures);

    this.hanteraIntrangLinjeLayer.getSource().clear();
    this.hanteraIntrangLinjeLayer.getSource().addFeatures(linjeFeatures);
  }

  getButtons(): VerktygsladaButton[] {
    return this.verktyg.map(v => ({
      id: v.id,
      icon: v.icon,
      iconText: v.iconText
    }));
  }

  private get hanteraIntrangPunktLayer(): VectorLayer<any> {
    return this.mapComponent.getLayer("hanteraIntrangPunktLayer") as VectorLayer<any>;
  }

  private get hanteraIntrangLinjeLayer(): VectorLayer<any> {
    return this.mapComponent.getLayer("hanteraIntrangLinjeLayer") as VectorLayer<any>;
  }

  private get avtalsstatusLayer(): Layer {
    return this.mapComponent.getLayer("avtalsstatus");
  }

  private get fastighetsytorLayer(): Layer {
    return this.mapComponent.getLayer("fastighetsytorLayer");
  }

  private get mittlinjeredovisadesamfLayer(): Layer {
    return this.mapComponent.getLayer("mittlinjeredovisadesamfLayer");
  }

  private get intrangLayer(): Layer {
    return this.mapComponent.getLayer("intrang_projekt");
  }

  private convertIntrangToFeatures(intrangMap: IntrangMap): Feature<Geometry>[] {
    const features: Feature<Geometry>[] = [];

    intrangMap.getAll().forEach(intrang => {
      const feature = new GeoJSON().readFeature(intrang.geom);

      if (intrang.status == IntrangsStatus.NY) {
        feature.set("id", intrang.id);
        feature.set("intrangstyp", intrang.type);
        feature.set("intrangsSubtyp", intrang.subtype);
        feature.set("avtalstyp", intrang.avtalstyp);
        features.push(feature);
      }
    });

    return features;
  }
}

class VerktygData<T> {
  id: IntrangVerktygEnum;
  component: Type<IntrangVerktygComponent<T>>;
  verktyg: MkIntrangVerktyg<T>;
  data: any;
  icon: string;
  iconText: string;
}
