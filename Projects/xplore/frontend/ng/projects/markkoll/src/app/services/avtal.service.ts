import { HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { forkJoin, Observable, Subject } from "rxjs";
import {
  AvtalsjobbProgress, Fastighetsinfo, AvtalsLoggItem, Markagare,
  Agartyp, Fastighet, Avtalsstatus, Intrangstyp, IntrangsSubtyp, AvtalsLoggType, Geometristatus,
  Fastighetsfilter, FastighetPage, GeometryType, ProjektApiService, FastighetApiService, AvtalApiService, TillvaratagandeTyp, AvtalMetadata, EditElnatVp
} from "../../../../../generated/markkoll-api";
import { flatMap, map } from "rxjs/operators";
import { TranslocoService } from "@ngneat/transloco";
import { VersionMessage } from "../markhandlaggning/fastighet/fastighet.component";
import { XpMessageSeverity } from "../../../../lib/ui/feedback/message/message.component";
import { XpPage } from "../../../../lib/ui/paginated-table/page";
import { MkAvtal } from "../model/avtal";
import { MkAgare } from "../model/agare";
import { MkLoggItem } from "../model/loggItem";
import { MkAvtalSummary } from "../model/avtalSummary";
import { MkAvtalsfilter } from "../model/avtalsfilter";
import { MkNoteringar } from "../model/noteringar";
import { uuid } from "../model/uuid";
import { MkAvtalMap } from "../model/avtalskarta";
import { FastighetsfilterService } from "./fastighetsfilter.service";

@Injectable({
  providedIn: "root"
})
export class AvtalService {

  avtalListChange = new Subject<void>()

  constructor(private projektApiService: ProjektApiService,
    private fastighetApiService: FastighetApiService,
    private avtalApiService: AvtalApiService,
    private translation: TranslocoService,
    private fastighetsfilterService: FastighetsfilterService) { }

  getAvtalListChange$() {
    return this.avtalListChange.asObservable();
  }

  sendAvtalListChange$() {
    this.avtalListChange.next();
  }

  getAvtal(projektId: uuid, fastighetId: uuid): Observable<MkAvtal> {
    return forkJoin({
      avtalId: this.projektApiService.getAvtalId(projektId, fastighetId),
      finfo: this.fastighetApiService.getFastighetsinfo(fastighetId, projektId),
      fast: this.projektApiService.getFastighet(projektId, fastighetId),
      status: this.projektApiService.getFastighetStatus(projektId, fastighetId)
    }).pipe(map(obs => this.avtal(obs.avtalId, obs.fast, obs.finfo, projektId, obs.status)));
  }

  setAvtalstatus(projektId: uuid, fastighetId: uuid, status: Avtalsstatus): Observable<void> {
    return this.projektApiService.setFastighetStatus(projektId, fastighetId, status).pipe(map(_ => void 0));
  }

  getAvtalstatus(projektId: uuid, fastighetId: uuid): Observable<Avtalsstatus> {
    return this.projektApiService.getFastighetStatus(projektId, fastighetId);
  }

  getFastighetInfoPage(projektId: uuid,
    page: number,
    size: number,
    filter: MkAvtalsfilter): Observable<XpPage<MkAvtalSummary>> {
    const f: Fastighetsfilter = this.fastighetsfilterService.getFastighetsfilter(filter, false);

    return this.projektApiService
      .projektFastigheterPage(projektId, page, size, f)
      .pipe(map(p => this.fastighetPage(p)));
  }

  getSamfallighetInfoPage(projektId: uuid,
    page: number,
    size: number,
    filter: MkAvtalsfilter): Observable<XpPage<MkAvtalSummary>> {
    const f: Fastighetsfilter = this.fastighetsfilterService.getFastighetsfilter(filter, false);

    return this.projektApiService
      .projektSamfalligheterPage(projektId, page, size, f)
      .pipe(map(p => this.fastighetPage(p)));
  }

  getAvtalSummary(projektId: uuid, fastighetId: uuid): Observable<MkAvtalSummary> {
    return this.fastighetApiService
      .getFastighetsinfo(fastighetId, projektId)
      .pipe(map(f => this.mkAvtalSummary(f)));
  }

  getGeometristatusMessage(geometristatus: Geometristatus): VersionMessage {
    switch (geometristatus) {
      case Geometristatus.NY:
        return {
          title: this.translation.translate("mk.fastighetsinformation.newVersionTitle"),
          text: this.translation.translate("mk.fastighetsinformation.newVersionText"),
          severity: XpMessageSeverity.Information,
          actionLabel: this.translation.translate("mk.fastighetsinformation.newVersionLabel"),
        };
      case Geometristatus.UPPDATERAD:
        return {
          title: this.translation.translate("mk.fastighetsinformation.updatedVersionTitle"),
          text: this.translation.translate("mk.fastighetsinformation.updatedVersionText"),
          severity: XpMessageSeverity.Information,
          actionLabel: this.translation.translate("mk.fastighetsinformation.updatedVersionLabel")
        };
      case Geometristatus.BORTTAGEN:
        return {
          title: this.translation.translate("mk.fastighetsinformation.deletedVersionTitle"),
          text: this.translation.translate("mk.fastighetsinformation.deletedVersionText"),
          severity: XpMessageSeverity.Warning,
          actionLabel: this.translation.translate("mk.fastighetsinformation.deletedVersionLabel"),
        };
      default:
        return null;
    }
  }

  isMittlinjeRedovisad(avtalskarta: MkAvtalMap) {
    if (avtalskarta == null || avtalskarta.omraden == null) {
      return false;
    }

    return avtalskarta.omraden.some(o => {
      return o.geometryType === GeometryType.LineString ||
        o.geometryType === GeometryType.MultiLineString;
    });
  }

  /**
   * Hämta senaste loggbokshändelserna.
   */
  getLoggbok$(projektId: uuid, fastighetId: uuid): Observable<MkLoggItem[]> {
    return this.getAvtal(projektId, fastighetId).pipe(map(avtal => avtal.loggbok));
  }

  /** @deprecated */
  createAvtalsjobb(projektId: uuid): Observable<uuid> {
    return this.projektApiService.createAvtalsjobb(projektId);
  }

  /** @deprecated */
  getAvtalsjobbProgress(avtalsjobbId: uuid): Observable<AvtalsjobbProgress> {
    return this.avtalApiService.avtalsjobbProgress(avtalsjobbId);
  }

  numOfAvtal(projektId: uuid, filter: MkAvtalsfilter): Observable<number> {
    const fastighetsfilter = this.fastighetsfilterService.getFastighetsfilter(filter, false);

    return this.avtalApiService.avtalCount(projektId, fastighetsfilter);
  }

  setSkogsfastighet(projektId: uuid, fastighetId: uuid, skogsfastighet: boolean): Observable<void> {
    return this.projektApiService
      .getAvtalId(projektId, fastighetId)
      .pipe(
        flatMap(avtalId => this.avtalApiService.setSkogsfastighet(projektId, avtalId, skogsfastighet)));
  }

  setAvtalMetadata(avtalId: uuid, metadata: AvtalMetadata): Observable<AvtalMetadata> {
    return this.avtalApiService.setAvtalMetadata(avtalId, metadata);
  }

  getEditElnatVpByUUID(projektId: uuid, filter: MkAvtalsfilter): Observable<EditElnatVp[]> {
    const f: Fastighetsfilter = this.fastighetsfilterService.getFastighetsfilter(filter, false);

    return this.avtalApiService.getEditElnatVpByUUID(projektId, f);
  }

  updateElnatVpAndAvtalMetadata(projektId: uuid, editElnatVps: EditElnatVp[]) {
    return this.avtalApiService.updateElnatVpAndAvtalMetadata(projektId, editElnatVps);
  }

  setTillvaratagandeTyp(projektId: uuid, fastighetId: uuid, tillvaratagandeTyp: TillvaratagandeTyp): Observable<void> {
    return this.projektApiService
      .getAvtalId(projektId, fastighetId)
      .pipe(
        flatMap(avtalId => this.avtalApiService.setTillvaratagandeTyp(avtalId, tillvaratagandeTyp)));
  }

  private getFileNameFromHttpResponse(httpResponse: HttpResponse<Blob>): string {
    const contentDispositionHeader = httpResponse.headers.get("Content-Disposition");
    const result = contentDispositionHeader.split(";")[1].trim().split("=")[1];
    return result.replace(/"/g, "");
  }

  private fastighetPage(fastighetPage: FastighetPage): XpPage<MkAvtalSummary> {
    return {
      number: fastighetPage.number,
      numberOfElements: fastighetPage.totalElements,
      totalElements: fastighetPage.totalElements,
      totalPages: fastighetPage.totalPages,
      content: fastighetPage.content.map(f => this.mkAvtalSummary(f))
    };
  }

  private mkAvtalSummary(fastighet: Fastighet): MkAvtalSummary {
    return {
      fastighetId: fastighet.id,
      fastighetsbeteckning: fastighet.fastighetsbeteckning,
      avtalsstatus: fastighet.avtalsstatus,
      information: this.avtalInformation(fastighet),
      notiser: fastighet.hasAnteckningar ? ["Anteckningar"] : []
    };
  }

  /** Det här är beräkningar som möjligen borde flyttas till backenden */
  private avtalInformation(fastighet: Fastighet): MkNoteringar {
    const detaljtyp = fastighet.detaljtyp;
    let labels: MkNoteringar = {
      outredd: detaljtyp != null && (!detaljtyp.localeCompare("FASTO") || !detaljtyp.localeCompare("SAMFO")),
      nyManuell: fastighet.manuelltTillagd,
      avvikelse: fastighet.avvikelse,
      nyFastighet: fastighet.geometristatus === Geometristatus.NY,
      uppdateratIntrang: fastighet.geometristatus === Geometristatus.UPPDATERAD,
      borttagenFastighet: fastighet.geometristatus === Geometristatus.BORTTAGEN,
      agareSaknas: fastighet.agareSaknas,
      skogsfastighet: fastighet.skogsfastighet
    };

    return labels;
  }

  private avtal(avtalId: uuid,
    fastighet: Fastighet,
    fastighetsinfo: Fastighetsinfo,
    projektId: uuid,
    status: Avtalsstatus): MkAvtal {

    const lf = fastighetsinfo.agare
      .filter(ag => ag.agartyp === Agartyp.LF)
      .map(ag => this.mkAgare(ag));

    const tr = fastighetsinfo.agare
      .filter(ag => ag.agartyp === Agartyp.TR)
      .map(ag => this.mkAgare(ag));

    const ombud = fastighetsinfo.agare
      .filter(ag => ag.agartyp === Agartyp.OMBUD)
      .map(ag => this.mkAgare(ag));

    const luftstrak = +(fastighetsinfo.intrang
      .filter(i => i.typ === Intrangstyp.STRAK && i.subtyp === IntrangsSubtyp.LUFTSTRAK)
      .reduce((acc, i) => acc += i.langd, 0)
      .toFixed(2));

    const markstrak = +(fastighetsinfo.intrang
      .filter(i => i.typ === Intrangstyp.STRAK && i.subtyp === IntrangsSubtyp.MARKSTRAK)
      .reduce((acc, i) => acc += i.langd, 0)
      .toFixed(2));

    const outredd = fastighet.detaljtyp != null &&
      (fastighet.detaljtyp.localeCompare("FASTO") === 0 ||
        fastighet.detaljtyp.localeCompare("SAMFO") === 0);

    const loggbok = fastighetsinfo.avtalsLogg.map(item => this.loggItem(item))
      .filter(item => item !== null);

    return {
      id: avtalId,
      lagfarnaAgare: lf,
      tomtrattsinnehavare: tr,
      ombud: ombud,
      status: status,
      outredd: outredd,
      intrang: {
        markstrak: markstrak,
        luftstrak: luftstrak,
      },
      ersattning: fastighetsinfo.ersattning,
      avtalskarta: {
        fastighetsId: fastighet.id,
        fastighetsbeteckning: fastighet.fastighetsbeteckning,
        extent: fastighetsinfo.extent,
        projektId: projektId,
        omraden: fastighetsinfo.omraden
      },
      loggbok: loggbok,
      anteckning: fastighetsinfo.anteckning,
      geometristatus: fastighet.geometristatus,
      skogsfastighet: fastighetsinfo.skogsfastighet,
      tillvaratagandeTyp: fastighetsinfo.tillvaratagandeTyp,
      metadata: {
        markslag: fastighetsinfo.markslag,
        matandeStation: fastighetsinfo.matandeStation,
        stationsnamn: fastighetsinfo.stationsnamn,
        franStation: fastighetsinfo.franStation,
        tillStation: fastighetsinfo.tillStation,
      },
      egetTillvaratagande: fastighetsinfo.egetTillvaratagande,
      samfallighet: fastighetsinfo.samfallighet
    };
  }

  private mkAgare(agare: Markagare): MkAgare {
    return {
      id: agare.id,
      namn: agare.namn,
      kontaktperson: agare.kontaktperson,
      adress: agare.adress,
      postnummer: agare.postnummer,
      ort: agare.postort,
      land: agare.land,
      telefon: agare.telefon,
      bankkonto: agare.bankkonto,
      ePost: agare.ePost,
      status: agare.agareStatus,
      andel: agare.andel,
      fodelsedatumEllerOrgnummer: agare.fodelsedatumEllerOrgnummer,
      inkluderaIAvtal: agare.inkluderaIAvtal,
      labels: agare.labels,
      agartyp: agare.agartyp,
      utbetalningsdatum: agare.utbetalningsdatum,
    };
  }

  private loggItem(logg: AvtalsLoggItem): MkLoggItem {
    switch (logg.avtalsLoggType) {
      case AvtalsLoggType.AVTALSSTATUS:
        return {
          title: this.translation.translate("mk.loggbok.avtalsStatusTitle",
            {
              avtalsPart: logg.avtalsPartNamn,
              avtalsStatus: this.translation.translate("mk.avtal." + logg.avtalsLoggStatus)
            }),

          text: this.translation.translate("mk.loggbok.avtalsStatusText",
            {
              createdBy: logg.skapadAv,
              avtalsPart: logg.avtalsPartNamn,
              avtalsStatus: this.translation.translate("mk.avtal." + logg.avtalsLoggStatus)
            }),

          createdDate: logg.skapadDatum
        };
      case AvtalsLoggType.GEOMETRISTATUS:
        return this.geometristatusItem(logg);
      case AvtalsLoggType.UTBETALNINGSDATUM:
        return {
          title: this.translation.translate("mk.loggbok.utbetalningsdatumTitle",
            {
              avtalsPart: logg.avtalsPartNamn,
              avtalsStatus: logg.avtalsLoggStatus
            }),
          text: this.translation.translate("mk.loggbok.utbetalningsdatumText",
            {
              createdBy: logg.skapadAv,
              avtalsPart: logg.avtalsPartNamn,
              avtalsStatus: logg.avtalsLoggStatus
            }),
          createdDate: logg.skapadDatum
        }
    }
  }

  private geometristatusItem(logg: AvtalsLoggItem): MkLoggItem {
    switch (logg.avtalsLoggStatus) {
      case Geometristatus.BORTTAGEN:
        return {
          title: this.translation.translate("mk.geometriStatus." + logg.avtalsLoggStatus),
          text: this.translation.translate("mk.loggbok.geometriStatusBorttagenText", { createdBy: logg.skapadAv }),
          createdDate: logg.skapadDatum
        };
      case Geometristatus.NY:
        return {
          title: this.translation.translate("mk.geometriStatus." + logg.avtalsLoggStatus),
          text: this.translation.translate("mk.loggbok.geometriStatusUppdateradText", { createdBy: logg.skapadAv }),
          createdDate: logg.skapadDatum
        };
      case Geometristatus.UPPDATERAD:
        return {
          title: this.translation.translate("mk.geometriStatus." + logg.avtalsLoggStatus),
          text: this.translation.translate("mk.loggbok.geometriStatusUppdateradText", { createdBy: logg.skapadAv }),
          createdDate: logg.skapadDatum
        };
      case Geometristatus.OFORANDRAD:
        return null;
    }
  }
}
