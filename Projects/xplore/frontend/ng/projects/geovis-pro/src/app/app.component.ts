import {
  Component
} from "@angular/core";
import {ConfigService} from "../../../lib/config/config.service";
import {MeasureUnit, StateService} from "../../../lib/map/services/state.service";



@Component({
  selector: "xp-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"]
})
export class AppComponent {
  public edpInfoPanelVisible = false;
  public castorInfoPanelVisible = false;

  constructor(public configService: ConfigService, private stateService: StateService) {
    // Default measuer unit för geovis-pro (kan overridas i konfigurationsfil
    stateService.setUiStates({measureUnit: MeasureUnit.Auto});
  }
}
