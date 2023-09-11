import {Component, OnDestroy} from "@angular/core";
import {SelectionService} from "../../../services/selection.service";
import {UntypedFormControl} from "@angular/forms";
import {QueryService} from "../../../services/query.service";
import {LayerService} from "../../../../map-core/layer.service";
import {finalize, timeout} from "rxjs/operators";
import {Subscription} from "rxjs";

@Component({
  selector: "xp-search-rattighet",
  template: `
    <mat-form-field class="form-field" *ngIf="isConfigured(); else notConfigured">
      <input type="text" placeholder="Ange rättighetsbeteckning" (keydown.enter)="onEnter()" matInput
             [formControl]="textInput">
      <button mat-button matSuffix mat-icon-button (click)="clear()">
        <mat-icon>clear</mat-icon>
      </button>
    </mat-form-field>

    <ng-template #notConfigured>Felkonfigurerad: rättighetslagret kan inte hittas</ng-template>
  `,
  styles: [`
    .form-field {
      width: 100%;
    }
  `]
})
export class SearchRattighetComponent implements OnDestroy {
  private rattighetLayer: any;
  private layerSubscription: Subscription;

  textInput = new UntypedFormControl();

  constructor(private layerService: LayerService, private selectionService: SelectionService, private queryService: QueryService) {
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

  onEnter() {
    const id = this.textInput.value;
    const query = `ratbet LIKE '${id}%' AND detaljtyp <> 'GAANLÄGGN'`;
    this.textInput.disable();
    this.queryService.wfsQuery(this.rattighetLayer, ["metria:rattighet_ga_punkt", "metria:rattighet_ga_linje", "metria:rattighet_ga_yta"], query)
      .pipe(timeout(10000), finalize(() => this.textInput.enable()))
      .subscribe(result => {
        this.selectionService.selectFeatures(result, true, true);
      });
  }

  clear() {
    this.textInput.setValue("");
  }
}

