import { HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import FileSaver from "file-saver";
import { Observable, of } from "rxjs";
import { map, switchMap, tap } from "rxjs/operators";
import { FilApiService } from "../../../../../generated/markkoll-api";
import { uuid } from "../model/uuid";
import { MkFile } from "./dokument.service";

@Injectable({
  providedIn: "root"
})
export class FilService {
  constructor(private filApiService: FilApiService) {}

  getFilData(filId: uuid): Observable<void> {
    return this.filApiService.getFilData(filId, "response")
      .pipe(
        map(response => this.fileFromResponse(response)),
        tap(mkFile => FileSaver.saveAs(mkFile.blob, mkFile.name)),
        switchMap(_ => of(void 0) as Observable<void>)
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
