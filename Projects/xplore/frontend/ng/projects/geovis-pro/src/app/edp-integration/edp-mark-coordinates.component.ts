import {Component, EventEmitter, Input, OnDestroy} from "@angular/core";
import {Coordinate} from "ol/coordinate";
import {MapService} from "../../../../lib/map-core/map.service";
import {Subscription} from "rxjs";
import {Place} from "../../../../lib/map/services/sok.service";
import {SelectionService} from "../../../../lib/map/services/selection.service";

@Component({
  selector: "xp-edp-mark-coordinates",
  template: `
    <ng-container>
      <H3 style="display: inline-flex; vertical-align: middle">
        <mat-icon style="display: inline-flex; vertical-align: middle;" color="accent">pin_drop</mat-icon>
        Markera koordinat
      </H3>
      <div>
        <h3>{{title}}</h3>
        <div *ngIf="markedCoordinate">
          <mat-label>{{coordinateText}}</mat-label>
        </div>
        <div style="margin-top: 10px">
          <button *ngIf="markedCoordinate" mat-stroked-button style="float: right;" (click)="finishedMarkingCoordinates()">Bekräfta</button>
          <button mat-button style="float: right;" (click)="cancel()">Avbryt</button>
        </div>
        <div style="clear: both;"></div>
      </div>
    </ng-container>
  `,
  styles: [`
  `]
})
export class EdpMarkCoordinatesComponent implements OnDestroy {

  @Input() resultEvent: EventEmitter<MarkedCoordinate[]>;
  markedCoordinate: MarkedCoordinate;
  private clickSubscription: Subscription;

  coordinateText: string;
  title: string;

  constructor(private selectionService: SelectionService,
              private mapService: MapService) {
    this.prettyCoordinate();
    this.updateTitle();
    this.clickSubscription = this.mapService.click$.subscribe(
      click => {
        this.markedCoordinate = {coordinate: click.coordinate, label: ""};
        this.updateTitle();
        this.prettyCoordinate();
        this.markOnMap();
      }
    );
  }

  private markOnMap() {
    const place: Place = {
      coordinate: this.markedCoordinate.coordinate, name: this.markedCoordinate.label, type: "edpCoordinate"
    };

    this.selectionService.setPlace(place, false);
  }

  private updateTitle() {
    this.title = this.markedCoordinate ? "Vald koordinat" : "Klicka i kartan för att välja koordinat";
  }

  ngOnDestroy() {
    if (!this.clickSubscription.closed) {
      this.clickSubscription.unsubscribe();
    }
  }

  finishedMarkingCoordinates() {
    this.clickSubscription.unsubscribe();
    this.resultEvent.emit([this.markedCoordinate]);
  }

  cancel() {
    if (!this.clickSubscription.closed) {
      this.clickSubscription.unsubscribe();
    }
    this.resultEvent.emit([]);
  }

  prettyCoordinate() {
    if (this.markedCoordinate) {
      const coordinate = this.markedCoordinate.coordinate;
      const northing = Math.round(coordinate[1]);
      const easting = Math.round(coordinate[0]);

      this.coordinateText = `N ${northing}, E ${easting}`;
    }
  }
}

export interface MarkedCoordinate {
  coordinate: Coordinate;
  label?: string;
}
