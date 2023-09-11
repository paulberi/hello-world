import {AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild} from "@angular/core";
import {UserService} from "./services/user.service";
import {NavigationEnd, Router} from "@angular/router";
import {filter} from "rxjs/operators";
import {MatSidenav} from "@angular/material/sidenav";
import {MediaMatcher} from "@angular/cdk/layout";
import {LoginService} from "../../../lib/oidc/login.service";
import {MatdatabasInitService} from "./services/matdatabas-init.service";
import { ConfigService, MapConfig } from "../../../lib/config/config.service";
import { Observable, of } from "rxjs";

@Component({
  selector: "mdb-app-shell",
  template: `
    <mat-sidenav-container>
      <mat-sidenav #sidenav autoFocus="false" fixedInViewport="true" [mode]="mobileQuery.matches ? 'over' : 'side'">
        <mat-nav-list>
          <h3 mat-subheader>Start</h3>
          <a mat-list-item routerLink="/start" routerLinkActive="active">
            <span>Startsidan</span></a>

          <ng-container *ngIf="userDetailsService.userDetails.isObservator()">
            <h3 mat-subheader>Mätningar</h3>
            <a mat-list-item routerLink="/rapportera-matdata" *ngIf="userDetailsService.userDetails.isMatrapportor()"
               routerLinkActive="active">
              <span>Rapportera mätdata</span></a>
            <a mat-list-item routerLink="/rapportera-vattenkemi" *ngIf="userDetailsService.userDetails.isMatrapportor()"
               routerLinkActive="active">
              <span>Rapportera vattenkemi</span></a>
            <a mat-list-item routerLink="/import" *ngIf="userDetailsService.userDetails.isMatrapportor()"
               routerLinkActive="active">
              <span>Import av mätdata</span></a>
            <a mat-list-item routerLink="/export" *ngIf="userDetailsService.userDetails.isObservator()"
               routerLinkActive="active">
              <span>Export av data</span></a>
          </ng-container>

          <ng-container *ngIf="userDetailsService.userDetails.isObservator()">
            <h3 mat-subheader>Granskning</h3>
            <a mat-list-item routerLink="/granskning" *ngIf="userDetailsService.userDetails.isObservator()" routerLinkActive="active">
              <span>Granska mätvärden</span></a>
            <a mat-list-item routerLink="/larm" *ngIf="userDetailsService.userDetails.isTillstandshandlaggare()" routerLinkActive="active">
              <span>Visa larm</span></a>
            <a mat-list-item routerLink="/paminnelser" *ngIf="userDetailsService.userDetails.isMatrapportor()" routerLinkActive="active">
              <span>Visa påminnelser</span></a>
          </ng-container>

          <ng-container *ngIf="userDetailsService.userDetails.isObservator()">
            <h3 mat-subheader>Mätobjekt</h3>
            <a mat-list-item routerLink="/matobjekt" routerLinkActive="active">
              <span>Hantera mätobjekt</span></a>
            <a mat-list-item routerLink="/matobjektgrupp" *ngIf="userDetailsService.userDetails.isMatrapportor()" routerLinkActive="active">
              <span>Gruppera mätobjekt</span></a>
            <a mat-list-item routerLink="/matrunda" *ngIf="userDetailsService.userDetails.isMatrapportor()" routerLinkActive="active">
              <span>Skapa mätrundor</span></a>
          </ng-container>

          <ng-container *ngIf="userDetailsService.userDetails.isTillstandshandlaggare()">
            <h3 mat-subheader>Rapporter</h3>
            <a mat-list-item routerLink="/rapporter" routerLinkActive="active">
              <span>Hantera rapporter</span></a>
          </ng-container>

          <ng-container *ngIf="userDetailsService.userDetails.isAdmin()">
            <h3 mat-subheader>Inställningar</h3>
            <a mat-list-item routerLink="/anvandare" routerLinkActive="active">
              <span>Användare</span></a>
            <a mat-list-item routerLink="/anvandargrupp" routerLinkActive="active">
              <span>Användargrupper</span></a>
            <a mat-list-item routerLink="/definitionmatningstyp" routerLinkActive="active">
              <span>Definiera mätningstyper</span></a>
            <a mat-list-item routerLink="/larmnivaer" routerLinkActive="active">
              <span>Definiera larmnivåer</span></a>
            <a mat-list-item routerLink="/kartlager" routerLinkActive="active">
              <span>Kartlager</span></a>
            <a mat-list-item routerLink="/meddelanden" routerLinkActive="active">
              <span>Meddelanden</span></a>
            <a mat-list-item routerLink="/systemlogg" routerLinkActive="active">
              <span>Systemlogg</span></a>
          </ng-container>

          <mat-divider></mat-divider>
          <a mat-list-item routerLink="" (click)="logout()">
            <span>Logga ut</span></a>
        </mat-nav-list>
      </mat-sidenav>
      <mat-sidenav-content class="main-content">
        <div [class.start-header]="startPage"
             class="header mat-elevation-z4">
          <button mat-icon-button (click)="sidenav.toggle()">
            <mat-icon>menu</mat-icon>
          </button>
          <div class="logo">
            <h1>Metria Miljökoll</h1>
          </div>
        </div>

        <ng-container *ngIf="(mapConfig | async); let mapConfig; else showLoading">
        <div class="current-page">
          <router-outlet></router-outlet>
        </div>
        </ng-container>
        <ng-template #showLoading>
          <ng-container *ngIf="!this.error; else showError">
            <div class="loading">
              <mat-spinner class="loading-spinner" [diameter]="64"></mat-spinner>
              <h3 class="loading-text">Hämtar kartkonfiguration...</h3>
            </div>
          </ng-container>
        </ng-template>
        <ng-template #showError>
          <ng-container [ngSwitch]="this.errorCode">
            <table *ngSwitchCase="403" style="width:100%; height:100%">
              <tr>
                <td style="width:100%; text-align: center; vertical-align: bottom;">
                  <h3 style="float: bottom">Behörighet saknades för att använda denna applikation</h3>
                </td>
              </tr>
              <tr>
                <td style="width:100%; text-align: center; vertical-align: top">
                  <button mat-button class="okButton" (click)="logout()" style="margin-left: 10px">Logga ut</button>
                </td>
              </tr>
            </table>
            <table *ngSwitchDefault style="width:100%; height:100%">
              <tr>
                <td style="width:100%; text-align: center; vertical-align: bottom;">
                  <h3 style="float: bottom">Kunde inte hämta kartkonfigurationen</h3>
                </td>
              </tr>
              <tr>
                <td style="width:100%; text-align: center; vertical-align: top">
                  <button mat-button class="okButton" onclick="window.location.reload()" style="margin-right: 10px">Försök igen</button>
                  <button mat-button class="okButton" (click)="logout()" style="margin-left: 10px">Logga ut</button>
                </td>
              </tr>
            </table>
          </ng-container>
        </ng-template>

        <div class="footer">
          <div>
            <h3>Support</h3>
            <p> {{supportFooterText}} </p>
            <p>Metria Miljökoll utvecklas av Metria AB</p>
          </div>
          <img class="logo" src="./assets/metria_logo.png" alt="Metria Logo"/>
        </div>
      </mat-sidenav-content>
    </mat-sidenav-container>
  `,
  styleUrls: ["./app-shell.component.scss"]
})
export class AppShellComponent implements OnInit, AfterViewInit {
  mobileQuery: MediaQueryList;
  public startPage = false;

