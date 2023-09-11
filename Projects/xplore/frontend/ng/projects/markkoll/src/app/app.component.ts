import { Component} from "@angular/core";
import { TranslocoService } from "@ngneat/transloco";
import { environment } from "../environments/environment";
import { IconService } from "./services/icon.service";

@Component({
  selector: "mk-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"]
})
export class AppComponent {
  title = "markkoll";

  upToDateActive = false;

  constructor(translate: TranslocoService, iconService: IconService) {
    // Vi vill inte att "up to date"-dialogrutan ska laddas in före vi har hunnit hämta
    // översättningsfilerna.
    translate.load(environment.defaultLanguage).subscribe(() => this.upToDateActive = true);
    iconService.registerIcons();
  }
}
