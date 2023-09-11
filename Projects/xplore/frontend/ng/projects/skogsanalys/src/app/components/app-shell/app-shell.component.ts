import { Component, OnInit } from "@angular/core";
import {Observable, of} from "rxjs";
import {LoginService} from "../../../../../lib/oidc/login.service";
import {ConfigService, MapConfig} from "../../../../../lib/config/config.service";
import {GeovInitService} from "../../../../../lib/config/geov-init.service";
import {OverlayContainer} from "@angular/cdk/overlay";
import {StorageItem, StorageService} from "../../../../../lib/map-core/storage.service";

@Component({
  selector: "xp-app-shell",
  templateUrl: "./app-shell.component.html",
  styleUrls: ["./app-shell.component.scss"]
})
export class AppShellComponent implements OnInit {
  error = false;
  errorCode = null;
  mapConfig: Observable<MapConfig>;
  theme = "metria-dark-theme";

  constructor(private geovInitService: GeovInitService,
              public configService: ConfigService,
              private storageService: StorageService,
              public loginService: LoginService,
              public overlayContainer: OverlayContainer) {
  }

  ngOnInit() {
    this.geovInitService.handleQueryParams(true);
    this.geovInitService.init((mapConfig) => this.handleConfigDownloadSuccess(mapConfig),
      (error) => this.handleConfigDownloadFailure(error));
  }

  private handleConfigDownloadSuccess(mapConfig: MapConfig) {

      if (mapConfig.app.theme) {
        this.theme = mapConfig.app.theme;
      } else {
        // Set application default theme in mapConfig so that it can be used in more places
        mapConfig.app.theme = this.theme;
      }

      this.overlayContainer.getContainerElement().classList.add(this.theme);

      this.setMapConfig(mapConfig);
    }

  private handleConfigDownloadFailure(error) {
    this.error = true;
    this.errorCode = error.status;
  }

  setMapConfig(mapConfig: MapConfig) {
    this.configService.setMapConfig(mapConfig);
    this.mapConfig = of(mapConfig);
  }

  logOut() {
    this.storageService.removeItem(StorageItem.QUERY_PARAMETERS);
    this.loginService.logout();
  }
}
