import { Component, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { Router, RoutesRecognized } from "@angular/router";
import { Observable, of } from "rxjs";
import { MenuItem, XpLayoutUserInfo } from "../../../../lib/ui/layout/layout.component";
import { AuthService } from "../services/auth.service";
import { TranslocoService } from "@ngneat/transloco";
import { XpErrorMap, XpErrorService } from "../../../../lib/error/error.service";
import { MarkkollError } from "../app.config";
import { DialogService } from "../../../../lib/dialogs/dialog.service";
import { InformationDialogModel } from "../../../../lib/dialogs/information-dialog/information-dialog.component";
import { ConfirmationDialogModel } from "../../../../lib/dialogs/confirmation-dialog/confirmation-dialog.component";
import { MarkkollUser, NisKalla } from "../../../../../generated/markkoll-api";
import { MkUserService } from "../services/user.service";
import { switchMap } from "rxjs/operators";
import { MkInstallningarService } from "../services/installningar.service";

@Component({
  selector: "mk-app-shell",
  templateUrl: "./app-shell.component.html",
  styleUrls: ["./app-shell.component.scss"]
})
export class AppShellComponent implements OnInit {
  markkollUser: Observable<MarkkollUser>;
  xpLayoutUserInfo: XpLayoutUserInfo;
  nisKalla: NisKalla;

  @ViewChild("helpTemplate") helpTemplate: TemplateRef<any>;

  constructor(
    private authService: AuthService,
    private router: Router,
    private errorService: XpErrorService,
    private translateService: TranslocoService,
    private mkUserService: MkUserService,
    private installningarService: MkInstallningarService,
    private dialogService: DialogService) {
      this.markkollUser = this.mkUserService.getMarkkollUser$();
  }

  menuItems: MenuItem[];
  analysMenuItem: MenuItem = {
    title: "mk.analysPoc.menuTitle",
    selected: false,
    path: "/analys-poc",
    icon: "emoji_objects",
  };

  adminMenuItem: MenuItem = {
    title: "mk.appShell.admin",
    selected: false,
    path: "/admin",
    icon: "admin_panel_settings",
  };

  projektMenuItem: MenuItem = {
    title: "mk.appShell.project",
    selected: true,
    icon: "work_outline",
    items: [{
      title: "mk.appShell.projectList",
      selected: true,
      path: "/projekt",
      icon: "format_list_bulleted",
    },
    {
      title: "mk.createProjekt.menuTitle",
      selected: false,
      path: "/skapa-projekt",
      icon: "add",
    }
    ]
  };

  ngOnInit() {
      this.markkollUser.pipe(
        switchMap((markkollUser: MarkkollUser) => {
          this.xpLayoutUserInfo = {
            id: markkollUser.email,
            fornamn: markkollUser.fornamn,
            efternamn: markkollUser.efternamn,
            email: markkollUser.email,
            kund: markkollUser.kundId
          };
          return this.installningarService.getNisKalla(markkollUser.kundId);
        }
        ))
      .subscribe(nisKalla => {
        this.nisKalla = nisKalla;
        this.setupMenu();
      });

      this.installningarService.nisKalla$.subscribe(res => {
        this.nisKalla = res;
        this.setupMenu();
      });

    // Mappning av felmeddelanden
    this.errorService.setErrorMap(this.translateErrorMap());

    this.router.events.subscribe((event) => {
      if (event instanceof RoutesRecognized) {
        this.setActivePage(event.urlAfterRedirects);
      }
    });
  }

  setupMenu() {
    const menuItems = [this.projektMenuItem];
    
    // Om kunden inte har valt NIS-Källor så ska inte projektfliken synas
    if (!this.nisKalla || Object.values(this.nisKalla).every(val => val === false)) {
      menuItems.splice(menuItems.indexOf(this.projektMenuItem), 1);
    }
    // Om användaren har rollen för analys så lägg till meny-knappen
    if (this.authService.isAnalysAllowed() && menuItems.indexOf(this.analysMenuItem) < 0) {
      menuItems.push(this.analysMenuItem);
    }
    if (this.authService.isAdminAllowed() && menuItems.indexOf(this.adminMenuItem) < 0) {
      menuItems.push(this.adminMenuItem);
    }

    this.menuItems = this.translateMenuItems(menuItems);
    this.setActivePage(this.router.url);
  }

  translateMenuItems(menuItems: MenuItem[]): MenuItem[] {
    return menuItems.map(item => this.translateMenuItem(item));
  }

  translateMenuItem(menuItem: MenuItem): MenuItem {
    const items = menuItem.items ? menuItem.items.map(item => this.translateMenuItem(item)) : [];

    return {
      title: this.translateService.translate(menuItem.title),
      selected: menuItem.selected,
      path: menuItem.path,
      icon: menuItem.icon,
      items: items
    };
  }

  /**
   * Översätt felmeddelanden utifrån enums i app.config
   */
  translateErrorMap(): XpErrorMap[] {    
    const StringIsNumber = value => isNaN(Number(value)) === false;
    return Object.keys(MarkkollError)
      .filter(StringIsNumber)
      .map(key => ({
        error: MarkkollError[key],
        message: this.translateService.translate("mk.errors." + MarkkollError[key])
      } as XpErrorMap));
  }

  /**
   * Navigera till sida.
   */
  changePage(menuItem: MenuItem) {
    this.router.navigateByUrl(menuItem.path);
  }

  /** 
   * Sätt valt menyalternativ.
   */
   setActivePage(path: string) {

    if (this.menuItems !== undefined) {
      this.menuItems.forEach(item => {
        // Huvudmenyn
        item.path === path ?
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

  /**
   * Öppna dialog för att logga ut.
   */
  openLogoutDialog() {
    this.dialogService.showConfirmationDialog(
      new ConfirmationDialogModel(
        this.translateService.translate("xp.common.logOut"),
        this.translateService.translate("xp.layout.logOutConfirm"),
        this.translateService.translate("xp.common.cancel"),
        this.translateService.translate("xp.common.logOut")),
      dialogResult => {
        if (dialogResult) {
          this.authService.logout();
        }
      });
    }
  
  /**
   * Öppna dialog för hjälp.
   */
  openHelpDialog() {
    this.dialogService.showInformationDialog(
      new InformationDialogModel(
        this.translateService.translate("xp.common.help"),
        this.helpTemplate
      ));
  }
}
