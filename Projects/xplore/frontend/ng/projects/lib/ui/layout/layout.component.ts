import { Component, EventEmitter, Input, Output } from "@angular/core";
import { MatIconRegistry } from "@angular/material/icon";
import { DomSanitizer } from "@angular/platform-browser";
import { TranslocoService } from "@ngneat/transloco";

export interface MenuItem {
  title: string;
  selected: boolean;
  path?: string;
  icon?: string;
  items?: MenuItem[];
}

export interface XpLayoutUserInfo {
  id: string;
  fornamn: string;
  efternamn: string;
  email: string;
  kund: string;
}

/**
 * Bygger upp Xplores gemensamma layout med sidhuvud, sidfot och plats för innehåll.
 */
@Component({
  selector: "xp-layout-ui",
  templateUrl: "./layout.component.html",
  styleUrls: ["./layout.component.scss"]
})
export class XpLayoutComponent {

  /**
   * Inloggad användare i applikationen.
   */
  @Input() loggedInUserInfo: XpLayoutUserInfo;

  /**
   * Applikationens namn.
   */
  @Input() appName: string;

  /**
   * Ska fothuvud visas eller inte.
   */
  @Input() isFooterVisible = true;

  /**
   * Ska hjälp visas i menyn.
   */
  @Input() isHelpVisible = true;

  /**
   * Lista med alternativ för navigationsmenyn.
   */
  @Input() menuItems: MenuItem[];

  /**
   * Event när användaren klickar på logga ut.
   */
  @Output() logoutClick = new EventEmitter<string>();

  /**
   * Event när användaren klickar på hjälp.
   */
  @Output() helpClick = new EventEmitter<void>();

  /**
   * Event när användaren byter sida i menyn.
   */
  @Output() pageChange = new EventEmitter<MenuItem>();

  constructor(
    private iconRegistry: MatIconRegistry,
    private sanitizer: DomSanitizer,
    private translateService: TranslocoService,
    ) {
    iconRegistry.addSvgIcon("logo", sanitizer.bypassSecurityTrustResourceUrl("assets/lib/icons/metria_logo.svg"));
    iconRegistry.addSvgIcon("logo_white", sanitizer.bypassSecurityTrustResourceUrl("assets/lib/icons/metria_logo_white.svg"));
  }  
}
