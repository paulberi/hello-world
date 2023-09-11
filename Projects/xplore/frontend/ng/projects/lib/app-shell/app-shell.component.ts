import {
  AfterViewChecked,
  Component, ElementRef,
  EventEmitter,
  HostListener,
  Input, OnDestroy,
  OnInit,
  Output,
  TemplateRef,
  ViewChild
} from "@angular/core";
import {MapComponent} from "../map-core/map.component";
import {OverlayContainer} from "@angular/cdk/overlay";
import {MatDialog} from "@angular/material/dialog";
import {MatIconRegistry} from "@angular/material/icon";
import {HelpDialogComponent} from "../map/components/dialogs/help-dialog/help-dialog.component";
import {InfoFastighetsgransDialogComponent} from "../map/components/dialogs/info-fastighetsgrans-dialog/info-fastighetsgrans-dialog.component";
import {ContactDialogComponent} from "../map/components/dialogs/contact-dialog/contact-dialog.component";
import {AboutDialogComponent} from "../map/components/dialogs/about-dialog/about-dialog.component";
import {DomSanitizer} from "@angular/platform-browser";
import {ConfigService, MapConfig} from "../config/config.service";
import {BehaviorSubject, Observable, of, Subject} from "rxjs";
import {LoginService, User} from "../oidc/login.service";
import {UrlService} from "../map/services/url.service";
import {ShareDialogComponent} from "../map/components/dialogs/share-dialog/share-dialog.component";
import {StorageItem, StorageService} from "../map-core/storage.service";
import {ExportUiService} from "../map/export/export-ui.service";
import {HttpClient} from "@angular/common/http";
import {EnvironmentConfigService} from "../config/environment-config.service";
import {GeovInitService} from "../config/geov-init.service";
import {ExportRequest} from "../map/export/export.service";
import {distinctUntilChanged, takeUntil} from "rxjs/operators";
import {KartmaterialDialogComponent} from "../map/components/dialogs/kartmaterial-dialog/kartmaterial-dialog.component";

@Component({
  selector: "app-shell",
  templateUrl: "./app-shell.component.html",
  styleUrls: ["./app-shell.component.scss"]
})
export class AppShellComponent implements OnInit, OnDestroy, AfterViewChecked {
  @Input() theme = "metria-dark-theme";
  @Input() leftPanelTemplateRef: TemplateRef<any>;
  @Input() leftBottomPanelTemplateRef: TemplateRef<any>;
  @Input() rightPanelTemplateRef: TemplateRef<any>;
  @Input() rightBottomPanelTemplateRef: TemplateRef<any>;
  @Input() miscTemplateRef: TemplateRef<any>; // Intended for elements that determine their own position (i.e attribution, etc)
  @Input() loginRequired: boolean;
  @Input() auth: boolean;
  @Input() externalInitalization = false;

  @Output() loggedIn = new EventEmitter<User>();
  @Output() mapConfigDownloaded = new EventEmitter<MapConfig>();

  hidePanelsAndTools = false;
  rightPanelClosed = true;
  mapConfig: Observable<MapConfig>;
  error = false;
  errorCode = null;

  windowWidth = window.innerWidth;

  @ViewChild("map") map: MapComponent;

  user: User;

  fitPadding$ = new BehaviorSubject<[number, number, number, number]>([0, 0, 0, 0]);

  private ngUnsubscribe = new Subject<void>();
  sizeChange = new Subject<any>();

  constructor(public loginService: LoginService, public overlayContainer: OverlayContainer,
              public dialog: MatDialog, public matIconRegistry: MatIconRegistry,
              public domSanitizer: DomSanitizer, public configService: ConfigService, private urlService: UrlService,
              private storageService: StorageService, private exportUiService: ExportUiService,
              private environmentConfigService: EnvironmentConfigService,
              private geovInitService: GeovInitService,
              private http: HttpClient) {

    if (!this.externalInitalization) {
      this.overlayContainer.getContainerElement().classList.add("metria-dark-theme");
    }
    // Note that this is only due to the fact that 'dashboard_outline' is not yet available, even
    // though present on Material Design icon list. Remove this special handling asap.
    this.matIconRegistry.addSvgIcon("valj_fastighet",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/lib/icons/outline-dashboard-24px.svg")
    );

    this.sizeChange
      .pipe(distinctUntilChanged((a, b) => a[0] === b[0] && a[1] === b[1]))
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((_) => {
        this.map.updateSize();
      });
  }

