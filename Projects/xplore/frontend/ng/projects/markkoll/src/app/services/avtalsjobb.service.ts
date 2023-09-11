import { HttpEventType } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, of, timer } from "rxjs";
import { map, switchMap, takeWhile, tap } from "rxjs/operators";
import { AvtalApiService, AvtalsjobbProgress, AvtalsjobbStatus, Avtalsstatus, Fastighetsfilter, ProjektApiService } from "../../../../../generated/markkoll-api";
import { MkAvtalsfilter } from "../model/avtalsfilter";
import { uuid } from "../model/uuid";
import { HttpDownloadService } from "../../../../lib/http/http-download.service";
import { DokumentjobbService } from "./dokumentjobb.service";
import { FastighetsfilterService } from "./fastighetsfilter.service";
@Injectable({
  providedIn: "root"
})
export class AvtalsjobbService implements DokumentjobbService {
  constructor(private projektApiService: ProjektApiService,
    private fastighetsfilterService: FastighetsfilterService,
    private httpDownloadService: HttpDownloadService,
    private avtalApiService: AvtalApiService) { }

  cancel(projektId: uuid, avtalsjobbId: uuid): Observable<void> {
    return this.avtalApiService.cancelAvtalsjobb(projektId, avtalsjobbId)
      .pipe(map(() => this.reset(projektId)));
  }

  create(projektId: uuid, filter: MkAvtalsfilter, template: uuid, interval: number = 2000): Observable<AvtalsjobbProgress> {
    return this.projektApiService
      .createAvtalsjobb(projektId, { filter: this.fastighetsfilterService.getFastighetsfilter(filter, false), template: template })
      .pipe(
        tap(jobbId => localStorage.setItem(this.storageKey(projektId), jobbId)),
        switchMap(jobbId => this.pollProgress(jobbId, interval)));
  }

  getProgress(projektId: uuid, interval: number = 1000): Observable<AvtalsjobbProgress> {
    const jobbId = localStorage.getItem(this.storageKey(projektId));

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

  reset(projektId: uuid) {
    localStorage.removeItem(this.storageKey(projektId));
  }

  getData(projektId: uuid, avtalsjobbId: uuid) {
    this.httpDownloadService.httpDownload("/api/projekt/" + projektId + "/avtal/" + avtalsjobbId);
  }

  private pollProgress(jobbId: uuid, interval: number): Observable<AvtalsjobbProgress> {
    return timer(0, interval)
      .pipe(
        switchMap(_ => this.checkJobProgress(jobbId)),
        takeWhile(progress => progress.status === AvtalsjobbStatus.INPROGRESS, true));
  }

  private checkJobProgress(jobbId: uuid): Observable<AvtalsjobbProgress> {
    if (jobbId) {
      return this.avtalApiService.avtalsjobbProgress(jobbId);
    } else {
      return of({ id: null, status: AvtalsjobbStatus.NONE, total: null, generated: null });
    }
  }

  private storageKey(projektId: uuid): string {
    return "avtal_projekt_" + projektId;
  }

}
