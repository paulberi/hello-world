import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { uuid } from "../model/uuid";
import { AvtalApiService, DokumentmallApiService, Fastighetsfilter, FilterAndTemplate, ProjektApiService } from "../../../../../generated/markkoll-api";
import { HttpResponse } from "@angular/common/http";
import { flatMap, map, tap } from "rxjs/operators";
import { FastighetsfilterService } from "./fastighetsfilter.service";
import { MkAvtalsfilter } from "../model/avtalsfilter";
import FileSaver from "file-saver";
import {HttpDownloadService} from "../../../../lib/http/http-download.service";

export interface MkFile {
  name: string;
  blob: Blob;
}

@Injectable({
  providedIn: "root"
})
export class DokumentService {
  constructor(private projektApiService: ProjektApiService,
              private dokumentApiService: DokumentmallApiService,
              private avtalApiService: AvtalApiService,
              private httpDownloadService: HttpDownloadService,
              private fastighetsfilterService: FastighetsfilterService) {}

  getAvtalPDF(projektId: uuid, fastighetsId: uuid): Observable<MkFile> {
    return this.avtalApiService.getAvtal(projektId, fastighetsId, "response")
               .pipe(map(response => this.fileFromResponse(response)));
  }

  getDokumentmall(kundId: string, dokumentId: uuid) {
    this.httpDownloadService.httpDownload(`/api/kund/${kundId}/dokumentmall/${dokumentId}/data`);
  }

  getVarderingSkogsmarkXlsx(projektId: uuid, fastighetId: uuid): Observable<MkFile> {
    return this.projektApiService.getAvtalId(projektId, fastighetId)
               .pipe(
                 flatMap(avtalId => this.avtalApiService.getVarderingSkogsmarkAvtal(projektId, avtalId, "response")),
                 map(response => this.fileFromResponse(response)));
  }

  getVarderingSkogsmarkProjektXlsx(projektId: uuid, filter: MkAvtalsfilter): Observable<MkFile> {
    const fastighetsfilter = this.fastighetsfilterService.getFastighetsfilter(filter, false);

    return this.projektApiService
               .getVarderingSkogsmarkProjekt(projektId, fastighetsfilter, "response")
               .pipe(
                 map(response => this.fileFromResponse(response)),
                 tap(file => FileSaver.saveAs(file.blob, file.name)));
  }

  prepareDokumentmall(kundId: string, file: File): Observable<MkFile> {
    return this.dokumentApiService.prepareDokumentmall(kundId, file, "response")
               .pipe(map(response => this.fileFromResponse(response)));
  }

  generateForteckning(projektId: uuid, dokumentmallId: uuid, filter: MkAvtalsfilter): Observable<MkFile> {
    var filterAndTemplate: FilterAndTemplate = {
      template: dokumentmallId,
      filter: this.fastighetsfilterService.getFastighetsfilter(filter, false)
    };

    return this.projektApiService.generateForteckning(projektId, filterAndTemplate, "response")
               .pipe(
                  map(response => this.fileFromResponse(response)),
                  tap(file => FileSaver.saveAs(file.blob, file.name))
                );
  }

  private fileFromResponse(response: HttpResponse<Blob>): MkFile {
      return {
          name: this.getFileNameFromHttpResponse(response),
          blob: response.body
      };
  }

  private getFileNameFromHttpResponse(httpResponse: HttpResponse<Blob> ): string {
    const contentDispositionHeader = httpResponse.headers.get("Content-Disposition");
    const result = contentDispositionHeader.split(";")[1].trim().split("=")[1];
    return result.replace(/"/g, "");
  }
}
