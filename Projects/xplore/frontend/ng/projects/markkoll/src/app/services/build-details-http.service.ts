import { Injectable } from '@angular/core';
import { BuildDetailsService } from "./build-details.service";
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from "rxjs";
import { map } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class BuildDetailsHttpService {

    constructor(private http: HttpClient, private buildDetailsService: BuildDetailsService) {}

    getAppVersion(): Observable<string> {
      return this.http.get("index.html", {headers: new HttpHeaders().set("Cache-Control", "no-cache"), responseType: 'text'})
        .pipe(map(response => {
          const appVersionRegexp = /appVersion=\\"(.+?)\\"/;
          const match = JSON.stringify(response).match(appVersionRegexp);

          return match[1];
        }))
    }

    fetchBuildDetails(): Promise<void> {
      return new Promise((resolve) =>
        this.getAppVersion().toPromise().then(appVersion => {
          this.buildDetailsService.buildDetails.appVersion = appVersion;
          resolve();
      }));
    }
}
