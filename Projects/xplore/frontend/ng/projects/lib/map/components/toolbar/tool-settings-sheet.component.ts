import {
  ChangeDetectionStrategy,
  Component
} from "@angular/core";
import {MatBottomSheetRef} from "@angular/material/bottom-sheet";
import {ToolSettingsComponent} from "./tool-settings.component";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-tool-settings-sheet",
  template: `
    <button id="close-button" mat-icon-button (click)="close()">
      <mat-icon id="close-button-icon">remove_circle</mat-icon>
    </button>
    <xp-tool-settings></xp-tool-settings>
  `,
  styles: [`
    #close-button {
      position: absolute;
      pointer-events: all;
      float: right;
      top: 5px;
      right: 5px;
      z-index: 1;
      height: 16px !important;
      width: 16px !important;
      font-size: 16px !important;
    }

    #close-button-icon {
      position: absolute;
      top: -4px;
      left: -4px;
      min-height: 20px !important;
      min-width: 20px !important;
      font-size: 20px !important;
    }
  `]
})
export class ToolSettingsSheetComponent {
  constructor(private settingsSheetRef: MatBottomSheetRef<ToolSettingsComponent>) {
  }

  close() {
    this.settingsSheetRef.dismiss();
  }

}
