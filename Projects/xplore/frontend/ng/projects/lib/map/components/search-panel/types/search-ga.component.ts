import {AfterViewInit, Component, OnDestroy, ViewChild} from "@angular/core";
import {SelectionService} from "../../../services/selection.service";
import {UntypedFormControl} from "@angular/forms";
import {QueryService} from "../../../services/query.service";
import {LayerService} from "../../../../map-core/layer.service";
import {finalize, flatMap, map, timeout} from "rxjs/operators";
import KommunKoder from "../../../../assets/kommunkoder.json";

import {ConfigService} from "../../../../config/config.service";
import {Observable, of, Subscription} from "rxjs";
import {Place} from "../../../services/sok.service";
import {SearchFieldComponent} from "../search-field.component";


interface Kommun {
  kod: string;
  namn: string;
}

@Component({
  selector: "xp-search-ga",
  template: `
    <xp-search-field #searchField [hidden]="!isConfigured()"
                     placeholder="Ange kommun trakt block:enhet"
                     [options]="options | async"
                     (placeSelected)="onPlaceSelected($event)">
    </xp-search-field>

    <div [hidden]="isConfigured()">Felkonfigurerad: rättighetslagret kan inte hittas</div>
  `,
  styles: []
})
export class SearchGaComponent implements AfterViewInit, OnDestroy {
  @ViewChild("searchField") searchField: SearchFieldComponent;
  options: Observable<Place[]>;

  private rattighetLayer: any;
  private readonly kommuner: Kommun[];

  textInput = new UntypedFormControl();
  private layerSubscription: Subscription;

  constructor(private layerService: LayerService, private selectionService: SelectionService,
              private queryService: QueryService, private configService: ConfigService) {
    this.kommuner = KommunKoder["koder"];
    this.rattighetLayer = this.layerService.getLayer("rattigheter");

    this.layerSubscription = this.layerService.getLayerChange().subscribe(() => {
      this.rattighetLayer = this.layerService.getLayer("rattigheter");
    });
  }

  ngOnDestroy() {
    this.layerSubscription.unsubscribe();
  }

  isConfigured() {
    return this.rattighetLayer != null;
  }

  ngAfterViewInit(): void {
    this.options = this.searchField.textInput.pipe(
      flatMap(query => query.length > 2 ? this.onEnter(query) : of([]))
    );
  }

  onPlaceSelected(place: Place) {
    this.selectionService.selectFeatures([place.featureInfo], true, true);
  }

  onEnter(query: string) {
    const parts = query.toUpperCase().split(/\s+/);

    // true if kommunnamn has more than 1 word
    let kommunMultiWord = false;

    const kommunOneWord = parts[0];
    let kommunTwoWords = parts[0];
    if (parts.length > 1) {
        kommunTwoWords += " " + parts[1];
    }

    const kommunnamn = [];
    this.kommuner.forEach((k) => {
      const namn = k.namn.toUpperCase();
      if (namn.includes(kommunTwoWords)) {
        kommunnamn.push(`'${k.kod}'`);
        kommunMultiWord = true;
      } else if (namn.includes(kommunOneWord)) {
        kommunnamn.push(`'${k.kod}'`);
      }
    });

    if (!kommunnamn.length) {
      return of([]);
    }


    const kommunKoder = kommunnamn.join(",");

    /*
      1 -> kommunKoder(1)
      2 -> kommunkoder(1) trakt(1) | kommunkoder(2)
      3 -> kommunKoder(1) trakt(1) blockenhet(1) | kommunKoder(1) trakt(2) | kommunKoder(2) trakt(1)
      4 -> kommunKoder(1) trakt(2) blockenhet(1) | kommunKoder(2) trakt(1) blockenhet(1) | kommunkoder(2) trakt(2)
      5 -> kommunKoder(2) trakt(2) blockenhet(1)
    */

    let cql_filter = "";
    if (parts.length === 1) {
      cql_filter = `kommunkod IN (${kommunKoder}) AND detaljtyp = 'GAANLÄGGN'`;
    } else if (parts.length === 2) {
      if (kommunMultiWord) {
        // If the kommunnamn contains 2
        cql_filter = `kommunkod IN (${kommunKoder}) AND detaljtyp = 'GAANLÄGGN'`;
      } else {
        // If the kommunnamn contains 1 word
        cql_filter = `(kommunkod IN (${kommunKoder}) AND trakt LIKE '${parts[1]}%%') AND detaljtyp = 'GAANLÄGGN'`;
      }
    } else if (parts.length === 3) {
      if (kommunMultiWord) {
        cql_filter = `(kommunkod IN (${kommunKoder}) AND trakt LIKE '${parts[2]}%%') AND detaljtyp = 'GAANLÄGGN'`;
      } else {
        cql_filter = `((kommunkod IN (${kommunKoder}) AND trakt LIKE '${parts[1]}%%' AND blockenhet LIKE '${parts[2]}%%') OR (kommunkod IN (${kommunKoder}) AND trakt LIKE '${parts[1]} ${parts[2]}%%')) AND detaljtyp = 'GAANLÄGGN'`;
      }
    } else if (parts.length === 4) {
      // T.ex "KIRUNA NEDRE NORRMALM GA:1"
      if (kommunMultiWord) {
        // T.ex "LILLA EDET AMNERÖD 1:10"
        cql_filter = `((kommunkod IN (${kommunKoder}) AND trakt LIKE '${parts[2]}%%' AND blockenhet LIKE '${parts[3]}%%') OR (kommunkod IN (${kommunKoder}) AND trakt LIKE '${parts[1]} ${parts[2]}%%' AND blockenhet LIKE '${parts[3]}%%')) AND detaljtyp = 'GAANLÄGGN'`;
      } else {
        // T.ex "KIRUNA NEDRE NORRMALM GA:1"
        cql_filter = `kommunkod IN (${kommunKoder}) AND trakt LIKE '${parts[1]} ${parts[2]}%%' AND blockenhet LIKE '${parts[3]}%%' AND detaljtyp = 'GAANLÄGGN'`;
      }
    } else if (parts.length >= 5) {
      cql_filter = `kommunkod IN (${kommunKoder}) AND trakt LIKE '${parts[2]} ${parts[3]}%%' AND blockenhet LIKE '${parts[4]}%%' AND detaljtyp = 'GAANLÄGGN'`;
    }

    this.textInput.disable();
    return this.queryService.wfsQuery(this.rattighetLayer, ["metria:rattighet_ga_punkt", "metria:rattighet_ga_linje", "metria:rattighet_ga_yta"], cql_filter)
      .pipe(
        timeout(10000), finalize(() => this.textInput.enable()),
        map(featureInfos => featureInfos.map(f => {
          let namn = "";
          this.kommuner.forEach((kommun) => {
            if (kommun.kod === f.feature.get("kommunkod")) {
              namn = kommun.namn.toUpperCase();
            }
          });

          return {
            name: namn + " " + f.feature.get("trakt") + " " + f.feature.get("blockenhet"),
            type: "0000",
            featureInfo: f
          };
        }))
      );
  }

  clear() {
    this.textInput.setValue("");
  }
}