  mapConfig: Observable<MapConfig>;
  error = false;
  errorCode = null;

  supportFooterText = "";

  @ViewChild(MatSidenav) sidenav: MatSidenav;

  constructor(private router: Router,
              private media: MediaMatcher,
              private cd: ChangeDetectorRef,
              private loginService: LoginService,
              public userDetailsService: UserService,
              public configService: ConfigService,
              private matdatabasInitService: MatdatabasInitService) {
                
    this.mobileQuery = media.matchMedia("(max-width: 1024px)");
    this.mobileQuery.onchange = () => cd.detectChanges();

    this.router.events.subscribe((event: NavigationEnd) => {
      if (event instanceof NavigationEnd ) {
        if (event.urlAfterRedirects.startsWith("/start")) {
          this.startPage = true;
        } else {
          this.startPage = false;
        }
      }
    });
  }

  ngOnInit() {
    this.matdatabasInitService.init((mapConfig) => this.handleConfigDownloadSuccess(mapConfig),
    (error) => this.handleConfigDownloadFailure(error));
  }

  ngAfterViewInit() {
    // Close sidenav when navigation is complete.
    this.router.events.pipe(
      filter(e => e instanceof NavigationEnd)
    ).subscribe(e => {
      if (this.mobileQuery.matches) {
        this.sidenav.close();
      }
    });
  }

  logout() {
    this.loginService.logout();
  }

  private handleConfigDownloadSuccess(mapConfig: MapConfig) {
    this.configService.setMapConfig(mapConfig);
    this.mapConfig = of(mapConfig);
    this.supportFooterText = this.configService.config.app.startPageFooterText;
  }

  private handleConfigDownloadFailure(error) {
    this.error = true;
    this.errorCode = error.status;
  }
}
