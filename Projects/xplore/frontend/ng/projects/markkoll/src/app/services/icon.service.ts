import { Injectable } from "@angular/core";
import { MatIconRegistry } from "@angular/material/icon";
import { DomSanitizer } from "@angular/platform-browser";
import { COUNTRIES_SE } from "../common/country-select/countries.se";
import { SvgIcons } from "./svg-icons";

@Injectable({
  providedIn: "root"
})
export class IconService {
  constructor(private matIconRegistry: MatIconRegistry,
              private sanitizer: DomSanitizer) {}

  registerIcons() {
    this.load(SvgIcons, 'assets');
  }

  private load(icons: typeof SvgIcons, url: string) {
    Object.keys(icons).forEach(icon => {
      this.matIconRegistry.addSvgIcon(
        icon,
        this.sanitizer.bypassSecurityTrustResourceUrl(`${url}/${icon}.svg`)
      );
    });
  }
}
