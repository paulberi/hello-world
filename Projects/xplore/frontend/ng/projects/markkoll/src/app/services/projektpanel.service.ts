import { Injectable } from "@angular/core";

@Injectable({
  providedIn: "root"
})
export class ProjektPanelService {
  constructor() {
    if(!localStorage.getItem("projektPanelStates")) {
      localStorage.setItem("projektPanelStates", JSON.stringify({}));
    }
  }

  isExpanded(panel: string) {
    const panelStates = JSON.parse(localStorage.getItem("projektPanelStates"));
    return panelStates[panel];
  }

  toggleExpanded(panel: string) {
    const panelStates = JSON.parse(localStorage.getItem("projektPanelStates"));
    const updatedStates = {
      ...panelStates,
      [panel]: panelStates[panel] ? false : true
    };
    localStorage.setItem("projektPanelStates", JSON.stringify(updatedStates));
  }
}
