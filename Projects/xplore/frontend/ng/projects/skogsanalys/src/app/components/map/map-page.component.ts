import { Component, OnInit } from "@angular/core";
import {BackgroundAction} from "../../../../../lib/map/services/state.service";

@Component({
  selector: "xp-map-page",
  templateUrl: "./map-page.component.html",
  styleUrls: ["./map-page.component.scss"]
})
export class MapPageComponent implements OnInit {
  public navigationToolbarConfig = {
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

  constructor() { }

  ngOnInit(): void {
  }

}
