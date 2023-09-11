import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ConfigService, MapConfig } from "../../../../lib/config/config.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: "root"
})
export class MatdatabasInitService {

  constructor(private http: HttpClient) {
  }

  init(handleConfigDownloadSuccess: (mapConfig: MapConfig) => void,
    handleConfigDownloadFailure: (error) => void) {
      this.getInitialConfigurationName()
        .subscribe(
        cfg => this.http.post<MapConfig>(ConfigService.urlConfig.configurationUrl, {
          app: ConfigService.appConfig.appName,
          config: cfg
        })
        .subscribe(
        mapConfig => handleConfigDownloadSuccess(mapConfig),
        error => handleConfigDownloadFailure(error)
        ),
        error => handleConfigDownloadFailure(error)
      );
  }

  private getInitialConfigurationName(): Observable<string> {
      return this.http.get(ConfigService.urlConfig.defaultConfigurationUrl, {responseType: "text"});
  }
}
