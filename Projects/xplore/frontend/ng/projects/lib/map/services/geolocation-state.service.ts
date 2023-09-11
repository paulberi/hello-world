import {Injectable} from "@angular/core";
import {BackgroundAction, StateService} from "./state.service";
import {GeolocationService} from "../../map-core/geolocation.service";

@Injectable({
    providedIn: "root"
})
export class GeolocationStateService {

    constructor(geolocationService: GeolocationService, stateService: StateService) {
        stateService.uiStates.subscribe((uiStates) => {
            if (uiStates.backgroundAction === BackgroundAction.Track) {
                if (!geolocationService.isTracking()) {
                    geolocationService.startTrackMyLocation();
                }
            } else {
                geolocationService.stopTrackMyLocation();
            }
        });
    }
}
