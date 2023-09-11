import {ChangeDetectionStrategy, Component} from "@angular/core";
import {ConfigService} from "../../../config/config.service";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-attribution",
  template: `
    <div class="attribution">
      {{attribution}}
    </div>
  `,
  styleUrls: ["./attribution.component.scss"]
})
export class AttributionComponent {
  attribution: string;

  constructor(private configService: ConfigService) {
    this.attribution = configService.config.app.copyright;
  }
}
