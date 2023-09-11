import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from "@angular/common/http";
import { saveAs } from "file-saver";
import {MatningFilter, MatningstypMatobjekt, MatningstypMatobjektFilter} from "./matobjekt.service";
import {Observable} from "rxjs";
import {Page} from "../common/page";


export enum ExportTyp {
  MATOBJEKT= "MATOBJEKT"
}

@Injectable({
  providedIn: "root"
})
export class ExportService {
  constructor(private http: HttpClient) {
  }

  getExportMatningstyperPage(page: number, size: number, sortProperty: string, sortDirection: string, filter?: MatningstypMatobjektFilter,
                             matningFilter?: MatningFilter):
    Observable<Page<MatningstypMatobjekt>> {
    let params = new HttpParams();

    if (page != null) {
      params = params.append("page", String(page));
    }
    if (size != null) {
      params = params.append("size", String(size));
    }
    if (sortProperty != null) {
      params = params.append("sortProperty", sortProperty);
    }
    if (sortDirection != null) {
      params = params.append("sortDirection", sortDirection);
    }

    if (matningFilter) {
      if (matningFilter.fromDatum) {
        params = params.append("fromDatum", matningFilter.fromDatum);
      }

      if (matningFilter.tomDatum) {
        params = params.append("tomDatum", matningFilter.tomDatum);
      }
    }

    return this.http.post<Page<MatningstypMatobjekt>>("/api/export/matningstyper", filter, {params});
  }


  downloadExportMatningstyp(exportTyp: ExportTyp, filter: MatningstypMatobjektFilter, matningFilter: MatningFilter) {
    let httpParams = new HttpParams();
    httpParams = httpParams.set("rapportTyp", exportTyp);

    if (matningFilter) {
      if (matningFilter.fromDatum) {
        httpParams = httpParams.append("fromDatum", matningFilter.fromDatum);
      }

      if (matningFilter.tomDatum) {
        httpParams = httpParams.append("tomDatum", matningFilter.tomDatum);
      }
    }

    return this.http
      .post("/api/export/export", filter, {
        params: httpParams,
        responseType: "blob",
        observe: "response"
      })
      .toPromise()
      .then(response => {
        saveAs(response.body, this.getFileNameFromHttpResponse(response));
      });
  }

  private getFileNameFromHttpResponse(httpResponse: HttpResponse<Blob> ) {
    const contentDispositionHeader = httpResponse.headers.get("Content-Disposition");
    const result = contentDispositionHeader.split(";")[1].trim().split("=")[1];
    return result.replace(/"/g, "");
  }
}
