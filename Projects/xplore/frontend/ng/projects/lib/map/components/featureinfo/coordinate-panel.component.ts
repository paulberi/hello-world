import {ChangeDetectionStrategy, Component} from "@angular/core";

import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {MapService} from "../../../map-core/map.service";
import {ConfigService} from "../../../config/config.service";
import {CoordsystemService} from "../../services/coordsystem.service";

/**
 * Visar koordinatsiffrorna för varje klick i kartan.
 *
 */
@Component({
  selector: "xp-coordinate-panel",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ng-container *ngIf="coord | async; let c">
      <div>{{"N " + round(c[1]) + ", E " + round(c[0])}} {{coordSystemText}}</div>
    </ng-container>
  `,
  styles: [`
  `]
})
export class CoordinatePanelComponent {
  coord: Observable<[number, number]>;
  coordSystemText: string;

  constructor(mapService: MapService, private configService: ConfigService,
              private coordSystemService: CoordsystemService) {
    this.coordSystemText = this.coordSystemService.getAppCoordSystemText();

    this.coord = mapService.click$.pipe(
      map(click => click.coordinate)
    );
  }

  round(n: number) {
    return Math.round(n);
  }

}
