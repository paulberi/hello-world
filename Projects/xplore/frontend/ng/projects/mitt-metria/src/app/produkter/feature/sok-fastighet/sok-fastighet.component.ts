import { AfterViewInit, Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { Observable, of } from "rxjs";
import { catchError, flatMap } from 'rxjs/operators';
import { SearchFieldComponent } from '../../../../../../lib/map/components/search-panel/search-field.component';
import { Place, SokService } from '../../../../../../lib/map/services/sok.service';

@Component({
  selector: 'mm-sok-fastighet',
  templateUrl: './sok-fastighet.component.html',
  styleUrls: ['./sok-fastighet.component.scss']
})
export class SokFastighetComponent implements AfterViewInit {
  @ViewChild("searchField", { static: true }) searchField: SearchFieldComponent;
  searchResult: Observable<Place[]>;
  @Input() placeholder: string;
  @Output() fastighetSelected = new EventEmitter<Place>();
  @Output() clear = new EventEmitter();

  constructor(private sokService: SokService) { }

  ngAfterViewInit(): void {
    this.searchResult = this.searchField.textInput.pipe(
      flatMap(query => query.toString().length > 2 ? this.sokService.findFastighet(query.toString()).pipe(
        catchError(() => of([]))
      ) : of([]))
    );
  }
}


