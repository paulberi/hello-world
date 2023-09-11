import {
  Component
} from "@angular/core";
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: "xp-root",
  templateUrl: "./app.component.html",
})
export class AppComponent {
  constructor(private matIconRegistry: MatIconRegistry,
              private domSanitizer: DomSanitizer,
              ) {
    this.matIconRegistry.addSvgIcon("skogsanalys",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/icons/skogsanalys.svg")
    );
  }
}
