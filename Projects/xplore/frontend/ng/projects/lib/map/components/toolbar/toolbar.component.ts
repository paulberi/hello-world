import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output
} from "@angular/core";
import {
  ActionMode,
  BackgroundAction,
  FeatureSelectionMode,
  MeasureMode,
  MeasureUnit,
  ResetPreviousSelectionOnMode,
  StateService
} from "../../services/state.service";
import {MeasureService} from "../../services/map-tools/measure.service";
import {DrawService} from "../../services/map-tools/draw.service";
import {ToolbarDirection} from "./sub-toolbars/base-toolbar.component";
import ExtractorUtil from "../../../util/extractor.util";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {ToolSettingsSheetComponent} from "./tool-settings-sheet.component";

export interface ToolbarConfig {
  allowDeselect?: boolean;
  showSettings?: boolean;
  tools: ToolbarItem[];
  buttons?: ToolbarButton[];
  measureUnit?: MeasureUnit;
}

export interface ToolbarButton {
  tooltip: string;
  tooltipPosition: string;
  icon_name: string;
  emitsEvent: string;
  visibleDevice?: boolean;
}

export interface ToolbarItem {
  tooltip: string;
  tooltipPosition?: string;
  icon_name?: string;
  svg_icon?: string;
  mode: {
    actionMode: ActionMode,
    featureSelectionMode?: FeatureSelectionMode,
    measureMode?: MeasureMode,
    resetPreviousSelectionOnMode?: ResetPreviousSelectionOnMode
  };
  backgroundMode?: {
    backgroundAction: BackgroundAction;
  };
  visibleDevice?: boolean;
}

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-toolbar",
  template: `
    <div class="toolbar">
      <mat-button-toggle-group #toggleGroup [multiple]="config.allowDeselect"
                               [value]="selectedMode" class="toggle-group"
                               (window:resize)="markForCheck()"
                               [ngClass]="direction">
        <button *ngIf="config.showSettings" mat-raised-button [matTooltip]="'Verktygsinställningar'"
                [matTooltipPosition]="'above'" (click)="openSettingsSheet()">
          <mat-icon class="mat-24">settings</mat-icon>
        </button>

        <ng-container *ngFor="let tool of config.tools; let i = index">
          <mat-button-toggle *ngIf="visibleOnDevice(tool)" [value]="i" [matTooltip]="tool.tooltip"
                             [matTooltipPosition]="tool.tooltipPosition ? tool.tooltipPosition : 'above'"
                             (change)="onToolChange($event)">
            <mat-icon *ngIf="tool.svg_icon" svgIcon="{{tool.svg_icon}}"></mat-icon>
            <mat-icon *ngIf="!tool.svg_icon">{{tool.icon_name}}</mat-icon>
          </mat-button-toggle>
        </ng-container>

        <ng-container *ngFor="let button of config.buttons; let i = index">
          <button *ngIf="visibleOnDevice(button)" mat-raised-button [matTooltip]="button.tooltip"
                  [matTooltipPosition]="button.tooltipPosition ? button.tooltipPosition : 'above'"
                  (click)="buttonClickedEvent.emit(button.emitsEvent)">
            <mat-icon class="mat-24">{{button.icon_name}}</mat-icon>
          </button>
        </ng-container>

        <ng-content></ng-content>

      </mat-button-toggle-group>
    </div>
  `,
  styles: [`
    .toggle-group {
      display: inline-flex;
    }

    .left {
      flex-direction: row-reverse;
    }

    .left > * {
      margin-right: 10px;
    }

    .up {
      flex-direction: column-reverse;
    }

    .up > * {
      margin-bottom: 10px;
    }

    .right {
      flex-direction: row;
    }

    .right > * {
      margin-left: 10px;
    }

    .down {
      flex-direction: column;
    }

    .down > * {
      margin-top: 10px;
    }
  `]
})
export class ToolbarComponent implements OnInit, OnDestroy {

  @Input() direction: ToolbarDirection = ToolbarDirection.right;
  @Input() switchToDeviceWidth = 660;

  @Input() config: ToolbarConfig = {
    allowDeselect: false,
    tools: [
      {
        tooltip: "Välj objekt",
        icon_name: "touch_app",
        mode: {
          actionMode: ActionMode.Select,
          featureSelectionMode: FeatureSelectionMode.Click,
          resetPreviousSelectionOnMode: ResetPreviousSelectionOnMode.Beginning
        }
      }
    ]
  };

  @Output() public buttonClickedEvent = new EventEmitter<string>();


  public selectedMode;

  private uistateSubscription;

  // measureService and drawService are injected but not used directly. The services listen
  // on the stateService for changes to the action/selection mode.
  constructor(private stateService: StateService, private changeDetectorRef: ChangeDetectorRef,
              private measureService: MeasureService, private drawService: DrawService, private settingsSheet: MatBottomSheet) {
  }

  ngOnInit() {
    this.uistateSubscription = this.stateService.uiStates.subscribe((uiStates) => {
      const mode = this.config.tools.find(tool => {
        if (tool.mode == null) {
          return null;
        }
        if (tool.mode.actionMode !== uiStates.actionMode) {
          return false;
        } else if (uiStates.actionMode === ActionMode.Select) {
          return tool.mode.featureSelectionMode === uiStates.featureSelectionMode;
        } else if (uiStates.actionMode === ActionMode.Measure) {
          return tool.mode.measureMode === uiStates.measureMode;
        }
      });
      const modeIndex = this.config.tools.indexOf(mode);
      this.selectedMode = mode ? (this.config.allowDeselect ? [modeIndex] : modeIndex) : null;
      this.changeDetectorRef.markForCheck();
    });
  }

  ngOnDestroy() {
    this.uistateSubscription.unsubscribe();
  }

  onToolChange(event) {
    if (this.config.allowDeselect) {
      const toggle = event.source;
      if (toggle && toggle.checked) {
        const group = toggle.buttonToggleGroup;
        if (event.value === toggle.value) {
          group.value = [toggle.value];
          if (this.config.tools[toggle.value].mode == null && this.config.tools[toggle.value].backgroundMode != null) {
            // Toggled a background action button, like My Location
            this.stateService.setUiStates({backgroundAction: this.config.tools[toggle.value].backgroundMode.backgroundAction});
          } else {
            this.stateService.setUiStates({...this.config.tools[toggle.value].mode});
          }
        }
      } else {
        if (this.config.tools[toggle.value].mode == null && this.config.tools[toggle.value].backgroundMode != null) {
          // We need to deactivate the background mode if toggled is false
          if (this.stateService.getUiStates().backgroundAction === this.config.tools[toggle.value].backgroundMode.backgroundAction) {
            this.stateService.setUiStates({backgroundAction: null});
          }
        } else {
          // If nothing is checked, default to the normal select
          this.stateService.setUiStates({
            ...{
              actionMode: ActionMode.Select,
              featureSelectionMode: FeatureSelectionMode.Click,
              resetPreviousSelectionOnMode: ResetPreviousSelectionOnMode.Beginning
            }
          });
        }
      }
    } else {
      this.stateService.setUiStates({...this.config.tools[event.value].mode});
    }
  }

  visibleOnDevice(item: ToolbarItem | ToolbarButton) {
    const visibleDevice = ExtractorUtil.extractBooleanValue('visibleDevice', item, true);

    return visibleDevice || window.innerWidth > this.switchToDeviceWidth;
  }

  markForCheck() {
    this.changeDetectorRef.markForCheck();
  }

  openSettingsSheet() {
    this.settingsSheet.open(ToolSettingsSheetComponent, {hasBackdrop: false});
  }
}
