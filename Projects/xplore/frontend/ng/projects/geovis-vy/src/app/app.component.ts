import {Component, ViewChild} from "@angular/core";
import {ConfigService} from "../../../lib/config/config.service";
import {User} from "../../../lib/oidc/login.service";
import {AppShellComponent} from "../../../lib/app-shell/app-shell.component";
import {MeasureUnit, StateService} from "../../../lib/map/services/state.service";

@Component({
  selector: "xp-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"]
})
export class AppComponent {
  @ViewChild("appShell", {static: true}) appShell: AppShellComponent;

  constructor(private stateService: StateService) {
    // Default measuer unit för geovis-vy (kan overridas i konfigurationsfil
    stateService.setUiStates({measureUnit: MeasureUnit.Auto});
  }

  loggedIn(user: User) {
    ConfigService.appConfig.layerEditing = user.roles && user.roles.indexOf("geovis_admin") > -1;
  }
}