  ngOnInit() {
    this.calculateFitPadding();

    if (!this.externalInitalization) {
      this.geovInitService.handleQueryParams(true);

      if (this.loginRequired) {
        this.login();
      } else {
        this.init();
      }
    } else {
      this.mapConfig = of(this.configService.config);
    }
  }

  ngAfterViewChecked() {
    // SKOG-218: Kartan tog inte upp hela bredden på skärmen, men kör man en update så rättar det till sig.
    if (this.map) {
      this.sizeChange.next([this.map.mapElement.nativeElement.offsetWidth, this.map.mapElement.nativeElement.offsetHeight]);
    }
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }

  login() {
    this.loginService.loginCodeFlow(ConfigService.authConfig).then(user => {
        this.user = user;
        if (user.loggedIn) {
          this.loggedIn.emit(this.user);
          if (this.auth) {
            this.http.get(ConfigService.urlConfig.authUrl).subscribe(
              resp => this.init(),
              error => this.handleConfigDownloadFailure(error));
          } else {
            this.init();
          }
        }
      return true;
      }
    );
  }

  init() {
    this.geovInitService.init((mapConfig) => this.handleConfigDownloadSuccess(mapConfig),
        (error) => this.handleConfigDownloadFailure(error));
  }

  setMapConfig(mapConfig: MapConfig) {
    this.configService.setMapConfig(mapConfig);
    this.mapConfig = of(mapConfig);
  }

  private handleConfigDownloadSuccess(mapConfig: MapConfig) {
    if (mapConfig.app.theme) {
      this.theme = mapConfig.app.theme;
    } else {
      // Set application default theme in mapConfig so that it can be used in more places
      mapConfig.app.theme = this.theme;
    }

    this.overlayContainer.getContainerElement().classList.add(this.theme);

    if (this.mapConfigDownloaded.observers.length) {
      this.mapConfigDownloaded.emit(mapConfig);
    } else {
      this.setMapConfig(mapConfig);
    }
  }

  private handleConfigDownloadFailure(error) {
    this.error = true;
    this.errorCode = error.status;
  }

  logOut() {
    this.storageService.removeItem(StorageItem.QUERY_PARAMETERS);
    this.loginService.logout();
  }

  share() {
    this.dialog.open(ShareDialogComponent, {width: "25%", minWidth: "300px"});
  }

  export(appName: string, exportButtonText?: string, staticOrientation?: "portrait" | "landscape", exportCallback?: (exportRequest: ExportRequest) => Promise<void>) {
    this.hidePanelsAndTools = true;

    this.exportUiService.open(this.map.mapOverlay, appName, exportButtonText, staticOrientation, exportCallback).finally(() => {
      this.hidePanelsAndTools = false;
    });
  }

  help() {
    this.dialog.open(HelpDialogComponent, {width: "500px", maxWidth: "500px", minWidth: "200px"});
  }

  contact() {
    this.dialog.open(ContactDialogComponent, {width: "250px"});
  }

  infoFastighetsgrans() {
    this.dialog.open(InfoFastighetsgransDialogComponent, {width: "500px"});
  }

  about() {
    this.dialog.open(AboutDialogComponent, {width: "500px", maxWidth: "500px", minWidth: "200px"});
  }

  kartmaterial() {
    this.dialog.open(KartmaterialDialogComponent, {width: '500px'});
  }

  isVisible(id: string, fallback: boolean) {
    return this.getProperty(id, "visible", fallback);
  }

  getProperty(id: string, property: string, fallback: any) {
    const obj = this.configService.getProperty("app.components." + id);
    return obj && obj.hasOwnProperty(property) ? obj[property] : fallback;
  }

  @HostListener("window:resize", ["$event"])
  getScreenSize(event?) {
    this.windowWidth = window.innerWidth;

    this.calculateFitPadding();
  }

  isSmallDevice() {
    return this.windowWidth <= 660;
  }

  setRightPanelClosed(rightPanelClosed: boolean) {
    this.rightPanelClosed = rightPanelClosed;
    this.calculateFitPadding();
  }

  calculateFitPadding() {
    if (this.isSmallDevice()) {
      this.fitPadding$.next([0, 0, 0, 0]);
    } else {
      this.fitPadding$.next([0, this.rightPanelClosed ? 0 : 350, 50, 350]);
    }
  }
}
