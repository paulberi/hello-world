import {AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild} from "@angular/core";
import {UserService} from "./../services/user.service";
import {NavigationEnd, Router, RoutesRecognized} from "@angular/router";
import {filter, map} from "rxjs/operators";
import {MatSidenav} from "@angular/material/sidenav";
import {MediaMatcher} from "@angular/cdk/layout";
import {LoginService, User} from "../../../../lib/oidc/login.service";
import { MenuItem, XpLayoutUserInfo } from "../../../../lib/ui/layout/layout.component";
import { DialogService } from "../../../../lib/dialogs/dialog.service";
import { ConfirmationDialogModel } from "../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import { InformationDialogModel } from "../../../../lib/dialogs/information-dialog/information-dialog.component";
import { TemplateRef } from "@angular/core";
import { XpUserService } from "../../../../lib/user/user.service";
import { Observable } from "rxjs";
import { XpErrorMap, XpErrorService } from "../../../../lib/error/error.service";
import { XpDefaultErrorMessage } from "../../../../lib/error/error.message";
import { TranslocoService } from "@ngneat/transloco";

@Component({
  selector: "adm-app-shell",
  templateUrl: "./app-shell.component.html",
  styleUrls: ["./app-shell.component.scss"]
})
export class AppShellComponent implements OnInit {
  mobileQuery: MediaQueryList;
  public startPage = false;
  @ViewChild("helpTemplate") helpTemplate: TemplateRef<any>;

  xpUser: Observable<XpLayoutUserInfo>;


  menuItems: MenuItem[] = [
    {
      title: "Administrera",
      selected: true,
      path: "/start",
      icon: "work_outline"
    },
    {
      title: "Skapa & Redigera",
      selected: false,
      path: "/kund",
      icon: "work_outline",
    }
  ];

  @ViewChild(MatSidenav) sidenav: MatSidenav;

  constructor(private router: Router,
              private media: MediaMatcher,
              private cd: ChangeDetectorRef,
              private loginService: LoginService,
              private dialogService: DialogService,
              private xpUserService: XpUserService,
              private errorService: XpErrorService,
              private translateService: TranslocoService,
              public userDetailsService: UserService) {
    this.mobileQuery = media.matchMedia("(max-width: 1024px)");
    this.mobileQuery.onchange = () => cd.detectChanges();
    this.xpUser = xpUserService.getUser$().pipe(map(user => {
      return {
        id: user.claims.email,
        fornamn: user.claims.given_name,
        efternamn: user.claims.family_name,
        email: user.claims.email
      } as XpLayoutUserInfo;
    }));

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
    // Close sidenav when navigation is complete.
    this.router.events.pipe(
      filter(e => e instanceof NavigationEnd)
    ).subscribe(e => {
      if (this.mobileQuery.matches) {
        this.sidenav.close();
      }
    });
    this.setActivePage(this.router.url);

    this.errorService.setErrorMap(this.translateErrorMap());

    this.router.events.subscribe((event) => {
      if (event instanceof RoutesRecognized) {
        this.setActivePage(event.urlAfterRedirects);
      }
    });
  }

  logout() {
    this.loginService.logout();
  }

  changePage(menuItem: MenuItem) {
    this.router.navigateByUrl(menuItem.path);
  }

  translateErrorMap(): XpErrorMap[] {    
    const StringIsNumber = value => isNaN(Number(value)) === false;
    return Object.keys(XpDefaultErrorMessage)
      .filter(StringIsNumber)
      .map(key => ({
        error: XpDefaultErrorMessage[key],
        message: this.translateService.translate("xp.errors." + XpDefaultErrorMessage[key])
      } as XpErrorMap));
  }

  setActivePage(path: string) {

    if (this.menuItems !== undefined) {
      this.menuItems.forEach(item => {
        // Huvudmenyn
        path.startsWith(item.path) ?
          item.selected = true : item.selected = false;

        // Undermenyn
        if (item.items !== undefined) {
          item.items.forEach(subItem => {
            if (subItem.path === path) {
              subItem.selected = true;
              item.selected = true;
            } else {
              subItem.selected = false;
            }
          });
        }
      });
    }
  }  

  openLogoutDialog() {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        "Logga ut",
        "Är du säker på att du vill logga ut?",
        "Avbryt",
        "Logga ut"),
      dialogResult => {
        if (dialogResult) {
          this.loginService.logout();
        }
      });
  }
    openHelpDialog() {
      this.dialogService.showInformationDialog(
        new InformationDialogModel(
          "Hjälp",
          this.helpTemplate
        ));
    }
}
