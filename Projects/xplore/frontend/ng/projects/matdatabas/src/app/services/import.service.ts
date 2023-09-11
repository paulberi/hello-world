import {Observable} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";
import {ImportMatning} from "./matobjekt.service";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: "root"
})
export class ImportService {
  constructor(private http: HttpClient) {
  }

  parseImport(format: string, kallsystem: string, file: File): Observable<ImportMatning[]> {
    const params = new HttpParams()
      .append("kallsystem", kallsystem)
      .append("format", format);
    const formData = new FormData();
    formData.append("file", file);
    return this.http.post<ImportMatning[]>("/api/import/matningar/parseimport", formData, {params: params});
  }

  executeImport(matrapportor: string, kallsystem: string,  matningar: ImportMatning[] ): Observable<void> {
    return this.http.post<void>("/api/import/matningar/import", matningar, {params: {matrapportor: matrapportor, kallsystem: kallsystem}});
  }
}
