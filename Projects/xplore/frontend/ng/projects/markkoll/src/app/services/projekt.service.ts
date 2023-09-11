import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { XpPage } from "../../../../lib/ui/paginated-table/page";
import { SortDirection } from "@angular/material/sort";
import { map } from "rxjs/operators";
import {
  ElnatProjekt,
  FiberProjekt,
  IndataTyp,
  ProjektApiService,
  ProjektInfo,
  ProjektLoggFilter,
  ProjektLoggItem,
  Version,
  SortDirection as SortDirectionDto,
  DokumentmallApiService,
  FastighetDelomradeInfo,
  ProjektIntrang
} from "../../../../../generated/markkoll-api";
import { HttpClient } from "@angular/common/http";
import { uuid } from "../model/uuid";
import { Dokumentmall } from "../../../../../generated/markkoll-api/model/dokumentmall";
import { SaveProjektdokument } from "../markhandlaggning/projekt/projektdokument/projektdokument.component";

/**
 * Projektrelaterade tjänster
 */
@Injectable({
  providedIn: "root"
})

export class ProjektService {
  constructor(
    private projektApiService: ProjektApiService,
    private dokumentmallApiService: DokumentmallApiService,
    private httpClient: HttpClient) { }

  /**
   * Skapa ett nytt fiberprojekt.
   */
  createFiberProjekt(kundId: string, fiberProjekt: FiberProjekt, file?: Blob): Observable<FiberProjekt> {
    return this.projektApiService.createFiberProjekt(kundId, fiberProjekt, file);
  }

  /**
   * Skapa ett nytt elnätsprojekt.
   */
  createElnatProjekt(kundId: string, elnatProjekt: ElnatProjekt, file?: Blob): Observable<ElnatProjekt> {
    return this.projektApiService.createElnatProjekt(kundId, elnatProjekt, file);
  }

  /**
   * Hämta fiberprojekt
   */
  getFiberProjekt(projektId: uuid): Observable<FiberProjekt> {
    return this.projektApiService.getFiberProjekt(projektId);
  }

  /**
   * Hämta elnätsprojekt
   */
  getElnatProjekt(projektId: uuid): Observable<ElnatProjekt> {
    return this.projektApiService.getElnatProjekt(projektId);
  }

  /**
   * Uppdatera fiberprojekt
   */
  updateFiberProjekt(projektId: uuid, fiberprojekt: FiberProjekt): Observable<FiberProjekt> {
    return this.projektApiService.updateFiberProjekt(projektId, fiberprojekt);
  }

  /**
   * Uppdatera elnätsprojekt
   */
  updateElnatProjekt(projektId: uuid, elnatprojekt: ElnatProjekt): Observable<ElnatProjekt> {
    return this.projektApiService.updateElnatProjekt(projektId, elnatprojekt);
  }


  /**
  * Hämta en sida med projekt
  *
  * @param pageNum       Sidnummer, där 0 är första sidan
  * @param size          Önskad sidstorlek
  * @param sortDirection Önskad sorteringsordning
  * @param sortProperty  Vilken egenskap projekten ska sorteras på
  * @param namnFilter    Filtrera efter projektnamn
  *
  * @returns En {@link Observable} med sidan.
  */
  getProjektPage(page: number,
                 size: number,
                 sortDirection: SortDirection = "",
                 sortProperty: string = "namn",
                 namnFilter: string = null):
                 Observable<XpPage<ProjektInfo>> {

    return this.projektApiService
               .projektPage(page, size, sortProperty, sortDirection, namnFilter)
               .pipe(map(projektPage => projektPage as XpPage<ProjektInfo>));
  }

  /**
  * Hämta projekt med ett visst ID.
  * @param id Id för projektet.
  */
  getProjektInfo(projektId: uuid): Observable<ProjektInfo> {
    return this.projektApiService.getProjekt(projektId);
  }

