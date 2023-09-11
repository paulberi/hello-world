import { Injectable } from "@angular/core";

export class BuildDetails {
  appVersion: string;
}

@Injectable({ providedIn: 'root' })
export class BuildDetailsService {
    public buildDetails: BuildDetails;

    constructor() {
        this.buildDetails = new BuildDetails();
    }
}
