import {AfterViewInit, Component, ViewChild} from "@angular/core";
import {Place, SokService} from "../../../services/sok.service";
import {SelectionService} from "../../../services/selection.service";
import {SearchFieldComponent} from "../search-field.component";
import {catchError, flatMap} from "rxjs/operators";
import {Observable, of} from "rxjs";

@Component({
  selector: "xp-search-kommun",
  template: `
    <xp-search-field #searchField placeholder="Ange kommun" [options]="options | async" (placeSelected)="onPlaceSelected($event)"></xp-search-field>
  `
})
export class SearchKommunComponent implements AfterViewInit {
  @ViewChild("searchField", { static: true }) searchField: SearchFieldComponent;
  options: Observable<Place[]>;

  constructor(private selectionService: SelectionService, private sokService: SokService) {
  }

  ngAfterViewInit(): void {
    this.options = this.searchField.textInput.pipe(
      flatMap(query => query.length > 2 ? this.sokService.findKommuner(query).pipe(
        catchError(() => of([]))
      ) : of([]))
    );
  }

  onPlaceSelected(place: Place) {
    this.selectionService.setPlace(place, true, true);
  }
}

