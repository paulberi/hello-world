import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input, OnDestroy,
  OnInit,
  Output,
  ViewChild
} from "@angular/core";
import {ActionMode, FeatureSelectionMode, StateService} from "../../../services/state.service";
import {ToolbarCollapsibleConfig} from "../toolbar-collapsible.component";
import {ConfigService} from "../../../../config/config.service";

export interface ToolbarCollapsible {
  toggleGroup: (boolean) => void;
}

export enum ToolbarDirection {
  left = "left",
  up = "up",
  right = "right",
  down = "down"
}

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-base-toolbar",
  template: `
    <div class="toolbar">
      <ng-container *ngIf="config?.tools?.length > 1; else single">
        <mat-button-toggle-group multiple>
          <mat-button-toggle #toggle [matTooltip]="config.tooltip" matTooltipPosition="above"
                            (change)="onToggleChange($event)" [ngClass]="{'active': selectedMode !== null}">
            <mat-icon *ngIf="selectedIcon.svg_icon" svgIcon="{{selectedIcon.svg_icon}}"></mat-icon>
            <mat-icon *ngIf="!selectedIcon.svg_icon">{{selectedIcon.icon_name}}</mat-icon>
          </mat-button-toggle>
        </mat-button-toggle-group>
        <div class="toolbar-content" [style.display]="toggle.checked ? 'flex' : 'none'" [ngClass]="direction">
          <mat-button-toggle-group multiple (change)="onToolChange($event)"
                                   [value]="[selectedMode]" [vertical]="isVertical()">
            <mat-button-toggle *ngFor="let mode of config.tools; let i = index" [value]="i" [matTooltip]="mode.tooltip"
                               [matTooltipPosition]="'above'" class="mat-elevation-z1">
              <mat-icon *ngIf="mode.svg_icon" svgIcon="{{mode.svg_icon}}"></mat-icon>
              <mat-icon *ngIf="!mode.svg_icon">{{mode.icon_name}}</mat-icon>
            </mat-button-toggle>
          </mat-button-toggle-group>

          <ng-content></ng-content>
        </div>
      </ng-container>

      <ng-template #single>
        <ng-container *ngIf="config?.tools?.length === 1">
          <mat-button-toggle-group multiple (change)="onToolChange($event)"
                                     [value]="[selectedMode]" [vertical]="isVertical()">
            <mat-button-toggle [matTooltip]="config.tools[0].tooltip" [matTooltipPosition]="'above'"
                               [value]="0" (change)="onToggleChange($event)">
              <mat-icon *ngIf="config.tools[0].svg_icon" svgIcon="{{config.tools[0].svg_icon}}"></mat-icon>
              <mat-icon *ngIf="!config.tools[0].svg_icon">{{config.tools[0].icon_name}}</mat-icon>
            </mat-button-toggle>
          </mat-button-toggle-group>
        </ng-container>
      </ng-template>
    </div>
  `,
  styles: [`
    .toolbar {
      position: relative;
      display: flex;
      flex-wrap: nowrap;
      background-color: rgba(0, 0, 0, 0) !important;
    }

    .toolbar-content {
      position: absolute;
      overflow: visible;
      padding: 5px;
    }

    .toolbar-content mat-button-toggle-group {
      box-shadow: none;
      padding: 0;
    }

    .left {
      right: 56px;
    }

    .left, .left ::ng-deep mat-button-toggle-group {
      flex-direction: row-reverse;
    }

    .left ::ng-deep mat-button-toggle {
      margin-right: 10px;
    }

    .up {
      bottom: 40px;
    }

    .up, .up ::ng-deep mat-button-toggle-group {
      flex-direction: column-reverse;
    }

    .up mat-button-toggle {
      margin-bottom: 10px;
    }

    .right {
      left: 56px;
    }

    .right, .right ::ng-deep mat-button-toggle-group {
      flex-direction: row;
    }

    .right ::ng-deep mat-button-toggle {
      margin-left: 10px;
    }

    .down {
      top: 36px;
    }

    .down, .down ::ng-deep mat-button-toggle-group {
      flex-direction: column;
    }

    .down ::ng-deep mat-button-toggle {
      margin-top: 10px;
    }

  `]
})
export class BaseToolbarComponent implements OnInit, OnDestroy, ToolbarCollapsible {

  @Input() direction: ToolbarDirection = ToolbarDirection.up;
  @Input() config: ToolbarCollapsibleConfig = {
    tooltip: "Ej konfigurerad",
    icon_name: "warning",
    tools: [],
    base_component: "base"
  };
  @Output() toggled = new EventEmitter<boolean>();

  @ViewChild("toggle") toggleGroupButton;

  public selectedMode;
  public selectedIcon = {icon_name: this.config.icon_name, svg_icon: this.config.svg_icon};

  private uistateSubscription;

  constructor(private stateService: StateService, private changeDetectorRef: ChangeDetectorRef) {
  }

  ngOnInit() {
    this.uistateSubscription = this.stateService.uiStates.subscribe((uiStates) => {
      const mode = this.config.tools.find(tool => {
        if (tool.mode.actionMode !== uiStates.actionMode) {
          return false;
        } else if (uiStates.actionMode === ActionMode.Select) {
          return tool.mode.featureSelectionMode === uiStates.featureSelectionMode;
        } else if (uiStates.actionMode === ActionMode.Measure) {
          return tool.mode.measureMode === uiStates.measureMode;
        }
      });
      this.selectedMode = mode ? this.config.tools.indexOf(mode) : null;
      this.selectedIcon = mode ? {icon_name: mode.icon_name, svg_icon: mode.svg_icon}
                               : {icon_name: this.config.icon_name, svg_icon: this.config.svg_icon};
      this.changeDetectorRef.markForCheck();
    });
  }

  ngOnDestroy() {
    this.uistateSubscription.unsubscribe();
  }

  onToolChange(event) {
    const toggle = event.source;
    if (toggle) {
      const group = toggle.buttonToggleGroup;
      if (event.value.some(item => item === toggle.value)) {
        group.value = [toggle.value];
        this.stateService.setUiStates({...this.config.tools[toggle.value].mode});
      }
    } else {
      // If nothing is checked, default to the normal select
      this.stateService.setUiStates(
        {...{actionMode: ActionMode.Select, featureSelectionMode: FeatureSelectionMode.Click}}
      );
    }
    this.toggled.emit(false);
  }

  onToggleChange(e) {
    this.toggled.emit(e.source._checked);
  }

  toggleGroup(toggle: boolean) {
    if (this.config.tools.length > 1) {
      this.toggleGroupButton.checked = toggle;
    }
  }

  isVertical() {
    return this.direction === ToolbarDirection.up || this.direction === ToolbarDirection.down;
  }
}
