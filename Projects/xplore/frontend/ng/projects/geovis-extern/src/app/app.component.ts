import {Component, ViewChild} from '@angular/core';
import { MatDialog } from "@angular/material/dialog";
import {AppShellComponent} from "../../../lib/app-shell/app-shell.component";
import {UrlService} from "../../../lib/map/services/url.service";
import {User} from "../../../lib/oidc/login.service";
import {
  ConfigProperty,
  ConfigService, LaddaNedKartmaterialInfo,
  MapConfig,
  RightBottomPanelElements
} from "../../../lib/config/config.service";
import ExtractorUtil from "../../../lib/util/extractor.util";

@Component({
  selector: "xp-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"]
})
export class AppComponent {
  @ViewChild("appShell", {static: true}) appShell: AppShellComponent;

  adminLogin: boolean;
  standardMenu: boolean = true;
  rightBottomPanelElements: RightBottomPanelElements;
  exporteraKarta?: boolean = true;
  kontakt?: boolean = true;
  egenHjalp_Path?: string = "assets/help/help.html";
  omKarta_Path?: string = "";
  infoOmFastighetsgranser?: string = "";
  laddaNedKartmaterial: LaddaNedKartmaterialInfo[];
  laddaNedKartmaterial_Exists: boolean = false;

  constructor(private dialog: MatDialog, private configService: ConfigService, private urlService: UrlService) {
    this.adminLogin = this.urlService.getFullPath().indexOf("/admin") > -1;
  }

  loggedIn(user: User) {
    ConfigService.appConfig.layerEditing = user && user.roles && user.roles.indexOf("geovis_admin") > -1;
  }

  mapConfigDownloaded(mapConfig: MapConfig) {
    if (this.adminLogin) {
      const defaultUrls = ExtractorUtil.extractObject(ConfigProperty.DEFAULT_URLS, mapConfig);
      defaultUrls["ows"].url = "/admin" + defaultUrls["ows"].url;
    }
    this.appShell.setMapConfig(mapConfig);
    if ( this.configService.config.app.rightBottomPanelElements !== undefined) {
      this.rightBottomPanelElements = this.configService.config.app.rightBottomPanelElements;
      this.standardMenu = false;
      if (this.rightBottomPanelElements.exporteraKarta !== undefined) {
        this.exporteraKarta = this.rightBottomPanelElements.exporteraKarta
      } else {
        this.exporteraKarta = false
      }

      if (this.rightBottomPanelElements.kontakt !== undefined) {
        this.kontakt = this.rightBottomPanelElements.kontakt
      } else {
        this.kontakt = false
      }

      if (this.rightBottomPanelElements.infoOmFastighetsgranser !== undefined && this.configService.config.app.rightBottomPanelElements.infoOmFastighetsgranser !== "")
        this.infoOmFastighetsgranser = this.configService.config.app.rightBottomPanelElements.infoOmFastighetsgranser

      if (this.rightBottomPanelElements.egenHjalp_Path !== undefined && this.rightBottomPanelElements.egenHjalp_Path !== "")
        this.egenHjalp_Path = this.rightBottomPanelElements.egenHjalp_Path;

      if (this.rightBottomPanelElements.omKarta_Path !== undefined && this.configService.config.app.rightBottomPanelElements.omKarta_Path !== "")
        this.omKarta_Path = this.rightBottomPanelElements.omKarta_Path;

      if (this.rightBottomPanelElements.laddaNedKartmaterial !== undefined) {
        if (this.rightBottomPanelElements.laddaNedKartmaterial[0].mapName !== undefined &&
          this.rightBottomPanelElements.laddaNedKartmaterial[0].title !== undefined &&
          this.rightBottomPanelElements.laddaNedKartmaterial[0].lanCatalogPath !== undefined &&
          this.rightBottomPanelElements.laddaNedKartmaterial[0].kommunCatalogPath !== undefined) {
          this.laddaNedKartmaterial_Exists = true;
        } else {
          this.laddaNedKartmaterial_Exists = false;
        }
      }
    }
  }
}
