import {ChangeDetectorRef, Component, Input, OnChanges} from "@angular/core";
import {ConfigProperty, ConfigService} from "../../../config/config.service";
import {ViewService} from "../../../map-core/view.service";
import {ToolbarDirection} from "./sub-toolbars/base-toolbar.component";
import {BackgroundAction, StateService} from "../../services/state.service";
import {MeasureService} from "../../services/map-tools/measure.service";
import {DrawService} from "../../services/map-tools/draw.service";
import {GeolocationStateService} from "../../services/geolocation-state.service";

@Component({
    selector: "xp-navigation-toolbar",
    template: `
        <xp-toolbar [config]="config" (buttonClickedEvent)="this.clickEvent($event)"
                    [direction]="direction" [switchToDeviceWidth]="switchToDeviceWidth">
          <ng-content></ng-content>
        </xp-toolbar>
    `,
    styles: [`

    `]
})
export class NavigationToolbarComponent implements OnChanges {
  defaultConfig = {
        allowDeselect: true,
        tools: [
            {
                tooltip: "Min position",
                tooltipPosition: "right",
                mode: null,
                backgroundMode: {
                    backgroundAction: BackgroundAction.Track
                },
                icon_name: "my_location"
            }
        ],
        buttons: [
            {
                tooltip: "Navigera till startvy",
                tooltipPosition: "right",
                icon_name: "home",
                emitsEvent: "go_to_home"
            },
            {
                tooltip: "Zooma ut",
                tooltipPosition: "right",
                icon_name: "remove",
                emitsEvent: "zoom_out",
                visibleDevice: false
            },
            {
                tooltip: "Zooma in",
                tooltipPosition: "right",
                icon_name: "add",
                emitsEvent: "zoom_in",
                visibleDevice: false
            }
        ]
    };

    @Input() switchToDeviceWidth = 660;
    @Input() config = this.defaultConfig;
    @Input() direction: ToolbarDirection = ToolbarDirection.up;

    constructor(private navStateService: StateService,
                private navChangeDetectorRef: ChangeDetectorRef,
                private navMeasureService: MeasureService,
                private navDrawService: DrawService,
                private configService: ConfigService,
                private viewService: ViewService,
                private geolocationStateService: GeolocationStateService) {
    }

    ngOnChanges(changes) {
      if (changes.config) {
        if (changes.config.firstChange) {
          if (!changes.config.currentValue) {
            this.config = this.defaultConfig;
            // Use a defined default value if the passed parameter is undefined.
            // The @input default value (i.e @Input() config = {...}) is only usable if nothing is passed to the input,
            // so if an undefined value was passed (say, if it wasnt defined in the config file so it was null) we
            // wouldn't be able to use the default since which would be overwritten.
          }
        }
      }
    }

    clickEvent(event) {
        switch (event) {
            case "go_to_home":
                this.viewService.zoomToStart();
                break;
            case "zoom_in":
                this.viewService.smoothZoom(1);
                break;
            case "zoom_out":
                this.viewService.smoothZoom(-1);
                break;
        }
    }
}
