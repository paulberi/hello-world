import {Component, HostBinding, Input, TemplateRef, ViewChild} from '@angular/core';

@Component({
  selector: 'xp-collapsable-info-panel',
  template: `
    <xp-collapsable-panel #collapsablePanel [icon]="'info'" [collapsed]="true" class="collapsable-panel" (stateUpdate)="showBoxShadow = !$event">
      <div class="info-panel">
        <xp-info-panel [infoPanelTemplateRef]="infoPanelTemplateRef" (shouldOpen)="onShouldOpen($event)"></xp-info-panel>
      </div>
    </xp-collapsable-panel>
  `,
  styles: [`
  `]
  })
export class CollapsableInfoPanelComponent {
  @Input() infoPanelTemplateRef: TemplateRef<any>;

  @ViewChild('collapsablePanel', { static: true }) collapsablePanel;
  @HostBinding('class.mat-elevation-z5') showBoxShadow = false;

  onShouldOpen(event) {
    this.collapsablePanel.collapsed = !event;
    this.showBoxShadow = event;
  }

}
