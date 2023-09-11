import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from "@angular/core";
import {Place} from "../../services/sok.service";
import {debounceTime, filter} from "rxjs/operators";
import {UntypedFormControl} from "@angular/forms";

/**
 * An autocompletable text field used for search queries that lead to a "place" in the map.
 * Don't use it for stuff that doesn't need autocompletion, just use a normal text input.
 */
@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-search-field",
  template: `
    <mat-form-field class="full-width" [appearance]="formAppearance">
      <input type="text" [placeholder]="placeholder" aria-label="Number" matInput [formControl]="myControl"
             [matAutocomplete]="auto">
      <button mat-button matSuffix mat-icon-button aria-label="Clear" (click)="clear()">
        <mat-icon>close</mat-icon>
      </button>
      <mat-autocomplete #auto="matAutocomplete" autoActiveFirstOption [displayWith]="displayFn"
                        (optionSelected)="placeSelected.emit($event.option.value)">
        <mat-option class="search-field-place-option" *ngFor="let option of options" [value]="option">
          <div class="container">
            <div class="name">
              <mat-icon *ngIf="showIcons">{{getPlaceIcon(option)}}</mat-icon>
              {{option.name}}
            </div>
            <div class="extra-container">
              <div class="extra" *ngIf="displayExtra(option) as extra">{{extra}}</div>
            </div>
          </div>
        </mat-option>
      </mat-autocomplete>
    </mat-form-field>
  `,
  styles: [`
    .full-width {
      width: 100%;
    }

    .name {
      font-size: 0.9em
    }

    .extra {
      text-align: right;
      font-size: 0.75em !important;
      line-height: 0.75em !important;
    }
  `]
})
export class SearchFieldComponent {
  @Input() placeholder: string;
  @Input() options: Place[];
  @Input() showIcons: boolean = true;
  @Input() formAppearance: "standard" | "legacy" | "fill" | "outline" = "legacy";
  @Output() placeSelected = new EventEmitter<Place>();
  @Output() clearSelectedPlace = new EventEmitter();

  myControl = new UntypedFormControl();

  get textInput() {
    return this.myControl.valueChanges.pipe(
      debounceTime(500),
      filter(value => typeof value === "string"),
    );
  }

  displayFn(place: Place) {
    return place ? place.name : undefined;
  }

  displayExtra(place: Place): string | undefined {
    let displayValue = "";
    if (place && place.extra) {
      place.extra.forEach((value, key) => {
        displayValue += (displayValue ? ", " : "") + value;
      });
    }
    return displayValue;
  }

  clear() {
    this.myControl.setValue("");
    this.options = [];
    this.clearSelectedPlace.emit();
  }

  getPlaceIcon(place: Place) {
    switch (place.type) {
      case "BEBTÄTTX":
      case "1119":
        return "location_city";
      case "3136":
        return "mail";
      case "BEBTX":
      case "TRAKTTX":
      case "4110":
        return "place";
      case "10000":
        return "place";
      case "NATTX":
        return "nature_outline";
      case "TERRTX":
        return "terrain";
      case "ANLTX":
        return "store";
      case "KOMMUN":
        return "map_outline";
      default:
        return "not_listed_location";
    }
  }
}

