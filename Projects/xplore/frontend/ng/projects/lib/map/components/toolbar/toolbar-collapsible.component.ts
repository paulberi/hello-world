import {
  ChangeDetectionStrategy,
  Component,
  Input,
  QueryList,
  ViewChildren
} from "@angular/core";
import {ToolbarConfig} from "./toolbar.component";
import {ToolbarCollapsible, ToolbarDirection} from "./sub-toolbars/base-toolbar.component";
import {MeasureService} from "../../services/map-tools/measure.service";
import {DrawService} from "../../services/map-tools/draw.service";

export interface ToolbarCollapsibleConfig extends ToolbarConfig {
  tooltip: string;
  icon_name?: string;
  svg_icon?: string;
  base_component: string;
}

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-toolbar-collapsible",
  template: `
    <div class="toolbar-collapsible">
      <ng-container *ngFor="let config of configs; let i = index">
        <ng-container [ngSwitch]="config.base_component">

          <xp-measure-toolbar #toolbar *ngSwitchCase="'measure'"
                              [config]="config" [direction]="direction" (toggled)="onToggle($event, i)">
          </xp-measure-toolbar>

          <xp-base-toolbar #toolbar *ngSwitchDefault
                              [config]="config" [direction]="direction" (toggled)="onToggle($event, i)">
          </xp-base-toolbar>
        </ng-container>
      </ng-container>
    </div>
  `,
  styles: [`
    .toolbar-collapsible {
      position: relative;
      display: flex;
      flex-wrap: nowrap;
    }
  `]
})
export class ToolbarCollapsibleComponent {
  @Input() configs: ToolbarCollapsibleConfig[] = [];
  @Input() direction: ToolbarDirection;

  @ViewChildren("toolbar") toolbars: QueryList<ToolbarCollapsible>;

  // measureService and drawService are injected but not used directly. The services listen
  // on the stateService for changes to the action/selection mode.
  constructor(private measureService: MeasureService, private drawService: DrawService) {}

  onToggle(value, index) {
    this.toolbars.forEach((item, index2) => {
      // Collapse all groups except the clicked one
      item.toggleGroup(index === index2 ? value : false);
    });
  }
}
