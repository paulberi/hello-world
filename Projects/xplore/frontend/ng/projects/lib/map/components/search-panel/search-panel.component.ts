import {
  AfterContentInit,
  ChangeDetectionStrategy,
  Component,
  ContentChildren,
  HostBinding,
  Input,
  OnInit,
  QueryList, TemplateRef
} from "@angular/core";
import {SearchTypeComponent} from "./search-type.component";
import {SelectionService} from "../../services/selection.service";
import {StateService} from "../../services/state.service";
import {FeatureInfo} from "../../../map-core/feature-info.model";

/**
 * The parent component of all search types. Allows switching between types with radio button or select box.
 */
@Component({
  changeDetection: ChangeDetectionStrategy.Default,
  selector: "xp-search-panel",
  template: `
    <xp-collapsable-panel [icon]="'search'" [collapsed]="collapsed" (stateUpdate)="showBoxShadow = !$event">
      <div class="search-panel">
        <ng-container *ngIf="selectBoxSelectionMode()">
          <mat-form-field>
            <mat-label>Sökläge</mat-label>
            <mat-select [(value)]="selectedSearchType" (selectionChange)="updateVisibleSearchType()">
              <mat-option *ngFor="let type of types" [value]="type.name">
                {{type.name}}
              </mat-option>
            </mat-select>
          </mat-form-field>
        </ng-container>

        <ng-content></ng-content>

        <ng-container *ngIf="radioSelectMode()">
          <mat-radio-group [(ngModel)]="selectedSearchType" name="modeGroup" (change)="updateVisibleSearchType()">
            <mat-radio-button *ngFor="let type of types" value="{{type.name}}">{{type.name}}</mat-radio-button>
          </mat-radio-group>
        </ng-container>
      </div>

      <div class="search-result">
        <ng-container [ngTemplateOutlet]="searchResultTemplate" *ngIf="showSearchResults"></ng-container>

        <button class="clear-btn border-btn" mat-stroked-button (click)="clear()" *ngIf="showResetButton">Rensa sökning</button>
      </div>
    </xp-collapsable-panel>
  `,
  styles: [`
    mat-radio-button {
      margin-right: 5px;
    }

    .clear-btn {
      margin-top: 20px;
      border: 2px solid;
    }

    .search-result {
      margin-top: 10px;
      max-width: 300px;
    }
    `
  ]
})
export class SearchPanelComponent implements OnInit, AfterContentInit {
  @Input() default: string;
  @Input() selectType: "radio" | "selectbox" = "radio";
  selectedSearchType: string;

  @ContentChildren(SearchTypeComponent) types: QueryList<SearchTypeComponent>;

  collapsed = false;
  @HostBinding("class.mat-elevation-z5") showBoxShadow = !this.collapsed;

  @Input() searchResultTemplate: TemplateRef<any>;

  showSearchResults = false;
  showResetButton = false;

  constructor(private selectionService: SelectionService, private stateService: StateService) {

  }

  ngOnInit() {
    // Collapse the panel by default on small screens
    this.collapsed = window.innerWidth <= 660;
    this.showBoxShadow = !this.collapsed;

    this.selectedSearchType = this.default;

    this.stateService.uiStates.subscribe((uiStates) => {
      if (uiStates.valdaDelomraden || uiStates.valdaFeatures) {
        // Om ett delområde valts, signalera för att visa sökresultatet.
        if (uiStates.valdaDelomraden && uiStates.valdaDelomraden.length) {
          this.showSearchResults = true;
          this.showResetButton = true;
        } else if (uiStates.valdaFeatures && uiStates.valdaFeatures.length) {
          const ismapPin = this.isMapPin(uiStates.valdaFeatures[0]);
          this.showSearchResults = true && !ismapPin;
          this.showResetButton = true;
        } else {
          this.showSearchResults = false;
          this.showResetButton = false;
        }
      }
    });
  }

  ngAfterContentInit(): void {
    // If no default is set, use the first one.
    if (!this.selectedSearchType && this.types.length > 0) {
      this.selectedSearchType = this.types.first.name;
    }
    this.updateVisibleSearchType();
  }

  radioSelectMode() {
    return this.selectType === "radio" && this.types.length > 1;
  }

  selectBoxSelectionMode() {
    return this.selectType === "selectbox" && this.types.length > 1;
  }

  updateVisibleSearchType() {
    this.types.forEach(type => type.visible = type.name === this.selectedSearchType);
  }

  isMapPin(featureInfo?: FeatureInfo): boolean {
    return featureInfo?.feature.getId() && String(featureInfo?.feature.getId()).startsWith("map_pin");
  }

  clear() {
    this.selectionService.resetSelectedFeatures();
    this.selectionService.resetSelectedDelomraden();
    this.selectionService.removeMapPin();
    this.selectionService.deselectEgenRitatOmrade();
  }

}

