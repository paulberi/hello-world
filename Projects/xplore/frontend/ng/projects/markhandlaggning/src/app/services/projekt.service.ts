import { Injectable } from "@angular/core";
import { HttpClient, HttpRequest, HttpHeaders } from "@angular/common/http";
import { DocumentRequest } from "../models/document-request.model";
import { environment } from "../../environments/environment";
import {publish, refCount, tap} from "rxjs/operators";
import { Observable } from "rxjs";
import { Projekt, Page } from "../components/projektlista/projektlista.component";

export interface GenerateDocumentResponse {
  serviceResponse: JSON;
}

@Injectable({
  providedIn: "root"
})
export class ProjektService {

  constructor(private http: HttpClient) { }

  private generateDocumentRequest(data): any {
    const request = new FormData();
    request.append("projektnr", data.projektNr);
    request.append("avtalstyp", data.avtalstyp);

    for (let index = 0; index < data.files.length; index++) {
      const filename = data.files[index].name;
      const fileExtension = filename.substring(filename.lastIndexOf(".") + 1, filename.length).toLowerCase();
      
      if (fileExtension === "zip" && request.get("shape") === null) {
        request.append("shape", data.files[index]);
      } else if ((fileExtension === "xlsx" || fileExtension === "xls") && request.get("markagarlista") === null) {
        request.append("markagarlista", data.files[index]);
      }
    }
    return request;
  }

  public createProjekt(formdata): any {
    const  headers = new HttpHeaders;
    headers.set("Content-Type", "multipart/form-data");
    return this.http.post(environment.projektUrl, this.generateDocumentRequest(formdata), {headers});
  }
  
  public getProjects(page: number, pagesize: number): Observable<Page<Projekt>> {
    return this.http.get<Page<Projekt>>(environment.projektUrl + "?page=" + page + "&pagesize=" + pagesize, {});
  }

  public deleteProjekt(id: String) {
    return this.http.delete(environment.projektUrl + "/" + id, {});
  }

  public updateProjektStatus(id: String, status: String) {
    return this.http.put(environment.projektUrl + "/" + id + "/status" + "/" + status, {});
  }

  public submitJob(id: String) {
    return this.http.post("/api/projekt/" + id + "/submitJob", {});
  }
}
