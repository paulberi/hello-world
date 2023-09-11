import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { switchMap } from "rxjs/operators";
import {
  ElnatBilaga,
  BilagaTyp,
  AvtalApiService,
  ElnatMarkledning,
  ElnatPrisomrade,
  ElnatPunktersattning,
  ElnatRotnetto,
  ElnatSsbSkogsmark,
  ElnatSsbVaganlaggning,
  ElnatVarderingsprotokollConfig,
  ElnatVarderingsprotokollMetadata,
  ElnatPunktersattningTyp,
  ElnatZon,
  ElnatLedningSkogsmark,
  ElnatOvrigtIntrang
} from "../../../../../generated/markkoll-api";
import { ElnatVarderingsprotokoll } from "../../../../../generated/markkoll-api";
import { ElnatVarderingsprotokollApiService } from "../../../../../generated/markkoll-api/api/elnatVarderingsprotokoll.service";
import { uuid } from "../model/uuid";

@Injectable({
  providedIn: "root"
})
export class ElnatVarderingsprotokollService {

  constructor(private varderingsprotokollAPIService: ElnatVarderingsprotokollApiService,
              private avtalApiService: AvtalApiService
    ) { }

  getVarderingsprotokoll(projektId, vpId): Observable<ElnatVarderingsprotokoll> {
    return this.varderingsprotokollAPIService.getElnatVarderingsprotokoll(projektId, vpId);
  }

  getVarderingsprotokollWithAvtalId(projektId, avtalId): Observable<ElnatVarderingsprotokoll> {
    return this.avtalApiService.getElnatVarderingsprotokollWithAvtalId(projektId, avtalId);
  }

  createDefaultMarkledning(projektId, vpId): Observable<ElnatMarkledning> {
    const markledning: ElnatMarkledning = {beskrivning: "", langd: 0, bredd: 1};
    return of(markledning);
  }

  createDefaultPunktersattning(projektId, vpId): Observable<ElnatPunktersattning> {
    const punktersattning: ElnatPunktersattning = {beskrivning: "", typ: ElnatPunktersattningTyp.NATSTATIONSKOG6X6M, antal: 0};
    return of(punktersattning);
  }

  createDefaultSsbSkogsmark(projektId, vpId): Observable<ElnatSsbSkogsmark> {
    const ssbSkogsmark: ElnatSsbSkogsmark = {beskrivning: "", langd: 0, bredd: 0};
    return of(ssbSkogsmark);
  }

  createDefaultSsbVaganlaggning(projektId, vpId): Observable<ElnatSsbVaganlaggning> {
    const ssbVaganlaggning: ElnatSsbVaganlaggning = {beskrivning: "", langd: 0, zon: ElnatZon.ZON_1};
    return of(ssbVaganlaggning);
  }

  createDefaultLedningSkogsmark(projektId: uuid, vpId: uuid): Observable<ElnatLedningSkogsmark> {
    const ledningSkogsmark: ElnatLedningSkogsmark = { beskrivning: "", ersattning: 0 };
    return of(ledningSkogsmark);
  }

  createDefaultOvrigErsattning(projektId: uuid, vpId: uuid): Observable<ElnatOvrigtIntrang> {
    const ovrigtIntrang: ElnatOvrigtIntrang = { beskrivning: "", ersattning: 0 };
    return of(ovrigtIntrang);
  }

  updateElnatVP(projektId, vpId, vp: ElnatVarderingsprotokoll): Observable<void> {
    return this.varderingsprotokollAPIService.updateElnatVarderingsprotokoll(projektId, vpId, vp);
  }

  addBilaga(projektId: uuid, vpId: uuid, file: Blob, typ: BilagaTyp): Observable<ElnatBilaga> {
    return this.varderingsprotokollAPIService.addElnatBilaga(projektId, vpId, file, typ);
  }

  getBilagor(projektId: uuid, vpId: uuid): Observable<ElnatBilaga[]> {
    return this.varderingsprotokollAPIService.getBilagor(projektId, vpId);
  }

  removeBilaga(projekTId: uuid, vpId: uuid, bilagaId: uuid): Observable<void> {
    return this.varderingsprotokollAPIService.removeElnatBilaga(projekTId, vpId, bilagaId);
  }
}
