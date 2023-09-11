import { Injectable } from "@angular/core";
import {
  AvtalsjobbStatus, Avtalsstatus,
  Fastighetsfilter,
  InfobrevApiService,
  InfobrevsjobbProgress,
  ProjektApiService,
} from "../../../../../generated/markkoll-api";
import { map, switchMap, takeWhile, tap } from "rxjs/operators";
import { uuid } from "../model/uuid";
import { Observable } from "rxjs/internal/Observable";
import { of, timer } from "rxjs";
import { MkAvtalsfilter } from "../model/avtalsfilter";
import { HttpDownloadService } from "../../../../lib/http/http-download.service";
import { DokumentjobbService } from "./dokumentjobb.service";
import { FastighetsfilterService } from "./fastighetsfilter.service";

@Injectable({
  providedIn: "root"
})
export class InfobrevsjobbService implements DokumentjobbService {
  constructor(private infobrevApiService: InfobrevApiService,
    private fastighetsfilterService: FastighetsfilterService,
    private httpDownloadService: HttpDownloadService,
    private projektApiService: ProjektApiService) { }

  private static storageKey(projektId: uuid): string {
    return "infobrev_projekt_" + projektId;
  }

  cancel(projektId: uuid, infobrevsjobbId: uuid) {
    return this.infobrevApiService.cancelInfobrevsjobb(infobrevsjobbId)
      .pipe(map(() => this.reset(projektId)));
  }

  create(projektId: uuid, filter: MkAvtalsfilter, template: uuid, interval: number = 1000): Observable<InfobrevsjobbProgress> {
    return this.projektApiService.createInfobrevsjobb(
      projektId,
      { filter: this.fastighetsfilterService.getFastighetsfilter(filter, false), template: template }
    ).pipe(
      tap(jobbId => localStorage.setItem(InfobrevsjobbService.storageKey(projektId), jobbId)),
      switchMap(jobbId => this.pollProgress(jobbId, interval))
    );
  }

  getProgress(projektId: uuid, interval: number = 1000): Observable<InfobrevsjobbProgress> {
    const jobbId = localStorage.getItem(InfobrevsjobbService.storageKey(projektId));

    if (jobbId) {
      return this.pollProgress(jobbId, interval);
    } else {
      return of({
        id: null,
        status: AvtalsjobbStatus.NONE,
        generated: null,
        total: null,
      });
    }
  }

  getData(projektId: uuid, infobrevsjobbId: uuid) {
    this.httpDownloadService.httpDownload("/api/infobrev/" + infobrevsjobbId);
  }

  reset(projektId: uuid) {
    localStorage.removeItem(InfobrevsjobbService.storageKey(projektId));
  }

  private pollProgress(jobbId: uuid, interval: number): Observable<InfobrevsjobbProgress> {
    return timer(0, interval)
      .pipe(
        switchMap(_ => this.checkJobProgress(jobbId)),
        takeWhile(progress => progress.status === AvtalsjobbStatus.INPROGRESS || progress.status === AvtalsjobbStatus.NONE, true));
  }

  private checkJobProgress(jobbId: uuid): Observable<InfobrevsjobbProgress> {
    if (jobbId) {
      return this.infobrevApiService.infobrevsjobbProgress(jobbId);
    } else {
      return of({ id: null, status: AvtalsjobbStatus.NONE, total: null, generated: null });
    }
  }

}
