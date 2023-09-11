import {Component, EventEmitter, Input, OnDestroy, Output, TemplateRef} from '@angular/core';
import {Subscription} from 'rxjs';
import {StateService} from "../../services/state.service";

@Component({
  selector: 'xp-info-panel',
  template: `
    <div class="panels">
      <ng-container [ngTemplateOutlet]="infoPanelTemplateRef"></ng-container>
    </div>
  `,
  styles: [`
    .panels {
      clear: left;
    }
  `]
})
export class InfoPanelComponent implements OnDestroy {
  @Input() infoPanelTemplateRef: TemplateRef<any>;
  @Output() shouldOpen = new EventEmitter<boolean>();

  uiStateSubscription: Subscription;

  constructor(stateService: StateService) {
    this.uiStateSubscription = stateService.partialUiStates.subscribe(uiStates => {
      // Om ett delområde valts, signalera för att öppna panelen.
      if (uiStates.valdaDelomraden && uiStates.valdaDelomraden.length) {
        this.shouldOpen.emit(true);
      }

      // Om en feature valts, kolla först så att det inte bara är knappnålen.
      if (uiStates.valdaFeatures && uiStates.valdaFeatures.length) {
        if (uiStates.valdaFeatures.length === 1) {
          const feature = uiStates.valdaFeatures[0];
          if (feature.feature.getId() && String(feature.feature.getId()).startsWith("map_pin")) {
            return;
          }
        }
        this.shouldOpen.emit(true);
      }
    });
  }

  ngOnDestroy() {
    this.uiStateSubscription.unsubscribe();
  }
}
