import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatIconModule, MatIconRegistry } from "@angular/material/icon";
import { DomSanitizer } from "@angular/platform-browser";
import { TranslocoModule } from "@ngneat/transloco";
import { MkMatSelectSearchComponent } from "../mat-select-search/mat-select-search.component";
import { COUNTRIES_SE } from "./countries.se";
import { MkCountrySelectComponent } from "./country-select.component";

@NgModule({
  declarations: [
    MkCountrySelectComponent
  ],
  imports: [
    CommonModule,
    MatIconModule,
    MkMatSelectSearchComponent,
    TranslocoModule
  ],
  exports: [
    MkCountrySelectComponent
  ]
})
export class MkCountrySelectModule {
  constructor(private matIconRegistry: MatIconRegistry, private sanitizer: DomSanitizer) {
    this.registerCountries();
  }

  registerCountries() {
    COUNTRIES_SE.forEach(country => {
      const alpha2CodeLower = country.alpha2Code.toLowerCase();
      this.matIconRegistry.addSvgIcon(
        "flag_" + country.alpha2Code,
        this.sanitizer.bypassSecurityTrustResourceUrl(`assets/svg-country-flags/svg/${alpha2CodeLower}.svg`)
      );
    });
  }
}
