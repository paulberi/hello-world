import {ChangeDetectionStrategy, Component, OnDestroy, OnInit} from "@angular/core";
import {FeatureSelectionMode, StateService, UIStates} from "../../services/state.service";
import {Observable} from "rxjs";
import {SelectionService} from "../../services/selection.service";
import {UntypedFormControl} from "@angular/forms";
import {debounceTime, distinctUntilChanged, switchMap} from "rxjs/operators";
import {SpatialPredicate} from "../../services/sok.service";
import {DrawService} from "../../services/map-tools/draw.service";
import { MatCheckboxChange } from "@angular/material/checkbox";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-tool-settings",
template: `
  <ng-container *ngIf="this.uiStates | async; let uiStates">
    <ng-container [ngSwitch]="uiStates.featureSelectionMode">
      <div *ngSwitchCase="FeatureSelectionMode.DrawPolygonIntersection" class="settings">
        <h3>Verktygsinställningar (Berör polygon)</h3>
        <mat-checkbox (change)="onValjHelaFastigheterCheckboxChange($event)"
                      [checked]="uiStates.valjHelaFastigheter">Välj hela fastigheter
        </mat-checkbox>
        <mat-form-field class="full-width">
          <input type="number" matInput placeholder="Buffer (meter)" class="spacey"
                 [formControl]="bufferFormControl" [min]="minBuffer" [max]="maxBuffer">
          <mat-hint>Max {{maxBuffer}} meter</mat-hint>
        </mat-form-field>
      </div>
      <div *ngSwitchCase="FeatureSelectionMode.DrawPolygonWithin" class="settings">
        <h3>Verktygsinställningar (Helt inom polygon)</h3>
        <mat-checkbox (change)="onValjHelaFastigheterCheckboxChange($event)"
                      [checked]="uiStates.valjHelaFastigheter">Välj hela fastigheter
        </mat-checkbox>
        <mat-form-field class="full-width">
          <input type="number" matInput placeholder="Buffer (meter)" class="spacey"
                 [formControl]="bufferFormControl" [min]="minBuffer" [max]="maxBuffer">
          <mat-hint>Max {{maxBuffer}} meter</mat-hint>
        </mat-form-field>
      </div>
      <div *ngSwitchCase="FeatureSelectionMode.DrawLineIntersection" class="settings">
        <h3>Verktygsinställningar (Berör linje)</h3>
        <mat-checkbox (change)="onValjHelaFastigheterCheckboxChange($event)"
                      [checked]="uiStates.valjHelaFastigheter">Välj hela fastigheter
        </mat-checkbox>
        <mat-form-field class="full-width">
          <input type="number" matInput placeholder="Buffer (meter)" class="spacey"
                 [formControl]="bufferFormControl" [min]="minBuffer" [max]="maxBuffer">
          <mat-hint>Max {{maxBuffer}} meter</mat-hint>
        </mat-form-field>
      </div>
      <div *ngSwitchDefault class="settings">
        <h3>Verktygsinställningar</h3>
          <mat-checkbox (change)="onValjHelaFastigheterCheckboxChange($event)"
                        [checked]="uiStates.valjHelaFastigheter">Välj hela fastigheter
          </mat-checkbox>
      </div>
    </ng-container>
  </ng-container>
`,
  styles: [`
    mat-radio-button {
      margin-right: 5px;
    }

    h3 {
      margin: 0;
    }

    .spacey {
      margin-top: 5px;
      margin-bottom: 5px;
    }

    .full-width {
      width: 100%;
    }

    .settings {
      display: flex;
      flex-direction: column;
    }

    mat-checkbox {
      width: 100%;
      padding-bottom: 10px;
    }
  `]
})
export class ToolSettingsComponent implements OnInit, OnDestroy {
  uiStates: Observable<UIStates>;

  FeatureSelectionMode = FeatureSelectionMode;

  minBuffer = 0;
  maxBuffer: number;

  bufferFormControl = new UntypedFormControl();
  bufferFormSubscription;

  constructor(private stateService: StateService, private selectionService: SelectionService, private drawService: DrawService) {
    this.uiStates = stateService.uiStates;
  }

  ngOnInit() {
    this.maxBuffer = this.selectionService.maxBuffer;

    this.bufferFormControl.setValue(this.drawService.selectionBufferDistance);
    this.bufferFormSubscription = this.bufferFormControl.valueChanges.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      switchMap(bufferDistance => {
        bufferDistance = Math.min(Math.max(bufferDistance, this.minBuffer), this.maxBuffer);
        this.drawService.selectionBufferDistance = bufferDistance;

        const predicate = this.stateService.getUiStates().featureSelectionMode === FeatureSelectionMode.DrawPolygonWithin ? SpatialPredicate.WITHIN : SpatialPredicate.INTERSECTS;
        return this.selectionService.selectFeaturesByPolygon(undefined, predicate, bufferDistance);
      })
    ).subscribe();
  }

  ngOnDestroy() {
    this.bufferFormSubscription.unsubscribe();
  }


  onValjHelaFastigheterCheckboxChange(event: MatCheckboxChange) {
    this.stateService.setUiStates({valjHelaFastigheter: event.checked});
  }
}

