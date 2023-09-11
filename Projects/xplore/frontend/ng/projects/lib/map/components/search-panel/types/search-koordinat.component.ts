import {ChangeDetectionStrategy, Component, Input} from "@angular/core";
import {Place} from "../../../services/sok.service";
import {SelectionService} from "../../../services/selection.service";
import {MapService} from "../../../../map-core/map.service";
import {UntypedFormControl} from "@angular/forms";
import {ConfigProperty, ConfigService} from "../../../../config/config.service";
import {containsXY} from "ol/extent";
import {CoordsystemService} from "../../../services/coordsystem.service";

@Component({
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: "xp-search-koordinat",
    template: `
      <div>
          <mat-form-field class="form-field">
              <input type="text" aria-label="Number" matInput placeholder="Ange på formatet {{this.coordOrderText}} ({{this.coordSystemText}})" (keydown.enter)="onEnterClicked()" [formControl]="textInputControl">
              <mat-hint align="start">
                  <strong class="xp-search-koordinat-hint" [class.failure]="this.hasFailed">{{errorMessage}}</strong>
              </mat-hint>
              <button mat-button matSuffix mat-icon-button aria-label="Clear" (click)="clear()">
                  <mat-icon>close</mat-icon>
              </button>
          </mat-form-field>
      </div>
  `,
    styles: [
        `
    .form-field {
        width: 100%;
    }
    `
    ]
})
export class SearchKoordinatComponent {

    @Input() zoomFactor: number;

    textInputControl = new UntypedFormControl();
    hasFailed = false;
    errorMessage = "";
    coordSystemText: string;
    coordOrderText: string;

    constructor(private selectionService: SelectionService, private configService: ConfigService,
                private mapService: MapService, private coordSystemService: CoordsystemService) {
        this.zoomFactor = this.zoomFactor == null ? 10 : this.zoomFactor;
        this.coordSystemText = this.coordSystemService.getAppCoordSystemText();
        this.coordOrderText = this.coordSystemService.getAppCoordOrderText();
    }

    onEnterClicked() {
        let coordinateParams: CoordinateParams = {
            query: this.textInputControl.value == null ? "" : this.textInputControl.value,
            isStrict: true
        };
        this.resetError();
        let place = this.parseInputCoordinate(coordinateParams);
        if(place != null) {
            this.textInputControl.setValue(place.name);
            this.selectionService.setPlace(place, true, true);
            this.mapService.map.getView().setZoom(this.zoomFactor);
        } else {
            this.hasFailed = true;
            this.errorMessage = "Ogiltig koordinat eller format angivet";
        }
    }

    resetError() {
        this.hasFailed = false;
        this.errorMessage = "";
    }

    clear() {
        this.textInputControl.setValue("");
        this.resetError();
    }

    parseInputCoordinate(coordinateParams: CoordinateParams): Place {
        let coordinates = coordinateParams.query.trim();
        let regex = /[A-Z:]*/gi;//Match letters A-Z :(colon) and matches Case insensitive (i) and anywhere in string (g)
        let foundLetters = coordinates.match(regex);
        if (foundLetters != null && foundLetters.length != null) {
            foundLetters.forEach(letter =>
                coordinates = coordinates.replace(letter, "")
            );
        }

        let splitRegEx = /[,|\s]/gi;//Split on all spaces and commas
        let coordinatesList = coordinates.trim().split(splitRegEx).filter(x => x != "");
        let northing = null;
        let easting = null;
        if (coordinatesList.length == 2) { //Dot decimal separator
            northing = parseFloat(coordinatesList[0].trim());
            easting = parseFloat(coordinatesList[1].trim());
        } else if (coordinatesList.length == 4) { //Comma decimal separator
            northing = parseFloat(coordinatesList[0].trim() + "." + coordinatesList[1].trim());
            easting = parseFloat(coordinatesList[2].trim() + "." + coordinatesList[3].trim());
        }

        if (isNaN(northing) || isNaN(easting) || northing == null || easting == null || (coordinateParams.isStrict && !containsXY(this.configService.getConfigProperty(ConfigProperty.EXTENT), easting, northing))) {
            return null;
        }

        return {
            coordinate: [easting, northing],
            type: "KOORDINAT",
            name: "N "+northing + ", E " + easting
        };
    }

}
export interface CoordinateParams {
    isStrict: boolean;
    query: string;
}

