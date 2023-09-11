import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject, timer } from 'rxjs';
import { BuildDetailsService } from "./build-details.service";
import { BuildDetailsHttpService } from "./build-details-http.service";

@Injectable({ providedIn: 'root' })
export class UpToDateBuildService {

    private appVersionUpToDateSubject = new BehaviorSubject<boolean>(true);
    private appVersionAtStartup: string;

    constructor(buildDetailsService: BuildDetailsService, private buildDetailsHttpService: BuildDetailsHttpService) {
        this.appVersionAtStartup = buildDetailsService.buildDetails.appVersion;
        this.pollForAppVersion();
    }

    public get buildIsUpToDate(): Observable<boolean> {
        return this.appVersionUpToDateSubject;
    }

    private pollForAppVersion() {
        const pollInterval = 1000 * 60 * 5;

        timer(pollInterval, pollInterval).subscribe(() => {
          this.buildDetailsHttpService.getAppVersion()
            .subscribe(appVersion => {
              const newAppVersion = appVersion;
              if (this.appVersionAtStartup !== newAppVersion) {
                  this.appVersionUpToDateSubject.next(false);
              }
            })
        });
    }
}
