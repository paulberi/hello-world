import {
  ChangeDetectionStrategy,
  Component, EventEmitter, Input,
  Output, ViewChild,
} from "@angular/core";
import {StateService} from "../../../services/state.service";
import {ToolbarCollapsible, ToolbarDirection} from "./base-toolbar.component";
import {MeasureService} from "../../../services/map-tools/measure.service";
import {ToolbarCollapsibleConfig} from "../toolbar-collapsible.component";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-measure-toolbar",
  template: `
    <xp-base-toolbar #baseToolbar [config]="config" [direction]="direction" (toggled)="toggled.emit($event)">
      <button mat-raised-button (click)="clear()" class="delete mat-elevation-z" [ngClass]="direction"
              matTooltip="Ta bort mätningar" matTooltipPosition="above">
        <mat-icon>delete</mat-icon>
      </button>
    </xp-base-toolbar>
  `,
  styles: [`
    .delete {
      margin-right: 4px;
    }
    .left {
      margin-right: 10px;
    }

    .up {
      margin-bottom: 10px;
    }

    .right {
      margin-left: 10px;
    }

    .down {
      margin-top: 10px;
    }
  `]
})
export class MeasureToolbarComponent implements ToolbarCollapsible {
  @Input() direction: ToolbarDirection = ToolbarDirection.up;
  @Input() config: ToolbarCollapsibleConfig = {
    tooltip: "Ej konfigurerad",
    icon_name: "warning",
    tools: [],
    base_component: "measure"
  };
  @Output() toggled = new EventEmitter<boolean>();

  @ViewChild("baseToolbar", { static: true }) baseToolbar;

  constructor(private stateService: StateService, private measureService: MeasureService) {
  }

  clear() {
    this.measureService.clearDrawings();
  }

  toggleGroup(toggle: boolean) {
    this.baseToolbar.toggleGroup(toggle);
  }
}
