import { Component, Input, OnInit } from "@angular/core";
import { MatIconRegistry } from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";
import { TranslocoService } from "@ngneat/transloco";

/**
 * Meddela användaren att det inte går att hitta sidan 
 * och ge möjlighet att navigera till en länk. 
 * Används tex. vid 404-svar från backend.
 */
@Component({
  selector: "xp-not-found",
  templateUrl: "./not-found.component.html",
  styleUrls: ["./not-found.component.scss"]
})
export class XpNotFoundComponent {

  /**
   * Titel som visas på sidan. Valfri.
   */
  @Input() title = this.translateService.translate("xp.notFound.pageNotFound");

  /**
   * Titel för länk. Valfri.
   */
  @Input() linkTitle = this.translateService.translate("xp.notFound.goToStartPageButton");

  /**
   * Url för länk. Valfri. Standardlänk är till roten.
   */
  @Input() linkUrl = "/";  

  constructor(
    public matIconRegistry: MatIconRegistry,
    public domSanitizer: DomSanitizer,
    private translateService: TranslocoService) {

      this.matIconRegistry.addSvgIcon("not-found",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/lib/icons/not-found.svg")
    );
    }
  
}
