import { Injectable } from "@angular/core";
import { combineLatest, merge, Observable } from "rxjs";
import { map } from "rxjs/operators";
import { AgareApiService, Markagare, MarkagareInfo, ProjektApiService, FastighetIds } from "../../../../../generated/markkoll-api";
import { MkAgare } from "../model/agare";
import { uuid } from "../model/uuid";
import { FastighetService } from "./fastighet.service";

@Injectable({
  providedIn: "root"
})
export class AgareService {
  constructor(private projektApiService: ProjektApiService,
              private agareApiService: AgareApiService,
              private fastighetService: FastighetService) {}

  importAgare(projektId: uuid, fastighetIds: FastighetIds): Observable<number> {
    return this.projektApiService.importAgare(projektId, fastighetIds, "body");
  }

  addAgare(projektId: uuid, fastighetId: uuid, agare: MkAgare): Observable<MkAgare> {
    return this.projektApiService.addAgare(projektId, fastighetId, this.markagareInfoAgare(agare))
                                 .pipe(map(markagare => this.mkAgare(markagare)));
  }

  deleteAgare(ombud: MkAgare): Observable<void> {
    return this.agareApiService.deleteAgare(ombud.id);
  }

  editAgare(agare: MkAgare): Observable<MkAgare> {
    return this.agareApiService.updateAgare(agare.id, this.markagareAgare(agare))
                               .pipe(map(markagare => this.mkAgare(markagare)));
  }

  setInkluderaIAvtal(markagare: MkAgare[], inkluderaIAvtal: boolean): Observable<void> {
    const edits$ = markagare
      .filter(ag => ag.inkluderaIAvtal !== inkluderaIAvtal)
      .map(ag => this.editAgare({...ag, inkluderaIAvtal: inkluderaIAvtal}));

    return combineLatest(edits$).pipe(map(_ => void 0));
  }

  hasUnimportedAgare(projektId: uuid): Observable<boolean> {
    return this.fastighetService
               .fastighetIdsUnimported(projektId)
               .pipe(
                 map(f => f.length === 0));
  }

  /** @deprecated */
  _updateAgare(agare: Markagare): Observable<Markagare> {
    return this.agareApiService.updateAgare(agare.id, agare);
  }

  /** @deprecated */
  _deleteAgare(agare: Markagare): Observable<void> {
    return this.agareApiService.deleteAgare(agare.id);
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
      utbetalningsdatum: agare.utbetalningsdatum
    };
  }

  private markagareAgare(agare: MkAgare): Markagare {
    return {
      id: agare.id,
      namn: agare.namn,
      adress: agare.adress,
      postnummer: agare.postnummer,
      postort: agare.ort,
      andel: agare.andel,
      telefon: agare.telefon,
      bankkonto: agare.bankkonto,
      ePost: agare.ePost,
      kontaktperson: agare.kontaktperson,
      agareStatus: agare.status,
      labels: agare.labels,
      fodelsedatumEllerOrgnummer: agare.fodelsedatumEllerOrgnummer,
      agartyp: agare.agartyp,
      inkluderaIAvtal: agare.inkluderaIAvtal,
      utbetalningsdatum: agare.utbetalningsdatum,
      land: agare.land
    };
  }

  private markagareInfoAgare(agare: MkAgare): MarkagareInfo {
    return {
      namn: agare.namn,
      adress: agare.adress,
      postnummer: agare.postnummer,
      postort: agare.ort,
      andel: agare.andel,
      telefon: agare.telefon,
      bankkonto: agare.bankkonto,
      ePost: agare.ePost,
      kontaktperson: agare.kontaktperson,
      agareStatus: agare.status,
      fodelsedatumEllerOrgnummer: agare.fodelsedatumEllerOrgnummer,
      agartyp: agare.agartyp,
      inkluderaIAvtal: agare.inkluderaIAvtal,
      utbetalningsdatum: agare.utbetalningsdatum
    };
  }
}
