import {Component, OnDestroy} from "@angular/core";
import {SelectionService} from "../../../services/selection.service";
import {UntypedFormControl} from "@angular/forms";
import {QueryService} from "../../../services/query.service";
import {LayerService} from "../../../../map-core/layer.service";
import {finalize, timeout} from "rxjs/operators";
import {Subscription} from "rxjs";

@Component({
  selector: "xp-search-plan",
  template: `
    <mat-form-field class="form-field" *ngIf="isConfigured(); else notConfigured">
      <input type="text" placeholder="Ange planbeteckning" (keydown.enter)="onEnter()" matInput
             [formControl]="textInput">
      <button mat-button matSuffix mat-icon-button (click)="clear()">
        <mat-icon>clear</mat-icon>
      </button>
    </mat-form-field>

    <ng-template #notConfigured>Felkonfigurerad: planlagret kan inte hittas</ng-template>
  `,
  styles: [`
    .form-field {
      width: 100%;
    }
  `]
})
export class SearchPlanComponent implements OnDestroy {
  private planLayer: any;
  private layerSubscription: Subscription;

  textInput = new UntypedFormControl();

  constructor(private layerService: LayerService, private selectionService: SelectionService, private queryService: QueryService) {
    this.planLayer = this.layerService.getLayer("planer");

    this.layerSubscription = this.layerService.getLayerChange().subscribe(() => {
      this.planLayer = this.layerService.getLayer("planer");
    });
  }

  ngOnDestroy() {
    this.layerSubscription.unsubscribe();
  }

  isConfigured() {
    return this.planLayer != null;
  }

  onEnter() {
    const id = this.textInput.value;
    const query = `planbet LIKE '${id}%'`;
    this.textInput.disable();
    this.queryService.wfsQuery(this.planLayer, ["metria:plan_yta"], query)
      .pipe(timeout(10000), finalize(() => this.textInput.enable()))
      .subscribe(result => {
        this.selectionService.selectFeatures(result, true, true);
      });
  }

  clear() {
    this.textInput.setValue("");
  }
}

