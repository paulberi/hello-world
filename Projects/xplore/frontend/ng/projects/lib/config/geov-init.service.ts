import {EventEmitter, Injectable, Input} from "@angular/core";
import {ConfigService, MapConfig} from "./config.service";
import {UrlService} from "../map/services/url.service";
import {HttpClient} from "@angular/common/http";
import {EnvironmentConfigService} from "./environment-config.service";
import {Observable, of} from "rxjs";
import {OverlayContainer} from "@angular/cdk/overlay";
import {StorageItem, StorageService} from "../map-core/storage.service";
import JSURL from "jsurl/lib/jsurl";

@Injectable({
  providedIn: "root"
})
export class GeovInitService {
  clearQuery = false;

  constructor(private urlService: UrlService,
              private environmentConfigService: EnvironmentConfigService,
              public configService: ConfigService,
              private storageService: StorageService,
              private http: HttpClient) {
  }

  init(handleConfigDownloadSuccess: (mapConfig: MapConfig) => void,
       handleConfigDownloadFailure: (error) => void) {

    if (this.clearQuery && (window.location.pathname === "/" || window.location.pathname === "")) {
      this.urlService.clearQuery(); // Clear query parameters on root page
    }


    this.http.post<any>(ConfigService.urlConfig.configurationUrl, {
      config: "environment-config"
    }).subscribe(environmentConfig => {
        this.environmentConfigService.setConfig(environmentConfig);

        this.configService.loadInitialParameters();
        this.getInitialConfigurationName().subscribe(
          cfg => this.http.post<MapConfig>(ConfigService.urlConfig.configurationUrl, {
            app: ConfigService.appConfig.appName,
            config: cfg
          }).subscribe(
            mapConfig => handleConfigDownloadSuccess(mapConfig),
            error => handleConfigDownloadFailure(error)
          ),
          error => handleConfigDownloadFailure(error)
        );
      },
      error => handleConfigDownloadFailure(error)
    );
  }

  private getInitialConfigurationName(): Observable<string> {
    if (ConfigService.appConfig.configuration) {
      return of(ConfigService.appConfig.configuration);
    } else {
      return this.http.get(ConfigService.urlConfig.defaultConfigurationUrl, {responseType: "text"});
    }
  }

  handleQueryParams(clearQuery: boolean) {
    const parametersObject = this.buildParamsObject();

    if (parametersObject) {
      this.storageService.setItem(StorageItem.QUERY_PARAMETERS, parametersObject);

      this.clearQuery = clearQuery;
    }
  }

  private buildParamsObject() {
    let paramFound = false;
    const queryParams: any = this.urlService.getParams();
    const params: any = {};

    if (queryParams.cfg) {
      params.cfg = queryParams.cfg;
      paramFound = true;
    }
    if (queryParams.x) {
      params.x = +queryParams.x;
      paramFound = true;
    }
    if (queryParams.y) {
      params.y = +queryParams.y;
      paramFound = true;
    }
    if (queryParams.zoom) {
      params.zoom = +queryParams.zoom;
      paramFound = true;
    }
    if (queryParams.layers) {
      params.layers = {};

      const layers = queryParams.layers.split("|").map(item => item.trim());
      for (const layer of layers) {
        params.layers[layer] = true;
      }

      paramFound = true;
    }
    if (queryParams.lay) {
      params.lay = JSURL.parse(queryParams.lay);
      paramFound = true;
    }

    if (paramFound) {
      return params;
    } else {
      return null;
    }
  }
}
