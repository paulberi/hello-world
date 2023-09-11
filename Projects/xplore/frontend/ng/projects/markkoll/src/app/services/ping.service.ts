import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root"
})
export class PingService {
  constructor(private httpClient: HttpClient) {
  }

  ping(): Observable<String> {
    return this.httpClient.get<String>("/api/ping", { responseType: "text" as "json" });
  }
}