  getProjektName(projektId: uuid): Observable<string> {
    return this.getProjektInfo(projektId).pipe(map(p => p.namn));
  }

  updateVersion(projektId: uuid, indataTyp: IndataTyp, shapeFile: File, buffert: number): Observable<Version> {
      return this.projektApiService.updateVersion(projektId, indataTyp, buffert, shapeFile);
  }

  deleteVersion(projektId: uuid, versionId: uuid): Observable<void> {
    return this.projektApiService.deleteVersion(projektId, versionId);
  }

  restoreVersion(projektId: uuid, versionId: uuid): Observable<void> {
    return this.projektApiService.restoreVersion(projektId, versionId);
  }

  getVersions(id: uuid): Observable<Version[]> {
    return id ? this.projektApiService.projektVersions(id) : of([]);
  }

  hasVersions(projektId: uuid): Observable<boolean> {
    return this.getVersions(projektId).pipe(map(v => v.length > 0));
  }

  createDokumentmall(kundId: uuid, saveProjektdokument: SaveProjektdokument): Observable<Dokumentmall> {
    return this.dokumentmallApiService.createDokumentmall(kundId, saveProjektdokument.file, saveProjektdokument.projektdokumentInfo);
  }

  updateDokumentmall(kundId: string, dokument: Dokumentmall): Observable<Dokumentmall> {
    return this.dokumentmallApiService.updateDokumentmall(kundId, dokument.id, dokument);
  }

  deleteDokument(projektid: uuid, dokumentId: uuid): Observable<void> {
    return this.dokumentmallApiService.deleteDokumentmall(projektid, dokumentId);
  }

  getDokumentmallarInfo(kundId: uuid): Observable<Dokumentmall[]> {
    return this.dokumentmallApiService.getKundDokumentmallar(kundId);
  }

  getAvtalId(projektId, fastighetId): Observable<string> {
    return this.projektApiService.getAvtalId(projektId, fastighetId);
  }

  /**
  * Hämta metadata för ett visst projekt.
  * @param id Id för projektet.
  */
 getProjektMeta(projektId: uuid): Observable<String> {
   return this.projektApiService.getProjektMeta(projektId);
  }

  deleteProjekt(projektId: uuid): Observable<void> {
    return this.projektApiService.deleteProjekt(projektId);
  }

  getProjektloggPage(projektId: uuid,
                     pageNum: number,
                     pageSize: number,
                     filter: ProjektLoggFilter[],
                     sortDirection: SortDirection): Observable<XpPage<ProjektLoggItem>> {
    return this.projektApiService.getProjektLoggPage(projektId, pageNum, pageSize, filter,
      sortDirection as SortDirectionDto).pipe(map(projektloggPage => projektloggPage as XpPage<ProjektLoggItem>));
  }

  getProjektintrang(projektId): Observable<ProjektIntrang[]> {
    return this.projektApiService.getProjektintrang(projektId);
  }

  getProjektintrangWithBuffert(projektId): Observable<string> {
    return this.projektApiService.getProjektintrangWithBuffert(projektId);
  }

  getCurrentVersion(projektId): Observable<Version> {
    return this.projektApiService.projektCurrentVersion(projektId);
  }

  getDelomradenForProjekt(projektId): Observable<FastighetDelomradeInfo[]> {
    return this.projektApiService.getDelomradenForProjekt(projektId);
  }

  addFastigheter(projektId: uuid, fastighetIds: uuid[]): Observable<void> {
    return this.projektApiService.addFastigheter(projektId, fastighetIds);
  }

  excludeFastigheter(projektId: uuid, fastighetIds: uuid[]): Observable<void> {
    return this.projektApiService.excludeFastigheter(projektId, fastighetIds);
  }

  updateVersionIntrang(projektId: uuid, projektIntrang: ProjektIntrang[]): Observable<Version> {
    return this.projektApiService.updateVersionIntrang(projektId, projektIntrang);
  }
}
