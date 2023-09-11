import {Component} from "@angular/core";
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: "mdb-app-root",
  template: `
    <router-outlet></router-outlet>
  `
})
export class AppComponent {
  constructor(private matIconRegistry: MatIconRegistry, private domSanitizer: DomSanitizer) {
    this.matIconRegistry.addSvgIcon("select_objects",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/icons/select_objects.svg")
    );
  }
}
