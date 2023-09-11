import { Injectable } from "@angular/core";
import { HttpClient, HttpEvent, HttpHeaders, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment";
import { map } from "rxjs/internal/operators/map";

@Injectable({
  providedIn: "root"
})
export class AvtalService {

  constructor(private http: HttpClient) { }

  public getAvtalURL(id): Observable<string> {
    return this.http.get(environment.hamtaAvtalUrl + id, {responseType: "text"});
  }
}
