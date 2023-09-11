import {ChangeDetectionStrategy, Component} from "@angular/core";

import {Observable, of} from "rxjs";
import {catchError, flatMap, map} from "rxjs/operators";

import {MapService} from "../../../map-core/map.service";
import {SokService} from "../../services/sok.service";

/**
 * Gör en omvänd geokodning (koordinat -> adress) på klick i kartan och visar adressen.
 */
@Component({
  selector: "xp-adress-panel",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span *ngIf="hits | async; let hit">{{hit}}</span>
  `,
  styles: [`
  `]
})
export class AdressPanelComponent {
  hits: Observable<string>;

  constructor(mapService: MapService, private sokService: SokService) {
    this.hits = mapService.click$.pipe(
      flatMap(click => this.sokService.reverseGeocode(click.coordinate).pipe(
        catchError(() => of([]))
      )),
      map(places => places.length > 0 ? places[0].name : null)
    );
  }
}
